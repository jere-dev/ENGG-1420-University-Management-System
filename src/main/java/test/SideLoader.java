package test;

import javafx.application.Platform;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

class SideLoader {
    private static final String defaultFileName = "subject_catalog_user"; // <-- PAGE HERE

    private static SideLoaderApp loadedApp;
    static SideLoaderApp getLoadedApp() {return loadedApp;}
    static void setLoadedApp(SideLoaderApp loadedApp) {SideLoader.loadedApp = loadedApp;}

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean isRepeat = true;

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

            if (fileName.isEmpty())
                System.out.printf("Opening default file %s.fxml\n", fileName = defaultFileName);
            else
                System.out.printf("Opening alternative file %s.fxml\n", fileName);
                
            try {;
                String finalFileName = fileName;
                Platform.runLater(() -> {
                    loadedApp.loadScene(finalFileName);
                    synchronized (finalFileName) {finalFileName.notify();}
                });

                synchronized (finalFileName) {finalFileName.wait();}
                TimeUnit.SECONDS.sleep(1);
                if (!windowThread.isAlive()) System.exit(1);
            } catch (Exception e) {
                System.err.printf("Error occurred while loading %s.fxml.\n", fileName);
                e.printStackTrace();
            }

        } while (isRepeat);
    }
}
