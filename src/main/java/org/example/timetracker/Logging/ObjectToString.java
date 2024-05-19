package org.example.timetracker.Logging;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class ObjectToString {
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
