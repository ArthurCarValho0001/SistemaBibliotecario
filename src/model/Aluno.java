package model;

public class Aluno extends Usuario {

    public Aluno() {
    }

    @Override
    public String getTipoUsuario() {
        return "Aluno";
    }
}