/*
 * Fornece classes para componentes do UI do centro de exposições.
 */
package centroexposicoes.ui.components;

import centroexposicoes.model.CentroExposicoes;
import centroexposicoes.ui.LoginUI;
import centroexposicoes.utils.FicheiroCentroExposicoes;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;

/**
 * Representa um template para o UI da aplicação.
 *
 * @author Daniel Gonçalves 1151452
 * @author Ivo Ferro 1151159
 */
public class GlobalJFrame extends JFrame {

    /**
     * Título da janela da aplicação por omissão.
     */
    private static final String APPLICATION_WINDOW_TITLE = "Centro de Exposições ISEP v0.1";

    /**
     * Centro de Exposições.
     */
    private final CentroExposicoes centroExposicoes;

    /**
     * Constrói uma instância de global JFrame recebendo o centro de exposições.
     *
     * @param centroExposicoes centro de exposições
     */
    public GlobalJFrame(CentroExposicoes centroExposicoes) {
        super(APPLICATION_WINDOW_TITLE);

        this.centroExposicoes = centroExposicoes;

        setJMenuBar(criarGlobalJMenuBar());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                sair();
            }
        });

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    /**
     * Cria o menu bar.
     *
     * @return menu bar
     */
    private JMenuBar criarGlobalJMenuBar() {
        GlobalJMenuBar menuBar = new GlobalJMenuBar();
        menuBar.getMenuItemSair().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sair();
            }
        });
        menuBar.getMenuItemTerminarSessao().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                terminarSessao();
            }
        });
        return menuBar;
    }

    /**
     * Fecha a aplicação e guarda os dados em ficheiro binário.
     */
    private void sair() {
        dispose();
        FicheiroCentroExposicoes.guardar(FicheiroCentroExposicoes.NOME, centroExposicoes);
    }

    /**
     * Volta ao login.
     */
    private void terminarSessao() {
        dispose();
        new LoginUI(centroExposicoes);
    }
}
