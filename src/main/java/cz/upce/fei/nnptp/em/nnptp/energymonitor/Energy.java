package cz.upce.fei.nnptp.em.nnptp.energymonitor;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
public class Energy {

    public static enum EnergyType {
        ELECTRICITY,
        GAS,
        COLD_WATER,
        HOT_WATER,
        CENTRAL_HEATING
    }

    @JsonProperty("name")
    private String name;

    @JsonProperty("energyType")
    private EnergyType energyType;

    @JsonProperty("pricePerMeasuredUnit")
    private double pricePerMeasuredUnit; // TODO move to initial price tag to observed values ?

    public Energy(@JsonProperty("energyType") EnergyType energyType) {
        this.energyType = energyType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
