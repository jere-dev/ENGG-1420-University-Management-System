package ca.uoguelph.frontend;

import ca.uoguelph.backend.*;

public class Main {
    public static void main(String[] args) {
        new database("database/UMS_Data.xlsx");
        database.print();
    }
}