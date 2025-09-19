package com.example.state.hierarchical;

import java.util.Stack;

public class HierarchicalContext {
    private HierarchicalState rootState;
    private Stack<HierarchicalState> activeStateStack = new Stack<>();
    
    public void setRootState(HierarchicalState root) {
        this.rootState = root;
        root.setContext(this);
    }
    
    public void start() {
        if (rootState != null) {
            rootState.enter();
            updateActiveStateStack();
        }
    }
    
    public void handleEvent(String event) {
        System.out.println("\n=== Processing Event: " + event + " ===");
        System.out.println("Active state path: " + getCurrentPath());
        
        if (rootState != null) {
            boolean handled = rootState.handleEvent(event);
            if (handled) {
                updateActiveStateStack();
                System.out.println("Event handled. New path: " + getCurrentPath());
            } else {
                System.out.println("Event not handled by any state");
            }
        }
    }
    
    private void updateActiveStateStack() {
        activeStateStack.clear();
        HierarchicalState current = rootState;
        while (current != null) {
            activeStateStack.push(current);
            current = current.activeChild;
        }
    }
    
    public String getCurrentPath() {
        if (rootState == null) {
            return "NO_STATE";
        }
        HierarchicalState leaf = rootState.getActiveLeaf();
        return leaf != null ? leaf.getFullPath() : "NO_ACTIVE_STATE";
    }
    
    public boolean isStateActive(HierarchicalState state) {
        return activeStateStack.contains(state);
    }
    
    public void printStateHierarchy() {
        System.out.println("\n=== State Hierarchy ===");
        if (rootState != null) {
            printStateTree(rootState, 0);
        }
    }
    
    private void printStateTree(HierarchicalState state, int level) {
        String indent = "  ".repeat(level);
        String active = isStateActive(state) ? " [ACTIVE]" : "";
        System.out.println(indent + "- " + state.getName() + active);
        
        for (HierarchicalState child : state.children.values()) {
            printStateTree(child, level + 1);
        }
    }
}