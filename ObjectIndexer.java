import java.lang.reflect.Field;
import java.util.*;

public class ObjectIndexer {
    
    /**
     * Sets default values for all members of an object and its nested objects
     * @param obj The object to process
     * @return The same object with default values set
     */
    public static <T> T setDefaultValues(T obj) {
        if (obj == null) {
            return null;
        }

        try {
            Class<?> objectClass = obj.getClass();
            
            // Process all fields of the object
            for (Field field : objectClass.getDeclaredFields()) {
                field.setAccessible(true);
                
                try {
                    // Get the current value
                    Object currentValue = field.get(obj);
                    
                    // If the field is null, create a new instance
                    if (currentValue == null) {
                        Class<?> fieldType = field.getType();
                        if (!isPrimitiveOrWrapper(fieldType) && !fieldType.equals(String.class)) {
                            // Create new instance for complex objects
                            Object newInstance = fieldType.getDeclaredConstructor().newInstance();
                            field.set(obj, newInstance);
                            // Recursively set default values for the new instance
                            setDefaultValues(newInstance);
                        } else {
                            // Set default value for primitive types and String
                            field.set(obj, getDefaultValue(fieldType));
                        }
                    } else {
                        // If the field is not null, process it recursively if it's a complex object
                        Class<?> fieldType = field.getType();
                        if (!isPrimitiveOrWrapper(fieldType) && !fieldType.equals(String.class)) {
                            setDefaultValues(currentValue);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error processing field: " + field.getName());
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.err.println("Error processing object: " + obj.getClass().getName());
            e.printStackTrace();
        }
        
        return obj;
    }
    
    /**
     * Get default value for a given type
     */
    private static Object getDefaultValue(Class<?> type) {
        if (type.equals(boolean.class) || type.equals(Boolean.class)) {
            return false;
        } else if (type.equals(byte.class) || type.equals(Byte.class)) {
            return (byte) 0;
        } else if (type.equals(short.class) || type.equals(Short.class)) {
            return (short) 0;
        } else if (type.equals(int.class) || type.equals(Integer.class)) {
            return 0;
        } else if (type.equals(long.class) || type.equals(Long.class)) {
            return 0L;
        } else if (type.equals(float.class) || type.equals(Float.class)) {
            return 0.0f;
        } else if (type.equals(double.class) || type.equals(Double.class)) {
            return 0.0;
        } else if (type.equals(char.class) || type.equals(Character.class)) {
            return '\u0000';
        } else if (type.equals(String.class)) {
            return "";
        } else {
            return null;
        }
    }
    
    /**
     * Check if a type is primitive or wrapper
     */
    private static boolean isPrimitiveOrWrapper(Class<?> type) {
        return type.isPrimitive() || 
               type.equals(Boolean.class) ||
               type.equals(Byte.class) ||
               type.equals(Short.class) ||
               type.equals(Integer.class) ||
               type.equals(Long.class) ||
               type.equals(Float.class) ||
               type.equals(Double.class) ||
               type.equals(Character.class);
    }
} 