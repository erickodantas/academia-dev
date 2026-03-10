package academia_dev;
public class InitialData {

    public static void populate(Service service) {
        SubscriptionPlan basicPlan = new BasicPlan();
        SubscriptionPlan premiumPlan = new PremiumPlan();

        Course c1 = new Course("Desenvolvimento de Jogos com Unity", "Crie seu primeiro jogo 2D e 3D", "Carlos", 40, Course.DifficultyLevel.BEGINNER, Course.CourseStatus.ACTIVE);
        Course c2 = new Course("Masterclass de Unreal Engine", "Lógica avançada e renderização realista", "Sabrina", 80, Course.DifficultyLevel.ADVANCED, Course.CourseStatus.ACTIVE);
        Course c3 = new Course("Programação Orientada a Objetos com Java", "Fundamentos de POO e Stream API", "Henrique", 60, Course.DifficultyLevel.INTERMEDIATE, Course.CourseStatus.ACTIVE);
        Course c4 = new Course("C# e .NET para Iniciantes", "Backend com C#", "Roberto", 50, Course.DifficultyLevel.BEGINNER, Course.CourseStatus.INACTIVE); 
        service.addCourse(c1);
        service.addCourse(c2);
        service.addCourse(c3);
        service.addCourse(c4);

        Admin admin1 = new Admin("Erick", "erick@academiadev.com");
        Student s1 = new Student("Sabrina", "sabrina@faculdade.com", premiumPlan); 
        Student s2 = new Student("Carlos", "carlos@email.com", basicPlan); 
        Student s3 = new Student("Henrique", "henrique@email.com", basicPlan);

        service.addUser(admin1);
        service.addUser(s1);
        service.addUser(s2);
        service.addUser(s3);

        service.enrollStudent("sabrina@faculdade.com", "Desenvolvimento de Jogos com Unity");
        service.enrollStudent("sabrina@faculdade.com", "Programação Orientada a Objetos com Java");
        service.enrollStudent("carlos@email.com", "Masterclass de Unreal Engine");
        service.openSupportTicket("henrique@email.com", "Problema no Login", "Não consigo acessar a plataforma pelo celular.");
        service.openSupportTicket("carlos@email.com", "Dúvida sobre o BasicPlan", "Posso fazer o upgrade para o Premium mês que vem?");
        System.out.println("dados carregados!");
    }
}