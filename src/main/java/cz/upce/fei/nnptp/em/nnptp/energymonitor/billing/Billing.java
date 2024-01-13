package cz.upce.fei.nnptp.em.nnptp.energymonitor.billing;

import cz.upce.fei.nnptp.em.nnptp.energymonitor.ConnectionPoint;
import cz.upce.fei.nnptp.em.nnptp.energymonitor.Meter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class Billing {

    private final ConnectionPoint connectionPoint;
    private LocalDateTime lastBillingDate;
    private int billingIntervalInDays;

    public Billing(ConnectionPoint connectionPoint, int billingIntervalInDays) {
        this.connectionPoint = connectionPoint;
        this.lastBillingDate = LocalDateTime.now();
        this.billingIntervalInDays = billingIntervalInDays;
    }

    public void setLastBillingDate(LocalDateTime billingInterval) {
        this.lastBillingDate = billingInterval;
    }

    public void setBillingIntervalInDays(int billingIntervalInDays) {
        this.billingIntervalInDays = billingIntervalInDays;
    }

    public double calculateTotalConsumedElectricity() {
        double totalConsumedElectricity = 0;
        List<Meter> meters = connectionPoint.getMeters();

        for (Meter meter : meters) {
            totalConsumedElectricity += meter.calculateConsumedElectricity(lastBillingDate, LocalDateTime.now());
        }

        return totalConsumedElectricity;
    }

    public double calculateTotalCost() {
        double totalPrice = 0;
        List<Meter> meters = connectionPoint.getMeters();

        for (Meter meter : meters) {
            totalPrice += meter.calculatePrice(lastBillingDate, LocalDateTime.now());
        }

        return totalPrice;
    }

    public boolean isOverdue() {
        LocalDateTime nextBillingDate = lastBillingDate.plusDays(billingIntervalInDays);
        return LocalDateTime.now().isAfter(nextBillingDate);
    }

    public long remainingDaysUntilNextBilling() {
        LocalDateTime nextBillingDate = lastBillingDate.plusDays(billingIntervalInDays);
        return Duration.between(LocalDateTime.now(), nextBillingDate).toDays();
    }
}
