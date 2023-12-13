package cz.upce.fei.nnptp.em.nnptp.energymonitor.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 *
 */

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ObservedTagsAndFlags.MeterReplacedJustAfterMeasurementTag.class, name = "MeterReplacedJustAfterMeasurementTag"),

        @JsonSubTypes.Type(value = ObservedTagsAndFlags.UnitPriceChangedJustAfterMeasurementTag.class, name = "UnitPriceChangedJustAfterMeasurementTag") }
)
public abstract class ObservedTagsAndFlags {

    public static class MeterReplacedJustAfterMeasurementTag extends ObservedTagsAndFlags {

        @JsonProperty("newMeterIdentification")
        private String newMeterIdentification;

        @JsonProperty("meterStartValue")
        private double meterStartValue;

        public MeterReplacedJustAfterMeasurementTag(@JsonProperty("newMeterIdentification") String newMeterIdentification, @JsonProperty("meterStartValue") double meterStartValue) {
            this.newMeterIdentification = newMeterIdentification;
            this.meterStartValue = meterStartValue;
        }

        public String getNewMeterIdentification() {
            return newMeterIdentification;
        }

        public void setNewMeterIdentification(String newMeterIdentification) {
            this.newMeterIdentification = newMeterIdentification;
        }

        public double getMeterStartValue() {
            return meterStartValue;
        }

        public void setMeterStartValue(double meterStartValue) {
            this.meterStartValue = meterStartValue;
        }
        
        
    }
    
    public static class UnitPriceChangedJustAfterMeasurementTag extends ObservedTagsAndFlags {

        @JsonProperty("newUnitPrice")
        private double newUnitPrice;

        public UnitPriceChangedJustAfterMeasurementTag(@JsonProperty("newUnitPrice") double newUnitPrice) {
            this.newUnitPrice = newUnitPrice;
        }

        public double getNewUnitPrice() {
            return newUnitPrice;
        }
        
        
        
    }
    
}
