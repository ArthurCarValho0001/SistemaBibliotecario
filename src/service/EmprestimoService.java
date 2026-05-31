package service;

import dao.EmprestimoDAO;
import dao.FilaEsperaDAO;
import dao.LivroDAO;
import dao.UsuarioDAO;
import model.*;

import java.time.LocalDate;
import java.util.List;

public class EmprestimoService {

    private EmprestimoDAO emprestimoDAO = new EmprestimoDAO();
    private FilaEsperaDAO filaEsperaDAO = new FilaEsperaDAO();
    private LivroDAO livroDAO = new LivroDAO();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public String realizarEmprestimo(String matricula, int livroId) {

        Usuario usuario = usuarioDAO.buscarPorMatricula(matricula);
        if (usuario == null) {
            return "Erro: usuário não encontrado.";
        }

        Livro livro = livroDAO.buscarPorId(livroId);
        if (livro == null) {
            return "Erro: livro não encontrado.";
        }

        if (livro.isDisponivel()) {
            // livro disponível — empréstimo direto
            Emprestimo emprestimo = new Emprestimo(usuario, livro);
            emprestimoDAO.salvar(emprestimo);

            livro.setDisponivel(false);
            livroDAO.atualizar(livro);

            return "Empréstimo realizado com sucesso! Devolução prevista: " + emprestimo.getDataDevolucaoPrevista();
        } else {
            return "INDISPONIVEL";
        }
    }

    public String entrarNaFila(String matricula, int livroId) {

        Usuario usuario = usuarioDAO.buscarPorMatricula(matricula);
        if (usuario == null) {
            return "Erro: usuário não encontrado.";
        }

        Livro livro = livroDAO.buscarPorId(livroId);
        if (livro == null) {
            return "Erro: livro não encontrado.";
        }

        if (filaEsperaDAO.usuarioJaNaFila(livroId, usuario.getId())) {
            return "Erro: você já está na fila para este livro.";
        }

        int posicao = filaEsperaDAO.proximaPosicao(livroId);
        FilaEspera fila = new FilaEspera(livro, usuario, posicao);
        filaEsperaDAO.adicionarNaFila(fila);

        Emprestimo reserva = new Emprestimo();
        reserva.setUsuario(usuario);
        reserva.setLivro(livro);
        reserva.setDataEmprestimo(LocalDate.now());
        reserva.setDataDevolucaoPrevista(LocalDate.now().plusDays(30));
        reserva.setStatus(StatusEmprestimo.RESERVADO);
        emprestimoDAO.salvar(reserva);

        return "Você entrou na fila na posição " + posicao + ".";
    }

    public String realizarDevolucao(int emprestimoId) {

        Emprestimo emprestimo = emprestimoDAO.buscarPorId(emprestimoId);
        if (emprestimo == null) {
            return "Erro: empréstimo não encontrado.";
        }

        if (emprestimo.getStatus() == StatusEmprestimo.DEVOLVIDO) {
            return "Erro: este empréstimo já foi devolvido.";
        }

        emprestimo.setStatus(StatusEmprestimo.DEVOLVIDO);
        emprestimo.setDataDevolucaoReal(LocalDate.now());
        emprestimoDAO.atualizar(emprestimo);

        Livro livro = emprestimo.getLivro();
        livro.setDisponivel(true);
        livroDAO.atualizar(livro);

        String avisoFila = verificarFila(livro);

        return "Devolução registrada com sucesso!" + avisoFila;
    }

    public void verificarAtrasos() {

        List<Emprestimo> ativos = emprestimoDAO.listarTodos();

        for (Emprestimo emp : ativos) {
            if (emp.getStatus() == StatusEmprestimo.ATIVO) {
                StatusEmprestimo statusAntes = emp.getStatus();
                emp.verificarAtraso();
                if (emp.getStatus() != statusAntes) {
                    emprestimoDAO.atualizar(emp);
                }
            }
        }
    }

    public String cancelarReserva(int emprestimoId) {

        Emprestimo emprestimo = emprestimoDAO.buscarPorId(emprestimoId);
        if (emprestimo == null) {
            return "Erro: reserva não encontrada.";
        }

        if (emprestimo.getStatus() != StatusEmprestimo.RESERVADO) {
            return "Erro: este empréstimo não é uma reserva ativa.";
        }

        emprestimo.setStatus(StatusEmprestimo.CANCELADO);
        emprestimoDAO.atualizar(emprestimo);

        List<FilaEspera> fila = filaEsperaDAO.listarFilaPorLivro(emprestimo.getLivro().getId());
        for (FilaEspera f : fila) {
            if (f.getUsuario().getId() == emprestimo.getUsuario().getId()) {
                filaEsperaDAO.removerDaFila(f.getId());
                break;
            }
        }

        return "Reserva cancelada com sucesso.";
    }

    public List<Emprestimo> listarTodos() {
        return emprestimoDAO.listarTodos();
    }

    public List<Emprestimo> listarAtrasados() {
        return emprestimoDAO.listarAtrasados();
    }

    private String verificarFila(Livro livro) {

        FilaEspera proximo = filaEsperaDAO.proximoDaFila(livro.getId());

        if (proximo == null) {
            return "";
        }

        filaEsperaDAO.removerDaFila(proximo.getId());

        Emprestimo novoEmprestimo = new Emprestimo(proximo.getUsuario(), livro);
        emprestimoDAO.salvar(novoEmprestimo);

        livro.setDisponivel(false);
        livroDAO.atualizar(livro);

        return "\nAviso: livro reservado automaticamente para " + proximo.getUsuario().getNome();
    }
}