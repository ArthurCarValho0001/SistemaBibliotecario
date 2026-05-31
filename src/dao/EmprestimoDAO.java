package dao;

import model.Emprestimo;
import model.StatusEmprestimo;
import util.ConexaoDB;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoDAO {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private LivroDAO livroDAO = new LivroDAO();

    public void salvar(Emprestimo emprestimo) {
        String sql = "INSERT INTO emprestimos (usuario_id, livro_id, data_emprestimo, " +
                "data_devolucao_prevista, data_devolucao_real, status) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            Connection conn = ConexaoDB.getConexao();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, emprestimo.getUsuario().getId());
            stmt.setInt(2, emprestimo.getLivro().getId());
            stmt.setString(3, emprestimo.getDataEmprestimo().toString());
            stmt.setString(4, emprestimo.getDataDevolucaoPrevista().toString());
            stmt.setString(5, emprestimo.getDataDevolucaoReal() != null ?
                    emprestimo.getDataDevolucaoReal().toString() : null);
            stmt.setString(6, emprestimo.getStatus().name());
            stmt.executeUpdate();

            // recupera o id gerado pelo banco e atribui ao objeto
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                emprestimo.setId(rs.getInt(1));
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Emprestimo buscarPorId(int id) {
        String sql = "SELECT * FROM emprestimos WHERE id = ?";
        try {
            Connection conn = ConexaoDB.getConexao();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return montarEmprestimo(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Emprestimo> listarTodos() {
        String sql = "SELECT * FROM emprestimos";
        List<Emprestimo> lista = new ArrayList<>();
        try {
            Connection conn = ConexaoDB.getConexao();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(montarEmprestimo(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Emprestimo> listarPorUsuario(int usuarioId) {
        String sql = "SELECT * FROM emprestimos WHERE usuario_id = ?";
        List<Emprestimo> lista = new ArrayList<>();
        try {
            Connection conn = ConexaoDB.getConexao();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(montarEmprestimo(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Emprestimo> listarAtrasados() {
        String sql = "SELECT * FROM emprestimos WHERE status = 'ATRASADO'";
        List<Emprestimo> lista = new ArrayList<>();
        try {
            Connection conn = ConexaoDB.getConexao();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(montarEmprestimo(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public Emprestimo buscarEmprestimoAtivo(int livroId) {
        String sql = "SELECT * FROM emprestimos WHERE livro_id = ? AND status = 'ATIVO'";
        try {
            Connection conn = ConexaoDB.getConexao();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, livroId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return montarEmprestimo(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void atualizar(Emprestimo emprestimo) {
        String sql = "UPDATE emprestimos SET status = ?, data_devolucao_real = ? WHERE id = ?";
        try {
            Connection conn = ConexaoDB.getConexao();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, emprestimo.getStatus().name());
            stmt.setString(2, emprestimo.getDataDevolucaoReal() != null ?
                    emprestimo.getDataDevolucaoReal().toString() : null);
            stmt.setInt(3, emprestimo.getId());
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Emprestimo montarEmprestimo(ResultSet rs) throws SQLException {
        Emprestimo emp = new Emprestimo();
        emp.setId(rs.getInt("id"));
        emp.setUsuario(usuarioDAO.buscarPorId(rs.getInt("usuario_id")));
        emp.setLivro(livroDAO.buscarPorId(rs.getInt("livro_id")));
        emp.setDataEmprestimo(LocalDate.parse(rs.getString("data_emprestimo")));
        emp.setDataDevolucaoPrevista(LocalDate.parse(rs.getString("data_devolucao_prevista")));
        String dataReal = rs.getString("data_devolucao_real");
        if (dataReal != null) {
            emp.setDataDevolucaoReal(LocalDate.parse(dataReal));
        }
        emp.setStatus(StatusEmprestimo.valueOf(rs.getString("status")));
        return emp;
    }
}