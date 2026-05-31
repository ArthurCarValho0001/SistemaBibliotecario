package ui;

import model.Aluno;
import model.Professor;
import model.Usuario;
import service.UsuarioService;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaCadastroUsuario extends JPanel {

    private UsuarioService usuarioService = new UsuarioService();

    private JTextField campoNome = new JTextField(20);
    private JTextField campoMatricula = new JTextField(10);
    private JTextField campoCpf = new JTextField(12);
    private JTextField campoEmail = new JTextField(20);
    private JTextField campoDepartamento = new JTextField(15);
    private JComboBox<String> comboTipo = new JComboBox<>(new String[]{"Aluno", "Professor"});

    private DefaultTableModel modeloTabela;

    public TelaCadastroUsuario() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(0xF5F5F5));

        add(criarFormulario(), BorderLayout.NORTH);
        add(criarTabela(), BorderLayout.CENTER);

        comboTipo.addActionListener(e -> {
            campoDepartamento.setVisible("Professor".equals(comboTipo.getSelectedItem()));
            revalidate();
        });
        campoDepartamento.setVisible(false);

        carregarTabela();
    }

    private JPanel criarFormulario() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(new Color(0xF5F5F5));
        painel.setBorder(new TitledBorder("Cadastrar Usuário"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        painel.add(campoNome, gbc);

        gbc.gridx = 2;
        painel.add(new JLabel("Matrícula:"), gbc);
        gbc.gridx = 3;
        painel.add(campoMatricula, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        painel.add(new JLabel("CPF:"), gbc);
        gbc.gridx = 1;
        painel.add(campoCpf, gbc);

        gbc.gridx = 2;
        painel.add(new JLabel("E-mail:"), gbc);
        gbc.gridx = 3;
        painel.add(campoEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        painel.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1;
        painel.add(comboTipo, gbc);

        gbc.gridx = 2;
        painel.add(new JLabel("Departamento:"), gbc);
        gbc.gridx = 3;
        painel.add(campoDepartamento, gbc);

        JButton botaoCadastrar = criarBotao("Cadastrar");
        botaoCadastrar.addActionListener(e -> cadastrar());

        gbc.gridx = 1; gbc.gridy = 3;
        gbc.gridwidth = 2;
        painel.add(botaoCadastrar, gbc);

        return painel;
    }

    private JScrollPane criarTabela() {
        modeloTabela = new DefaultTableModel(
                new String[]{"ID", "Nome", "Matrícula", "Tipo", "E-mail"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        JTable tabela = new JTable(modeloTabela);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabela.setRowHeight(22);
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        return new JScrollPane(tabela);
    }

    private void cadastrar() {
        Usuario usuario;

        if ("Professor".equals(comboTipo.getSelectedItem())) {
            Professor prof = new Professor();
            prof.setDepartamento(campoDepartamento.getText().trim());
            usuario = prof;
        } else {
            usuario = new Aluno();
        }

        usuario.setNome(campoNome.getText().trim());
        usuario.setMatricula(campoMatricula.getText().trim());
        usuario.setCpf(campoCpf.getText().trim());
        usuario.setEmail(campoEmail.getText().trim());

        String resultado = usuarioService.cadastrarUsuario(usuario);
        JOptionPane.showMessageDialog(this, resultado);

        if (resultado.startsWith("Usuário")) {
            limparCampos();
            carregarTabela();
        }
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);
        List<Usuario> usuarios = usuarioService.listarTodos();
        for (Usuario u : usuarios) {
            modeloTabela.addRow(new Object[]{
                    u.getId(),
                    u.getNome(),
                    u.getMatricula(),
                    u.getTipoUsuario(),
                    u.getEmail()
            });
        }
    }

    private void limparCampos() {
        campoNome.setText("");
        campoMatricula.setText("");
        campoCpf.setText("");
        campoEmail.setText("");
        campoDepartamento.setText("");
        comboTipo.setSelectedIndex(0);
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