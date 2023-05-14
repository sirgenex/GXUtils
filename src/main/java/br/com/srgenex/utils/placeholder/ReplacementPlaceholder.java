package br.com.srgenex.utils.placeholder;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@Getter
public class ReplacementPlaceholder {

    private final String placeholder, replacement;

    public ReplacementPlaceholder(Object placeholder, Object replacement) {
        this.placeholder = String.valueOf(placeholder);
        this.replacement = String.valueOf(replacement);
    }

    public ReplacementPlaceholder(String placeholder, String replacement) {
        this.placeholder = placeholder;
        this.replacement = replacement;
    }

    public String replace(String line){
        return line.replace(placeholder, replacement);
    }

    public static List<ReplacementPlaceholder> fromStringList(List<String> replacements) {
        List<ReplacementPlaceholder> placeholders = new ArrayList<>();
        for (String replacement : replacements) {
            String previous = replacement.split(";")[0];
            String next = replacement.split(";")[1];
            placeholders.add(new ReplacementPlaceholder(previous, next));
        }
        return placeholders;
    }

    public static List<ReplacementPlaceholder> fromStringArray(String... replacements) {
        List<ReplacementPlaceholder> placeholders = new ArrayList<>();
        for (String replacement : replacements) {
            placeholders.add(fromString(replacement));
        }
        return placeholders;
    }

    public static ReplacementPlaceholder fromString(String replacement) {
        String previous = replacement.split(";")[0];
        String next = replacement.split(";")[1];
        return new ReplacementPlaceholder(previous, next);
    }

    public static String[] asStringArray(List<ReplacementPlaceholder> replacements){
        List<String> placeholders = new ArrayList<>();
        for (ReplacementPlaceholder replacement : replacements) placeholders.add(replacement.getPlaceholder()+", "+replacement.getReplacement());
        return (String[]) placeholders.toArray();
    }

}
