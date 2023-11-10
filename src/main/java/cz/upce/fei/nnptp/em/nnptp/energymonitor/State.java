package cz.upce.fei.nnptp.em.nnptp.energymonitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class representing the state of the system with connected measurement points.
 */
public class State {

    private final List<ConnectionPoint> connectionPoints;

    public State() {
        connectionPoints  = new ArrayList<>();
    }

    /**
     * Returns an uneditable list of connected measurement points.
     *
     * @return List of connected measuring points
     */
    public List<ConnectionPoint> getConnectionPoints() {
        return Collections.unmodifiableList(connectionPoints);
    }

    public void addConnection(ConnectionPoint newConnectionPoint) {
        if (connectionPoints.contains(newConnectionPoint)) {
            return;
        }

        connectionPoints.add(newConnectionPoint);
    }
}
