package com.example.builder.dsl;

import java.util.ArrayList;
import java.util.List;

public class SqlQuery {
    private final String query;
    
    private SqlQuery(String query) {
        this.query = query;
    }
    
    public String getQuery() {
        return query;
    }
    
    @Override
    public String toString() {
        return query;
    }
    
    public static SelectBuilder select(String... columns) {
        return new QueryBuilder().select(columns);
    }
    
    public static class QueryBuilder {
        
        public SelectBuilder select(String... columns) {
            return new SelectBuilder(String.join(", ", columns));
        }
    }
    
    public static class SelectBuilder {
        private String selectClause;
        
        private SelectBuilder(String selectClause) {
            this.selectClause = "SELECT " + selectClause;
        }
        
        public FromBuilder from(String table) {
            return new FromBuilder(selectClause + " FROM " + table);
        }
    }
    
    public static class FromBuilder {
        private String currentQuery;
        
        private FromBuilder(String currentQuery) {
            this.currentQuery = currentQuery;
        }
        
        public WhereBuilder where(String condition) {
            return new WhereBuilder(currentQuery + " WHERE " + condition);
        }
        
        public JoinBuilder innerJoin(String table) {
            return new JoinBuilder(currentQuery + " INNER JOIN " + table);
        }
        
        public JoinBuilder leftJoin(String table) {
            return new JoinBuilder(currentQuery + " LEFT JOIN " + table);
        }
        
        public GroupByBuilder groupBy(String... columns) {
            return new GroupByBuilder(currentQuery + " GROUP BY " + String.join(", ", columns));
        }
        
        public OrderByBuilder orderBy(String column) {
            return new OrderByBuilder(currentQuery + " ORDER BY " + column);
        }
        
        public SqlQuery build() {
            return new SqlQuery(currentQuery);
        }
    }
    
    public static class JoinBuilder {
        private String currentQuery;
        
        private JoinBuilder(String currentQuery) {
            this.currentQuery = currentQuery;
        }
        
        public FromBuilder on(String condition) {
            return new FromBuilder(currentQuery + " ON " + condition);
        }
    }
    
    public static class WhereBuilder {
        private String currentQuery;
        
        private WhereBuilder(String currentQuery) {
            this.currentQuery = currentQuery;
        }
        
        public WhereBuilder and(String condition) {
            return new WhereBuilder(currentQuery + " AND " + condition);
        }
        
        public WhereBuilder or(String condition) {
            return new WhereBuilder(currentQuery + " OR " + condition);
        }
        
        public GroupByBuilder groupBy(String... columns) {
            return new GroupByBuilder(currentQuery + " GROUP BY " + String.join(", ", columns));
        }
        
        public OrderByBuilder orderBy(String column) {
            return new OrderByBuilder(currentQuery + " ORDER BY " + column);
        }
        
        public LimitBuilder limit(int count) {
            return new LimitBuilder(currentQuery + " LIMIT " + count);
        }
        
        public SqlQuery build() {
            return new SqlQuery(currentQuery);
        }
    }
    
    public static class GroupByBuilder {
        private String currentQuery;
        
        private GroupByBuilder(String currentQuery) {
            this.currentQuery = currentQuery;
        }
        
        public HavingBuilder having(String condition) {
            return new HavingBuilder(currentQuery + " HAVING " + condition);
        }
        
        public OrderByBuilder orderBy(String column) {
            return new OrderByBuilder(currentQuery + " ORDER BY " + column);
        }
        
        public SqlQuery build() {
            return new SqlQuery(currentQuery);
        }
    }
    
    public static class HavingBuilder {
        private String currentQuery;
        
        private HavingBuilder(String currentQuery) {
            this.currentQuery = currentQuery;
        }
        
        public OrderByBuilder orderBy(String column) {
            return new OrderByBuilder(currentQuery + " ORDER BY " + column);
        }
        
        public SqlQuery build() {
            return new SqlQuery(currentQuery);
        }
    }
    
    public static class OrderByBuilder {
        private String currentQuery;
        
        private OrderByBuilder(String currentQuery) {
            this.currentQuery = currentQuery;
        }
        
        public OrderByBuilder asc() {
            return new OrderByBuilder(currentQuery + " ASC");
        }
        
        public OrderByBuilder desc() {
            return new OrderByBuilder(currentQuery + " DESC");
        }
        
        public LimitBuilder limit(int count) {
            return new LimitBuilder(currentQuery + " LIMIT " + count);
        }
        
        public SqlQuery build() {
            return new SqlQuery(currentQuery);
        }
    }
    
    public static class LimitBuilder {
        private String currentQuery;
        
        private LimitBuilder(String currentQuery) {
            this.currentQuery = currentQuery;
        }
        
        public SqlQuery build() {
            return new SqlQuery(currentQuery);
        }
    }
}