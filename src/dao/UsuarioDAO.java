package dao;

import model.Aluno;
import model.Professor;
import model.Usuario;
import util.ConexaoDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public void salvar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nome, cpf, email, matricula, tipo, departamento) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try {
            Connection conn = ConexaoDB.getConexao();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getCpf());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getMatricula());
            stmt.setString(5, usuario.getTipoUsuario());

            // só Professor tem departamento
            if (usuario instanceof Professor) {
                stmt.setString(6, ((Professor) usuario).getDepartamento());
            } else {
                stmt.setNull(6, Types.VARCHAR);
            }

            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Usuario buscarPorId(int id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        try {
            Connection conn = ConexaoDB.getConexao();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return montarUsuario(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Usuario buscarPorMatricula(String matricula) {
        String sql = "SELECT * FROM usuarios WHERE matricula = ?";
        try {
            Connection conn = ConexaoDB.getConexao();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, matricula);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return montarUsuario(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Usuario> listarTodos() {
        String sql = "SELECT * FROM usuarios";
        List<Usuario> usuarios = new ArrayList<>();
        try {
            Connection conn = ConexaoDB.getConexao();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                usuarios.add(montarUsuario(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    public void atualizar(Usuario usuario) {
        String sql = "UPDATE usuarios SET nome = ?, cpf = ?, email = ?, " +
                "matricula = ?, tipo = ?, departamento = ? WHERE id = ?";
        try {
            Connection conn = ConexaoDB.getConexao();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getCpf());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getMatricula());
            stmt.setString(5, usuario.getTipoUsuario());
            if (usuario instanceof Professor) {
                stmt.setString(6, ((Professor) usuario).getDepartamento());
            } else {
                stmt.setNull(6, Types.VARCHAR);
            }
            stmt.setInt(7, usuario.getId());
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deletar(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
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

    // polimorfismo na prática: instancia Aluno ou Professor conforme o campo "tipo" no banco
    private Usuario montarUsuario(ResultSet rs) throws SQLException {
        String tipo = rs.getString("tipo");
        Usuario usuario;

        if ("Professor".equals(tipo)) {
            Professor prof = new Professor();
            prof.setDepartamento(rs.getString("departamento"));
            usuario = prof;
        } else {
            usuario = new Aluno();
        }

        usuario.setId(rs.getInt("id"));
        usuario.setNome(rs.getString("nome"));
        usuario.setCpf(rs.getString("cpf"));
        usuario.setEmail(rs.getString("email"));
        usuario.setMatricula(rs.getString("matricula"));

        return usuario;
    }
}