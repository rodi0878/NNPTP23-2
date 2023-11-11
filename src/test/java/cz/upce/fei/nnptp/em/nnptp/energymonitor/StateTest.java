package cz.upce.fei.nnptp.em.nnptp.energymonitor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StateTest {

    public StateTest() {
    }

    @Test
    public void testGetConnectionPoints() {
        State state = new State();
        ConnectionPoint connectionPoint = new ConnectionPoint(0, "", null);
        state.addConnection(connectionPoint);

        assertThrows(UnsupportedOperationException.class, () -> state.getConnectionPoints().add(connectionPoint));
    }

    /**
     * Test of addConnection method, of class State.
     */
    @Test
    public void testAddConnectionBehaviour() {
        State state = new State();

        ConnectionPoint connectionPoint1 = new ConnectionPoint(0, "", null);
        ConnectionPoint connectionPoint2 = new ConnectionPoint(0, "", null);

        assertTrue(state.getConnectionPoints().isEmpty());

        state.addConnection(connectionPoint1);

        assertFalse(state.getConnectionPoints().isEmpty());
        assertEquals(connectionPoint1, state.getConnectionPoints().get(0));
        assertEquals(1, state.getConnectionPoints().size());

        state.addConnection(connectionPoint1);

        assertFalse(state.getConnectionPoints().isEmpty());
        assertEquals(connectionPoint1, state.getConnectionPoints().get(0));
        assertEquals(1, state.getConnectionPoints().size());

        state.addConnection(connectionPoint2);

        assertFalse(state.getConnectionPoints().isEmpty());
        assertEquals(connectionPoint1, state.getConnectionPoints().get(0));
        assertEquals(connectionPoint2, state.getConnectionPoints().get(1));
        assertEquals(2, state.getConnectionPoints().size());

    }

}
