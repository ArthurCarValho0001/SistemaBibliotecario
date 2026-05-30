package util;

import java.sql.Connection;
import java.sql.Statement;

public class InicializadorDB {

    public static void inicializar() {

        try {

            Connection conexao = ConexaoDB.getConexao();
            Statement stmt = conexao.createStatement();
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS usuarios (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        nome TEXT NOT NULL,
                        email TEXT UNIQUE NOT NULL
                    )
                    """);

            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS livros (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        titulo TEXT NOT NULL,
                        autor TEXT NOT NULL,
                        quantidade INTEGER NOT NULL
                    )
                    """);

            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS emprestimos (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        usuario_id INTEGER NOT NULL,
                        livro_id INTEGER NOT NULL,
                        data_emprestimo TEXT NOT NULL,
                        data_devolucao TEXT,
                        FOREIGN KEY(usuario_id) REFERENCES usuarios(id),
                        FOREIGN KEY(livro_id) REFERENCES livros(id)
                    )
                    """);

            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS fila_espera (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        livro_id INTEGER NOT NULL,
                        usuario_id INTEGER NOT NULL,
                        data_entrada TEXT NOT NULL,
                        posicao INTEGER NOT NULL,
                        FOREIGN KEY(livro_id) REFERENCES livros(id),
                        FOREIGN KEY(usuario_id) REFERENCES usuarios(id)
                    )
                    """);

            stmt.close();

            System.out.println("Banco inicializado com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}