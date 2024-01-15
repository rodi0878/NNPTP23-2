package cz.upce.fei.nnptp.em.nnptp.energymonitor;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.upce.fei.nnptp.em.nnptp.energymonitor.entity.ObservedValue;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 */
public class Meter {

    public static enum MeterType {
        CUMULATIVE_VALUE,
        ACTUAL_VALUE
    }

    @JsonProperty("energy")
    private Energy energy;

    @JsonProperty("distribution")
    private Distribution distribution;

    @JsonProperty("meterType")
    private MeterType meterType;

    @JsonProperty("observedValues")
    private List<ObservedValue> observedValues;

    public Meter(@JsonProperty("energy") Energy energy, @JsonProperty("distribution") Distribution distribution, @JsonProperty("meterType") MeterType meterType, @JsonProperty("observedValues") List<ObservedValue> observedValues) {
        this.energy = energy;
        this.distribution = distribution;
        this.meterType = meterType;
        this.observedValues = observedValues;
    }


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

    private boolean isObservedValuesEmpty() {
        return observedValues == null || observedValues.isEmpty();
    }

    /**
     * Calculate consumed power by selected MeterType.
     *
     * @return total consumed power
     */
    public double calculateConsumedPower() {
        return calculateConsumedPower(null, null);
    }

    public double calculateConsumedPower(LocalDateTime from, LocalDateTime to) {
        //Calculate consumed power from selected measurements
        if (isObservedValuesEmpty()) {
            return 0.0;
        }

        double totalConsumedPower = 0.0;
        double previousValue = 0.0;
        List<ObservedValue> observedValuesInTimeInterval = getObservedValuesInTimeInterval(from, to);

        for (int i = 0; i < observedValuesInTimeInterval.size(); i++) {
            ObservedValue observedValue = observedValuesInTimeInterval.get(i);

            if (meterType == MeterType.CUMULATIVE_VALUE) {
                if (previousValue != 0.0) {
                    totalConsumedPower += observedValue.getValue() - previousValue;
                }
                previousValue = observedValue.getValue();
                if (observedValue.getNewMeterStartValueIfReplaced() != null) {
                    previousValue = observedValue.getNewMeterStartValueIfReplaced();
                }
            } else {
                totalConsumedPower += observedValue.getValue();
            }
        }

        return totalConsumedPower;
    }

    public double calculatePrice() {
        return calculatePrice(null, null);
    }

    public double calculatePrice(LocalDateTime from, LocalDateTime to) {
        if (isObservedValuesEmpty()) {
            return 0.0;
        }

        double currentUnitPrice = energy.getPricePerMeasuredUnit();
        double totalPrice = 0.0;
        double lastValue = 0.0;

        List<ObservedValue> observedValuesInTimeInterval = getObservedValuesInTimeInterval(from, to);

        for (int i = 0; i < observedValuesInTimeInterval.size(); i++) {
            ObservedValue observedValue = observedValuesInTimeInterval.get(i);
            double consumedElectricity = observedValue.getValue();

            if (meterType == MeterType.CUMULATIVE_VALUE) {
                if (i == 0) {
                    lastValue = observedValue.getValue();
                    continue;
                }
                consumedElectricity = observedValue.getValue() - lastValue;
                lastValue = observedValue.getValue();
            }

            totalPrice += consumedElectricity * currentUnitPrice;

            if (observedValue.getNewUnitPriceIfChanged() != null) {
                currentUnitPrice = observedValue.getNewUnitPriceIfChanged();
            }
            if (observedValue.getNewMeterStartValueIfReplaced() != null) {
                lastValue = observedValue.getNewMeterStartValueIfReplaced();
            }
        }
        return totalPrice;
    }

    private List<ObservedValue> getObservedValuesInTimeInterval(LocalDateTime from, LocalDateTime to) {
        return observedValues.stream()
                .filter(observedValue -> isValueInTimeInterval(from, to, observedValue))
                .toList();
    }

    private boolean isValueInTimeInterval(LocalDateTime from, LocalDateTime to, ObservedValue observedValue) {
        LocalDateTime valueDateTime = observedValue.getLocalDateTime();
        boolean isFromAfterOrEqual = from == null || valueDateTime.isEqual(from) || valueDateTime.isAfter(from);
        boolean isToBeforeOrEqual = to == null || valueDateTime.isEqual(to) || valueDateTime.isBefore(to);
        return isFromAfterOrEqual && isToBeforeOrEqual;
    }

}
