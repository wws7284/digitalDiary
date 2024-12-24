package assignmentcreateedit;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class AssignmentCreateEdit extends Application {
    
    @Override
    public void start(Stage primaryStage){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Create.fxml"));
            AnchorPane root = loader.load();
            
            primaryStage.setTitle("Diary entry creation");
            primaryStage.setScene(new Scene(root));
            
            primaryStage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args){
        launch(args);
    }
}
