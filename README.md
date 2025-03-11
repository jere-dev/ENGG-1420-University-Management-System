# ENGG-1420-University-Management-System

An open-ended project to use object-oriented programming and JavaFX to recreate 
the University of Guelph's WebAdvisor interface as a local Java application. By importing data from a .xlsx spreadsheet file, 
users can mimic administrators, faculty and students logging into the website and viewing 
or (admin-specific) editing data entries. (e.g. subjects, courses, other faculty/students and scheduling events.)

Developers will follow the unique design of the Java language by implementing OOP-related concepts 
i.e. language-specific features including the following concepts (taken from the ENGG\*1420 Course Outline).

* Access controls (e.g. packages, modifiers)
* Object instantiation, member fields and methods
* `static` and `final` keyword modifiers
* Method overloading, overriding and hiding
* Class inheritance (`extends`), abstraction (`abstract`), and interfaces (`interface`)
* Error control and exception handling (`try-catch`)

As a consequence, developers are mandated to utilize all OOP-related concepts within the course curriculum to create a locally hosted WebAdvisor Java application.

See https://docs.oracle.com/javafx/2/get_started/jfxpub-get_started.htm for the GUI framework of this project.

See https://docs.oracle.com/javase/tutorial/java/index.html for language-specific features meant to be implemented in the project.
Alternative tutorial websites include:
* https://dev.java/learn/
* https://exercism.org/tracks/java (As suggested by ASHRREAL)

See https://github.com/jere-dev/ENGG-1420-University-Management-System/wiki for more information pertaining to the project's development.

<!--

Final group project for ENGG-1420: Develop a system where students and faculty can view their courses, subjects, and professors, as well as register for events. Administrators will have the ability to modify these details. All data will be stored in a database (Excel sheet).

---

## Planning

* Use access modifiers to restrict the usage of functions and variables.
  * Prevent them from being used in unintended places.
* Add comments to document the purpose of functions and specify their accessibility.
  * Clarify what should be used and how it works.
* Create a database class.
  * It should read from and write to an Excel file.
  * Design it with compatibility for the manager classes.
* Clarify how events are stored for professors.
  * Update relevant classes based on the specifications.
* Create an authentication function.
  * It should interact with both the faculty and student managers.

-->