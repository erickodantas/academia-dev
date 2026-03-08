package academia_dev;

public class Course {
	private String title;
    private String description;
    private String instructorName;
    private int durationInHours;
    private DifficultyLevel difficultyLevel;
    private CoursesStatus status;
    public enum CoursesStatus {
        ACTIVE,
        INACTIVE
    }
    public enum DifficultyLevel {
        BEGINNER,
        INTERMEDIATE,
        ADVANCED
    }
    
    public Course(String title, String description, String instructorName, int durationInHours, DifficultyLevel difficultyLevel, CoursesStatus status) {
        this.title = title;
        this.description = description;
        this.instructorName = instructorName;
        this.durationInHours = durationInHours;
        this.difficultyLevel = difficultyLevel;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public int getDurationInHours() {
        return durationInHours;
    }

    public void setDurationInHours(int durationInHours) {
        this.durationInHours = durationInHours;
    }

    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public CoursesStatus getStatus() {
        return status;
    }

    public void setStatus(CoursesStatus status) {
        this.status = status;
    }

    public void activate() {
        this.status = CoursesStatus.ACTIVE;
    }

    public void inactivate() {
        this.status = CoursesStatus.INACTIVE;
    }
}