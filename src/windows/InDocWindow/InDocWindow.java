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
        abonentNameCl.setCellValueFactory(new PropertyValueFactory<Abonent, String>("nameAbonent"));
        abonentDescCl = new TableColumn<>("ОПИСАНИЕ");
        abonentDescCl.setCellValueFactory(new PropertyValueFactory<Abonent, String>("descAbonent"));
        abonentTable.getColumns().addAll(abonentNameCl, abonentDescCl);
    }

    private void initOutDocTable() {
        outDocNumCl = new TableColumn<>("НОМЕР");
        outDocNumCl.setCellValueFactory(new PropertyValueFactory<OutDoc, String>("numDoc"));
        outDocDateCl = new TableColumn<>("ДАТА");
        outDocDateCl.setCellValueFactory(new PropertyValueFactory<OutDoc, String>("dateDc"));
        ouDocDescCl = new TableColumn<>("ОПИСАНИЕ");
        ouDocDescCl.setCellValueFactory(new PropertyValueFactory<OutDoc, String>("descOutDoc"));
        outDocTable.getColumns().addAll(outDocNumCl, outDocDateCl, ouDocDescCl);
    }

    private void initSysTrfTable() {
        sysTrfNameCl = new TableColumn<>("НАИМЕНОВАНИЕ");
        sysTrfNameCl.setCellValueFactory(new PropertyValueFactory<SystemTransfer, String>("nameSysTrf"));
        sysTrfDescCl = new TableColumn<>("ОПИСАНИЕ");
        sysTrfDescCl.setCellValueFactory(new PropertyValueFactory<SystemTransfer, String>("descSysTrf"));
        sysTrfTable.getColumns().addAll(sysTrfNameCl, sysTrfDescCl);
    }

    private void initPersonTable() {
        personfNameCl = new TableColumn<>("ИМЯ");
        personfNameCl.setCellValueFactory(new PropertyValueFactory<Person, String>("firstName"));
        personlNameCl = new TableColumn<>("ФАМИЛИЯ");
        personlNameCl.setCellValueFactory(new PropertyValueFactory<Person, String>("lastName"));
        personPatronCl = new TableColumn<>("ОТЧЕСТВО");
        personPatronCl.setCellValueFactory(new PropertyValueFactory<Person, String>("patronPers"));
        personDescCl = new TableColumn<>("ОПИСАНИЕ");
        personDescCl.setCellValueFactory(new PropertyValueFactory<Person, String>("descPerson"));
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
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("ВНИМАНИЕ");
                alert.setTitle("ОШИБКА ДАННЫХ");
                alert.setContentText("СНАЧАЛА НУЖНО СОХРАНИТЬ ДОКУМЕНТ");
                alert.showAndWait();
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
                stage.setScene(new Scene(root, 800, 600));
//                stage.getScene().getStylesheets().add(thisWindowStage.getScene().getStylesheets().get(0));
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
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("ВНИМАНИЕ");
                alert.setTitle("ОШИБКА ДАННЫХ");
                alert.setContentText("СНАЧАЛА НУЖНО СОХРАНИТЬ ДОКУМЕНТ");
                alert.showAndWait();
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
                stage.setScene(new Scene(root, 800, 600));
//                stage.getScene().getStylesheets().add(thisWindowStage.getScene().getStylesheets().get(0));
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
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("ВНИМАНИЕ");
                alert.setTitle("ОШИБКА ДАННЫХ");
                alert.setContentText("СНАЧАЛА НУЖНО СОХРАНИТЬ ДОКУМЕНТ");
                alert.showAndWait();
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
                stage.setScene(new Scene(root, 800, 600));
//                stage.getScene().getStylesheets().add(thisWindowStage.getScene().getStylesheets().get(0));
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

        saveBt.setOnAction(event -> {
            saveDocument();
        });

        closeBt.setOnAction(event -> {
            thisWindowStage.hide();
        });

        addOutDocBt.setOnAction(event -> {
            if (!isCreatedDoc()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("ВНИМАНИЕ");
                alert.setTitle("ОШИБКА ДАННЫХ");
                alert.setContentText("СНАЧАЛА НУЖНО СОХРАНИТЬ ДОКУМЕНТ");
                alert.showAndWait();
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
                stage.setScene(new Scene(root, 800, 600));
//                stage.getScene().getStylesheets().add(thisWindowStage.getScene().getStylesheets().get(0));
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
                            stage.setScene(new Scene(root, 800, 600));
//                            stage.getScene().getStylesheets().add(thisWindowStage.getScene().getStylesheets().get(0));
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
        }
        else {
            this.inDoc = new InDoc(null, null, null, null, null, null, null);
        }
    }

    private void deleteThisDoc() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("ЗАПРОС НА УДАЛЕНИЕ");
        alert.setTitle("ПОДТВЕРЖДЕНИЕ УДАЛЕНИЯ");
        alert.setContentText("ВЫ ДЕЙСТВИТЕЛЬНО ХОТИТЕ УДАЛИТЬ ДАННЫЙ ДОКУМЕНТ? " +
                "ПОСЛЕ ПОДТВЕРЖДЕНИЯ УДАЛЕНИЕ ОБРАТИТЬ НЕ ВОЗМОЖНО!");
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
        if (inDoc.getIdInDoc() == null) {
            return false;
        }
        else {
            return true;
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
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("ИНФОРМАЦИЯ");
                    alert.setTitle("СОХРАНЕНИЕ ДАННЫХ");
                    alert.setContentText("ДАННЫЕ ОБНОВЛЕНЫ");
                    alert.showAndWait();
                    thisWindowStage.setTitle("ВХОДЯЩИЙ ДОКУМЕНТ: №" + inDoc.getCurrNum());
                    return;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            try {
                int r = DBControl.InDoc.add(inDoc);
                inDoc = DBControl.InDoc.getFromID(r);
                abstrWindow.updateData();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("ИНФОРМАЦИЯ");
                alert.setTitle("СОХРАНЕНИЕ ДАННЫХ");
                alert.setContentText("ДАННЫЕ СОХРАНЕНЫ");
                alert.showAndWait();
                thisWindowStage.setTitle("ВХОДЯЩИЙ ДОКУМЕНТ: №" + inDoc.getCurrNum());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("ВНИМАНИЕ");
            alert.setTitle("ОШИБКА ДАННЫХ");
            alert.setContentText("ЗАПОЛНИТЕ ДАТУ, ВХОДЯЩИЙ НОМЕР И НОМЕР РЕГИСТРАЦИИ");
            alert.showAndWait();
        }
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
