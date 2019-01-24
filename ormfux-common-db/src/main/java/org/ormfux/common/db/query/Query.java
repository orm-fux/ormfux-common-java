package org.ormfux.common.db.query;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.ormfux.common.db.annotation.Entity;
import org.ormfux.common.db.annotation.Id;
import org.ormfux.common.db.exception.NonMatchedParamException;
import org.ormfux.common.db.exception.NonUniqueResultException;
import org.ormfux.common.db.exception.SQLException;
import org.ormfux.common.db.query.QueryResult.QueryResultRow;
import org.ormfux.common.db.query.connection.DbConnectionProvider;
import org.ormfux.common.utils.ListUtils;
import org.ormfux.common.utils.reflection.ClassUtils;
import org.ormfux.common.utils.reflection.PropertyUtils;

/**
 * A simple implementation for an executable SQL query, which can define parameters with parameter names.
 * Parameter names in the query String have to be prefixed with ':'. The query can be executed only once.
 */
public class Query extends AbstractQuery {
    
    /**
     * Creates a new query.
     * 
     * @param dbConnection TThe connection to the database.
     * @param queryString The query to execute.
     */
    protected Query(final DbConnectionProvider dbConnection, final String queryString) {
        super(dbConnection, queryString);
    }
    
    /**
     * Executes an the query as an update query (update/insert/create/drop). 
     * Explicitly commits the changes.
     * 
     * @return the number of affected rows.
     * 
     * @throws SQLException
     */
    public int executeUpdate() throws SQLException {
        final PreparedQuery preparedQuery = prepareQueryForExecution();
        
        try {
            int affectedRows = 0;
            final Connection connection = getDbConnection();
            
            //Multiple parameterized queries in one query string are not supported by H2. so we need to split.
            final String[] subQueries = StringUtils.split(preparedQuery.getQueryString(), ';');
            int paramNbr = 0;
            
            for (final String subQuery : subQueries) {
               if (!StringUtils.isBlank(subQuery)) {
                   final int subQueryParamCount = StringUtils.countMatches(subQuery, '?');
                   final PreparedStatement statement = connection.prepareStatement(subQuery);
                       
                   try {
                       for (int paramIdx = 0; paramIdx < subQueryParamCount; paramIdx++) {
                           statement.setObject(paramIdx + 1, preparedQuery.getParamValues().get(paramNbr + paramIdx));
                       }
                       
                       statement.addBatch();
                       
                       final int[] updateResult = statement.executeBatch();
                       
                       for (int updateResultEntry : updateResult) {
                           affectedRows += updateResultEntry;
                       }
                       
                       paramNbr = paramNbr + subQueryParamCount;
                       
                   } catch (java.sql.SQLException e) {
                       throw new SQLException("Error executing query.", e);
                   } finally {
                       statement.close();
                   }
                   
               }
            }
            
            connection.commit();
            connection.close();
            
            return affectedRows;
            
        } catch (java.sql.SQLException e) {
            throw new SQLException("Error connecting or commiting to database.", e);
        }
        
    }
    
    /**
     * Executes the query as a select query that should return only one result row.
     * 
     * @return The result row; {code null} when the query returned an empty result.
     * 
     * @throws SQLException
     */
    public QueryResultRow getSingleResult() throws SQLException {
        final QueryResult resultList = getResultList();
        
        if (resultList.size() < 1) {
            return null;
            
        } else if (resultList.size() > 1) {
            throw new NonUniqueResultException("The query result contains more than one row.");
            
        } else {
            return resultList.iterator().next();
        }
    }
    
    /**
     * Executes an the query as a select query.
     * 
     * @return The query result as iterable object.
     * 
     * @throws SQLException
     */
    public QueryResult getResultList() throws SQLException {
        final PreparedQuery preparedQuery = prepareQueryForExecution();
        
        try {
            final Connection connection = getDbConnection();
            final PreparedStatement statement = connection.prepareStatement(preparedQuery.getQueryString());
            
            try {
                for (int queryIdx = 0; queryIdx < preparedQuery.getParamValues().size(); queryIdx++) {
                    statement.setObject(queryIdx + 1, preparedQuery.getParamValues().get(queryIdx));
                }
                
                statement.addBatch();
                
                final QueryResult result = new QueryResult();
                
                final ResultSet resultSet = statement.executeQuery();
                final int resultColumnCount = resultSet.getMetaData().getColumnCount();
                
                for (int columnIdx = 1; columnIdx <= resultColumnCount; columnIdx++) {
                    //our indices are zero-based
                    result.addColumn(columnIdx - 1, resultSet.getMetaData().getColumnLabel(columnIdx));
                }
                
                while (resultSet.next()) {
                    final List<Object> rowData = new ArrayList<>(resultColumnCount);
                    
                    for (int columnIdx = 1; columnIdx <= resultColumnCount; columnIdx++) {
                        rowData.add(resultSet.getObject(columnIdx));
                    }
                    
                    result.addRow(rowData);
                }
                
                return result;
                
            } catch (java.sql.SQLException e) {
                throw new SQLException("Error connecting to database.", e);
            } finally {
                statement.close();
                connection.close();
            }
        } catch (java.sql.SQLException e) {
            throw new SQLException("Error connecting to database.", e);
        }
    }
    
    /**
     * Prepares the query for execution.
     *  
     * @return The query as it can be executed.
     * 
     * @throws SQLException
     */
    private PreparedQuery prepareQueryForExecution() throws SQLException {
        final PreparedQuery query = new PreparedQuery();
        query.setQueryString(getQueryString());
        
        if (!getQueryParams().isEmpty()) {
            final Set<String> matchedParams = new HashSet<>();
            int nextParamIndex = query.getQueryString().indexOf(':');
            
            while (nextParamIndex > -1) {
                //determine next param
                int endOfParamName = indexOfSeparator(query.getQueryString(), nextParamIndex);
                
                if (endOfParamName < 0) { //query should end with this parameter.
                    endOfParamName = query.getQueryString().length();
                }
                
                final String nextParamName = query.getQueryString().substring(nextParamIndex + 1, endOfParamName);
                
                if (!getQueryParams().containsKey(nextParamName)) {
                    throw new SQLException("Parameter value not defined: " + nextParamName);
                }
                
                matchedParams.add(nextParamName);
                final List<Object> paramValues = new ArrayList<>();
                final Object paramValue = getQueryParams().get(nextParamName);
                final String paramInQuery;
                
                if (paramValue instanceof Collection) {
                    final Collection<?> collection = (Collection<?>) paramValue;
                    
                    if (collection.isEmpty()) {
                        paramInQuery = "null";
                        
                    } else {
                        paramInQuery = '(' + StringUtils.repeat("?", ",", collection.size()) + ')';
                        paramValues.addAll(collection);
                    }
                    
                    
                } else {
                    paramInQuery = "?";
                    paramValues.add(paramValue);
                }
                
                query.setQueryString(StringUtils.replaceOnce(query.getQueryString(), ':' + nextParamName, paramInQuery));
                
                for (final Object param : paramValues) {
                    if (param == null) {
                        query.addParamValue(null);
                        
                    } else if (param.getClass().isEnum()) {
                        query.addParamValue(((Enum<?>) param).name());
                        
                    } else if (param.getClass().isAnnotationPresent(Entity.class)) {
                        final Field idField = ListUtils.selectFirst(ClassUtils.getAllFields(param.getClass()), field -> field.isAnnotationPresent(Id.class));
                        
                        query.addParamValue(PropertyUtils.read(param, idField.getName()));
                        
                    } else {
                        query.addParamValue(paramValue);
                    }
                }
                
                //next param position
                nextParamIndex = query.getQueryString().indexOf(':');
            }
            
            if (matchedParams.size() != getQueryParams().size()) {
                throw new NonMatchedParamException("At least one parameter value was not matched. Non-matched values: " 
                                                    + new HashSet<>(getQueryParams().keySet()).removeAll(matchedParams));
            }
            
        }
        
        return query;
    }
    
    /**
     * Determines the index of the next "separator character" in the query.
     * Separators are white spaces, comma, semi colon, tabs, line breaks, and closing 
     * brackets.
     *
     * @param queryString The query.
     * @param startPos The idnex from which to search for the next separator.
     * @return The index of the next separator character; {@code -1} when there is none.
     */
    private int indexOfSeparator(final String queryString, int startPos) {
        int index = -1;
        
        for (char separator : new char[]{' ', ';', ',', '\t', '\n', ')'}) {
            final int separatorIndex = queryString.indexOf(separator, startPos);
            
            if (separatorIndex > 0 && (index == -1 || index > separatorIndex)) {
                index = separatorIndex;
            }
        }
        
        return index;
    }
    
    /**
     * A query prepared for execution.
     */
    private class PreparedQuery {
        
        /**
         * The final query String.
         */
        private String queryString;
        
        /**
         * Values of the parameters in the order they have to be applied to the query.
         */
        private List<Object> paramValues = new ArrayList<>();
        
        /**
         * Adds a parameter value for the execution. 
         */
        public void addParamValue(final Object paramValue) {
            this.paramValues.add(paramValue);
        }
        
        /**
         * The parameter values in the order their corresponding parameter appears in the query.
         */
        public List<Object> getParamValues() {
            return paramValues;
        }
        
        /**
         * The final query String.
         */
        public String getQueryString() {
            return queryString;
        }
        
        /**
         * @see #getQueryString()
         */
        public void setQueryString(final String queryString) {
            this.queryString = queryString;
        }
        
    }
}
