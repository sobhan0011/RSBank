package sample;
import java.io.*;
import java.util.Date;
import javafx.animation.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.geometry.Side;
import javafx.geometry.Insets;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.input.KeyCode;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Fx extends Application
{
    private Stage stage;
    Client client = new Client();
    private static String previousState;

    public static void main (String[]args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        stage = primaryStage;
        primaryStage.setResizable(false);
        InputStream inputStream2 = null;
        try {
            inputStream2 = new FileInputStream("src\\sample\\data\\image\\icon.jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        primaryStage.setHeight(700.0);
        primaryStage.setWidth(900.0);
        primaryStage.setTitle("RSBank");
        if (inputStream2 != null) {
            primaryStage.getIcons().add(new Image(inputStream2));
        }
        primaryStage.setScene(welcomeScene());
        primaryStage.show();
    }

    public Scene welcomeScene()
    {
        GridPane root = new GridPane();
        Scene scene = new Scene(root);

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("src\\sample\\data\\image\\rose.jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (inputStream != null)
            root.setBackground(new Background(new BackgroundImage(new Image(inputStream), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, new BackgroundPosition(Side.LEFT, 0, true, Side.BOTTOM, 0, true), new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, false, true))));

        Label label = new Label("Press Enter To Continue");
        label.setFont(new Font(50.0));
        label.setPrefSize(525.0, 50.0);
        label.setTextFill(Color.WHITE);
        root.addRow(0, label);
        root.setPadding(new Insets((stage.getHeight() / 2 - 25.0), 0.0, 0.0, (stage.getWidth() / 2 - 525.0 / 2)));

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), label);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), label);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        Timeline flushing = new Timeline(new KeyFrame(Duration.seconds(0.5), new EventHandler<>() {
            int i = 0;
            @Override
            public void handle(ActionEvent actionEvent) {
                if (i % 2 == 0)
                    fadeOut.play();
                else
                    fadeIn.play();
                i++;
            }
        }));
        flushing.setCycleCount(Timeline.INDEFINITE);
        flushing.play();

        scene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                fadeIn.stop();
                fadeOut.stop();
                flushing.stop();
                stage.setScene(logInSignUpScene());
            }
        });

        return scene;
    }

    protected Scene logInSignUpScene()
    {
        GridPane root = new GridPane();
        Button logIn = new Button("Log in");
        logIn.setId("logIn");

        Button signUp = new Button("Sign up");
        signUp.setId("signUp");
        root.addRow(1, logIn, signUp);
        root.setHgap(20.0);
        root.setPadding(new Insets(450.0, 0.0, 0.0, 550.0));
        logIn.setOnAction(actionEvent -> {
            previousState = "login";
            stage.setScene(logInScene());
        });

        signUp.setOnAction(actionEvent -> {
            previousState = "signup";
            stage.setScene(signUpScene());
        });
        Scene scene = new Scene(root);
        root.setId("signUplogIn");
        scene.getStylesheets().add("sample/UI.css");
        return scene;
    }

    protected Scene signUpScene()
    {
        GridPane root = new GridPane();

        Button back = new Button("Back");
        back.setId("signUp");

        Label[] texts = new Label[6];
        Label error = new Label();
        TextField[] textFields = new TextField[6];
        for (int i = 0; i < textFields.length; i++) {
            texts[i] = new Label();
            textFields[i] = new TextField();
        }
        texts[0].setText("First Name : ");
        texts[1].setText("Last Name : ");
        texts[2].setText("National Number : ");
        texts[3].setText("Password : ");
        texts[4].setText("Phone Number : ");
        texts[5].setText("Email : ");

        Button ok = new Button("OK");
        ok.setId("logIn");

        ok.setOnAction(actionEvent ->
        {
            String message = Checkers.signUpCheck(textFields);
            if (message.equals("No problem"))
            {
                try {
                    client.writer("Sign Up");
                    client.writer("\"" + textFields[0].getText() + "\" \"" + textFields[1].getText() + "\" \"" + textFields[2].getText() + "\" \"" + textFields[3].getText() + "\" \"" + textFields[4].getText() + "\" \"" + textFields[5].getText() + "\" ");
                    String status = client.reader();
                    if(status.equals("Successful"))
                        stage.setScene(accountScene(stage));
                    else
                        error.setText(status);
                }
                catch (IOException e){
                    e.printStackTrace();
                }

            }
            else
                error.setText(message);
        });

        back.setOnAction(actionEvent -> stage.setScene(logInSignUpScene()));

        root.addColumn(0, texts);
        root.addColumn(1, textFields);
        root.addColumn(2, ok, back, error);
        root.setPadding(new Insets(50.0));
        root.setHgap(20.0);
        root.setVgap(20.0);

        Scene scene = new Scene(root);
        scene.getStylesheets().add("sample/UI.css");
        return scene;
    }

    protected Scene logInScene()
    {
        GridPane root = new GridPane();
        root.setHgap(20.0);
        Button ok = new Button("Submit");
        ok.setId("lilButtonLogIn");
        Button back = new Button("Back");
        back.setId("lilButtonsignUp");
        Button signUpFirst = new Button("Sign Up");
        signUpFirst.setId("logIn");
        Label label1 = new Label("National Number : ");
        Label label2 = new Label("Password : ");
        Label error = new Label();

        TextField textField = new TextField();
        TextField textField2 = new TextField();

        root.addColumn(0, label1, label2);
        root.addColumn(1, textField, textField2);
        root.addColumn(2, ok, back);
        root.addColumn(3, error);
        root.setVgap(20.0);
        root.setPadding(new Insets(200.0, 0.0, 0.0, 50.0));

        back.setOnAction(actionEvent -> stage.setScene(logInSignUpScene()));

        ok.setOnAction(actionEvent ->
        {

            if (textField.getText().equals("admin") && textField2.getText().equals("admin")) {
                try {
                    stage.setScene(adminPage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            String check = Checkers.logInCheck(textField.getText(),textField2.getText());

            if (check.equals("No problem"))
            {
                try {
                    client.writer("Log In");
                    client.writer("\"" + textField.getText() + "\" \"" + textField2.getText() + "\"");
                    String message = client.reader();

                    if(message.equals("Found"))
                        stage.setScene(accountScene(stage));

                    else
                    {
                        error.setText("There is no such account. Sign up first.");
                        root.add(signUpFirst, 3, 1);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            else
                error.setText(check);
        });

        signUpFirst.setOnAction(actionEvent -> stage.setScene(signUpScene()));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("sample/UI.css");
        return scene;
    }

    private Scene adminPage() throws IOException
    {
        client.writer("Admin");
        TableView<Transaction> table = new TableView<>();
        ObservableList<Transaction> data = FXCollections.observableArrayList();

        int list = Integer.parseInt(client.reader());
        for (int i = 0; i < list; i++)
            data.add(new Transaction(client.reader()));

        table.setPrefSize(stage.getWidth() - 150, stage.getHeight());
        TableColumn firstNameCol = new TableColumn("Information");
        firstNameCol.setPrefWidth(stage.getWidth() / 2);
        firstNameCol.setCellValueFactory(new PropertyValueFactory<Transaction, String>("firstName"));
        firstNameCol.setPrefWidth(stage.getWidth());
        table.setItems(data);
        table.getColumns().addAll(firstNameCol);
        table.setEditable(true);

        HBox vBox = new HBox();
        Button back = new Button("Back");
        back.setId("logIn");
        vBox.getChildren().addAll(table, back);
        Scene scene = new Scene(vBox);
        scene.getStylesheets().add("sample/UI.css");
        back.setId("signUp");
        vBox.setId("gridPane");
        back.setOnAction(actionEvent -> {
            for (int i = 0; i < table.getItems().size(); i++)
                client.writer(table.getItems().get(i).getFirstName());
            stage.setScene(logInSignUpScene());
        });
        return scene;
    }

    protected Scene accountScene(Stage stage) throws IOException
    {
        client.writer("Accounts");

        GridPane root = new GridPane();
        GridPane bigRoot = new GridPane();

        Button[] buttons = new Button[Integer.parseInt(client.reader())];

        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new Button(client.reader());
            buttons[i].setPrefSize(350.0, 20.0);
        }

        VBox vBox = new VBox(buttons);
        vBox.setPrefSize(350.0, 400.0);
        ScrollPane scrollPane = new ScrollPane(vBox);
        scrollPane.setFitToWidth(true);
        vBox.setId("gridPane");


        TableView<Transaction> table = new TableView<>();
        ObservableList<Transaction> data = FXCollections.observableArrayList();

        int list = Integer.parseInt(client.reader());
        for (int i = 0; i < list; i++)
            data.add(new Transaction(client.reader()));

        table.setPrefSize(stage.getWidth() / 2, stage.getHeight());
        TableColumn firstNameCol = new TableColumn("Transactions");
        firstNameCol.setPrefWidth(stage.getWidth() / 2);
        firstNameCol.setCellValueFactory(new PropertyValueFactory<Transaction, String>("firstName"));
        table.setItems(data);
        table.getColumns().addAll(firstNameCol);
        table.setEditable(true);

        GridPane gridPane = new GridPane();
        gridPane.setId("gridPane");
        gridPane.addColumn(0, table);
        gridPane.setPrefSize(stage.getWidth() / 2, stage.getHeight());

        for (Button button : buttons)
            button.setOnAction(actionEvent ->
            {
                client.writer("Selected Account");
                String[] split = button.getText().split(" - ");
                client.writer(split[1]);
                for (int i = 0; i < buttons.length; i++)
                {
                    if (button.getText().contains("Current")) {
                        try {
                            stage.setScene(manageCurrentAccountScene());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                        stage.setScene(manageSavingAccountScene());
                }
            });

        GridPane buttonsPane = new GridPane();
        Button back = new Button("Back");
        Button remove = new Button("Remove");
        Button newAccount = new Button("New account");
        Button favoriteAccount = new Button("Favorite");
        back.setId("lilButtonsignUp");
        remove.setPrefSize(150.0, 30.0);
        newAccount.setPrefSize(150.0, 30.0);
        newAccount.setId("lilButtonLogIn");
        remove.setId("lilButtonLogIn");
        favoriteAccount.setId("lilButtonLogIn");
        favoriteAccount.setPrefSize(150.0, 30.0);
        buttonsPane.add(newAccount, 0, 0);
        buttonsPane.add(remove, 0, 1);
        buttonsPane.add(back, 1, 1);
        buttonsPane.add(favoriteAccount, 1, 0);
        buttonsPane.setHgap(20.0);
        buttonsPane.setVgap(20.0);
        buttonsPane.setPadding(new Insets(175.0, 0, 0, 0));

        root.add(scrollPane, 0, 0);
        root.add(buttonsPane, 0, 1);
        root.setHgap(50.0);

        bigRoot.addRow(0, root, gridPane);
        bigRoot.setHgap(30.0);

        remove.setOnAction(actionEvent -> {
            Stage removeStage = removeAccount();
            removeStage.show();
        });

        newAccount.setOnAction(actionEvent -> {
            Stage newAccountStage = newAccount(stage);
            newAccountStage.show();
        });

        favoriteAccount.setOnAction(actionEvent -> {
            Stage favoriteAccountStage = favoriteAccount();
            favoriteAccountStage.show();
        });

        back.setOnAction(actionEvent -> {
            switch (previousState) {
                case "login" :
                    stage.setScene(logInScene());
                    break;
                case "signup" :
                    stage.setScene(signUpScene());
                    break;
            }
        });

        Scene scene = new Scene(bigRoot);
        scene.getStylesheets().add("sample/UI.css");
        return scene;
    }

    public static class Transaction
    {
        private final SimpleStringProperty firstName;

        private Transaction(String fName) {
            this.firstName = new SimpleStringProperty(fName);
        }

        public String getFirstName() {
            return firstName.get();
        }

        public void setFirstName(String fName) {
            firstName.set(fName);
        }

    }

    protected Stage newAccount(Stage stage)
    {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("src\\sample\\data\\image\\icon.jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Stage newAccountStage = new Stage();
        newAccountStage.setTitle("New Account");
        newAccountStage.setResizable(false);
        newAccountStage.setWidth(200.0);
        newAccountStage.setHeight(220.0);
        if (inputStream != null)
            newAccountStage.getIcons().add(new Image(inputStream));
        GridPane newAccountGridPane = new GridPane();
        Button newAccountOK = new Button("OK");
        TextField newAccountName = new TextField();
        TextField newAccountPassword = new TextField();
        TextField newAccountBalance = new TextField();
        Label error = new Label();
        newAccountStage.initOwner(stage);
        newAccountStage.initModality(Modality.APPLICATION_MODAL);
        ObservableList<String> accountTypes = FXCollections.observableArrayList("Current Account", "Saving Account");
        ChoiceBox<String> newAccountTypesChoiceBox = new ChoiceBox<>(accountTypes);
        newAccountOK.setPrefSize(200.0, 25.0);
        newAccountPassword.setPrefSize(200.0, 35.0);
        newAccountPassword.setPromptText("Password");
        newAccountName.setPrefSize(200.0, 35.0);
        newAccountName.setPromptText("Name");
        newAccountBalance.setPrefSize(200.0, 35.0);
        newAccountBalance.setPromptText("Balance");
        newAccountTypesChoiceBox.setPrefSize(200.0, 35.0);
        newAccountTypesChoiceBox.setValue("Account Type");
        newAccountGridPane.add(newAccountTypesChoiceBox, 0, 0);
        newAccountGridPane.add(newAccountName, 0, 1);
        newAccountGridPane.add(newAccountPassword, 0, 2);
        newAccountGridPane.add(newAccountBalance, 0, 3);
        newAccountGridPane.add(newAccountOK, 0, 4);
        newAccountGridPane.add(error, 0, 5);
        newAccountStage.setScene(new Scene(newAccountGridPane));

        newAccountOK.setOnAction(actionEvent ->
        {
            String message = Checkers.newAccountCheck(newAccountTypesChoiceBox.getValue(), newAccountName.getText(), newAccountPassword.getText(), newAccountBalance.getText());
            if (message.equals("Successful"))
            {
                try {
                    client.writer("New Account");
                    client.writer("\"" + newAccountTypesChoiceBox.getValue() + "\" \"" + newAccountName.getText() + "\" \"" + newAccountPassword.getText() + "\" \"" + newAccountBalance.getText() + "\" \"" + new Date() + "\" ");
                    String status = client.reader();

                    if (status.equals("Successful"))
                    {
                        newAccountStage.close();
                        stage.setScene(accountScene(stage));
                    }
                    else
                        error.setText(status);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            else
                error.setText(message);


        });

        return newAccountStage;
    }

    protected Stage removeAccount()
    {
        Stage removeAccountStage = new Stage();
        removeAccountStage.setTitle("Remove Account");
        removeAccountStage.setResizable(false);
        removeAccountStage.setWidth(200.0);
        removeAccountStage.setHeight(200.0);

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("src\\sample\\data\\image\\icon.jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (inputStream != null)
            removeAccountStage.getIcons().add(new Image(inputStream));

        GridPane removeAccountGridPane = new GridPane();
        Button removeAccountOK = new Button("OK");
        TextField removeAccountName = new TextField();
        TextField removeAccountPassword = new TextField();
        TextField removeAccountDestination = new TextField();
        Label error = new Label("");
        removeAccountStage.initOwner(stage);
        removeAccountStage.initModality(Modality.APPLICATION_MODAL);
        removeAccountOK.setPrefSize(200.0, 25.0);
        removeAccountPassword.setPrefSize(200.0, 35.0);
        removeAccountPassword.setPromptText("Password");
        removeAccountDestination.setPrefSize(200.0, 35.0);
        removeAccountDestination.setPromptText("Destination");
        removeAccountName.setPrefSize(200.0, 35.0);
        removeAccountName.setPromptText("Name");
        removeAccountGridPane.add(removeAccountName, 0, 0);
        removeAccountGridPane.add(removeAccountPassword, 0, 1);
        removeAccountGridPane.add(removeAccountDestination, 0, 2);
        removeAccountGridPane.add(removeAccountOK, 0, 3);
        removeAccountGridPane.add(error, 0, 4);
        removeAccountStage.setScene(new Scene(removeAccountGridPane));

        removeAccountOK.setOnAction(actionEvent -> {
            String message = Checkers.removeAccountCheck(removeAccountDestination.getText());
            if (message.equals("No problem"))
            {
                try {
                    client.writer("Remove Account");
                    client.writer("\"" + removeAccountName.getText() + "\" \"" + removeAccountPassword.getText() + "\" \"" + removeAccountDestination.getText() + "\"");
                    String status = client.reader();
                    if (status.equals("Account was removed successfully."))
                    {
                        removeAccountStage.close();
                        stage.setScene(accountScene(stage));
                    }
                    else
                        error.setText(status);
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
            else
                error.setText(message);

        });

        return removeAccountStage;
    }

    protected Stage favoriteAccount()
    {
        Stage favoriteAccountStage = new Stage();
        favoriteAccountStage.setTitle("Favorite Account");
        favoriteAccountStage.setResizable(false);
        favoriteAccountStage.setWidth(200.0);
        favoriteAccountStage.setHeight(200.0);

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("src\\sample\\data\\image\\icon.jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (inputStream != null)
            favoriteAccountStage.getIcons().add(new Image(inputStream));

        GridPane favoriteAccountGridPane = new GridPane();
        Button favoriteAccountOK = new Button("Submit");
        TextField accountNumber = new TextField();
        TextField favoriteAccountNumber = new TextField();
        TextField favoriteAccountName = new TextField();
        Label error = new Label("");

        favoriteAccountStage.initOwner(stage);
        favoriteAccountStage.initModality(Modality.APPLICATION_MODAL);

        accountNumber.setPrefSize(200.0, 25.0);
        accountNumber.setPromptText("Account number");
        favoriteAccountName.setPrefSize(200.0, 35.0);
        favoriteAccountName.setPromptText("Name");
        favoriteAccountNumber.setPrefSize(200.0, 35.0);
        favoriteAccountNumber.setPromptText("Favorite account number");
        favoriteAccountOK.setPrefSize(200.0, 25.0);

        favoriteAccountGridPane.add(accountNumber, 0, 0);
        favoriteAccountGridPane.add(favoriteAccountNumber, 0, 1);
        favoriteAccountGridPane.add(favoriteAccountName, 0, 2);
        favoriteAccountGridPane.add(favoriteAccountOK, 0, 3);
        favoriteAccountGridPane.add(error, 0, 4);

        favoriteAccountStage.setScene(new Scene(favoriteAccountGridPane));

        favoriteAccountOK.setOnAction(actionEvent -> {
            String message = Checkers.favoriteCheck(favoriteAccountName.getText(), favoriteAccountNumber.getText());
            if (message.equals("No problem"))
            {
                try {
                    client.writer("Favorite Account");
                    client.writer("\"" + accountNumber.getText() + "\" \"" + favoriteAccountNumber.getText() + "\" \"" + favoriteAccountName.getText() + "\"");
                    String status = client.reader();
                    if (status.equals("Favorite Account added."))
                    {
                        favoriteAccountStage.close();
                        stage.setScene(accountScene(stage));
                    }
                    else
                        error.setText(status);
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
            else
                error.setText(message);
        });

        return favoriteAccountStage;
    }

    protected Scene manageCurrentAccountScene() throws IOException
    {
        Label transactionLabel = new Label("Transaction");
        Label payingBillLabel = new Label("PayingBill------------------------------");
        Label loanLabel = new Label("Loan-----------------------------------");
        Label depositLabel = new Label("Deposit");
        Label withdrawLabel = new Label("Withdraw-------------------------");
        transactionLabel.setId("bigLabel");
        payingBillLabel.setId("bigLabel");
        loanLabel.setId("bigLabel");
        depositLabel.setId("bigLabel");
        withdrawLabel.setId("bigLabel");

        GridPane transaction = new GridPane();
        GridPane payingBill = new GridPane();
        GridPane loan = new GridPane();
        transaction.setId("gridPane");
        payingBill.setId("gridPane");
        loan.setId("gridPane");
        GridPane gridPane = new GridPane();
        gridPane.setId("gridPane");
        gridPane.addColumn(0, transactionLabel, transaction, payingBillLabel, payingBill, loanLabel, loan);
        gridPane.setVgap(50.0);

        GridPane deposit = new GridPane();
        GridPane withdraw = new GridPane();
        GridPane gridPane2 = new GridPane();
        deposit.setId("gridPane");
        withdraw.setId("gridPane");
        gridPane2.addColumn(0,depositLabel, deposit, withdrawLabel, withdraw);
        gridPane2.setVgap(75.0);
        gridPane2.setId("gridPane");

        Label error = new Label();

        Label[] transactionTexts = new Label[3];
        Label[] payingBillTexts = new Label[3];
        Label[] depositTexts = new Label[2];
        Label[] withdrawTexts = new Label[2];

        TextField[] transactionTextFields = new TextField[3];
        TextField[] payingBillTextFields = new TextField[3];
        TextField[] depositTextsFields = new TextField[2];
        TextField[] withdrawTextsFields = new TextField[2];

        for (int i = 0; i < transactionTextFields.length; i++)
        {
            transactionTexts[i] = new Label();
            transactionTexts[i].setId("lilLabel");
            transactionTextFields[i] = new TextField();
            payingBillTexts[i] = new Label();
            payingBillTexts[i].setId("lilLabel");
            payingBillTextFields[i] = new TextField();
            if (i < 2)
            {
                depositTexts[i] = new Label();
                depositTexts[i].setId("lilLabel");
                depositTextsFields[i] = new TextField();
                withdrawTexts[i] = new Label();
                withdrawTexts[i].setId("lilLabel");
                withdrawTextsFields[i] = new TextField();
            }
        }

        transactionTexts[0].setText("Amount: ");
        transactionTexts[1].setText("Destination: ");
        transactionTexts[2].setText("Account Password: ");
        Button transactionOK = new Button("Submit");
        transactionOK.setId("lilButtonLogIn");

        payingBillTexts[0].setText("Bill ID: ");
        payingBillTexts[1].setText("Payment ID: ");
        payingBillTexts[2].setText("Password: ");
        Button payingBillOK = new Button("Submit");
        payingBillOK.setId("lilButtonLogIn");

        depositTexts[0].setText("Amount:");
        depositTexts[1].setText("Account Password: ");
        Button depositOK = new Button("Submit");
        depositOK.setId("lilButtonLogIn");

        withdrawTexts[0].setText("Amount: ");
        withdrawTexts[1].setText("Account Password: ");
        Button withdrawOK = new Button("Submit");
        withdrawOK.setId("lilButtonLogIn");

        Label loanText = new Label("Amount: ");
        loanText.setId("lilLabel");
        TextField loanTextFields = new TextField();
        Button loanButton = new Button("Submit");
        loanButton.setId("lilButtonLogIn");

        deposit.addColumn(0, depositTexts);
        deposit.addColumn(1, depositTextsFields);
        deposit.add(depositOK, 2, 1);
        deposit.setHgap(10.0);
        deposit.setVgap(10.0);

        withdraw.addColumn(0, withdrawTexts);
        withdraw.addColumn(1, withdrawTextsFields);
        withdraw.add(withdrawOK, 2, 1);
        withdraw.setHgap(10.0);
        withdraw.setVgap(10.0);

        client.writer("Favorite List");

        ObservableList<String> favoriteAccountsObservabaleList = FXCollections.observableArrayList();
        int a;
        a = Integer.parseInt(client.reader());
        System.out.println(a);
        for (int i = 0; i < a; i++) {
            String string = client.reader();
            System.out.println(string);
            favoriteAccountsObservabaleList.add(string);
        }

        ChoiceBox<String> favoriteAccountTypesChoiceBox = new ChoiceBox<>(favoriteAccountsObservabaleList);
        favoriteAccountTypesChoiceBox.setPrefSize(100.0, 35.0);
        favoriteAccountTypesChoiceBox.setValue("Favorites");

        favoriteAccountTypesChoiceBox.setId("favorites");

        favoriteAccountTypesChoiceBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (!newValue.equals("Favorite Accounts"))
                {
                    String string = newValue.substring(newValue.indexOf('\"') + 1, newValue.lastIndexOf('\"'));
                    transactionTextFields[1].setText(string);
                }

            }
        });

        transaction.addColumn(0, transactionTexts);
        transaction.addColumn(1, transactionTextFields);
        transaction.addColumn(2, favoriteAccountTypesChoiceBox, transactionOK);
        transaction.setHgap(20.0);
        transaction.setVgap(10.0);
        transaction.setGridLinesVisible(false);

        payingBill.addColumn(0, payingBillTexts);
        payingBill.addColumn(1, payingBillTextFields);
        payingBill.add(payingBillOK, 2, 2);
        payingBill.setGridLinesVisible(false);
        payingBill.setHgap(20.0);
        payingBill.setVgap(10.0);

        loan.addColumn(0, loanText);
        loan.addColumn(1, loanTextFields);
        loan.addColumn(3, loanButton);
        loan.setHgap(20.0);
        loan.setVgap(10.0);
        loanTextFields.setPromptText("ex: 3000-5(10)(20)");

        loanButton.setOnAction(actionEvent -> {
            String[] split = loanTextFields.getText().split("-");
            String status = Checkers.loanCheck(split[0], split[1]);
            if (status.equals("Successful"))
            {
                try {
                    client.writer("Loan");
                    client.writer("\"" + split[0] + "\" \"" + split[1] + "\"");
                    String message = client.reader();
                    error.setText(message);
                    loanTextFields.setText("");
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }

            else
                error.setText(status);
        });

        transactionOK.setOnAction(actionEvent -> {
            String status = Checkers.transactionCheck(transactionTextFields[0].getText(), transactionTextFields[1].getText(), transactionTextFields[2].getText());
            if (status.equals("Successful"))
            {
                try {
                    client.writer("Transaction");
                    client.writer("\"" + transactionTextFields[0].getText() + "\" \"" + transactionTextFields[1].getText() + "\" \"" + transactionTextFields[2].getText() + "\"");
                    String message = client.reader();
                    error.setText(message);
                    transactionTextFields[0].setText("");
                    transactionTextFields[1].setText("");
                    transactionTextFields[1].setText("");
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }

            else
                error.setText(status);

        });

        payingBillOK.setOnAction(actionEvent ->
        {
            String status = Checkers.billCheck(transactionTextFields[0].getText(), transactionTextFields[1].getText(), transactionTextFields[2].getText());
            if (status.equals("Successful"))
            {
                try{
                    client.writer("Bill");
                    client.writer("\"" + payingBillTextFields[0].getText() + "\" \"" + payingBillTextFields[1].getText() + "\" \"" + payingBillTextFields[2].getText() + "\"");
                    String message = client.reader();
                    error.setText(message);
                    payingBillTextFields[0].setText("");
                    payingBillTextFields[1].setText("");
                    payingBillTextFields[2].setText("");
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
            else
                error.setText(status);
        });

        withdrawOK.setOnAction(actionEvent -> {
            String status = Checkers.depositWithdrawCheck(withdrawTextsFields[0].getText(), withdrawTextsFields[1].getText());
            if (status.equals("Successful"))
            {
                try {
                    client.writer("Withdraw");
                    client.writer("\"" + withdrawTextsFields[0].getText() + "\" \"" + withdrawTextsFields[1].getText() + "\"");
                    String message = client.reader();
                    error.setText(message);
                    withdrawTextsFields[0].setText("");
                    withdrawTextsFields[1].setText("");
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }

            else
                error.setText(status);

        });

        depositOK.setOnAction(actionEvent -> {
            String status = Checkers.depositWithdrawCheck(depositTextsFields[0].getText(), depositTextsFields[1].getText());
            if (status.equals("Successful"))
            {
                try {
                    client.writer("Deposit");
                    client.writer("\"" + depositTextsFields[0].getText() + "\" \"" + depositTextsFields[1].getText() + "\"");
                    String message = client.reader();
                    error.setText(message);
                    depositTextsFields[0].setText("");
                    depositTextsFields[1].setText("");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
                error.setText(status);
        });

        Button back = new Button("Back");
        back.setId("signUp");

        back.setOnAction(actionEvent -> {
            try {
                stage.setScene(accountScene(stage));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        gridPane2.add(back, 0, 4);

        GridPane bigGridPane = new GridPane();
        bigGridPane.addRow(0, gridPane, gridPane2);
        bigGridPane.addRow(1, error);
        bigGridPane.setHgap(50.0);
        bigGridPane.setPadding(new Insets(40.0, 20.0 ,0.0 ,20.0));

        Scene scene = new Scene(bigGridPane);
        scene.getStylesheets().add("sample/UI.css");
        return scene;
    }

    protected Scene manageSavingAccountScene()
    {
        GridPane root = new GridPane();
        root.setHgap(50.0);
        root.setPadding(new Insets(50.0, 0.0, 0.0, 240.0));

        Label depositLabel = new Label("Deposit");
        Label withdrawLabel = new Label("Withdraw----------------------------");
        depositLabel.setId("bigLabel");
        withdrawLabel.setId("bigLabel");

        GridPane deposit = new GridPane();
        GridPane withdraw = new GridPane();
        GridPane gridPane = new GridPane();
        deposit.setId("gridPane");
        withdraw.setId("gridPane");
        gridPane.addColumn(0,depositLabel, deposit, withdrawLabel, withdraw);
        gridPane.setVgap(75.0);
        gridPane.setId("gridPane");

        root.setPrefSize(stage.getWidth(), stage.getHeight());
        root.addColumn(0, gridPane);
        root.setVgap(50.0);

        Label[] depositTexts = new Label[2];
        Label[] withdrawTexts = new Label[2];
        TextField[] depositTextsFields = new TextField[2];
        TextField[] withdrawTextsFields = new TextField[2];

        for (int i = 0; i < depositTextsFields.length; i++)
        {
                depositTexts[i] = new Label();
                depositTexts[i].setId("lilLabel");
                depositTextsFields[i] = new TextField();
                withdrawTexts[i] = new Label();
                withdrawTexts[i].setId("lilLabel");
                withdrawTextsFields[i] = new TextField();
        }

        depositTexts[0].setText("Amount:");
        depositTexts[1].setText("Account Password: ");
        Button depositOK = new Button("Submit");
        depositOK.setId("lilButtonLogIn");

        withdrawTexts[0].setText("Amount: ");
        withdrawTexts[1].setText("Account Password: ");
        Button withdrawOK = new Button("Submit");
        withdrawOK.setId("lilButtonLogIn");

        deposit.addColumn(0, depositTexts);
        deposit.addColumn(1, depositTextsFields);
        deposit.add(depositOK, 2, 1);
        deposit.setHgap(20.0);
        deposit.setVgap(10.0);

        withdraw.addColumn(0, withdrawTexts);
        withdraw.addColumn(1, withdrawTextsFields);
        withdraw.add(withdrawOK, 2, 1);
        withdraw.setHgap(20.0);
        withdraw.setVgap(10.0);

        Button back = new Button("Back");
        back.setId("signUp");
        gridPane.add(back, 0, 4);

        Label error = new Label("");

        back.setOnAction(actionEvent -> {
            try {
                stage.setScene(accountScene(stage));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        depositOK.setOnAction(actionEvent -> {
            String status = Checkers.depositWithdrawCheck(depositTextsFields[0].getText(), depositTextsFields[1].getText());
            if (status.equals("Successful"))
            {
                try {
                    client.writer("Deposit");
                    client.writer("\"" + depositTextsFields[0].getText() + "\" \"" + depositTextsFields[1].getText() + "\"");
                    String message = client.reader();
                    error.setText(message);
                    depositTextsFields[0].setText("");
                    depositTextsFields[1].setText("");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
                error.setText(status);
        });

        withdrawOK.setOnAction(actionEvent -> {
            String status = Checkers.depositWithdrawCheck(withdrawTextsFields[0].getText(), withdrawTextsFields[1].getText());
            if (status.equals("Successful"))
            {
                try {
                    client.writer("Withdraw");
                    client.writer("\"" + withdrawTextsFields[0].getText() + "\" \"" + withdrawTextsFields[1].getText() + "\"");
                    String message = client.reader();
                    error.setText(message);
                    withdrawTextsFields[0].setText("");
                    withdrawTextsFields[1].setText("");
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }

            else
                error.setText(status);
        });


        Scene scene = new Scene(root);
        root.setId("transactions");
        scene.getStylesheets().add("sample/UI.css");
        return scene;
    }

    @Override
    public void stop(){
        client.writer("Exit");
    }
}
