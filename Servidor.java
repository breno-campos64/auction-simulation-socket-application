import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor {
    
    public static void main(String[] args) throws IOException {
        
        // Definindo a constants que seá fundamental para o funcionamento do servidor
        final int PORTA = 6464;

        // Declarando as variáveis necessárias para o cliente se comunicar com o servidor
        Socket socket = null;
        ServerSocket serverSocket = null;
        InputStreamReader inputStreamReader = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        ArrayList<Produto> produtosEmLeilao = new ArrayList<>();

        // Iniciando o socket do servidor
        serverSocket = new ServerSocket(PORTA);

        System.out.println("SERVIDOR LIGADO");
        System.out.println("Aguardando cliente...");

        while (true) {

            try {   
                // Permitindo que o servidor aceite a conexão de cliente afora
                socket = serverSocket.accept();

                // Permitindo que o Socket receba e envie mensagens
                inputStreamReader = new InputStreamReader(socket.getInputStream());
                outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());

                bufferedReader = new BufferedReader(inputStreamReader);
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                while (true) {
                    // O servidor irá esperar, neste ponto, algum cliente se conectar e requisitar algo
                    System.out.println("reset");
                    String mensagemDoCliente = bufferedReader.readLine();
                    System.out.println("CLIENTE: " + mensagemDoCliente);

                    // O cliente escolheu ver a lista de produtos em leilao
                    if (mensagemDoCliente.equals("lista")) {
                        int quantidade = 0;

                        for (int i=0; i<produtosEmLeilao.size(); i++) {
                           quantidade++;
                        }

                        enviarMensagemAoCliente(converterMensagemInt(quantidade), bufferedReader, bufferedWriter);
                        String mensagem = bufferedReader.readLine();

                        if (quantidade == 0) {
                             enviarMensagemAoCliente("Nao existem produtos em leilao...", bufferedReader, bufferedWriter);
                        } else {
                            for (int i=0; i<quantidade; i++) {
                                enviarMensagemAoCliente(produtosEmLeilao.get(i).toString(), bufferedReader, bufferedWriter);
                            }
                        }
                    }


                    // O cliente escolheu fazer um lance para um produto em leilao
                    if (mensagemDoCliente.equals("lance")) {
                        String produtoEscolhidoPeloCliente = bufferedReader.readLine();
                        boolean temProdutoNaLista = false;

                        for (Produto produto : produtosEmLeilao) {
                            if (produtoEscolhidoPeloCliente.equalsIgnoreCase(produto.getNome())) {    
                                temProdutoNaLista = true;
                                break;
                            }
                        }

                        if (temProdutoNaLista) {
                            enviarMensagemAoCliente("tem produto", bufferedReader, bufferedWriter);

                            double lanceDoCliente = Double.parseDouble(bufferedReader.readLine());

                            for (Produto produto : produtosEmLeilao) {
                                if (produtoEscolhidoPeloCliente.equalsIgnoreCase(produto.getNome())) {
                                    if (lanceDoCliente > produto.getMaiorLance()) {
                                        enviarMensagemAoCliente("lance bom", bufferedReader, bufferedWriter);
                                        String emailDoCliente = bufferedReader.readLine();
                                        enviarMensagemAoCliente("O seu lance eh o maior ate o momento!", bufferedReader, bufferedWriter);
                                        produto.setMaiorLance(lanceDoCliente);
                                        produto.setEmailDoCliente(emailDoCliente);
                                        break;

                                    } else {
                                        enviarMensagemAoCliente("lance ruim", bufferedReader, bufferedWriter); 
                                        String emailDoCliente = bufferedReader.readLine();     
                                        enviarMensagemAoCliente("O seu lance nao eh o maior...", bufferedReader, bufferedWriter);                                           
                                        break;
                                    }
                                }
                            }

                        } else {
                            enviarMensagemAoCliente("nao tem produto", bufferedReader, bufferedWriter);
                        }
                    }                 


                    // O cliente escolheu iniciar um novo leilao
                    if (mensagemDoCliente.equals("novo")) {
                        String nomeDoProdutoDoLeilao = bufferedReader.readLine();
                        String descricaoDoProdutoDoLeilao = bufferedReader.readLine();
                        double lanceMinimoDoProdutoDoLeilao = Double.parseDouble(bufferedReader.readLine());
                        produtosEmLeilao.add(new Produto(nomeDoProdutoDoLeilao, descricaoDoProdutoDoLeilao, lanceMinimoDoProdutoDoLeilao));
                        enviarMensagemAoCliente("Novo leilao criado com sucesso!", bufferedReader, bufferedWriter);
                    }


                    // O cliente escolheu finalizar um leilao
                    if (mensagemDoCliente.equals("finalizar")) {
                        String leilaoASerFinalizadoPeloCliente = bufferedReader.readLine();
                        boolean temProdutoNaLista = false;

                        for (Produto produto : produtosEmLeilao) {
                            if (leilaoASerFinalizadoPeloCliente.equalsIgnoreCase(produto.getNome())) {
                                temProdutoNaLista = true;
                                break;
                            }
                        }
                             
                        if (temProdutoNaLista)  {
                            for (Produto produto : produtosEmLeilao) {
                                if (leilaoASerFinalizadoPeloCliente.equalsIgnoreCase(produto.getNome())) {
                                    if (produto.getEmailDoCliente() != null) {                                       
                                        StringBuffer stringBuffer = new StringBuffer();
                                        stringBuffer.append("Contato do vencedor: ");
                                        stringBuffer.append(produto.getEmailDoCliente());
                                        stringBuffer.append(" | ");
                                        stringBuffer.append("Lance do vencedor: ");
                                        stringBuffer.append(String.format("%.2f",produto.getMaiorLance()));
                                        stringBuffer.append(" reais");
                                        enviarMensagemAoCliente(stringBuffer.toString(), bufferedReader, bufferedWriter);
                                        produtosEmLeilao.remove(produto);
                                        break;

                                    } else {
                                        enviarMensagemAoCliente("Nao houveram lances para este produto...", bufferedReader, bufferedWriter);                                       
                                        produtosEmLeilao.remove(produto);
                                        break;
                                    }       
                                }
                            }

                        } else {
                            enviarMensagemAoCliente("Nao existe um produto com este nome em leilao...", bufferedReader, bufferedWriter);
                        }
                    }


                    // O cliente escolheu finalizar a conexão com o servidor
                    if (mensagemDoCliente.equals("desconectar")) {
                        socket.close();
                        inputStreamReader.close();
                        outputStreamWriter.close();
                        bufferedReader.close();
                        bufferedWriter.close();
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println("CLIENTE: desconectar");
            }
        }
    }


    // Método para enviar mensagens ao cliente
    public static void enviarMensagemAoCliente(String mensagemASerEnviada, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
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
}

