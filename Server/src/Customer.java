package sample;

import java.util.ArrayList;

public class Customer
{
    private String email;
    private String password;
    private String lastname;
    private String firstname;
    private String phoneNumber;
    private String nationalNumber;
    private ArrayList<Account> accounts = new ArrayList<>();

    public Customer(String firstname, String lastname, String nationalNumber, String password, String email, String phoneNumber)
    {
        setEmail(email);
        setPassword(password);
        setLastname(lastname);
        setFirstname(firstname);
        setPhoneNumber(phoneNumber);
        setNationalNumber(nationalNumber);
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getFirstname()
    {
        return firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setNationalNumber(String nationalNumber) {
        this.nationalNumber = nationalNumber;
    }

    public String getNationalNumber() {
        return nationalNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setAccounts(ArrayList<Account> accounts) {
        this.accounts = accounts;
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }


    @Override
    public String toString() {
        return "!!!\n" + "\"" + firstname + "\" " + "\"" + lastname + "\" " + "\"" + nationalNumber + "\" " + "\"" + password + "\" " + "\"" + phoneNumber + "\" " + "\"" + email + "\"" + "\n";
    }

    public void newAccount(String accountType, String name, String password, double accountBalance, long accountNumber, String dateCreated)
    {
        Account newAccount = new Account(accountType, name, password, accountBalance, accountNumber, dateCreated);
        accounts.add(newAccount);
    }


}