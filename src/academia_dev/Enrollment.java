package academia_dev;

public class Enrollment {
    private Student student;
    private Course course;
    private int progress;

    public Enrollment(Student student, Course course) {
        this.student = student;
        this.course = course;
        this.progress = 0;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public int getProgress() {
        return progress;
    }

    public void updateProgress(int newProgress) {
        if (newProgress < 0 || newProgress > 100) {
            throw new EnrollmentException("o progresso deve ser um valor entre 0 e 100");
        } else {
        	this.progress = newProgress;
        }
    }
}
