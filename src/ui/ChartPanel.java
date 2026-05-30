package ui;

import database.AttendanceDAO;
import model.AttendanceRecord;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import java.util.List;

public class ChartPanel {

    public void show(Stage stage) {
        stage.setTitle("Attendance Charts");

        // ── Load records ──────────────────────────────────────────
        AttendanceDAO dao = new AttendanceDAO();
        List<AttendanceRecord> records = dao.getAllRecords();

        if (records.isEmpty()) {
            Label empty = new Label(
                "No records found.\nPlease add some attendance records first.");
            empty.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
            empty.setTextFill(Color.GRAY);
            empty.setAlignment(Pos.CENTER);

            VBox layout = new VBox(empty);
            layout.setAlignment(Pos.CENTER);
            layout.setPadding(new Insets(40));
            stage.setScene(new Scene(layout, 600, 400));
            stage.show();
            return;
        }

        // ── Title ─────────────────────────────────────────────────
        Label title = new Label("📊 Attendance Overview");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#1E3A5F"));

        // ── Pie Chart — Attended vs Missed ────────────────────────
        int totalAttended = records.stream()
            .mapToInt(AttendanceRecord::getAttendedClasses).sum();
        int totalClasses  = records.stream()
            .mapToInt(AttendanceRecord::getTotalClasses).sum();
        int totalMissed   = totalClasses - totalAttended;

        PieChart pieChart = new PieChart(
            FXCollections.observableArrayList(
                new PieChart.Data(
                    "Attended (" + totalAttended + ")", totalAttended),
                new PieChart.Data(
                    "Missed (" + totalMissed + ")", totalMissed)
            )
        );
        pieChart.setTitle("Overall Attended vs Missed");
        pieChart.setLegendVisible(true);
        pieChart.setPrefSize(350, 300);

        // ── Bar Chart — Subject wise attendance % ─────────────────
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis   yAxis = new NumberAxis(0, 100, 10);
        xAxis.setLabel("Subject");
        yAxis.setLabel("Attendance %");

        BarChart<String, Number> barChart =
            new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Subject-wise Attendance %");
        barChart.setLegendVisible(false);
        barChart.setPrefSize(450, 300);

        XYChart.Series<String, Number> series =
            new XYChart.Series<>();
        series.setName("Attendance %");

        // Group by subject — take latest record per subject
        records.stream()
            .collect(java.util.stream.Collectors.toMap(
                AttendanceRecord::getSubjectName,
                r -> r,
                (existing, replacement) -> existing
            ))
            .forEach((subject, record) -> {
                series.getData().add(
                    new XYChart.Data<>(
                        subject,
                        record.getAttendancePercentage()
                    )
                );
            });

        barChart.getData().add(series);

        // ── Risk Summary ──────────────────────────────────────────
        long safe     = records.stream()
            .filter(r -> r.getRiskLevel().equals("SAFE")).count();
        long warning  = records.stream()
            .filter(r -> r.getRiskLevel().equals("WARNING")).count();
        long critical = records.stream()
            .filter(r -> r.getRiskLevel().equals("CRITICAL")).count();

        HBox riskSummary = new HBox(20,
            riskBadge("✅ Safe",     String.valueOf(safe),     "#D1FAE5", "#065F46"),
            riskBadge("⚠ Warning",  String.valueOf(warning),  "#FEF3C7", "#92400E"),
            riskBadge("❌ Critical", String.valueOf(critical), "#FEE2E2", "#991B1B")
        );
        riskSummary.setAlignment(Pos.CENTER);
        riskSummary.setPadding(new Insets(10));

        // ── Layout ────────────────────────────────────────────────
        HBox charts = new HBox(20, pieChart, barChart);
        charts.setAlignment(Pos.CENTER);
        charts.setPadding(new Insets(10));

        VBox layout = new VBox(15,
            title, riskSummary, charts);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setStyle("-fx-background-color: #FFFFFF;");

        stage.setScene(new Scene(layout, 850, 500));
        stage.show();
    }

    private VBox riskBadge(String label, String count,
                            String bg, String fg) {
        Label countLbl = new Label(count);
        countLbl.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        countLbl.setTextFill(Color.web(fg));

        Label nameLbl = new Label(label);
        nameLbl.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        nameLbl.setTextFill(Color.web(fg));

        VBox box = new VBox(4, countLbl, nameLbl);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(12, 20, 12, 20));
        box.setStyle(
            "-fx-background-color: " + bg + ";" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;"
        );
        return box;
    }
}