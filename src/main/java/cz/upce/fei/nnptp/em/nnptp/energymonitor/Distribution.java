package cz.upce.fei.nnptp.em.nnptp.energymonitor;

/**
 *
 */
public class Distribution {

    private String name;
    private String address;

    public Distribution(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
}
