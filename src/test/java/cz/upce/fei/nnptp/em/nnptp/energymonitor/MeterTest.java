package cz.upce.fei.nnptp.em.nnptp.energymonitor;

import cz.upce.fei.nnptp.em.nnptp.energymonitor.entity.ObservedTagsAndFlags;
import cz.upce.fei.nnptp.em.nnptp.energymonitor.entity.ObservedValue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MeterTest {

    private final LocalDateTime currentTime = LocalDateTime.now();

    private Meter createMeterWithValues(Energy energy, Meter.MeterType meterType) {
        List<ObservedValue> observedValues = new ArrayList<>(List.of(new ObservedValue(currentTime.minusDays(4), 50.0),
                                                                     new ObservedValue(currentTime.minusDays(2), 70.5),
                                                                     new ObservedValue(currentTime, 80.0),
                                                                     new ObservedValue(currentTime.plusDays(1), 100.5),
                                                                     new ObservedValue(currentTime.plusDays(4), 150.0),
                                                                     new ObservedValue(currentTime.plusDays(9), 200.5),
                                                                     new ObservedValue(currentTime.plusMonths(2), 500.0)));
        return createMeterWithCustomValues(energy, meterType, observedValues);
    }

    private Meter createMeterWithCustomValues(Energy energy, Meter.MeterType meterType, List<ObservedValue> observedValues) {
        return new Meter(energy, null, meterType, observedValues);
    }

    @Test
    public void testCalculateConsumedPowerActualInInterval() {
        Meter meter = createMeterWithValues(null, Meter.MeterType.ACTUAL_VALUE);
        //70.5 + 80 + 100.5 + 150 = 401
        assertEquals(401.0, meter.calculateConsumedPower(currentTime.minusDays(3), currentTime.plusDays(5)));
    }

    @Test
    public void testCalculateConsumedPowerCumulativeInInterval() {
        Meter meter = createMeterWithValues(null, Meter.MeterType.CUMULATIVE_VALUE);
        //(80 - 70.5) + (100.5 - 80) + (150 - 100.5) = 79.5
        assertEquals(79.5, meter.calculateConsumedPower(currentTime.minusDays(3), currentTime.plusDays(5)));
    }

    @Test
    public void testCalculateConsumedPowerCumulativeValuesIsEmpty() {
        Meter meter = createMeterWithCustomValues(null, Meter.MeterType.CUMULATIVE_VALUE, new ArrayList<>());
        assertEquals(0.0, meter.calculateConsumedPower());
    }

    @Test
    public void testCalculateConsumedPowerCumulativeValuesIsNull() {
        Meter meter = createMeterWithCustomValues(null, Meter.MeterType.CUMULATIVE_VALUE, null);
        assertEquals(0.0, meter.calculateConsumedPower());
    }

    @Test
    public void testCalculateConsumedPowerActualInIntervalValuesIsEmpty() {
        Meter meter = createMeterWithCustomValues(null, Meter.MeterType.ACTUAL_VALUE, new ArrayList<>());
        assertEquals(0.0, meter.calculateConsumedPower(currentTime.minusDays(3), currentTime.plusDays(5)));
    }

    @Test
    public void testCalculateConsumedPowerActualInIntervalValuesIsNull() {
        Meter meter = createMeterWithCustomValues(null, Meter.MeterType.ACTUAL_VALUE, null);
        assertEquals(0.0, meter.calculateConsumedPower(currentTime.minusDays(3), currentTime.plusDays(5)));
    }

    @Test
    public void testCalculateConsumedPowerCumulativeInIntervalChangedMeter() {
        List<ObservedValue> observedValues = new ArrayList<>(List.of(new ObservedValue(currentTime.minusDays(4), 1.0),
                                                                     new ObservedValue(currentTime.minusDays(2), 2.5),
                                                                     new ObservedValue(currentTime, 5.5)));

        ObservedValue observedWithFirstNewMeter = new ObservedValue(currentTime.plusDays(1), 12.0);
        observedWithFirstNewMeter.getTagsAndFlags().add(new ObservedTagsAndFlags.MeterReplacedJustAfterMeasurementTag("M1", 40.0));
        observedValues.add(observedWithFirstNewMeter);

        observedValues.add(new ObservedValue(currentTime.plusDays(2), 50.0));
        observedValues.add(new ObservedValue(currentTime.plusDays(4), 60.5));

        ObservedValue observedWithSecondNewMeter = new ObservedValue(currentTime.plusDays(9), 60.5);
        observedWithSecondNewMeter.getTagsAndFlags().add(new ObservedTagsAndFlags.MeterReplacedJustAfterMeasurementTag("M2", 20.0));
        observedValues.add(observedWithSecondNewMeter);

        observedValues.add(new ObservedValue(currentTime.plusDays(10), 30.5));
        observedValues.add(new ObservedValue(currentTime.plusMonths(2), 30.5));

        Meter meter = createMeterWithCustomValues(null, Meter.MeterType.CUMULATIVE_VALUE, observedValues);
        //(5,5 - 2,5) + (12 - 5,5) + (50 - 40) + (60,5 - 50) + (30,5 - 20) = 40.5
        assertEquals(40.5, meter.calculateConsumedPower(currentTime.minusDays(3), currentTime.plusDays(10)));
    }

    @Test
    public void testCalculateConsumedPowerCumulativeSameTimeOfObservedValues() {
        List<ObservedValue> observedValues = new ArrayList<>(List.of(new ObservedValue(currentTime, 100.0),
                                                                     new ObservedValue(currentTime.plusDays(1), 150.0),
                                                                     new ObservedValue(currentTime.plusDays(1), 210.0)));

        Meter meter = createMeterWithCustomValues(null, Meter.MeterType.CUMULATIVE_VALUE, observedValues);
        //210 - 100 = 110
        assertEquals(110, meter.calculateConsumedPower());
    }

    @Test
    public void testCalculateConsumedPowerCumulativeWithMeterChange() {
        List<ObservedValue> observedValues = new ArrayList<>();

        observedValues.add(new ObservedValue(currentTime, 100.0));

        ObservedValue observedValue = new ObservedValue(currentTime.plusDays(1), 150.0);
        ObservedTagsAndFlags tagAndFlag = new ObservedTagsAndFlags.MeterReplacedJustAfterMeasurementTag("M1", 50.0);
        observedValue.getTagsAndFlags().add(tagAndFlag);

        observedValues.add(observedValue);
        observedValues.add(new ObservedValue(currentTime.plusDays(2), 110.0));

        Meter meter = createMeterWithCustomValues(null, Meter.MeterType.CUMULATIVE_VALUE, observedValues);
        //(150 - 100) + (110 - 50) = 110
        assertEquals(110, meter.calculateConsumedPower());
    }

    @Test
    public void testCalculateConsumedPowerCumulativeWithMultipleMeterChange() {
        List<ObservedValue> observedValues = new ArrayList<>();
        ObservedValue observedValue;
        ObservedTagsAndFlags tagAndFlag;

        observedValues.add(new ObservedValue(currentTime, 100.0));

        observedValue = new ObservedValue(currentTime.plusHours(1), 150.0);
        tagAndFlag = new ObservedTagsAndFlags.MeterReplacedJustAfterMeasurementTag("M1", 50.0);
        observedValue.getTagsAndFlags().add(tagAndFlag);

        observedValues.add(observedValue);
        observedValues.add(new ObservedValue(currentTime.plusHours(2), 150.0));

        observedValue = new ObservedValue(currentTime.plusHours(3), 170.0);
        tagAndFlag = new ObservedTagsAndFlags.MeterReplacedJustAfterMeasurementTag("M2", 10.0);
        observedValue.getTagsAndFlags().add(tagAndFlag);

        observedValues.add(observedValue);
        observedValues.add(new ObservedValue(currentTime.plusHours(4), 30.0));

        Meter meter = createMeterWithCustomValues(null, Meter.MeterType.CUMULATIVE_VALUE, observedValues);
        //(150 - 100) + (170 - 50) + (30 - 10) = 190
        assertEquals(190, meter.calculateConsumedPower());
    }

    @Test
    public void testCalculateConsumedPowerActual() {
        Meter meter = createMeterWithValues(null, Meter.MeterType.ACTUAL_VALUE);
        //50 + 70.5 + 80 + 100.5 + 150 + 200.5 + 500 = 1151.5
        assertEquals(1151.5, meter.calculateConsumedPower());
    }

    @Test
    public void testCalculateElectricityPriceCumulativeWithUnitPrice() {
        Meter meter = createMeterWithValues(new Energy(Energy.EnergyType.ELECTRICITY, 10.0), Meter.MeterType.CUMULATIVE_VALUE);
        //(20.5 * 10) + (10.5 * 10) + ... + (299.5 * 10) = 4500
        assertEquals(4500.0, meter.calculatePrice());
    }

    @Test
    public void testCalculateGasPriceActualWithUnitPrice() {
        Meter meter = createMeterWithValues(new Energy(Energy.EnergyType.GAS, 20.0), Meter.MeterType.ACTUAL_VALUE);
        //(50 * 20) + (70.5 * 20) + ... + (500 * 20) = 23030
        assertEquals(23030.0, meter.calculatePrice());
    }

    @Test
    public void testCalculateHotWaterPriceCumulativeWithPriceChange() {
        ObservedValue changedValue = new ObservedValue(currentTime.plusHours(5), 300);
        ObservedTagsAndFlags changedPrice = new ObservedTagsAndFlags.UnitPriceChangedJustAfterMeasurementTag(10.0);
        changedValue.getTagsAndFlags().add(changedPrice);

        List<ObservedValue> observedValues = new ArrayList<>(List.of(new ObservedValue(currentTime, 200),
                                                                     changedValue,
                                                                     new ObservedValue(currentTime.plusHours(8), 350)));

        Meter meter = createMeterWithCustomValues(new Energy(Energy.EnergyType.HOT_WATER, 5.0), Meter.MeterType.CUMULATIVE_VALUE, observedValues);
        //((300 - 200) * 5) + ((350 - 300) * 10) = 1000
        assertEquals(1000.0, meter.calculatePrice());
    }

    @Test
    public void testCalculateHotWaterPriceCumulativeWithPriceAndMeterChange() {
        ObservedValue changedValue = new ObservedValue(currentTime.plusHours(5), 300);
        ObservedTagsAndFlags changedPrice = new ObservedTagsAndFlags.UnitPriceChangedJustAfterMeasurementTag(10.0);
        ObservedTagsAndFlags changedMeter = new ObservedTagsAndFlags.MeterReplacedJustAfterMeasurementTag("M1", 50);
        changedValue.getTagsAndFlags().add(changedPrice);
        changedValue.getTagsAndFlags().add(changedMeter);

        List<ObservedValue> observedValues = new ArrayList<>(List.of(new ObservedValue(currentTime, 200),
                                                                     changedValue,
                                                                     new ObservedValue(currentTime.plusHours(8), 150)));

        Meter meter = createMeterWithCustomValues(new Energy(Energy.EnergyType.HOT_WATER, 5.0), Meter.MeterType.CUMULATIVE_VALUE, observedValues);
        //((300 - 200) * 5) + ((150 - 50) * 10) = 1500
        assertEquals(1500.0, meter.calculatePrice());
    }

    @Test
    public void testCalculateElectricityPriceCumulativeInInterval() {
        Meter meter = createMeterWithValues(new Energy(Energy.EnergyType.ELECTRICITY, 10.0), Meter.MeterType.CUMULATIVE_VALUE);
        //(150 - 100.5) * 10 = 495
        assertEquals(495.0, meter.calculatePrice(currentTime.plusDays(1), currentTime.plusDays(5)));
    }

    @Test
    public void testCalculateElectricityPriceCumulativeFromDate() {
        Meter meter = createMeterWithValues(new Energy(Energy.EnergyType.ELECTRICITY, 10.0), Meter.MeterType.CUMULATIVE_VALUE);
        //(500 - 200.5) * 10 = 2995
        assertEquals(2995.0, meter.calculatePrice(currentTime.plusDays(5), null));
    }

    @Test
    public void testCalculateElectricityPriceCumulativeToDate() {
        Meter meter = createMeterWithValues(new Energy(Energy.EnergyType.ELECTRICITY, 10.0), Meter.MeterType.CUMULATIVE_VALUE);
        //((70,5 - 50) * 10) + ((80 - 70,5) * 10) = 1000
        assertEquals(300.0, meter.calculatePrice(null, currentTime));
    }

}
