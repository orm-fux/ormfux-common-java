package org.blue.bunny.ledger.customlibs.db.query;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import org.apache.commons.lang3.StringUtils;
import org.blue.bunny.ledger.customlibs.db.annotation.CollectionOfEntities;
import org.blue.bunny.ledger.customlibs.db.annotation.Column;
import org.blue.bunny.ledger.customlibs.db.annotation.Entity;
import org.blue.bunny.ledger.customlibs.db.annotation.Id;
import org.blue.bunny.ledger.customlibs.db.annotation.Version;
import org.blue.bunny.ledger.customlibs.db.exception.NonUniqueResultException;
import org.blue.bunny.ledger.customlibs.db.exception.SQLException;
import org.blue.bunny.ledger.customlibs.db.exception.StaleEntityException;
import org.blue.bunny.ledger.customlibs.db.generators.LongIncrementGenerator;
import org.blue.bunny.ledger.customlibs.db.generators.NoValueGenerator;
import org.blue.bunny.ledger.customlibs.db.generators.ValueGenerator;
import org.blue.bunny.ledger.customlibs.db.query.QueryResult.QueryResultRow;
import org.blue.bunny.ledger.customlibs.utils.reflection.ClassUtils;
import org.blue.bunny.ledger.customlibs.utils.reflection.PropertyUtils;

/**
 * Query returning the results as instances of entities.
 *
 * @param <T> The entity type.
 */
public class TypedQuery<T> extends AbstractQuery {
    
    /**
     * The type that will be returned and updated by this query.
     */
    private final Class<T> entityType;
    
    /**
     * The alias for the table to use in the automatically generated query parts.
     */
    private final String entityAlias;
    
    /**
     * @param dbConnection URL to the database to use.
     * @param querySuffix The suffix (joins, where conditions, sort, etc.) for the query.
     * @param resultType The entity type.
     */
    public TypedQuery(final String dbConnection, final String querySuffix, final Class<T> resultType) {
       this(dbConnection, querySuffix, resultType, null);
    }
    
    /**
     * @param dbConnection URL to the database to use.
     * @param querySuffix The suffix (joins, where conditions, sort, etc.) for the query.
     * @param resultType The entity type.
     * @param entityAlias The alias for the table to use in the automatically generated query parts.
     */
    public TypedQuery(final String dbConnection, final String querySuffix, final Class<T> resultType, final String entityAlias) {
        super(dbConnection, querySuffix);
        
        if (!resultType.isAnnotationPresent(Entity.class)) {
            throw new IllegalArgumentException("The result type must have an @Entity annotation.");
        }
        
        this.entityType = resultType;
        this.entityAlias = entityAlias;
    }
    
    /**
     * Updates/creates the entity in the database.
     * 
     * @param entity The entity to update in the database.
     * @return The entity id.
     * 
     * @throws SQLException 
     */
    public Object update(final T entity) throws SQLException {
        final Field idField = getIdField();
        final Object entityId = PropertyUtils.read(entity, idField.getName());
        
        if (entityId != null) {
            //entity is already persisted. db version check
            checkVersion(entityType, entity);
            doUpdate(entity);
            
            return entityId;
            
        } else {
            return doCreate(entity);
        }
    }
    
    /**
     * Updates an existing entity in the database.
     * 
     * @param entity The entity to update.
     *  
     * @throws SQLException
     */
    private void doUpdate(final T entity) throws SQLException {
        final List<Field> simpleFields = getMappedSimpleFields();
        
        //increment version
        final Field versionField = getVersionField();
        Object entityVersion = PropertyUtils.read(entity, versionField.getName());
        
        entityVersion = new LongIncrementGenerator().generate(entityVersion);
        PropertyUtils.write(entity, versionField.getName(), entityVersion);
        
        //set automatic valued fields
        for (final Field simpleField : simpleFields) {
            final Column colDef = simpleField.getAnnotation(Column.class);
            
            if (!colDef.generator().isAssignableFrom(NoValueGenerator.class)) {
                final ValueGenerator<?> valueGenerator = ClassUtils.createObject(colDef.generator());
                final Object previousValue = PropertyUtils.read(entity, simpleField.getName());
                PropertyUtils.write(entity, simpleField.getName(), valueGenerator.generate(previousValue));
            }
            
        }
        
        //build update query. first main entity then collections
        final String tableName = getTableName();
        final Field idField = getIdField();
        final Column idColumn = idField.getAnnotation(Column.class);
        
        final StringBuilder queryString = new StringBuilder();
        
        //main update query
        final StringJoiner updateQuery = new StringJoiner(", ", 
                                                          " update " + tableName + " set ", 
                                                          " where " + tableName + '.' + idColumn.columnName() + " = :id; ");
        final Map<String, Object> queryParams = new HashMap<>();
        
        for (final Field simpleField : simpleFields) {
            if (!simpleField.isAnnotationPresent(Id.class)) {
                final Class<?> fieldType = simpleField.getType();
                final String fieldName = simpleField.getName();
                
                Object updateValue = PropertyUtils.read(entity, fieldName);
                
                if (updateValue == null) {
                    //no action necessary
                    
                } else if (fieldType.isEnum()) {
                    //enum fields
                    updateValue = ((Enum<?>) updateValue).name();
                    
                } else if (fieldType.isAnnotationPresent(Entity.class)) {
                    //field is another entity. load it
                    final Field nestedEntityIdField = getIdField(fieldType);
                    updateValue = PropertyUtils.read(updateValue, nestedEntityIdField.getName());
                    
                } else {
                    //field is a "simple type". no action necessary.
                }
                
                final Column colDef = simpleField.getAnnotation(Column.class);
                final String columnName = colDef.columnName();
                
                updateQuery.add(tableName + '.' + columnName + " = :" + columnName);
                queryParams.put(columnName, updateValue);
                
            }
            
        }
        
        queryString.append(updateQuery);
        
        //collections
        queryString.append(createClearCollectionsQuery());
        
        final Query updateCollectionsVersionQuery = createCollectionEntityVersionUpdateQuery(entity);
        queryString.append(updateCollectionsVersionQuery.getQueryString());
        queryParams.putAll(updateCollectionsVersionQuery.getQueryParams());
        
        final Query insertCollectionsQuery = createInsertCollectionsQuery(entity);
        queryString.append(insertCollectionsQuery.getQueryString());
        queryParams.putAll(insertCollectionsQuery.getQueryParams());
        
        final Query query = new Query(getDbConnection(), queryString.toString());
        query.addParameter("id", PropertyUtils.read(entity, idField.getName()));
        query.addParameters(queryParams);
        
        if (query.executeUpdate() < 1) {
            throw new SQLException("Nothing was updated.");
        }
        
    }
    
    /**
     * Persists a transient entity.
     *
     * @param entity The entity to create in the database.
     * @return The entity id.
     * 
     * @throws SQLException 
     */
    private Object doCreate(final T entity) throws SQLException {
        final List<Field> simpleFields = getMappedSimpleFields();
        
        //create new entity id
        final Field idField = getIdField();
        final Object entityId =  ClassUtils.createObject(idField.getAnnotation(Id.class).value()).generateId();
        PropertyUtils.write(entity, idField.getName(), entityId);
        
        //set automatic valued fields
        for (final Field simpleField : simpleFields) {
            final Column colDef = simpleField.getAnnotation(Column.class);
            
            if (!colDef.generator().isAssignableFrom(NoValueGenerator.class)) {
                final ValueGenerator<?> valueGenerator = ClassUtils.createObject(colDef.generator());
                final Object previousValue = PropertyUtils.read(entity, simpleField.getName());
                PropertyUtils.write(entity, simpleField.getName(), valueGenerator.generate(previousValue));
            }
            
        }
        
        final Map<String, Object> queryParams = new HashMap<>();
        final StringBuilder queryString = new StringBuilder(); 
        final String tableName = getTableName();
        
        final StringJoiner insertColumns = new StringJoiner(", ", "(", ")");
        final StringJoiner valuesDef = new StringJoiner(", ", "(", ")");
        
        for (final Field simpleField : simpleFields) {
            final Class<?> fieldType = simpleField.getType();
            final String fieldName = simpleField.getName();
            
            Object updateValue = PropertyUtils.read(entity, fieldName);
            
            if (updateValue != null) {
                if (fieldType.isEnum()) {
                    //enum fields
                    updateValue = ((Enum<?>) updateValue).name();
                    
                } else if (fieldType.isAnnotationPresent(Entity.class)) {
                    //field is another entity. load it
                    final Field nestedEntityIdField = getIdField(fieldType);
                    updateValue = PropertyUtils.read(updateValue, nestedEntityIdField.getName());
                    
                } else {
                    //field is a "simple type". no action necessary.
                }
                
                final Column colDef = simpleField.getAnnotation(Column.class);
                final String columnName = colDef.columnName();
                
                insertColumns.add(columnName);
                valuesDef.add(":" + columnName);
                queryParams.put(columnName, updateValue);
            }
            
        }
        
        queryString.append("insert into ").append(tableName)
                   .append(insertColumns)
                   .append(" values ").append(valuesDef)
                   .append("; ");
        
        final Query updateCollectionsVersionQuery = createCollectionEntityVersionUpdateQuery(entity);
        queryString.append(updateCollectionsVersionQuery.getQueryString());
        queryParams.putAll(updateCollectionsVersionQuery.getQueryParams());
        
        final Query insertCollectionsQuery = createInsertCollectionsQuery(entity);
        queryString.append(insertCollectionsQuery.getQueryString());
        queryParams.putAll(insertCollectionsQuery.getQueryParams());
        
        final Query query = new Query(getDbConnection(), queryString.toString());
        query.addParameters(queryParams);
        
        if (query.executeUpdate() < 1) {
            throw new SQLException("Nothing was inserted.");
        }
        
        return entityId;
        
    }
    
    /**
     * Creates an update query to increment the version number of entities referenced in another
     * entity's collection.
     * 
     * @param entity The entity holding the collections.
     * @return The query.
     */
    @SuppressWarnings("unchecked")
    private Query createCollectionEntityVersionUpdateQuery(final T entity) {
        final List<Field> collectionFields = getMappedCollectionFields();
        
        final StringBuilder insertCollectionsQuery = new StringBuilder();
        final Map<String, Object> paramValues = new HashMap<>();
        int paramIdx = 0;
        
        for (final Field collectionField : collectionFields) {
            final List<Object> collection = (List<Object>) PropertyUtils.read(entity, collectionField.getName());
            
            if (collection != null && !collection.isEmpty()) {
                final Class<?> collEntityType = getCollectionEntityType(collectionField);
                final Field collEntityIdField = getIdField(collEntityType);
                final Field versionField = getVersionField(collEntityType);
                final String joinTableName = getJoinTableName(collectionField);
                
                if (StringUtils.isBlank(joinTableName)) {
                    //collection is mapped with simple join column
                    final String collEntityTable = getTableName(collEntityType);
                    
                    final String baseUpdate = "update " + collEntityTable 
                                                + " set " + collEntityTable + '.' + versionField.getAnnotation(Column.class).columnName() + " = :";
                    
                    for (final Object collEntity : collection) {
                        final String verIdParamName = "refVerIdPar" + paramIdx;
                        final String verParamName = "refVerPar" + (paramIdx++);
                        
                        insertCollectionsQuery.append(baseUpdate)
                                              .append(verParamName)
                                              .append(" where " + collEntityTable +  '.' + getIdField(collEntityType).getAnnotation(Column.class).columnName() + " = :")
                                              .append(verIdParamName).append("; ");
                        
                        paramValues.put(verIdParamName, PropertyUtils.read(collEntity, collEntityIdField.getName()));
                        paramValues.put(verParamName, new LongIncrementGenerator().generate(PropertyUtils.read(collEntity, versionField.getName())));
                    }
                }
            }
        }
        
        final Query query = new Query(getDbConnection(), insertCollectionsQuery.toString());
        query.addParameters(paramValues);
        
        return query; 
    }
    
    /**
     * Deletes the entity.
     * 
     * @param entity The entity to delete.
     * @return the number of rows removed from the database.
     * 
     * @throws SQLException 
     */
    public int delete(final T entity) throws SQLException {
        //Creating a batch update query, which removes collection content first.
        //This way, when we fail at one part, we commit nothing.
        final StringBuilder deleteQuery = new StringBuilder();
        
        deleteQuery.append(createClearCollectionsQuery());
        
        final Field idField = getIdField();
        final Column columnDef = idField.getAnnotation(Column.class);
        
        deleteQuery.append("delete from " + getTableName() + " where " + getTableName() + '.' + columnDef.columnName() + " = :id; ");
        
        final Query collectionVersionQuery = createCollectionEntityVersionUpdateQuery(entity);
        
        final Query query = new Query(getDbConnection(), collectionVersionQuery.getQueryString() + deleteQuery.toString());
        query.addParameters(collectionVersionQuery.getQueryParams());
        query.addParameter("id", PropertyUtils.read(entity, idField.getName()));
        
        return query.executeUpdate();
    }
    
    /**
     * Creates the query String to clear the values of a collection of entities.
     *
     * @return The query string for the collection update.
     */
    private String createClearCollectionsQuery() {
        final List<Field> collectionFields = getMappedCollectionFields();
        final StringBuilder clearCollectionsQuery = new StringBuilder();
        
        for (final Field collectionField : collectionFields) {
            final CollectionOfEntities collDef = collectionField.getAnnotation(CollectionOfEntities.class);
            final String joinTableName = getJoinTableName(collectionField);
            
            if (!StringUtils.isBlank(joinTableName)) {
                clearCollectionsQuery.append("delete from " + joinTableName + " where  " + joinTableName + '.' + collDef.joinColumn()+ " = :id; ");
                
            } else {
                final Class<?> collEntityType = getCollectionEntityType(collectionField);
                final String collEntityTable = getTableName(collEntityType);
                
                clearCollectionsQuery.append("update " + collEntityTable + " set " + collEntityTable + '.' + collDef.joinColumn() +  " = null ")
                                     .append("where " + collEntityTable + '.' + collDef.joinColumn() + " = :id; ");
                
            }
        }
        
        return clearCollectionsQuery.toString();
    }
    
    /**
     * Creates the query for persisting the relation between an entity and the collections of entities in it.
     *
     * @param entity The entity with the entity collections.
     * @return The query.
     */
    @SuppressWarnings("unchecked")
    private Query createInsertCollectionsQuery(final T entity) {
        final List<Field> collectionFields = getMappedCollectionFields();
        
        final StringBuilder insertCollectionsQuery = new StringBuilder();
        final Map<String, Object> paramValues = new HashMap<>();
        int paramIdx = 0;
        
        for (final Field collectionField : collectionFields) {
            final List<Object> collection = (List<Object>) PropertyUtils.read(entity, collectionField.getName());
            
            if (collection != null && !collection.isEmpty()) {
                final CollectionOfEntities collDef = collectionField.getAnnotation(CollectionOfEntities.class);
                final Class<?> collEntityType = getCollectionEntityType(collectionField);
                final Field collEntityIdField = getIdField(collEntityType);
                final String joinTableName = getJoinTableName(collectionField);
                
                if (!StringUtils.isBlank(joinTableName)) {
                    //collection is mapped with join table
                    final StringJoiner collectionInsert = new StringJoiner(", ", 
                                                                           "insert into " + joinTableName + 
                                                                           '(' + collDef.joinColumn() + ',' + collDef.inverseJoinColumn() 
                                                                           + ") values ", 
                                                                           ";");
                    
                    for (final Object collEntity : collection) {
                        final String paramName = "joinInsertPar" + (paramIdx++);
                        collectionInsert.add("(:id, :" + paramName + ')');
                        paramValues.put(paramName, PropertyUtils.read(collEntity, collEntityIdField.getName()));
                    }
                    
                    insertCollectionsQuery.append(collectionInsert);
                    
                } else {
                    //collection is mapped with simple join column
                    final String collEntityTable = getTableName(collEntityType);
                    
                    final String baseUpdate = "update " + collEntityTable 
                                                + " set " + collEntityTable + '.' + collDef.joinColumn() + " = :id "
                                                + "where " + collEntityTable +  '.' + getIdField(collEntityType).getAnnotation(Column.class).columnName() + " = :";
                    
                    for (final Object collEntity : collection) {
                        checkVersion(collEntityType, collEntity);
                        
                        final String paramName = "refUpdatePar" + (paramIdx++);
                        insertCollectionsQuery.append(baseUpdate).append(paramName).append("; ");
                        paramValues.put(paramName, PropertyUtils.read(collEntity, collEntityIdField.getName()));
                    }
                }
            }
        }
        
        final Query query = new Query(getDbConnection(), insertCollectionsQuery.toString());
        query.addParameters(paramValues);
        
        return query; 
    }
    
    /**
     * Executes the query returning a single entity. Throws an error when more than 
     * one entity is found.
     * 
     * @return The found entity.
     * 
     * @throws SQLException  
     */
    public T getSingleResult() throws SQLException {
        return getSingleResult(new HashMap<>());
    }
    
    /**
     * Executes the query returning a single entity. Throws an error when more than 
     * one entity is found.
     * 
     * @param loadedEntities The already loaded entities.
     * @return The entity fulfilling the query.
     * 
     * @throws SQLException  
     */
    private T getSingleResult(final Map<String, Object> loadedEntities) throws SQLException {
        final List<T> results = getResultList(loadedEntities);
        
        if (results.isEmpty()) {
            return null;
        } else if (results.size() > 1) {
            throw new NonUniqueResultException("The query returned more than one result.");
        } else {
            return results.get(0);
        }
        
    }
    
    /**
     * Executes the query returning a list of entities.
     * 
     * @return The entities fulfilling the query.
     * @throws SQLException
     */
    public List<T> getResultList() throws SQLException {
        return getResultList(new HashMap<>());
    }
    
    /**
     * Executes the query returning a list of entities.
     * 
     * @param loadedEntities The already loaded entities.
     * @return The entities fulfilling the query.
     * 
     * @throws SQLException
     */
    private List<T> getResultList(final Map<String, Object> loadedEntities) throws SQLException {
        final String queryString;
        
        if (getQueryString() != null && !getQueryString().isEmpty()) {
            queryString = buildSelectAll() + getQueryString();
        } else {
            queryString = buildSelectAll().toString();
        }
        
        //use classic query
        final Query query = new Query(getDbConnection(), queryString);
        query.addParameters(getQueryParams());
        
        final QueryResult queryResults = query.getResultList();
        
        //map query result to entities.
        final List<T> resultList = new ArrayList<>(queryResults.size());
        
        for (final QueryResultRow row : queryResults) {
            final T entity = createEntityInstance(row, loadedEntities);
            resultList.add(entity);
        }
        
        return resultList;
    }
    
    /**
     * Creates a new entity instance from the row data.
     * 
     * @param row The row data.
     * @param loadedEntities The already loaded entities.
     * @return The entity instance.
     * 
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
    private T createEntityInstance(final QueryResultRow row, 
                                   final Map<String, Object> loadedEntities) throws SQLException {
        try {
            //use existing instance when already loaded.
            final Field idField = getIdField();
            idField.setAccessible(true);
            final Object entityId = row.getValue(idField.getAnnotation(Column.class).columnLabel());
            
            if (loadedEntities.containsKey(entityType.getName() + ':' + entityId)) {
                return (T) loadedEntities.get(entityType.getName() + ':' + entityId);
            }
            
            //actually create the instance.
            final List<Field> simpleFields = getMappedSimpleFields();
            final List<Field> collectionFields = getMappedCollectionFields();
            
            //put in loaded entities before doing anything else to handle circular references
            final T entity = ClassUtils.createObject(entityType);
            idField.set(entity, entityId);
            loadedEntities.put(entityType.getName() + ':' + entityId, entity);  
            
            //simple fields
            for (final Field simpleField : simpleFields) {
                final Class<?> fieldType = simpleField.getType();
                final String columnLabel = simpleField.getAnnotation(Column.class).columnLabel();
                simpleField.setAccessible(true);
                
                final Object rawValue = row.getValue(columnLabel);
                
                if (fieldType.isEnum()) {
                    //enum fields
                    PropertyUtils.write(entity, simpleField.getName(), convertToEnumValue(rawValue, fieldType));
                    
                } else if (fieldType.isAnnotationPresent(Entity.class) && rawValue != null) {
                    //field is another entity. load it
                    Object referencedEntity = load(fieldType, rawValue, loadedEntities);
                    
                    if (referencedEntity == null) {
                        throw new SQLException("Entity not found: " + entityType + ':' + entityId);
                    }
                    
                    PropertyUtils.write(entity, simpleField.getName(), referencedEntity);
                    
                } else {
                    //field is a "simple type" or null-entity-reference. just assign
                    //TODO should we make an effort to map value to the correct type?
                    PropertyUtils.write(entity, simpleField.getName(), rawValue);
                }
                
            }
            
            //collection fields
            for (final Field collectionField : collectionFields) {
                final Class<?> collectionEntityType = getCollectionEntityType(collectionField);
                
                final CollectionOfEntities collDef = collectionField.getAnnotation(CollectionOfEntities.class);
                final String collectionQuerySuffix;
                final String collEntityTable = getTableName(collectionEntityType);
                final String joinTableName = getJoinTableName(collectionField);
                
                if (!StringUtils.isBlank(joinTableName)) {
                    final Field collIdField = getIdField(collectionEntityType);
                    
                    collectionQuerySuffix = "join " + joinTableName + ' ' + joinTableName 
                                            + " on " + joinTableName + '.' + collDef.inverseJoinColumn()
                                            + " = " + collEntityTable + '.' + collIdField.getAnnotation(Column.class).columnName()
                                            + " where " + joinTableName + '.' + collDef.joinColumn() + " = :id ";
                } else {
                    collectionQuerySuffix = " where " + collEntityTable + '.' + collDef.joinColumn() + " = :id ";
                }
                
                final TypedQuery<?> collectionQuery = new TypedQuery<>(getDbConnection(), collectionQuerySuffix, collectionEntityType);
                collectionQuery.addParameter("id", entityId);
                
                final List<?> collectionEntities = collectionQuery.getResultList(loadedEntities);
                
                PropertyUtils.write(entity, collectionField.getName(), collectionEntities);
            }
            
            return entity;
            
        } catch (final IllegalAccessException e) {
            throw new SQLException("Cannot create new entity instance.", e);
        }
    }
    
    /**
     * Determines the name of the join table for the field.
     */
    private String getJoinTableName(final Field collectionField) {
        final CollectionOfEntities collDef = collectionField.getAnnotation(CollectionOfEntities.class);
        final String joinTableName = collDef.joinTable();
        
        if (!StringUtils.isBlank(joinTableName)) {
            return joinTableName;
            
        } else if (!StringUtils.isBlank(collDef.inverseJoinColumn())) {
            return StringUtils.lowerCase(getTableName() // collectionField.getDeclaringClass().getSimpleName() 
                                            + '_'
                                            + getCollectionEntityType(collectionField).getSimpleName());
        } else {
            return null;
        }
        
    }
    
    /**
     * The type of entity in entity collections.
     * 
     * @param collectionField The collection field.
     * @return The entity type.
     */
    private Class<?> getCollectionEntityType(final Field collectionField) {
        final ParameterizedType collectionParamType = (ParameterizedType) collectionField.getGenericType();
        final Class<?> collectionEntityType = (Class<?>) collectionParamType.getActualTypeArguments()[0];
        
        return collectionEntityType;
    }
    
    /**
     * Loads the entity with the given id.
     * 
     * @param entityId The entity id.
     * @return The entity; {@code null} when it does not exist.
     * 
     * @throws SQLException
     */
    public T load(final Object entityId) throws SQLException {
        return load(entityId, new HashMap<>());
    }
    
    /**
     * Loads the entity with the given id.
     * 
     * @param entityId The entity id.
     * @param loadedEntities The already loaded entities.
     * @return The entity; {@code null} when it does not exist.
     * 
     * @throws SQLException
     */
    private T load(final Object entityId, final Map<String, Object> loadedEntities) throws SQLException {
        final Field idField = getIdField();
        final Column columnDef = idField.getAnnotation(Column.class);
        
        final String querySuffix = "where " + getTableName() + '.' + columnDef.columnName() + " = :id";
        
        final TypedQuery<T> loadQuery = new TypedQuery<>(getDbConnection(), querySuffix, entityType);
        loadQuery.addParameter("id", entityId);
        
        return loadQuery.getSingleResult(loadedEntities);
    }
    
    /**
     * Loads the entity with the given id.
     * 
     * @param entityType The type of entity to load.
     * @param entityId The entity id.
     * @param loadedEntities The already loaded entities.
     * @return The entity; {@code null} when it does not exist.
     * 
     * @throws SQLException
     */
    private Object load(final Class<?> entityType, final Object entityId, final Map<String, Object> loadedEntities) throws SQLException {
        Object loadedEntity = loadedEntities.get(entityType.getName() + ':' + entityId);
        
        if (loadedEntity == null) {
            final TypedQuery<?> query = new TypedQuery<>(getDbConnection(), null, entityType);
            loadedEntity = query.load(entityId, loadedEntities);
        }
        
        return loadedEntity;
    }
    
    /**
     * Converts the object value to an enum value.
     * 
     * @param value The value to convert.
     * @param enumType The enum type.
     * @return The converted value.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Enum convertToEnumValue(final Object value, final Class<?> enumType) {
        if (value == null) {
            return null;
        } else {
            return Enum.valueOf((Class<Enum>) enumType, value.toString());
        }
    }
    
    /**
     * Evaluates the entity type annotations and builds a "select all" query from them.
     * The query does not include collection property values.
     * 
     * @return The query string.
     */
    private StringBuilder buildSelectAll() {
        final List<Field> mappedFields = getMappedSimpleFields();
        final String tableName = getTableName();
        final String alias = StringUtils.isBlank(entityAlias) ? tableName : entityAlias;
        
        final StringJoiner select = new StringJoiner(", ", "select distinct ", " ");
        
        for (final Field mappedField : mappedFields) {
            final Column columnDef = mappedField.getAnnotation(Column.class);
            select.add(alias + '.' + columnDef.columnName() + " as " + columnDef.columnLabel());
        }
        
        final StringBuilder query = new StringBuilder();
        
        query.append(select)
             .append("from ")
             .append(tableName)
             .append(' ')
             .append(alias)
             .append(' ');
        
        return query;
    }
    
    /**
     * Checks that there is different version of the entity in the database.
     * 
     * @param entityType The type of the entity.
     * @param entity The entity to check.
     *
     * @throws StaleEntityException When the version of the entity does not match the version in the database.
     */
    private void checkVersion(final Class<?> entityType, final Object entity) {
        final String tableName = getTableName(entityType);
        final Field versionField = getVersionField(entityType);
        final Column versionColumn = versionField.getAnnotation(Column.class);
        
        final Field idField = getIdField(entityType);
        final Column idColumn = idField.getAnnotation(Column.class);
        
        final Object entityId = PropertyUtils.read(entity, idField.getName());
        
        final String versionQuery = "select " + tableName + '.' + versionColumn.columnName() 
                                        + " from " + tableName
                                        + " where " + tableName + '.' + idColumn.columnName() + " = :id"
                                        + " and " + tableName + '.' + versionColumn.columnName() + " = :version";
        
        final Query query = new Query(getDbConnection(), versionQuery);
        query.addParameter("id", entityId);
        query.addParameter("version", PropertyUtils.read(entity, versionField.getName()));
        
        if (query.getSingleResult() == null) {
            throw new StaleEntityException("The entity version has changed in the database: " + entityType.getName() + ":" + entityId);
        }
    }
    
    /**
     * Determines the field which represents the entity id for this query's entity type.
     * 
     * @return The id field.
     * 
     * @throws SQLException
     */
    private Field getIdField() throws SQLException {
        return getIdField(entityType);
    }
    
    /**
     * Determines the field which represents the entity id.
     * 
     * @param entityType The entity type.
     * @return The id field.
     * 
     * @throws SQLException
     */
    private Field getIdField(final Class<?> entityType) throws SQLException {
        final List<Field> fields = ClassUtils.getAllFields(entityType);
        
        for (final Field field: fields) {
            if (field.isAnnotationPresent(Id.class)) {
                return field;
            }
        }
        
        throw new SQLException("No id field found for type: " + entityType);
    }
    
    /**
     * Determines the field which represents the version for this query's entity type.
     * 
     * @return The version field.
     * 
     * @throws SQLException
     */
    private Field getVersionField() throws SQLException {
        return getVersionField(entityType);
    }
    
    /**
     * Determines the field which represents the entity version.
     * 
     * @param entityType The entity type.
     * @return The version field.
     * 
     * @throws SQLException
     */
    private Field getVersionField(final Class<?> entityType) throws SQLException {
        final List<Field> fields = ClassUtils.getAllFields(entityType);
        
        for (final Field field: fields) {
            if (field.isAnnotationPresent(Version.class)) {
                return field;
            }
        }
        
        throw new SQLException("No version field found for type: " + entityType);
    }
    
    /**
     * The "non-collection" fields for this query's entity type.
     */
    private List<Field> getMappedSimpleFields() {
        final List<Field> mappedFields = getMappedFields();
        mappedFields.removeIf(field -> !field.isAnnotationPresent(Column.class));
        
        return mappedFields;
    }
    
    /**
     * The "entity-collection" fields for this query's entity type.
     */
    private List<Field> getMappedCollectionFields() {
        final List<Field> mappedFields = getMappedFields();
        mappedFields.removeIf(field -> !field.isAnnotationPresent(CollectionOfEntities.class));
        
        return mappedFields;
    }

    /**
     * All fields for this query's entity type which have persisted values in the database.
     */
    private List<Field> getMappedFields() {
        final List<Field> mappedFields = ClassUtils.getAllFields(entityType);
        mappedFields.removeIf(field -> !field.isAnnotationPresent(Column.class) && !field.isAnnotationPresent(CollectionOfEntities.class));
        
        return mappedFields;
    }
    
    /**
     * The name of the table for this query's entity type.
     */
    private String getTableName() {
        return getTableName(entityType);
    }
    
    /**
     * The name of the table for the provided entity type.
     * 
     * @param entityType The entity type.
     * @return The table name.
     */
    private String getTableName(final Class<?> entityType) {
        return entityType.getAnnotation(Entity.class).table();
    }
    
    
}
