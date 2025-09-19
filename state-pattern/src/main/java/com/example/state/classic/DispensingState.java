package com.example.state.classic;

public class DispensingState implements State {
    @Override
    public void handle(VendingMachine context, String event) {
        switch (event) {
            case "DISPENSE":
                context.dispenseProduct();
                if (context.hasProduct()) {
                    context.setState(new IdleState());
                } else {
                    context.setState(new OutOfStockState());
                }
                break;
            default:
                System.out.println("Dispensing in progress, please wait");
        }
    }

    @Override
    public String getName() {
        return "DISPENSING";
    }
}