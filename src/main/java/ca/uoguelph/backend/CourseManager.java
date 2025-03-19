package ca.uoguelph.backend;

import java.util.ArrayList;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

//TODO: modify excel sheet
public class CourseManager {
    private static ArrayList<Course> courses = new ArrayList<Course>();
    private static ObjectMapper mapper = new ObjectMapper();

//load course
    public static void loadCourses(){
        var arar = Database.loadStrings(1);
        int i = 0;
        for(var ar : arar){
            i++;
            if(i > 1000){break;}
            try {
                ArrayList<Section> sections = mapper.readValue(ar.get(9), new TypeReference<ArrayList<Section>>(){});
                courses.add(new Course(ar.get(0), ar.get(1), ar.get(2), Float.parseFloat(ar.get(3)), ar.get(4), ar.get(5), ar.get(6), ar.get(7), ar.get(8), sections));
                
            } catch (Exception e) {
                System.err.println("Json error at entry: " + i);
                e.printStackTrace();
            }
        }
    }

//get course
    public static Course getCourse(String subjectCode, String courseCode){
        Course course = courses.stream().filter(c -> c.getSubjectCode().equals(subjectCode) && c.getCourseCode().equals(courseCode)).findFirst().orElse(null);
        if(course == null){throw new IllegalArgumentException("Invalid Course Code");}
        return course;
    }
    public static ArrayList<Course> getCourses(){return courses;}
    public static ArrayList<Course> searchCoursesBySubjectCourseCode(String code){return courses.stream().filter(c -> (c.getSubjectCode()+c.getCourseCode()).contains(code)).collect(Collectors.toCollection(ArrayList::new));}
    public static ArrayList<Course> searchCoursesBySubjectCode(String code){return courses.stream().filter(c -> c.getSubjectCode().contains(code)).collect(Collectors.toCollection(ArrayList::new));}
    public static ArrayList<Course> searchCoursesByCourseCode(String code){return courses.stream().filter(c -> c.getCourseCode().contains(code)).collect(Collectors.toCollection(ArrayList::new));}
    public static ArrayList<Course> searchCoursesByTitle(String title){return courses.stream().filter(c -> c.getTitle().contains(title)).collect(Collectors.toCollection(ArrayList::new));}

//modify course
    //add course
    public static void addCourse(String subjectCode, String courseCode, String title, float credits, String description, String requisites, String locations, String offered, String department, ArrayList<Section> sections){
        //TODO: make sure Course ID is unque in conjunction with subject ID
        courses.add(new Course(subjectCode, courseCode, title, credits, description, requisites, locations, offered, department, sections));
    }
    //edit course
    //TODO: edit sections
    public static void editCourseDepartment(Course course, String department){course.setDepartment(department);}
    public static void editCourseOffered(Course course, String offered){course.setOffered(offered);}
    public static void editCourseLocations(Course course, String locations){course.setLocations(locations);}
    public static void editCourseRequisites(Course course, String requisites){course.setRequisites(requisites);}
    public static void editCourseDescription(Course course, String description){course.setDescription(description);}
    public static void editCourseCredits(Course course, float credits){course.setCredits(credits);}
    public static void editCourseTitle(Course course, String title){course.setTitle(title);}
    public static void editCourseCourseCode(Course course, String courseCode){course.setCourseCode(courseCode);}
    public static void editCourseSubjectCode(Course course, String subjectCode){course.setSubjectCode(subjectCode);}
    //remove course
    public static void removeCourse(Course course){courses.remove(course);}
    public static void removeCourse(String subjectCode, String courseCode){courses.remove(getCourse(subjectCode, courseCode));}
}