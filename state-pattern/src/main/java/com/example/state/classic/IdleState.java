package com.example.state.classic;

public class IdleState implements State {
    @Override
    public void handle(VendingMachine context, String event) {
        switch (event) {
            case "INSERT_COIN":
                context.setState(new AcceptingMoneyState());
                context.handle(event);
                break;
            case "SELECT_PRODUCT":
                System.out.println("Please insert money first");
                break;
            case "CANCEL":
                System.out.println("Nothing to cancel");
                break;
            case "MAINTENANCE":
                context.setState(new OutOfServiceState());
                break;
            default:
                System.out.println("Invalid action in idle state");
        }
    }

    @Override
    public String getName() {
        return "IDLE";
    }
}