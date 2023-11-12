package cz.upce.fei.nnptp.em.nnptp.energymonitor;

import cz.upce.fei.nnptp.em.nnptp.energymonitor.entity.ObservedTagsAndFlags;
import cz.upce.fei.nnptp.em.nnptp.energymonitor.entity.ObservedValue;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MeterTest {

    public MeterTest() {
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
    public void testCalculateConsumedElectricityWithMeterChange() {
        LocalDateTime time1 = LocalDateTime.now();
        LocalDateTime time2 = time1.plusHours(1);
        LocalDateTime time3 = time2.plusHours(1);

        List<ObservedValue> observedValues = new ArrayList<>();
        observedValues.add(new ObservedValue(time1, 100.0));

        ObservedValue observerWithMeterChange = new ObservedValue(time2, 150.0);
        observerWithMeterChange.getTagsAndFlags().add(new ObservedTagsAndFlags.MeterReplacedJustAfterMeasurementTag(
                "newMeterId", 50.0));
        observedValues.add(observerWithMeterChange);

        observedValues.add(new ObservedValue(time3, 110.0));

        Meter meter = new Meter(null, null, Meter.MeterType.CumulativeValue, observedValues);
        double consumedPower = meter.calculateConsumedElectricits();

        // Očekávaný výsledek by měl být 150 - 100 + 110 - 50 = 110
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
