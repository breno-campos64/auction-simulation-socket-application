import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

class ClienteVendedor {

    public static void main(String[] args) {
        
        // Definindo as constantes que serão fundamentais para o funcionamento do cliente
        final int PORTA = 6464;
        final String ENDERECO_DO_SERVIDOR = "localhost"; // Troque o "localhost" pelo endereço IPv4 do servidor

        // Declarando as variáveis necessárias para o cliente se comunicar com o servidor
        Socket socket = null;
        InputStreamReader inputStreamReader = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;


        // Bloco de try para evitar exceções de Input e Outputs
        try {

            // Inicializando o socket com as constantes definidas acima
            socket = new Socket(ENDERECO_DO_SERVIDOR, PORTA);

            // Permitindo que o Socket receba e envie mensagens
            inputStreamReader = new InputStreamReader(socket.getInputStream());
            outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());

            bufferedReader = new BufferedReader(inputStreamReader);
            bufferedWriter = new BufferedWriter(outputStreamWriter);

            // Criando um scanner para permitir que o usuário utilize o teclado
            Scanner scanner = new Scanner(System.in);
            
            // Variável para guardar a opção que cliente escolhe no menu
            int opcao = 0;

            // Deixando o console mais limpo para o iniciar a aplicação
            limparConsole();

            // Menu da interface do cliente comprador
            do {    
                System.out.println("================================================================================");
                System.out.println("BEM VINDO AO APLICATIVO DE LEILAO!");
                System.out.println("Por favor, Selecione uma das opcoes abaixo: ");
                System.out.println("1) Iniciar novo leilao");
                System.out.println("2) Finalizar um leilao");
                System.out.println("3) Sair");
                System.out.println("================================================================================\n");
                System.out.print("Sua escolha: ");

                opcao = scanner.nextInt();

                switch (opcao) {
                    case 1: limparConsole();
                            enviarMensagemAoServidor("novo", bufferedReader, bufferedWriter);
                            System.out.print("Digite o nome do produto a ser leiloado: ");
                            scanner.nextLine();
                            String nomeDoProdutoASerLeiloado = scanner.nextLine();
                            enviarMensagemAoServidor(nomeDoProdutoASerLeiloado, bufferedReader, bufferedWriter);
                            System.out.print("Digite a descricao do produto a ser leiloado: ");
                            String descricaoDoProdutoASerLeiloado = scanner.nextLine();
                            enviarMensagemAoServidor(descricaoDoProdutoASerLeiloado, bufferedReader, bufferedWriter);
                            System.out.print("Digite o lance inicial do produto a ser leiloado: ");
                            double lanceInicialDoProdutoASerLeiloado = scanner.nextDouble();
                            enviarMensagemAoServidor(converterMensagemDouble(lanceInicialDoProdutoASerLeiloado), bufferedReader, bufferedWriter);
                            System.out.println("\n");
                            System.out.println(receberMensagem(bufferedReader, bufferedWriter));
                            break;

                    case 2: limparConsole();
                            enviarMensagemAoServidor("finalizar", bufferedReader, bufferedWriter);
                            System.out.print("Digite o nome do produto cujo leilao sera finalizado: ");
                            scanner.nextLine();
                            String nomeDoLeilaoASerFinalizado = scanner.nextLine();
                            enviarMensagemAoServidor(nomeDoLeilaoASerFinalizado, bufferedReader, bufferedWriter);                       
                            System.out.println("\n");
                            System.out.println("Leilao finalizado com sucesso!");
                            System.out.println(receberMensagem(bufferedReader, bufferedWriter));
                            break;                        

                    case 3: limparConsole();
                            enviarMensagemAoServidor("desconectar", bufferedReader, bufferedWriter);
                            System.out.println("DESCONECTADO");
                            break;

                    default: limparConsole();
                             System.out.println("Por favor, escolha uma opcao entre 1, 2 ou 3");
                             break;
                }

            } while(opcao != 3);

            scanner.close();

        } catch (IOException e) {
            e.printStackTrace();
        } 
        try {
            if (socket != null) socket.close();
            if (inputStreamReader != null) inputStreamReader.close();
            if (outputStreamWriter != null) outputStreamWriter.close();
            if (bufferedReader != null) bufferedReader.close();
            if (bufferedWriter != null) bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Método para enviar mensagens ao servidor do leilao
    public static void enviarMensagemAoServidor(String mensagemASerEnviada, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            bufferedWriter.write(mensagemASerEnviada);
            bufferedWriter.newLine();
            bufferedWriter.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    // Método para converter mensagem de int para String
    public static String converterMensagemInt(int mensagemASerConvertida) {
        return String.valueOf(mensagemASerConvertida);
    }


    // Método para converter mensagem de double para String
    public static String converterMensagemDouble(double mensagemASerConvertida) {
        return String.valueOf(mensagemASerConvertida);
    }


    // Método para receber mensagem do servidor
    public static String receberMensagem(BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            return bufferedReader.readLine();

        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }


    // Método para limpar o console
    public static void limparConsole() {  
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    } 
}
