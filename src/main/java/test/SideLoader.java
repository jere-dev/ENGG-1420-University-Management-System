package test;

import ca.uoguelph.frontend.SideLoaderApp;

public class SideLoader {
    public static void main(String[] args) {

        SideLoaderApp.fxmlPath = "faculty_profile_faculty"; // <-- PAGE HERE

        SideLoaderApp.launch(SideLoaderApp.class, args);
    }
}
