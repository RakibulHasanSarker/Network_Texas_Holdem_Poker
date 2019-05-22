import javafx.geometry.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import java.util.*;

public class Menu1 {
    private String temp;
    private int choice;
    private double amount;

    private TextField tfAmount = new TextField();
    private Stage stg = new Stage();
    private Vector<Player> players;

    private RadioButton rdoCheck = new RadioButton("Check");
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
        VBox rdoPane = new VBox(30);
        VBox pane = new VBox(20);

        btnSubmit.setFont(Font.font("Helvetica", 14));
        btnSubmit.setMinWidth(100);
        btnSubmit.setDefaultButton(true);

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

        //----------------------------------

        btnSubmit.setOnAction(e -> submit(id));

        rdoCheck.setFont(Font.font("Helvetica", 17));
        rdoCheck.setToggleGroup(group);
        rdoCheck.setSelected(true);

        rdoRaise.setFont(Font.font("Helvetica", 17));
        rdoRaise.setToggleGroup(group);

        tfAmount.setPromptText("Enter amount of chips to raise to");

        HBox raisePane = new HBox(20);
        raisePane.getChildren().addAll(rdoRaise, tfAmount);

        rdoAllIn.setToggleGroup(group);
        rdoAllIn.setFont(Font.font("Helvetica", 17));

        rdoPane.getChildren().addAll(rdoCheck, raisePane, rdoAllIn);
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
        else if (rdoCheck.isSelected()) {
            choice = 1;
            stg.close();
        }
        else if(rdoAllIn.isSelected()){
            choice = 3;
            stg.close();
        }
        tfAmount.clear();
        rdoCheck.setSelected(true);
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
                choice = 2;
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