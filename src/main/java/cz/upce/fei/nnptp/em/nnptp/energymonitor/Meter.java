package cz.upce.fei.nnptp.em.nnptp.energymonitor;

import cz.upce.fei.nnptp.em.nnptp.energymonitor.entity.ObservedTagsAndFlags;
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
        return 0.0;
    }

    public double calculateConsumedElectricits(LocalDateTime from, LocalDateTime to) {
        // TODO calculate consumed power from selected measurements
        throw new RuntimeException();
    }

    public double calculatePrice() {
        double totalPrice = 0.0;
        double currentUnitPrice = energy.getPricePerMeasuredUnit();
        double lastValue = 0.0;

        if (getObservedValues() == null || getObservedValues().isEmpty()) {
            return 0.0;
        }

        for (int i = 0; i < getObservedValues().size(); i++) {
            ObservedValue observedValue = getObservedValues().get(i);

            for (ObservedTagsAndFlags tag : observedValue.getTagsAndFlags()) {
                if (tag instanceof ObservedTagsAndFlags.UnitPriceChangedJustAfterMeasurementTag) {
                    ObservedTagsAndFlags.UnitPriceChangedJustAfterMeasurementTag priceChangeTag
                            = (ObservedTagsAndFlags.UnitPriceChangedJustAfterMeasurementTag) tag;
                    currentUnitPrice = priceChangeTag.getNewUnitPrice();
                }

                if (tag instanceof ObservedTagsAndFlags.MeterReplacedJustAfterMeasurementTag) {
                    ObservedTagsAndFlags.MeterReplacedJustAfterMeasurementTag meterReplacementTag
                            = (ObservedTagsAndFlags.MeterReplacedJustAfterMeasurementTag) tag;
                    lastValue = meterReplacementTag.getMeterStartValue();
                }
            }

            double consumedElectricity;
            if (meterType == MeterType.CumulativeValue) {
                if (i == 0) {
                    lastValue = observedValue.getValue();
                    continue;
                }
                consumedElectricity = observedValue.getValue() - lastValue;
                lastValue = observedValue.getValue();
            } else {
                consumedElectricity = observedValue.getValue();
            }

            totalPrice += consumedElectricity * currentUnitPrice;
        }

        return totalPrice;
    }

    public double calculatePrice(LocalDateTime from, LocalDateTime to) {
        // TODO
        throw new RuntimeException();

    }

}
