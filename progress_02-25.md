# University Management System (UMS) Progress Report 
*2025-02-25*

This outline lists, outlines, and summarizes every Java class and method and some .fxml files created as of 2025-02-25 for the UMS.

- [Back-End](#back-end)
  - [Course](#course)
  - [CourseManger](#coursemanger)
  - [database](#database)
  - [Event](#event)
  - [EventManager](#eventmanager)
- [Front-End](#front-end)
  - [CourseCatalogController](#coursecatalogcontroller)
  - [DashboardController](#dashboardcontroller)
  - [EventManagerController](#eventmanagercontroller)
  - [FacultyListController](#facultylistcontroller)
  - [LoginController](#logincontroller)
  - [StudentListController](#studentlistcontroller)
  - [ProfileController](#profilecontroller)
  - [SubjectManagerController](#subjectmanagercontroller)
- [Unlinked .fxml Files](#unlinked-fxml-files)
  - [subject\_catalog](#subject_catalog)
  - [faculty\_course\_catalog.fxml](#faculty_course_catalogfxml)
  - [faculty\_profile\_admin.fxml](#faculty_profile_adminfxml)
  - [student\_profile\_admin.fxml](#student_profile_adminfxml)
  - [student\_profile\_faculty.fxml](#student_profile_facultyfxml)
  - [student\_profile\_student.fxml](#student_profile_studentfxml)


## Back-End

### Course

An object encompassing the data for one course under a subject:
- Course name, course code, subject code;
- Student capacity, lecture time, exam date, location.

```Java
public class Course {
    public Course(String _name, String _code, 
            int _capacity, String _section, 
            String _lecTime, String _examDate,
            String _location, Subject _subject);
    public void removeStudent();    // removes enrolled student
    public void removeSelf();   // deletes course from database
}
```

### CourseManger

A Hashmap wrapper for all courses within the university database.

```Java
public class CourseManger {
    public static void loadCourses();   // loads courses from .xlsx
    public static void removeCourse();  // removes courses from .xlsx
    public static void addCourse(String _name, String _code, 
            int _capacity, String _section, 
            String _lecTime, String _examDate, 
            String _location, Subject _subject);    // creates new course in database
    public static void getCourse(); // retrieves course via course name
}
```

### database

Unknown current function. Should read and output the .xlsx file to terminal.

### Event

An object encompassing the properties of an event:
- Name, code, description, location;
- Image symbolizing the event;
- Date and time;
- Student capacity and cost.

```Java
public class Event {
    public Event(String _name, String _code, 
            String _description, String _image, 
            String _location, String _dateAndTime,
            int _capacity, String _cost);
    public void removeSelf();   // deletes event from database
    public void removeStudent(Student _student); // removes student from event
    public void addStudent(Student _student);   // adds student to event
    /* public void removeProf(Prof _prof);
    public void addProf(Prof _prof); */
}
```

### EventManager

A Hashmap wrapper for events based on the event name.

```Java
public class EventManager {
    public static void load();  // loads all events from .xlsx
    public static void removeEvent(Event _event);   // removes events from .xlsx 
}
```

## Front-End

### CourseCatalogController
*attached to course_catalog.fxml*

Handles a student's course catalog:
- A search bar;
- A list of courses found with the search term.

```Java
public class CourseCatalogController{
    private TextField searchField;
    private Button searchButton;
    private void handleSearch(ActionEvent event);   // lists courses under search term
}
```

### DashboardController
*attached to dashboard.fxml*

Handles buttons on the dashboard (screen after login):
- Subject manager, event manager, course catalog;
- Student list, faculty list;
- User profile, home (return to dashboard).

```Java
public class DashboardController {
    private Button dashboardButton, subjectManagerButton,
            studentListButton, facultyListButton,
            EventManagerButton, courseCatalogButton, 
            profileButton;
    ... // methods to load pages from buttons
}
```

### EventManagerController
*attached to event_manager_admin.fxml*

An admin menu to add, edit and delete events.

```Java
public class EventManagerController {
    private Button addEventButton, editEventButton, deleteEventButton;
    private void handleAddEvent(ActionEvent event);  // opens event creation menu
    private void handleAddEvent(ActionEvent event);  // opens event editor menu
    private void handleDeleteEvent(ActionEvent event);
}
```

### FacultyListController
*attached to faculty_list.fxml*

A search menu for and list of faculty members across the system for administrators.

```Java
public class FacultyListController {
    private TextField searchField;
    private Button searchButton;
    private void handleSearch(ActionEvent event);   // start searching for faculty
}
```

### LoginController
*attached to login.fxml*

The login menu that checks a user's incoming username and password, checks the type of user (student, faculty, admin), and loads the usertype-specific dashboard.

```Java
public class LoginController {
    ...; // username, password textFields and button interface
    private void handleLogin(ActionEvent event) {
        String username = ..., password = ...;
        if ("admin".equals(username) && "admin".equals(password)) {...} // load admin dashboard
        else {...}  // report incorrect user/pass
    }
}
```

### StudentListController
*attached to student_list.fxml*

The admin search menu for all the students in the system.

```Java
public class StudentListController {
    private TextField searchField;
    private Button searchButton;
    private void handleSearch(ActionEvent event);   // search for all students
}
```

### ProfileController
*unlinked .fxml file*

Unknown purpose - used for
 password changing.

```Java
public class ProfileController {
    private PasswordField currentPasswordField, newPasswordField;
    private Button changePasswordButton;
    private void handleChangePassword(ActionEvent event);   // change password
}
```

### SubjectManagerController
*attached to subject_manager_admin.fxml*

A menu for administrators to add, edit, or delete subjects.

```Java
public class SubjectManagerController {
    private Button addSubjectButton, editSubjectButton, deleteSubjectButton;
    private void handleAddSubject(ActionEvent event);   // add a subject to database
    private void handleEditSubject(ActionEvent event);  // edit a subject in database
    private void handleDeleteSubject(ActionEvent event);    // delete a subject from database
}
```

## Unlinked .fxml Files

### subject_catalog

Display to list the code and name of subjects. 

### faculty_course_catalog.fxml

Display to allow a faculty member to view the courses they have taught(?) and the students enrolled.

### faculty_profile_admin.fxml

Display to list and be able to edit:
- Profile information (name, email, office);
- List of courses assigned (course name and code).

### student_profile_admin.fxml

Display to list and be able to edit:
- Personal info (name, ID, address);
- Academic info (list of courses and grades).

### student_profile_faculty.fxml

Display to list the courses and associated grades of a student.

### student_profile_student.fxml

The student menu that displays their name and student ID. Students can also upload a profile photo and change their password here.