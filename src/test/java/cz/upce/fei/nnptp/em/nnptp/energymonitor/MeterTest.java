package cz.upce.fei.nnptp.em.nnptp.energymonitor;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import cz.upce.fei.nnptp.em.nnptp.energymonitor.entity.ObservedValue;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class MeterTest {

    public MeterTest() {
    }
    
    @Test
    public void testCalculatePriceWithNull() {
        Meter meter = new Meter(null, null, Meter.MeterType.ActualValue, null);

        assertEquals(0.0, meter.calculatePrice());
    }

    @Test
    public void testCalculatePriceWithZeroConsumption() {
        Energy energy = new Energy(Energy.EnergyType.HotWater);
        energy.setPricePerMeasuredUnit(2.0);

        LocalDateTime time = LocalDateTime.now();
        List<ObservedValue> observedValues = new ArrayList<>();
        observedValues.add(new ObservedValue(time, 0.0));

        Meter meter = new Meter(energy, null, Meter.MeterType.ActualValue, observedValues);

        assertEquals(0.0, meter.calculatePrice());
    }

    @Test
    public void testCalculatePriceWithCumulativeValueMeterType() {
        Energy energy = new Energy(Energy.EnergyType.Electricity);
        energy.setPricePerMeasuredUnit(0.15);

        LocalDateTime time1 = LocalDateTime.now();
        LocalDateTime time2 = time1.plusHours(3);
        LocalDateTime time3 = time1.plusHours(7);

        List<ObservedValue> observedValues = new ArrayList<>();
        observedValues.add(new ObservedValue(time1, 100.0));
        observedValues.add(new ObservedValue(time2, 120.0));
        observedValues.add(new ObservedValue(time3, 180.0));
        Meter meter = new Meter(energy, null, Meter.MeterType.CumulativeValue, observedValues);

        double expectedPrice = (180 - 100) * 0.15;
        assertEquals(expectedPrice, meter.calculatePrice());
    }

    @Test
    public void testCalculatePriceWithActualValueMeterType() {
        Energy energy = new Energy(Energy.EnergyType.Electricity);
        energy.setPricePerMeasuredUnit(0.43);

        LocalDateTime time1 = LocalDateTime.now();
        LocalDateTime time2 = time1.plusHours(2);
        LocalDateTime time3 = time1.plusHours(3);

        List<ObservedValue> observedValues = new ArrayList<>();
        observedValues.add(new ObservedValue(time1, 20.0));
        observedValues.add(new ObservedValue(time2, 30.0));
        observedValues.add(new ObservedValue(time3, 40.0));
        Meter meter = new Meter(energy, null, Meter.MeterType.ActualValue, observedValues);

        double expectedPrice = (20 + 30 + 40) * 0.43;
        assertEquals(expectedPrice, meter.calculatePrice());
    }
}
