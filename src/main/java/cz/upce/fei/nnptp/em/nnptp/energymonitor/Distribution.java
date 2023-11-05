package cz.upce.fei.nnptp.em.nnptp.energymonitor;

/**
 *
 */
public class Distribution {

    private String name;
    private String addr;

    public Distribution(String name, String addr) {
        this.name = name;
        this.addr = addr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }
    
    
    
}
