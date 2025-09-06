package com.example.builder.functional;

import java.util.function.Consumer;
import java.util.Properties;

public final class Configuration {
    private final String host;
    private final int port;
    private final boolean ssl;
    private final int timeout;
    private final int retries;
    private final Properties additionalSettings;
    
    private Configuration(String host, int port, boolean ssl, int timeout, 
                         int retries, Properties additionalSettings) {
        this.host = host;
        this.port = port;
        this.ssl = ssl;
        this.timeout = timeout;
        this.retries = retries;
        this.additionalSettings = new Properties();
        this.additionalSettings.putAll(additionalSettings);
    }
    
    public String getHost() { return host; }
    public int getPort() { return port; }
    public boolean isSsl() { return ssl; }
    public int getTimeout() { return timeout; }
    public int getRetries() { return retries; }
    public Properties getAdditionalSettings() { 
        Properties copy = new Properties();
        copy.putAll(additionalSettings);
        return copy;
    }
    
    @Override
    public String toString() {
        return String.format("Configuration{host='%s', port=%d, ssl=%b, timeout=%d, retries=%d, additionalSettings=%d}",
                           host, port, ssl, timeout, retries, additionalSettings.size());
    }
    
    public static Configuration build(Consumer<Builder> configurator) {
        Builder builder = new Builder();
        configurator.accept(builder);
        return builder.build();
    }
    
    public static Configuration defaultConfig() {
        return new Builder().build();
    }
    
    public static class Builder {
        private String host = "localhost";
        private int port = 8080;
        private boolean ssl = false;
        private int timeout = 30000;
        private int retries = 3;
        private Properties additionalSettings = new Properties();
        
        public Builder host(String host) {
            this.host = host;
            return this;
        }
        
        public Builder port(int port) {
            this.port = port;
            return this;
        }
        
        public Builder ssl(boolean ssl) {
            this.ssl = ssl;
            return this;
        }
        
        public Builder timeout(int timeout) {
            this.timeout = timeout;
            return this;
        }
        
        public Builder retries(int retries) {
            this.retries = retries;
            return this;
        }
        
        public Builder setting(String key, String value) {
            this.additionalSettings.setProperty(key, value);
            return this;
        }
        
        public Configuration build() {
            return new Configuration(host, port, ssl, timeout, retries, additionalSettings);
        }
    }
}