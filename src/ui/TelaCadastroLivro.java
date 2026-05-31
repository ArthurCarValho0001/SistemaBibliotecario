package ui;

import model.Livro;
import service.LivroService;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaCadastroLivro extends JPanel {

    private LivroService livroService = new LivroService();

    private JTextField campoTitulo = new JTextField(20);
    private JTextField campoAutor = new JTextField(20);
    private JTextField campoAno = new JTextField(6);
    private JTextField campoEditora = new JTextField(15);

    private DefaultTableModel modeloTabela;
    private JTable tabela;

    public TelaCadastroLivro() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(0xF5F5F5));

        add(criarFormulario(), BorderLayout.NORTH);
        add(criarTabela(), BorderLayout.CENTER);

        carregarTabela();
    }

    private JPanel criarFormulario() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(new Color(0xF5F5F5));
        painel.setBorder(new TitledBorder("Cadastrar Livro"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(new JLabel("Título:"), gbc);
        gbc.gridx = 1;
        painel.add(campoTitulo, gbc);

        gbc.gridx = 2; gbc.gridy = 0;
        painel.add(new JLabel("Autor:"), gbc);
        gbc.gridx = 3;
        painel.add(campoAutor, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        painel.add(new JLabel("Ano:"), gbc);
        gbc.gridx = 1;
        painel.add(campoAno, gbc);

        gbc.gridx = 2; gbc.gridy = 1;
        painel.add(new JLabel("Editora:"), gbc);
        gbc.gridx = 3;
        painel.add(campoEditora, gbc);

        JButton botaoCadastrar = criarBotao("Cadastrar");
        botaoCadastrar.addActionListener(e -> cadastrar());

        gbc.gridx = 1; gbc.gridy = 2;
        gbc.gridwidth = 2;
        painel.add(botaoCadastrar, gbc);

        return painel;
    }

    private JScrollPane criarTabela() {
        modeloTabela = new DefaultTableModel(
                new String[]{"ID", "Título", "Autor", "Ano", "Editora", "Status"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        tabela = new JTable(modeloTabela);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabela.setRowHeight(22);
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        return new JScrollPane(tabela);
    }

    private void cadastrar() {
        try {
            Livro livro = new Livro();
            livro.setTitulo(campoTitulo.getText().trim());
            livro.setAutor(campoAutor.getText().trim());
            livro.setAnoPublicacao(Integer.parseInt(campoAno.getText().trim()));
            livro.setEditora(campoEditora.getText().trim());

            String resultado = livroService.cadastrarLivro(livro);
            JOptionPane.showMessageDialog(this, resultado);

            if (resultado.startsWith("Livro")) {
                limparCampos();
                carregarTabela();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Erro: ano deve ser um número.");
        }
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);
        List<Livro> livros = livroService.listarTodos();
        for (Livro l : livros) {
            modeloTabela.addRow(new Object[]{
                    l.getId(),
                    l.getTitulo(),
                    l.getAutor(),
                    l.getAnoPublicacao(),
                    l.getEditora(),
                    l.isDisponivel() ? "Disponível" : "Emprestado"
            });
        }

        tabela.getColumnModel().getColumn(5).setCellRenderer((table, value, isSelected, hasFocus, row, col) -> {
            JLabel label = new JLabel(value.toString());
            label.setOpaque(true);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            if ("Disponível".equals(value)) {
                label.setForeground(new Color(0x2E7D32));
            } else {
                label.setForeground(new Color(0xC62828));
            }
            if (isSelected) label.setBackground(table.getSelectionBackground());
            else label.setBackground(table.getBackground());
            return label;
        });
    }

    private void limparCampos() {
        campoTitulo.setText("");
        campoAutor.setText("");
        campoAno.setText("");
        campoEditora.setText("");
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