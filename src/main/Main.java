package main;

import dao.LivroDAO;
import model.Livro;
import util.InicializadorDB;

public class Main {

    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));
        InicializadorDB.inicializar();
        System.out.println(System.getProperty("user.dir"));

        System.out.println("Sistema iniciado.");

        LivroDAO livroDAO = new LivroDAO();

        Livro livro = new Livro();

        livro.setTitulo("Clean Code");
        livro.setAutor("Robert Martin");
        livro.setAnoPublicacao(2008);
        livro.setEditora("Prentice Hall");
        livro.setDisponivel(true);

        livroDAO.salvar(livro);

        Livro encontrado = livroDAO.buscarPorId(1);

        if (encontrado != null) {
            System.out.println(
                    "Livro encontrado: "
                            + encontrado.getTitulo());
        } else {
            System.out.println(
                    "Livro não encontrado.");
        }
    }
}