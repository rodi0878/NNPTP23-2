package cz.upce.fei.nnptp.em.nnptp.energymonitor;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
public class Distribution {

    @JsonProperty("name")
    private String name;

    @JsonProperty("address")
    private String address;

    public Distribution(@JsonProperty("name") String name, @JsonProperty("address") String address) {
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
