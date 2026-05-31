package service;

import dao.LivroDAO;
import model.Livro;

import java.util.List;

public class LivroService {

    private LivroDAO livroDAO = new LivroDAO();

    public String cadastrarLivro(Livro livro) {

        if (livro.getTitulo() == null || livro.getTitulo().isBlank()) {
            return "Erro: título não pode ser vazio.";
        }

        if (livro.getAutor() == null || livro.getAutor().isBlank()) {
            return "Erro: autor não pode ser vazio.";
        }

        livro.setDisponivel(true);
        livroDAO.salvar(livro);
        return "Livro cadastrado com sucesso!";
    }

    public Livro buscarPorId(int id) {
        return livroDAO.buscarPorId(id);
    }

    public List<Livro> listarTodos() {
        return livroDAO.listarTodos();
    }

    public List<Livro> listarDisponiveis() {
        return livroDAO.listarDisponiveis();
    }

    public void atualizar(Livro livro) {
        livroDAO.atualizar(livro);
    }

    public void deletar(int id) {
        livroDAO.deletar(id);
    }
}