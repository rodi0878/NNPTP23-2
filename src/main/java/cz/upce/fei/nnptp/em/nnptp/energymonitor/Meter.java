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

    /**
     * Calculate consumed electrics by selected MeterType.
     * @return total consumed power
     */
    public double calculateConsumedElectricits() {
        if (obVals == null || obVals.isEmpty()) {
            return 0.0;
        }

        double totalConsumedPower = 0.0;

        if (meterType == MeterType.CumulativeValue) {
            ObservedValue first = obVals.get(0);
            ObservedValue last = obVals.get(obVals.size() - 1);
            totalConsumedPower = last.getValue() - first.getValue();
        } else if (meterType == MeterType.ActualValue) {
            for (ObservedValue observedValue : obVals) {
                totalConsumedPower += observedValue.getValue();
            }
        }

        return totalConsumedPower;
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
