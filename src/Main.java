import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import windows.MainWindow.MainWindow;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //setUserAgentStylesheet(STYLESHEET_);
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("windows/MainWindow/MainWindow.fxml"));
        Parent root = loader.load();
        MainWindow window = loader.getController();
        primaryStage.setTitle("Журнал регистрации");
        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icon.png")));
//        primaryStage.getScene().getStylesheets().add(getClass().getResource("bootstrap3.css").toString());
        window.setStage(primaryStage);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
