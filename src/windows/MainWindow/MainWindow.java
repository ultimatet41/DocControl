package windows.MainWindow;

import data.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import utils.ComboBoxAutoComplid;
import utils.DBControl;
import utils.InDocReport;
import utils.OutDocReport;
import windows.AbstrWindow;
import windows.InDocWindow.InDocWindow;
import windows.OutDocWindow.OutDocWindow;
import windows.SettingsWindow.SettingsWindow;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class MainWindow extends AbstrWindow {

    @FXML
    public void initialize() {
        createSettingsWindow();
        initInDocTable();
        initOutDocTable();
        initDatePattern();
        loadDataInTables();
        initContextMenu();
        connectActions();
        fInDoc.setToggleGroup(group);
        fInDoc.setSelected(true);
        fOutDoc.setToggleGroup(group);
        addImageOnButton();
        fDescTxt.setWrapText(true);
        fOtherDataTxt.setWrapText(true);
        ComboBoxAutoComplid.autoCompleteComboBoxPlus(fAbonentBox, (typedText, objectToCompare) -> objectToCompare.getNameAbonent().toLowerCase().contains(typedText.toLowerCase()));
    }

    private void addImageOnButton() {
        Image image = new Image(this.getClass().getResourceAsStream("add.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        imageView.setPreserveRatio(true);
        inDocAddBt.setGraphic(imageView);

        ImageView imageView1 = new ImageView(image);
        imageView1.setFitWidth(30);
        imageView1.setFitHeight(30);
        imageView1.setPreserveRatio(true);
        outDocAddBt.setGraphic(imageView1);

        Image findImg = new Image(this.getClass().getResourceAsStream("find.png"));
        ImageView findIV = new ImageView(findImg);
        findIV.setFitWidth(30);
        findIV.setFitHeight(30);
        findIV.setPreserveRatio(true);
        findBt.setGraphic(findIV);

        Image clearImg = new Image(this.getClass().getResourceAsStream("clear.png"));
        ImageView clearIV = new ImageView(clearImg);
        clearIV.setFitWidth(30);
        clearIV.setFitHeight(30);
        clearIV.setPreserveRatio(true);
        clearFindBt.setGraphic(clearIV);

        Image repoImg = new Image(this.getClass().getResourceAsStream("repo.png"));
        ImageView repoIV = new ImageView(repoImg);
        repoIV.setFitWidth(30);
        repoIV.setFitHeight(30);
        repoIV.setPreserveRatio(true);
        createRepoBt.setGraphic(repoIV);

//        Image calendImg = new Image(this.getClass().getResourceAsStream("calendar.png"));
//        ImageView calendIV = new ImageView(calendImg);
//        calendIV.setFitWidth(30);
//        calendIV.setFitHeight(30);
//        calendIV.setPreserveRatio(true);
//        setDateBt.setGraphic(calendIV);

        Image indImg = new Image(this.getClass().getResourceAsStream("in.png"));
        ImageView inIV = new ImageView(indImg);
        inIV.setFitWidth(30);
        inIV.setFitHeight(30);
        inIV.setPreserveRatio(true);
        fInDoc.setGraphic(inIV);

        Image outImg = new Image(this.getClass().getResourceAsStream("out.png"));
        ImageView outIV = new ImageView(outImg);
        outIV.setFitWidth(30);
        outIV.setFitHeight(30);
        outIV.setPreserveRatio(true);
        fOutDoc.setGraphic(outIV);
    }

    private void initContextMenu() {
        inDocContMenu = new ContextMenu();
        addItemInDoc = new MenuItem("Добавить");
        inDocContMenu.getItems().add(addItemInDoc);
        inDocTable.setContextMenu(inDocContMenu);
    }


    private void initInDocTable() {
        inNumInDocCl = new TableColumn<>("ВХ. НОМЕР");
        inNumInDocCl.setCellValueFactory(new PropertyValueFactory<>("inNum"));
        currNumInDocCl = new TableColumn<>("НОМЕР");
        currNumInDocCl.setCellValueFactory(new PropertyValueFactory<>("currNum"));
        currNumInDocCl.setMaxWidth(2000);
        dateInDocCl = new TableColumn<>("ДАТА РЕГ.");
        dateInDocCl.setCellValueFactory(new PropertyValueFactory<>("dateDc"));
        dateInDocCl.setMaxWidth(3000);
        descInDocCl = new TableColumn<>("ОПИСАНИЕ");
        descInDocCl.setCellValueFactory(new PropertyValueFactory<>("descInDoc"));
        abonnentsInDocCl = new TableColumn<>("ПРИШЛО ОТ");
        abonnentsInDocCl.setCellValueFactory(new PropertyValueFactory<>("abonnents"));
        inDocTable.getColumns().addAll(currNumInDocCl, dateInDocCl, inNumInDocCl, descInDocCl, abonnentsInDocCl);
    }

    private void connectDB() {
        try {
            if (settingsWindow.isLocalDB()) {
                DBControl.connectDB(DBControl.LOCAL_ADDRESS, settingsWindow.getNameDB(), settingsWindow.getLoginDB(), settingsWindow.getPasswordDB());
            }
            else {
                DBControl.connectDB(settingsWindow.getAddresDB(), settingsWindow.getNameDB(), settingsWindow.getLoginDB(), settingsWindow.getPasswordDB());
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("ВНИМАНИЕ", "ОШИБКА ПОДКЛЮЧЕНИЯ",
                    "НЕ ВОЗМОЖНО ПОДКЛЮЧИТСЯ К БД. ПРОВЕРЬТЕ НАСТРОЙКИ ПОДКЛЮЧЕНИЯ", Alert.AlertType.WARNING);
            settingsWindow.getStage().showAndWait();
            connectDB();
        }
    }

    private void initOutDocTable() {
        numOutDocCl = new TableColumn<>("НОМЕР");
        numOutDocCl.setCellValueFactory(new PropertyValueFactory<>("numDoc"));
        numOutDocCl.setMaxWidth(2000);
        dateOutDocCl = new TableColumn<>("ДАТА РЕГ.");
        dateOutDocCl.setCellValueFactory(new PropertyValueFactory<>("dateDc"));
        dateOutDocCl.setMaxWidth(3000);
        descOutDocCL = new TableColumn<>("ОПИСАНИЕ");
        descOutDocCL.setCellValueFactory(new PropertyValueFactory<>("descOutDoc"));
        abonnentsOutDocCl = new TableColumn<>("КОМУ ОТПР.");
        abonnentsOutDocCl.setCellValueFactory(new PropertyValueFactory<>("abonnents"));
        outDocTable.getColumns().addAll(numOutDocCl, dateOutDocCl, descOutDocCL, abonnentsOutDocCl);
    }

    private void initDatePattern() {
        String pattern = "yyyy-MM-dd";
        startDate.setPromptText(pattern.toLowerCase());
        endDate.setPromptText(pattern.toLowerCase());
        startDate.setValue(LocalDate.now());
        endDate.setValue(LocalDate.now());
        fStartDatePk.setPromptText(pattern.toLowerCase());
        fEndDatePk.setPromptText(pattern.toLowerCase());
    }

    private void connectActions() {
        addItemInDoc.setOnAction(event -> addNewInDoc());

//        setDateBt.setOnAction(event -> loadDataInTables());

        startDate.valueProperty().addListener((observable, oldValue, newValue) -> loadDataInTables());

        endDate.valueProperty().addListener((observable, oldValue, newValue) -> loadDataInTables());

        inDocTable.setRowFactory(new Callback<TableView<InDocMW>, TableRow<InDocMW>>() {
            @Override
            public TableRow<InDocMW> call(TableView<InDocMW> param) {
                TableRow<InDocMW> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2 && (!row.isEmpty())) {
                        InDoc doc = row.getItem().getInDoc();
                        Parent root;
                        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("windows/InDocWindow/InDocWindow.fxml"));
                        try {
                            root = loader.load();
                            InDocWindow inDocWindow = loader.getController();
                            inDocWindow.setData(doc);
                            inDocWindow.setAbstrWindow(MainWindow.this);
                            Stage stage = new Stage();
                            stage.setTitle("ВХОДЯЩИЙ ДОКУМЕНТ: №" + doc.getCurrNum());
                            Scene scene = new Scene(root, 800, 700);
                            scene.getStylesheets().add(thisStage.getScene().getStylesheets().get(0));
                            stage.setScene(scene);
                            stage.getIcons().add(thisStage.getIcons().get(0));
                            inDocWindow.setStage(stage);
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                return row;
            }
        });

        inDocAddBt.setOnAction(event -> addNewInDoc());

        outDocTable.setRowFactory(new Callback<TableView<OutDocMW>, TableRow<OutDocMW>>() {
            @Override
            public TableRow<OutDocMW> call(TableView<OutDocMW> param) {
                TableRow<OutDocMW> row = new TableRow<>(); row.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2 && (!row.isEmpty())) {
                        OutDoc doc = row.getItem().getOutDoc();
                        Parent root;
                        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("windows/OutDocWindow/OutDocWindow.fxml"));
                        try {
                            root = loader.load();
                            OutDocWindow outDocWindow = loader.getController();
                            outDocWindow.setData(doc);
                            outDocWindow.setAbstrWindow(MainWindow.this);
                            Stage stage = new Stage();
                            stage.setTitle("ИСХОДЯЩИЙ ДОКУМЕНТ: №" + doc.getNumDoc());
                            Scene scene = new Scene(root, 800, 700);
                            scene.getStylesheets().add(thisStage.getScene().getStylesheets().get(0));
                            stage.setScene(scene);
                            stage.getIcons().add(thisStage.getIcons().get(0));
                            outDocWindow.setStage(stage);
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                return row;
            }
        });

        outDocAddBt.setOnAction(event -> {
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("windows/OutDocWindow/OutDocWindow.fxml"));
            try {
                root = loader.load();
                OutDocWindow outDocWindow = loader.getController();
                outDocWindow.setData(null);
                outDocWindow.setAbstrWindow(MainWindow.this);
                Stage stage = new Stage();
                stage.setTitle("НОВЫЙ ИСХОДЯЩИЙ ДОКУМЕНТ");
                Scene scene = new Scene(root, 800, 700);
                scene.getStylesheets().add(thisStage.getScene().getStylesheets().get(0));
                stage.setScene(scene);
                stage.getIcons().add(thisStage.getIcons().get(0));
                outDocWindow.setStage(stage);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        findBt.setOnAction(event -> finder());

        fOutDoc.setOnAction(event -> inNumDocTxt.setDisable(true));

        fInDoc.setOnAction(event -> inNumDocTxt.setDisable(false));

        clearFindBt.setOnAction(event -> {
            fEndDatePk.setValue(null);
            fStartDatePk.setValue(null);
            numDocTxt.clear();
            inNumDocTxt.clear();
            fDescTxt.clear();
            fOtherDataTxt.clear();
            loadDataInTables();
        });

        closeItemMenu.setOnAction(event -> Platform.exit());

        settingsBDItemMenu.setOnAction(event -> {
            settingsWindow.loadSettings();
            settingsWindow.getStage().getIcons().add(thisStage.getIcons().get(0));
            settingsWindow.setHideExitBt(true);
            settingsWindow.getStage().show();
        });

        aboutItemMenu.setOnAction(event -> {
            showAlert("ИНФОРМАЦИЯ", "О ПРОГРАММЕ. ВЕРСИЯ 1.6",
                    "РАЗРАБОТАНО СПЕЦИАЛЬНО ДЛЯ АДМИНИСТРАЦИИ МР \"СПАС-ДЕМЕНСКИЙ РАЙОН\". КОНОВАЛОВ К.В.",
                    Alert.AlertType.INFORMATION);
        });

        createRepoBt.setOnAction(event -> {
            try {
                DirectoryChooser chooser = new DirectoryChooser();
                chooser.setTitle("Выбор каталога для отчёта");
                File selDir = chooser.showDialog(null);
                if (selDir != null) {
                    InDocReport.createReport(DBControl.InDoc.getFromDate(startDate.getValue().toString(), endDate.getValue().toString()),
                            selDir + File.separator + "Входящие.xls");
                    OutDocReport.createReport(DBControl.OutDoc.getFromDate(startDate.getValue().toString(), endDate.getValue().toString()),
                            selDir + File.separator + "Исходящие.xls");
                    showAlert("ОТЧЕТ", "ИНФОРМАЦИЯ", "ОТЧЕТЫ СФОРМИРОВАННЫ", Alert.AlertType.INFORMATION);
                    try {
                        Desktop.getDesktop().open(selDir);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }  catch (SQLException e) {
                e.printStackTrace();
            }
        });

        createDumpDB.setOnAction(event -> {
            DirectoryChooser chooser = new DirectoryChooser();
            File dir = chooser.showDialog(thisStage);
            if (dir != null) {
                if (settingsWindow.getDumpexe() != null && !settingsWindow.getDumpexe().isEmpty()) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm");
                        String dateTime = LocalDateTime.now().format(formatter);

                        String prog = "cmd.exe /c \"" + settingsWindow.getDumpexe() +"\"" + " -u " + settingsWindow.getLoginDB() +
                                " -p" + settingsWindow.getPasswordDB() + " " + settingsWindow.getNameDB() +
                                " > ";
                        String fullFileName = dir.getAbsolutePath() +File.separator + dateTime + ".sql";
                        System.out.println(prog + fullFileName);
                    Platform.runLater(() -> {
                        try {
                            Runtime.getRuntime().exec(prog + fullFileName).waitFor();
                        } catch (InterruptedException | IOException e) {
                            e.printStackTrace();
                        }
                        if (new File(fullFileName).exists()) {
                            showAlert("ОТЧЕТ", "ИНФОРМАЦИЯ", "РЕЗЕРВНАЯ КОПИЯ СОЗДАНА: (" + fullFileName + ")",
                                    Alert.AlertType.INFORMATION);
                        } else {
                            showAlert("ОТЧЕТ", "ПРЕДУПРЕЖДЕНИЕ", "РЕЗЕРВНАЯ КОПИЯ НЕ СОЗДАНА!", Alert.AlertType.WARNING);
                            }
                        });
                }
                else {
                    showAlert("ОТЧЕТ", "ИНФОРМАЦИЯ", "НЕ ДОСТАТОЧНО ПАРАМЕТРОВ!", Alert.AlertType.INFORMATION);
                }
            }
        });
    }

    private void addNewInDoc() {
        Parent root;
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("windows/InDocWindow/InDocWindow.fxml"));
        try {
            root = loader.load();
            InDocWindow inDocWindow = loader.getController();
            inDocWindow.setData(null);
            inDocWindow.setAbstrWindow(MainWindow.this);
            Stage stage = new Stage();
            stage.setTitle("НОВЫЙ ВХОДЯЩИЙ ДОКУМЕНТ");
            Scene scene = new Scene(root, 800, 700);
            scene.getStylesheets().add(thisStage.getScene().getStylesheets().get(0));
            stage.setScene(scene);
            stage.getIcons().add(thisStage.getIcons().get(0));
            inDocWindow.setStage(stage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void finder() {
        HashMap<String, String> param = new HashMap<>();
        if(fInDoc.isSelected()) {
            if ((fStartDatePk.getValue() != null && !fStartDatePk.getValue().toString().isEmpty()) &&
                    (fEndDatePk.getValue() != null && !fEndDatePk.getValue().toString().isEmpty())) {
                if (fEndDatePk.getValue().compareTo(fStartDatePk.getValue()) < 0) {
                    showAlert("ВНИМАНИЕ!", "ОШИБКА ДАТЫ", "Начальная дата не может быть больше конечной!",
                            Alert.AlertType.WARNING);
                    return;
                }
                param.put(DBControl.START_DATE, fStartDatePk.getValue().toString());
                param.put(DBControl.END_DATE, fEndDatePk.getValue().toString());
            }
            if (!numDocTxt.getText().isEmpty()) {
                param.put(DBControl.NUM_DOC, numDocTxt.getText());
            }
            if (!inNumDocTxt.getText().isEmpty()) {
                param.put(DBControl.NUM_INDOC, inNumDocTxt.getText());
            }

            if (!fDescTxt.getText().isEmpty()) {
                param.put(DBControl.DESC_DOC, fDescTxt.getText());
            }

            if (!fOtherDataTxt.getText().isEmpty()) {
                param.put(DBControl.OTHER_DATA_DOC, fOtherDataTxt.getText());
            }
            if (fAbonentBox.getSelectionModel().getSelectedItem() != null) {
                Abonent abonent = ComboBoxAutoComplid.getComboBoxValue(fAbonentBox);
                assert abonent != null;
                param.put(DBControl.ID_ABONENT, String.valueOf(abonent.getIdAbonent()));
            }
            try {
                if (param.size() == 0) {
                    showAlert("ПОИСК", "ИНФОРМАЦИЯ", "НЕ ВЫБРАН ПАРАМЕТР ПОИСКА", Alert.AlertType.INFORMATION);
                    return;
                }
                dataInDoc.clear();
                dataInDoc.addAll(convertToInDocMW(DBControl.InDoc.findDocs(param)));
                inDocTable.setItems(dataInDoc);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else if (fOutDoc.isSelected()) {
            if ((fStartDatePk.getValue() != null && !fStartDatePk.getValue().toString().isEmpty()) &&
                    (fEndDatePk.getValue() != null && !fEndDatePk.getValue().toString().isEmpty())) {
                if (fEndDatePk.getValue().compareTo(fStartDatePk.getValue()) < 0) {
                    showAlert("ВНИМАНИЕ", "ОШИБКА ДАТЫ", "Начальная дата не может быть больше конечной!",
                            Alert.AlertType.WARNING);
                    return;
                }
                param.put(DBControl.START_DATE, fStartDatePk.getValue().toString());
                param.put(DBControl.END_DATE, fEndDatePk.getValue().toString());
            }
            if (!numDocTxt.getText().isEmpty()) {
                param.put(DBControl.NUM_DOC, numDocTxt.getText());
            }

            if (!fDescTxt.getText().isEmpty()) {
                param.put(DBControl.DESC_DOC, fDescTxt.getText());
            }

            if (!fOtherDataTxt.getText().isEmpty()) {
                param.put(DBControl.OTHER_DATA_DOC, fOtherDataTxt.getText());
            }
            if (fAbonentBox.getSelectionModel().getSelectedItem() != null) {
                Abonent abonent = ComboBoxAutoComplid.getComboBoxValue(fAbonentBox);
                assert abonent != null;
                param.put(DBControl.ID_ABONENT, String.valueOf(abonent.getIdAbonent()));
            }
            try {
                if (param.size() == 0) {
                    showAlert("ПОИСК", "ИНФОРМАЦИЯ", "НЕ ВЫБРАН ПАРАМЕТР ПОИСКА", Alert.AlertType.INFORMATION);
                    return;
                }
                dataOutDoc.clear();
                dataOutDoc.addAll(convertToOutDocMW(DBControl.OutDoc.findDocs(param)));
                outDocTable.setItems(dataOutDoc);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    private void loadDataInTables() {
        if (endDate.getValue().compareTo(startDate.getValue()) < 0) {
            showAlert("ВНИМАНИЕ", "ОШИБКА ДАТЫ", "Начальная дата не может быть больше конечной!",
                    Alert.AlertType.WARNING);
            return;
        }
        String start = startDate.getValue().toString();
        String end = endDate.getValue().toString();

        try {
            dataInDoc.clear();
            dataInDoc.addAll(convertToInDocMW(DBControl.InDoc.getFromDate(start, end)));
            inDocTable.setItems(dataInDoc);

            dataOutDoc.clear();
            dataOutDoc.addAll(convertToOutDocMW(DBControl.OutDoc.getFromDate(start, end)));
            outDocTable.setItems(dataOutDoc);
            abonents.clear();
            abonents.add(null);
            abonents.addAll(DBControl.Abonent.getAll());
            fAbonentBox.setItems(abonents);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void updateData() {
        loadDataInTables();
    }

    private void showAlert(String headText, String titleText, String contentText, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(thisStage.getIcons().get(0));
        stage.getScene().getStylesheets().add(thisStage.getScene().getStylesheets().get(0));
        alert.setHeaderText(headText);
        alert.setTitle(titleText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
    public void setData(AbstrDoc abstrDoc) {

    } //not use

    @Override
    public void setStage(Stage thisWindowStage) {
        thisStage = thisWindowStage;
    }

    @Override
    public void setAbstrWindow(AbstrWindow abstrWindow) {

    } //not use

    private ArrayList<InDocMW> convertToInDocMW(ArrayList<InDoc> inDocs) {
        ArrayList<InDocMW> inDocMWS = new ArrayList<>(inDocs.size());
        for (InDoc inDoc : inDocs) {
            inDocMWS.add(new InDocMW(inDoc));
        }
        return inDocMWS;
    }

    private ArrayList<OutDocMW> convertToOutDocMW(ArrayList<OutDoc> outDocs) {
        ArrayList<OutDocMW> outDocMWS = new ArrayList<>(outDocs.size());
        for (OutDoc outDoc : outDocs) {
            outDocMWS.add(new OutDocMW(outDoc));
        }
        return outDocMWS;
    }

    private void createSettingsWindow() {
        if (settingsWindow == null) {
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("windows/SettingsWindow/SettingsWindow.fxml"));
            try {
                root = loader.load();
                settingsWindow = loader.getController();
                settingsWindow.setAbstrWindow(MainWindow.this);
                Stage stage = new Stage();
                stage.setTitle("НАСТРОЙКИ");
                Scene scene = new Scene(root, 800, 200);
                scene.getStylesheets().add(getClass().getClassLoader().getResource("resources/bootstrap2.css").toExternalForm());
                stage.setScene(scene);
                settingsWindow.setStage(stage);
                if (settingsWindow.getNameDB().isEmpty()) {
                    stage.showAndWait();
                    System.out.println("show");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            settingsWindow.getStage().showAndWait();
        }
        connectDB();
    }

    @FXML
    private DatePicker startDate;

    @FXML
    private DatePicker endDate;

//    @FXML
//    private Button setDateBt;

    @FXML
    private  Button inDocAddBt;

    @FXML
    private Button outDocAddBt;

    @FXML
    private TableView<InDocMW> inDocTable;

    @FXML
    private TableColumn<InDocMW, String> inNumInDocCl;
    @FXML
    private TableColumn<InDocMW, String> currNumInDocCl;
    @FXML
    private TableColumn<InDocMW, String> dateInDocCl;
    @FXML
    private TableColumn<InDocMW, String> descInDocCl;
    @FXML
    private TableColumn<InDocMW, String> abonnentsInDocCl;
    @FXML
    private ObservableList<InDocMW> dataInDoc = FXCollections.observableArrayList();

    @FXML
    private TableView<OutDocMW> outDocTable;
    @FXML
    private TableColumn<OutDocMW, String> numOutDocCl;
    @FXML
    private TableColumn<OutDocMW, String> dateOutDocCl;
    @FXML
    private TableColumn<OutDocMW, String> descOutDocCL;
    @FXML
    private TableColumn<OutDocMW, String> abonnentsOutDocCl;
    @FXML
    private ObservableList<OutDocMW> dataOutDoc = FXCollections.observableArrayList();
    @FXML
    private RadioButton fInDoc;
    @FXML
    private RadioButton fOutDoc;
    @FXML
    private DatePicker fStartDatePk;
    @FXML
    private DatePicker fEndDatePk;
    @FXML
    private TextField numDocTxt;
    @FXML
    private TextField inNumDocTxt;
    @FXML
    private Button findBt;
    @FXML
    private Button clearFindBt;
    @FXML
    private MenuItem createDumpDB;
    @FXML
    private MenuItem closeItemMenu;
    @FXML
    private MenuItem settingsBDItemMenu;
    @FXML
    private MenuItem aboutItemMenu;
    @FXML
    private Button createRepoBt;
    @FXML
    private TextArea fDescTxt;
    @FXML
    private TextArea fOtherDataTxt;
    private ContextMenu inDocContMenu;
    private MenuItem addItemInDoc;
    @FXML
    private ComboBox<Abonent> fAbonentBox;
    private ObservableList<Abonent> abonents = FXCollections.observableArrayList();

    private Stage thisStage;

    private ToggleGroup group = new ToggleGroup();
    private SettingsWindow settingsWindow;

}
