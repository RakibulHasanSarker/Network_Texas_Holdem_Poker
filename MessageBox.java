import javafx.scene.text.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class MessageBox {
	private static boolean flag;

	public static boolean getFlag() {
		return flag;
	}

	public static void show(String message) {
		flag = false;
		Stage stg = new Stage();

		Button btnOK = new Button("OK");
		btnOK.setMinWidth(100);
		btnOK.setOnAction(e -> {
			stg.close();
			flag = true;
		});
		btnOK.setFont(Font.font("Helvetica", 14));
		btnOK.setDefaultButton(true);

		Text txt = new Text();
		txt.setFont(Font.font("Helvetica", 20));
		txt.setText(message);

		VBox pane = new VBox(40);
		pane.getChildren().addAll(txt, btnOK);
		pane.setAlignment(Pos.CENTER);
		pane.setPadding(new Insets(30));

		stg.setResizable(false);
		stg.setScene(new Scene(pane));
		stg.setTitle("Message");
		stg.setX(800);
		stg.setY(10);
		stg.showAndWait();
	}
}