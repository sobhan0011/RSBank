package sample;

import java.io.Serializable;

public class Loan implements Serializable
{
    private int period;
    private double lastAmount;
    private final double firstAmount;
    private double interest;

    Loan(double firstAmount, int period)
    {
        switch (period){
            case 5 :
                this.interest = (5 / 100) * firstAmount;
                break;
            case 10 :
                this.interest = (10 / 100) * firstAmount;
                break;
            case 20 :
                this.interest = (20 / 100) * firstAmount;
                break;
        }
        this.firstAmount = firstAmount + this.interest;
        this.lastAmount = firstAmount;
        this.period = period;
    }

    public double getFirstAmount() {
        return firstAmount;
    }

    public double getLastAmount() {
        return lastAmount;
    }

    public void setLastAmount(double lastAmount) {
        this.lastAmount = lastAmount;
    }

    public int getPeriod() {
        return period;
    }
}
