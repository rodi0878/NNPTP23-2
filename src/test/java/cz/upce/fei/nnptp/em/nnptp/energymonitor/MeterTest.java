package cz.upce.fei.nnptp.em.nnptp.energymonitor;

import cz.upce.fei.nnptp.em.nnptp.energymonitor.entity.ObservedTagsAndFlags;
import cz.upce.fei.nnptp.em.nnptp.energymonitor.entity.ObservedValue;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 */
public class MeterTest {
    
    public MeterTest() {
    }
    
    @Test
    public void testCalculateConsumedElectricityInIntervalActual() {
        List<ObservedValue> list = new ArrayList<>();
        LocalDateTime time = LocalDateTime.now();
        list.add(new ObservedValue(time.minusDays(4),5.0));
        list.add(new ObservedValue(time.minusDays(2),2.6));
        list.add(new ObservedValue(time,5.5));
        list.add(new ObservedValue(time.plusDays(1),12.0));
        list.add(new ObservedValue(time.plusDays(4),9.0));
        list.add(new ObservedValue(time.plusDays(9),22.6));
        list.add(new ObservedValue(time.plusMonths(2),56.8));      
        Meter meter = new Meter(null, new Distribution("Cez", "Praha"), Meter.MeterType.ACTUAL_VALUE, list);
        
        double totalConsumedPower = meter.calculateConsumedElectricity(time.minusDays(3), time.plusDays(5));
        assertEquals(29.1, totalConsumedPower);       
    }
    
    @Test
    public void testCalculateConsumedElectricityInIntervalCumulative() {
        List<ObservedValue> list = new ArrayList<>();
        LocalDateTime time = LocalDateTime.now();
        list.add(new ObservedValue(time.minusDays(4),1.3));
        list.add(new ObservedValue(time.minusDays(2),2.6));
        list.add(new ObservedValue(time,5.5));
        list.add(new ObservedValue(time.plusDays(1),12.0));
        list.add(new ObservedValue(time.plusDays(4),18.6));
        list.add(new ObservedValue(time.plusDays(9),22.6));
        list.add(new ObservedValue(time.plusMonths(2),33.6));   
        Meter meter = new Meter(null, new Distribution("Cez", "Praha"), Meter.MeterType.CUMULATIVE_VALUE, list);
        
        double totalConsumedPower = meter.calculateConsumedElectricity(time.minusDays(3), time.plusDays(5));
        assertEquals(16.0, totalConsumedPower);       
    }
    
    @Test
    public void testCalculateConsumedElectricityInIntervalEmpty() {
        List<ObservedValue> list = new ArrayList<>();
        LocalDateTime time = LocalDateTime.now();
        Meter meter = new Meter(null, new Distribution("Cez", "Praha"), Meter.MeterType.ACTUAL_VALUE, list);

        double totalConsumedPower = meter.calculateConsumedElectricity(time.minusDays(3), time.plusDays(5));
        assertEquals(0.0, totalConsumedPower);
    }
    
    @Test
    public void testCalculateConsumedElectricityInIntervalNull() {
        LocalDateTime time = LocalDateTime.now();
        Meter meter = new Meter(null, new Distribution("Cez", "Praha"), Meter.MeterType.ACTUAL_VALUE, null);
        double totalConsumedPower = meter.calculateConsumedElectricity(time.minusDays(3), time.plusDays(5));
        assertEquals(0.0, totalConsumedPower);
    }
    
        @Test
    public void testCalculateConsumedElectricityInIntervalCumulativeChangedMeter() {
        List<ObservedValue> list = new ArrayList<>();
        LocalDateTime time = LocalDateTime.now();
        list.add(new ObservedValue(time.minusDays(4),1.3));
        list.add(new ObservedValue(time.minusDays(2),2.6));
        list.add(new ObservedValue(time,5.5));
        
        ObservedValue observedWithFirstNewMeter = new ObservedValue(time.plusDays(1),12.0);
        observedWithFirstNewMeter.getTagsAndFlags().add(new ObservedTagsAndFlags.MeterReplacedJustAfterMeasurementTag(
                "T1", 40.0));
        list.add(observedWithFirstNewMeter);
                
        list.add(new ObservedValue(time.plusDays(1),52.0));
        list.add(new ObservedValue(time.plusDays(4),58.6));
        
        ObservedValue observedWithSecondNewMeter = new ObservedValue(time.plusDays(9),62.6);
        observedWithSecondNewMeter.getTagsAndFlags().add(new ObservedTagsAndFlags.MeterReplacedJustAfterMeasurementTag(
                "T2", 20.0));
        list.add(observedWithSecondNewMeter);
        list.add(new ObservedValue(time.plusDays(10),28.6));
        list.add(new ObservedValue(time.plusMonths(2),33.6));   
        Meter meter = new Meter(null, new Distribution("Cez", "Praha"), Meter.MeterType.CUMULATIVE_VALUE, list);
        
        double totalConsumedPower = meter.calculateConsumedElectricity(time.minusDays(3), time.plusDays(10));
        assertEquals(40.6, totalConsumedPower);       
    }

    @Test
    public void testCalculateConsumedElectricityNull() {
        Meter meter = new Meter(null, null, Meter.MeterType.CUMULATIVE_VALUE, null);

        double consumedPower = meter.calculateConsumedElectricity();
        assertEquals(0, consumedPower);
    }

    @Test
    public void testCalculateConsumedElectricityEmpty() {
        List<ObservedValue> observedValues = new ArrayList<>();
        Meter meter = new Meter(null, null, Meter.MeterType.CUMULATIVE_VALUE, observedValues);

        double consumedPower = meter.calculateConsumedElectricity();
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
        Meter meter = new Meter(null, null, Meter.MeterType.CUMULATIVE_VALUE, observedValues);
        
        double consumedPower = meter.calculateConsumedElectricity();
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

        Meter meter = new Meter(null, null, Meter.MeterType.CUMULATIVE_VALUE, observedValues);
        double consumedPower = meter.calculateConsumedElectricity();

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


        Meter meter = new Meter(null, null, Meter.MeterType.CUMULATIVE_VALUE, observedValues);
        double consumedPower = meter.calculateConsumedElectricity();

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
        Meter meter = new Meter(null, null, Meter.MeterType.ACTUAL_VALUE,observedValues);

        double consumedPower = meter.calculateConsumedElectricity();
        assertEquals(60.0, consumedPower);
    }
    
    @Test
    public void testCalculatePriceWithCumulativeMeter() {
        Energy energy = new Energy(Energy.EnergyType.ELECTRICITY);
        energy.setPricePerMeasuredUnit(10.0);

        List<ObservedValue> observedValues = new ArrayList<>();
        observedValues.add(new ObservedValue(LocalDateTime.now(), 100.0));
        observedValues.add(new ObservedValue(LocalDateTime.now().plusHours(1), 120.0));
        observedValues.add(new ObservedValue(LocalDateTime.now().plusHours(2), 180.0));

        Meter meter = new Meter(energy, null, Meter.MeterType.CUMULATIVE_VALUE, observedValues);
        
        double expectedPrice = 800.0;
        assertEquals(expectedPrice, meter.calculatePrice());
    }

    @Test
    public void testCalculatePriceWithActualMeter() {
        Energy energy = new Energy(Energy.EnergyType.GAS);
        energy.setPricePerMeasuredUnit(20.0);

        List<ObservedValue> observedValues = new ArrayList<>();
        observedValues.add(new ObservedValue(LocalDateTime.now(), 50.0));
        observedValues.add(new ObservedValue(LocalDateTime.now().plusHours(1), 80.0));
        observedValues.add(new ObservedValue(LocalDateTime.now().plusHours(2), 100.0));

        Meter meter = new Meter(energy, null, Meter.MeterType.ACTUAL_VALUE, observedValues);

        double expectedPrice = 4600.0;
        assertEquals(expectedPrice, meter.calculatePrice());
    }

    @Test
    public void testCalculatePriceWithPriceChange() {
        Energy energy = new Energy(Energy.EnergyType.HOT_WATER);
        energy.setPricePerMeasuredUnit(5.0);
        
        List<ObservedValue> observedValues = new ArrayList<>();
        ObservedValue value1 = new ObservedValue(LocalDateTime.now(), 200);      
        ObservedValue value2 = new ObservedValue(LocalDateTime.now().plusHours(5), 300);
        ObservedValue value3 = new ObservedValue(LocalDateTime.now().plusHours(8), 350);

        ObservedTagsAndFlags priceChange = new ObservedTagsAndFlags.UnitPriceChangedJustAfterMeasurementTag(10.0);
        value2.getTagsAndFlags().add(priceChange);

        observedValues.add(value1);
        observedValues.add(value2);
        observedValues.add(value3);

        Meter meter = new Meter(energy, null, Meter.MeterType.CUMULATIVE_VALUE, observedValues);
        
        double expectedPrice = 1000.0;
        assertEquals(expectedPrice, meter.calculatePrice());
    }
    
    @Test
    public void testCalculatePriceWithPriceAndMeterChange() {
        Energy energy = new Energy(Energy.EnergyType.HOT_WATER);
        energy.setPricePerMeasuredUnit(5.0);
        
        List<ObservedValue> observedValues = new ArrayList<>();
        ObservedValue value1 = new ObservedValue(LocalDateTime.now(), 200);      
        ObservedValue value2 = new ObservedValue(LocalDateTime.now().plusHours(5), 300);
        ObservedValue value3 = new ObservedValue(LocalDateTime.now().plusHours(8), 150);

        ObservedTagsAndFlags priceChange = new ObservedTagsAndFlags.UnitPriceChangedJustAfterMeasurementTag(10.0);
        ObservedTagsAndFlags meterChange = new ObservedTagsAndFlags.MeterReplacedJustAfterMeasurementTag("ID007", 50);
        value2.getTagsAndFlags().add(priceChange);
        value2.getTagsAndFlags().add(meterChange);

        observedValues.add(value1);
        observedValues.add(value2);
        observedValues.add(value3);

        Meter meter = new Meter(energy, null, Meter.MeterType.CUMULATIVE_VALUE, observedValues);
        
        double expectedPrice = 1500.0;
        assertEquals(expectedPrice, meter.calculatePrice());
    }
    
        public void testAddToTAgsAndFlags() {
            ObservedValue observedValue = new ObservedValue(LocalDateTime.now(), 50.0);
            observedValue.addTagsAndFlags(new ObservedTagsAndFlags.MeterReplacedJustAfterMeasurementTag(
                "newMeterId2", 10.0));
            observedValue.addTagsAndFlags(new ObservedTagsAndFlags.UnitPriceChangedJustAfterMeasurementTag(10.0));
        
        double listSize = observedValue.getTagsAndFlags().size();
        assertEquals(2, listSize);
    }
}
