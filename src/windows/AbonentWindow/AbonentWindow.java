package windows.AbonentWindow;

import data.Abonent;
import data.AbstrDoc;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import utils.DBControl;
import windows.AbstrWindow;

import java.sql.SQLException;

public class AbonentWindow extends AbstrWindow {

    @FXML
    public void initialize() {
        initTable();
        connectActions();
        loadData();
    }

    private void initTable() {
        nameCl = new TableColumn<>("НАИМЕНОВАНИЕ");
        nameCl.setCellValueFactory(new PropertyValueFactory<Abonent, String>("nameAbonent"));
        descCl = new TableColumn<>("ОПИСАНИЕ");
        descCl.setCellValueFactory(new PropertyValueFactory<Abonent, String>("descAbonent"));
        abonentTable.getColumns().addAll(nameCl, descCl);
    }

    private void loadData() {
        abonentData.clear();
        try {
            abonentData.addAll(DBControl.Abonent.getAll());
            abonentTable.setItems(abonentData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void connectActions() {
        addBt.setOnAction(event -> {
            if (!nameTxt.getText().isEmpty()) {
                Abonent abonent = new Abonent(null, nameTxt.getText(), descTxt.getText());
                try {
                    DBControl.Abonent.add(abonent);
                    nameTxt.clear();
                    descTxt.clear();
                    loadData();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(thisWindowStage.getIcons().get(0));
                stage.getScene().getStylesheets().add(thisWindowStage.getScene().getStylesheets().get(0));
                alert.setHeaderText("ВНИМАНИЕ");
                alert.setTitle("ОШИБКА ДАННЫХ");
                alert.setContentText("ПОЛЕ \"НАИМЕНОВАНИЕ НЕ ЗАПОЛНЕНО\"");
                alert.showAndWait();
            }
        });

        abonentTable.setRowFactory(param -> {
            TableRow<Abonent> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Abonent abonent = row.getItem();
                    if (!editModeCh.isSelected()) {
                        try {
                            DBControl.AbonentLink.add(abstrDoc, abonent);
                            abstrWindow.updateData();
                            thisWindowStage.hide();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        nameTxt.setText(abonent.getNameAbonent());
                        descTxt.setText(abonent.getDescAbonent());
                        editBt.setDisable(false);
                    }
                }
            });
            return row;
        });

        editBt.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(thisWindowStage.getIcons().get(0));
            stage.getScene().getStylesheets().add(thisWindowStage.getScene().getStylesheets().get(0));
            alert.setHeaderText("ЗАПРОС");
            alert.setTitle("ПОДТВЕРЖДЕНИЕ ИЗМЕНЕНИЯ ДАННЫХ");
            alert.setContentText("ВЫ ДЕЙСТВИТЕЛЬНО ХОТИТЕ ИЗМЕНИТЬ ДАННЫЕ?" +
                    " ПОСЛЕ ПОДТВЕРЖДЕНИЯ ИЗМЕНЕНИЯ ПРОИЗОЙДУТ СРАЗУ!");
            alert.showAndWait();
            if (alert.getResult().getButtonData().isCancelButton()) return;
            Abonent abonent = abonentTable.getSelectionModel().getSelectedItem();
            abonent.setNameAbonent(nameTxt.getText());
            abonent.setDescAbonent(descTxt.getText());
            nameTxt.clear();
            descTxt.clear();
            editBt.setDisable(true);
            try {
                DBControl.Abonent.update(abonent);
                loadData();
                abstrWindow.updateData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        selBt.setOnAction(event -> {
            Abonent abonent = abonentTable.getSelectionModel().getSelectedItem();
            if (abonent != null) {
                if (!editModeCh.isSelected()) {
                    try {
                        DBControl.AbonentLink.add(abstrDoc, abonent);
                        abstrWindow.updateData();
                        thisWindowStage.hide();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(thisWindowStage.getIcons().get(0));
                stage.getScene().getStylesheets().add(thisWindowStage.getScene().getStylesheets().get(0));
                alert.setHeaderText("ВНИМАНИЕ");
                alert.setTitle("ОШИБКА ДАННЫХ");
                alert.setContentText("НЕ ВЫБРАНА ЗАПИСЬ");
                alert.showAndWait();
            }
        });

//        findBt.setOnAction(event -> {
//            if (!findTxt.getText().isEmpty()) {
//                abonentData.clear();
//                try {
//                    abonentData.addAll(DBControl.Abonent.findForName(findTxt.getText()));
//                    abonentTable.setItems(abonentData);
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        findTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                abonentData.clear();
                abonentData.addAll(DBControl.Abonent.findForName(findTxt.getText()));
                abonentTable.setItems(abonentData);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        abortBt.setOnAction(event -> {
            abonentData.clear();
            loadData();

        });
    }

    public void setAbstrWindow(AbstrWindow abstrWindow) {
        this.abstrWindow = abstrWindow;
    }

    @Override
    public void updateData() {

    }

    @Override
    public void setData(AbstrDoc abstrDoc) {
        this.abstrDoc = abstrDoc;
    }

    @Override
    public void setStage(Stage thisWindowStage) {
        this.thisWindowStage = thisWindowStage;
    }

    @FXML
    private TableView<Abonent> abonentTable;
    @FXML
    private TableColumn<Abonent, String> nameCl;
    @FXML
    private TableColumn<Abonent, String> descCl;
    @FXML
    private ObservableList<Abonent> abonentData = FXCollections.observableArrayList();

    @FXML
    private TextField nameTxt;

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
    private TextField findTxt;
    //    @FXML
//    private Button findBt;
    @FXML
    private Button abortBt;

    private AbstrWindow abstrWindow;

    private AbstrDoc abstrDoc;

    private Stage thisWindowStage;
}
