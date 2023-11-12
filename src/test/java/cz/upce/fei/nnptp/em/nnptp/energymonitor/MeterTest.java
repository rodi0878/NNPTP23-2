package cz.upce.fei.nnptp.em.nnptp.energymonitor;

import cz.upce.fei.nnptp.em.nnptp.energymonitor.entity.ObservedValue;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class MeterTest {

    public MeterTest() {
    }

    @Test
    public void testCalculatePriceWithZeroConsumption() {
        Energy energy = new Energy(Energy.EnergyType.HotWater);
        energy.setPricePerMeasuredUnit(2.0);
        
        Meter meter = Mockito.mock(Meter.class);
        when(meter.calculateConsumedElectricits()).thenReturn(0.0);

        assertEquals(0.0, meter.calculatePrice());
    }

    @Test
    public void testCalculatePriceWithSpecificConsumption() {
        Energy energy = new Energy(Energy.EnergyType.Electricity);
        energy.setPricePerMeasuredUnit(0.15);
        
        LocalDateTime time1 = LocalDateTime.now();
        LocalDateTime time2 = time1.plusHours(3);
        LocalDateTime time3 = time1.plusHours(7);

        List<ObservedValue> observedValues = new ArrayList<>();
        observedValues.add(new ObservedValue(time1, 100.0));
        observedValues.add(new ObservedValue(time2, 120.0));
        observedValues.add(new ObservedValue(time3, 180.0));

        Meter realMeter = new Meter(energy, null, Meter.MeterType.CumulativeValue, observedValues);
        Meter meterSpy = Mockito.spy(realMeter);
        
        when(meterSpy.calculateConsumedElectricits()).thenReturn(80.0);
        
        double expectedPrice = (180 - 100) * 0.15;
        assertEquals(expectedPrice, meterSpy.calculatePrice());
    }
}
