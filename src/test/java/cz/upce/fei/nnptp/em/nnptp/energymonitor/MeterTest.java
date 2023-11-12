package cz.upce.fei.nnptp.em.nnptp.energymonitor;

import cz.upce.fei.nnptp.em.nnptp.energymonitor.entity.ObservedTagsAndFlags;
import cz.upce.fei.nnptp.em.nnptp.energymonitor.entity.ObservedValue;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MeterTest {

    public MeterTest() {
    }

    @Test
    public void testCalculatePriceWithCumulativeMeter() {
        Energy energy = new Energy(Energy.EnergyType.Electricity);
        energy.setPricePerMeasuredUnit(10.0);

        List<ObservedValue> observedValues = new ArrayList<>();
        observedValues.add(new ObservedValue(LocalDateTime.now(), 100.0));
        observedValues.add(new ObservedValue(LocalDateTime.now().plusHours(1), 120.0));
        observedValues.add(new ObservedValue(LocalDateTime.now().plusHours(2), 180.0));

        Meter meter = new Meter(energy, null, Meter.MeterType.CumulativeValue, observedValues);
        
        double expectedPrice = 800.0;
        assertEquals(expectedPrice, meter.calculatePrice());
    }

    @Test
    public void testCalculatePriceWithActualMeter() {
        Energy energy = new Energy(Energy.EnergyType.Gas);
        energy.setPricePerMeasuredUnit(20.0);

        List<ObservedValue> observedValues = new ArrayList<>();
        observedValues.add(new ObservedValue(LocalDateTime.now(), 50.0));
        observedValues.add(new ObservedValue(LocalDateTime.now().plusHours(1), 80.0));
        observedValues.add(new ObservedValue(LocalDateTime.now().plusHours(2), 100.0));

        Meter meter = new Meter(energy, null, Meter.MeterType.ActualValue, observedValues);

        double expectedPrice = 4600.0;
        assertEquals(expectedPrice, meter.calculatePrice());
    }

    @Test
    public void testCalculatePriceWithPriceChange() {
        Energy energy = new Energy(Energy.EnergyType.HotWater);
        energy.setPricePerMeasuredUnit(2.0);
        
        List<ObservedValue> observedValues = new ArrayList<>();
        ObservedValue value1 = new ObservedValue(LocalDateTime.now(), 200);
        ObservedValue value2 = new ObservedValue(LocalDateTime.now().plusHours(1), 350);

        ObservedTagsAndFlags priceChange = new ObservedTagsAndFlags.UnitPriceChangedJustAfterMeasurementTag(5.0);
        value2.getTagsAndFlags().add(priceChange);

        observedValues.add(value1);
        observedValues.add(value2);

        Meter meter = new Meter(energy, null, Meter.MeterType.ActualValue, observedValues);
        
        double expectedPrice = 2150.0;
        assertEquals(expectedPrice, meter.calculatePrice());
    }
}
