package cz.upce.fei.nnptp.em.nnptp.energymonitor;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 */
public class StateTest {
    
    public StateTest() {
    }

    @Test
    public void testGetConnectionPoints() {
 State state = new State();
        ConnectionPoint cp1 = new ConnectionPoint(0, "", null);
        state.addConnection(cp1);
        
        assertThrows(UnsupportedOperationException.class, () -> {
        state.getConnectionPoints().add(cp1);
        });
    }

    /**
     * Test of addConnection method, of class State.
     */
    @Test
    public void testAddConnectionBehaviour() {
        State state = new State();
        
        ConnectionPoint cp1 = new ConnectionPoint(0, "", null);
        ConnectionPoint cp2 = new ConnectionPoint(0, "", null);
        
        assertTrue(state.getConnectionPoints().isEmpty());
        
        state.addConnection(cp1);
        
        assertFalse(state.getConnectionPoints().isEmpty());
        assertEquals(cp1, state.getConnectionPoints().get(0));
        assertEquals(1, state.getConnectionPoints().size());
        
        state.addConnection(cp1);
        
        assertFalse(state.getConnectionPoints().isEmpty());
        assertEquals(cp1, state.getConnectionPoints().get(0));
        assertEquals(1, state.getConnectionPoints().size());
        
                state.addConnection(cp2);
        
        assertFalse(state.getConnectionPoints().isEmpty());
        assertEquals(cp1, state.getConnectionPoints().get(0));
        assertEquals(cp2, state.getConnectionPoints().get(1));
        assertEquals(2, state.getConnectionPoints().size());
        
    }
    
}
