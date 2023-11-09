package cz.upce.fei.nnptp.em.nnptp.energymonitor;

import cz.upce.fei.nnptp.em.nnptp.energymonitor.entity.ObservedValue;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Test
    public void testCalculateConsumedElectricityNull() {
        Meter meter = new Meter(null, null, Meter.MeterType.CumulativeValue, null);

        double consumedPower = meter.calculateConsumedElectricits();
        assertEquals(0, consumedPower);
    }

    @Test
    public void testCalculateConsumedElectricityEmpty() {
        List<ObservedValue> observedValues = new ArrayList<>();
        Meter meter = new Meter(null, null, Meter.MeterType.CumulativeValue, observedValues);

        double consumedPower = meter.calculateConsumedElectricits();
        assertEquals(0, consumedPower);
    }

    @Test
    public void testCalculateConsumedElectricityCumulative() {
        LocalDateTime time1 = LocalDateTime.now();
        LocalDateTime time2 = time1.plusHours(1);
        List<ObservedValue> observedValues = new ArrayList<>();
        observedValues.add(new ObservedValue(time1, 100.0));
        observedValues.add(new ObservedValue(time2, 150.0));
        observedValues.add(new ObservedValue(time2, 210.0));
        Meter meter = new Meter(null, null, Meter.MeterType.CumulativeValue, observedValues);
        
        double consumedPower = meter.calculateConsumedElectricits();
        assertEquals(110, consumedPower);
    }

    @Test
    public void testCalculateConsumedElectricityActual() {
        LocalDateTime time1 = LocalDateTime.now();
        LocalDateTime time2 = time1.plusHours(1);
        LocalDateTime time3 = time2.plusHours(1);
        List<ObservedValue> observedValues = new ArrayList<>();
        observedValues.add(new ObservedValue(time1, 10.0));
        observedValues.add(new ObservedValue(time2, 20.0));
        observedValues.add(new ObservedValue(time3, 30.0));
        Meter meter = new Meter(null, null, Meter.MeterType.ActualValue,observedValues);

        double consumedPower = meter.calculateConsumedElectricits();
        assertEquals(60.0, consumedPower);
    }
    
}
