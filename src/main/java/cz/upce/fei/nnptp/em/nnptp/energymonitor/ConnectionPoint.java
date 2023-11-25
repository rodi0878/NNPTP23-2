package cz.upce.fei.nnptp.em.nnptp.energymonitor;

import java.io.Serializable;
import java.util.List;

/**
 * Connection Point - house/flat/land
 */
public class ConnectionPoint implements Serializable {

    private int id;
    private String address;
    
    private List<Meter> meters;

    public ConnectionPoint(int id, String address, List<Meter> meters) {
        this.id = id;
        this.address = address;
        this.meters = meters;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Meter> getMeters() {
        return meters;
    }

    public void setMeters(List<Meter> meters) {
        this.meters = meters;
    }
    
    
    
}
