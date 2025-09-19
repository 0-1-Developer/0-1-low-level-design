package com.example.state.persistent;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class PersistentWorkflowEngine {
    private final StateStore stateStore;
    private final Map<String, PersistentWorkflow> activeWorkflows = new HashMap<>();
    private final AtomicInteger versionCounter = new AtomicInteger(0);
    
    public PersistentWorkflowEngine(StateStore stateStore) {
        this.stateStore = stateStore;
    }
    
    public PersistentWorkflow createWorkflow(String workflowId, String initialState) {
        Map<String, Object> initialContext = new HashMap<>();
        PersistentWorkflow workflow = new PersistentWorkflow(workflowId, initialState, stateStore);
        activeWorkflows.put(workflowId, workflow);
        
        StateSnapshot snapshot = new StateSnapshot(workflowId, initialState, 
            versionCounter.incrementAndGet(), initialContext);
        stateStore.saveSnapshot(snapshot);
        
        System.out.println("Created workflow: " + workflowId + " in state: " + initialState);
        return workflow;
    }
    
    public PersistentWorkflow recoverWorkflow(String workflowId) {
        StateSnapshot latest = stateStore.loadLatest(workflowId);
        if (latest == null) {
            System.out.println("No saved state found for workflow: " + workflowId);
            return null;
        }
        
        if (!latest.validate()) {
            System.err.println("Corrupted state detected for workflow: " + workflowId);
            return null;
        }
        
        PersistentWorkflow workflow = new PersistentWorkflow(workflowId, latest.getStateName(), stateStore);
        workflow.restoreFromSnapshot(latest);
        activeWorkflows.put(workflowId, workflow);
        
        System.out.println("Recovered workflow: " + workflowId + " from state: " + latest.getStateName());
        return workflow;
    }
    
    public void saveCheckpoint(String workflowId) {
        PersistentWorkflow workflow = activeWorkflows.get(workflowId);
        if (workflow != null) {
            StateSnapshot snapshot = new StateSnapshot(workflowId, workflow.getCurrentState(),
                versionCounter.incrementAndGet(), workflow.getContext());
            stateStore.saveSnapshot(snapshot);
            System.out.println("Checkpoint saved for workflow: " + workflowId);
        }
    }
    
    public void rollback(String workflowId, int targetVersion) {
        StateSnapshot snapshot = stateStore.loadSnapshot(workflowId, targetVersion);
        if (snapshot != null && snapshot.validate()) {
            PersistentWorkflow workflow = activeWorkflows.get(workflowId);
            if (workflow != null) {
                workflow.restoreFromSnapshot(snapshot);
                System.out.println("Rolled back workflow: " + workflowId + " to version: " + targetVersion);
            }
        } else {
            System.err.println("Cannot rollback - snapshot not found or corrupted");
        }
    }
    
    public List<StateSnapshot> getWorkflowHistory(String workflowId) {
        return stateStore.getHistory(workflowId);
    }
    
    public void pauseWorkflow(String workflowId) {
        saveCheckpoint(workflowId);
        PersistentWorkflow workflow = activeWorkflows.remove(workflowId);
        if (workflow != null) {
            System.out.println("Workflow paused and persisted: " + workflowId);
        }
    }
    
    public void cleanupOldVersions(String workflowId, int keepVersions) {
        stateStore.cleanup(workflowId, keepVersions);
    }
}