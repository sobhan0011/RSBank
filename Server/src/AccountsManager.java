package sample;
import java.util.ArrayList;

public class AccountsManager implements Runnable
{
    @Override
    public void run()
    {
        ArrayList<Customer> customers = ClientManger.getCustomers();
        for (int i = 0; i < customers.size(); i++)
            for (int j = 0; j < customers.get(i).getAccounts().size(); j++)
                for (int k = 0; k < customers.get(i).getAccounts().get(i).getLoans().size(); k++)
                {
                    if (customers.get(i).getAccounts().get(j).getLoans().get(k).getLastAmount() < 0)
                        customers.get(i).getAccounts().get(j).getLoans().remove(k);

                    else
                    {
                        int period = customers.get(i).getAccounts().get(j).getLoans().get(k).getPeriod();
                        customers.get(i).getAccounts().get(j).getLoans().get(k).setLastAmount(customers.get(i).getAccounts().get(j).getLoans().get(k).getLastAmount()
                                - (1.0 / period) * customers.get(i).getAccounts().get(i).getLoans().get(k).getFirstAmount());

                        customers.get(i).getAccounts().get(j).setAccountBalance(customers.get(i).getAccounts().get(j).getAccountBalance()
                                - (1.0 / period) * customers.get(i).getAccounts().get(i).getLoans().get(k).getFirstAmount());
                    }

                }


        for (Customer customer : customers)
            for (int j = 0; j < customer.getAccounts().size(); j++)
                if (customer.getAccounts().get(j).getAccountType() == Account.AccountType.SAVING)
                    customer.getAccounts().get(j).profit();

    }
}
