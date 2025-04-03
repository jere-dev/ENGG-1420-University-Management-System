package ca.uoguelph.frontend.objects.section;

import javafx.util.StringConverter;

import java.util.regex.Pattern;

/**
 * @author 180Sai
 */
public class SectionCodeConverter extends StringConverter<String> {

    @Override
    public String toString(String s) {
        if (Pattern.matches(s, "[0-9]{1,4}"))
            return s;
        return null;
    }

    @Override
    public String fromString(String s) {
        return s;
    }
}
