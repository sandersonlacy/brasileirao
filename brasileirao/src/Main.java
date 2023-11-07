import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Dados.n_rodadas = 33;
            Dados dados = new Dados();
            dados.computaDados(".." + File.separator + "brasileirao" + File.separator + 
            "data" + File.separator + "rodadas_data.3.txt");
            new InterfaceTabela();
        } catch (IOException e) {
            System.out.println("Ocorreu uma exceção " + e);
        }
    }
}
