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
    
    public Meter(Energy energy, Distribution distribution, MeterType meterType, List<ObservedValue> obVals) {
        this.energy = energy;
        this.distribution = distribution;
        this.meterType = meterType;
        this.obVals = obVals;
    }

    private Energy energy;
    private Distribution distribution;
    private MeterType meterType;
    
    private List<ObservedValue> obVals;

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

    public List<ObservedValue> getObVals() {
        return obVals;
    }

    public void setObVals(List<ObservedValue> obVals) {
        this.obVals = obVals;
    }
    
    public double calculateConsumedElectricits() {
        // TODO calculate consumed power from all measurements
        // according to metertype and observedvalues
        throw new RuntimeException();
    }
    
    public double calculateConsumedElectricits(LocalDateTime from, LocalDateTime to) {
       //Calculate consumed power from selected measurements
        double totalConsumedElectricits = 0.0;
        
        if (obVals == null || obVals.isEmpty()) {
            return totalConsumedElectricits;
        }
        
        if (meterType == MeterType.CumulativeValue) {
            double previousValue = 0.0;
            for (ObservedValue observedValue : obVals) {
                if(observedValue.getLocalDateTime().compareTo(from) >= 0 && observedValue.getLocalDateTime().compareTo(to) <= 0){
                    if(previousValue != 0.0){
                        totalConsumedElectricits += observedValue.getValue() - previousValue;
                    }                   
                    previousValue = observedValue.getValue();
                }
            }
        }else if (meterType == MeterType.ActualValue) {
            for (ObservedValue observedValue : obVals) {
                if(observedValue.getLocalDateTime().compareTo(from) >= 0 && observedValue.getLocalDateTime().compareTo(to) <= 0){
                    totalConsumedElectricits += observedValue.getValue();
                }
            }
        }
        return totalConsumedElectricits;
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
