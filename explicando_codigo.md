## Por dentro do código
### *Java*
##### Autor do código em Java: Sanderson Lacy Alves dos Santos.

### Construção do Código
Antes de compreender a lógica do código, é necessário o entendimento da entidade principal do programa: o Time!
O Time é uma classe simples composta pelos atributos: Nome, sigla, historico, n_rodadas e stats, em que stats é um array com os dados de cada time como partidas jogadas, vitórias, derrotas e etc. O funcionamento da classe e seus atributos serão explicados no decorrer da documentação.
Por ora, apenas é necessário saber que os objetos da classe Time são peças chave na compreessão do código. A primeira parte da lógica por trás do script é obviamente a leitura e tratamento dos dados, e todas operações relacionadas a isso são realizadas na classe Dados. Dados herda Time pois como estamos trabalhando com 20 times, faz sentido que só uma classe monopolize todos os objetos time, caso contrário o acesso a informações ficaria bem mais verboso. A lógica principal do acesso a times por meio de Dados é essa: Dois arrays são definidos na Dados: "array_nomes" e "array_siglas". Quando o objeto Dados é instanciado, o construtor se encarrega de inicializar um array do tipo Time sendo passado como argumentos os respectivos nomes e siglas.
```
public class Dados extends Time{
    private Historico calculador_historico;
    protected static String chaves_ordenadas[];
    protected static Time times[];
    protected static final String array_siglas[] = {"FLA", "PAL", "FLU", "SAO", "SAN", "CAM", "CAP", "CFC", "COR", "FOR", "BOT", "JUV",
    "CUI", "GOI", "BRA", "ACG", "INT", "CEA", "AVA", "AMG"};
    protected static final String array_nomes[] = {"Flamengo", "Palmeiras", "Fluminense", "São Paulo", "Santos", "Atlético Mineiro",
    "Athlético Paranaense", "Coritiba", "Corinthians", "Fortaleza", "Botafogo", "Juventude", "Cuiabá", "Goiás", "Bragantino", "Atlético Goianiense", "Internacional", "Ceará", "Avaí", "América Mineiro"};

public Dados(){
    calculador_historico = new Historico();
    times = new Time[20];
    for (int i=0; i<20; i++){//CRIA O OBJETO TIME COM O NOME, SIGLA E POSICAO DOS ARRAYS ACIMA
        times[i] = new Time(array_nomes[i], array_siglas[i]);
    }
}
```
Alguns detalhes precisam ser levados em consideração: Primeiramente, o atributo "final" dos arrays de nomes e siglas. Não há ponteiros para os times, ou seja o acesso aos objetos ocorre exclusivamente pelo índice dos array times e esse índice é fornecido pelos arrays de nome e sigla, pois são todos iguais. Por isso o "final" é essencial, pois caso alguma das posicões fossem alteradas, o acesso aos objetos time estaria comprometido. Segundamente, o atributo "static" dos arrays de nomes, siglas e times. Como Dados é uma classe que monopoliza o acesso a times e os dados dos times são necessários em praticamente toda parte do programa, o atributo static é crucial, pois com ele não precisamos passar o objeto Dados de classe em classe, sendo necessário apenas fazer referência a Dados. Isso garante acesso fácil e rápido dos dados de times em qualquer parte do código! Para acesso a esses objetos, dois métodos da classe Dados são utilizados: getTimeByNome e getTimeBySigla:
```
    public static Time getTimeBySigla(String sigla){
        return times[Arrays.asList(array_siglas).indexOf(sigla)];
    }

    public static Time getTimeByNome(String nome){
        return times[Arrays.asList(array_nomes).indexOf(nome)];
    }
```

Perceba que o retorno do objeto se dá de acordo com a posição do argumento passado nos respectivos arrays.
Entendido como o acesso a objetos time se dá, o resto do código é bem explicativo, mas alguns pontos merecem ressalva:
### Leitura e Tratamento dos Dados
Obviamente, precisamos de uma base de dados onde as informações de resultados das rodadas são retirados. A base utilizada em Java foi um arquivo de texto simples pois não enxergamos necessidade de uma implementação mais sofisticada nessa parte do programa. Felizmente o Java possui diversas classes e métodos para captura de dados em arquivos externos. A abordagem utilizada foi usando o BufferedReader junto com o FileReader fornecidos pelo java.io. A leitura e tratamento dos dados ocorrem quase concomitantemente. Isso é feito para evitar a criação de novas variáveis para armazenamento e uso posterior. A leitura e processamento é realizada no método computaDados definido na classe Dados, em que o único argumento pra ela é uma string informando o nome do arquivo de texto.
```
    public void computaDados(String nome_arq) throws IOException{//realiza os calculos de dados
        BufferedReader buffer = new BufferedReader(new FileReader(nome_arq));
        for(int i=0; i<33; i++){
            String jogos[] = buffer.readLine().split(",");
            for (int j=0; j<10; j++){
                Time time1 = getTimeBySigla(jogos[j].substring(0, 3));
                Time time2 = getTimeBySigla(jogos[j].substring(6, 9));

                int gol_time1 = Integer.parseInt(jogos[j].substring(4, 5));
                int gol_time2 = Integer.parseInt(jogos[j].substring(10,11));
```
Dois laços "for" são utilizados, um varrendo as rodadas no arquivo, e outro varrendo um array temporário criado para armazenar os jogos de cada rodada. Nesse segundo "for" objetos do tipo Time são criados para receber os times em que o confronto está sendo processado, junto a duas variáveis para armazenamento dos gols dos times. A partir disso, uma sequência "if else" é iniciada para comparação dos gols e atualização das estatísticas dos times utilizando os getters e setters definidos na classe Time.

```
    if (gol_time1 > gol_time2){
        //TIME 1 VENCEU
        time1.setPontos(time1.getPontos() + 3);//contabiliza pontos
        time1.setPartidas(time1.getPartidas() + 1);//contabiliza partidas
        time1.setVitorias(time1.getVitorias() + 1);//VITORIA
        time1.setSaldo(time1.getSaldo() + (gol_time1 - gol_time2));//contabiliza saldo
        time1.setGolsPro(time1.getGolsPro() + gol_time1);//contabiliza gols pro
        time1.setGolsContra(time1.getGolsContra() + gol_time2);//contabiliza gols contra

        //TIME 2 PERDEU
        time2.setPontos(time2.getPontos() + 0);//contabiliza pontos
        time2.setPartidas(time2.getPartidas() + 1);//contabiliza partidas
        time2.setDerrotas(time2.getDerrotas() + 1);//DERROTA
        time2.setSaldo(time2.getSaldo() getGolsPro+ (gol_time2 - gol_time1));//contabiliza saldo
        time2.setGolsPro(time2.() + gol_time2);//contabiliza gols pro
        time2.setGolsContra(time2.getGolsContra() + gol_time1);//contabiliza gols contra
    }
    else if (gol_time1 < gol_time2){
        //TIME 2 VENCEU
        time2.setPontos(time2.getPontos() + 3);//contabiliza pontos
        time2.setPartidas(time2.getPartidas() + 1);//contabiliza partidas
        time2.setVitorias(time2.getVitorias() + 1);//VITORIA
        time2.setSaldo(time2.getSaldo() + (gol_time2 - gol_time1));//contabiliza saldo
        time2.setGolsPro(time2.getGolsPro() + gol_time2);//contabiliza gols pro
        time2.setGolsContra(time2.getGolsContra() + gol_time1);//contabiliza gols contra 

        //TIME 1 PERDEU
        time1.setPontos(time1.getPontos() + 0);//contabiliza pontos
        time1.setPartidas(time1.getPartidas() + 1);//contabiliza partidas
        time1.setDerrotas(time1.getDerrotas() + 1);//DERROTA
        time1.setSaldo(time1.getSaldo() + (gol_time1 - gol_time2));//contabiliza saldo
        time1.setGolsPro(time1.getGolsPro() + gol_time1);//contabiliza gols pro
        time1.setGolsContra(time1.getGolsContra() + gol_time2);//contabiliza gols contra
    }
    else{
        //TIME 1 EMPATOU
        time1.setPontos(time1.getPontos() + 1);//contabiliza pontos
        time1.setPartidas(time1.getPartidas() + 1);//contabiliza partidas
        time1.setEmpates(time1.getEmpates() + 1);//EMPATE
        time1.setSaldo(time1.getSaldo() + (gol_time1 - gol_time2));//contabiliza saldo
        time1.setGolsPro(time1.getGolsPro() + gol_time1);//contabiliza gols pro
        time1.setGolsContra(time1.getGolsContra() + gol_time2);//contabiliza gols contra

        //TIME 2 EMPATOU
        time2.setPontos(time2.getPontos() + 1);//contabiliza pontos
        time2.setPartidas(time2.getPartidas() + 1);//contabiliza partidas
        time2.setEmpates(time2.getEmpates() + 1);//EMPATE
        time2.setSaldo(time2.getSaldo() + (gol_time2 - gol_time1));//contabiliza saldo
        time2.setGolsPro(time2.getGolsPro() + gol_time2);//contabiliza gols pro
        time2.setGolsContra(time2.getGolsContra() + gol_time1);//contabiliza gols contra 
```
### Criação do Histórico de Tabelas
Com os dados atualizados em seus respectivos objetos, já temos, ainda que de forma primitiva, os dados da tabela final de cada time. Porém, não temos uma forma de classificar os times formando um ranking, e neste momento os dados referentes a isso estão perdidos. Em Java, a implementação adotada foi utilizando duas classes internas a Dados: OrdenaDados e Historico, ambas públicas. Foi escolhido tornar as classes internas para, como foi dito anteriormente, monopolizar o tratamento de dados em só uma classe, reduzindo assim a complexidade.
A classe OrdenaTimes tem um único método com apenas uma função: dada a atual situação dos objetos time, criar uma tabela de classificação para eles. Para criação dessa tabela foi criado um array de string chamado chaves, em que toda vez que o método fosse invocado, esse array seria retornado informando com base em seus índices a posição de cada time na tabela.
```
    public class OrdenaTimes{
        protected String chaves[];
        OrdenaTimes(){
            chaves = new String[20];
            for (int k=0; k<20; k++){//fazendo uma copia das siglas
                chaves[k] = array_siglas[k];
            }
        }
        public String[] inOrder(){
            for (int i=1;i<chaves.length;i++){
                for (int j=0; j<i; j++){
                    Time time1 = getTimeBySigla(chaves[i]);
                    Time time2 = getTimeBySigla(chaves[j]);
                    try{//Aplicacao do algoritmo de ordenacao BUBBLE SORT com 3 criterios de desempate
                        if (time1.getPontos() > time2.getPontos()){//ordena por pontos
                            String temp = chaves[i];
                            chaves[i] = chaves[j];
                            chaves[j] = temp;
                        }
                        else if (time1.getVitorias() > time2.getVitorias() && time1.getPontos() == time2.getPontos()){
                            String temp = chaves[i];
                            chaves[i] = chaves[j];
                            chaves[j] = temp;
                        }
                        else if(time1.getSaldo() > time2.getSaldo() && time1.getPontos() == time2.getPontos() && time1.getVitorias() == time2.getVitorias()){//ordena por saldo
                            String temp = chaves[i];
                            chaves[i] = chaves[j];
                            chaves[j] = temp;
                        }
                        else if (time1.getSaldo() == time2.getSaldo() && time1.getPontos() == time2.getPontos() && time1.getGolsPro() > time2.getGolsPro() && time1.getVitorias() == time2.getVitorias()){//ordena por vitoria
                            String temp = chaves[i];
                            chaves[i] = chaves[j];
                            chaves[j] = temp;
                        }
                    }
                    catch(NullPointerException exception){
                        System.out.println("Ponteiro Nulo");
                    }
                }
            }
        return chaves;
        }
```
Foi utilizado para classificação dos times o algoritmo de ordenação "Bubble Sort", com 3 critérios de desempate:
Vitorias, saldo de gols e gols pró. Agora que temos uma forma de calcular a tabela de classificação, precisamos de uma maneira de calcular o historico de cada time durante a competição. A classe Historico serve para resolver esse problema, e usando uma instância de OrdenaTimes consegue atualizar o historico de cada time à medida que os dados estão sendo computados.

```   public class Historico{//cria um array de inteiros pra cada objeto time
        protected OrdenaTimes times_ordenados;
        public Historico(){
            times_ordenados = new OrdenaTimes();
        }

        public void calculaHistorico(int rodada){//funcao usada a cada rodada
            String chaves[] = times_ordenados.inOrder();
            for (String p : chaves){
                getTimeBySigla(p).setHistorico(rodada, Arrays.asList(chaves).indexOf(p) + 1);//atualizando historico a cada rodada
            }
            if ((rodada + 1) == Dados.n_rodadas){
                chaves_ordenadas = chaves;
            }
        }
    }
```
A classe recebe as chaves de OrdenaTimes contendo a classificação dos times naquele momento na tabela, e atualiza o historico de cada um dos times com o seu indice no array chaves + 1.
Dois detalhes são importantes: A classe Dados tem uma instância de Historico e com seu objeto, a cada iteração dos "for" do método computaDados, atualiza os historicos dos times. E Dados também tem um atributo estático chaves_ordenadas para tornar acessível o resultado final da tabela para todo o programa. chaves_ordenadas é atribuído no último "if" de Historico, observe:
```
    if ((rodada + 1) == Dados.n_rodadas){
        chaves_ordenadas = chaves;
    }
```
Agora finalmente temos uma maneira de calcular dados e métricas de maneira eficiente

### Interface da Tabela
A interface da tabela foi feita usando uma JTable provida pelo Swing. A classe InterfaceTabela herda um JFrame e se responsabiliza por criar uma instância da JTable e realizar as configurações necessárias para funcionamento correto da tabela, como torná-la visível, colocar em modo tela cheia, configurar largura de linha, desligar realinhamento de coluna, "setar" o renderizador e modelo de tabela(serão contemplados posteriormente), entre outras coisas.
```
public class InterfaceTabela extends JFrame{
    private int largura_linha = 34;//em pixels
    private JTable tbl;
    private JScrollPane scroll;

    public InterfaceTabela(){
        initComponents();//inicializa os componentes
    }

    private void initComponents(){//inicializa os componentes da tabela
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//fecha a janela ao sair
        tbl = new JTable();
        scroll = new JScrollPane(tbl);
        tbl.setModel(new ModeloTabela());
        //tbl.setPreferredSize(new Dimension(400, 400));
        tbl.setRowHeight(largura_linha);
        this.add(scroll);
        this.setVisible(true);
        this.setResizable(true);
        this.setBackground(new Color(34, 40, 49));
        tbl.setDefaultRenderer(Object.class, new RenderizaTabela());//setando renderizador
        tbl.getTableHeader().setReorderingAllowed(false);//desligando realinhamento de coluna!
        EventoDeMouse k = new EventoDeMouse(tbl);
        tbl.addKeyListener(k);
        tbl.addMouseListener(k);
        this.setTitle("Brasileirão Série A 2022");
    }
}
```
Atenção para os métodos seDefaultRenderer, setModel, addKeyListener e addMouseListener, pois eles que são responsáveis pelo funcionamento correto da tabela.
A JTable possui um Model padrão onde as configurações padrão são utilizadas, e para usá-lo é necessário apenas passar os dados da tabela como argumento na hora de instanciar a JTable. Porém essa implementação é limitada por duas razões: Não poderíamos customizar os dados da tabela e a tabela ficaria surpreendentemente mais pesada, pois seriam usadas classes sincronizadas e dados duplicados, sendo necessário mais memória para seu funcionamento. Por isso a necessidade da criação de um modelo de tabela específico para o programa. Este modelo está contido na classe ModeloTabela que herda AbstractTableModel, e com isso, podemos sobrescrever os métodos da superclasse, e modelar os dados da forma que quisermos, obedecendo apenas os parâmetros e retornos dos métodos sobrescritos. Depois de "setar" o Model, o JTable se encarrega de interpretar os dados passados.

```public class ModeloTabela extends AbstractTableModel{
    private String colunas[] = {"Time", "Pontos", "Partidas", "Vitórias", "Empates", "Derrotas",
    "Saldo de Gols", "Gols Pró", "Gols Contra", " "};//ultima coluna vazia para botao
    //SOBRESCREVENDO OS METODOS DE AbstractTableModel
    @Override
    public int getRowCount() {
        return Dados.array_siglas.length;
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }
    @Override
    public String getColumnName(int num){
        return colunas[num];
    }
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {//desligando a edicao de celula
        return false;
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0){//retornando para a coluna 0 dados formatados com a colocacao
            String time = Dados.getTimeBySigla(Dados.chaves_ordenadas[rowIndex]).getNome();
            return (rowIndex + 1) + "º " + time;
        }
        else if (columnIndex == 9){//caso a coluna seja a ultima, e retornado valor nulo para 
            return null;//ser renderizado como botao
        }
        else{
            return Dados.getTimeBySigla(Dados.chaves_ordenadas[rowIndex]).getStats()[columnIndex -1];
        }
    }
}
```
Adendo especial para o método getValueAt, ele deve retornar um object dado um indice de linha e de coluna.
Para a formatação tabela ficar da maneira que desejamos, perceba que adicionamos no array de colunas uma última coluna de nome vazio. Essa coluna ao passar pelo pelo segundo "if" do getValueAt, é retornado um objeto nulo, pra posteriormente ser renderizado como um botão. Agora vamos ao renderizador
```
public class RenderizaTabela implements TableCellRenderer{//renderiza a tabela

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,int row, int column){
        table.getTableHeader().setBackground(new Color(57, 62, 70));//seta cor do background do header
        table.getTableHeader().setForeground(new Color(238, 238, 238));//seta cor da fonte do header
        if (value != null){//caso nao seja a ultima coluna cria um label dentro de um panel com o valor de value
            JPanel p = new JPanel();
            p.setBackground(new Color(34, 40, 49));
            JLabel l = new JLabel(value.toString());
            l.setForeground(new Color(238, 238, 238));
            p.add(l);
            return p;
        }
        JButton b = new JButton("Detalhes");
        b.setBackground(new Color(0, 173, 181));
        return b;//retorna o botao detalhes
    }
}
```
A função desta classe é bastante simples: Renderizar todos os componentes da tabela. Ou seja, todos headers e células necessariamente passam por ela, isso nos permite um enorme poder de customização da tabela, pois podemos retornar Components da forma que desejarmos. E é exatamente isso que fazemos: caso o valor o Object seja nulo, ele é renderizado com um botão de nome Detalhes(exatamente a última coluna da tabela, como estabelecemos no modelo), caso contrário, retornamos um JLabel dentro de um JPanel com o valor passado para string.
Com a nossa tabela instanciada, modelada e devidamente renderizada, precisamos de uma maneira de quando o usuário clique no botão detalhes, seja criado um novo frame com os gráficos do time selecionado.
Adendo: Como o botão é retornado a uma JTable, ele não é reponsivo a actionListeners, já que ele não é exatamente um botão mas sim um frame de um botão que foi adicionado à JTable. Para resolver esse impasse, foram "setados" listeners para o mouse e teclado para que seja detectado quando o usuário clicou na tabela. Todos eventos são gerenciados pela classe EventoDeMouse que herda MouseAdapter e implementa KeyListener para suprir essa necessidade. Nela, são sobrescritos os métodos que geram interrupções de sistema quando o mouse e teclado são acionados na tabela.
```
public class EventoDeMouse extends MouseAdapter implements KeyListener{
    private JTable tabela;

    public EventoDeMouse(JTable tabela){
        this.tabela = tabela;

    }
    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER){//tecla clicada (ENTER) na coluna 9
            if (tabela.getSelectedColumn() == 9){
                String sigla_time = "";
                String pos_time = (String) tabela.getValueAt(tabela.getSelectedRow(), 0);
                if (pos_time.substring(1, 2).equals("º")){
                    sigla_time = Dados.traducao(pos_time.substring(3, pos_time.length()));//ignorando a posicao do time
                }
                else{
                    sigla_time = Dados.traducao(pos_time.substring(4, pos_time.length()));//ignorando a posicao do time
                }
                new CriaFrame(new CriaGrafico(sigla_time, Dados.getTimeBySigla(sigla_time).getHistorico()));//cria o gráfico
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {   
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (tabela.getSelectedColumn() == 9){//mouse clicado na coluna 9
            String sigla_time = "";
            String pos_time = (String) tabela.getValueAt(tabela.getSelectedRow(), 0);
            if (pos_time.substring(1, 2).equals("º")){
                sigla_time = Dados.traducao(pos_time.substring(3, pos_time.length()));//ignorando a posicao do time
            }
            else{
                sigla_time = Dados.traducao(pos_time.substring(4, pos_time.length()));//ignorando a posicao do time
            }
            new CriaFrame(new CriaGrafico(sigla_time, Dados.getTimeBySigla(sigla_time).getHistorico()));//cria o gráfico
        }
    }
}
```
Os métodos keyPressed e mouseReleased são resposáveis pela detecção do clique, porém ele é detectável a qualquer ponto da tabela, ou seja, caso o mouse seja acionado em qualquer lugar, o evento é criado. Em outras palavras, é necessário um filtro para que o frame seja criado apenas quando clicado no botão na última coluna. Por isso nos dois métodos são adicionados "ifs" que filtram quando a coluna foi selecionada foi realmente a última(com uma pequena adição de "if" ao keyPressed para que seja responsivo a apenas cliques ENTER). Com a coluna e linha da célula selecionada, podemos capturar o dado da tabela e traduzir usando o método estático "traduzir" da classe Dados, que converte siglas em nomes e vice-versa, conseguindo assim, a sigla do time cujo botão detalhes foi selecionado. A partir daí é criado uma instância da classe criadora do gráfico e do frame de gráfico: CriaGrafico e CriaFrame.
### Criação do Gráfico
A função da classe CriaGrafico é simples: Criar um Painel e pintar o graficos neste com os dados de historico e sigla do time passados como argumentos para ele. Para isso sobrecarregamos o método paint do JPanel e dividimos as operação de pintura em 5 funções diferentes:
insereEixos, insereRotulos, insereIndicadores, insereEscudo e insereLegenda , cada uma realizando o downcasting do Graphics para Graphics2D.

InsereEixos: para inserirmos os eixos, atualizamos a largura da linha para uma definida anteriormente e alteramos sua cor. Para pintar os eixos do gráfico usamos o método drawLine (pertencente ao objeto g2d obtido quando realizamos o downcasting), que cria uma linha em qualquer posição da tela dadas coordenadas x e y iniciais e finais. Usamos dois drawLine para desenhar os eixos x e y do gráfico. Obs: perceba que o tamanho do eixo x é variável à quantidade de rodadas, caso seja necessário adicionar mais rodadas no futuro, o gráfico já se reajusta automaticamente.
```
    public void insereEixos(Graphics g){
        Graphics2D g2d = (Graphics2D) g;//downcasting
        g2d.setStroke(new BasicStroke(largura_linhas_eixos));
        g2d.setPaint(new Color(238, 238, 238));
        g2d.drawLine(25, 85, 25, 515);//desenha eixo Y do grafico
        g2d.drawLine(25, 515, 600 + (5 * qtd_rodadas), 515);//desenha eixo X do grafico eixo X é variável de acordo com a quantidade de rodadas
    }
```
InsereRotulos: este método insere os rótulos do gráfico de rodadas e posicao, para isso definimos um valor base de distância em pixels entre cada rótulo(Atenção, pois esse valor será usado como base para cálculo das distâncias entre indicadores) chamado de : "discrepancia_indicadores" junto com variáveis de margem para padronizar os cálculos de pixels, chamados margemX e margemY. Com isso, criamos dois laços "for" para cada conjunto de rótulos e a cada iteração usamos um drawString para escrever os rótulos com margem e distância entre si previamente determinada.
```
    public void insereRotulos(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        pos_x = 8;
        pos_y = baseY;
        for(int i=1; i<21; i++){//insere os rotulos de posicao(varia sobre o eixo Y)
            g2d.drawString(i + "", pos_x, pos_y);
            pos_y += distancia_rotulos;
        }
        pos_x = baseX + discrepancia_indicadores;
        pos_y = 528;
        for (int j=1; j < qtd_rodadas + 1; j++){//insere os rotulos de rodadas (varia sobre o eixo X)
            g2d.drawString(j + "", pos_x, pos_y);
            pos_x += distancia_rotulos;
        }
    }
```
No primeiro "for" apenas o eixo Y é variável já que estamos escrevendo sobre ele e no segundo "for" apenas o eixo X varia pelo mesmo motivo

InsereIndicadores: A função é dividida em duas etapas: Cálculo de indicadores de linha e de indicadores circulares(que indicam como o time se saiu em relação à partida anterior). Para cálculo das posições, usamos os valores de distancia entre indicadores e um contador para acrescentar a distância dos rótulos a cada iteração.As vitórias ou derrotas de um time são usadas para calcular a posicao final da linha, juntamente com variáveis chamadas buffer que vão armazenar a posição do time na próxima rodada. Exemplo: A distância de rótulos em pixels é n, se na primeira rodada o time cujo gráfico está sendo pintado, estava em 7º, então 7n + margemY será pintado no gráfico sobre o eixo Y (já que o eixo X sempre varia constantemente em n) como posição inicial, e buffer_y como posição final(em que bufferY é calculado também multiplicando a próxima posição do time na tabela por n), após isso o contador é somado a n para ser utilizado na próxima iteração como valor inicial de x.
```
    Graphics2D g2d = (Graphics2D) g;
    int contador = baseX;
        
    int buffer_x = baseX;
    int buffer_y = 0;

    for (int k=0; k < historico.length-1;k++){//insere as linhas do grafico
        buffer_x += distancia_rotulos;
        buffer_y  = margemY + (distancia_rotulos * historico[k + 1]);//armazena o valor da posicao na proxima rodada
        g2d.setPaint(new Color(0, 173, 181));
        g2d.drawLine(contador, margemY + (distancia_rotulos * historico[k]), buffer_x, buffer_y);
        contador += distancia_rotulos;
    }
```
Para pintar os indicadores circulares a lógica utilizada é parecida, com a pequena diferença de que o buffer é utilizado sobre o resultado anterior para comparar com o resultado atual. Vejamos:
```
    int contador2 = baseX;
    int buffer_posicao = 0;
    for (int x : historico){//insere os indicadores de vitoria(discrepancia adicionada ao calculo)
        if (buffer_posicao < x && buffer_posicao != 0){
            g2d.setPaint(Color.red);
            g2d.fillOval(contador2 + discrepancia_indicadores, margemY + discrepancia_indicadores + (distancia_rotulos * x), 10, 10);
        }
        else if (buffer_posicao > x && buffer_posicao != 0){
            g2d.setPaint(Color.green);
            g2d.fillOval(contador2 + discrepancia_indicadores, margemY + discrepancia_indicadores + (distancia_rotulos * x), 10, 10);
        }
        else{
            g2d.setPaint(Color.lightGray);
            g2d.fillOval(contador2 + discrepancia_indicadores, margemY + discrepancia_indicadores + (distancia_rotulos * x), 10, 10);
        }
        buffer_posicao = x;//atualizando o buffer
        contador2 += distancia_rotulos;
    }
```
Se o buffer de posicão(que armazena a posicao da rodada anterior) for menor que o atual, significa que o time perdeu colocações na tabela, e utilizando um cálculo parecido com o anterior pinta um círculo vermelho na posição do gráfico, indicando perda de posição. A lógica é similar para ganho de posição(cor verde) e manutenção de posição(cor cinza). O método utilizado para pintar o círculo é o fillOval que preenche um círculo dadas posições x e y, e altura e largura que se deseja obter. A variável discrepancia_indicadores é usada para ajustar a posição dos indicadores de linha e circulares.
insereLegendas: Este método utiliza um drawString para escrever na tela o nome do time e o nome "POSIÇÃO X RODADA" para facilitar compreensão do gráfico.
```
    public void insereLegendas(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(new Color(238, 238, 238));
        g2d.setFont(new Font("Serif", Font.BOLD, 20));
        g2d.drawString("RODADA x POSIÇÃO", 280, 570);
        g2d.drawString(Dados.traducao(sigla_time), 350, 30);
    }
```
O método tradução utilizado na última linha serve para retornar o nome completo do time, pois apenas temos a sigla pertencente a ele.
insereEscudo: Cria um ImageIcon e insere na tela
```
    public void insereEscudo(Graphics g){
        Graphics g2d = (Graphics2D) g;
        Image escudo = new ImageIcon("../brasileirao_novo/img/" + sigla_time + ".png").getImage();
        g2d.drawImage(escudo, 310, 10, null);
    }
```
O método sobrescrito paint une todos esses métodos e pinta no JPanel na ordem especificada, veja:
```
    @Override
    public void paint(Graphics g){
        insereEixos(g);
        insereRotulos(g);
        insereIndicadores(g);
        insereEscudo(g);
        insereLegendas(g);
    }
```
Pronto, agora que temos uma classe que pinta o gráfico na tela, podemos criar o JFrame a adicionar o JPanel criado. Isso é feito usando a classe CriaFrame:
```
public class CriaFrame{//cria o frame do grafico!
    private JFrame frame;
    
    public CriaFrame(CriaGrafico grafico){

        frame = new JFrame("Brasileirão Série A 2022");
        frame.add(grafico);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setBackground(new Color(34, 40, 49));//cor do frame
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//não encerra o programa apenas fecha a janela

    }
}
```
### Junção das Classes
Usando a classe Main, podemos criar uma instância de dados, computá-los, e criar a interface da tabela esperando que as classes realizem suas funções corretamente
```
public class Main {
    public static void main(String[] args) {
        try {
            Dados.n_rodadas = 33;
            Dados dados = new Dados();
            dados.computaDados("../brasileirao_novo/data/rodadas_data.3.txt");
            new InterfaceTabela();
        } catch (IOException e) {
            System.out.println("Ocorreu uma exceção " + e);
        }
    }
}
```
