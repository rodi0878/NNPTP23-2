package cz.upce.fei.nnptp.em.nnptp.energymonitor.persistence;

import cz.upce.fei.nnptp.em.nnptp.energymonitor.State;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/**
 *
 */
public class Persistence {
    public static void saveState(State state, File file) {
        try (FileOutputStream outputStateFile = new FileOutputStream(file)) {
            ObjectOutputStream saveStateStream = new ObjectOutputStream(outputStateFile);
            saveStateStream.writeObject(state);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    // TODO loadState
}
