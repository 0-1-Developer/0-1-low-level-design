package com.example.builder.dsl;

public class DSLBuilderDemo {
    
    public static void main(String[] args) {
        System.out.println("=== DSL (Domain-Specific Language) Builder Demo ===");
        System.out.println("Building SQL queries using readable, domain-focused method names\n");
        
        System.out.println("1. Simple SELECT query:");
        SqlQuery simpleQuery = SqlQuery
            .select("name", "email")
            .from("users")
            .build();
        
        System.out.println("Query: " + simpleQuery);
        System.out.println();
        
        System.out.println("2. Query with WHERE clause:");
        SqlQuery whereQuery = SqlQuery
            .select("*")
            .from("products")
            .where("price > 100")
            .and("category = 'Electronics'")
            .build();
        
        System.out.println("Query: " + whereQuery);
        System.out.println();
        
        System.out.println("3. Complex query with JOIN and ORDER BY:");
        SqlQuery complexQuery = SqlQuery
            .select("u.name", "p.title", "p.price")
            .from("users u")
            .innerJoin("orders o")
            .on("u.id = o.user_id")
            .innerJoin("products p")
            .on("o.product_id = p.id")
            .where("u.active = true")
            .orderBy("p.price")
            .desc()
            .build();
        
        System.out.println("Query: " + complexQuery);
        System.out.println();
        
        System.out.println("4. Query with GROUP BY and HAVING:");
        SqlQuery groupQuery = SqlQuery
            .select("category", "COUNT(*) as total", "AVG(price) as avg_price")
            .from("products")
            .where("active = true")
            .groupBy("category")
            .having("COUNT(*) > 5")
            .orderBy("avg_price")
            .desc()
            .build();
        
        System.out.println("Query: " + groupQuery);
        System.out.println();
        
        System.out.println("5. Query with LIMIT:");
        SqlQuery limitQuery = SqlQuery
            .select("name", "created_at")
            .from("articles")
            .where("published = true")
            .orderBy("created_at")
            .desc()
            .limit(10)
            .build();
        
        System.out.println("Query: " + limitQuery);
        System.out.println();
        
        System.out.println("Key Benefits of DSL Builder:");
        System.out.println("- Highly readable, domain-specific API");
        System.out.println("- Natural language-like construction");
        System.out.println("- IDE auto-completion guides usage");
        System.out.println("- Type safety prevents invalid combinations");
        System.out.println("- Encapsulates domain knowledge");
        System.out.println("- Reduces learning curve for domain experts");
        
        System.out.println("\n=== Demo Complete ===");
    }
}