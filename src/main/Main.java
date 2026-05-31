package main;

import ui.TelaInicial;
import util.InicializadorDB;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        InicializadorDB.inicializar();

        SwingUtilities.invokeLater(() -> {
            TelaInicial tela = new TelaInicial();
            tela.setVisible(true);
        });
    }
}