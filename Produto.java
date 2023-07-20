public class Produto {

    private String nome;
    private String descricao;
    private String emailDoCliente;
    private double maiorLance;


    // Construtor
    public Produto(String nome, String descricao, double maiorLance) {
        this.nome = nome;
        this.descricao = descricao;
        this.maiorLance = maiorLance;
    }


    // Gets e Sets
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getMaiorLance() {
        return maiorLance;
    }

    public String getEmailDoCliente() {
        return emailDoCliente;
    }

    public void setEmailDoCliente(String emailDoCliente) {
        this.emailDoCliente = emailDoCliente;
    }

    public void setMaiorLance(double maiorLance) {
        this.maiorLance = maiorLance;
    }
    
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }


    // Método para atribuir uma string característica ao Objeto
    @Override
    public String toString() {
        return "Nome: '" + nome + "' | Descricao: '" + descricao + "' | Maior Lance: '" + String.format("%.2f", maiorLance) + " reais'";
    }
}
