/*
 * Fornece as classe que controlam a interface gráfica.
 */
package centroexposicoes.ui;

import centroexposicoes.controller.RegistarCandidaturaController;
import centroexposicoes.model.CentroExposicoes;
import centroexposicoes.model.Demonstracao;
import centroexposicoes.model.Exposicao;
import centroexposicoes.ui.components.DialogNovoProduto;
import centroexposicoes.ui.components.DialogSelecionarExposicao;
import centroexposicoes.ui.components.ExposicaoSelecionavel;
import centroexposicoes.ui.components.GlobalJFrame;
import centroexposicoes.ui.components.ModeloListProdutos;
import centroexposicoes.ui.components.ModeloTabelaListaDemonstracao;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

/**
 * Interface gráfica para o registo de candidatura.
 *
 * @author Daniel Gonçalves 1151452
 * @author Ivo Ferro 1151159
 */
public class RegistarCandidaturaUI extends GlobalJFrame implements ExposicaoSelecionavel {

    /**
     * Exposição selecionada pelo UI.
     */
    private Exposicao exposicaoSelecionada;
    private List<Exposicao> listaExposicoes;
    private List<Demonstracao> listaDemonstracoes;
    private ModeloListProdutos modeloListProdutos;

    private final RegistarCandidaturaController controller;

    private JTable listaDemonstracoesJTable;
    
    private JTextField txtNomeEmpresa;
    private JTextArea txtMorada;
    private JTextField txtTelemovel;
    private JTextField txtAreaExpositor;
    private JTextField txtNumConvites;

    private static final Dimension LBL_TAMANHO = new Dimension(94, 16);
    final int MARGEM_SUPERIOR = 0, MARGEM_INFERIOR = 0;
    final int MARGEM_ESQUERDA = 10, MARGEM_DIREITA = 0;
    private static final int CAMPO_TXT_LARGURA = 20;
    private static final int CAMPO_NUM_LARGURA = 6;

    public RegistarCandidaturaUI(CentroExposicoes centroExposicoes) {

        this.controller = new RegistarCandidaturaController(centroExposicoes);
        this.listaExposicoes = controller.getExposições();
        
        // REVER NO CONTROLLER - SE NÃO HOUVEREM DEMONSRAÇÕES?
        //this.listaDemonstracoes = controller.getListaDemonstracoes();

        //APENAS PARA TESTAR INICIO
        Exposicao e1 = new Exposicao();
        this.listaExposicoes.add(e1);
        this.listaExposicoes.add(e1);
        this.listaExposicoes.add(e1);
        this.listaExposicoes.add(e1);
        Demonstracao d1 = new Demonstracao();
        this.listaDemonstracoes = new ArrayList<>();
        this.listaDemonstracoes.add(d1);
        this.listaDemonstracoes.add(d1);
        this.listaDemonstracoes.add(d1);
        this.listaDemonstracoes.add(d1);
        new DialogSelecionarExposicao<>(this, this.listaExposicoes);
        //APENAS PARA TESTAR FIM

        //ESTÁ CORRECTO. APAGAR A PARTE DE "APENAS PARA TESTAR" E DESCOMENTAR ESTA.
        //new DialogSelecionarExposicao<>(this, this.listaExposicoes);
        criarComponentes();

        setVisible(true);
    }

    private void criarComponentes() {

        add(criarPainelDados(), BorderLayout.WEST);
        add(criarPainelProdutos(), BorderLayout.CENTER);
        add(criarPainelDemonstracoes(), BorderLayout.EAST);
    }

    private JPanel criarPainelDados() {

        JPanel painelDados = new JPanel(new GridLayout(5, 1));

        painelDados.add(criarPainelCampo("Nome Empresa:", this.txtNomeEmpresa, CAMPO_TXT_LARGURA));
        painelDados.add(criarPainelMorada());
        painelDados.add(criarPainelCampo("Telemovel:", this.txtTelemovel, CAMPO_TXT_LARGURA));
        painelDados.add(criarPainelCampo("Area do Expositor:", this.txtAreaExpositor, CAMPO_NUM_LARGURA));
        painelDados.add(criarPainelCampo("Número de Convites:", this.txtNumConvites, CAMPO_NUM_LARGURA));

        return painelDados;
    }

    private JPanel criarPainelProdutos() {

        JPanel pProdutos = new JPanel(new BorderLayout());

        JPanel pTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pTitulo.add(new JLabel("Lista de Produtos"));

        pProdutos.add(pTitulo, BorderLayout.NORTH);
        pProdutos.add(criarJListProdutos(), BorderLayout.CENTER);

        JPanel pBotao = new JPanel();
        pBotao.add(criarBotaoAddProduto());
        
        pProdutos.add(pBotao, BorderLayout.SOUTH);

        return pProdutos;
    }

    private JPanel criarPainelDemonstracoes() {
        JPanel pDemonstracoes = new JPanel(new BorderLayout());
        
        JPanel pTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pTitulo.add(new JLabel("Lista de Demonstrações"));
        
        pDemonstracoes.add(pTitulo, BorderLayout.NORTH);
        pDemonstracoes.add(criarScrollPaneDemonstrações(), BorderLayout.CENTER);
        
        return pDemonstracoes;
    }    

    private JScrollPane criarScrollPaneDemonstrações() {
        listaDemonstracoesJTable = new JTable(new ModeloTabelaListaDemonstracao(listaDemonstracoes));
        listaDemonstracoesJTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane scrollPane = new JScrollPane(listaDemonstracoesJTable);

        final int MARGEM_SUPERIOR = 20, MARGEM_INFERIOR = 20;
        final int MARGEM_ESQUERDA = 20, MARGEM_DIREITA = 20;
        scrollPane.setBorder(BorderFactory.createEmptyBorder( MARGEM_SUPERIOR, 
                                                              MARGEM_ESQUERDA,
                                                              MARGEM_INFERIOR, 
                                                              MARGEM_DIREITA));

        return scrollPane;
    }

    private JPanel criarPainelCampo(String lblTexto, JTextField txtField, int largura) {

        JLabel lbl = new JLabel(lblTexto, JLabel.RIGHT);
        lbl.setPreferredSize(LBL_TAMANHO);

        txtField = new JTextField(largura);

        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));

        p.setBorder(new EmptyBorder(MARGEM_SUPERIOR, MARGEM_ESQUERDA,
                MARGEM_INFERIOR, MARGEM_DIREITA));
        p.add(lbl);
        p.add(txtField);

        return p;
    }

    private JPanel criarPainelMorada() {

        this.txtMorada = new JTextArea(4, CAMPO_TXT_LARGURA);
        JLabel lblMorada = new JLabel("Morada:", JLabel.RIGHT);
        JPanel pMorada = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pMorada.setBorder(new EmptyBorder(MARGEM_SUPERIOR, MARGEM_ESQUERDA,
                MARGEM_INFERIOR, MARGEM_DIREITA));
        pMorada.add(lblMorada);
        pMorada.add(this.txtMorada);

        return pMorada;
    }

    private JScrollPane criarJListProdutos() {

        this.modeloListProdutos = new ModeloListProdutos(controller);

        JList jListProdutos = new JList(modeloListProdutos);

        JScrollPane scrollPane = new JScrollPane(jListProdutos);

        final int S_MARGEM_SUPERIOR = 20, S_MARGEM_INFERIOR = 20;
        final int S_MARGEM_ESQUERDA = 20, S_MARGEM_DIREITA = 20;
        scrollPane.setBorder(BorderFactory.createEmptyBorder(S_MARGEM_SUPERIOR,
                S_MARGEM_ESQUERDA,
                S_MARGEM_INFERIOR,
                S_MARGEM_DIREITA));

        return scrollPane;
    }

    private JButton criarBotaoAddProduto() {

        JButton btn = new JButton("Adiciona Produto");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                new DialogNovoProduto(RegistarCandidaturaUI.this);
            }
        });

        return btn;
    }

    public boolean novoProduto(String designacao) {

//        return controller.addProduto(designacao);
        return this.modeloListProdutos.addRow(designacao);
    }

    @Override
    public void setExposicao(Exposicao exposicao) {
        exposicaoSelecionada = exposicao;

        this.controller.novaCandidatura(exposicao);
    }

    /**
     * Método para testar o UI.
     *
     * @param args argumentos da linha de comandos
     */
    public static void main(String[] args) {

        CentroExposicoes ce = new CentroExposicoes();

        List<Exposicao> listaExposicoes = new ArrayList<>();
        Exposicao e1 = new Exposicao();
        Exposicao e2 = new Exposicao();
        listaExposicoes.add(e1);
        listaExposicoes.add(e2);

        new RegistarCandidaturaUI(ce);
    }
}
