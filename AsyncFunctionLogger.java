import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

public class AsyncFunctionLogger {
    
    /**
     * Wrapper class to hold before and after images
     */
    public static class FunctionImages<T> {
        private final T beforeImage;
        private final T afterImage;
        
        public FunctionImages(T beforeImage, T afterImage) {
            this.beforeImage = beforeImage;
            this.afterImage = afterImage;
        }
        
        public T getBeforeImage() {
            return beforeImage;
        }
        
        public T getAfterImage() {
            return afterImage;
        }
    }
    
    /**
     * Deep copy utility using serialization
     */
    private static <T> T deepCopy(T obj) {
        try {
            // Using ObjectMapper for deep copy
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            String json = mapper.writeValueAsString(obj);
            return mapper.readValue(json, (Class<T>) obj.getClass());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create deep copy", e);
        }
    }
    
    /**
     * Main function that demonstrates the async function logging pattern
     */
    public static <T> FunctionImages<T> logAsyncFunctionExecution(
            T input,
            Supplier<CompletableFuture<T>> asyncFunction) {
        
        // Create deep copy of input for before image
        T beforeImage = deepCopy(input);
        
        // Execute the async function
        CompletableFuture<T> future = asyncFunction.get();
        
        // Wait for all async operations to complete
        T afterImage;
        try {
            afterImage = future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to execute async function", e);
        }
        
        return new FunctionImages<>(beforeImage, afterImage);
    }
    
    /**
     * Example usage of nested async functions
     */
    public static CompletableFuture<String> exampleAsyncFunction(String input) {
        return CompletableFuture.supplyAsync(() -> {
            // Simulate some async operation
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return input + " processed by funcA";
        }).thenCompose(result -> CompletableFuture.supplyAsync(() -> {
            // Simulate another async operation
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return result + " processed by funcB";
        })).thenCompose(result -> CompletableFuture.supplyAsync(() -> {
            // Simulate final async operation
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return result + " processed by funcC";
        }));
    }
    
    /**
     * Example of how to use the logging functionality
     */
    public static void main(String[] args) {
        String input = "Initial data";
        
        FunctionImages<String> images = logAsyncFunctionExecution(
            input,
            () -> exampleAsyncFunction(input)
        );
        
        System.out.println("Before image: " + images.getBeforeImage());
        System.out.println("After image: " + images.getAfterImage());
    }
} 