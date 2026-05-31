package model;

import java.time.LocalDate;

public class Emprestimo {

    private int id;
    private Usuario usuario;
    private Livro livro;

    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucaoPrevista;
    private LocalDate dataDevolucaoReal;

    private StatusEmprestimo status;

    public Emprestimo() {
    }

    public Emprestimo(Usuario usuario, Livro livro) {

        this.usuario = usuario;
        this.livro = livro;

        this.dataEmprestimo = LocalDate.now();

        this.dataDevolucaoPrevista =
                dataEmprestimo.plusDays(30);

        this.status = StatusEmprestimo.ATIVO;
    }

    public void verificarAtraso() {

        if (status == StatusEmprestimo.ATIVO &&
                LocalDate.now().isAfter(dataDevolucaoPrevista)) {

            status = StatusEmprestimo.ATRASADO;
        }
    }

    public StatusEmprestimo getStatus() {
        return status;
    }
}