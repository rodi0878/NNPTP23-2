/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cz.upce.fei.nnptp.em.nnptp.energymonitor;

import cz.upce.fei.nnptp.em.nnptp.energymonitor.entity.ObservedTagsAndFlags;
import cz.upce.fei.nnptp.em.nnptp.energymonitor.entity.ObservedValue;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author agedo
 */
public class ObservedValueTagsAndFlagsTest {
    
    public ObservedValueTagsAndFlagsTest(){
    }
    
    ObservedValue observedValue;
    
    @BeforeEach
    public void setUp(){
        observedValue = new ObservedValue(LocalDateTime.now(), 50.0);
    }
    
    @Test
    public void testAddToTagsAndFlags() {
        observedValue.addTagsAndFlags(new ObservedTagsAndFlags.MeterReplacedJustAfterMeasurementTag(
                "M2", 10.0));
        observedValue.addTagsAndFlags(new ObservedTagsAndFlags.UnitPriceChangedJustAfterMeasurementTag(10.0));

        double listSize = observedValue.getTagsAndFlags().size();
        assertEquals(2, listSize);
    }
    
    @Test
    public void testGetNewUnitPriceIfChanged() {
        observedValue.addTagsAndFlags(new ObservedTagsAndFlags.UnitPriceChangedJustAfterMeasurementTag(20.0));

        double actualNewPrice = observedValue.getNewUnitPriceIfChanged();
        assertEquals(20, actualNewPrice);
    }
    
    @Test
    public void testGetNewMeterStartValueIfReplaced() {
        observedValue.addTagsAndFlags(new ObservedTagsAndFlags.MeterReplacedJustAfterMeasurementTag(
                "M2", 25.0));

        double actualStartValue = observedValue.getNewMeterStartValueIfReplaced();
        assertEquals(25, actualStartValue);
    }
}
