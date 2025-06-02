package br.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GerenciadorAutomoveis {
    private List<Automovel> automoveis;
    private static final String NOME_ARQUIVO = "automoveis.txt";

    public GerenciadorAutomoveis() {
        this.automoveis = new ArrayList<>();
        carregarDados();
    }

    // --- Persistência ---
    public void carregarDados() {
        try (BufferedReader reader = new BufferedReader(new FileReader(NOME_ARQUIVO))) {
            String linha;
            automoveis.clear(); // Limpa a lista atual antes de carregar
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(",");
                if (dados.length == 5) {
                    try {
                        String placa = dados[0].trim();
                        String modelo = dados[1].trim();
                        String marca = dados[2].trim();
                        int ano = Integer.parseInt(dados[3].trim());
                        double valor = Double.parseDouble(dados[4].trim());
                        automoveis.add(new Automovel(placa, modelo, marca, ano, valor));
                    } catch (NumberFormatException e) {
                        System.err.println("Erro ao parsear dados numéricos da linha: " + linha + " - " + e.getMessage());
                    }
                } else {
                     System.err.println("Linha ignorada (formato inválido): " + linha);
                }
            }
            System.out.println("Dados carregados do arquivo: " + NOME_ARQUIVO);
        } catch (IOException e) {
            System.out.println("Arquivo de dados não encontrado ou erro ao ler. Iniciando com lista vazia. (" + e.getMessage() + ")");
        }
    }

    public void salvarDados() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(NOME_ARQUIVO))) {
            for (Automovel auto : automoveis) {
                writer.write(auto.toCsvString());
                writer.newLine();
            }
            System.out.println("Dados salvos no arquivo: " + NOME_ARQUIVO);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados no arquivo: " + e.getMessage());
        }
    }

    // --- Funcionalidades CRUD ---

    public Optional<Automovel> buscarPorPlaca(String placa) {
        return automoveis.stream()
                         .filter(auto -> auto.getPlaca().equalsIgnoreCase(placa))
                         .findFirst();
    }

    public boolean incluirAutomovel(Automovel automovel) {
        if (buscarPorPlaca(automovel.getPlaca()).isPresent()) {
            System.out.println("Erro: Placa '" + automovel.getPlaca() + "' já cadastrada.");
            return false;
        }
        automoveis.add(automovel);
        System.out.println("Automóvel incluído com sucesso: " + automovel.getPlaca());
        return true;
    }

    public boolean removerAutomovel(String placa) {
        Optional<Automovel> automovelParaRemover = buscarPorPlaca(placa);
        if (automovelParaRemover.isPresent()) {
            automoveis.remove(automovelParaRemover.get());
            System.out.println("Automóvel com placa '" + placa + "' removido com sucesso.");
            return true;
        } else {
            System.out.println("Erro: Automóvel com placa '" + placa + "' não encontrado.");
            return false;
        }
    }

    public boolean alterarAutomovel(String placa, String novoModelo, String novaMarca, int novoAno, double novoValor) {
        Optional<Automovel> automovelParaAlterar = buscarPorPlaca(placa);
        if (automovelParaAlterar.isPresent()) {
            Automovel auto = automovelParaAlterar.get();
            auto.setModelo(novoModelo);
            auto.setMarca(novaMarca);
            auto.setAno(novoAno);
            auto.setValor(novoValor);
            System.out.println("Dados do automóvel com placa '" + placa + "' alterados com sucesso.");
            return true;
        } else {
            System.out.println("Erro: Automóvel com placa '" + placa + "' não encontrado para alteração.");
            return false;
        }
    }

    public void consultarAutomovel(String placa) {
        Optional<Automovel> automovel = buscarPorPlaca(placa);
        if (automovel.isPresent()) {
            System.out.println("Dados do Automóvel:");
            System.out.println(automovel.get());
        } else {
            System.out.println("Automóvel com placa '" + placa + "' não encontrado.");
        }
    }

    // --- Listagem Ordenada ---

    public void listarAutomoveis(Comparator<Automovel> comparador, String criterio) {
        if (automoveis.isEmpty()) {
            System.out.println("Nenhum automóvel cadastrado.");
            return;
        }
        System.out.println("\n--- Lista de Automóveis Ordenados por " + criterio + " ---");
        List<Automovel> listaOrdenada = new ArrayList<>(automoveis); // Cria cópia para não alterar a original
        listaOrdenada.sort(comparador);
        for (Automovel auto : listaOrdenada) {
            System.out.println(auto);
        }
        System.out.println("----------------------------------------------------");
    }

    // Comparators
    public static Comparator<Automovel> getComparadorPorPlaca() {
        return Comparator.comparing(Automovel::getPlaca);
    }

    public static Comparator<Automovel> getComparadorPorModelo() {
        return Comparator.comparing(Automovel::getModelo, String.CASE_INSENSITIVE_ORDER)
                         .thenComparing(Automovel::getPlaca); // Critério de desempate
    }

    public static Comparator<Automovel> getComparadorPorMarca() {
        return Comparator.comparing(Automovel::getMarca, String.CASE_INSENSITIVE_ORDER)
                         .thenComparing(Automovel::getModelo, String.CASE_INSENSITIVE_ORDER)
                         .thenComparing(Automovel::getPlaca); // Critérios de desempate
    }
}