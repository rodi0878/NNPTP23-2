package cz.upce.fei.nnptp.em.nnptp.energymonitor;

import cz.upce.fei.nnptp.em.nnptp.energymonitor.entity.ObservedTagsAndFlags;
import cz.upce.fei.nnptp.em.nnptp.energymonitor.entity.ObservedValue;
import cz.upce.fei.nnptp.em.nnptp.energymonitor.persistence.Persistence;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
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
    public void testSaveStateToFile() throws IOException {
        File file = new File("TestStateFile.bin");
        State state = new State();
        ArrayList<Meter> meters = new ArrayList<>();
        ArrayList<ObservedValue> observedValues = new ArrayList<>();
        ObservedValue observedValue = new ObservedValue(LocalDateTime.of(2023, Month.JULY, 11, 20, 15, 30), 15);

        observedValue.addTagsAndFlags(new ObservedTagsAndFlags.MeterReplacedJustAfterMeasurementTag("Identification", 500));
        observedValue.addTagsAndFlags(new ObservedTagsAndFlags.UnitPriceChangedJustAfterMeasurementTag(1000));
        observedValues.add(observedValue);
        meters.add(new Meter(new Energy(Energy.EnergyType.ELECTRICITY), new Distribution("Test distribution", "Test distribution address"), Meter.MeterType.ACTUAL_VALUE, observedValues));
        state.addConnection(new ConnectionPoint(1, "Test address", meters));

        Persistence.saveState(state, file);
        assertTrue(file.exists());
    }


    @Test
    public void testSaveAndLoadStateFile() throws IOException {
        File file = new File("TestStateFile.bin");
        State saveState = new State();
        ArrayList<Meter> meters = new ArrayList<>();
        ArrayList<ObservedValue> observedValues = new ArrayList<>();
        ObservedValue observedValue = new ObservedValue(LocalDateTime.of(2023, Month.JULY, 11, 20, 15, 30), 15);

        observedValue.addTagsAndFlags(new ObservedTagsAndFlags.MeterReplacedJustAfterMeasurementTag("Identification", 500));
        observedValue.addTagsAndFlags(new ObservedTagsAndFlags.UnitPriceChangedJustAfterMeasurementTag(1000));
        observedValues.add(observedValue);
        meters.add(new Meter(new Energy(Energy.EnergyType.ELECTRICITY), new Distribution("Test distribution", "Test distribution address"), Meter.MeterType.ACTUAL_VALUE, observedValues));
        saveState.addConnection(new ConnectionPoint(1, "Test address", meters));

        Persistence.saveState(saveState, file);
        State loadState = Persistence.loadState(file);

        assertEquals(1, loadState.getConnectionPoints().size());
        ConnectionPoint loadStateConnectionPoint = loadState.getConnectionPoints().get(0);
        assertEquals("Test address" , loadStateConnectionPoint.getAddress());
        assertEquals(1, loadStateConnectionPoint.getId());

        assertEquals(1, loadStateConnectionPoint.getMeters().size());

        Meter loadStateConnectionPointMeter = loadStateConnectionPoint.getMeters().get(0);
        assertEquals(Energy.EnergyType.ELECTRICITY, loadStateConnectionPointMeter.getEnergy().getEnergyType());
        assertNull(loadStateConnectionPointMeter.getEnergy().getName());
        assertEquals(0, loadStateConnectionPointMeter.getEnergy().getPricePerMeasuredUnit());
        assertEquals(Meter.MeterType.ACTUAL_VALUE, loadStateConnectionPointMeter.getMeterType());

        assertEquals("Test distribution address", loadStateConnectionPointMeter.getDistribution().getAddress());
        assertEquals("Test distribution", loadStateConnectionPointMeter.getDistribution().getName());

        assertEquals(1, loadState.getConnectionPoints().get(0).getMeters().get(0).getObservedValues().size());

        ObservedValue loadObservedValue = loadState.getConnectionPoints().get(0).getMeters().get(0).getObservedValues().get(0);
        assertEquals(LocalDateTime.of(2023, Month.JULY, 11, 20, 15, 30), loadObservedValue.getLocalDateTime());
        assertEquals(15, loadObservedValue.getValue());
        assertEquals(2, loadObservedValue.getTagsAndFlags().size());

        assertEquals("Identification", ((ObservedTagsAndFlags.MeterReplacedJustAfterMeasurementTag)loadObservedValue.getTagsAndFlags().get(0)).getNewMeterIdentification());
        assertEquals(500, ((ObservedTagsAndFlags.MeterReplacedJustAfterMeasurementTag)loadObservedValue.getTagsAndFlags().get(0)).getMeterStartValue());

        assertEquals(1000, ((ObservedTagsAndFlags.UnitPriceChangedJustAfterMeasurementTag)loadObservedValue.getTagsAndFlags().get(1)).getNewUnitPrice());
    }
}
