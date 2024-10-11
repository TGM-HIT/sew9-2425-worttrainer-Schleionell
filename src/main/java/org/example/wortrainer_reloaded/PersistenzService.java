package org.example.wortrainer_reloaded;

import service.WorttrainerService;

/**
 * Interface für den Persistenz-Service.
 */
public interface PersistenzService {
    void speichern(WorttrainerService worttrainerService);

    WorttrainerService laden();
}
