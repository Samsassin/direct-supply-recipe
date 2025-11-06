package org.osa.directsupplyrecipe.utils;

/**
 * Utility class for sanitizing input strings into valid JSON arrays.
 * Provides methods to sanitize potentially malformed JSON array strings,
 * remove wrapping code fences, extract bracketed arrays, and validate
 * whether a string conforms to the structure of a JSON array.
 *
 * This class is intended for use in scenarios where the input string
 * may contain additional non-JSON content or formatting artifacts and
 * needs to be processed into a clean JSON array format.
 */
public class JsonArraySanitizer {

    private static final String EMPTY_JSON_ARRAY = "[]";

    /**
     * Private constructor to prevent instantiation of the utility class.
     *
     * This is a utility class that provides methods to sanitize strings into
     * valid JSON array format. The constructor is intentionally private to
     * prevent instantiation, as all methods in this class are static.
     */
    private JsonArraySanitizer() {
        // Utility class; prevent instantiation
    }

    /**
     * Sanitizes an input string and extracts a valid JSON array. If the input is null, malformed,
     * or does not resemble a JSON array, an empty JSON array ("[]") is returned.
     *
     * The method performs the following steps:
     * - Trims unnecessary whitespace from the input string.
     * - Removes optional wrapping code fences, typically marked by triple backticks.
     * - Extracts the first bracketed array structure from the modified input.
     * - Validates whether the extracted string is a valid JSON array structure.
     *
     * @param inputText the input string potentially containing a JSON array. It may include extra
     *                  formatting artifacts like code fences or additional non-JSON content.
     * @return a valid JSON array string if the input contains a valid JSON array, or "[]"
     *         if the input is null, malformed, or does not resemble a JSON array.
     */
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

    /**
     * Removes optional wrapping code fences (triple backticks) from a given string.
     * If the string starts and ends with triple backticks, they are removed along with any
     * surrounding whitespace. If the string does not meet these conditions, it is returned unmodified.
     *
     * @param text the input string that may be wrapped with triple backticks.
     * @return the string with surrounding code fences removed if they exist, otherwise the original string.
     */
    public static String stripWrappingCodeFences(String text) {
        if (text.length() >= 6 && text.startsWith("```") && text.endsWith("```") ) {
            return text.substring(3, text.length() - 3).trim();
        }
        return text;
    }

    /**
     * Extracts the first bracketed array structure from the specified input string.
     * If no bracketed array is found, returns the trimmed version of the input string.
     *
     * This method identifies the first occurrence of a '[' character and the last occurrence
     * of a ']' character in the string. If both are found and the indices are valid (i.e., the
     * ']' comes after the '['), it returns the substring enclosed by these brackets. Otherwise,
     * it returns the trimmed input string as is.
     *
     * @param text the input string potentially containing a bracketed array. This can be any string,
     *             including those without brackets or with improperly paired brackets.
     * @return a substring of the input string containing the first bracketed array if one exists;
     *         otherwise, the trimmed input string.
     */
    public static String extractFirstBracketedArray(String text) {
        int start = text.indexOf('[');
        int end = text.lastIndexOf(']');
        if (start >= 0 && end > start) {
            return text.substring(start, end + 1).trim();
        }
        return text.trim();
    }

    /**
     * Determines whether the given string appears to be a JSON array.
     * A string is considered to resemble a JSON array if it is not null, starts with
     * a '[' character, and ends with a ']' character.
     *
     * @param text the input string to check. It can be any string, including null or empty.
     * @return true if the input string resembles a JSON array by satisfying the criteria; false otherwise.
     */
    public static boolean looksLikeJsonArray(String text) {
        return text != null && text.startsWith("[") && text.endsWith("]");
    }
}