package com.example.builder.nested;

import java.util.Properties;
import java.util.HashMap;
import java.util.Map;

public final class DatabaseConnection {
    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;
    private final String driver;
    private final int connectionTimeout;
    private final int readTimeout;
    private final int maxPoolSize;
    private final boolean autoCommit;
    private final String isolationLevel;
    private final Properties additionalProperties;
    
    private DatabaseConnection(Builder builder) {
        this.host = builder.host;
        this.port = builder.port;
        this.database = builder.database;
        this.username = builder.username;
        this.password = builder.password;
        this.driver = builder.driver;
        this.connectionTimeout = builder.connectionTimeout;
        this.readTimeout = builder.readTimeout;
        this.maxPoolSize = builder.maxPoolSize;
        this.autoCommit = builder.autoCommit;
        this.isolationLevel = builder.isolationLevel;
        this.additionalProperties = new Properties();
        this.additionalProperties.putAll(builder.additionalProperties);
    }
    
    public String getHost() { return host; }
    public int getPort() { return port; }
    public String getDatabase() { return database; }
    public String getUsername() { return username; }
    public String getDriver() { return driver; }
    public int getConnectionTimeout() { return connectionTimeout; }
    public int getReadTimeout() { return readTimeout; }
    public int getMaxPoolSize() { return maxPoolSize; }
    public boolean isAutoCommit() { return autoCommit; }
    public String getIsolationLevel() { return isolationLevel; }
    public Properties getAdditionalProperties() { 
        Properties copy = new Properties();
        copy.putAll(additionalProperties);
        return copy;
    }
    
    public String getConnectionString() {
        return String.format("jdbc:%s://%s:%d/%s", driver, host, port, database);
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    @Override
    public String toString() {
        return String.format("DatabaseConnection{host='%s', port=%d, database='%s', " +
                           "username='%s', driver='%s', connectionTimeout=%d, readTimeout=%d, " +
                           "maxPoolSize=%d, autoCommit=%b, isolationLevel='%s', " +
                           "additionalProperties=%d properties}",
                           host, port, database, username, driver, connectionTimeout,
                           readTimeout, maxPoolSize, autoCommit, isolationLevel,
                           additionalProperties.size());
    }
    
    public static final class Builder {
        private String host = "localhost";
        private int port = 5432;
        private String database;
        private String username;
        private String password;
        private String driver = "postgresql";
        private int connectionTimeout = 30000;
        private int readTimeout = 0;
        private int maxPoolSize = 10;
        private boolean autoCommit = true;
        private String isolationLevel = "READ_COMMITTED";
        private Map<String, String> additionalProperties = new HashMap<>();
        
        private Builder() {}
        
        public Builder host(String host) {
            this.host = host;
            return this;
        }
        
        public Builder port(int port) {
            if (port <= 0 || port > 65535) {
                throw new IllegalArgumentException("Port must be between 1 and 65535");
            }
            this.port = port;
            return this;
        }
        
        public Builder database(String database) {
            this.database = database;
            return this;
        }
        
        public Builder username(String username) {
            this.username = username;
            return this;
        }
        
        public Builder password(String password) {
            this.password = password;
            return this;
        }
        
        public Builder driver(String driver) {
            this.driver = driver;
            return this;
        }
        
        public Builder connectionTimeout(int timeoutMs) {
            if (timeoutMs < 0) {
                throw new IllegalArgumentException("Connection timeout cannot be negative");
            }
            this.connectionTimeout = timeoutMs;
            return this;
        }
        
        public Builder readTimeout(int timeoutMs) {
            if (timeoutMs < 0) {
                throw new IllegalArgumentException("Read timeout cannot be negative");
            }
            this.readTimeout = timeoutMs;
            return this;
        }
        
        public Builder maxPoolSize(int maxPoolSize) {
            if (maxPoolSize <= 0) {
                throw new IllegalArgumentException("Max pool size must be positive");
            }
            this.maxPoolSize = maxPoolSize;
            return this;
        }
        
        public Builder autoCommit(boolean autoCommit) {
            this.autoCommit = autoCommit;
            return this;
        }
        
        public Builder isolationLevel(String isolationLevel) {
            String[] validLevels = {"READ_UNCOMMITTED", "READ_COMMITTED", "REPEATABLE_READ", "SERIALIZABLE"};
            boolean valid = false;
            for (String level : validLevels) {
                if (level.equals(isolationLevel)) {
                    valid = true;
                    break;
                }
            }
            if (!valid) {
                throw new IllegalArgumentException("Invalid isolation level: " + isolationLevel);
            }
            this.isolationLevel = isolationLevel;
            return this;
        }
        
        public Builder property(String key, String value) {
            this.additionalProperties.put(key, value);
            return this;
        }
        
        public Builder properties(Map<String, String> properties) {
            this.additionalProperties.putAll(properties);
            return this;
        }
        
        public DatabaseConnection build() {
            if (database == null || database.trim().isEmpty()) {
                throw new IllegalStateException("Database name is required");
            }
            if (username == null || username.trim().isEmpty()) {
                throw new IllegalStateException("Username is required");
            }
            if (password == null) {
                throw new IllegalStateException("Password is required");
            }
            
            return new DatabaseConnection(this);
        }
    }
}