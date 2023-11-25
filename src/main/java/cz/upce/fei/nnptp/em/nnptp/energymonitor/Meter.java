package cz.upce.fei.nnptp.em.nnptp.energymonitor;

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

    private Energy energy;
    private Distribution distribution;
    private MeterType meterType;
    private List<ObservedValue> observedValues;

    public Meter(Energy energy, Distribution distribution, MeterType meterType, List<ObservedValue> observedValues) {
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

    private boolean isObservedValuesEmpty(){
        return observedValues == null || observedValues.isEmpty();
    }

    /**
     * Calculate consumed electrics by selected MeterType.
     *
     * @return total consumed power
     */
    public double calculateConsumedElectricity() {
        if (isObservedValuesEmpty()) {
            return 0.0;
        }

        double totalConsumedPower = 0.0;

        if (meterType == MeterType.CUMULATIVE_VALUE) {
            double lastValue = observedValues.get(0).getValue();
            double lastMeterStartValue = lastValue;

            for (ObservedValue observedValue : observedValues) {
                lastValue = observedValue.getValue();
                if (observedValue.getNewMeterStartValueIfReplaced() != null) {
                    totalConsumedPower += lastValue - lastMeterStartValue;
                    lastMeterStartValue = observedValue.getNewMeterStartValueIfReplaced();
                }
            }
            totalConsumedPower += lastValue - lastMeterStartValue;
        } else if (meterType == MeterType.ACTUAL_VALUE) {
            totalConsumedPower = observedValues.stream().mapToDouble(ObservedValue::getValue).sum();
        }

        return totalConsumedPower;
    }

    public double calculateConsumedElectricity(LocalDateTime from, LocalDateTime to) {
        //Calculate consumed power from selected measurements
        double totalConsumedElectricity = 0.0;

        if (isObservedValuesEmpty()) {
            return totalConsumedElectricity;
        }

        if (meterType == MeterType.CUMULATIVE_VALUE) {
            double previousValue = 0.0;
            for (ObservedValue observedValue : observedValues) {
                if (observedValue.getLocalDateTime().compareTo(from) >= 0 && observedValue.getLocalDateTime().compareTo(to) <= 0) {
                    if (previousValue != 0.0) {
                        totalConsumedElectricity += observedValue.getValue() - previousValue;
                    }
                    previousValue = observedValue.getValue();
                    if (observedValue.getNewMeterStartValueIfReplaced() != null) {
                        previousValue = observedValue.getNewMeterStartValueIfReplaced();
                    }
                }
            }
        } else if (meterType == MeterType.ACTUAL_VALUE) {
            for (ObservedValue observedValue : observedValues) {
                if (observedValue.getLocalDateTime().compareTo(from) >= 0 && observedValue.getLocalDateTime().compareTo(to) <= 0) {
                    totalConsumedElectricity += observedValue.getValue();
                }
            }
        }
        return totalConsumedElectricity;
    }

    public double calculatePrice() {
        return calculatePrice(null, null);
    }

    public double calculatePrice(LocalDateTime from, LocalDateTime to) {
        double totalPrice = 0.0;
        double currentUnitPrice = energy.getPricePerMeasuredUnit();
        double lastValue = 0.0;

        if (isObservedValuesEmpty()) {
            return 0.0;
        }

        List<ObservedValue> observedValuesInTimeInterval = getObservedValuesInTimeInterval(from, to);

        for (int i = 0; i < observedValuesInTimeInterval.size(); i++) {
            ObservedValue observedValue = observedValuesInTimeInterval.get(i);

            double consumedElectricity = 0.0;
            if (meterType == MeterType.CUMULATIVE_VALUE) {
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
        List<ObservedValue> observedValuesInTimeInterval;
        if (from == null && to == null) {
            observedValuesInTimeInterval = observedValues;
        } else {
            observedValuesInTimeInterval = observedValues.stream()
                    .filter((observedValue) -> isValueInTimeInterval(from, to, observedValue))
                    .toList();
        }
        return observedValuesInTimeInterval;
    }

    private boolean isValueInTimeInterval(LocalDateTime from, LocalDateTime to, ObservedValue observedValue) {
        if (from == null && to == null) {
            return true;
        } else if (to == null) {
            return observedValue.getLocalDateTime().isAfter(from) || observedValue.getLocalDateTime().isEqual(from);
        } else if (from == null) {
            return observedValue.getLocalDateTime().isBefore(to) || observedValue.getLocalDateTime().isEqual(to);
        } else {
            return (observedValue.getLocalDateTime().isAfter(from) || observedValue.getLocalDateTime().isEqual(from)) &&
                    (observedValue.getLocalDateTime().isBefore(to) || observedValue.getLocalDateTime().isEqual(to));
        }
    }

}
