package cz.upce.fei.nnptp.em.nnptp.energymonitor.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ObservedValue implements Serializable {

    private LocalDateTime localDateTime;
    private double value;

    private List<ObservedTagsAndFlags> tagsAndFlags;

    public ObservedValue(LocalDateTime localDateTime, double value) {
        this.localDateTime = localDateTime;
        this.value = value;
        this.tagsAndFlags = new ArrayList<>();
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public List<ObservedTagsAndFlags> getTagsAndFlags() {
        return tagsAndFlags;
    }

    public Double getNewUnitPriceIfChanged() {
        for (int i = tagsAndFlags.size() - 1; i >= 0; i--) {
            ObservedTagsAndFlags tag = tagsAndFlags.get(i);
            if (tag instanceof ObservedTagsAndFlags.UnitPriceChangedJustAfterMeasurementTag) {
                return ((ObservedTagsAndFlags.UnitPriceChangedJustAfterMeasurementTag) tag).getNewUnitPrice();
            }
        }
        return null;
    }

    public Double getNewMeterStartValueIfReplaced() {
        for (int i = tagsAndFlags.size() - 1; i >= 0; i--) {
            ObservedTagsAndFlags tag = tagsAndFlags.get(i);
            if (tag instanceof ObservedTagsAndFlags.MeterReplacedJustAfterMeasurementTag) {
                return ((ObservedTagsAndFlags.MeterReplacedJustAfterMeasurementTag) tag).getMeterStartValue();
            }
        }
        return null;
    }

    public void addTagsAndFlags(ObservedTagsAndFlags tf) {
        if (!tagsAndFlags.contains(tf)) {
            tagsAndFlags.add(tf);
        }
    }

}
