package cz.upce.fei.nnptp.em.nnptp.energymonitor.billing;

import cz.upce.fei.nnptp.em.nnptp.energymonitor.ConnectionPoint;
import cz.upce.fei.nnptp.em.nnptp.energymonitor.Distribution;
import cz.upce.fei.nnptp.em.nnptp.energymonitor.Energy;
import cz.upce.fei.nnptp.em.nnptp.energymonitor.Meter;
import cz.upce.fei.nnptp.em.nnptp.energymonitor.entity.ObservedValue;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BillingTest {
    private ConnectionPoint createConnectionPointWithMeter() {
        Energy energy = new Energy(Energy.EnergyType.ELECTRICITY);
        energy.setPricePerMeasuredUnit(3);
        Meter meter = new Meter(energy, new Distribution("Cez", "Praha"), Meter.MeterType.CUMULATIVE_VALUE,
                List.of(new ObservedValue(LocalDateTime.now().minusDays(21), 10),
                        new ObservedValue(LocalDateTime.now().minusDays(14), 210),
                        new ObservedValue(LocalDateTime.now().minusDays(7), 510)));
        return new ConnectionPoint(1, "Karlova 15", List.of(meter));
    }

    @Test
    public void calculateTotalConsumedElectricity() {
        Billing billing = new Billing(createConnectionPointWithMeter(), 30);
        billing.setLastBillingDate(LocalDateTime.now().minusDays(30));
        assertEquals(500, billing.calculateTotalConsumedElectricity());
    }

    @Test
    public void calculateTotalCost() {
        Billing billing = new Billing(createConnectionPointWithMeter(), 30);
        billing.setLastBillingDate(LocalDateTime.now().minusDays(30));
        assertEquals(1500, billing.calculateTotalCost());
    }

    @Test
    public void isOverdueWhenNotOverdue() {
        ConnectionPoint connectionPoint = createConnectionPointWithMeter();
        Billing billing = new Billing(connectionPoint, 30);
        billing.setLastBillingDate(LocalDateTime.now());
        assertFalse(billing.isOverdue());
    }

    @Test
    public void isOverdueWhenOverdue() {
        ConnectionPoint connectionPoint = createConnectionPointWithMeter();
        Billing billing = new Billing(connectionPoint, 30);
        billing.setLastBillingDate(LocalDateTime.now().minusDays(35));
        assertTrue(billing.isOverdue());
    }

    @Test
    public void remainingDaysUntilNextBillingArePositive() {
        ConnectionPoint connectionPoint = createConnectionPointWithMeter();
        Billing billing = new Billing(connectionPoint, 30);
        billing.setLastBillingDate(LocalDateTime.now());
        assertTrue(billing.remainingDaysUntilNextBilling() > 0);
    }
}
