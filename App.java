import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        // Inicia a medição de tempo usando nossa chamada nativa
        long startTime = System.nanoTime();
        long[] startTimes = TimeUtils.getProcessTimes(); // [0] = user, [1] = sys

        LinkedList lista = new LinkedList();
        
        carregarListaInicial("lista_inicial.txt", lista);
        executarComandos("comandos.txt", lista);
        
        System.out.println("Programa encerrado. Calculando tempos...");

        // Finaliza a medição de tempo
        long endTime = System.nanoTime();
        long[] endTimes = TimeUtils.getProcessTimes(); // [0] = user, [1] = sys

        // Calcula as durações
        // Tempo real em segundos
        double realDuration = (endTime - startTime) / 1_000_000_000.0;
        // Tempos do Windows são em intervalos de 100ns. Para converter para segundos, dividimos por 10^7.
        double userDuration = (endTimes[0] - startTimes[0]) / 10_000_000.0;
        double sysDuration = (endTimes[1] - startTimes[1]) / 10_000_000.0;

        System.out.println("\n--- Tempos de Execucao ---");
        System.out.printf("real\t%.3fs%n", realDuration);
        System.out.printf("user\t%.3fs%n", userDuration);
        System.out.printf("sys\t%.3fs%n", sysDuration);
    }

    private static void carregarListaInicial(String nomeArquivo, LinkedList lista) {
        try (Scanner leitor = new Scanner(new File(nomeArquivo))) {
            System.out.println("\nLista inicial carregada do arquivo '" + nomeArquivo + "':");
            int i = 0;
            while (leitor.hasNextDouble()) {
                lista.adicionar(leitor.nextDouble(), i++);
            }
            lista.imprimirLista();
        } catch (FileNotFoundException e) {
            System.out.println("AVISO: Arquivo '" + nomeArquivo + "' nao encontrado. A lista comecara vazia.");
        }
    }

    private static void executarComandos(String nomeArquivo, LinkedList lista) {
        try (Scanner leitor = new Scanner(new File(nomeArquivo))) {
            System.out.println("\n--- Executando Comandos do Arquivo ---");

            while (leitor.hasNextLine()) {
                String linha = leitor.nextLine().trim();
                if (linha.isEmpty() || linha.startsWith("#")) {
                    continue;
                }

                System.out.println("> " + linha);
                String[] partes = linha.split("\\s+");
                char acao = partes.length > 0 ? Character.toUpperCase(partes[0].charAt(0)) : ' ';
                
                try {
                    switch (acao) {
                        case 'A':
                            double numeroAdd = Double.parseDouble(partes[1]);
                            int posicao = Integer.parseInt(partes[2]);
                            lista.adicionar(numeroAdd, posicao);
                            break;
                        case 'R':
                            double numeroRem = Double.parseDouble(partes[1]);
                            lista.remover(numeroRem);
                            break;
                        case 'P':
                            lista.imprimirLista();
                            break;
                        default:
                            System.out.println("ERRO: Acao invalida '" + acao + "' encontrada no arquivo.");
                            break;
                    }
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    System.out.println("ERRO DE SINTAXE no comando: " + linha);
                }
            }
            System.out.println("--------------------------------------\n");
        } catch (FileNotFoundException e) {
            System.out.println("ERRO: Nao foi possivel abrir o arquivo de comandos '" + nomeArquivo + "'.");
        }
    }
}