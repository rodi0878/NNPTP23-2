package cz.upce.fei.nnptp.em.nnptp.energymonitor;

import cz.upce.fei.nnptp.em.nnptp.energymonitor.entity.ObservedValue;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 */
public class Meter {
    
    public static enum MeterType {
        CumulativeValue,
        ActualValue
    }
    
    public Meter(Energy energy, Distribution distribution, MeterType meterType, List<ObservedValue> observedValues) {
        this.energy = energy;
        this.distribution = distribution;
        this.meterType = meterType;
        this.observedValues = observedValues;
    }

    private Energy energy;
    private Distribution distribution;
    private MeterType meterType;
    
    private List<ObservedValue> observedValues;

    public Energy getEnergy() {
        return energy;
    }

    public void setEnergy(Energy energy) {
        this.energy = energy;
    }

    public Distribution getDistribution() {
        return distribution;
    }

    public void setDistribution(Distribution distribution) {
        this.distribution = distribution;
    }

    public MeterType getMeterType() {
        return meterType;
    }

    public void setMeterType(MeterType meterType) {
        this.meterType = meterType;
    }

    public List<ObservedValue> getObservedValues() {
        return observedValues;
    }

    public void setObservedValues(List<ObservedValue> observedValues) {
        this.observedValues = observedValues;
    }
    
    public double calculateConsumedElectricits() {
        // TODO calculate consumed power from all measurements
        // according to metertype and observedvalues
        throw new RuntimeException();
    }
    
    public double calculateConsumedElectricits(LocalDateTime from, LocalDateTime to) {
                // TODO calculate consumed power from selected measurements
        throw new RuntimeException();
    }
    
    public double calculatePrice() {
        // TODO ...
        throw new  RuntimeException();
    }
    
    public double calculatePrice(LocalDateTime from, LocalDateTime to) {
        // TODO
        throw new RuntimeException();
        
    }

    
}
