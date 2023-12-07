package cz.upce.fei.nnptp.em.nnptp.energymonitor.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ObservedValue {

    @JsonProperty("localDateTime")
    private LocalDateTime localDateTime;

    @JsonProperty("value")
    private double value;

    private List<ObservedTagsAndFlags> tagsAndFlags;

    public ObservedValue(@JsonProperty("localDateTime") LocalDateTime localDateTime, @JsonProperty("value") double value) {
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

    @JsonIgnore
    public Double getNewUnitPriceIfChanged() {
        for (int i = tagsAndFlags.size() - 1; i >= 0; i--) {
            ObservedTagsAndFlags tag = tagsAndFlags.get(i);
            if (tag instanceof ObservedTagsAndFlags.UnitPriceChangedJustAfterMeasurementTag) {
                return ((ObservedTagsAndFlags.UnitPriceChangedJustAfterMeasurementTag) tag).getNewUnitPrice();
            }
        }
        return null;
    }

    @JsonIgnore
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
