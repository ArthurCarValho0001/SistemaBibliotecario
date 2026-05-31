package dao;

import model.FilaEspera;
import util.ConexaoDB;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FilaEsperaDAO {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private LivroDAO livroDAO = new LivroDAO();

    public void adicionarNaFila(FilaEspera fila) {
        String sql = "INSERT INTO fila_espera (livro_id, usuario_id, data_entrada, posicao) " +
                "VALUES (?, ?, ?, ?)";
        try {
            Connection conn = ConexaoDB.getConexao();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, fila.getLivro().getId());
            stmt.setInt(2, fila.getUsuario().getId());
            stmt.setString(3, fila.getDataEntrada().toString());
            stmt.setInt(4, fila.getPosicao());
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public FilaEspera proximoDaFila(int livroId) {
        String sql = "SELECT * FROM fila_espera WHERE livro_id = ? ORDER BY posicao ASC LIMIT 1";
        try {
            Connection conn = ConexaoDB.getConexao();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, livroId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return montarFilaEspera(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int proximaPosicao(int livroId) {
        String sql = "SELECT MAX(posicao) FROM fila_espera WHERE livro_id = ?";
        try {
            Connection conn = ConexaoDB.getConexao();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, livroId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) + 1; // próxima posição = MAX + 1
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1; // fila vazia, começa na posição 1
    }

    public boolean usuarioJaNaFila(int livroId, int usuarioId) {
        String sql = "SELECT COUNT(*) FROM fila_espera WHERE livro_id = ? AND usuario_id = ?";
        try {
            Connection conn = ConexaoDB.getConexao();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, livroId);
            stmt.setInt(2, usuarioId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void removerDaFila(int id) {
        String sql = "DELETE FROM fila_espera WHERE id = ?";
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

    public List<FilaEspera> listarFilaPorLivro(int livroId) {
        String sql = "SELECT * FROM fila_espera WHERE livro_id = ? ORDER BY posicao ASC";
        List<FilaEspera> lista = new ArrayList<>();
        try {
            Connection conn = ConexaoDB.getConexao();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, livroId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(montarFilaEspera(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    private FilaEspera montarFilaEspera(ResultSet rs) throws SQLException {
        FilaEspera fila = new FilaEspera();
        fila.setId(rs.getInt("id"));
        fila.setLivro(livroDAO.buscarPorId(rs.getInt("livro_id")));
        fila.setUsuario(usuarioDAO.buscarPorId(rs.getInt("usuario_id")));
        fila.setDataEntrada(LocalDate.parse(rs.getString("data_entrada")));
        fila.setPosicao(rs.getInt("posicao"));
        return fila;
    }
}