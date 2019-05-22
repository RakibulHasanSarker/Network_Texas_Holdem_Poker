import javafx.geometry.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

public class NameWindow {
    private static String name;
    private static Stage stg = new Stage();
    private static TextField tfName = new TextField();

    public static String getName() {
        return name;
    }

    public static void show(int id) {
        Text txt = new Text("Name: ");
        txt.setFont(Font.font("Helvetica", 25));

        Text prompt = new Text("Player " + id + ", enter your name");
        prompt.setFont(Font.font("Helvetica", 20));

        Region spacer = new Region();

        Button btnSubmit = new Button("Submit");
        btnSubmit.setFont(Font.font("Helvetica", 17));
        btnSubmit.setMinWidth(100);
        btnSubmit.setOnAction(e -> submit());
        btnSubmit.setDefaultButton(true);

        tfName.clear();
        tfName.setPrefColumnCount(20);
        tfName.setFont(Font.font("Helvetica", 20));

        HBox namePane = new HBox(10), btnPane = new HBox();
        namePane.getChildren().addAll(txt, tfName);

        btnPane.getChildren().addAll(spacer, btnSubmit);
        btnPane.setHgrow(spacer, Priority.ALWAYS);
        btnPane.setPadding(new Insets(40, 0, 0, 0));

        VBox pane = new VBox(30);
        pane.getChildren().addAll(prompt, namePane, btnPane);
        pane.setPadding(new Insets(30));

        stg.setResizable(false);
        stg.setTitle("Data Entry");
        stg.setScene(new Scene(pane));
        stg.showAndWait();
    }

    private static void submit() {
        name = tfName.getText();
        stg.close();
    }
}