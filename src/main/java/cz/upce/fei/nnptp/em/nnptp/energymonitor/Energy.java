package cz.upce.fei.nnptp.em.nnptp.energymonitor;

import cz.upce.fei.nnptp.em.nnptp.energymonitor.enums.EnergyType;

/**
 *
 */
public class Energy {
    
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    private EnergyType enetEnergyType;
    private double pricePerMeasuredUnit; // TODO move to initial price tag to observed values ?

    public Energy(EnergyType enetEnergyType) {
        this.enetEnergyType = enetEnergyType;
    }

    public EnergyType getEnetEnergyType() {
        return enetEnergyType;
    }

    public void setEnetEnergyType(EnergyType enetEnergyType) {
        this.enetEnergyType = enetEnergyType;
    }

    public double getPricePerMeasuredUnit() {
        return pricePerMeasuredUnit;
    }

    public void setPricePerMeasuredUnit(double pricePerMeasuredUnit) {
        this.pricePerMeasuredUnit = pricePerMeasuredUnit;
    }
    
    
    
    
}
