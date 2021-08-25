package sample;
import javafx.scene.control.TextField;

public abstract class Checkers
{
    public static String logInCheck(String nationalNumber, String password)
    {
        if (nationalNumber.equals("") || password.equals(""))
            return "Please fill all of the blanks";

        if (nationalNumber.length() != 10 || !nationalNumber.matches("^[0-9]*$"))
            return "Please enter right national number";

        if (password.contains("!") || password.contains("\"") || password.contains("\n"))
            return "Pass shouldn't contain \" or ! or enter";

        return "No problem";
    }

    public static String signUpCheck(TextField[] textFields)
    {
        for (TextField textField : textFields)
        {
            if (textField.getText().equals(""))
                return "Please fill all of the blanks";
        }

        for (int i = 0; i < 2; i++) {
            if (!textFields[i].getText().matches("[a-zA-Z ]+"))
                return "Please enter right name or lastname";
        }

        if(textFields[2].getText().length() != 10 || !textFields[2].getText().matches("^[0-9]*$"))
            return "Please enter right national number";

        if (textFields[3].getText().length() < 8)
            return "Pass must have more than 8 characters";

        if(!textFields[4].getText().matches("^[0-9]*$") || textFields[4].getText().length() < 11)
            return "Enter a valid phone number";

        if (!textFields[5].getText().matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$"))
            return "Enter a valid email";

        if (textFields[5].getText().contains("!") || textFields[5].getText().contains("\"") || textFields[5].getText().contains("\n"))
            return "Email shouldn't contain \" or ! or enter";

        if (textFields[3].getText().contains("!") || textFields[3].getText().contains("\"") || textFields[3].getText().contains("\n"))
            return "Password shouldn't contain \" or ! or enter";

        return "No problem";
    }

    public static String newAccountCheck(String accountType, String name, String password, String balance) {
        if (accountType.equals("") || name.equals("") || password.equals("") || balance.equals(""))
            return "Please fill out all of the blanks!";

        if (!name.matches("[a-zA-Z ]+"))
            return "Please enter right name for account";

        if (!(accountType.equals("Current Account") || accountType.equals("Saving Account")))
            return "Please select one of account types";

        if (password.length() < 8)
            return "Password must be more than 8 character";

        if (password.contains("!") || password.contains("\"") || password.contains("\n"))
            return "Password shouldn't contains \" or ! or enter";

        if (!balance.matches("[0-9]{1,13}(\\.[0-9]*)?"))
            return "Enter right balance";

        return "Successful";
    }

    public static String removeAccountCheck(String destination)
    {
        if (destination.equals(""))
            return "Please fill out all of the blanks!";
        if (!destination.matches("[0-9]+"))
            return "Destinations should only consist of digits";

        return "No problem";
    }

    public static String transactionCheck(String amount, String destination, String password)
    {
        if (amount.equals("") || destination.equals("") || password.equals(""))
            return "Please fill out all of the blanks!";

        if (!amount.matches("[0-9]{1,13}(\\.[0-9]*)?"))
            return "Enter right amount";

        if (!destination.matches("^[0-9]*$"))
            return "Enter right destination";

        if(password.length() < 8)
            return "Pass must be more than 8 character";

        if (password.contains("!") || password.contains("\n") || password.contains("\""))
            return "Pass shouldn't contain \" or ! or enter";

        return "Successful";
    }

    public static String billCheck(String  billID, String paymentID, String password)
    {
        if (billID.equals("") || paymentID.equals("") || password.equals(""))
            return "Please fill out all of the blanks!";

        if (!billID.matches("[0-9]+"))
            return "Enter right billID";

        if (!paymentID.matches("[0-9]+"))
            return "Enter right paymentID";

        if(password.length() < 8)
            return "Password must be more than 8 character";

        if (password.contains("!") || password.contains("\n") || password.contains("\""))
            return "Password shouldn't contain \" or ! or enter";

        return "Successful";
    }

    public static String depositWithdrawCheck(String amount, String password)
    {
        if (amount.equals("") || password.equals(""))
            return "Please fill out all of the blanks!";

        if (!amount.matches("[0-9]{1,13}(\\.[0-9]*)?"))
            return "Enter right amount";

        if(password.length() < 8)
            return "Pass must be more than 8 character";

        if (password.contains("!") || password.contains("\n") || password.contains("\""))
            return "Pass shouldn't contain \" or ! or enter";

        return "Successful";
    }

    public static String loanCheck(String amount, String period)
    {
        if (amount.equals("") || period.equals(""))
            return "Please fill out all of the blanks!";

        if (!amount.matches("[0-9]{1,13}(\\.[0-9]*)?"))
            return "Amount can only be a positive double number!";

        if(period != "5" && period != "10" && period != "20")
            return "You can only choose a period of 5,10, or 20 months!";

        return "Successful";
    }

    public static String favoriteCheck(String name, String destination)
    {

        if (name.equals("") || destination.equals(""))
            return "Please fill all of the blanks";

        if (!name.matches("[a-zA-Z ]+"))
            return "Please enter right name for account";

        if (!destination.matches("[0-9]+"))
            return "Destinations should only consist of digits";

        return "No problem";

    }

}
