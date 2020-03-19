package windows.PersonWindow;

import data.AbstrDoc;
import data.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import utils.DBControl;
import windows.AbstrWindow;

import java.sql.SQLException;

public class PersonWindow extends AbstrWindow{
    @FXML
    public void initialize() {
        initTable();
        connectActions();
        loadData();
    }

    private void initTable() {
        lNameCl = new TableColumn<>("ФАМИЛИЯ");
        lNameCl.setCellValueFactory(new PropertyValueFactory<Person, String>("lastName"));
        fNameCl = new TableColumn<>("ИМЯ");
        fNameCl.setCellValueFactory(new PropertyValueFactory<Person, String>("firstName"));
        patronCl = new TableColumn<>("ОТЧЕСТВО");
        patronCl.setCellValueFactory(new PropertyValueFactory<Person, String>("patronPers"));
        descCl = new TableColumn<>("ОПИСАНИЕ");
        descCl.setCellValueFactory(new PropertyValueFactory<Person, String>("descPerson"));
        personTable.getColumns().addAll(lNameCl, fNameCl, patronCl, descCl);
    }

    private void loadData() {
        try {
            personData.clear();
            personData.addAll(DBControl.Person.getAll());
            personTable.setItems(personData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void connectActions() {
        addBt.setOnAction(event -> {
            if (!lNameTxt.getText().isEmpty() && !fNameTxt.getText().isEmpty()) {
                Person person = new Person(null, fNameTxt.getText(), lNameTxt.getText(), patronTxt.getText(),
                descTxt.getText());
                try {
                    DBControl.Person.add(person);
                    lNameTxt.clear();
                    fNameTxt.clear();
                    patronTxt.clear();
                    descTxt.clear();
                    loadData();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("ВНИМАНИЕ");
                alert.setTitle("ОШИБКА ДАННЫХ");
                alert.setContentText("ПРОВЕРЬТЕ ПОЛЯ \"ФАМИЛИЯ\" \"ИМЯ\"");
                alert.showAndWait();
            }
        });

        personTable.setRowFactory(param -> {
            TableRow<Person> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Person person = row.getItem();
                    if (!editModeCh.isSelected()) {
                        try {
                            DBControl.PersonLink.add(abstrDoc, person);
                            abstrWindow.updateData();
                            thisWindowStage.hide();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        lNameTxt.setText(person.getLastName());
                        fNameTxt.setText(person.getFirstName());
                        patronTxt.setText(person.getPatronPers());
                        descTxt.setText(person.getDescPerson());
                        editBt.setDisable(false);
                    }
                }
            });
            return row;
        });

        editBt.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("ЗАПРОС");
            alert.setTitle("ПОДТВЕРЖДЕНИЕ ИЗМЕНЕНИЯ ДАННЫХ");
            alert.setContentText("ВЫ ДЕЙСТВИТЕЛЬНО ХОТИТЕ ИЗМЕНИТЬ ДАННЫЕ?" +
                    " ПОСЛЕ ПОДТВЕРЖДЕНИЯ ИЗМЕНЕНИЯ ПРОИЗОЙДУТ СРАЗУ!");
            alert.showAndWait();
            if (alert.getResult().getButtonData().isCancelButton()) return;
            Person person = personTable.getSelectionModel().getSelectedItem();
            person.setLastName(lNameTxt.getText());
            person.setFirstName(fNameTxt.getText());
            person.setPatronPers(patronTxt.getText());
            person.setDescPerson(descTxt.getText());
            editBt.setDisable(true);
            lNameTxt.clear();
            fNameTxt.clear();
            patronTxt.clear();
            descTxt.clear();
            try {
                DBControl.Person.update(person);
                loadData();
                abstrWindow.updateData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        selBt.setOnAction(event -> {
            Person person = personTable.getSelectionModel().getSelectedItem();
            if (person != null) {
                if (!editModeCh.isSelected()) {
                    try {
                        DBControl.PersonLink.add(abstrDoc, person);
                        abstrWindow.updateData();
                        thisWindowStage.hide();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("ВНИМАНИЕ");
                alert.setTitle("ОШИБКА ДАННЫХ");
                alert.setContentText("НЕ ВЫБРАНА ЗАПИСЬ");
                alert.showAndWait();
            }
        });

        findBt.setOnAction(event -> {
            findFormLName();
        });

        abortBt.setOnAction(event -> {
            loadData();
        });
    }

    private void findFormLName() {
        personData.clear();
        if (fLNameTxt.getText().isEmpty()) return;

        try {
            personData.addAll(DBControl.Person.findForLName(fLNameTxt.getText()));
            personTable.setItems(personData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateData() {
        loadData();
    }

    @Override
    public void setData(AbstrDoc abstrDoc) {
        this.abstrDoc = abstrDoc;
    }

    @Override
    public void setStage(Stage thisWindowStage) {
        this.thisWindowStage = thisWindowStage;
    }

    @Override
    public void setAbstrWindow(AbstrWindow abstrWindow) {
        this.abstrWindow = abstrWindow;
    }

    @FXML
    private TableView<Person> personTable;
    @FXML
    private TableColumn<Person, String> lNameCl;
    @FXML
    private TableColumn<Person, String> fNameCl;
    @FXML
    private TableColumn<Person, String> patronCl;
    @FXML
    private TableColumn<Person, String> descCl;
    @FXML
    private ObservableList<Person> personData = FXCollections.observableArrayList();

    @FXML
    private TextField lNameTxt;
    @FXML
    private TextField fNameTxt;
    @FXML
    private TextField patronTxt;
    @FXML
    private TextArea descTxt;

    @FXML
    private Button addBt;

    @FXML
    private Button selBt;

    @FXML
    private Button editBt;

    @FXML
    private CheckBox editModeCh;

    @FXML
    private TextField fLNameTxt;
    @FXML
    private Button findBt;
    @FXML
    private Button abortBt;

    private AbstrWindow abstrWindow;

    private AbstrDoc abstrDoc;

    private Stage thisWindowStage;

}
