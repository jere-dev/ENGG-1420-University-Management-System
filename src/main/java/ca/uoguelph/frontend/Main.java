package ca.uoguelph.frontend;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        System.out.println(LocalDateTime.now().toString());
        App.launch(App.class, args);
    }
}