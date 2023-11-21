/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cz.upce.fei.nnptp.em.nnptp.energymonitor.entity;

import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author pjiruse
 */
public class ObservedValueTest {

    @Test
    public void testGetNewUnitPriceIfChangedWithNoTags() {
        ObservedValue testValue = new ObservedValue(LocalDateTime.now(), 100);
        assertEquals(null, testValue.getNewUnitPriceIfChanged());
    }

    @Test
    public void testGetNewMeterStartValueIfReplacedWithNoTags() {
        ObservedValue testValue = new ObservedValue(LocalDateTime.now(), 100);
        assertEquals(null, testValue.getNewMeterStartValueIfReplaced());
    }

    @Test
    public void testGetNewUnitPriceIfChangedWithActualTags() {
        ObservedValue testValue = new ObservedValue(LocalDateTime.now(), 100);
        ObservedTagsAndFlags priceChange1 = new ObservedTagsAndFlags.UnitPriceChangedJustAfterMeasurementTag(90.0);
        ObservedTagsAndFlags priceChange2 = new ObservedTagsAndFlags.UnitPriceChangedJustAfterMeasurementTag(80.0);
        testValue.getTagsAndFlags().add(priceChange1);
        testValue.getTagsAndFlags().add(priceChange2);

        assertEquals(80, testValue.getNewUnitPriceIfChanged());
    }

    @Test
    public void testGetNewMeterStartValueIfReplacedWithActualTags() {
        ObservedValue testValue = new ObservedValue(LocalDateTime.now(), 100);
        ObservedTagsAndFlags meterChange1 = new ObservedTagsAndFlags.MeterReplacedJustAfterMeasurementTag("ID001", 50);
        ObservedTagsAndFlags meterChange2 = new ObservedTagsAndFlags.MeterReplacedJustAfterMeasurementTag("ID002", 75);
        testValue.getTagsAndFlags().add(meterChange1);
        testValue.getTagsAndFlags().add(meterChange2);

        assertEquals(75, testValue.getNewMeterStartValueIfReplaced());
    }
}
