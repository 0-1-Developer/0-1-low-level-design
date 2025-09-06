package com.example.builder.fluent;

public class Computer {
    private final String cpu;
    private final String memory;
    private final String storage;
    private final String graphics;
    private final String motherboard;
    private final boolean hasWifi;
    private final boolean hasBluetooth;
    private final boolean hasWebcam;
    private final int ports;
    private final String operatingSystem;
    
    private Computer(ComputerBuilder builder) {
        this.cpu = builder.cpu;
        this.memory = builder.memory;
        this.storage = builder.storage;
        this.graphics = builder.graphics;
        this.motherboard = builder.motherboard;
        this.hasWifi = builder.hasWifi;
        this.hasBluetooth = builder.hasBluetooth;
        this.hasWebcam = builder.hasWebcam;
        this.ports = builder.ports;
        this.operatingSystem = builder.operatingSystem;
    }
    
    public String getCpu() { return cpu; }
    public String getMemory() { return memory; }
    public String getStorage() { return storage; }
    public String getGraphics() { return graphics; }
    public String getMotherboard() { return motherboard; }
    public boolean hasWifi() { return hasWifi; }
    public boolean hasBluetooth() { return hasBluetooth; }
    public boolean hasWebcam() { return hasWebcam; }
    public int getPorts() { return ports; }
    public String getOperatingSystem() { return operatingSystem; }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Computer Configuration:\n");
        sb.append("  CPU: ").append(cpu != null ? cpu : "Not specified").append("\n");
        sb.append("  Memory: ").append(memory != null ? memory : "Not specified").append("\n");
        sb.append("  Storage: ").append(storage != null ? storage : "Not specified").append("\n");
        sb.append("  Graphics: ").append(graphics != null ? graphics : "Integrated").append("\n");
        sb.append("  Motherboard: ").append(motherboard != null ? motherboard : "Standard").append("\n");
        sb.append("  WiFi: ").append(hasWifi ? "Yes" : "No").append("\n");
        sb.append("  Bluetooth: ").append(hasBluetooth ? "Yes" : "No").append("\n");
        sb.append("  Webcam: ").append(hasWebcam ? "Yes" : "No").append("\n");
        sb.append("  Ports: ").append(ports).append("\n");
        sb.append("  OS: ").append(operatingSystem != null ? operatingSystem : "None");
        return sb.toString();
    }
    
    public static class ComputerBuilder {
        private String cpu;
        private String memory;
        private String storage;
        private String graphics;
        private String motherboard;
        private boolean hasWifi = false;
        private boolean hasBluetooth = false;
        private boolean hasWebcam = false;
        private int ports = 4;
        private String operatingSystem;
        
        public ComputerBuilder cpu(String cpu) {
            this.cpu = cpu;
            return this;
        }
        
        public ComputerBuilder memory(String memory) {
            this.memory = memory;
            return this;
        }
        
        public ComputerBuilder storage(String storage) {
            this.storage = storage;
            return this;
        }
        
        public ComputerBuilder graphics(String graphics) {
            this.graphics = graphics;
            return this;
        }
        
        public ComputerBuilder motherboard(String motherboard) {
            this.motherboard = motherboard;
            return this;
        }
        
        public ComputerBuilder withWifi() {
            this.hasWifi = true;
            return this;
        }
        
        public ComputerBuilder withBluetooth() {
            this.hasBluetooth = true;
            return this;
        }
        
        public ComputerBuilder withWebcam() {
            this.hasWebcam = true;
            return this;
        }
        
        public ComputerBuilder ports(int ports) {
            this.ports = ports;
            return this;
        }
        
        public ComputerBuilder operatingSystem(String os) {
            this.operatingSystem = os;
            return this;
        }
        
        public Computer build() {
            if (cpu == null) {
                throw new IllegalStateException("CPU is required");
            }
            if (memory == null) {
                throw new IllegalStateException("Memory is required");
            }
            return new Computer(this);
        }
    }
}