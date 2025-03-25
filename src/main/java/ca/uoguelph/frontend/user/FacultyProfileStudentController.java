package ca.uoguelph.frontend.user;
import ca.uoguelph.backend.Course;
import ca.uoguelph.backend.Faculty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Pair;
import java.util.ArrayList;

public class FacultyProfileStudentController {
    @FXML
    private Label facultyNameLabel;

    @FXML
    private Label facultyEmailLabel;

    @FXML
    private Label officeLocationLabel;

    @FXML
    private Label researchInterestsLabel;

    @FXML
    private ListView<String> coursesTaughtList;

    @FXML
    private ImageView facultyImageView;

    //@FXML
    //private Button backButton;

    private Faculty faculty;

    private void initialize(){
        //run when file load lol
    }

    public void setFacultyData(Faculty faculty){//object type faculty named faculty
        this.faculty = faculty;

        facultyNameLabel.setText("Name: " + faculty.getName());
        facultyEmailLabel.setText("Email: "+ faculty.getEmail());
        officeLocationLabel.setText("Office: "+ faculty.getOfficeLocation());
        researchInterestsLabel.setText("Research Interests: "+ faculty.getResearchInterest());

        if(faculty.getProfilePhoto()!=null && !faculty.getProfilePhoto().isEmpty()) {//if faculty profile is not null or empty
            facultyImageView.setImage(new Image(faculty.getProfilePhoto()));
        }
        else{
            facultyImageView.setImage(new Image("Default_pfp.jpg"));
        }
       ArrayList<Pair<String, String>> facultyCoursesTaught = new ArrayList<>();//make a new array list

        for (Pair<String, String> course : faculty.getCourses()){//add the object course of type pair string, iterete thru faculty.getcourses()
            facultyCoursesTaught.add(course);//add the pair string wtvr course to the end of arraylist above
        }
    }
}
