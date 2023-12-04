import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Classe para gerenciamento das doações de sangues atráves de um arquivo CSV, permite a leitura do CSV
 * exibindo na tela o conteudo do arquivo, permite também a inserção de uma nova doação no arquivo
 * e por fim a deleção de um registro atráves do código de doação informado.
 *
 * @author Artur Cadorin
 * @version 1.0
 * */

public class Doacoes {

    /**
     * Executando o sistema de gerenciamento de doações, recebendo o endereço do arquivo e repassando
     * para as demais funções do sistema.
     */

    public static void main(String[] args) {
        File arquivoCsv = new File("resources/doacoes.csv");
        menuDoacoes(arquivoCsv);
    }

    /**
     * Menu principal do sistema, controle e chamada dos métodos ocorre através de um switch case,
     * controlado pelo usuario através do menu exibido na tela.
     *
     * @param arquivoCsv = File responsável por passar o arquivo CSV.
     */

    private static void menuDoacoes(File arquivoCsv) {
        Scanner teclado = new Scanner(System.in);
        int op = 0;

        while (op != 4) {
            System.out.println("--- Menu de doações ---");
            System.out.println("1 - Mostrar doações");
            System.out.println("2 - Inserir nova doação");
            System.out.println("3 - Excluir doação");
            System.out.println("4 - Sair...");
            System.out.print("Escolha a opção desejada: ");
            op = teclado.nextInt();

            switch (op) {
                case 1:
                    mostrarConteudoArquivo(arquivoCsv);
                    break;
                case 2:
                    inserirDoacao(arquivoCsv);
                    break;
                case 3:
                    deletarDocao(arquivoCsv);
                    break;
                case 4:
                    System.out.println("Encerrando sistema...");
                    System.exit(0);
                default:
                    System.out.println("Opção invalida, digite novamente");
                    break;
            }
        }
        teclado.close();
    }

    /**
     * Printar na tela o conteudo do arquivo CSV, esse método primeiramente recebe o arquivo CSV,
     * logo depois passando como parâmetro para o método lerArquivoCsv(), que realiza a chamada do arquivo em si,
     * por fim o conteudo do arquivo é printado na tela.
     *
     * @param arquivoCsv = File responsável por passar o arquivo CSV.
     */

    private static void mostrarConteudoArquivo(File arquivoCsv) {
        List<String> listaDoacoes = lerArquivoCsv(arquivoCsv);

        System.out.println();
        for (String linha : listaDoacoes) {
            System.out.println(linha);
        }
    }

    /**
     * Leitura do arquivo CSV, método responsável pela abertura do arquivo atráves do endereço informado no método
     * mostrarConteudoArquivo(), é utilizado uma lista de String para representar o conteudo do arquivo.
     *
     * @param arquivoCsv = File responsável por passar o arquivo CSV.
     * @return Uma lista de String com o conteúdo que estava no arquivo informado.
     */

    private static List<String> lerArquivoCsv(File arquivoCsv) {
        List<String> listCsv = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(arquivoCsv))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                listCsv.add(linha);
            }
        } catch (IOException e) {
            System.out.println("Erro na leitura do arquivo...");
        }
        return listCsv;
    }

    /**
     * Inserindo uma nova doação no arquivo com os dados preenchidos pelo usuário, a nova doação
     * será inserida ao final do arquivo.
     *
     * @param arquivoCsv = File responsável por passar o arquivo CSV.
     */

    private static void inserirDoacao(File arquivoCsv) {
        Scanner teclado = new Scanner(System.in);

        System.out.print("\nInsira o código da doação: ");
        int codigo = teclado.nextInt();
        teclado = new Scanner(System.in);
        System.out.print("Nome do doador: ");
        String nome = teclado.nextLine();
        System.out.print("CPF: ");
        String cpf = teclado.nextLine();
        System.out.print("Data de nascimento (yyyy-mm-dd): ");
        String dataNascimento = teclado.nextLine();
        System.out.print("Tipo sanguíneo: ");
        String tipoSangue = teclado.nextLine();
        System.out.print("Quantidade doada (ml): ");
        int qtdDoada = teclado.nextInt();

        String doacao = String.format("%d,%s,%s,%s,%s,%d", codigo, nome, cpf, dataNascimento, tipoSangue, qtdDoada);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivoCsv, true))) {
            bw.write(doacao);
            bw.newLine();
            System.out.println("Doação inserida com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao inserir doação no arquivo...");
        }
    }

    /**
     * Deleção de linha através do código de doação informado, método ira criar uma lista temporária
     * para transferencia do conteudo, inserindo no novo arquivo apenas as linhas que possuem um código
     * diferente do informado para a deleção.
     *
     * @param arquivoCsv = File responsável por passar o arquivo CSV.
     */

    private static void deletarDocao(File arquivoCsv) {
        Scanner teclado = new Scanner(System.in);
        int codigo;

        System.out.print("Informe o código da doação a ser excluída: ");
        int codigoDeletar = teclado.nextInt();

        List<String> arquivo = lerArquivoCsv(arquivoCsv);
        List<String> novoArquivo = new ArrayList<>();

        for (String linha : arquivo) {
            String[] campos = linha.split(",");
            codigo = Integer.parseInt(campos[0]);
            if (codigo != codigoDeletar) {
                novoArquivo.add(linha);
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivoCsv))) {
                for (String novaLinha : novoArquivo) {
                    bw.write(novaLinha);
                    bw.newLine();
                }
            } catch (IOException e) {
                System.out.println("Erro ao deletar a doação...");
            }
        }
        System.out.println("Doação deletada com sucesso.");
    }
}
