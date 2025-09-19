package com.example.state.hierarchical;

public class PhoneStates {
    
    public static class PhoneRootState extends HierarchicalState {
        public PhoneRootState() {
            super("PHONE");
        }
        
        @Override
        protected void onEnter() {
            System.out.println("Phone system activated");
        }
        
        @Override
        protected HierarchicalState getDefaultChild() {
            return children.get("OFF");
        }
    }
    
    public static class OffState extends HierarchicalState {
        public OffState() {
            super("OFF");
        }
        
        @Override
        protected boolean handleLocalEvent(String event) {
            if ("POWER_ON".equals(event)) {
                transitionToSibling("ON");
                return true;
            }
            return false;
        }
        
        @Override
        protected void onEnter() {
            System.out.println("Phone powered off");
        }
    }
    
    public static class OnState extends HierarchicalState {
        public OnState() {
            super("ON");
        }
        
        @Override
        protected boolean handleLocalEvent(String event) {
            if ("POWER_OFF".equals(event)) {
                transitionToSibling("OFF");
                return true;
            }
            return false;
        }
        
        @Override
        protected void onEnter() {
            System.out.println("Phone powered on");
        }
        
        @Override
        protected HierarchicalState getDefaultChild() {
            return children.get("LOCKED");
        }
    }
    
    public static class LockedState extends HierarchicalState {
        public LockedState() {
            super("LOCKED");
        }
        
        @Override
        protected boolean handleLocalEvent(String event) {
            if ("UNLOCK".equals(event)) {
                transitionToSibling("UNLOCKED");
                return true;
            } else if ("EMERGENCY_CALL".equals(event)) {
                System.out.println("Emergency call allowed even when locked");
                return true;
            }
            return false;
        }
        
        @Override
        protected void onEnter() {
            System.out.println("Screen locked - swipe to unlock");
        }
        
        @Override
        protected HierarchicalState getDefaultChild() {
            return children.get("DISPLAY_OFF");
        }
    }
    
    public static class DisplayOffState extends HierarchicalState {
        public DisplayOffState() {
            super("DISPLAY_OFF");
        }
        
        @Override
        protected boolean handleLocalEvent(String event) {
            if ("WAKE".equals(event)) {
                transitionToSibling("DISPLAY_ON");
                return true;
            }
            return false;
        }
        
        @Override
        protected void onEnter() {
            System.out.println("Display turned off to save battery");
        }
    }
    
    public static class DisplayOnState extends HierarchicalState {
        public DisplayOnState() {
            super("DISPLAY_ON");
        }
        
        @Override
        protected boolean handleLocalEvent(String event) {
            if ("SLEEP".equals(event) || "TIMEOUT".equals(event)) {
                transitionToSibling("DISPLAY_OFF");
                return true;
            }
            return false;
        }
        
        @Override
        protected void onEnter() {
            System.out.println("Display turned on - showing lock screen");
        }
    }
    
    public static class UnlockedState extends HierarchicalState {
        public UnlockedState() {
            super("UNLOCKED");
        }
        
        @Override
        protected boolean handleLocalEvent(String event) {
            if ("LOCK".equals(event)) {
                transitionToSibling("LOCKED");
                return true;
            }
            return false;
        }
        
        @Override
        protected void onEnter() {
            System.out.println("Phone unlocked - home screen visible");
        }
        
        @Override
        protected HierarchicalState getDefaultChild() {
            return children.get("IDLE");
        }
    }
    
    public static class IdleState extends HierarchicalState {
        public IdleState() {
            super("IDLE");
        }
        
        @Override
        protected boolean handleLocalEvent(String event) {
            switch (event) {
                case "OPEN_APP":
                    transitionToSibling("APP_RUNNING");
                    return true;
                case "INCOMING_CALL":
                    transitionToSibling("CALL_ACTIVE");
                    return true;
                default:
                    return false;
            }
        }
        
        @Override
        protected void onEnter() {
            System.out.println("Phone idle - ready for use");
        }
    }
    
    public static class AppRunningState extends HierarchicalState {
        public AppRunningState() {
            super("APP_RUNNING");
        }
        
        @Override
        protected boolean handleLocalEvent(String event) {
            switch (event) {
                case "CLOSE_APP":
                    transitionToSibling("IDLE");
                    return true;
                case "HOME":
                    transitionToSibling("IDLE");
                    return true;
                case "INCOMING_CALL":
                    System.out.println("Call notification shown over app");
                    return true;
                default:
                    return false;
            }
        }
        
        @Override
        protected void onEnter() {
            System.out.println("Application launched");
        }
        
        @Override
        protected HierarchicalState getDefaultChild() {
            return children.get("FOREGROUND");
        }
    }
    
    public static class ForegroundState extends HierarchicalState {
        public ForegroundState() {
            super("FOREGROUND");
        }
        
        @Override
        protected boolean handleLocalEvent(String event) {
            if ("MINIMIZE".equals(event)) {
                transitionToSibling("BACKGROUND");
                return true;
            }
            return false;
        }
        
        @Override
        protected void onEnter() {
            System.out.println("App in foreground - full resources available");
        }
    }
    
    public static class BackgroundState extends HierarchicalState {
        public BackgroundState() {
            super("BACKGROUND");
        }
        
        @Override
        protected boolean handleLocalEvent(String event) {
            if ("RESTORE".equals(event)) {
                transitionToSibling("FOREGROUND");
                return true;
            }
            return false;
        }
        
        @Override
        protected void onEnter() {
            System.out.println("App in background - limited resources");
        }
    }
    
    public static class CallActiveState extends HierarchicalState {
        public CallActiveState() {
            super("CALL_ACTIVE");
        }
        
        @Override
        protected boolean handleLocalEvent(String event) {
            if ("END_CALL".equals(event)) {
                transitionToSibling("IDLE");
                return true;
            }
            return false;
        }
        
        @Override
        protected void onEnter() {
            System.out.println("Call in progress");
        }
        
        @Override
        protected HierarchicalState getDefaultChild() {
            return children.get("RINGING");
        }
    }
    
    public static class RingingState extends HierarchicalState {
        public RingingState() {
            super("RINGING");
        }
        
        @Override
        protected boolean handleLocalEvent(String event) {
            switch (event) {
                case "ANSWER":
                    transitionToSibling("TALKING");
                    return true;
                case "REJECT":
                    if (parent != null) {
                        parent.transitionToSibling("IDLE");
                    }
                    return true;
                default:
                    return false;
            }
        }
        
        @Override
        protected void onEnter() {
            System.out.println("Phone ringing - incoming call");
        }
    }
    
    public static class TalkingState extends HierarchicalState {
        public TalkingState() {
            super("TALKING");
        }
        
        @Override
        protected boolean handleLocalEvent(String event) {
            if ("HOLD".equals(event)) {
                transitionToSibling("ON_HOLD");
                return true;
            }
            return false;
        }
        
        @Override
        protected void onEnter() {
            System.out.println("Call connected - talking");
        }
    }
    
    public static class OnHoldState extends HierarchicalState {
        public OnHoldState() {
            super("ON_HOLD");
        }
        
        @Override
        protected boolean handleLocalEvent(String event) {
            if ("RESUME".equals(event)) {
                transitionToSibling("TALKING");
                return true;
            }
            return false;
        }
        
        @Override
        protected void onEnter() {
            System.out.println("Call on hold");
        }
    }
    
    public static HierarchicalState buildPhoneHierarchy() {
        PhoneRootState phone = new PhoneRootState();
        
        OffState off = new OffState();
        OnState on = new OnState();
        
        phone.addChild(off);
        phone.addChild(on);
        
        LockedState locked = new LockedState();
        UnlockedState unlocked = new UnlockedState();
        
        on.addChild(locked);
        on.addChild(unlocked);
        
        DisplayOffState displayOff = new DisplayOffState();
        DisplayOnState displayOn = new DisplayOnState();
        
        locked.addChild(displayOff);
        locked.addChild(displayOn);
        
        IdleState idle = new IdleState();
        AppRunningState appRunning = new AppRunningState();
        CallActiveState callActive = new CallActiveState();
        
        unlocked.addChild(idle);
        unlocked.addChild(appRunning);
        unlocked.addChild(callActive);
        
        ForegroundState foreground = new ForegroundState();
        BackgroundState background = new BackgroundState();
        
        appRunning.addChild(foreground);
        appRunning.addChild(background);
        
        RingingState ringing = new RingingState();
        TalkingState talking = new TalkingState();
        OnHoldState onHold = new OnHoldState();
        
        callActive.addChild(ringing);
        callActive.addChild(talking);
        callActive.addChild(onHold);
        
        return phone;
    }
}