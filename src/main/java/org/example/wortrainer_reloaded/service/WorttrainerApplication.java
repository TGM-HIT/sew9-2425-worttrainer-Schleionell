package org.example.wortrainer_reloaded.service;

import org.example.wortrainer_reloaded.gui.WorttrainerGUI;

/**
 * Startet die Anwendung.
 * @author Lionel Ruf
 * @version 10.10.2024
 */
public class WorttrainerApplication {
    public static void main(String[] args) {
        WorttrainerGUI gui = new WorttrainerGUI();
        gui.starten();
    }
}
