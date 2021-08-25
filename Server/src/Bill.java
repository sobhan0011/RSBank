package sample;

public class Bill
{
    private long billingID;
    private long paymentID;
    private double amountPaid;

    public Bill(long billingID, long paymentID, double amountPaid)
    {
        setBillingID(billingID);
        setPaymentID(paymentID);
        setAmountPaid(amountPaid);
    }

    @Override
    public String toString(){
        return "\"" + billingID + "\" \"" + paymentID + "\" \"" + amountPaid + "\"";
    }

    public long getBillingID() {
        return billingID;
    }

    public void setBillingID(long billingID) {
        this.billingID = billingID;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public long getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(long paymentID) {
        this.paymentID = paymentID;
    }
}
