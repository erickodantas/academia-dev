package academia_dev;

import java.util.Scanner;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static Service service = new Service();
    public static void main(String[] args) {
        InitialData.populate(service);
        System.out.println("  --BEM-VINDO A PLATAFORMA ACADEMIADEV--  ");

        while (true) {
            System.out.print("\ndigite seu e-mail para logar 'sair' para encerrar: ");
            String emailDigitado = scanner.nextLine().trim(); 
            if (emailDigitado.equalsIgnoreCase("sair")) {
                System.out.println("encerrando o sistema.");
                break;
            }
            try {
                User usuarioLogado = service.getUserByEmail(emailDigitado);
                if (usuarioLogado == null) {
                    System.out.println("usuário não encontrado, tente novamente.");
                    continue;
                }
                System.out.println("\nolá, " + usuarioLogado.getName() + "!");

                if (usuarioLogado instanceof Admin) {
                    exibirMenuAdmin((Admin) usuarioLogado);
                } else if (usuarioLogado instanceof Student) {
                    exibirMenuAluno((Student) usuarioLogado);
                }

            } catch (Exception e) {
                System.out.println("ocorreu um erro inesperado: " + e.getMessage());
            }
        }
    }

 // --- MENU DO ADMINISTRADOR ---
    private static void exibirMenuAdmin(Admin admin) {
        int opcao = 0;

        while (opcao != 8) {
            System.out.println("\n-- PAINEL DE ADMIN --");
            System.out.println("1. gerenciar status de cursos");
            System.out.println("2. gerenciar planos de alunos");
            System.out.println("3. atender tickets de suporte");
            System.out.println("4. gerar relatórios e análises");
            System.out.println("5. exportar dados (CSV)");
            System.out.println("6. consultar catálogo de cursos");
            System.out.println("7. abrir ticket de suporte");
            System.out.println("8. deslogar");
            System.out.print("escolha uma opção: ");
            opcao = Integer.parseInt(scanner.nextLine());

            try {
                switch (opcao) {
                    case 1:
                        System.out.print("digite o título do curso: ");
                        String tituloCurso = scanner.nextLine();
                        Course curso = service.getCourseByTitle(tituloCurso);
                        
                        if (curso == null) {
                            System.out.println("curso não encontrado");
                        } else {
                            System.out.print("deseja ativar(1) ou inativar(2) o curso?");
                            int acao = Integer.parseInt(scanner.nextLine());
                            if (acao == 1) {
                                curso.activate();
                                System.out.println("curso ativado com sucesso!");
                            } else if (acao == 2) {
                                curso.inactivate();
                                System.out.println("curso inativado com sucesso!");
                            } else {
                                System.out.println("ação inválida");
                            }
                        }
                        break;
                    case 2:
                        System.out.print("digite o e-mail do aluno: ");
                        String emailAluno = scanner.nextLine();
                        User usuario = service.getUserByEmail(emailAluno);
                        
                        if (usuario instanceof Student) {
                            Student aluno = (Student) usuario;
                            System.out.print("escolha o novo plano - BasicPlan(1) PremiumPlan(2): ");
                            int escolhaPlano = Integer.parseInt(scanner.nextLine());
                            
                            if (escolhaPlano == 1) {
                                aluno.setSubscriptionPlan(new BasicPlan());
                                System.out.println("plano alterado para Basic");
                            } else if (escolhaPlano == 2) {
                                aluno.setSubscriptionPlan(new PremiumPlan());
                                System.out.println("plano alterado para Premium");
                            } else {
                                System.out.println("opção de plano inválida");
                            }
                        } else {
                            System.out.println("aluno não encontrado ou e-mail pertence a um admin");
                        }
                        break;
                    case 3:
                        SupportTicket ticket = service.processNextTicket(admin.getEmail());
                        if (ticket == null) {
                            System.out.println("a fila de suporte está vazia");
                        } else {
                            System.out.println("\n-- ATENDENDO TICKET --");
                            System.out.println("autor: " + ticket.getAuthor().getName() + " (" + ticket.getAuthor().getEmail() + ")");
                            System.out.println("assunto: " + ticket.getTitle());
                            System.out.println("mensagem: " + ticket.getMessage());
                            System.out.println("------------------------");
                            System.out.println("ticket marcado como resolvido e retirado da fila");
                        }
                        break;
                    case 4:
                        System.out.println("\n-- RELATÓRIOS DA PLATAFORMA --");
                        
                        System.out.println("\n1. cursos iniciantes:");
                        service.getCoursesByDifficultySorted(Course.DifficultyLevel.BEGINNER)
                               .forEach(c -> System.out.println(" - " + c.getTitle()));

                        System.out.println("\n2. instrutores unicos com cursos ativos:");
                        service.getUniqueActiveInstructors()
                               .forEach(inst -> System.out.println(" - " + inst));

                        System.out.println("\n3. alunos agrupados por plano:");
                        Map<String, List<Student>> agrupamento = service.getStudentsGroupedByPlan();
                        agrupamento.forEach((plano, listaAlunos) -> {
                            System.out.println(" [" + plano + "]");
                            listaAlunos.forEach(a -> System.out.println("   - " + a.getName()));
                        });

                        System.out.println("\n4. média geral de progresso: " + service.getAverageOverallProgress() + "%");

                        System.out.println("\n5. aluno com mais matrículas:");
                        service.getStudentWithMostEnrollments()
                               .ifPresentOrElse(
                                   a -> System.out.println(" - " + a.getName()),
                                   () -> System.out.println(" - nenhum aluno matriculado")
                               );
                        break;
                    case 5:
                        System.out.println("qual entidade deseja exportar?");
                        System.out.println("1. cursos");
                        System.out.println("2. alunos");
                        System.out.println("3. matrículas");
                        System.out.println("4. fila de Suporte");
                        System.out.print("escolha: ");
                        int entidade = Integer.parseInt(scanner.nextLine());
                        System.out.print("digite os nomes das colunas separados por vírgula: ");
                        String colunasInput = scanner.nextLine();
                        List<String> colunas = Arrays.stream(colunasInput.split(","))
                                                     .map(String::trim)
                                                     .collect(Collectors.toList());
                        
                        System.out.println("\n-- EXPORTAÇÃO CSV --");
                        if (entidade == 1) {
                            System.out.println(GenericCsvExporter.exportToCsv(service.getAllCourses(), colunas));
                        } else if (entidade == 2) {
                            System.out.println(GenericCsvExporter.exportToCsv(service.getAllStudents(), colunas));
                        } else if (entidade == 3) {
                            System.out.println(GenericCsvExporter.exportToCsv(service.getAllEnrollments(), colunas));
                        } else if (entidade == 4) {
                            System.out.println(GenericCsvExporter.exportToCsv(service.getAllTickets(), colunas));
                        } else {
                            System.out.println("opção inválida");
                        }
                        break;
                    case 6:
                        System.out.println("\n-- CATÁLOGO DE CURSOS --");
                        service.getAllCourses().forEach(c -> {
                            System.out.println("título: " + c.getTitle() + " | dificuldade: " + c.getDifficultyLevel() + " | status: " + c.getStatus());
                        });
                        break;
                    case 7:
                        System.out.print("digite o título do problema: ");
                        String tituloTicket = scanner.nextLine();
                        System.out.print("digite a mensagem detalhada: ");
                        String mensagemTicket = scanner.nextLine();
                        service.openSupportTicket(admin.getEmail(), tituloTicket, mensagemTicket);
                        System.out.println("ticket aberto e enviado para a fila");
                        break;
                    case 8:
                        System.out.println("deslogando do painel de administrador...");
                        break;
                    default:
                        System.out.println("opção inválida tente novamente");
                }
            } catch (Exception e) {
                System.out.println("erro em: " + e.getMessage());
            }
        }
    }
    

    // --- MENU DO ALUNO ---
    private static void exibirMenuAluno(Student student) {
        int opcao = 0;

        while (opcao != 7) {
            System.out.println("\n-- PAINEL DO ALUNO --");
            System.out.println("plano atual: " + student.getSubscriptionPlan().getClass().getSimpleName());
            System.out.println("1. matricular-se em curso");
            System.out.println("2. consultar minhas matrículas e progresso");
            System.out.println("3. atualizar progresso de um curso");
            System.out.println("4. cancelar matrícula");
            System.out.println("5. consultar catálogo de cursos");
            System.out.println("6. abrir ticket de suporte");
            System.out.println("7. deslogar");
            System.out.print("escolha uma opção: ");

            opcao = Integer.parseInt(scanner.nextLine());

            try {
                switch (opcao) {
                    case 1:
                        System.out.print("digite o título exato do curso que deseja se matricular: ");
                        String tituloMatricula = scanner.nextLine();
                        service.enrollStudent(student.getEmail(), tituloMatricula);
                        System.out.println("matrícula realizada no curso: " + tituloMatricula);
                        break;
                        
                    case 2:
                        System.out.println("\n-- MINHAS MATRÍCULAS --");
                        List<Enrollment> minhasMatriculas = service.getStudentEnrollments(student.getEmail());
                        if (minhasMatriculas.isEmpty()) {
                            System.out.println("você não está matriculado em nenhum curso");
                        } else {
                            minhasMatriculas.forEach(e -> {
                                System.out.println("curso: " + e.getCourse().getTitle() + " | progresso: " + e.getProgress() + "%");
                            });
                        }
                        break;
                        
                    case 3:
                        System.out.print("digite o título do curso que deseja atualizar: ");
                        String tituloAtualizar = scanner.nextLine();
                        System.out.print("digite o novo percentual de progresso(0 a 100): ");
                        int novoProgresso = Integer.parseInt(scanner.nextLine());
                        
                        service.updateEnrollmentProgress(student.getEmail(), tituloAtualizar, novoProgresso);
                        System.out.println("progresso atualizado com sucesso!");
                        break;
                        
                    case 4:
                        System.out.print("digite o titulo do curso que quer cancelar a matrícula: ");
                        String tituloCancelar = scanner.nextLine();
                        
                        service.cancelEnrollment(student.getEmail(), tituloCancelar);
                        System.out.println("matrícula cancelada com sucesso");
                        break;
                        
                    case 5:
                        System.out.println("\n-- CATÁLOGO DE CURSOS ATIVOS --");
                        service.getAllCourses().stream()
                               .filter(c -> c.getStatus() == Course.CourseStatus.ACTIVE)
                               .forEach(c -> System.out.println("título: " + c.getTitle() + " | dificuldade: " + c.getDifficultyLevel() + " | duração: " + c.getDurationInHours() + "h"));
                        break;
                        
                    case 6:
                        System.out.print("digite o título do problema: ");
                        String tituloTicket = scanner.nextLine();
                        System.out.print("digite a mensagem detalhada do suporte: ");
                        String mensagemTicket = scanner.nextLine();
                        
                        service.openSupportTicket(student.getEmail(), tituloTicket, mensagemTicket);
                        System.out.println("ticket aberto e enviado para a fila");
                        break;
                        
                    case 7:
                        System.out.println("deslogando do painel do aluno...");
                        break;
                        
                    default:
                        System.out.println("opção inválida tente novamente");
                }
            } catch (EnrollmentException e) {
                System.out.println("\natenção: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("\nerro na operação: " + e.getMessage());
            }
        }
    }
}