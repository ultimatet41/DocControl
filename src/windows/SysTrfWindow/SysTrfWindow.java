package windows.SysTrfWindow;

import data.AbstrDoc;
import data.SystemTransfer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import utils.DBControl;
import windows.AbstrWindow;

import java.sql.SQLException;

public class SysTrfWindow extends AbstrWindow{


    @FXML
    public void initialize() {
        initTable();
        connectActions();
        loadData();
    }

    private void initTable() {
        nameCl = new TableColumn<>("НАИМЕНОВАНИЕ");
        nameCl.setCellValueFactory(new PropertyValueFactory<>("nameSysTrf"));
        descCl = new TableColumn<>("ОПИСАНИЕ");
        descCl.setCellValueFactory(new PropertyValueFactory<>("descSysTrf"));
        sysTrfTable.getColumns().addAll(nameCl, descCl);
    }

    private void loadData() {
        sysTrfData.clear();
        try {
            sysTrfData.addAll(DBControl.SysTrf.getAll());
            sysTrfTable.setItems(sysTrfData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void connectActions() {
        addBt.setOnAction(event -> {
            if (!nameTxt.getText().isEmpty() && !nameTxt.getText().equals("")) {
                SystemTransfer transfer = new SystemTransfer(null, nameTxt.getText(), descTxt.getText());
                try {
                    DBControl.SysTrf.add(transfer);
                    nameTxt.clear();
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
                alert.setContentText("ПОЛЕ \"НАИМЕНОВАНИЕ НЕ ЗАПОЛНЕНО\"");
                alert.showAndWait();
            }
        });

        sysTrfTable.setRowFactory(param -> {
            TableRow<SystemTransfer> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    SystemTransfer transfer = row.getItem();
                    if (!editModeCh.isSelected()) {
                        try {
                            DBControl.SysTrfLink.add(abstrDoc, transfer);
                            abstrWindow.updateData();
                            thisWindowStage.hide();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        nameTxt.setText(transfer.getNameSysTrf());
                        descTxt.setText(transfer.getDescSysTrf());
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
            SystemTransfer transfer = sysTrfTable.getSelectionModel().getSelectedItem();
            transfer.setNameSysTrf(nameTxt.getText());
            transfer.setDescSysTrf(descTxt.getText());
            nameTxt.clear();
            descTxt.clear();
            editBt.setDisable(true);
            try {
                DBControl.SysTrf.update(transfer);
                loadData();
                abstrWindow.updateData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        selBt.setOnAction(event -> {
            SystemTransfer transfer = sysTrfTable.getSelectionModel().getSelectedItem();
            if (transfer != null) {
                if (!editModeCh.isSelected()) {
                    try {
                        DBControl.SysTrfLink.add(abstrDoc, transfer);
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
    private TableView<SystemTransfer> sysTrfTable;
    @FXML
    private TableColumn<SystemTransfer, String> nameCl;
    @FXML
    private TableColumn<SystemTransfer, String> descCl;
    @FXML
    private ObservableList<SystemTransfer> sysTrfData = FXCollections.observableArrayList();

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

    private AbstrWindow abstrWindow;

    private AbstrDoc abstrDoc;

    private Stage thisWindowStage;
}
