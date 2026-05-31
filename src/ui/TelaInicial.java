package ui;

import service.EmprestimoService;
import util.ConexaoDB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TelaInicial extends JFrame {

    public TelaInicial() {
        setTitle("Biblioteca UDF");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane abas = new JTabbedPane();
        abas.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        abas.addTab("Livros", new TelaCadastroLivro());
        abas.addTab("Usuários", new TelaCadastroUsuario());
        abas.addTab("Empréstimos", new TelaEmprestimo());
        abas.addTab("Fila de Espera", new TelaFilaEspera());

        add(abas);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    ConexaoDB.getConexao().close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                dispose();
                System.exit(0);
            }
        });
    }
}