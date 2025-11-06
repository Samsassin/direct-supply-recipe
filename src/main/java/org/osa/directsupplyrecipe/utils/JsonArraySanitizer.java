package org.osa.directsupplyrecipe.utils;

public class JsonArraySanitizer {

    private static final String EMPTY_JSON_ARRAY = "[]";

    private JsonArraySanitizer() {
        // Utility class; prevent instantiation
    }

    public static String sanitizeToJsonArray(String inputText) {
        if (inputText == null) return EMPTY_JSON_ARRAY;

        String trimmed = inputText.trim();
        String withoutFences = stripWrappingCodeFences(trimmed);
        String arrayOnly = extractFirstBracketedArray(withoutFences);

        if (!looksLikeJsonArray(arrayOnly)) {
            return EMPTY_JSON_ARRAY;
        }
        return arrayOnly;
    }

    public static String stripWrappingCodeFences(String text) {
        if (text.length() >= 6 && text.startsWith("```") && text.endsWith("```") ) {
            return text.substring(3, text.length() - 3).trim();
        }
        return text;
    }

    public static String extractFirstBracketedArray(String text) {
        int start = text.indexOf('[');
        int end = text.lastIndexOf(']');
        if (start >= 0 && end > start) {
            return text.substring(start, end + 1).trim();
        }
        return text.trim();
    }

    public static boolean looksLikeJsonArray(String text) {
        return text != null && text.startsWith("[") && text.endsWith("]");
    }
}