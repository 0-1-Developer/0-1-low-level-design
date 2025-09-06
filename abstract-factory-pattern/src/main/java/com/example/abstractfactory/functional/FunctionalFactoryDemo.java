package com.example.abstractfactory.functional;

import com.example.abstractfactory.common.*;

/**
 * Demonstrates Functional/Lambda Abstract Factory.
 * Shows how to use function composition and lambdas for factory creation.
 */
public class FunctionalFactoryDemo {
    
    private static void testFactory(FunctionalFactory factory) {
        System.out.println("\n=== " + factory.getThemeName() + " ===");
        
        Button button = factory.createButton();
        Checkbox checkbox = factory.createCheckbox();
        ScrollBar scrollBar = factory.createScrollBar();
        
        button.render();
        checkbox.render();
        scrollBar.render();
        
        System.out.println("\nInteractions:");
        button.onClick();
        checkbox.check();
        scrollBar.scrollTo(80);
        
        System.out.println("\nFamily consistency:");
        boolean consistent = button.getStyle().equals(checkbox.getStyle()) &&
                           checkbox.getStyle().equals(scrollBar.getStyle());
        System.out.println("All components from " + button.getStyle() + " family: " + consistent);
    }
    
    private static void demonstrateInlineLambdas() {
        System.out.println("\n=== Inline Lambda Factory ===");
        
        FunctionalFactory customFactory = new FunctionalFactory(
            "Custom Inline",
            () -> new Button() {
                @Override
                public void render() {
                    System.out.println("Custom inline button rendered");
                }
                
                @Override
                public void onClick() {
                    System.out.println("Custom inline button clicked");
                }
                
                @Override
                public String getStyle() {
                    return "Custom";
                }
            },
            () -> new Checkbox() {
                private boolean checked = false;
                
                @Override
                public void render() {
                    System.out.println("Custom inline checkbox rendered");
                }
                
                @Override
                public void check() {
                    checked = true;
                    System.out.println("Custom inline checkbox checked");
                }
                
                @Override
                public void uncheck() {
                    checked = false;
                    System.out.println("Custom inline checkbox unchecked");
                }
                
                @Override
                public boolean isChecked() {
                    return checked;
                }
                
                @Override
                public String getStyle() {
                    return "Custom";
                }
            },
            () -> new ScrollBar() {
                private int position = 0;
                
                @Override
                public void render() {
                    System.out.println("Custom inline scroll bar rendered");
                }
                
                @Override
                public void scrollTo(int position) {
                    this.position = Math.max(0, Math.min(position, 100));
                    System.out.println("Custom inline scroll bar at: " + this.position);
                }
                
                @Override
                public int getPosition() {
                    return position;
                }
                
                @Override
                public String getStyle() {
                    return "Custom";
                }
            }
        );
        
        testFactory(customFactory);
    }
    
    private static void demonstrateMethodReferences() {
        System.out.println("\n=== Method Reference Factory ===");
        
        // Using method references instead of lambdas
        FunctionalFactory.Builder builder = new FunctionalFactory.Builder()
            .withTheme("Method Reference Theme")
            .withButton(MethodReferenceComponents::createButton)
            .withCheckbox(MethodReferenceComponents::createCheckbox)
            .withScrollBar(MethodReferenceComponents::createScrollBar);
        
        FunctionalFactory methodRefFactory = builder.build();
        testFactory(methodRefFactory);
    }
    
    public static void main(String[] args) {
        System.out.println("===== Functional/Lambda Abstract Factory Demo =====");
        
        // Test predefined themes
        testFactory(FunctionalFactory.Themes.createFlatDesign());
        testFactory(FunctionalFactory.Themes.createSkeuomorphic());
        testFactory(FunctionalFactory.Themes.createAccessible());
        
        // Demonstrate inline lambda creation
        demonstrateInlineLambdas();
        
        // Demonstrate method references
        demonstrateMethodReferences();
        
        System.out.println("\n=== Functional Factory Benefits ===");
        System.out.println("- Concise syntax with lambdas");
        System.out.println("- Easy to compose and modify at runtime");
        System.out.println("- No need for extensive class hierarchies");
        System.out.println("- Ideal for simple product implementations");
        
        System.out.println("\n=== Trade-offs ===");
        System.out.println("- Less discoverable than traditional classes");
        System.out.println("- Can become unwieldy with complex logic");
        System.out.println("- Harder to test individual components");
        System.out.println("- Limited IDE support for navigation");
    }
    
    // Helper class for method reference demonstration
    private static class MethodReferenceComponents {
        static Button createButton() {
            return new Button() {
                @Override
                public void render() {
                    System.out.println("Method reference button rendered");
                }
                
                @Override
                public void onClick() {
                    System.out.println("Method reference button clicked");
                }
                
                @Override
                public String getStyle() {
                    return "MethodRef";
                }
            };
        }
        
        static Checkbox createCheckbox() {
            return new Checkbox() {
                private boolean checked = false;
                
                @Override
                public void render() {
                    System.out.println("Method reference checkbox rendered");
                }
                
                @Override
                public void check() {
                    checked = true;
                    System.out.println("Method reference checkbox checked");
                }
                
                @Override
                public void uncheck() {
                    checked = false;
                    System.out.println("Method reference checkbox unchecked");
                }
                
                @Override
                public boolean isChecked() {
                    return checked;
                }
                
                @Override
                public String getStyle() {
                    return "MethodRef";
                }
            };
        }
        
        static ScrollBar createScrollBar() {
            return new ScrollBar() {
                private int position = 0;
                
                @Override
                public void render() {
                    System.out.println("Method reference scroll bar rendered");
                }
                
                @Override
                public void scrollTo(int position) {
                    this.position = Math.max(0, Math.min(position, 100));
                    System.out.println("Method reference scroll bar at: " + this.position);
                }
                
                @Override
                public int getPosition() {
                    return position;
                }
                
                @Override
                public String getStyle() {
                    return "MethodRef";
                }
            };
        }
    }
}