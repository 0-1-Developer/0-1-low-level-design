package com.example.state.dynamic;

public class GameStates {
    
    public static class MenuState implements DynamicState {
        @Override
        public void enter(DynamicContext context) {
            System.out.println("Entering MENU - Display main menu");
            context.setData("music", "menu_theme.mp3");
        }
        
        @Override
        public void execute(DynamicContext context, String event) {
            switch (event) {
                case "START_GAME":
                    context.transitionTo("PLAYING");
                    break;
                case "OPTIONS":
                    context.transitionTo("OPTIONS");
                    break;
                case "QUIT":
                    context.transitionTo("EXIT");
                    break;
                default:
                    System.out.println("Unknown menu option: " + event);
            }
        }
        
        @Override
        public void exit(DynamicContext context) {
            System.out.println("Leaving menu");
        }
        
        @Override
        public String getName() {
            return "MENU";
        }
    }
    
    public static class PlayingState implements DynamicState {
        @Override
        public void enter(DynamicContext context) {
            System.out.println("Starting game - Loading level");
            context.setData("score", 0);
            context.setData("lives", 3);
            context.setData("music", "game_theme.mp3");
        }
        
        @Override
        public void execute(DynamicContext context, String event) {
            switch (event) {
                case "PAUSE":
                    context.transitionTo("PAUSED");
                    break;
                case "SCORE":
                    Integer score = context.getData("score", Integer.class);
                    context.setData("score", score + 10);
                    System.out.println("Score: " + context.getData("score"));
                    break;
                case "DIE":
                    Integer lives = context.getData("lives", Integer.class);
                    lives--;
                    context.setData("lives", lives);
                    System.out.println("Lives remaining: " + lives);
                    if (lives <= 0) {
                        context.transitionTo("GAME_OVER");
                    }
                    break;
                case "WIN":
                    context.transitionTo("VICTORY");
                    break;
                default:
                    System.out.println("Game action: " + event);
            }
        }
        
        @Override
        public void exit(DynamicContext context) {
            System.out.println("Exiting gameplay");
        }
        
        @Override
        public String getName() {
            return "PLAYING";
        }
    }
    
    public static class PausedState implements DynamicState {
        @Override
        public void enter(DynamicContext context) {
            System.out.println("Game PAUSED - Display pause menu");
            context.setData("music", "pause_ambience.mp3");
        }
        
        @Override
        public void execute(DynamicContext context, String event) {
            switch (event) {
                case "RESUME":
                    context.transitionTo("PLAYING");
                    break;
                case "MENU":
                    context.transitionTo("MENU");
                    break;
                case "QUIT":
                    context.transitionTo("EXIT");
                    break;
                default:
                    System.out.println("Invalid pause menu option: " + event);
            }
        }
        
        @Override
        public void exit(DynamicContext context) {
            System.out.println("Unpausing game");
        }
        
        @Override
        public String getName() {
            return "PAUSED";
        }
    }
    
    public static class GameOverState implements DynamicState {
        @Override
        public void enter(DynamicContext context) {
            System.out.println("GAME OVER! Final score: " + context.getData("score"));
            context.setData("music", "game_over.mp3");
        }
        
        @Override
        public void execute(DynamicContext context, String event) {
            switch (event) {
                case "RETRY":
                    context.transitionTo("PLAYING");
                    break;
                case "MENU":
                    context.transitionTo("MENU");
                    break;
                default:
                    System.out.println("Game over screen: " + event);
            }
        }
        
        @Override
        public void exit(DynamicContext context) {
            System.out.println("Leaving game over screen");
        }
        
        @Override
        public String getName() {
            return "GAME_OVER";
        }
    }
    
    public static class VictoryState implements DynamicState {
        @Override
        public void enter(DynamicContext context) {
            System.out.println("VICTORY! You won! Score: " + context.getData("score"));
            context.setData("music", "victory_fanfare.mp3");
        }
        
        @Override
        public void execute(DynamicContext context, String event) {
            switch (event) {
                case "NEXT_LEVEL":
                    System.out.println("Loading next level...");
                    context.transitionTo("PLAYING");
                    break;
                case "MENU":
                    context.transitionTo("MENU");
                    break;
                default:
                    System.out.println("Victory screen: " + event);
            }
        }
        
        @Override
        public void exit(DynamicContext context) {
            System.out.println("Leaving victory screen");
        }
        
        @Override
        public String getName() {
            return "VICTORY";
        }
    }
    
    public static class CustomModState implements DynamicState {
        private final String modName;
        private final String nextState;
        
        public CustomModState(String modName, String nextState) {
            this.modName = modName;
            this.nextState = nextState;
        }
        
        @Override
        public void enter(DynamicContext context) {
            System.out.println("Custom mod state: " + modName + " activated!");
            context.setData("mod_active", modName);
        }
        
        @Override
        public void execute(DynamicContext context, String event) {
            if ("CONTINUE".equals(event)) {
                context.transitionTo(nextState);
            } else {
                System.out.println("Mod " + modName + " handling: " + event);
            }
        }
        
        @Override
        public void exit(DynamicContext context) {
            System.out.println("Deactivating mod: " + modName);
        }
        
        @Override
        public String getName() {
            return "MOD_" + modName.toUpperCase();
        }
    }
}