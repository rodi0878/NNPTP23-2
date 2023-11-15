package cz.upce.fei.nnptp.em.nnptp.energymonitor;

/**
 *
 */
public class Energy {

    public static enum EnergyType {
        Electricity,
        Gas,
        ColdWater,
        HotWater,
        CentralHeating
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
