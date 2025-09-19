package com.example.state.classic;

public class OutOfStockState implements State {
    @Override
    public void handle(VendingMachine context, String event) {
        switch (event) {
            case "INSERT_COIN":
                System.out.println("Machine is out of stock. Money not accepted.");
                break;
            case "SELECT_PRODUCT":
                System.out.println("All products are out of stock");
                break;
            case "CANCEL":
                if (context.getBalance() > 0) {
                    context.refundMoney();
                }
                System.out.println("Machine remains out of stock");
                break;
            case "MAINTENANCE":
                context.setState(new OutOfServiceState());
                break;
            default:
                System.out.println("Machine is out of stock");
        }
    }

    @Override
    public String getName() {
        return "OUT_OF_STOCK";
    }
}