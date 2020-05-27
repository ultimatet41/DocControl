package windows.SettingsWindow;

import data.AbstrDoc;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import windows.AbstrWindow;
import windows.MainWindow.MainWindow;

import java.io.File;
import java.util.prefs.Preferences;

public class SettingsWindow extends AbstrWindow {

    @FXML
    public void initialize() {
        loadSettings();
        connectActions();
    }

    public void loadSettings() {
        if (preferences == null) {
            preferences = Preferences.userNodeForPackage(MainWindow.class);
        }

        addresDB.setText(getAddresDB());
        isLocalDB.setSelected(isLocalDB());
        nameDB.setText(getNameDB());
        loginDB.setText(getLoginDB());
        passwordDB.setText(getPasswordDB());
        mysqldumpTxt.setText(getDumpexe());
    }

    private void saveSettings() {
        preferences.put(ADDRES_DB, addresDB.getText());
        preferences.put(ISLOCAL_DB, String.valueOf(isLocalDB.isSelected()));
        preferences.put(NAME_DB, nameDB.getText());
        preferences.put(LOGIN_DB, loginDB.getText());
        preferences.put(PASSWORD_DB, passwordDB.getText());
        preferences.put(DUMPEXE, mysqldumpTxt.getText());
    }

    private void connectActions() {
        saveSettings.setOnAction(event -> {
            saveSettings();
            loadSettings();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(thisWindowStage.getIcons().get(0));
            stage.getScene().getStylesheets().add(thisWindowStage.getScene().getStylesheets().get(0));
            alert.setHeaderText("ИНФОРМАЦИЯ");
            alert.setTitle("ВНИМАНИЕ");
            alert.setContentText("НАСТРОЙКИ БУДУТ ПРИМЕНЕНЫ ПОСЛЕ ПЕРЕЗАПУСКА ПРОГРАММЫ");
            alert.showAndWait();
            thisWindowStage.hide();
        });

        isLocalDB.setOnAction(event -> {
            if (isLocalDB.isSelected()) {
                addresDB.setDisable(true);
            }
            else {
                addresDB.setDisable(false);
            }
        });

        selDumpExeBt.setOnAction(event -> {

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Исполняемый файл(exe)", "*.exe"));
            File f = fileChooser.showOpenDialog(thisWindowStage);
            if (f != null) mysqldumpTxt.setText(f.getAbsolutePath());
        });

        exitBt.setOnAction(event -> System.exit(0));
    }

    public String getAddresDB() {
        String s = preferences.get(ADDRES_DB, "");
        return s;
    }

    public Boolean isLocalDB() {
        Boolean value = Boolean.valueOf(preferences.get(ISLOCAL_DB, "false"));
        return value;
    }

    public String getNameDB() {
        String s = preferences.get(NAME_DB, "");
        return s;
    }

    public String getLoginDB() {
        String s = preferences.get(LOGIN_DB, "");
        return s;
    }

    public String getPasswordDB() {
        String s = preferences.get(PASSWORD_DB, "");
        return s;
    }

    public String getDumpexe() {
        String s = preferences.get(DUMPEXE, "");
        return s;
    }

    @Override
    public void updateData() {

    }

    @Override
    public void setData(AbstrDoc abstrDoc) {

    }

    @Override
    public void setStage(Stage thisWindowStage) {
        this.thisWindowStage = thisWindowStage;
    }

    public Stage getStage() {
        return thisWindowStage;
    }

    @Override
    public void setAbstrWindow(AbstrWindow abstrWindow) {

    }

    public void setHideExitBt(boolean isHide) {
        exitBt.setVisible(!isHide);
    }

    @FXML
    private TextField addresDB;
    @FXML
    private CheckBox isLocalDB;
    @FXML
    private TextField nameDB;
    @FXML
    private TextField loginDB;
    @FXML
    private TextField passwordDB;
    @FXML
    private Button saveSettings;
    private Stage thisWindowStage;
    @FXML
    private Button selDumpExeBt;
    @FXML
    private TextField mysqldumpTxt;
    @FXML
    private Button exitBt;

    private Preferences preferences;
    private static final String ADDRES_DB = "ADDRES_DB";
    private static  final String NAME_DB = "NAME_DB";
    private static final String ISLOCAL_DB = "ISLOCAL_DB";
    private static final String LOGIN_DB = "LOGIN_DB";
    private static final String PASSWORD_DB = "PASSWORD_DB";
    private static final String DUMPEXE = "DUMPEXE";
}
