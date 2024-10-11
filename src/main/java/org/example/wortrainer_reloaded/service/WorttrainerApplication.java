package org.example.wortrainer_reloaded.service;

import org.example.wortrainer_reloaded.gui.WorttrainerGUI;

/**
 * Startet die Anwendung.
 */
public class WorttrainerApplication {
    public static void main(String[] args) {
        WorttrainerGUI gui = new WorttrainerGUI();
        gui.starten();
    }
}
