package com.example.state.persistent;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class StateSnapshot implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final String workflowId;
    private final String stateName;
    private final int version;
    private final Instant timestamp;
    private final Map<String, Object> context;
    private final String checksum;
    
    public StateSnapshot(String workflowId, String stateName, int version, Map<String, Object> context) {
        this.workflowId = workflowId;
        this.stateName = stateName;
        this.version = version;
        this.timestamp = Instant.now();
        this.context = new HashMap<>(context);
        this.checksum = calculateChecksum();
    }
    
    private String calculateChecksum() {
        int hash = workflowId.hashCode();
        hash = 31 * hash + stateName.hashCode();
        hash = 31 * hash + version;
        hash = 31 * hash + context.hashCode();
        return Integer.toHexString(hash);
    }
    
    public boolean validate() {
        return checksum.equals(calculateChecksum());
    }
    
    public String getWorkflowId() {
        return workflowId;
    }
    
    public String getStateName() {
        return stateName;
    }
    
    public int getVersion() {
        return version;
    }
    
    public Instant getTimestamp() {
        return timestamp;
    }
    
    public Map<String, Object> getContext() {
        return new HashMap<>(context);
    }
    
    public String getChecksum() {
        return checksum;
    }
    
    @Override
    public String toString() {
        return String.format("StateSnapshot[workflow=%s, state=%s, version=%d, time=%s]",
            workflowId, stateName, version, timestamp);
    }
}