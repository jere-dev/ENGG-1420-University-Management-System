package test;

import ca.uoguelph.frontend.SideLoaderApp;

public class SideLoader {
    public static void main(String[] args) {

        SideLoaderApp.fxmlPath = "subject_catalog_user"; // <-- PAGE HERE

        SideLoaderApp.launch(SideLoaderApp.class, args);
    }
}
