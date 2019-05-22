import javafx.geometry.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

public class PlayerCountWindow {
    private static Stage stg;
    private static int option;
    private static RadioButton rdoTwo = new RadioButton("2");
    private static RadioButton rdoThree = new RadioButton("3");
    private static RadioButton rdoFour = new RadioButton("4");
    private static RadioButton rdoFive = new RadioButton("5");
    private static RadioButton rdoSix = new RadioButton("6");

    public static int getOption() {
        return option;
    }

    public static void show()
    {
        stg = new Stage();
        Text txt = new Text("How many players should play this game?");
        txt.setFont(Font.font("Helvetica", 20));

        Region spacer = new Region();

        Button btnSubmit = new Button("Submit");
        btnSubmit.setFont(Font.font("Helvetica", 17));
        btnSubmit.setMinWidth(100);
        btnSubmit.setOnAction(e -> submit());
        btnSubmit.setDefaultButton(true);

        HBox btnPane = new HBox();
        btnPane.getChildren().addAll(spacer, btnSubmit);
        btnPane.setHgrow(spacer, Priority.ALWAYS);

        ToggleGroup group = new ToggleGroup();


        rdoTwo.setFont(Font.font("Helvetica", 17));
        rdoTwo.setToggleGroup(group);
        rdoTwo.setSelected(true);


        rdoThree.setFont(Font.font("Helvetica", 17));
        rdoThree.setToggleGroup(group);

        rdoFour.setFont(Font.font("Helvetica", 17));
        rdoFour.setToggleGroup(group);

        rdoFive.setFont(Font.font("Helvetica", 17));
        rdoFive.setToggleGroup(group);

        rdoSix.setFont(Font.font("Helvetica", 17));
        rdoSix.setToggleGroup(group);

        VBox rdoPane = new VBox(20);
        rdoPane.getChildren().addAll(rdoTwo, rdoThree, rdoFour, rdoFive, rdoSix);
        rdoPane.setPadding(new Insets(0, 0, 0, 40));

        VBox pane = new VBox(20);
        pane.setPadding(new Insets(30, 30, 20, 30));
        pane.getChildren().addAll(txt, rdoPane, btnPane);

        stg.setResizable(false);
        stg.setTitle("Data Entry");
        stg.setScene(new Scene(pane));
        stg.showAndWait();
    }

    private static void submit()
    {
        if(rdoTwo.isSelected())
            option = 2;
        else if(rdoThree.isSelected())
            option = 3;
        else if(rdoFour.isSelected())
            option = 4;
        else if(rdoFive.isSelected())
            option = 5;
        else
            option = 6;

        stg.close();
    }
}
