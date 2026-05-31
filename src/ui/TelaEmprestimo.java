package ui;

import model.Emprestimo;
import model.StatusEmprestimo;
import service.EmprestimoService;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaEmprestimo extends JPanel {

    private EmprestimoService emprestimoService = new EmprestimoService();

    private JTextField campoMatricula = new JTextField(12);
    private JTextField campoLivroId = new JTextField(6);
    private JTextField campoEmprestimoId = new JTextField(6);

    private DefaultTableModel modeloTabela;
    private JTable tabela;

    public TelaEmprestimo() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(0xF5F5F5));

        add(criarPainelAcoes(), BorderLayout.NORTH);
        add(criarTabela(), BorderLayout.CENTER);

        carregarTabela();
    }

    private JPanel criarPainelAcoes() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(new Color(0xF5F5F5));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // painel de empréstimo
        JPanel painelEmprestar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelEmprestar.setBorder(new TitledBorder("Realizar Empréstimo"));
        painelEmprestar.setBackground(new Color(0xF5F5F5));
        painelEmprestar.add(new JLabel("Matrícula:"));
        painelEmprestar.add(campoMatricula);
        painelEmprestar.add(new JLabel("ID do Livro:"));
        painelEmprestar.add(campoLivroId);

        JButton botaoEmprestar = criarBotao("Emprestar");
        botaoEmprestar.addActionListener(e -> emprestar());
        painelEmprestar.add(botaoEmprestar);

        // painel de devolução
        JPanel painelDevolver = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelDevolver.setBorder(new TitledBorder("Realizar Devolução"));
        painelDevolver.setBackground(new Color(0xF5F5F5));
        painelDevolver.add(new JLabel("ID do Empréstimo:"));
        painelDevolver.add(campoEmprestimoId);

        JButton botaoDevolver = criarBotao("Devolver");
        botaoDevolver.addActionListener(e -> devolver());
        painelDevolver.add(botaoDevolver);

        gbc.gridx = 0; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        painel.add(painelEmprestar, gbc);
        gbc.gridy = 1;
        painel.add(painelDevolver, gbc);

        return painel;
    }

    private JScrollPane criarTabela() {
        modeloTabela = new DefaultTableModel(
                new String[]{"ID", "Usuário", "Livro", "Data Empréstimo", "Prev. Devolução", "Status"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        tabela = new JTable(modeloTabela);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabela.setRowHeight(22);
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        tabela.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                if ("ATRASADO".equals(value)) {
                    label.setForeground(new Color(0xC62828));
                } else if ("ATIVO".equals(value)) {
                    label.setForeground(new Color(0x2E7D32));
                } else if ("RESERVADO".equals(value)) {
                    label.setForeground(new Color(0xE65100));
                } else {
                    label.setForeground(Color.GRAY);
                }
                return label;
            }
        });

        return new JScrollPane(tabela);
    }

    private void emprestar() {
        String matricula = campoMatricula.getText().trim();
        String livroIdStr = campoLivroId.getText().trim();

        if (matricula.isEmpty() || livroIdStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha a matrícula e o ID do livro.");
            return;
        }

        try {
            int livroId = Integer.parseInt(livroIdStr);
            String resultado = emprestimoService.realizarEmprestimo(matricula, livroId);

            if ("INDISPONIVEL".equals(resultado)) {
                int opcao = JOptionPane.showConfirmDialog(this,
                        "Este livro está emprestado no momento.\nDeseja entrar na fila de espera?",
                        "Livro Indisponível",
                        JOptionPane.YES_NO_OPTION);

                if (opcao == JOptionPane.YES_OPTION) {
                    String resultadoFila = emprestimoService.entrarNaFila(matricula, livroId);
                    JOptionPane.showMessageDialog(this, resultadoFila);
                }
            } else {
                JOptionPane.showMessageDialog(this, resultado);
            }

            campoMatricula.setText("");
            campoLivroId.setText("");
            carregarTabela();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Erro: ID do livro deve ser um número.");
        }
    }

    private void devolver() {
        String idStr = campoEmprestimoId.getText().trim();

        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o ID do empréstimo.");
            return;
        }

        try {
            int emprestimoId = Integer.parseInt(idStr);
            String resultado = emprestimoService.realizarDevolucao(emprestimoId);
            JOptionPane.showMessageDialog(this, resultado);
            campoEmprestimoId.setText("");
            carregarTabela();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Erro: ID deve ser um número.");
        }
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);
        List<Emprestimo> lista = emprestimoService.listarTodos();
        for (Emprestimo emp : lista) {
            modeloTabela.addRow(new Object[]{
                    emp.getId(),
                    emp.getUsuario() != null ? emp.getUsuario().getNome() : "-",
                    emp.getLivro() != null ? emp.getLivro().getTitulo() : "-",
                    emp.getDataEmprestimo(),
                    emp.getDataDevolucaoPrevista(),
                    emp.getStatus().name()
            });
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