import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

class ClienteComprador {

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


        // Bloco de try para evitar exceções de Input e Output
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
                System.out.println("1) Ver lista de produtos em leilao");
                System.out.println("2) Realizar um lance");
                System.out.println("3) Sair");
                System.out.println("================================================================================\n");
                System.out.print("Sua escolha: ");

                opcao = scanner.nextInt();

                switch (opcao) {
                    case 1: limparConsole();
                            enviarMensagemAoServidor("lista", bufferedReader, bufferedWriter); 
                            int quantidadeDeProdutos = Integer.parseInt(receberMensagem(bufferedReader, bufferedWriter));
                            enviarMensagemAoServidor("quantidade registrada", bufferedReader, bufferedWriter); 

                            if (quantidadeDeProdutos == 0) {
                                System.out.println(receberMensagem(bufferedReader, bufferedWriter));
                            } else {
                                System.out.println("Produtos em leilao:\n");  
                                for (int i=0; i<quantidadeDeProdutos; i++) {                       
                                    System.out.println(receberMensagem(bufferedReader, bufferedWriter));
                                }
                            }
                            break;

                    case 2: limparConsole();
                            enviarMensagemAoServidor("lance", bufferedReader, bufferedWriter);
                            System.out.print("Digite o nome do produto para o qual voce deseja realizar um lance: ");
                            scanner.nextLine();
                            String nomeDoProduto = scanner.nextLine();
                            enviarMensagemAoServidor(nomeDoProduto, bufferedReader, bufferedWriter);

                            if (receberMensagem(bufferedReader, bufferedWriter).equals("tem produto")) {
                                System.out.print("Digite o valor do seu lance: ");
                                double lance = scanner.nextDouble();
                                String stringLance = String.valueOf(lance);
                                enviarMensagemAoServidor(stringLance, bufferedReader, bufferedWriter);

                                if (receberMensagem(bufferedReader, bufferedWriter).equals("lance bom")) {
                                    System.out.print("Digite o seu email para contato: ");
                                    scanner.nextLine();
                                    String email = scanner.nextLine();
                                    enviarMensagemAoServidor(email, bufferedReader, bufferedWriter);
                                    System.out.println("\n\nLance realizado com sucesso!");
                                    System.out.println(receberMensagem(bufferedReader, bufferedWriter));
                                } else {
                                    System.out.print("Digite o seu email para contato: ");
                                    scanner.nextLine();
                                    String email = scanner.nextLine();
                                    enviarMensagemAoServidor(email, bufferedReader, bufferedWriter);
                                    System.out.println("\n\nLance realizado com sucesso!");
                                    System.out.println(receberMensagem(bufferedReader, bufferedWriter));
                                }

                            } else {
                                System.out.println("\n\nNao existe um produto com este nome em leilao...");
                            }
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