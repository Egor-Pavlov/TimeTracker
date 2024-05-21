package org.example.timetracker.Logging;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * A class that provides a method for converting an object to a string representation.
 */
public class ObjectToString {

    /**
     * Converts an object to a string representation.
     * If the object is null, returns "null".
     * If the object is a collection (List, Set), converts each element to a string
     * and joins them with commas, enclosed in square brackets.
     * If the object is an array, converts each element to a string
     * and joins them with commas, enclosed in square brackets.
     * If the object is neither a collection nor an array, simply calls the object's toString method.
     *
     * @param result the object to convert to a string
     * @return a string representation of the object
     */
    public static String objectToString(Object result) {
        if (result == null) {
            return "null";
        }
        // Проверяем, является ли результат коллекцией (например, List, Set)
        if (result instanceof Collection) {
            // Преобразуем каждый элемент коллекции в строку и объединяем их в строку с помощью Collectors.joining
            return ((Collection<?>) result).stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(", ", "[", "]"));
        }
        // Проверяем, является ли результат массивом
        else if (result.getClass().isArray()) {
            // Преобразуем каждый элемент массива в строку и объединяем их в строку с помощью Collectors.joining
            return Arrays.stream((Object[]) result)
                    .map(Object::toString)
                    .collect(Collectors.joining(", ", "[", "]"));
        }
        // Если результат не коллекция и не массив, просто вызываем метод toString у объекта
        else {
            return result.toString();
        }
    }
}
