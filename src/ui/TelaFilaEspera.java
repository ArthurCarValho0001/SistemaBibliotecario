package ui;

import model.FilaEspera;
import service.EmprestimoService;
import dao.FilaEsperaDAO;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaFilaEspera extends JPanel {

    private FilaEsperaDAO filaEsperaDAO = new FilaEsperaDAO();
    private EmprestimoService emprestimoService = new EmprestimoService();

    private JTextField campoLivroId = new JTextField(6);
    private JTextField campoEmprestimoId = new JTextField(6);

    private DefaultTableModel modeloTabela;

    public TelaFilaEspera() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(0xF5F5F5));

        add(criarPainelAcoes(), BorderLayout.NORTH);
        add(criarTabela(), BorderLayout.CENTER);
    }

    private JPanel criarPainelAcoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painel.setBackground(new Color(0xF5F5F5));
        painel.setBorder(new TitledBorder("Consultar Fila"));

        painel.add(new JLabel("ID do Livro:"));
        painel.add(campoLivroId);

        JButton botaoBuscar = criarBotao("Ver Fila");
        botaoBuscar.addActionListener(e -> buscarFila());
        painel.add(botaoBuscar);

        painel.add(Box.createHorizontalStrut(30));
        painel.add(new JLabel("ID do Empréstimo:"));
        painel.add(campoEmprestimoId);

        JButton botaoCancelar = criarBotao("Cancelar Reserva");
        botaoCancelar.setBackground(new Color(0xC62828));
        botaoCancelar.addActionListener(e -> cancelarReserva());
        painel.add(botaoCancelar);

        return painel;
    }

    private JScrollPane criarTabela() {
        modeloTabela = new DefaultTableModel(
                new String[]{"Posição", "Usuário", "Livro", "Data de Entrada"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        JTable tabela = new JTable(modeloTabela);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabela.setRowHeight(22);
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        return new JScrollPane(tabela);
    }

    private void buscarFila() {
        String idStr = campoLivroId.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o ID do livro.");
            return;
        }

        try {
            int livroId = Integer.parseInt(idStr);
            modeloTabela.setRowCount(0);
            List<FilaEspera> fila = filaEsperaDAO.listarFilaPorLivro(livroId);

            if (fila.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhuma pessoa na fila para este livro.");
                return;
            }

            for (FilaEspera f : fila) {
                modeloTabela.addRow(new Object[]{
                        f.getPosicao(),
                        f.getUsuario().getNome(),
                        f.getLivro().getTitulo(),
                        f.getDataEntrada()
                });
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Erro: ID deve ser um número.");
        }
    }

    private void cancelarReserva() {
        String idStr = campoEmprestimoId.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o ID do empréstimo.");
            return;
        }

        try {
            int emprestimoId = Integer.parseInt(idStr);
            String resultado = emprestimoService.cancelarReserva(emprestimoId);
            JOptionPane.showMessageDialog(this, resultado);
            campoEmprestimoId.setText("");
            modeloTabela.setRowCount(0);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Erro: ID deve ser um número.");
        }
    }

    private JButton criarBotao(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(new Color(0x1565C0));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        return btn;
    }
}