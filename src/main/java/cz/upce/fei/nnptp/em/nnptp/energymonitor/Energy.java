package cz.upce.fei.nnptp.em.nnptp.energymonitor;

import java.io.Serializable;

/**
 *
 */
public class Energy implements Serializable {

    public static enum EnergyType {
        ELECTRICITY,
        GAS,
        COLD_WATER,
        HOT_WATER,
        CENTRAL_HEATING
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    private EnergyType energyType;
    private double pricePerMeasuredUnit; // TODO move to initial price tag to observed values ?

    public Energy(EnergyType energyType) {
        this.energyType = energyType;
    }

    public EnergyType getEnergyType() {
        return energyType;
    }

    public void setEnergyType(EnergyType energyType) {
        this.energyType = energyType;
    }

    public double getPricePerMeasuredUnit() {
        return pricePerMeasuredUnit;
    }

    public void setPricePerMeasuredUnit(double pricePerMeasuredUnit) {
        this.pricePerMeasuredUnit = pricePerMeasuredUnit;
    }
    
    
    
    
}
