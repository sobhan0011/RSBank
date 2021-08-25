package sample;
import java.util.ArrayList;
import java.util.Date;

public class Account
{
    private String name;
    private String password;
    private long accountNumber;
    private String dateCreated;
    private double accountBalance;
    private AccountType accountType;
    private final ArrayList<String> transactions = new ArrayList<>();
    private final ArrayList<Loan> loans = new ArrayList<>();
    private final ArrayList<String> favorites = new ArrayList<>();

    public enum AccountType
    {
        SAVING,
        CURRENT
    }

    public Account(String accountType, String name, String password, double accountBalance, long accountNumber, String dateCreated)
    {
        setName(name);
        setPassword(password);
        setAccountNumber(accountNumber);
        setAccountBalance(accountBalance);
        setAccountType(((accountType.equals("Saving Account")) || accountType.equalsIgnoreCase("SAVING")) ? AccountType.SAVING : AccountType.CURRENT);
        setDateCreated(dateCreated);
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public void setAccountNumber(long accountNumber)
    {
        this.accountNumber = accountNumber;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public ArrayList<Loan> getLoans(){return loans;}

    public ArrayList<String> getTransactions() {
        return transactions;
    }

    public String transaction(double moneyAmount, long destination, String accPassword, ArrayList<Customer> customers, Account currentAccount)
    {
        if (moneyAmount > currentAccount.getAccountBalance())
            return "Not enough balance";

        if (!accPassword.equals(currentAccount.getPassword()))
            return "Wrong password";

        for (Customer customer : customers)
            for (int j = 0; j < customer.getAccounts().size(); j++)
                if (destination == customer.getAccounts().get(j).getAccountNumber() && accPassword.equals(this.password)) {
                    setAccountBalance(getAccountBalance() - moneyAmount);
                    customer.getAccounts().get(j).setAccountBalance(customer.getAccounts().get(j).getAccountBalance() + moneyAmount);
                    transactions.add("\"" + currentAccount.getAccountNumber()  + "Sent " + moneyAmount + " to " + destination + " " + "\" ");
                    return "Transaction Complete!";
                }

        return "No such destination";
    }

    public String payBill(long billingID, long paymentID, String password, ArrayList<Bill> bills, Account currentAccount)
    {
        if (!password.equals(currentAccount.getPassword()))
            return "Wrong password!";

        for (int i = 0; i < bills.size(); i++)
            if (billingID == bills.get(i).getBillingID() && paymentID == bills.get(i).getPaymentID()) {
                if (bills.get(i).getAmountPaid() > currentAccount.getAccountBalance())
                    return "Insufficient funds!";

                setAccountBalance(getAccountBalance() - bills.get(i).getAmountPaid());
                transactions.add("\"" + currentAccount.getAccountNumber() + " paid bill " + billingID + " " + paymentID + " " + bills.get(i).getAmountPaid() + "\" ");
                bills.remove(i);
                return "Bill was paid successfully.";
            }

        return "Bill was not found.";
    }

    public String withdraw(double amount, String password, Account currentAccount)
    {
        if (amount > currentAccount.getAccountBalance())
            return "Insufficient funds!";

        if (!password.equals(currentAccount.getPassword()))
            return "Wrong password!";

        currentAccount.setAccountBalance(currentAccount.getAccountBalance() - amount);
        transactions.add("\"" + currentAccount.getAccountNumber() + " withdrew " + amount + " on " + new Date() + "\" ");
        return "Withdrew " + amount;
    }

    public String deposit(double amount, String password, Account currentAccount)
    {
        if (!password.equals(currentAccount.getPassword()))
            return "Wrong password!";

        currentAccount.setAccountBalance(currentAccount.getAccountBalance() + amount);
        transactions.add("\"" + currentAccount.getAccountNumber() + " deposited " + amount + " on " + new Date() + "\" ");
        return "deposited " + amount;
    }

    public void addTransactions(String transaction)
    {
        transactions.add(transaction);
    }

    public void addLoans(Loan loan)
    {
        loans.add(loan);
    }

    public void addFavorites(String favorite)
    {
        favorites.add(favorite);
    }

    public ArrayList<String> getFavorites() {
        return favorites;
    }

    public String loan(double moneyAmount, String period, Account currentAccount)
    {
        Loan loan = new Loan(moneyAmount, Integer.parseInt(period));
        if (moneyAmount > 2 * currentAccount.accountBalance)
            return "You can not apply for a loan with an amount more than double your balance!";

        currentAccount.setAccountBalance(currentAccount.getAccountBalance() + moneyAmount);
        transactions.add("\"" + "Loan " + moneyAmount + "\" ");
        currentAccount.addLoans(loan);
        return "Done";
    }

    public void profit()
    {
        transactions.add("\"" + "Profit " + this.accountBalance * (5.0 / 100) + "\" ");
        this.accountBalance += this.accountBalance * (5.0 / 100);
        ClientManger.writer();
    }

    @Override
    public String toString()
    {
        String str = "\"" + accountType + "\" " + "\"" + name + "\" " + "\"" + password + "\" " + "\"" + accountBalance + "\" " + "\"" + accountNumber + "\" \"" + dateCreated + "\" ";

        for (String transaction : this.transactions)
            str += transaction;

        str += "\"Favorites: \" ";

        for (String favorite : this.favorites) {
            str += favorite;
        }

        return str + "\n";
    }

}
