package ca.uoguelph.frontend.objects.section;

import javafx.util.StringConverter;

import java.util.regex.Pattern;

/**
 * @author 180Sai
 */
public class DateConverter extends StringConverter<String> {
    @Override
    public String toString(String s) {
        System.out.println("Outgoing: " + s);

        return s;
    }

    @Override
    public String fromString(String s) {
        System.out.println("Incoming: " + s);
        return Pattern.matches(s, "(([0-9]*? \\/ ){0,2})[0-9]*") ? s : "";
    }
}
