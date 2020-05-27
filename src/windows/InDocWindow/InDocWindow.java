package windows.InDocWindow;

import data.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import utils.DBControl;
import windows.AbonentWindow.AbonentWindow;
import windows.AbstrWindow;
import windows.FindWindow.FindWindow;
import windows.OutDocWindow.OutDocWindow;
import windows.PersonWindow.PersonWindow;
import windows.SysTrfWindow.SysTrfWindow;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class InDocWindow extends AbstrWindow {

    @FXML
    public void initialize() {
        initAbonentTable();
        initOutDocTable();
        initPersonTable();
        initSysTrfTable();
        connectActions();
        descTxt.setWrapText(true);
        otherDataTxt.setWrapText(true);
    }

    private void initAbonentTable() {
        abonentNameCl = new TableColumn<>("НАИМЕНОВАНИЕ");
        abonentNameCl.setCellValueFactory(new PropertyValueFactory<>("nameAbonent"));
        abonentDescCl = new TableColumn<>("ОПИСАНИЕ");
        abonentDescCl.setCellValueFactory(new PropertyValueFactory<>("descAbonent"));
        abonentTable.getColumns().addAll(abonentNameCl, abonentDescCl);
    }

    private void initOutDocTable() {
        outDocNumCl = new TableColumn<>("НОМЕР");
        outDocNumCl.setCellValueFactory(new PropertyValueFactory<>("numDoc"));
        outDocDateCl = new TableColumn<>("ДАТА");
        outDocDateCl.setCellValueFactory(new PropertyValueFactory<>("dateDc"));
        ouDocDescCl = new TableColumn<>("ОПИСАНИЕ");
        ouDocDescCl.setCellValueFactory(new PropertyValueFactory<>("descOutDoc"));
        outDocTable.getColumns().addAll(outDocNumCl, outDocDateCl, ouDocDescCl);
    }

    private void initSysTrfTable() {
        sysTrfNameCl = new TableColumn<>("НАИМЕНОВАНИЕ");
        sysTrfNameCl.setCellValueFactory(new PropertyValueFactory<>("nameSysTrf"));
        sysTrfDescCl = new TableColumn<>("ОПИСАНИЕ");
        sysTrfDescCl.setCellValueFactory(new PropertyValueFactory<>("descSysTrf"));
        sysTrfTable.getColumns().addAll(sysTrfNameCl, sysTrfDescCl);
    }

    private void initPersonTable() {
        personfNameCl = new TableColumn<>("ИМЯ");
        personfNameCl.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        personlNameCl = new TableColumn<>("ФАМИЛИЯ");
        personlNameCl.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        personPatronCl = new TableColumn<>("ОТЧЕСТВО");
        personPatronCl.setCellValueFactory(new PropertyValueFactory<>("patronPers"));
        personDescCl = new TableColumn<>("ОПИСАНИЕ");
        personDescCl.setCellValueFactory(new PropertyValueFactory<>("descPerson"));
        personTable.getColumns().addAll(personlNameCl, personfNameCl, personPatronCl, personDescCl);
    }

    private void loadAbonentData() {
        try {
            abonentData.clear();
            abonentData.addAll(DBControl.AbonentLink.getOfDoc(inDoc));
            abonentTable.setItems(abonentData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadOutDocData() {
        try {
            outDocData.clear();
            for (AbstrDoc outDoc : DBControl.DocLink.getOutDocs(inDoc)) {
                outDocData.add((OutDoc) outDoc);
                System.out.println("add outdoc");
            }
            outDocTable.setItems(outDocData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadSysTrfData() {
        sysTrfData.clear();
        try {
            sysTrfData.addAll(DBControl.SysTrfLink.getOfDoc(inDoc));
            sysTrfTable.setItems(sysTrfData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadPersonData() {
        personData.clear();
        try {
            personData.addAll(DBControl.PersonLink.getOfDoc(inDoc));
            personTable.setItems(personData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void connectActions() {
        addAbonentBt.setOnAction(event -> {
            if (!isCreatedDoc()) {
                showAlert("ВНИМАНИЕ", "ОШИБКА ДАННЫХ", "СНАЧАЛА НУЖНО СОХРАНИТЬ ДОКУМЕНТ", Alert.AlertType.WARNING);
                return;
            }
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("windows/AbonentWindow/AbonentWindow.fxml"));
            try {
                root = loader.load();
                AbonentWindow abonentWindow = loader.getController();
                abonentWindow.setAbstrWindow(InDocWindow.this);
                abonentWindow.setData(inDoc);
                Stage stage = new Stage();
                stage.setTitle("АБОНЕНТЫ");
                Scene scene = new Scene(root, 800, 700);
                scene.getStylesheets().add(thisWindowStage.getScene().getStylesheets().get(0));
                stage.setScene(scene);
                stage.getIcons().add(thisWindowStage.getIcons().get(0));
                abonentWindow.setStage(stage);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        delAbonentBt.setOnAction(event -> {
            Abonent abonent = abonentTable.getSelectionModel().getSelectedItem();
            if (abonent == null) return;
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(thisWindowStage.getIcons().get(0));
            stage.getScene().getStylesheets().add(thisWindowStage.getScene().getStylesheets().get(0));
            alert.setHeaderText("ЗАПРОС");
            alert.setTitle("ПОДТВЕРЖДЕНИЕ УДАЛЕНИЯ");
            alert.setContentText("ВЫ ДЕЙСТВИТЕЛЬНО ХОТИТЕ УДАЛИТЬ ДАННУЮ ЗАПИСЬ? " +
                    "ПОСЛЕ ПОДТВЕРЖДЕНИЯ ИЗМЕНЕНИЯ ПРОИЗОЙДУТ СРАЗУ!");
            alert.showAndWait();
            if (alert.getResult().getButtonData().isCancelButton()) return;
            try {
                DBControl.AbonentLink.delete(inDoc, abonent);
                updateData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        addSysTrfBt.setOnAction(event -> {
            if (!isCreatedDoc()) {
                showAlert("ВНИМАНИЕ", "ОШИБКА ДАННЫХ", "СНАЧАЛА НУЖНО СОХРАНИТЬ ДОКУМЕНТ", Alert.AlertType.WARNING);
                return;
            }
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("windows/SysTrfWindow/SysTrfWindow.fxml"));
            try {
                root = loader.load();
                SysTrfWindow sysTrfWindow = loader.getController();
                sysTrfWindow.setAbstrWindow(InDocWindow.this);
                sysTrfWindow.setData(inDoc);
                Stage stage = new Stage();
                stage.setTitle("СИСТЕМЫ ПЕРЕДАЧИ ИНФОРРМАЦИИ");
                Scene scene = new Scene(root, 800, 700);
                scene.getStylesheets().add(thisWindowStage.getScene().getStylesheets().get(0));
                stage.setScene(scene);
                stage.getIcons().add(thisWindowStage.getIcons().get(0));
                sysTrfWindow.setStage(stage);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        delSysTrfBt.setOnAction(event -> {
            SystemTransfer transfer = sysTrfTable.getSelectionModel().getSelectedItem();
            if (transfer == null) return;
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(thisWindowStage.getIcons().get(0));
            stage.getScene().getStylesheets().add(thisWindowStage.getScene().getStylesheets().get(0));
            alert.setHeaderText("ЗАПРОС");
            alert.setTitle("ПОДТВЕРЖДЕНИЕ УДАЛЕНИЯ");
            alert.setContentText("ВЫ ДЕЙСТВИТЕЛЬНО ХОТИТЕ УДАЛИТЬ ДАННУЮ ЗАПИСЬ? " +
                    "ПОСЛЕ ПОДТВЕРЖДЕНИЯ ИЗМЕНЕНИЯ ПРОИЗОЙДУТ СРАЗУ!");
            alert.showAndWait();
            if (alert.getResult().getButtonData().isCancelButton()) return;
            try {
                DBControl.SysTrfLink.delete(inDoc, transfer);
                updateData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        addPersonBt.setOnAction(event -> {
            if (!isCreatedDoc()) {
                showAlert("ВНИМАНИЕ", "ОШИБКА ДАННЫХ", "СНАЧАЛА НУЖНО СОХРАНИТЬ ДОКУМЕНТ", Alert.AlertType.WARNING);
                return;
            }
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("windows/PersonWindow/PersonWindow.fxml"));
            try {
                root = loader.load();
                PersonWindow personWindow = loader.getController();
                personWindow.setAbstrWindow(InDocWindow.this);
                personWindow.setData(inDoc);
                Stage stage = new Stage();
                stage.setTitle("СОТРУДНИКИ");
                Scene scene = new Scene(root, 800, 700);
                scene.getStylesheets().add(thisWindowStage.getScene().getStylesheets().get(0));
                stage.setScene(scene);
                stage.getIcons().add(thisWindowStage.getIcons().get(0));
                personWindow.setStage(stage);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        delPersonBt.setOnAction(event -> {
            Person person = personTable.getSelectionModel().getSelectedItem();
            if (person == null) return;
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(thisWindowStage.getIcons().get(0));
            stage.getScene().getStylesheets().add(thisWindowStage.getScene().getStylesheets().get(0));
            alert.setHeaderText("ЗАПРОС");
            alert.setTitle("ПОДТВЕРЖДЕНИЕ УДАЛЕНИЯ");
            alert.setContentText("ВЫ ДЕЙСТВИТЕЛЬНО ХОТИТЕ УДАЛИТЬ ДАННУЮ ЗАПИСЬ? " +
                    "ПОСЛЕ ПОДТВЕРЖДЕНИЯ ИЗМЕНЕНИЯ ПРОИЗОЙДУТ СРАЗУ!");
            alert.showAndWait();
            if (alert.getResult().getButtonData().isCancelButton()) return;
            try {
                DBControl.PersonLink.delete(inDoc, person);
                updateData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        saveBt.setOnAction(event -> saveDocument());

        closeBt.setOnAction(event -> thisWindowStage.hide());

        addOutDocBt.setOnAction(event -> {
            if (!isCreatedDoc()) {
                showAlert("ВНИМАНИЕ", "ОШИБКА ДАННЫХ", "СНАЧАЛА НУЖНО СОХРАНИТЬ ДОКУМЕНТ", Alert.AlertType.WARNING);
                return;
            }
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("windows/FindWindow/FindWindow.fxml"));
            try {
                root = loader.load();
                FindWindow findWindow = loader.getController();
                findWindow.setAbstrWindow(InDocWindow.this);
                findWindow.setData(inDoc);
                Stage stage = new Stage();
                stage.setTitle("ПОИСК ДОКУМЕНТОВ");
                Scene scene = new Scene(root, 800, 700);
                scene.getStylesheets().add(thisWindowStage.getScene().getStylesheets().get(0));
                stage.setScene(scene);
                stage.getIcons().add(thisWindowStage.getIcons().get(0));
                findWindow.setStage(stage);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        delOutDocBt.setOnAction(event -> {
            OutDoc outDoc = outDocTable.getSelectionModel().getSelectedItem();
            if (outDoc == null) return;
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(thisWindowStage.getIcons().get(0));
            stage.getScene().getStylesheets().add(thisWindowStage.getScene().getStylesheets().get(0));
            alert.setHeaderText("ЗАПРОС");
            alert.setTitle("ПОДТВЕРЖДЕНИЕ УДАЛЕНИЯ");
            alert.setContentText("ВЫ ДЕЙСТВИТЕЛЬНО ХОТИТЕ УДАЛИТЬ ДАННУЮ ЗАПИСЬ? " +
                    "ПОСЛЕ ПОДТВЕРЖДЕНИЯ ИЗМЕНЕНИЯ ПРОИЗОЙДУТ СРАЗУ!");
            alert.showAndWait();
            if (alert.getResult().getButtonData().isCancelButton()) return;
            try {
                DBControl.DocLink.delete(inDoc, outDoc);
                updateData();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });

        outDocTable.setRowFactory(new Callback<TableView<OutDoc>, TableRow<OutDoc>>() {
            @Override
            public TableRow<OutDoc> call(TableView<OutDoc> param) {
                TableRow<OutDoc> row = new TableRow<>(); row.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2 && (!row.isEmpty())) {
                        OutDoc doc = row.getItem();
                        Parent root;
                        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("windows/OutDocWindow/OutDocWindow.fxml"));
                        try {
                            root = loader.load();
                            OutDocWindow outDocWindow = loader.getController();
                            outDocWindow.setData(doc);
                            outDocWindow.setAbstrWindow(InDocWindow.this);
                            Stage stage = new Stage();
                            stage.setTitle("ИСХОДЯЩИЙ ДОКУМЕНТ: №" + doc.getNumDoc());
                            Scene scene = new Scene(root, 800, 700);
                            scene.getStylesheets().add(thisWindowStage.getScene().getStylesheets().get(0));
                            stage.setScene(scene);
                            stage.getIcons().add(thisWindowStage.getIcons().get(0));
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

        deleteDocBt.setOnAction(event -> deleteThisDoc());
    }

    @Override
    public void updateData() {
        if (isCreatedDoc()) {
            loadAbonentData();
            loadOutDocData();
            loadPersonData();
            loadSysTrfData();
            if (abstrWindow != null) {
                abstrWindow.updateData();
            }
        }
    }

    @Override
    public void setData(AbstrDoc abstrDoc) {
        if (abstrDoc != null) {
            this.inDoc = (InDoc) abstrDoc;
            dateRegTxt.setValue(LocalDate.parse(inDoc.getDateDc()));
            inNumTxt.setText(inDoc.getInNum());
            currNumTxt.setText(inDoc.getCurrNum());
            descTxt.setText(inDoc.getDescInDoc());
            dateInPk.setValue(LocalDate.parse(inDoc.getDateIn()));
            otherDataTxt.setText(inDoc.getOtherData());
            updateData();
            deleteDocBt.setDisable(false);
            saveBt.setText("Обновить");
        }
        else {
            this.inDoc = new InDoc(null, null, null, null, null, null, null);
        }
    }

    private void deleteThisDoc() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(thisWindowStage.getIcons().get(0));
        stage.getScene().getStylesheets().add(thisWindowStage.getScene().getStylesheets().get(0));
        alert.setHeaderText("ЗАПРОС НА УДАЛЕНИЕ");
        alert.setTitle("ПОДТВЕРЖДЕНИЕ УДАЛЕНИЯ");
        alert.setContentText("ВЫ ДЕЙСТВИТЕЛЬНО ХОТИТЕ УДАЛИТЬ ДАННЫЙ ДОКУМЕНТ? " +
                "ПОСЛЕ ПОДТВЕРЖДЕНИЯ, УДАЛЕНИЕ ОБРАТИТЬ НЕ ВОЗМОЖНО!");
        alert.showAndWait();
        if (alert.getResult().getButtonData().isCancelButton()) return;
        try {
            for (data.Person person : personData) {
                DBControl.PersonLink.delete(inDoc, person);
            }
            for (data.Abonent abonent : abonentData) {
                DBControl.AbonentLink.delete(inDoc, abonent);
            }
            for (data.SystemTransfer transfer : sysTrfData) {
                DBControl.SysTrfLink.delete(inDoc, transfer);
            }
            for (data.OutDoc outDoc : outDocData) {
                DBControl.DocLink.delete(inDoc, outDoc);
            }
            DBControl.InDoc.delete(inDoc);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        abstrWindow.updateData();
        thisWindowStage.hide();
    }

    private boolean isCreatedDoc() {
        return inDoc.getIdInDoc() != null;
    }

    @Override
    public void setStage(Stage thisWindowStage) {
        this.thisWindowStage = thisWindowStage;
    }

    @Override
    public void setAbstrWindow(AbstrWindow abstrWindow) {
        this.abstrWindow = abstrWindow;
    }

    private void saveDocument() {
        if (dateRegTxt.getValue() != null && !inNumTxt.getText().isEmpty() && !currNumTxt.getText().isEmpty()) {
            inDoc.setInNum(inNumTxt.getText());
            inDoc.setCurrNum(currNumTxt.getText());
            inDoc.setDateDc(dateRegTxt.getValue().toString());
            inDoc.setDateIn(dateInPk.getValue().toString());
            inDoc.setDescInDoc(descTxt.getText());
            inDoc.setOtherData(otherDataTxt.getText());
            if (isCreatedDoc()) {
                try {
                    DBControl.InDoc.update(inDoc);
                    abstrWindow.updateData();
                    showAlert("ИНФОРМАЦИЯ", "ОБНОВЛЕНИЕ ДАННЫХ", "ДАННЫЕ ОБНОВЛЕНЫ", Alert.AlertType.INFORMATION);
                    thisWindowStage.setTitle("ВХОДЯЩИЙ ДОКУМЕНТ: №" + inDoc.getCurrNum());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else try {
                if (DBControl.InDoc.isExist(inDoc)) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(thisWindowStage.getIcons().get(0));
                    stage.getScene().getStylesheets().add(thisWindowStage.getScene().getStylesheets().get(0));
                    alert.setHeaderText("ПРОВЕРКА НА ДУБЛИКАТ");
                    alert.setTitle("ПОДТВЕРЖДЕНИЕ СОХРАНЕНИЯ ДУБЛИКАТА");
                    alert.setContentText("ДОКУМЕНТ С ТАКИМ НОМЕРОМ И ДАТОЙ УЖЕ ЕСТЬ." +
                            "ВЫ ДЕСТВИТЕЛЬНО ХОТИТЕ СОХРАНИТЬ ДУБЛИРУЮЩИЙ ДОКУМЕНТ?");
                    alert.showAndWait();
                    if (alert.getResult().getButtonData().isCancelButton()) return;
                }
                int r = DBControl.InDoc.add(inDoc);
                if (r == DBControl.DATA_IS_NOT_CREATED) {
                    showAlert("ВНИМАНИЕ", "ОШИБКА СОХРАНЕНИЯ", "ВО ВРЕМЯ СОХРАНЕНИЯ ПРОИЗОШЛА ОШИБКА: " + DBControl.DATA_IS_NOT_CREATED,
                            Alert.AlertType.ERROR);
                }
                inDoc = DBControl.InDoc.getFromID(r);
                abstrWindow.updateData();
                showAlert("ИНФОРМАЦИЯ", "СОХРАНЕНИЕ ДАННЫХ", "ДАННЫЕ СОХРАНЕНЫ", Alert.AlertType.INFORMATION);
                thisWindowStage.setTitle("ВХОДЯЩИЙ ДОКУМЕНТ: №" + inDoc.getCurrNum());
                saveBt.setText("Обновить");
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("ВНИМАНИЕ", "ОШИБКА СОХРАНЕНИЯ", "ВО ВРЕМЯ СОХРАНЕНИЯ ПРОИЗОШЛА ОШИБКА",
                        Alert.AlertType.ERROR);
            }
        }
        else {
            showAlert("ВНИМАНИЕ", "ОШИБКА ДАННЫХ", "ЗАПОЛНИТЕ ДАТУ, ВХОДЯЩИЙ НОМЕР И НОМЕР РЕГИСТРАЦИИ",
                    Alert.AlertType.WARNING);
        }
    }

    private void showAlert(String headText, String titleText, String contentText, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(thisWindowStage.getIcons().get(0));
        stage.getScene().getStylesheets().add(thisWindowStage.getScene().getStylesheets().get(0));
        alert.setHeaderText(headText);
        alert.setTitle(titleText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    @FXML
    private DatePicker dateRegTxt;

    @FXML
    private TextField inNumTxt;

    @FXML
    private TextField currNumTxt;

    @FXML
    private TextArea descTxt;
    @FXML
    private TextArea otherDataTxt;

    @FXML
    private TableView<Abonent> abonentTable;
    @FXML
    private TableColumn<Abonent, String> abonentNameCl;
    @FXML
    private TableColumn<Abonent, String> abonentDescCl;
    @FXML
    private ObservableList<Abonent> abonentData = FXCollections.observableArrayList();

    @FXML
    private Button addAbonentBt;
    @FXML
    private Button delAbonentBt;

    @FXML
    private TableView<OutDoc> outDocTable;
    @FXML
    private TableColumn<OutDoc, String> outDocNumCl;
    @FXML
    private TableColumn<OutDoc, String> outDocDateCl;
    @FXML
    private TableColumn<OutDoc, String> ouDocDescCl;
    @FXML
    private ObservableList<OutDoc> outDocData = FXCollections.observableArrayList();

    @FXML
    private Button addOutDocBt;
    @FXML
    private Button delOutDocBt;

    @FXML
    private TableView<SystemTransfer> sysTrfTable;
    @FXML
    private TableColumn<SystemTransfer, String> sysTrfNameCl;
    @FXML
    private TableColumn<SystemTransfer, String> sysTrfDescCl;
    @FXML
    private ObservableList<SystemTransfer> sysTrfData = FXCollections.observableArrayList();

    @FXML
    private Button addSysTrfBt;
    @FXML
    private Button delSysTrfBt;

    @FXML
    private TableView<Person> personTable;
    @FXML
    private TableColumn<Person, String> personfNameCl;
    @FXML
    private TableColumn<Person, String> personlNameCl;
    @FXML
    private TableColumn<Person, String> personPatronCl;
    @FXML
    private  TableColumn<Person, String> personDescCl;
    @FXML
    private ObservableList<Person> personData = FXCollections.observableArrayList();

    @FXML
    private Button addPersonBt;
    @FXML
    private Button delPersonBt;

    @FXML
    private Button saveBt;

    @FXML
    private Button closeBt;
    @FXML
    private DatePicker dateInPk;
    @FXML
    private Button deleteDocBt;

    private InDoc inDoc;
    private AbstrWindow abstrWindow;
    private Stage thisWindowStage;
}
