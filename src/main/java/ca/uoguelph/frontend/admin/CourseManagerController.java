package ca.uoguelph.frontend.admin;
 
 import ca.uoguelph.backend.Course;
 import ca.uoguelph.backend.CourseManager;
 import ca.uoguelph.frontend.objects.DisplayError;
 import ca.uoguelph.frontend.objects.controller.AbstractAdminListController;
 import ca.uoguelph.frontend.objects.table.ScaledTable;
 import ca.uoguelph.frontend.objects.table.row.HeaderRowPreset;
 import ca.uoguelph.frontend.objects.table.row.TableRow;
 import ca.uoguelph.frontend.objects.table.row.TableRowPreset;
 import javafx.fxml.FXML;
 import javafx.fxml.FXMLLoader;
 import javafx.scene.Node;
 import javafx.scene.Parent;
 import javafx.scene.control.*;
 import javafx.event.ActionEvent;
 import javafx.scene.input.KeyCode;
 import javafx.scene.input.KeyEvent;
 import javafx.scene.layout.Priority;
 import javafx.scene.layout.StackPane;
 import javafx.scene.layout.VBox;
 
 import java.rmi.NoSuchObjectException;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.List;
 import java.util.regex.Pattern;
 
 // TODO: add tabs to open multiple courses at once?
 public final class CourseManagerController extends AbstractAdminListController implements DisplayError {
     private final List<Course> courseList = new ArrayList<>();
     private final HashMap<TableRow, Course> rowCourseMap = new HashMap<>();
     private int page = 0;
     private int pageRowCount = 40;
 
     @FXML private Button leftPageButton, rightPageButton;
     @FXML private TextField pageText, rowCountText;
     @FXML private VBox screenBox;
     @FXML private Label errorLabel;
 
     @FXML private TextField searchField;
 
     @FXML private ScrollPane scrollPane;
     private ScaledTable table;
 
     @FXML
     public void initialize() {
         table = new ScaledTable(scrollPane, 5);
 
         // Set column widths with updated proportions
         table.setPercentWidth(0, 20); // Subject code
         table.setPercentWidth(1, 12); // Course code  
         table.setPercentWidth(2, 46); // Title
         table.setPercentWidth(3, 20); // Credits
         table.setPercentWidth(4, 10); // Edit button
 
         // Set styles for better visual appearance
         scrollPane.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #dddddd; -fx-border-radius: 4;");
 
         // Set grow priorities
         table.setHGrow(2, Priority.ALWAYS);
         table.setHGrow(3, Priority.SOMETIMES);
 
         courseList.addAll(CourseManager.getCourses());
 
         page = 0;
         updateTable("");
 
         // dynamic searching
         searchField.textProperty().addListener(
                 (obs, old, newText) -> updateTable(newText));
     }
 
     @Override
     protected void updateTable(String search) {
         courseList.clear();
         page = 0;
 
         if (search.isEmpty())
             courseList.addAll(CourseManager.getCourses());
         else
             courseList.addAll(CourseManager.searchCoursesByTitle(search));
 
         loadTable();
     }
 
     private void loadTable() {
         rowCourseMap.clear();
         table.clear();
 
         updatePageButtons();
 
         table.addRow(HeaderRowPreset.COURSE.getPreset());
 
         for (int i = page * pageRowCount; i < Math.min((page+1) * pageRowCount, courseList.size()); i++) {
             Course c = courseList.get(i);
 
             TableRow cr = new TableRowPreset(c);
 
             // manage hyperlinks
             Hyperlink editLink = (Hyperlink) cr.get(4);
             editLink.setOnAction(this::handleEdit);
 
             rowCourseMap.put(cr, c);
             table.addRow(cr);
         }
 
         if (scrollPane.getVvalue() == 1.0)
             scrollPane.setVvalue(0.0);
     }
 
     private void updatePageButtons() {
         boolean leftDisable = page <= 0;
         boolean rightDisable = (page + 1) * pageRowCount >= courseList.size();
 
         leftPageButton.setDisable(leftDisable);
         rightPageButton.setDisable(rightDisable);
         pageText.setPromptText(String.valueOf(page + 1));
         rowCountText.setPromptText(pageRowCount + " courses per page");
     }
 
     @Override
     protected void handleSearch(ActionEvent event) {updateTable(searchField.getText());}
 
     @FXML @Override
     protected void handleAdd(ActionEvent event) {handleLoadEditor(event);}
 
     @FXML
     private void handleNextPage(ActionEvent ignoredEvent) {
         page++; loadTable();
     }
 
     @FXML
     private void handlePrevPage(ActionEvent ignoredEvent) {
         page--; loadTable();
     }
 
     @FXML
     private void handleKeyPage(KeyEvent event) {
         System.out.println(event.getCode());
         if (event.getCode() != KeyCode.ENTER) return;
 
         String userText = pageText.getText();
         pageText.setPromptText(pageText.getText());
         pageText.setText("");
         screenBox.requestFocus();
 
         if (userText.equals(String.valueOf(page))) return;
 
         if (errorIf("Page not valid", () -> !Pattern.matches("[0-9]+", userText) ||
                 errorIf("Page does not exist", () -> (Integer.parseInt(userText)-1) * pageRowCount >= courseList.size() ||
                         Integer.parseInt(userText) < 0))) {
             pageText.setPromptText(String.valueOf(page));
             return;
         }
 
         try {Thread.sleep(300);} catch (Exception ignored) {
             System.err.println("CourseManager:158: thread-sleep failed\n-----");
         }
 
         page = Integer.parseInt(userText) - 1;
         loadTable();
     }
 
     @FXML
     private void handleKeyCount(KeyEvent event) {
         if (event.getCode() != KeyCode.ENTER) return;
 
         String text = rowCountText.getText();
         rowCountText.setText("");
         screenBox.requestFocus();
 
         if (errorIf("Display count not valid", () -> !Pattern.matches("[0-9]+", text))
                 || errorIf("Display count too small", () -> Integer.parseInt(text) <= 5))
             return;
 
         try {Thread.sleep(300);} catch (Exception ignored) {
             System.err.println("CourseManager:178: thread-sleep failed\n-----");
         }
 
         page = Math.min(page * pageRowCount / Integer.parseInt(text), courseList.size() - 1);
         pageRowCount = Integer.parseInt(text);
         loadTable();
     }
 
     @Override
     public void displayError(String err) {
         displayShortError(err, errorLabel, 2.0);
     }
 
     @FXML @Override
     protected void handleEdit(ActionEvent event) {
         if (!(event.getSource() instanceof Node))
             throw new IllegalArgumentException("Source of event is object: " + event.getSource().getClass());
 
         TableRow sourceR = table.getRow((Node) event.getSource());
         handleLoadEditor(event, rowCourseMap.get(sourceR));
     }
 
     @Override
     protected void handleLoadEditor(ActionEvent event) {
         handleLoadEditor(event, null);
     }
 
     private void handleLoadEditor(ActionEvent event, Course c) {
         try {
             FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/fxml/admin/course_editor.fxml"));
             Parent content = loader.load();
 
             StackPane contentArea = null;
             Parent parent = ((Node) event.getSource()).getParent();
             while (parent != null) {
                 if (parent.getId() != null && parent.getId().equals("contentArea")) {
                     contentArea = (StackPane) parent;
                     break;
                 }
                 parent = parent.getParent();
             }
             if (contentArea == null) throw new NoSuchObjectException("Could not find parent StackPane");
 
             CourseEditorController controller = loader.getController();
 //            controller.loadCourse(c);
             controller.setParentAndRow(this, 0);
 //            controller.setParentAndRow(this, 0);
 
             contentArea.getChildren().clear();
             contentArea.getChildren().add(content);
         } catch (Exception e) {
             displayError(e.getClass().getName() + ": see terminal for more information");
             log.error(e);
         }
     }
 
 //    @FXML private GridPane tableGrid;
 //
 //    @FXML
 //    public void initialize() {
 //        // Clear any existing constraints to avoid duplication
 //        tableGrid.getColumnConstraints().clear();
 //        tableGrid.getRowConstraints().clear();
 //
 //        // Ensure GridPane fills its ScrollPane
 //        tableGrid.setMaxWidth(Double.MAX_VALUE);
 //        tableGrid.setMaxHeight(Double.MAX_VALUE);
 //
 //        // Set up column constraints to allow dynamic resizing
 //        int totalColumns = 9; // 8 data columns + 1 button column
 //        for (int i = 0; i < totalColumns; i++) {
 //            ColumnConstraints col = new ColumnConstraints();
 //            col.setMinWidth(100); // Minimum width to ensure readability
 //            col.setHgrow(Priority.ALWAYS);
 //            tableGrid.getColumnConstraints().add(col);
 //        }
 //        System.out.println("Columns set: " + tableGrid.getColumnConstraints().size());
 //
 //        // Populate the GridPane
 //        for (int i = 0; i < courseEntries.length; i++) {
 //            tableGrid.addRow(i + 1); // Start at row 1 like example
 //
 //            // Set row constraint to allow dynamic height
 //            RowConstraints row = new RowConstraints();
 //            row.setMinHeight(30);
 //            row.setPrefHeight(60);
 //            row.setVgrow(Priority.ALWAYS);
 //            tableGrid.getRowConstraints().add(row);
 //
 //            // Create TextAreas for each field
 //            TextArea courseCode = new TextArea(courseEntries[i].courseCode());
 //            TextArea sectionNumber = new TextArea(courseEntries[i].sectionNumber());
 //            TextArea professorName = new TextArea(courseEntries[i].professorName());
 //            TextArea capacity = new TextArea(courseEntries[i].capacity());
 //            TextArea lectureTime = new TextArea(courseEntries[i].lectureTime());
 //            TextArea lectureLocation = new TextArea(courseEntries[i].lectureLocation());
 //            TextArea examDate = new TextArea(courseEntries[i].examDate());
 //            TextArea examLocation = new TextArea(courseEntries[i].examLocation());
 //            Button editButton = new Button("✎");
 //
 //            // Add to GridPane
 //            tableGrid.add(courseCode, 0, i + 1);
 //            tableGrid.add(sectionNumber, 1, i + 1);
 //            tableGrid.add(professorName, 2, i + 1);
 //            tableGrid.add(capacity, 3, i + 1);
 //            tableGrid.add(lectureTime, 4, i + 1);
 //            tableGrid.add(lectureLocation, 5, i + 1);
 //            tableGrid.add(examDate, 6, i + 1);
 //            tableGrid.add(examLocation, 7, i + 1);
 //            tableGrid.add(editButton, 8, i + 1);
 //
 //            // Configure TextAreas to prevent overlap
 //            configureTextArea(courseCode);
 //            configureTextArea(sectionNumber);
 //            configureTextArea(professorName);
 //            configureTextArea(capacity);
 //            configureTextArea(lectureTime);
 //            configureTextArea(lectureLocation);
 //            configureTextArea(examDate);
 //            configureTextArea(examLocation);
 //
 //            // Button constraints
 //            GridPane.setHgrow(editButton, Priority.NEVER);
 //            GridPane.setVgrow(editButton, Priority.NEVER);
 //            GridPane.setFillWidth(editButton, false);
 //            GridPane.setFillHeight(editButton, false);
 //            GridPane.setMargin(editButton, new Insets(5));
 //            editButton.setPadding(new Insets(5, 10, 5, 10));
 //            editButton.setStyle("-fx-font-size: 14px;");
 //
 //            // Attach edit handler
 //            editButton.setOnAction(event -> handleEditCourse(event));
 //        }
 //    }
 //
 //    private void configureTextArea(TextArea textArea) {
 //        textArea.setWrapText(false);
 //        textArea.setStyle("-fx-font-size: 14px; -fx-vbar-policy: never; -fx-hbar-policy: never;");
 //        textArea.setPrefRowCount(1);
 //        textArea.setPrefColumnCount(1);
 //        textArea.textProperty().addListener((obs, oldText, newText) -> {
 //            double textWidth = computeTextWidth(newText, textArea.getFont());
 //            textArea.setPrefColumnCount((int) (textWidth / 7) + 1);
 //            textArea.setPrefWidth(textWidth + 20);
 //        });
 //        GridPane.setHgrow(textArea, Priority.ALWAYS);
 //        GridPane.setVgrow(textArea, Priority.ALWAYS);
 //        GridPane.setFillWidth(textArea, true);
 //        GridPane.setFillHeight(textArea, true);
 //        GridPane.setMargin(textArea, new Insets(5));
 //    }
 //
 //    private double computeTextWidth(String text, javafx.scene.text.Font font) {
 //        javafx.scene.text.Text tempText = new javafx.scene.text.Text(text);
 //        tempText.setFont(font);
 //        return tempText.getBoundsInLocal().getWidth();
 //    }
 //
 //    private Node searchObjects(int row, int col) {
 //        for (Node node : tableGrid.getChildren()) {
 //            if (GridPane.getRowIndex(node) == null || GridPane.getColumnIndex(node) == null) continue;
 //            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
 //                System.out.println("Found child " + node.getClass() + " at (" + row + ", " + col + ")");
 //                return node;
 //            }
 //        }
 //        System.out.println("Child not found at (" + row + ", " + col + ")");
 //        return null;
 //    }
 //
 //    @FXML
 //    private void handleEditCourse(ActionEvent event) {
 //        System.out.println("Edit Course clicked");
 //        try {
 //            int sourceRow = GridPane.getRowIndex((Button) event.getSource());
 //            TextArea courseCode = (TextArea) searchObjects(sourceRow, 0);
 //            TextArea sectionNumber = (TextArea) searchObjects(sourceRow, 1);
 //            TextArea professorName = (TextArea) searchObjects(sourceRow, 2);
 //            TextArea capacity = (TextArea) searchObjects(sourceRow, 3);
 //            TextArea lectureTime = (TextArea) searchObjects(sourceRow, 4);
 //            TextArea lectureLocation = (TextArea) searchObjects(sourceRow, 5);
 //            TextArea examDate = (TextArea) searchObjects(sourceRow, 6);
 //            TextArea examLocation = (TextArea) searchObjects(sourceRow, 7);
 //
 //            if (courseCode == null || sectionNumber == null || professorName == null ||
 //                    capacity == null || lectureTime == null || lectureLocation == null ||
 //                    examDate == null || examLocation == null) {
 //                handleLoadEditor(event);
 //            } else {
 //                handleLoadEditor(courseCode.getText(), sectionNumber.getText(), professorName.getText(),
 //                        capacity.getText(), lectureTime.getText(), lectureLocation.getText(),
 //                        examDate.getText(), examLocation.getText(), event, sourceRow);
 //            }
 //        } catch (Exception e) {
 //            log.error("Error in handleEditCourse", e);
 //        }
 //    }
 //
 //    private void handleLoadEditor(ActionEvent event) {
 //        handleLoadEditor("", "", "", "", "", "", "", "", event, -1);
 //    }
 //
 //    private void handleLoadEditor(String courseCode, String sectionNumber, String professorName,
 //                                  String capacity, String lectureTime, String lectureLocation,
 //                                  String examDate, String examLocation, ActionEvent event, int row) {
 //        try {
 //            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/fxml/admin/course_editor.fxml"));
 //            Parent content = loader.load();
 //
 //            StackPane contentArea = null;
 //            Parent parent = ((Node) event.getSource()).getParent();
 //            while (parent != null) {
 //                if (parent.getId() != null && parent.getId().equals("contentArea")) {
 //                    contentArea = (StackPane) parent;
 //                    break;
 //                }
 //                parent = parent.getParent();
 //            }
 //            if (contentArea == null) throw new RuntimeException("Could not find parent StackPane");
 //
 //            CourseEditorController controller = loader.getController();
 //            controller.loadCourse(courseCode, sectionNumber, professorName, capacity,
 //                    lectureTime, lectureLocation, examDate, examLocation);
 //            controller.setParentAndRow(this, row);
 //
 //            contentArea.getChildren().clear();
 //            contentArea.getChildren().add(content);
 //        } catch (IOException e) {
 //            log.error("Error loading course editor", e);
 //        }
 //    }
 //
 //    public void updateRow(int row, String courseCode, String sectionNumber, String professorName,
 //                          String capacity, String lectureTime, String lectureLocation,
 //                          String examDate, String examLocation) {
 //        TextArea courseCodeText = (TextArea) searchObjects(row, 0);
 //        TextArea sectionNumberText = (TextArea) searchObjects(row, 1);
 //        TextArea professorNameText = (TextArea) searchObjects(row, 2);
 //        TextArea capacityText = (TextArea) searchObjects(row, 3);
 //        TextArea lectureTimeText = (TextArea) searchObjects(row, 4);
 //        TextArea lectureLocationText = (TextArea) searchObjects(row, 5);
 //        TextArea examDateText = (TextArea) searchObjects(row, 6);
 //        TextArea examLocationText = (TextArea) searchObjects(row, 7);
 //
 //        if (courseCodeText != null) courseCodeText.setText(courseCode);
 //        if (sectionNumberText != null) sectionNumberText.setText(sectionNumber);
 //        if (professorNameText != null) professorNameText.setText(professorName);
 //        if (capacityText != null) capacityText.setText(capacity);
 //        if (lectureTimeText != null) lectureTimeText.setText(lectureTime);
 //        if (lectureLocationText != null) lectureLocationText.setText(lectureLocation);
 //        if (examDateText != null) examDateText.setText(examDate);
 //        if (examLocationText != null) examLocationText.setText(examLocation);
 //    }
    }