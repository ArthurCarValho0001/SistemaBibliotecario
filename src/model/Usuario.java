package model;

public abstract class Usuario {

    private int id;
    private String nome;
    private String cpf;
    private String email;
    private String matricula;

    public abstract String getTipoUsuario();
}