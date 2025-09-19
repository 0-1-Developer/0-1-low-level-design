package com.example.state.classic;

public class AcceptingMoneyState implements State {
    @Override
    public void handle(VendingMachine context, String event) {
        switch (event) {
            case "INSERT_COIN":
                context.addMoney(25);
                if (context.hasEnoughBalance()) {
                    context.setState(new SelectingProductState());
                }
                break;
            case "SELECT_PRODUCT":
                if (context.hasEnoughBalance()) {
                    context.setState(new SelectingProductState());
                    context.handle(event);
                } else {
                    System.out.println("Insufficient balance. Need: " + context.getProductPrice() + 
                                     ", Have: " + context.getBalance());
                }
                break;
            case "CANCEL":
                context.refundMoney();
                context.setState(new IdleState());
                break;
            default:
                System.out.println("Invalid action while accepting money");
        }
    }

    @Override
    public String getName() {
        return "ACCEPTING_MONEY";
    }
}