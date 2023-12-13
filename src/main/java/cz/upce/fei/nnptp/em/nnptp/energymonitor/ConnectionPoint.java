package cz.upce.fei.nnptp.em.nnptp.energymonitor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Connection Point - house/flat/land
 */
public class ConnectionPoint {

    @JsonProperty("id")
    private int id;

    @JsonProperty("address")
    private String address;

    @JsonProperty("meters")
    private List<Meter> meters;

    public ConnectionPoint(@JsonProperty("id") int id, @JsonProperty("address") String address, @JsonProperty("meters") List<Meter> meters) {
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
