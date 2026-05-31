package dao;

import model.Livro;
import util.ConexaoDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO {

    public void salvar(Livro livro) {
        String sql = "INSERT INTO livros (titulo, autor, ano_publicacao, editora, disponivel) " +
                "VALUES (?, ?, ?, ?, ?)";
        try {
            Connection conn = ConexaoDB.getConexao();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getAutor());
            stmt.setInt(3, livro.getAnoPublicacao());
            stmt.setString(4, livro.getEditora());
            stmt.setBoolean(5, livro.isDisponivel());
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Livro buscarPorId(int id) {
        String sql = "SELECT * FROM livros WHERE id = ?";
        try {
            Connection conn = ConexaoDB.getConexao();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return montarLivro(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Livro> listarTodos() {
        String sql = "SELECT * FROM livros";
        List<Livro> livros = new ArrayList<>();
        try {
            Connection conn = ConexaoDB.getConexao();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                livros.add(montarLivro(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return livros;
    }

    public List<Livro> listarDisponiveis() {
        String sql = "SELECT * FROM livros WHERE disponivel = 1";
        List<Livro> livros = new ArrayList<>();
        try {
            Connection conn = ConexaoDB.getConexao();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                livros.add(montarLivro(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return livros;
    }

    public void atualizar(Livro livro) {
        String sql = "UPDATE livros SET titulo = ?, autor = ?, ano_publicacao = ?, " +
                "editora = ?, disponivel = ? WHERE id = ?";
        try {
            Connection conn = ConexaoDB.getConexao();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getAutor());
            stmt.setInt(3, livro.getAnoPublicacao());
            stmt.setString(4, livro.getEditora());
            stmt.setBoolean(5, livro.isDisponivel());
            stmt.setInt(6, livro.getId());
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deletar(int id) {
        String sql = "DELETE FROM livros WHERE id = ?";
        try {
            Connection conn = ConexaoDB.getConexao();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // método privado auxiliar — evita repetir o mesmo código de montagem em cada método
    private Livro montarLivro(ResultSet rs) throws SQLException {
        Livro livro = new Livro();
        livro.setId(rs.getInt("id"));
        livro.setTitulo(rs.getString("titulo"));
        livro.setAutor(rs.getString("autor"));
        livro.setAnoPublicacao(rs.getInt("ano_publicacao"));
        livro.setEditora(rs.getString("editora"));
        livro.setDisponivel(rs.getBoolean("disponivel"));
        return livro;
    }
}