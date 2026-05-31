package service;

import dao.UsuarioDAO;
import model.Usuario;

import java.util.List;

public class UsuarioService {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public String cadastrarUsuario(Usuario usuario) {

        if (usuario.getNome() == null || usuario.getNome().isBlank()) {
            return "Erro: nome não pode ser vazio.";
        }

        if (usuario.getCpf() == null || !usuario.getCpf().matches("\\d{11}")) {
            return "Erro: CPF deve conter exatamente 11 dígitos numéricos.";
        }

        if (usuario.getEmail() == null || !usuario.getEmail().contains("@")) {
            return "Erro: e-mail inválido.";
        }

        if (usuario.getMatricula() == null || usuario.getMatricula().isBlank()) {
            return "Erro: matrícula não pode ser vazia.";
        }

        if (usuarioDAO.buscarPorMatricula(usuario.getMatricula()) != null) {
            return "Erro: já existe um usuário com essa matrícula.";
        }

        usuarioDAO.salvar(usuario);
        return "Usuário cadastrado com sucesso!";
    }

    public Usuario buscarPorMatricula(String matricula) {
        return usuarioDAO.buscarPorMatricula(matricula);
    }

    public Usuario buscarPorId(int id) {
        return usuarioDAO.buscarPorId(id);
    }

    public List<Usuario> listarTodos() {
        return usuarioDAO.listarTodos();
    }

    public void atualizar(Usuario usuario) {
        usuarioDAO.atualizar(usuario);
    }

    public void deletar(int id) {
        usuarioDAO.deletar(id);
    }
}