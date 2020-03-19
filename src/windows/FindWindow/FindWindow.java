package windows.FindWindow;

import data.AbstrDoc;
import data.InDoc;
import data.OutDoc;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.DBControl;
import windows.AbstrWindow;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;

public class FindWindow extends AbstrWindow {

    @FXML
    public void initialize() {
        initDatePattern();
        connectActions();
    }

    private void initTable() {
        if (abstrDoc != null) {
            if (abstrDoc.getType().equals(AbstrDoc.INDOC)) {
                vboxPn.getChildren().remove(inDocTable);
                outDocData = FXCollections.observableArrayList();
                initOutDocTable();
                System.out.println("outdoc");
            } else {
                vboxPn.getChildren().remove(outDocTable);
                inDocData = FXCollections.observableArrayList();
                initInDocTable();
            }
            loadData();
        }
    }

    private void initDatePattern() {
        String pattern = "yyyy-MM-dd";
        dateStart.setPromptText(pattern.toLowerCase());
        dateStart.setValue(LocalDate.now());
        dateEnd.setPromptText(pattern.toLowerCase());
        dateEnd.setValue(LocalDate.now());
    }

    private void initInDocTable() {
        inNumInDocCl = new TableColumn<>("ВХ. НОМЕР");
        inNumInDocCl.setCellValueFactory(new PropertyValueFactory<InDoc, String>("inNum"));
        TableColumn<InDoc, String> currNumInDocCl = new TableColumn<>("НОМЕР");
        currNumInDocCl.setCellValueFactory(new PropertyValueFactory<InDoc, String>("currNum"));
        TableColumn<InDoc, String> dateInDocCl = new TableColumn<>("ДАТА РЕГ.");
        dateInDocCl.setCellValueFactory(new PropertyValueFactory<InDoc, String>("dateDc"));
        TableColumn<InDoc, String> descInDocCl = new TableColumn<>("ОПИСАНИЕ");
        descInDocCl.setCellValueFactory(new PropertyValueFactory<InDoc, String>("descInDoc"));
        inDocTable.getColumns().addAll(currNumInDocCl, dateInDocCl, inNumInDocCl, descInDocCl);
    }

    private void initOutDocTable() {
        numOutDocCl = new TableColumn<>("НОМЕР");
        numOutDocCl.setCellValueFactory(new PropertyValueFactory<OutDoc, String>("numDoc"));
        dateOutDocCl = new TableColumn<>("ДАТА РЕГ.");
        dateOutDocCl.setCellValueFactory(new PropertyValueFactory<OutDoc, String>("dateDc"));
        descOutDocCL = new TableColumn<>("ОПИСАНИЕ");
        descOutDocCL.setCellValueFactory(new PropertyValueFactory<OutDoc, String>("descOutDoc"));
        outDocTable.getColumns().addAll(numOutDocCl, dateOutDocCl, descOutDocCL);
    }

    private void loadData() {
        try {
            if (abstrDoc.getType().equals(AbstrDoc.INDOC)) {
                outDocData.clear();
                if (!dateStart.getValue().toString().isEmpty() && !dateEnd.getValue().toString().isEmpty()) {
                    if(dateEnd.getValue().compareTo(dateStart.getValue()) == -1) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setHeaderText("ВНИМАНИЕ");
                        alert.setTitle("ОШИБКА ДАТЫ");
                        alert.setContentText("Начальная дата не может быть больше конечной!");
                        alert.showAndWait();
                        return;
                    }
                    HashMap<String, String> param = new HashMap<>();
                    param.put(DBControl.START_DATE, dateStart.getValue().toString());
                    param.put(DBControl.END_DATE, dateEnd.getValue().toString());
                    if (!regNumTxt.getText().isEmpty()) param.put(DBControl.NUM_DOC, regNumTxt.getText());
                    outDocData.addAll(DBControl.OutDoc.findDocs(param));
                }
                System.out.println(outDocData.size());
                outDocTable.setItems(outDocData);
            } else {

                inDocData.clear();
                if (!dateStart.getValue().toString().isEmpty() && !dateEnd.getValue().toString().isEmpty()) {
                    if (dateEnd.getValue().compareTo(dateStart.getValue()) == -1) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setHeaderText("ВНИМАНИЕ");
                        alert.setTitle("ОШИБКА ДАТЫ");
                        alert.setContentText("Начальная дата не может быть больше конечной!");
                        alert.showAndWait();
                        return;
                    }
                    HashMap<String, String> param = new HashMap<>();
                    param.put(DBControl.START_DATE, dateStart.getValue().toString());
                    param.put(DBControl.END_DATE, dateEnd.getValue().toString());
                    if (!regNumTxt.getText().isEmpty()) param.put(DBControl.NUM_DOC, regNumTxt.getText());
                    inDocData.addAll(DBControl.InDoc.findDocs(param));
                    inDocTable.setItems(inDocData);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void connectActions() {
        findBt.setOnAction(event -> {
            loadData();
        });

        outDocTable.setRowFactory(param -> {
            TableRow<OutDoc> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    OutDoc doc = row.getItem();
                    if (abstrDoc != null) {
                        try {
                            DBControl.DocLink.add(abstrDoc, doc);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        abstrWindow.updateData();
                        thisWindowStage.hide();
                    }
                }
            });
            return row;
        });

        inDocTable.setRowFactory(param -> {
            TableRow<InDoc> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    InDoc doc = row.getItem();
                    if (abstrDoc != null) {
                        try {
                            DBControl.DocLink.add(doc, abstrDoc);
                            abstrWindow.updateData();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        abstrWindow.updateData();
                        thisWindowStage.hide();
                    }
                }
            });
            return row;
        });

        selBt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (abstrDoc.getType().equals(AbstrDoc.INDOC)) {
                    data.OutDoc outDoc = outDocTable.getSelectionModel().getSelectedItem();
                    if (abstrDoc != null) {
                        try {
                            DBControl.DocLink.add(abstrDoc, outDoc);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        abstrWindow.updateData();
                        thisWindowStage.hide();
                    }
                }
                else {
                    data.InDoc inDoc = inDocTable.getSelectionModel().getSelectedItem();
                    if (abstrDoc != null) {
                        try {
                            DBControl.DocLink.add(inDoc, abstrDoc);
                            abstrWindow.updateData();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        abstrWindow.updateData();
                        thisWindowStage.hide();
                    }
                }
            }
        });

        closeBt.setOnAction(event -> thisWindowStage.hide());
    }

    @Override
    public void updateData() {

    }

    @Override
    public void setData(AbstrDoc abstrDoc) {
        this.abstrDoc = abstrDoc;
        initTable();
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
    private TableView<OutDoc> outDocTable;

    @FXML
    private ObservableList<InDoc> inDocData;
    @FXML
    private ObservableList<OutDoc> outDocData;

    @FXML
    private Button selBt;
    @FXML
    private Button closeBt;
    @FXML
    private DatePicker dateStart;
    @FXML
    private DatePicker dateEnd;
    @FXML
    private Button findBt;
    @FXML
    private VBox vboxPn;
    @FXML
    private TextField regNumTxt;


    private AbstrWindow abstrWindow;
    private Stage thisWindowStage;
    private AbstrDoc abstrDoc;
}
