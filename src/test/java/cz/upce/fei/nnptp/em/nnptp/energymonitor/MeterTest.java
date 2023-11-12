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
    public void testCalculateConsumedElectricitsActual() {
        List<ObservedValue> list = new ArrayList<>();
        LocalDateTime time = LocalDateTime.now();
        list.add(new ObservedValue(time.minusDays(4),5.0));
        list.add(new ObservedValue(time.minusDays(2),2.6));
        list.add(new ObservedValue(time,5.5));
        list.add(new ObservedValue(time.plusDays(1),12.0));
        list.add(new ObservedValue(time.plusDays(4),9.0));
        list.add(new ObservedValue(time.plusDays(9),22.6));
        list.add(new ObservedValue(time.plusMonths(2),56.8));      
        Meter meter = new Meter(null, new Distribution("Cez", "Praha"), Meter.MeterType.ActualValue, list);
        
        double totalConsumedPower = meter.calculateConsumedElectricits(time.minusDays(3), time.plusDays(5));
        assertEquals(29.1, totalConsumedPower);       
    }
    
    @Test
    public void testCalculateConsumedElectricitsCumulative() {
        List<ObservedValue> list = new ArrayList<>();
        LocalDateTime time = LocalDateTime.now();
        list.add(new ObservedValue(time.minusDays(4),1.3));
        list.add(new ObservedValue(time.minusDays(2),2.6));
        list.add(new ObservedValue(time,5.5));
        list.add(new ObservedValue(time.plusDays(1),12.0));
        list.add(new ObservedValue(time.plusDays(4),18.6));
        list.add(new ObservedValue(time.plusDays(9),22.6));
        list.add(new ObservedValue(time.plusMonths(2),33.6));   
        Meter meter = new Meter(null, new Distribution("Cez", "Praha"), Meter.MeterType.CumulativeValue, list);
        
        double totalConsumedPower = meter.calculateConsumedElectricits(time.minusDays(3), time.plusDays(5));
        assertEquals(16.0, totalConsumedPower);       
    }
    
    @Test
    public void testCalculateConsumedElectricityEmpty() {
        List<ObservedValue> list = new ArrayList<>();
        LocalDateTime time = LocalDateTime.now();
        Meter meter = new Meter(null, new Distribution("Cez", "Praha"), Meter.MeterType.ActualValue, list);

        double totalConsumedPower = meter.calculateConsumedElectricits(time.minusDays(3), time.plusDays(5));
        assertEquals(0.0, totalConsumedPower);
    }
    
    @Test
    public void testCalculateConsumedElectricityNull() {
        LocalDateTime time = LocalDateTime.now();
        Meter meter = new Meter(null, new Distribution("Cez", "Praha"), Meter.MeterType.ActualValue, null);
        double totalConsumedPower = meter.calculateConsumedElectricits(time.minusDays(3), time.plusDays(5));
        assertEquals(0.0, totalConsumedPower);
    }
    
        @Test
    public void testCalculateConsumedElectricitsCumulativeChangedMeter() {
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
        Meter meter = new Meter(null, new Distribution("Cez", "Praha"), Meter.MeterType.CumulativeValue, list);
        
        double totalConsumedPower = meter.calculateConsumedElectricits(time.minusDays(3), time.plusDays(10));
        assertEquals(40.6, totalConsumedPower);       
    }
    
}
