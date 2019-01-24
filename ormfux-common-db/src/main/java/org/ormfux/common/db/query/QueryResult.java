package org.ormfux.common.db.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Iterable result of a query execution.
 */
public class QueryResult implements Iterable<QueryResult.QueryResultRow> {
    
    /**
     * The indices of the columns in the result by name.
     */
    private final Map<String, Integer> columns = new HashMap<>();
    
    /**
     * The rows in the result.
     */
    private final List<QueryResultRow> rows = new ArrayList<>();
    
    /**
     * Adds a column definition to the result.
     * 
     * @param columnIdx The index of the column.
     * @param columnName The name of the column in the result.
     */
    protected void addColumn(final int columnIdx, final String columnName) {
        columns.put(columnName, columnIdx);
    }
    
    /**
     * Adds the values of a row to the result.
     *
     * @param row The data of a single row.
     */
    protected void addRow(final List<Object> row) {
        rows.add(new QueryResultRow(row));
    }
    
    /**
     * The numer of entries in the result.
     */
    public int size() {
        return rows.size();
    }
    
    /** {@inheritDoc} */
    @Override
    public Iterator<QueryResultRow> iterator() {
        return rows.iterator();
    }
    
    /**
     * A row in a query result.
     */
    public class QueryResultRow {
        
        /**
         * The row data.
         */
        private final List<Object> rowData;
        
        /**
         * @param rowData The row data.
         */
        protected QueryResultRow(List<Object> rowData) {
            this.rowData = rowData;
        }
        
        /**
         * Gets the value of a column from the row.
         * 
         * @param columnIdx The column's index in the row.
         * @return The value.
         */
        public Object getValue(final int columnIdx) {
            return rowData.get(columnIdx);
        }
        
        /**
         * Gets the value of a column from the row.
         * 
         * @param columnName The column's name.
         * @return The value.
         */
        public Object getValue(final String columnName) {
            return rowData.get(columns.get(columnName));
        }
        
    }

}
