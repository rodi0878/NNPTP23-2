package cz.upce.fei.nnptp.em.nnptp.energymonitor.entity;

/**
 *
 */
public abstract class ObservedTagsAndFlags {

    public static class MeterReplacedJustAfterMeasurementTag extends ObservedTagsAndFlags {

        private String newMeterIdentification;
        private double meterStartValue;

        public MeterReplacedJustAfterMeasurementTag(String newMeterIdentification, double meterStartValue) {
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
    
    private static class UnitPriceChangedJustAfterMeasurementTag extends ObservedTagsAndFlags {
        private double newUnitPrice;

        public UnitPriceChangedJustAfterMeasurementTag(double newUnitPrice) {
            this.newUnitPrice = newUnitPrice;
        }

        public double getNewUnitPrice() {
            return newUnitPrice;
        }
        
        
        
    }
    
}
