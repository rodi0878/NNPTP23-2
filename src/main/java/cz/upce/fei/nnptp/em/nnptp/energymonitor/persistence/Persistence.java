package cz.upce.fei.nnptp.em.nnptp.energymonitor.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cz.upce.fei.nnptp.em.nnptp.energymonitor.State;

import java.io.*;

/**
 * Persistence - save and load state of all
 * ConnectionPoints, Meters and Distributions
 */
public class Persistence {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void saveState(State state, File file) {
        if (fileExists(file)) {
            try (Writer writer = new FileWriter(file)) {
                gson.toJson(state, writer);
                System.out.println("State saved successfully.");
            } catch (IOException e) {
                System.err.println("Failed to save state: " + e.getMessage());
            }
        } else {
            System.err.println("Failed to save state.");
        }
    }

    public void loadState(File file) {
        //TODO
    }

    private boolean fileExists(File file) {
        //TODO
        return true;
    }
}
