import atlantafx.base.theme.PrimerDark;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class FileManagerApplication extends Application {
    private static final int DEFAULT_WIDTH = 1100;
    private static final int DEFAULT_HEIGHT = 720;

    @Override
    public void start(Stage stage) {
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());

        var root = new BorderPane();
        root.setCenter(new Label("Veloce Commander"));

        var scene = new Scene(root, DEFAULT_WIDTH, DEFAULT_HEIGHT);

        stage.setTitle("Veloce Commander");
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.show();
    }
}
