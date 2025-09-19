package com.example.state.classic;

public class SelectingProductState implements State {
    @Override
    public void handle(VendingMachine context, String event) {
        switch (event) {
            case "SELECT_PRODUCT":
                if (context.hasProduct()) {
                    context.setState(new DispensingState());
                    context.handle("DISPENSE");
                } else {
                    System.out.println("Product out of stock!");
                    context.setState(new OutOfStockState());
                }
                break;
            case "CANCEL":
                context.refundMoney();
                context.setState(new IdleState());
                break;
            case "INSERT_COIN":
                context.addMoney(25);
                break;
            default:
                System.out.println("Please select a product or cancel");
        }
    }

    @Override
    public String getName() {
        return "SELECTING_PRODUCT";
    }
}