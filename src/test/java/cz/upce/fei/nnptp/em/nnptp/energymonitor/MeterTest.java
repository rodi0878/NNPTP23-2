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

        // The expected result should be 150 - 100 + 110 - 50 = 110
        assertEquals(110, consumedPower);
    }

    @Test
    public void testCalculateConsumedElectricityWithMultipleMeterChange() {
        LocalDateTime time = LocalDateTime.now();

        List<ObservedValue> observedValues = new ArrayList<>();
        observedValues.add(new ObservedValue(time, 100.0));

        ObservedValue observerWithMeterChange1 = new ObservedValue(time.plusHours(1), 150.0);
        observerWithMeterChange1.getTagsAndFlags().add(new ObservedTagsAndFlags.MeterReplacedJustAfterMeasurementTag(
                "newMeterId1", 50.0));
        observedValues.add(observerWithMeterChange1);

        observedValues.add(new ObservedValue(time.plusHours(2), 110.0));
        observedValues.add(new ObservedValue(time.plusHours(2), 150.0));

        ObservedValue observerWithMeterChange2 = new ObservedValue(time.plusHours(3), 170.0);
        observerWithMeterChange2.getTagsAndFlags().add(new ObservedTagsAndFlags.MeterReplacedJustAfterMeasurementTag(
                "newMeterId2", 10.0));
        observedValues.add(observerWithMeterChange2);

        observedValues.add(new ObservedValue(time.plusHours(4), 30.0));


        Meter meter = new Meter(null, null, Meter.MeterType.CumulativeValue, observedValues);
        double consumedPower = meter.calculateConsumedElectricits();

        // The expected result should be 150 - 100 + 170 - 50 + 30 - 10 = 190
        assertEquals(190, consumedPower);
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
