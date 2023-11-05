package cz.upce.fei.nnptp.em.nnptp.energymonitor;

import java.util.List;

/**
 * Connection Point - house/flat/land
 */
public class ConnectionPoint {

    private int id;
    private String addr;
    
    private List<Meter> meters;

    public ConnectionPoint(int id, String addr, List<Meter> meters) {
        this.id = id;
        this.addr = addr;
        this.meters = meters;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public List<Meter> getMeters() {
        return meters;
    }

    public void setMeters(List<Meter> meters) {
        this.meters = meters;
    }
    
    
    
}
