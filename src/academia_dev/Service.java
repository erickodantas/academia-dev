package academia_dev;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Service {
    private Map<String, Course> courses;
    private Map<String, User> users;
    private List<Enrollment> enrollments;
    private Queue<SupportTicket> ticketQueue;

    public Service() {
        this.courses = new HashMap<>();
        this.users = new HashMap<>();
        this.enrollments = new ArrayList<>();
        this.ticketQueue = new LinkedList<>(); 
    }
    
 // cursos e usuarios

    public void addCourse(Course course) {
        if (courses.containsKey(course.getTitle())) {
            throw new UnauthorizedActionException("já existe um curso com o título: " + course.getTitle());
        }
        courses.put(course.getTitle(), course);
    }

    public void addUser(User user) {
        if (users.containsKey(user.getEmail())) {
            throw new UnauthorizedActionException("já existe um usuário com o e-mail: " + user.getEmail());
        }
        users.put(user.getEmail(), user);
    }

    public User getUserByEmail(String email) {
        return users.get(email);
    }

    public Course getCourseByTitle(String title) {
        return courses.get(title);
    }

 // matricula

    public void enrollStudent(String studentEmail, String courseTitle) {
        User user = users.get(studentEmail);
        Course course = courses.get(courseTitle);

        if (user == null || course == null) {
            throw new ResourceNotFoundException("usuário ou curso não encontrado no sistema");
        }

        if (!(user instanceof Student)) {
            throw new ResourceNotFoundException("apenas alunos podem se matricular em cursos");
        }

        Student student = (Student) user;

        if (course.getStatus() != Course.CourseStatus.ACTIVE) {
            throw new EnrollmentException("o curso '" + courseTitle + "' está inativo");
        }

        long activeEnrollmentsCount = enrollments.stream()
                .filter(e -> e.getStudent().getEmail().equals(studentEmail))
                .count();

        if (!student.getSubscriptionPlan().canEnroll((int) activeEnrollmentsCount)) {
            throw new EnrollmentException("limite de cursos excedido para o seu plano atual");
        }

        boolean alreadyEnrolled = enrollments.stream()
                .anyMatch(e -> e.getStudent().getEmail().equals(studentEmail) && e.getCourse().getTitle().equals(courseTitle));

        if (alreadyEnrolled) {
            throw new EnrollmentException("o aluno já está matriculado neste curso");
        }

        Enrollment newEnrollment = new Enrollment(student, course);
        enrollments.add(newEnrollment);
    }
    
 // ticket suporte

    public void openSupportTicket(String userEmail, String title, String message) {
        User user = users.get(userEmail);
        if (user == null) {
            throw new ResourceNotFoundException("usuário não encontrado");
        }
        SupportTicket ticket = new SupportTicket(user, title, message);
        ticketQueue.add(ticket);
    }

    public SupportTicket processNextTicket(String adminEmail) {
        User adminUser = users.get(adminEmail);
        if (!(adminUser instanceof Admin)) {
            throw new UnauthorizedActionException("apenas administradores podem processar tickets");
        }
        
        if (ticketQueue.isEmpty()) {
            return null;
        }
        return ticketQueue.poll(); 
    }
    
 // stream

    public List<Course> getCoursesByDifficultySorted(Course.DifficultyLevel level) {
        return courses.values().stream()
                .filter(course -> course.getDifficultyLevel() == level)
                .sorted(Comparator.comparing(Course::getTitle)) 
                .collect(Collectors.toList());
    }

    public Set<String> getUniqueActiveInstructors() {
        return courses.values().stream()
                .filter(course -> course.getStatus() == Course.CourseStatus.ACTIVE)
                .map(Course::getInstructorName)
                .collect(Collectors.toSet());
    }

    public Map<String, List<Student>> getStudentsGroupedByPlan() {
        return users.values().stream()
                .filter(user -> user instanceof Student)
                .map(user -> (Student) user)
                .collect(Collectors.groupingBy(student -> student.getSubscriptionPlan().getClass().getSimpleName()));
    }

    public double getAverageOverallProgress() {
        return enrollments.stream()
                .mapToInt(Enrollment::getProgress)
                .average()
                .orElse(0.0);
    }
    public Optional<Student> getStudentWithMostEnrollments() {
        return enrollments.stream()
                .collect(Collectors.groupingBy(Enrollment::getStudent, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }
    public List<Course> getAllCourses() {
        return new ArrayList<>(courses.values());
    }

    public List<Student> getAllStudents() {
        return users.values().stream()
                .filter(u -> u instanceof Student)
                .map(u -> (Student) u)
                .collect(Collectors.toList());
    }
    public List<Enrollment> getStudentEnrollments(String studentEmail) {
        return enrollments.stream()
                .filter(e -> e.getStudent().getEmail().equals(studentEmail))
                .collect(Collectors.toList());
    }

    public void updateEnrollmentProgress(String studentEmail, String courseTitle, int newProgress) {
        Enrollment enrollment = enrollments.stream()
                .filter(e -> e.getStudent().getEmail().equals(studentEmail) && e.getCourse().getTitle().equals(courseTitle))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Você não está matriculado no curso: " + courseTitle));
        
        enrollment.updateProgress(newProgress);
    }

    public void cancelEnrollment(String studentEmail, String courseTitle) {
        boolean removido = enrollments.removeIf(e -> 
            e.getStudent().getEmail().equals(studentEmail) && e.getCourse().getTitle().equals(courseTitle)
        );
        
        if (!removido) {
            throw new ResourceNotFoundException("Matrícula não encontrada para cancelamento.");
        }
    }
    public List<Enrollment> getAllEnrollments() {
        return new ArrayList<>(enrollments);
    }

    public List<SupportTicket> getAllTickets() {
        return new ArrayList<>(ticketQueue);
    }
}
