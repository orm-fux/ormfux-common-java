package org.blue.bunny.ledger.customlibs.db.query;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.blue.bunny.ledger.customlibs.db.exception.DuplicateParamException;
import org.blue.bunny.ledger.customlibs.db.exception.SQLException;

/**
 * Base class for queries.
 */
public abstract class AbstractQuery {
    
    /**
     * The database connection with which to execute the query.
     */
    private final String dbConnection;
    
    /**
     * The query to execute.
     */
    private final String queryString;
    
    /**
     * The parameter values for the query.
     */
    private final Map<String, Object> queryParams = new HashMap<>();
    
    /**
     * Creates a new query.
     * 
     * @param dbConnection The database URL on which to execute the query.
     * @param queryString The query to execute.
     */
    protected AbstractQuery(final String dbConnection, final String queryString) {
        this.dbConnection = dbConnection;
        this.queryString = queryString;
    }
    
    /**
     * Adds a parameter to use for the query execution.
     * 
     * @param paramName The name of the parameter in the query.
     * @param value The parameter value.
     * 
     * @throws SQLException 
     */
    public void addParameter(final String paramName, final Object value) throws SQLException {
        if (queryParams.containsKey(paramName)) {
            throw new DuplicateParamException("A parameter with this name is already defined: " + paramName);
        } else {
            queryParams.put(paramName, value);
        }
    }
    
    /**
     * Adds multiple parameters to use for query execution.
     * 
     * @param parameters The parameters. Key is the parameter name and value, well, the value.
     */
    public void addParameters(final Map<String, Object> parameters) throws SQLException {
        for (final Entry<String, Object> param : parameters.entrySet()) {
            addParameter(param.getKey(), param.getValue());
        }
    }
    
    /**
     * The database connection with which to execute the query.
     */
    protected String getDbConnection() {
        return dbConnection;
    }
    
    /**
     * The query to execute.
     */
    protected String getQueryString() {
        return queryString;
    }
    
    /**
     * The parameter values for the query.
     */
    protected Map<String, Object> getQueryParams() {
        return queryParams;
    }
    
}
