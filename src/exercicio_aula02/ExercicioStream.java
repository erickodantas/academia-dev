package exercicio_aula02;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class Produto {
    private String nome;
    private double preco;
    private String categoria;
    public Produto(String nome, double preco, String categoria) {
        this.nome = nome;
        this.preco = preco;
        this.categoria = categoria;
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }

    public String getCategoria() {
        return categoria;
    }

    @Override
    public String toString() {
        return "Produto [nome=" + nome + ", preco=" + preco + ", categoria=" + categoria + "]";
    }
}

public class ExercicioStream {

    public static void main(String[] args) {
        List<Produto> produtos = new ArrayList<>();
        produtos.add(new Produto("notebook", 3500.0, "Eletrônicos"));
        produtos.add(new Produto("1984", 85.0, "Livros"));
        produtos.add(new Produto("iphone 2000", 2100.0, "Eletrônicos"));
        produtos.add(new Produto("herois do olimpo", 120.0, "Livros"));
        produtos.add(new Produto("fone de Ouvido", 250.0, "Eletrônicos"));
        produtos.add(new Produto("monitor 24 polegadas", 800.0, "Eletrônicos"));
        produtos.add(new Produto("harry hotter", 45.0, "Livros"));
        produtos.add(new Produto("mouse gamer", 150.0, "Eletrônicos"));

        // EXERCICIO A
        System.out.println("--- EXERCICIO A ---");
        System.out.println("Eletrônicos (forEach + if tradicional):");
        produtos.forEach(p -> {
            if (p.getCategoria().equals("Eletrônicos")) {
                System.out.println("- " + p.getNome());
            }
        });

        System.out.println("\nEletrônicos (stream + filter):");
        produtos.stream()
                .filter(p -> p.getCategoria().equals("Eletrônicos"))
                .forEach(p -> System.out.println("- " + p.getNome()));
        // EXERCICIO B
        System.out.println("\n--- EXERCICIO B ---");
        List<Double> precosMaioresQue500 = produtos.stream()
                .filter(p -> p.getPreco() > 500.0)
                .map(p -> p.getPreco())
                .collect(Collectors.toList());
        
        System.out.println("Preços maiores que 500.0: " + precosMaioresQue500);


        // EXERCICIO C
        System.out.println("\n--- EXERCICIO C ---");
        double totalLivros = produtos.stream()
                .filter(p -> p.getCategoria().equals("Livros"))
                .mapToDouble(Produto::getPreco)
                .sum();
        
        System.out.println("Valor total do estoque de Livros: R$ " + totalLivros);

        // EXERCICIO F
        System.out.println("\n--- EXERCICIO F --");
        List<String> nomesLambda = produtos.stream()
                .map(p -> p.getNome())
                .collect(Collectors.toList());
        System.out.println("Lista de nomes (Lambda): " + nomesLambda);
        List<String> nomesMethodRef = produtos.stream()
                .map(Produto::getNome)
                .collect(Collectors.toList());
        System.out.println("Lista de nomes (Method Reference): " + nomesMethodRef);

        // EXERCICIO E
        System.out.println("\n--- EXERCICIO E ---");
        System.out.println("1. Buscando um produto que EXISTE ('notebook'):");
        buscarProdutoPorNome(produtos, "notebook").ifPresent(p -> System.out.println("Detalhes: " + p));

        System.out.println("\n2. Buscando um produto que NÃO EXISTE ('Geladeira'):");
        try {
            Produto produtoInexistente = buscarProdutoPorNome(produtos, "Geladeira")
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado!"));
        } catch (RuntimeException e) {
            System.out.println("Exceção Lançada: " + e.getMessage());
        }
    }

    //EXERCICIO D
    public static Optional<Produto> buscarProdutoPorNome(List<Produto> produtos, String nome) {
        return produtos.stream()
                .filter(p -> p.getNome().equals(nome))
                .findFirst();
    }
}