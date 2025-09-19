package com.example.state.classic;

public class OutOfServiceState implements State {
    @Override
    public void handle(VendingMachine context, String event) {
        switch (event) {
            case "MAINTENANCE":
                System.out.println("Maintenance completed, returning to idle");
                context.setState(new IdleState());
                break;
            default:
                System.out.println("Machine is out of service. Maintenance required.");
        }
    }

    @Override
    public String getName() {
        return "OUT_OF_SERVICE";
    }
}