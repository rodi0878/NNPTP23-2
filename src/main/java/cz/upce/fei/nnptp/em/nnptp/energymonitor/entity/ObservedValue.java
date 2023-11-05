package cz.upce.fei.nnptp.em.nnptp.energymonitor.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ObservedValue {

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

    
    
    
}
