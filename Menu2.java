import javafx.geometry.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import java.util.*;

public class Menu2 {
    private String temp;
    private int choice;
    private double amount;

    private TextField tfAmount = new TextField();
    private Stage stg = new Stage();
    private Vector<Player> players;

    private RadioButton rdoFold = new RadioButton("Fold");
    private RadioButton rdoCall = new RadioButton("Call");
    private RadioButton rdoRaise = new RadioButton("Raise to");
    private RadioButton rdoAllIn = new RadioButton("All-in");

    public void setPlayers(Vector<Player> players) {
        this.players = players;
    }

    public int getChoice() {
        return choice;
    }

    public double getAmount() {
        return amount;
    }

    public void show(int id) {
        Button btnSubmit = new Button("Submit");
        Region spacer = new Region();
        ToggleGroup group = new ToggleGroup();
        Text txt = new Text();
        HBox btnPane = new HBox();
        VBox rdoPane = new VBox(20);
        VBox pane = new VBox(20);

        btnSubmit.setFont(Font.font("Helvetica", 14));
        btnSubmit.setMinWidth(100);

        txt.setFont(Font.font("Helvetica", 20));
        txt.setText(players.get(id).getName() + ", choose any of these options:");

        tfAmount.clear();
        tfAmount.setFont(Font.font("Helvetica", 17));
        tfAmount.setPrefColumnCount(16);

        rdoPane.setPadding(new Insets(20, 0, 30, 30));
        pane.setPadding(new Insets(30));

        btnPane.getChildren().addAll(spacer, btnSubmit);
        btnPane.setHgrow(spacer, Priority.ALWAYS);

        stg.setTitle("Data Entry");

        //------------------------------------

        btnSubmit.setOnAction(e -> submit(id));
        btnSubmit.setDefaultButton(true);

        rdoFold.setFont(Font.font("Helvetica", 17));
        rdoFold.setToggleGroup(group);

        rdoCall.setFont(Font.font("Helvetica", 17));
        rdoCall.setToggleGroup(group);
        rdoCall.setSelected(true);

        rdoRaise.setFont(Font.font("Helvetica", 17));
        rdoRaise.setToggleGroup(group);

        tfAmount.setPromptText("Enter amount of chips to raise to");

        HBox raisePane = new HBox(20);
        raisePane.getChildren().addAll(rdoRaise, tfAmount);

        double callAmount = players.get(id).getCallAmount() - players.get(id).getGiven();
        Text txtCallAmount = new Text(callAmount + "$");
        txtCallAmount.setFont(Font.font("Helvetica", 20));

        HBox callPane = new HBox(30);
        callPane.getChildren().addAll(rdoCall, txtCallAmount);

        rdoAllIn.setToggleGroup(group);
        rdoAllIn.setFont(Font.font("Helvetica", 17));

        rdoPane.getChildren().addAll(rdoFold, callPane, raisePane, rdoAllIn);
        pane.getChildren().addAll(txt, rdoPane, btnPane);

        stg.setResizable(false);
        stg.setScene(new Scene(pane));
        stg.setX(800);
        stg.setY(10);
        stg.showAndWait();
    }

    public void submit(int id) {
        if (rdoRaise.isSelected()) {
            handleException(id);
        }
        else if (rdoCall.isSelected()) {

            if (players.get(0).getCallAmount() >= players.get(id).getChips()) {
                temp = players.get(id).getName();
                temp += ", call amount is not less than your chips.";
                temp += "\nYou can't call.";

                MessageBox.show(temp);
                tfAmount.clear();
            }
            else {
                choice = 2;
                stg.close();
            }
        }
        else if (rdoFold.isSelected()) {
            choice = 1;
            stg.close();
        }
        else if (rdoAllIn.isSelected()) {
            choice = 4;
            stg.close();
        }
        tfAmount.clear();
        rdoCall.setSelected(true);
    }

    public void handleException(int id) {
        try {
            double tempAmount = Double.parseDouble(tfAmount.getText());

            if (tempAmount < 2 * Math.max(players.get(0).getCallAmount(), players.get(1).getCallAmount())) {
                temp = players.get(id).getName();
                temp += ", amount have to be at least ";
                temp += 2 * Math.max(players.get(0).getCallAmount(), players.get(1).getCallAmount()) + "$";

                MessageBox.show(temp);
                tfAmount.clear();
            }

            else if(tempAmount >= players.get(id).getChips()) {
                temp = players.get(id).getName();
                temp += ", amount have to be less than your chips.";

                MessageBox.show(temp);
                tfAmount.clear();
            }

            else {
                choice = 3;
                amount = tempAmount;
                stg.close();
            }
        }
        catch (Exception e) {
            temp = players.get(id).getName();
            temp += ", enter a valid number";
            MessageBox.show(temp);
        }
    }
}
