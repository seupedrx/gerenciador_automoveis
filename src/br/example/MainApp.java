package br.example;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MainApp {
    private static GerenciadorAutomoveis gerenciador = new GerenciadorAutomoveis();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int opcao;
        do {
            exibirMenu();
            try {
                opcao = scanner.nextInt();
                scanner.nextLine(); // Consumir nova linha

                switch (opcao) {
                    case 1:
                        incluir();
                        break;
                    case 2:
                        remover();
                        break;
                    case 3:
                        alterar();
                        break;
                    case 4:
                        consultar();
                        break;
                    case 5:
                        listar();
                        break;
                    case 6:
                        gerenciador.salvarDados();
                        System.out.println("Saindo do sistema...");
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, insira um número.");
                scanner.nextLine(); // Limpar buffer do scanner
                opcao = 0; // Resetar opção para continuar no loop
            }
            System.out.println(); // Linha em branco para melhor formatação
        } while (opcao != 6);
        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println("========= CADASTRO DE AUTOMÓVEIS =========");
        System.out.println("1 - Incluir automóvel");
        System.out.println("2 - Remover automóvel");
        System.out.println("3 - Alterar dados de automóvel");
        System.out.println("4 - Consultar automóvel por placa");
        System.out.println("5 - Listar automóveis (ordenado)");
        System.out.println("6 - Salvar e sair");
        System.out.print("Escolha uma opção: ");
    }

    private static void incluir() {
        System.out.println("\n--- Incluir Automóvel ---");
        System.out.print("Placa: ");
        String placa = scanner.nextLine().toUpperCase();
        if (gerenciador.buscarPorPlaca(placa).isPresent()){
            System.out.println("Erro: Placa já existente. A inclusão foi cancelada.");
            return;
        }
        System.out.print("Modelo: ");
        String modelo = scanner.nextLine();
        System.out.print("Marca: ");
        String marca = scanner.nextLine();
        int ano = 0;
        while (true) {
            try {
                System.out.print("Ano: ");
                ano = scanner.nextInt();
                scanner.nextLine(); // Consumir nova linha
                if (ano > 1880 && ano <= java.time.Year.now().getValue() + 1) break; // Validação básica
                System.out.println("Ano inválido. Tente novamente.");
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida para o ano. Por favor, insira um número.");
                scanner.nextLine(); // Limpar buffer
            }
        }
        double valor = 0.0;
         while (true) {
            try {
                System.out.print("Valor (ex: 25000.50): ");
                valor = scanner.nextDouble();
                scanner.nextLine(); // Consumir nova linha
                if (valor >= 0) break;
                System.out.println("Valor não pode ser negativo. Tente novamente.");
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida para o valor. Por favor, insira um número (use vírgula ou ponto como separador decimal dependendo da sua localidade).");
                scanner.nextLine(); // Limpar buffer
            }
        }

        Automovel novoAuto = new Automovel(placa, modelo, marca, ano, valor);
        gerenciador.incluirAutomovel(novoAuto);
    }

    private static void remover() {
        System.out.println("\n--- Remover Automóvel ---");
        System.out.print("Digite a placa do automóvel a ser removido: ");
        String placa = scanner.nextLine().toUpperCase();
        gerenciador.removerAutomovel(placa);
    }

    private static void alterar() {
        System.out.println("\n--- Alterar Dados do Automóvel ---");
        System.out.print("Digite a placa do automóvel a ser alterado: ");
        String placa = scanner.nextLine().toUpperCase();

        if (gerenciador.buscarPorPlaca(placa).isEmpty()) {
            System.out.println("Automóvel com placa '" + placa + "' não encontrado.");
            return;
        }

        System.out.print("Novo Modelo: ");
        String novoModelo = scanner.nextLine();
        System.out.print("Nova Marca: ");
        String novaMarca = scanner.nextLine();
        int novoAno = 0;
        while (true) {
            try {
                System.out.print("Novo Ano: ");
                novoAno = scanner.nextInt();
                scanner.nextLine(); // Consumir
                if (novoAno > 1880 && novoAno <= java.time.Year.now().getValue() + 1) break;
                System.out.println("Ano inválido. Tente novamente.");
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida para o ano. Por favor, insira um número.");
                scanner.nextLine(); // Limpar buffer
            }
        }
        double novoValor = 0.0;
        while (true) {
            try {
                System.out.print("Novo Valor (ex: 25000.50): ");
                novoValor = scanner.nextDouble();
                scanner.nextLine(); // Consumir
                if (novoValor >= 0) break;
                System.out.println("Valor não pode ser negativo. Tente novamente.");
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida para o valor. Por favor, insira um número.");
                scanner.nextLine(); // Limpar buffer
            }
        }
        gerenciador.alterarAutomovel(placa, novoModelo, novaMarca, novoAno, novoValor);
    }

    private static void consultar() {
        System.out.println("\n--- Consultar Automóvel por Placa ---");
        System.out.print("Digite a placa do automóvel: ");
        String placa = scanner.nextLine().toUpperCase();
        gerenciador.consultarAutomovel(placa);
    }

    private static void listar() {
        System.out.println("\n--- Listar Automóveis ---");
        System.out.println("Ordenar por:");
        System.out.println("1 - Placa");
        System.out.println("2 - Modelo");
        System.out.println("3 - Marca");
        System.out.print("Escolha o critério de ordenação: ");
        int criterioOpcao;
        try {
            criterioOpcao = scanner.nextInt();
            scanner.nextLine(); // Consumir nova linha

            switch (criterioOpcao) {
                case 1:
                    gerenciador.listarAutomoveis(GerenciadorAutomoveis.getComparadorPorPlaca(), "Placa");
                    break;
                case 2:
                    gerenciador.listarAutomoveis(GerenciadorAutomoveis.getComparadorPorModelo(), "Modelo");
                    break;
                case 3:
                    gerenciador.listarAutomoveis(GerenciadorAutomoveis.getComparadorPorMarca(), "Marca");
                    break;
                default:
                    System.out.println("Opção de ordenação inválida.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida para o critério de ordenação. Por favor, insira um número.");
            scanner.nextLine(); // Limpar buffer
        }
    }
}