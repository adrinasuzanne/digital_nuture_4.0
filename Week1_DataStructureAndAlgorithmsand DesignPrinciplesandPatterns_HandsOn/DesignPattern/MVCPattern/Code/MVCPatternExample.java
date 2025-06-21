class Student {
    private String name;
    private int id;
    private String grade;

    public Student(String name, int id, String grade) {
        this.name = name;
        this.id = id;
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getGrade() {
        return grade;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class StudentView {
    void displayStudentDetails(String name, int id, String grade) {
        System.out.println("Name: " + name + ", ID: " + id + ", Grade: " + grade);
    }
}

class StudentController {
    private Student model;
    private StudentView view;

    StudentController(Student model, StudentView view) {
        this.model = model;
        this.view = view;
    }

    void updateView() {
        view.displayStudentDetails(model.getName(), model.getId(), model.getGrade());
    }

    void setStudentName(String name) {
        model.setName(name);
    }
}

public class MVCPatternExample {
    public static void main(String[] args) {
        Student student = new Student("John", 101, "A");
        StudentView view = new StudentView();
        StudentController controller = new StudentController(student, view);
        controller.updateView();
        controller.setStudentName("David");
        controller.updateView();
    }
}
