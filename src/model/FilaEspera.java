package model;

import java.time.LocalDate;

public class FilaEspera {

    private int id;

    private Livro livro;

    private Usuario usuario;

    private LocalDate dataEntrada;

    private int posicao;

    public FilaEspera() {
    }

    public FilaEspera(Livro livro,
                      Usuario usuario,
                      int posicao) {

        this.livro = livro;
        this.usuario = usuario;
        this.posicao = posicao;

        this.dataEntrada = LocalDate.now();
    }
}