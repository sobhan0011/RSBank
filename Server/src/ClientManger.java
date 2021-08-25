package sample;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.concurrent.locks.ReentrantLock;

public class ClientManger implements Runnable
{

    Socket client;
    private static Scanner scanner;
    private static Account currentAccount;
    private static Customer currentCustomer;
    private final BufferedReader fromClient;
    private final PrintWriter toClient;
    private static final ArrayList<Bill> bills = new ArrayList<>();
    private static final ArrayList<Customer> customers = new ArrayList<>();
    private static final ArrayList<String> informationContent = new ArrayList<>();
    private static final ReentrantLock lock = new ReentrantLock(true);


    public ClientManger(Socket client) throws IOException
    {
        this.client = client;
        fromClient = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
        toClient = new PrintWriter((new OutputStreamWriter(this.client.getOutputStream())), true);
    }

    public static ArrayList<Customer> getCustomers() {
        return customers;
    }

    @Override
    public void run()
    {
        try
        {
            while (true)
            {
                String command = fromClient.readLine();
                switch (command)
                {
                    case "Sign Up":
                        signUp(fromClient.readLine());
                        break;

                    case "Log In":
                        logIn(fromClient.readLine());
                        break;

                    case "Admin":
                        admin();
                        break;

                    case "Accounts":
                        accounts();
                        break;

                    case "New Account":
                        newAccount(fromClient.readLine());
                        break;

                    case "Favorite Account":
                        favorite(fromClient.readLine());
                        break;

                    case "Favorite List":
                        favoriteList();
                        break;

                    case "Remove Account":
                        removeAccount(fromClient.readLine());
                        break;

                    case "Selected Account":
                        setCurrentAccount(fromClient.readLine());
                        break;

                    case "Transaction":
                        transaction(fromClient.readLine());
                        break;

                    case "Bill":
                        bill(fromClient.readLine());
                        break;

                    case "Loan":
                        loan(fromClient.readLine());
                        break;

                    case "Withdraw":
                        withdraw(fromClient.readLine());
                        break;

                    case "Deposit":
                        deposit(fromClient.readLine());
                        break;

                    case "Exit":
                        return;
                }
        } } catch (IOException e) {
            e.printStackTrace();
        }

        lock.lock();
        writer();
        lock.unlock();
    }

    static
    {
        reader();
    }

    public static void reader()
    {
        File customerInfo = new File("information.txt");
        try {
            scanner = new Scanner(customerInfo);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (scanner.hasNext())
            informationContent.add(scanner.nextLine());

        for (int i = 0; i < informationContent.size(); i++)
        {
            String s = informationContent.get(i);
            if(s.equals("!!!"))
            {
                addCustomer(informationContent.get(i + 1));
                i++;
                continue;
            }
            try {
                addAccountsAndTransactionsAndFavorites(informationContent.get(i));
            }
            catch (Exception e) {
            }

            File bills = new File("Bills.txt");
            try {
                scanner = new Scanner(bills);
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            while (scanner.hasNext())
                addBills(scanner.nextLine());

        }


    }

    private static void addBills(String s)
    {
        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(s);
        ArrayList<String> str = new ArrayList<>();
        while (m.find())
            str.add(m.group(1));
        Bill newBill = new Bill(Long.parseLong(str.get(0)), Long.parseLong(str.get(1)), Double.parseDouble(str.get(2)));
        bills.add(newBill);
    }

    private static void addCustomer(String s)
    {
        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(s);
        String[] str = new String[6];
        int l = 0;
        while (m.find()){
            str[l] = m.group(1);
            l++;
        }
        Customer customer = new Customer(str[0], str[1], str[2], str[3], str[4], str[5]);
        customers.add(customer);
    }

    private static void addAccountsAndTransactionsAndFavorites(String s)
    {
        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(s);
        ArrayList<String> str = new ArrayList<>();

        while (m.find())
            str.add(m.group(1));

        customers.get(customers.size() - 1).newAccount(str.get(0), str.get(1), str.get(2), Double.parseDouble(str.get(3)), Long.parseLong(str.get(4)), str.get(5));
        for (int i = 6; !str.get(i).equals("Favorites: "); i++) {
            customers.get(customers.size() - 1).getAccounts().get(customers.get(customers.size() - 1).getAccounts().size() - 1).addTransactions("\"" + str.get(i) + "\" ");
        }

        for (int i = str.indexOf("Favorites: ") + 1; i < str.size(); i++) {
            customers.get(customers.size() - 1).getAccounts().get(customers.get(customers.size() - 1).getAccounts().size() - 1).addFavorites("\"" + str.get(i) + "\" ");
        }
    }

    public static void writer()
    {
        PrintWriter customerInfoWriter = null;
        try {
            customerInfoWriter = new PrintWriter(new BufferedWriter(new FileWriter("information.txt", false)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Customer customer : customers)
        {
            if (customerInfoWriter != null)
                customerInfoWriter.print(customer);

            for (int j = 0; j < customer.getAccounts().size(); j++)
                if (customerInfoWriter != null)
                    customerInfoWriter.print(customer.getAccounts().get(j));
        }

        if (customerInfoWriter != null)
            customerInfoWriter.close();
    }

    public static void billsUpdate()
    {
        PrintWriter billsUpdate = null;

        try {
            billsUpdate = new PrintWriter(new BufferedWriter(new FileWriter("Bills.txt", false)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Bill bill : bills) {
            if (billsUpdate != null)
                billsUpdate.println(bill);
        }

        if (billsUpdate != null)
            billsUpdate.close();
    }

    public void signUp(String information)
    {
        boolean addClient = true;
        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(information);
        String[] strings = new String[6];
        for (int i = 0; m.find(); i++)
            strings[i] = m.group(1);
        for (Customer value : customers) {
            if (value.getNationalNumber().equals(strings[2])) {
                toClient.println("There is already an account with this national number!");
                addClient = false;
                break;
            }
            if (value.getPhoneNumber().equals(strings[4])) {
                toClient.println("There is already an account with this phone number!");
                addClient = false;
                break;
            }
            if (value.getEmail().equals(strings[5])) {
                toClient.println("There is already an account with this Email account!");
                addClient = false;
                break;
            }
        }
        if (addClient)
        {
            lock.lock();
            try {
                Customer customer = new Customer(strings[0], strings[1], strings[2], strings[3], strings[4], strings[5]);
                customers.add(customer);
                currentCustomer = customer;
                writer();
            }
            finally {
                lock.unlock();
            }

            toClient.println("Successful");
        }


    }

    public void logIn(String information)
    {
        boolean found = false;
        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(information);
        String[] strings = new String[2];
        for (int i = 0; m.find(); i++) {
            strings[i] = m.group(1);
        }

        for (Customer customer : customers) {
            if (customer.getNationalNumber().equals(strings[0]) && customer.getPassword().equals(strings[1])) {
                found = true;
                currentCustomer = customer;
                break;
            }
        }

        if (found)
            toClient.println("Found");

        else
            toClient.println("Not Found");
    }

    public void accounts()
    {
        int transactionsNumber = 0;

        toClient.println(currentCustomer.getAccounts().size() + "");

        for (int i = 0; i < currentCustomer.getAccounts().size(); i++)
        {
            if (currentCustomer.getAccounts().get(i).getAccountType().equals(Account.AccountType.CURRENT))
                toClient.println("Current - " + currentCustomer.getAccounts().get(i).getName() + " - " + currentCustomer.getAccounts().get(i).getAccountBalance() + "$");
            else
                toClient.println("Saving - " + currentCustomer.getAccounts().get(i).getName() + " - " + currentCustomer.getAccounts().get(i).getAccountBalance() + "$");
            transactionsNumber += currentCustomer.getAccounts().get(i).getTransactions().size();
        }

        toClient.println(transactionsNumber + "");
        for (int i = 0; i < currentCustomer.getAccounts().size(); i++)
            for (int j = 0; j < currentCustomer.getAccounts().get(i).getTransactions().size(); j++)
                toClient.println(currentCustomer.getAccounts().get(i).getTransactions().get(j));


    }

    public void newAccount(String information)
    {
        Random random = new Random();
        long rand = 0;
        boolean sameName = false;

        System.out.println(information);

        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(information);
        String[] strings = new String[5];
        for (int i = 0; m.find(); i++)
            strings[i] = m.group(1);

        for (int i = 0; i < currentCustomer.getAccounts().size(); i++) {
            if (currentCustomer.getAccounts().get(i).getName().equals(strings[1]))
            {
                sameName = true;
                break;
            }

        }

        thisOne :
        while (!sameName)
        {
            rand = random.nextInt(20000);
            for (Customer customer : customers)
                for (int j = 0; j < customer.getAccounts().size(); j++)
                    if (rand == customer.getAccounts().get(j).getAccountNumber())
                        continue thisOne;

            break thisOne;
        }


        if (sameName)
            toClient.println("You have an account with the same name");

        else
        {
            lock.lock();
            try {
                currentCustomer.getAccounts().add(new Account(strings[0], strings[1], strings[2],  Double.parseDouble(strings[3]), rand, strings[4]));
                toClient.println("Successful");
                writer();
            }
            finally {
                lock.unlock();
            }

        }

    }

    private void removeAccount(String information)
    {
        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(information);
        String[] strings = new String[3];
        for (int i = 0; m.find(); i++) {
            strings[i] = m.group(1);
        }

        boolean found = false;
            for (int j = 0; j < currentCustomer.getAccounts().size(); j++) {
               if (strings[0].equals(currentCustomer.getAccounts().get(j).getName())) {
                   currentAccount = currentCustomer.getAccounts().get(j);
                   found = true;
                   break;
               }
            }

        if (!found)
        {
            toClient.println("Account was not found!");
            return;
        }

        if (!currentAccount.getPassword().equals(strings[1])) {
            toClient.println("Wrong password!");
            return;
        }

        double debt = 0;

        for (int i = 0; i < currentAccount.getLoans().size(); i++) {
            debt += currentAccount.getLoans().get(i).getLastAmount();
        }

        if (currentAccount.getAccountBalance() + debt < 0)
        {
            toClient.println("You can not close the account, you have unpaid debts to the bank!");
            return;
        }

        boolean destinationFound = false;
        Account destination = null;
        for (Customer customer : customers) {
            for (int j = 0; j < customer.getAccounts().size(); j++) {
                if (Long.parseLong(strings[2]) == customer.getAccounts().get(j).getAccountNumber()) {
                    destination = customer.getAccounts().get(j);
                    destinationFound = true;
                }
            }
        }
        if (!destinationFound)
        {
            toClient.println("Destination account was not found!");
            return;
        }

        lock.lock();
        try {
            destination.setAccountBalance(destination.getAccountBalance() + currentAccount.getAccountBalance());
            currentAccount.setAccountBalance(0.0);
            currentCustomer.getAccounts().remove(currentAccount);
            toClient.println("Account was removed successfully.");
            writer();
        }
        finally {
            lock.unlock();
        }

    }

    public void setCurrentAccount(String name)
    {
        for (int i = 0; i < currentCustomer.getAccounts().size(); i++) {
            if (currentCustomer.getAccounts().get(i).getName().equals(name))
                currentAccount = currentCustomer.getAccounts().get(i);
        }
    }

    private void transaction(String information)
    {
        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(information);
        String[] strings = new String[3];
        for (int i = 0; m.find(); i++) {
            strings[i] = m.group(1);
        }

        if (!currentAccount.getPassword().equals(strings[2])){
            toClient.println("Wrong password");
            return;
        }

        lock.lock();
        try {
            toClient.println(currentAccount.transaction(Double.parseDouble(strings[0]), Long.parseLong(strings[1]), strings[2], customers, currentAccount));
            writer();
        }
        finally {
            lock.unlock();
        }

    }

    private void bill(String information)
    {
        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(information);
        String[] strings = new String[3];
        for (int i = 0; m.find(); i++) {
            strings[i] = m.group(1);
        }
        lock.lock();
        try {
            toClient.println(currentAccount.payBill(Long.parseLong(strings[0]), Long.parseLong(strings[1]), strings[2], bills, currentAccount));
            billsUpdate();
            writer();
        }
        finally {
            lock.unlock();
        }

    }

    private void withdraw(String information)
    {
        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(information);
        String[] strings = new String[2];
        for (int i = 0; m.find(); i++)
            strings[i] = m.group(1);
        lock.lock();
        try {
            toClient.println(currentAccount.withdraw(Double.parseDouble(strings[0]), strings[1], currentAccount));
            writer();
        }
        finally {
            lock.unlock();
        }
    }

    private void deposit(String information)
    {
        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(information);
        String[] strings = new String[2];
        for (int i = 0; m.find(); i++)
            strings[i] = m.group(1);
        lock.lock();
        try {
            toClient.println(currentAccount.deposit(Double.parseDouble(strings[0]), strings[1], currentAccount));
            writer();
        }
        finally {
            lock.unlock();
        }
    }

    private void loan(String information)
    {
        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(information);
        String[] strings = new String[2];
        for (int i = 0; m.find(); i++)
            strings[i] = m.group(1);
        lock.lock();
        try {
            toClient.println(currentAccount.loan(Double.parseDouble(strings[0]), strings[1], currentAccount));
            writer();
        }
        finally {
            lock.unlock();
        }
    }

    private void admin()
    {
        int number = customers.size();

        for (Customer value : customers)
            number += value.getAccounts().size();

        toClient.println(number + "");

        for (Customer customer : customers) {
            toClient.println(customer.getFirstname() + " " + customer.getLastname() + " " + customer.getNationalNumber() + " " + customer.getPassword() + " " + customer.getPhoneNumber() + " " + customer.getEmail());
            for (int j = 0; j < customer.getAccounts().size(); j++) {
                String str = ((customer.getAccounts().get(j).getAccountType() == Account.AccountType.SAVING) ? "Saving" : "Current");
                toClient.println(customer.getAccounts().get(j).getAccountNumber() + str + customer.getAccounts().get(j).getName() + customer.getAccounts().get(j).getAccountBalance() + customer.getAccounts().get(j).getPassword());

            }
        }

    }

    private void favorite(String information)
    {
        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(information);
        String[] strings = new String[3];
        for (int i = 0; m.find(); i++)
            strings[i] = m.group(1);

        Account account = null;

        boolean find = false;

        for (int i = 0; i < currentCustomer.getAccounts().size(); i++)
            if (strings[0].equals((currentCustomer.getAccounts().get(i).getAccountNumber()) + "")) {
                account = currentCustomer.getAccounts().get(i);
                find = true;
                break;
            }

        if (!find)
        {
            toClient.println("There is no such account to add favorite to it");
            return;
        }

        find = false;

        outer : for (Customer customer : customers)
            for (int j = 0; j < customer.getAccounts().size(); j++)
                if ((customer.getAccounts().get(j).getAccountNumber() + "").equals(strings[1])) {
                    find = true;
                    break outer;
                }

        if (!find)
        {
            toClient.println("There is no such favorite account");
            return;
        }


        for (int i = 0; i < account.getFavorites().size(); i++)
            if (strings[2].contains(account.getFavorites().get(i)))
            {
                toClient.println("There is such alias");
                return;
            }

        lock.lock();
        for (int i = 0; i < strings.length; i++) {
            account.addFavorites("\"" + strings[i] + "\" ");
        }

        writer();
        lock.unlock();
        System.out.println("injam");
        toClient.println("Successful");
    }

    private void favoriteList()
    {
        int counter = currentAccount.getFavorites().size() / 3;
        toClient.println(counter);


        for (int i = 0; i < counter; i++)
            toClient.println(currentAccount.getFavorites().get(i * 3 + 2).substring(1, currentAccount.getFavorites().get(i * 3 + 2).length() - 1) + currentAccount.getFavorites().get(i * 3 + 1).substring(1, currentAccount.getFavorites().get(i * 3 + 1).length() - 1));


    }

}
