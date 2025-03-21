package test;

import ca.uoguelph.backend.*;
import ca.uoguelph.backend.login.LoginManager;
import ca.uoguelph.backend.login.LoginState;
import ca.uoguelph.frontend.objects.DisplayError;
import javafx.application.Platform;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

class SideLoader {
    private static final String defaultFileName = "event_calendar.fxml"; // <-- PAGE HERE
    private static final LoginState defaultLoginState = LoginState.ADMIN; // <-- PERMISSION HERE

    private static SideLoaderApp loadedApp;
    static SideLoaderApp getLoadedApp() {return loadedApp;}
    static void setLoadedApp(SideLoaderApp loadedApp) {SideLoader.loadedApp = loadedApp;}

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean isRepeat = true;

        Database.loadExcelSheet(SideLoader.class.getResource("/database/UMS_Data.xlsx").getPath());
        SubjectManager.loadSubjects();
        CourseManager.loadCourses();
        StudentManager.loadStudents();
        FacultyManager.loadFaculty();
        EventManager.loadCourses();

        String login = switch (defaultLoginState) {
            case ADMIN -> "admin";
            case STUDENT -> "student";
            case FACULTY -> "faculty";
        };
        LoginManager.addUser(new Admin("admin", "admin", "Test@uoguelph.ca", "test", "default"));
        LoginManager.addUser(new Student("student", "student", "123","123","Test@uoguelph.ca",
                "test", "default","default",new ArrayList<>(),"",0,"student"));
        LoginManager.addUser(new Faculty("faculty", "faculty", "default","default",
                "Test@uoguelph.ca", "test",new ArrayList<>(), "faculty","default"));
        LoginManager.login(login, login);

        Thread windowThread = new Thread(() -> SideLoaderApp.launch(SideLoaderApp.class, args));
        windowThread.setDaemon(true);
        windowThread.start();

        try {TimeUnit.SECONDS.sleep(1);} catch (Exception ignored) {}
        do {
            System.out.println("Name of file to open (enter for default file, cancel to quit)");
            String fileName = sc.nextLine();

            if (fileName.equals("cancel") || fileName.equals("c")) {
                isRepeat = false;
                continue;
            }
            File savedFile;
            String savedFileName = "";
            try {
                savedFile = new File(Objects.requireNonNull(SideLoader.class.getResource("/testresource/testpath.txt")).getPath());
                if (new Scanner(savedFile).hasNextLine()) savedFileName = new Scanner(savedFile).nextLine();
            } catch (FileNotFoundException e) {
                DisplayError.log.error("testing path not found\n", e);
                break;
            }

            try {
                if (!fileName.isEmpty()){
                    System.out.printf("Opening and saving alternative file %s\n", fileName);

                    PrintWriter pw = new PrintWriter(savedFile);
                    pw.println(fileName);
                    pw.close();
                } else if (!savedFileName.isEmpty()) {
                    System.out.printf("Opening saved file %s\n", fileName = savedFileName);
                } else {
                    System.out.printf("Opening and saving default file %s\n", fileName = defaultFileName);
                }

                String finalFileName = fileName;
                Platform.runLater(() -> {
                    loadedApp.loadScene(finalFileName);
                    synchronized (finalFileName) {finalFileName.notify();}
                });

                synchronized (finalFileName) {finalFileName.wait();}
                TimeUnit.SECONDS.sleep(1);
                if (!windowThread.isAlive()) System.exit(1);
            } catch (Exception e) {
                System.err.printf("Error occurred while loading %s.\n", fileName);
                e.printStackTrace();
            }

        } while (isRepeat);
    }
}
