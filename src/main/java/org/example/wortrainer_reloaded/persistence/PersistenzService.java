package org.example.wortrainer_reloaded.persistence;

import org.example.wortrainer_reloaded.service.WorttrainerService;

/**
 * Interface für den Persistenz-Service.
 */
public interface PersistenzService {
    void speichern(WorttrainerService worttrainerService);

    WorttrainerService laden();
}
