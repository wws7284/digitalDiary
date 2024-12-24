package assignmentcreateedit;

import javafx.stage.FileChooser;
import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Create {
    @FXML
    private TextField titleField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextArea contentField;
    @FXML
    private Slider moodSlider;
    
    private String filePath;
    
    @FXML
    private void onCreateButtonClick(){
        //get input values
        if (titleField.getText().isEmpty() || contentField.getText().isEmpty()){
            showAlert("Incomplete","Please fill in the required fields.",Alert.AlertType.ERROR);
            return;
        }
        
        String title = titleField.getText();
        LocalDate date = (datePicker.getValue() != null)? datePicker.getValue() : LocalDate.now();
        String content = contentField.getText();
        double mood = moodSlider.getValue();
        
        if (filePath == null || filePath.isEmpty()) {
            filePath = new File("src/assignmentcreateedit/entry.csv").getAbsolutePath();
            return; 
        }
        
        File file = new File(filePath);
        boolean isNewFile = false;
        
        
        String actualContent = escapeCSV(content);
        
        try {
            if (!file.exists()){
                isNewFile = file.createNewFile();
            }
            
            try (FileWriter writer = new FileWriter(file, true)){
                if (isNewFile){
                    writer.write("Title,Date,Mood,Content\n");
                }
                writer.write(title+","+date+","+mood+","+actualContent+"\n");
                showAlert("Success","Diary entry is created successfully!", Alert.AlertType.INFORMATION);
            } 
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error","Diary entry creation failed.",Alert.AlertType.ERROR);
            }
    }
    
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private String escapeCSV(String input) {
        if (input.contains(",") || input.contains("\n") || input.contains("\"")) {
            input = input.replace("\"", "\"\""); 
            input = "\"" + input + "\"";         
        }
        return input;
    }
    
    @FXML
    private void onSetFilePathButtonClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
    
        Stage stage = (Stage) titleField.getScene().getWindow();
        File selectedFile = fileChooser.showSaveDialog(stage);

        if (selectedFile != null) {
            filePath = selectedFile.getAbsolutePath();

            showAlert("Success", "File path has been set successfully!", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Error", "No file selected. File path has been set to default location.", Alert.AlertType.ERROR);
        }
    }
    @FXML
    private void onCancelButtonClick() {
        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.close();
    }
}