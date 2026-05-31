package model;

public class Professor extends Usuario {

    private String departamento;

    public Professor() {
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    @Override
    public String getTipoUsuario() {
        return "Professor";
    }
}