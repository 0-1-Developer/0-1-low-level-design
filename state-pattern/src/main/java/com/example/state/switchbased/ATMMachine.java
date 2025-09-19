package com.example.state.switchbased;

public class ATMMachine {
    private static final int STATE_NO_CARD = 0;
    private static final int STATE_CARD_INSERTED = 1;
    private static final int STATE_PIN_ENTERED = 2;
    private static final int STATE_TRANSACTION = 3;
    private static final int STATE_ERROR = 4;
    
    private int currentState;
    private String cardNumber;
    private boolean pinValid;
    private double balance;
    private int pinAttempts;
    
    public ATMMachine() {
        this.currentState = STATE_NO_CARD;
        this.balance = 1000.0;
        this.pinAttempts = 0;
    }
    
    public void insertCard(String cardNumber) {
        switch (currentState) {
            case STATE_NO_CARD:
                this.cardNumber = cardNumber;
                currentState = STATE_CARD_INSERTED;
                System.out.println("Card inserted: " + maskCardNumber(cardNumber));
                System.out.println("Please enter PIN");
                break;
            case STATE_CARD_INSERTED:
            case STATE_PIN_ENTERED:
            case STATE_TRANSACTION:
                System.out.println("Card already inserted");
                break;
            case STATE_ERROR:
                System.out.println("ATM in error state - please contact support");
                break;
        }
    }
    
    public void enterPin(String pin) {
        switch (currentState) {
            case STATE_NO_CARD:
                System.out.println("Please insert card first");
                break;
            case STATE_CARD_INSERTED:
                if (validatePin(pin)) {
                    pinValid = true;
                    currentState = STATE_PIN_ENTERED;
                    pinAttempts = 0;
                    System.out.println("PIN accepted. Select transaction:");
                    showMenu();
                } else {
                    pinAttempts++;
                    if (pinAttempts >= 3) {
                        currentState = STATE_ERROR;
                        System.out.println("Too many incorrect attempts. Card blocked!");
                    } else {
                        System.out.println("Incorrect PIN. Attempts remaining: " + (3 - pinAttempts));
                    }
                }
                break;
            case STATE_PIN_ENTERED:
            case STATE_TRANSACTION:
                System.out.println("PIN already entered");
                break;
            case STATE_ERROR:
                System.out.println("Card is blocked");
                break;
        }
    }
    
    public void selectTransaction(String transaction) {
        switch (currentState) {
            case STATE_NO_CARD:
                System.out.println("Please insert card first");
                break;
            case STATE_CARD_INSERTED:
                System.out.println("Please enter PIN first");
                break;
            case STATE_PIN_ENTERED:
            case STATE_TRANSACTION:
                currentState = STATE_TRANSACTION;
                processTransaction(transaction);
                break;
            case STATE_ERROR:
                System.out.println("Cannot process transaction - card blocked");
                break;
        }
    }
    
    private void processTransaction(String transaction) {
        switch (transaction.toUpperCase()) {
            case "BALANCE":
                System.out.println("Your balance is: $" + balance);
                showMenu();
                break;
            case "WITHDRAW":
                withdraw(100);
                showMenu();
                break;
            case "DEPOSIT":
                deposit(50);
                showMenu();
                break;
            case "EXIT":
                ejectCard();
                break;
            default:
                System.out.println("Invalid transaction: " + transaction);
                showMenu();
        }
    }
    
    private void withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            System.out.println("Withdrawn: $" + amount);
            System.out.println("New balance: $" + balance);
        } else {
            System.out.println("Insufficient funds");
        }
    }
    
    private void deposit(double amount) {
        balance += amount;
        System.out.println("Deposited: $" + amount);
        System.out.println("New balance: $" + balance);
    }
    
    public void ejectCard() {
        switch (currentState) {
            case STATE_NO_CARD:
                System.out.println("No card to eject");
                break;
            case STATE_CARD_INSERTED:
            case STATE_PIN_ENTERED:
            case STATE_TRANSACTION:
                System.out.println("Card ejected. Thank you!");
                reset();
                break;
            case STATE_ERROR:
                System.out.println("Card retained due to security");
                reset();
                break;
        }
    }
    
    private void reset() {
        currentState = STATE_NO_CARD;
        cardNumber = null;
        pinValid = false;
        pinAttempts = 0;
    }
    
    private boolean validatePin(String pin) {
        return "1234".equals(pin);
    }
    
    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }
    
    private void showMenu() {
        System.out.println("\nAvailable transactions:");
        System.out.println("- BALANCE: Check balance");
        System.out.println("- WITHDRAW: Withdraw $100");
        System.out.println("- DEPOSIT: Deposit $50");
        System.out.println("- EXIT: Return card");
    }
    
    public String getCurrentStateName() {
        switch (currentState) {
            case STATE_NO_CARD: return "NO_CARD";
            case STATE_CARD_INSERTED: return "CARD_INSERTED";
            case STATE_PIN_ENTERED: return "PIN_ENTERED";
            case STATE_TRANSACTION: return "TRANSACTION";
            case STATE_ERROR: return "ERROR";
            default: return "UNKNOWN";
        }
    }
    
    public void printStatus() {
        System.out.println("\n=== ATM Status ===");
        System.out.println("State: " + getCurrentStateName());
        System.out.println("Card: " + (cardNumber != null ? maskCardNumber(cardNumber) : "None"));
        System.out.println("PIN Valid: " + pinValid);
        System.out.println("Balance: $" + balance);
    }
}