package cz.upce.fei.nnptp.em.nnptp.energymonitor;

import cz.upce.fei.nnptp.em.nnptp.energymonitor.entity.ObservedTagsAndFlags;
import cz.upce.fei.nnptp.em.nnptp.energymonitor.entity.ObservedValue;
import cz.upce.fei.nnptp.em.nnptp.energymonitor.persistence.Persistence;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

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

    @Test
    public void testSaveStateToFile(){
        File file = new File("TestStateFile.bin");
        State state = new State();
        ArrayList<Meter> meters = new ArrayList<>();
        ArrayList<ObservedValue> observedValues = new ArrayList<>();
        ObservedValue observedValue = new ObservedValue(LocalDateTime.of(2023, Month.JULY, 11, 20, 15, 30), 15);

        observedValue.addTagsAndFlags(new ObservedTagsAndFlags.MeterReplacedJustAfterMeasurementTag("Identification", 5));
        observedValue.addTagsAndFlags(new ObservedTagsAndFlags.UnitPriceChangedJustAfterMeasurementTag(10));
        observedValues.add(observedValue);
        meters.add(new Meter(new Energy(Energy.EnergyType.ELECTRICITY), new Distribution("Test distribution", "Test distribution address"), Meter.MeterType.ActualValue, observedValues));
        state.addConnection(new ConnectionPoint(1, "Test address", meters));

        Persistence.saveState(state, file);
    }

}
