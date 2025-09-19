package com.example.state.table;

public class TrafficLightMachine extends StateMachine {
    private int timer = 0;
    
    public TrafficLightMachine() {
        super("RED");
        initializeTransitions();
    }
    
    private void initializeTransitions() {
        addTransition("RED", "TIMER_EXPIRED", "GREEN", sm -> {
            System.out.println("Light turns GREEN - Cars can GO");
            ((TrafficLightMachine)sm).resetTimer(30);
        });
        
        addTransition("GREEN", "TIMER_EXPIRED", "YELLOW", sm -> {
            System.out.println("Light turns YELLOW - Prepare to STOP");
            ((TrafficLightMachine)sm).resetTimer(5);
        });
        
        addTransition("YELLOW", "TIMER_EXPIRED", "RED", sm -> {
            System.out.println("Light turns RED - STOP!");
            ((TrafficLightMachine)sm).resetTimer(40);
        });
        
        addTransition("RED", "EMERGENCY", "FLASHING_RED", sm -> {
            System.out.println("EMERGENCY! Flashing RED mode");
        });
        
        addTransition("GREEN", "EMERGENCY", "FLASHING_RED", sm -> {
            System.out.println("EMERGENCY! Flashing RED mode");
        });
        
        addTransition("YELLOW", "EMERGENCY", "FLASHING_RED", sm -> {
            System.out.println("EMERGENCY! Flashing RED mode");
        });
        
        addTransition("FLASHING_RED", "RESET", "RED", sm -> {
            System.out.println("Emergency cleared, returning to normal operation");
            ((TrafficLightMachine)sm).resetTimer(40);
        });
        
        addTransition("RED", "PEDESTRIAN_REQUEST", "RED", sm -> {
            System.out.println("Pedestrian crossing requested - will activate at next cycle");
            sm.setContextValue("pedestrian_waiting", true);
        });
        
        addTransition("GREEN", "PEDESTRIAN_REQUEST", "GREEN", sm -> {
            System.out.println("Pedestrian crossing requested - will activate at next cycle");
            sm.setContextValue("pedestrian_waiting", true);
        });
    }
    
    public void resetTimer(int seconds) {
        this.timer = seconds;
        System.out.println("Timer set to " + seconds + " seconds");
    }
    
    public void tick() {
        if (timer > 0) {
            timer--;
            if (timer == 0) {
                handleEvent("TIMER_EXPIRED");
            }
        }
    }
    
    public int getTimer() {
        return timer;
    }
}