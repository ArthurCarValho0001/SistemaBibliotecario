package model;

import java.time.LocalDate;

public class FilaEspera {

    private int id;
    private Livro livro;
    private Usuario usuario;
    private LocalDate dataEntrada;
    private int posicao;

    public FilaEspera() {}

    public FilaEspera(Livro livro, Usuario usuario, int posicao) {
        this.livro = livro;
        this.usuario = usuario;
        this.posicao = posicao;
        this.dataEntrada = LocalDate.now();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Livro getLivro() { return livro; }
    public void setLivro(Livro livro) { this.livro = livro; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public LocalDate getDataEntrada() { return dataEntrada; }
    public void setDataEntrada(LocalDate dataEntrada) { this.dataEntrada = dataEntrada; }

    public int getPosicao() { return posicao; }
    public void setPosicao(int posicao) { this.posicao = posicao; }
}