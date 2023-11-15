package cz.upce.fei.nnptp.em.nnptp.energymonitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class State {

    private final List<ConnectionPoint> connectionPoints;

    public State() {
        connectionPoints  = new ArrayList<>();
    }

    public List<ConnectionPoint> getConnectionPoints() {
        return Collections.unmodifiableList(connectionPoints);
    }
    
    public void addConnection(ConnectionPoint cp) {
        if (!connectionPoints.contains(cp)) {
            connectionPoints.add(cp);
        }
    }

    
    
}
