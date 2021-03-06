package windows.OutDocWindow;

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
import windows.InDocWindow.InDocWindow;
import windows.PersonWindow.PersonWindow;
import windows.SysTrfWindow.SysTrfWindow;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class OutDocWindow extends AbstrWindow {

    @FXML
    public void initialize() {
        initAbonentTable();
        initPersonTable();
        initSysTrfTable();
        initInDocTable();
        connectActions();
        descTxt.setWrapText(true);
        otherDataTxt.setWrapText(true);
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
                abonentWindow.setAbstrWindow(OutDocWindow.this);
                abonentWindow.setData(outDoc);
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
                DBControl.AbonentLink.delete(outDoc, abonent);
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
                sysTrfWindow.setAbstrWindow(OutDocWindow.this);
                sysTrfWindow.setData(outDoc);
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
                DBControl.SysTrfLink.delete(outDoc, transfer);
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
                personWindow.setAbstrWindow(OutDocWindow.this);
                personWindow.setData(outDoc);
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
                DBControl.PersonLink.delete(outDoc, person);
                updateData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        saveBt.setOnAction(event -> saveDocument());

        closeBt.setOnAction(event -> thisWindowStage.hide());

        addInDocBt.setOnAction(event -> {
            if (!isCreatedDoc()) {
                showAlert("ВНИМАНИЕ", "ОШИБКА ДАННЫХ", "СНАЧАЛА НУЖНО СОХРАНИТЬ ДОКУМЕНТ", Alert.AlertType.WARNING);
                return;
            }
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("windows/FindWindow/FindWindow.fxml"));
            try {
                root = loader.load();
                FindWindow findWindow = loader.getController();
                findWindow.setAbstrWindow(OutDocWindow.this);
                findWindow.setData(outDoc);
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

        delInDocBt.setOnAction(event -> {
            InDoc inDoc = inDocTable.getSelectionModel().getSelectedItem();
            if (inDoc == null) return;
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

        inDocTable.setRowFactory(new Callback<TableView<InDoc>, TableRow<InDoc>>() {
            @Override
            public TableRow<InDoc> call(TableView<InDoc> param) {
                TableRow<InDoc> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2 && (!row.isEmpty())) {
                        InDoc doc = row.getItem();
                        Parent root;
                        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("windows/InDocWindow/InDocWindow.fxml"));
                        try {
                            root = loader.load();
                            InDocWindow inDocWindow = loader.getController();
                            inDocWindow.setData(doc);
                            inDocWindow.setAbstrWindow(OutDocWindow.this);
                            Stage stage = new Stage();
                            stage.setTitle("ВХОДЯЩИЙ ДОКУМЕНТ: №" + doc.getCurrNum());
                            Scene scene = new Scene(root, 800, 700);
                            scene.getStylesheets().add(thisWindowStage.getScene().getStylesheets().get(0));
                            stage.setScene(scene);
                            stage.getIcons().add(thisWindowStage.getIcons().get(0));
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

        deleteDocBt.setOnAction(event -> deleteThisDoc());
    }

    private void initAbonentTable() {
        abonentNameCl = new TableColumn<>("НАИМЕНОВАНИЕ");
        abonentNameCl.setCellValueFactory(new PropertyValueFactory<>("nameAbonent"));
        abonentDescCl = new TableColumn<>("ОПИСАНИЕ");
        abonentDescCl.setCellValueFactory(new PropertyValueFactory<>("descAbonent"));
        abonentTable.getColumns().addAll(abonentNameCl, abonentDescCl);
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

    private void initInDocTable() {
        inNumInDocCl = new TableColumn<>("ВХ. НОМЕР");
        inNumInDocCl.setCellValueFactory(new PropertyValueFactory<>("inNum"));
        TableColumn<InDoc, String> currNumInDocCl = new TableColumn<>("НОМЕР");
        currNumInDocCl.setCellValueFactory(new PropertyValueFactory<>("currNum"));
        TableColumn<InDoc, String> dateInDocCl = new TableColumn<>("ДАТА РЕГ.");
        dateInDocCl.setCellValueFactory(new PropertyValueFactory<>("dateDc"));
        TableColumn<InDoc, String> descInDocCl = new TableColumn<>("ОПИСАНИЕ");
        descInDocCl.setCellValueFactory(new PropertyValueFactory<>("descInDoc"));
        inDocTable.getColumns().addAll(currNumInDocCl, dateInDocCl, inNumInDocCl, descInDocCl);
    }

    private void loadAbonentData() {
        try {
            abonentData.clear();
            abonentData.addAll(DBControl.AbonentLink.getOfDoc(outDoc));
            abonentTable.setItems(abonentData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadSysTrfData() {
        sysTrfData.clear();
        try {
            sysTrfData.addAll(DBControl.SysTrfLink.getOfDoc(outDoc));
            sysTrfTable.setItems(sysTrfData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadPersonData() {
        personData.clear();
        try {
            personData.addAll(DBControl.PersonLink.getOfDoc(outDoc));
            personTable.setItems(personData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadInDocData() {
        try {
            inDocData.clear();
            for (AbstrDoc inDoc : DBControl.DocLink.getInDocs(outDoc)) {
                inDocData.add((InDoc) inDoc);
                System.out.println("add outdoc");
            }
            inDocTable.setItems(inDocData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateData() {
        if (isCreatedDoc()) {
            loadAbonentData();
            loadPersonData();
            loadSysTrfData();
            loadInDocData();
            if (abstrWindow != null) abstrWindow.updateData();
        }
    }

    @Override
    public void setData(AbstrDoc abstrDoc) {
        if (abstrDoc != null) {
            this.outDoc = (OutDoc) abstrDoc;
            dateRegTxt.setValue(LocalDate.parse(outDoc.getDateDc()));
            numDocTxt.setText(outDoc.getNumDoc());
            descTxt.setText(outDoc.getDescOutDoc());
            otherDataTxt.setText(outDoc.getOtherData());
            System.out.println(this.outDoc);
            updateData();
            deleteDocBt.setDisable(false);
            saveBt.setText("Обновить");
        }
        else {
            this.outDoc = new OutDoc(null, null, null, null, null);
        }
    }

    @Override
    public void setStage(Stage thisWindowStage) {
        this.thisWindowStage = thisWindowStage;
    }

    @Override
    public void setAbstrWindow(AbstrWindow abstrWindow) {
        this.abstrWindow = abstrWindow;
    }

    private boolean isCreatedDoc() {
        return outDoc.getIdOutDoc() != null;
    }

    private void saveDocument() {
        if (dateRegTxt.getValue() != null && !numDocTxt.getText().isEmpty()) {
            outDoc.setNumDoc(numDocTxt.getText());
            outDoc.setDateDc(dateRegTxt.getValue().toString());
            outDoc.setDescOutDoc(descTxt.getText());
            outDoc.setOtherData(otherDataTxt.getText());
            System.out.println(outDoc);
            if (isCreatedDoc()) {
                try {
                    DBControl.OutDoc.update(outDoc);
                    abstrWindow.updateData();
                    showAlert("ИНФОРМАЦИЯ", "ОБНОВЛЕНИЕ ДАННЫХ", "ДАННЫЕ ОБНОВЛЕНЫ", Alert.AlertType.INFORMATION);
                    thisWindowStage.setTitle("ИСХОДЯЩИЙ ДОКУМЕНТ: №" + outDoc.getNumDoc());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else try {
                if (DBControl.OutDoc.isExist(outDoc)) {
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
                int r = DBControl.OutDoc.add(outDoc);
                if (r == DBControl.DATA_IS_NOT_CREATED) {
                    showAlert("ВНИМАНИЕ", "ОШИБКА СОХРАНЕНИЯ", "ВО ВРЕМЯ СОХРАНЕНИЯ ПРОИЗОШЛА ОШИБКА: " + DBControl.DATA_IS_NOT_CREATED,
                            Alert.AlertType.ERROR);
                }
                outDoc = DBControl.OutDoc.getFromID(r);
                abstrWindow.updateData();
                showAlert("ИНФОРМАЦИЯ", "СОХРАНЕНИЕ ДАННЫХ", "ДАННЫЕ СОХРАНЕНЫ", Alert.AlertType.INFORMATION);
                thisWindowStage.setTitle("ИСХОДЯЩИЙ ДОКУМЕНТ: №" + outDoc.getNumDoc());
                saveBt.setText("Обновить");
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("ВНИМАНИЕ", "ОШИБКА СОХРАНЕНИЯ", "ВО ВРЕМЯ СОХРАНЕНИЯ ПРОИЗОШЛА ОШИБКА",
                        Alert.AlertType.ERROR);
            }
        }
        else {
            showAlert("ВНИМАНИЕ", "ОШИБКА ДАННЫХ", "ЗАПОЛНИТЕ ДАТУ И НОМЕР РЕГИСТРАЦИИ",
                    Alert.AlertType.WARNING);
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
                DBControl.PersonLink.delete(outDoc, person);
            }
            for (data.Abonent abonent : abonentData) {
                DBControl.AbonentLink.delete(outDoc, abonent);
            }
            for (data.SystemTransfer transfer : sysTrfData) {
                DBControl.SysTrfLink.delete(outDoc, transfer);
            }
            for (data.InDoc inDoc : inDocData) {
                DBControl.DocLink.delete(inDoc, outDoc);
            }
            DBControl.OutDoc.delete(outDoc);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        abstrWindow.updateData();
        thisWindowStage.hide();
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
    private TableView<InDoc> inDocTable;
    @FXML
    private TableColumn<InDoc, String> inNumInDocCl;
    @FXML
    private TableColumn<OutDoc, String> numOutDocCl;
    @FXML
    private TableColumn<OutDoc, String> dateOutDocCl;
    @FXML
    private TableColumn<OutDoc, String> descOutDocCL;
    @FXML
    private ObservableList<InDoc> inDocData = FXCollections.observableArrayList();

    @FXML
    private Button addPersonBt;
    @FXML
    private Button delPersonBt;

    @FXML
    private Button saveBt;

    @FXML
    private Button closeBt;

    @FXML
    private DatePicker dateRegTxt;

    @FXML
    private TextField numDocTxt;

    @FXML
    private TextArea descTxt;
    @FXML
    private TextArea otherDataTxt;

    @FXML
    private Button addInDocBt;
    @FXML
    private Button delInDocBt;
    @FXML
    private Button deleteDocBt;

    private OutDoc outDoc;
    private AbstrWindow abstrWindow;
    private Stage thisWindowStage;
}
