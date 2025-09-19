package com.example.state.hierarchical;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class HierarchicalState {
    protected String name;
    protected HierarchicalState parent;
    protected HierarchicalState activeChild;
    protected Map<String, HierarchicalState> children = new HashMap<>();
    protected HierarchicalContext context;
    
    public HierarchicalState(String name) {
        this.name = name;
    }
    
    public void setParent(HierarchicalState parent) {
        this.parent = parent;
    }
    
    public void addChild(HierarchicalState child) {
        children.put(child.getName(), child);
        child.setParent(this);
    }
    
    public void setContext(HierarchicalContext context) {
        this.context = context;
        for (HierarchicalState child : children.values()) {
            child.setContext(context);
        }
    }
    
    public void enter() {
        System.out.println("Entering " + getFullPath());
        onEnter();
        
        if (!children.isEmpty() && activeChild == null) {
            HierarchicalState defaultChild = getDefaultChild();
            if (defaultChild != null) {
                activeChild = defaultChild;
                activeChild.enter();
            }
        }
    }
    
    public void exit() {
        if (activeChild != null) {
            activeChild.exit();
            activeChild = null;
        }
        onExit();
        System.out.println("Exiting " + getFullPath());
    }
    
    public boolean handleEvent(String event) {
        if (activeChild != null && activeChild.handleEvent(event)) {
            return true;
        }
        
        return handleLocalEvent(event);
    }
    
    protected boolean handleLocalEvent(String event) {
        return false;
    }
    
    protected void onEnter() {
    }
    
    protected void onExit() {
    }
    
    protected HierarchicalState getDefaultChild() {
        if (!children.isEmpty()) {
            return children.values().iterator().next();
        }
        return null;
    }
    
    public void transitionToChild(String childName) {
        HierarchicalState newChild = children.get(childName);
        if (newChild == null) {
            System.out.println("Child state " + childName + " not found in " + name);
            return;
        }
        
        if (activeChild != null && activeChild != newChild) {
            activeChild.exit();
        }
        
        activeChild = newChild;
        activeChild.enter();
    }
    
    public void transitionToSibling(String siblingName) {
        if (parent != null) {
            parent.transitionToChild(siblingName);
        }
    }
    
    public String getName() {
        return name;
    }
    
    public String getFullPath() {
        List<String> path = new ArrayList<>();
        HierarchicalState current = this;
        while (current != null) {
            path.add(0, current.getName());
            current = current.parent;
        }
        return String.join(".", path);
    }
    
    public HierarchicalState getActiveLeaf() {
        if (activeChild == null) {
            return this;
        }
        return activeChild.getActiveLeaf();
    }
    
    public boolean isActive() {
        return context != null && context.isStateActive(this);
    }
}