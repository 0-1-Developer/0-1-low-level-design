package com.example.state.dynamic;

public class DynamicStateDemo {
    public static void main(String[] args) {
        System.out.println("=== Dynamic Registry-Backed State Pattern Demo ===\n");
        
        StateRegistry registry = new StateRegistry();
        
        System.out.println("--- Registering core game states ---");
        registry.registerState("MENU", new GameStates.MenuState());
        registry.registerState("PLAYING", new GameStates.PlayingState());
        registry.registerState("PAUSED", new GameStates.PausedState());
        registry.registerState("GAME_OVER", new GameStates.GameOverState());
        registry.registerState("VICTORY", new GameStates.VictoryState());
        
        registry.registerAlias("MAIN_MENU", "MENU");
        registry.registerAlias("GAMEPLAY", "PLAYING");
        
        registry.listStates();
        
        DynamicContext game = new DynamicContext(registry);
        
        System.out.println("\n--- Starting game flow ---");
        game.initialize("MENU");
        game.handleEvent("START_GAME");
        game.handleEvent("SCORE");
        game.handleEvent("SCORE");
        game.handleEvent("PAUSE");
        game.handleEvent("RESUME");
        game.handleEvent("DIE");
        game.handleEvent("DIE");
        game.handleEvent("DIE");
        game.handleEvent("RETRY");
        game.handleEvent("SCORE");
        game.handleEvent("WIN");
        game.handleEvent("MENU");
        
        game.printStatus();
        
        System.out.println("\n--- Dynamic state addition (MOD support) ---");
        registry.registerState("BONUS_ROUND", new GameStates.CustomModState("BonusLevel", "PLAYING"));
        
        game.handleEvent("START_GAME");
        System.out.println("\nTriggering bonus round...");
        game.transitionTo("BONUS_ROUND");
        game.handleEvent("COLLECT_BONUS");
        game.handleEvent("CONTINUE");
        
        System.out.println("\n--- Runtime state replacement ---");
        System.out.println("Replacing PAUSED state with enhanced version...");
        
        DynamicState enhancedPause = new DynamicState() {
            @Override
            public void enter(DynamicContext context) {
                System.out.println("ENHANCED PAUSE - With screenshot capture!");
                context.setData("screenshot", "screen_" + System.currentTimeMillis() + ".png");
            }
            
            @Override
            public void execute(DynamicContext context, String event) {
                if ("RESUME".equals(event)) {
                    context.transitionTo("PLAYING");
                } else if ("SAVE".equals(event)) {
                    System.out.println("Game saved at pause!");
                    context.setData("save_file", "save_" + System.currentTimeMillis() + ".dat");
                } else {
                    System.out.println("Enhanced pause: " + event);
                }
            }
            
            @Override
            public void exit(DynamicContext context) {
                System.out.println("Deleting screenshot: " + context.getData("screenshot"));
            }
            
            @Override
            public String getName() {
                return "ENHANCED_PAUSE";
            }
        };
        
        registry.replaceState("PAUSED", enhancedPause);
        
        game.handleEvent("PAUSE");
        game.handleEvent("SAVE");
        game.handleEvent("RESUME");
        
        System.out.println("\n--- Handling unknown states (fallback) ---");
        game.transitionTo("UNDEFINED_STATE");
        game.handleEvent("SOME_EVENT");
        
        System.out.println("\n--- Using aliases ---");
        game.transitionTo("MAIN_MENU");
        game.transitionTo("GAMEPLAY");
        
        System.out.println("\n--- State unregistration ---");
        registry.unregisterState("BONUS_ROUND");
        registry.listStates();
        
        game.printStatus();
    }
}