package com.example.builder.prototype;

import java.util.HashMap;
import java.util.Map;

public final class ServerProfile implements Cloneable {
    private final String name;
    private final String os;
    private final int cpuCores;
    private final int memoryGB;
    private final int storageGB;
    private final String networkConfig;
    private final Map<String, String> environmentVars;
    private final boolean monitoring;
    private final boolean backup;
    
    private ServerProfile(String name, String os, int cpuCores, int memoryGB, 
                         int storageGB, String networkConfig, 
                         Map<String, String> environmentVars, 
                         boolean monitoring, boolean backup) {
        this.name = name;
        this.os = os;
        this.cpuCores = cpuCores;
        this.memoryGB = memoryGB;
        this.storageGB = storageGB;
        this.networkConfig = networkConfig;
        this.environmentVars = new HashMap<>(environmentVars);
        this.monitoring = monitoring;
        this.backup = backup;
    }
    
    public String getName() { return name; }
    public String getOs() { return os; }
    public int getCpuCores() { return cpuCores; }
    public int getMemoryGB() { return memoryGB; }
    public int getStorageGB() { return storageGB; }
    public String getNetworkConfig() { return networkConfig; }
    public Map<String, String> getEnvironmentVars() { return new HashMap<>(environmentVars); }
    public boolean hasMonitoring() { return monitoring; }
    public boolean hasBackup() { return backup; }
    
    @Override
    public String toString() {
        return String.format("ServerProfile{name='%s', os='%s', cpu=%d cores, memory=%dGB, " +
                           "storage=%dGB, network='%s', envVars=%d, monitoring=%b, backup=%b}",
                           name, os, cpuCores, memoryGB, storageGB, networkConfig, 
                           environmentVars.size(), monitoring, backup);
    }
    
    @Override
    public ServerProfile clone() {
        try {
            ServerProfile cloned = (ServerProfile) super.clone();
            return new ServerProfile(cloned.name, cloned.os, cloned.cpuCores, cloned.memoryGB,
                                   cloned.storageGB, cloned.networkConfig, cloned.environmentVars,
                                   cloned.monitoring, cloned.backup);
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Clone not supported", e);
        }
    }
    
    public ServerProfileBuilder toBuilder() {
        return new ServerProfileBuilder(this);
    }
    
    public static ServerProfileBuilder builder() {
        return new ServerProfileBuilder();
    }
    
    public static class ServerProfileBuilder {
        private String name = "default-server";
        private String os = "Ubuntu 20.04";
        private int cpuCores = 2;
        private int memoryGB = 4;
        private int storageGB = 50;
        private String networkConfig = "bridge";
        private Map<String, String> environmentVars = new HashMap<>();
        private boolean monitoring = false;
        private boolean backup = false;
        
        public ServerProfileBuilder() {}
        
        public ServerProfileBuilder(ServerProfile prototype) {
            this.name = prototype.name;
            this.os = prototype.os;
            this.cpuCores = prototype.cpuCores;
            this.memoryGB = prototype.memoryGB;
            this.storageGB = prototype.storageGB;
            this.networkConfig = prototype.networkConfig;
            this.environmentVars = new HashMap<>(prototype.environmentVars);
            this.monitoring = prototype.monitoring;
            this.backup = prototype.backup;
        }
        
        public ServerProfileBuilder name(String name) {
            this.name = name;
            return this;
        }
        
        public ServerProfileBuilder os(String os) {
            this.os = os;
            return this;
        }
        
        public ServerProfileBuilder cpuCores(int cpuCores) {
            this.cpuCores = cpuCores;
            return this;
        }
        
        public ServerProfileBuilder memoryGB(int memoryGB) {
            this.memoryGB = memoryGB;
            return this;
        }
        
        public ServerProfileBuilder storageGB(int storageGB) {
            this.storageGB = storageGB;
            return this;
        }
        
        public ServerProfileBuilder networkConfig(String networkConfig) {
            this.networkConfig = networkConfig;
            return this;
        }
        
        public ServerProfileBuilder environmentVariable(String key, String value) {
            this.environmentVars.put(key, value);
            return this;
        }
        
        public ServerProfileBuilder withMonitoring() {
            this.monitoring = true;
            return this;
        }
        
        public ServerProfileBuilder withBackup() {
            this.backup = true;
            return this;
        }
        
        public ServerProfile build() {
            return new ServerProfile(name, os, cpuCores, memoryGB, storageGB, 
                                   networkConfig, environmentVars, monitoring, backup);
        }
    }
    
    public static final class Prototypes {
        public static final ServerProfile WEB_SERVER = ServerProfile.builder()
            .name("web-server-template")
            .os("Ubuntu 22.04")
            .cpuCores(4)
            .memoryGB(8)
            .storageGB(100)
            .networkConfig("public")
            .environmentVariable("NGINX_VERSION", "1.20")
            .environmentVariable("PHP_VERSION", "8.1")
            .withMonitoring()
            .build();
            
        public static final ServerProfile DATABASE_SERVER = ServerProfile.builder()
            .name("db-server-template")
            .os("CentOS 8")
            .cpuCores(8)
            .memoryGB(32)
            .storageGB(500)
            .networkConfig("private")
            .environmentVariable("MYSQL_VERSION", "8.0")
            .environmentVariable("INNODB_BUFFER_POOL_SIZE", "16G")
            .withMonitoring()
            .withBackup()
            .build();
            
        public static final ServerProfile DEVELOPMENT_SERVER = ServerProfile.builder()
            .name("dev-server-template")
            .os("Ubuntu 20.04")
            .cpuCores(2)
            .memoryGB(4)
            .storageGB(50)
            .networkConfig("bridge")
            .environmentVariable("NODE_ENV", "development")
            .environmentVariable("DEBUG", "true")
            .build();
    }
}