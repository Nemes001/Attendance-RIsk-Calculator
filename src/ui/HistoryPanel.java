package ui;

import database.AttendanceDAO;
import model.AttendanceRecord;
import threads.LoadWorker;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.List;

public class HistoryPanel {

    private Label statusLabel = new Label("Loading history...");

    public void show(Stage stage) {

        // ── Table ─────────────────────────────────────────────────
        TableView<AttendanceRecord> table = new TableView<>();
        table.setColumnResizePolicy(
            TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPlaceholder(new Label("No records found."));

        TableColumn<AttendanceRecord, Integer> totalCol =
            new TableColumn<>("Total");
        totalCol.setCellValueFactory(
            new PropertyValueFactory<>("totalClasses"));

        TableColumn<AttendanceRecord, Integer> attendedCol =
            new TableColumn<>("Attended");
        attendedCol.setCellValueFactory(
            new PropertyValueFactory<>("attendedClasses"));

        TableColumn<AttendanceRecord, Double> requiredCol =
            new TableColumn<>("Required %");
        requiredCol.setCellValueFactory(
            new PropertyValueFactory<>("requiredPercentage"));

        TableColumn<AttendanceRecord, Double> percentCol =
            new TableColumn<>("Attendance %");
        percentCol.setCellValueFactory(
            new PropertyValueFactory<>("attendancePercentage"));

        TableColumn<AttendanceRecord, String> riskCol =
            new TableColumn<>("Risk Level");
        riskCol.setCellValueFactory(
            new PropertyValueFactory<>("riskLevel"));

        TableColumn<AttendanceRecord, String> suggestionCol =
            new TableColumn<>("Suggestion");
        suggestionCol.setCellValueFactory(
            new PropertyValueFactory<>("suggestion"));

        table.getColumns().addAll(
            totalCol, attendedCol, requiredCol,
            percentCol, riskCol, suggestionCol
        );

        // ── Clear History Button ──────────────────────────────────
        Button clearBtn = new Button("🗑 Clear History");
        clearBtn.setStyle(
            "-fx-background-color: #DC2626; -fx-text-fill: white;");
        clearBtn.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Clear History");
            confirm.setContentText(
                "Are you sure you want to delete all records?");
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    AttendanceDAO dao = new AttendanceDAO();
                    if (dao.deleteAllRecords()) {
                        table.getItems().clear();
                        statusLabel.setText("✅ History cleared.");
                    }
                }
            });
        });

        // ── Layout ────────────────────────────────────────────────
        HBox bottomBar = new HBox(10, clearBtn, statusLabel);
        bottomBar.setPadding(new Insets(5));

        VBox layout = new VBox(10, table, bottomBar);
        layout.setPadding(new Insets(15));

        stage.setTitle("Attendance History");
        stage.setScene(new Scene(layout, 850, 400));
        stage.show();

        // ── Load records in background ────────────────────────────
        LoadWorker worker = new LoadWorker(
            new LoadWorker.LoadCallback() {
                @Override
                public void onLoaded(List<AttendanceRecord> records) {
                    ObservableList<AttendanceRecord> data =
                        FXCollections.observableArrayList(records);
                    table.setItems(data);
                    statusLabel.setText(
                        "✅ " + records.size() + " record(s) loaded.");
                }
                @Override
                public void onFailure(String msg) {
                    statusLabel.setText("❌ " + msg);
                }
            });

        Thread thread = new Thread(worker);
        thread.setDaemon(true);
        thread.start();
    }
}