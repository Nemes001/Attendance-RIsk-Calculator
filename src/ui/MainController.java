package ui;

import logic.AttendanceCalculator;
import logic.RiskEvaluator;
import logic.SuggestionEngine;
import model.AttendanceRecord;
import threads.SaveWorker;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class MainController {

    private static final int MAX_CLASSES = 40;

    // ── Input Fields ─────────────────────────────────────────────
    private TextField subjectField  = new TextField();
    private TextField totalField    = new TextField();
    private TextField attendedField = new TextField();
    private TextField requiredField = new TextField();

    // ── Output Labels ────────────────────────────────────────────
    private Label percentageLabel = new Label("Attendance %:   --");
    private Label riskLabel       = new Label("Risk Level:       --");
    private Label suggestionLabel = new Label("Suggestion:      --");
    private Label statusLabel     = new Label(" ");

    // ── Result box reference ──────────────────────────────────────
    private VBox resultBox = new VBox(8);

    // ── Controls ─────────────────────────────────────────────────
    private ProgressIndicator spinner    = new ProgressIndicator();
    private Button             calculateBtn = new Button("Calculate");

    public VBox buildUI() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(15));

        // ── Input Section ─────────────────────────────────────────
        Label inputTitle = new Label("Enter Attendance Details");
        inputTitle.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        inputTitle.setTextFill(Color.web("#374151"));

        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(15);
        inputGrid.setVgap(12);
        inputGrid.setPadding(new Insets(10));
        inputGrid.setStyle(
            "-fx-background-color: #F9FAFB;" +
            "-fx-border-color: #E5E7EB;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;"
        );

        String fieldStyle =
            "-fx-border-color: #D1D5DB;" +
            "-fx-border-radius: 4;" +
            "-fx-padding: 6;" +
            "-fx-font-size: 13px;";

        subjectField.setStyle(fieldStyle);
        totalField.setStyle(fieldStyle);
        attendedField.setStyle(fieldStyle);
        requiredField.setStyle(fieldStyle);

        subjectField.setPromptText("e.g. Mathematics");
        totalField.setPromptText("Max " + MAX_CLASSES);
        attendedField.setPromptText("Cannot exceed total");
        requiredField.setPromptText("e.g. 75");

        subjectField.setPrefWidth(180);
        totalField.setPrefWidth(180);
        attendedField.setPrefWidth(180);
        requiredField.setPrefWidth(180);

        inputGrid.add(styledLabel("Subject Name:"),           0, 0);
        inputGrid.add(subjectField,                            1, 0);
        inputGrid.add(styledLabel("Total Classes (max 40):"), 0, 1);
        inputGrid.add(totalField,                              1, 1);
        inputGrid.add(styledLabel("Classes Attended:"),       0, 2);
        inputGrid.add(attendedField,                           1, 2);
        inputGrid.add(styledLabel("Required Attendance(%):"), 0, 3);
        inputGrid.add(requiredField,                           1, 3);

        // ── Buttons ───────────────────────────────────────────────
        Button historyBtn = new Button("📋 History");
        Button chartBtn   = new Button("📊 Charts");
        Button clearBtn   = new Button("🗑 Clear");

        calculateBtn.setStyle(
            "-fx-background-color: #2563EB;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 8 16;" +
            "-fx-border-radius: 5;" +
            "-fx-background-radius: 5;"
        );
        historyBtn.setStyle(
            "-fx-background-color: #1E3A5F;" +
            "-fx-text-fill: white;" +
            "-fx-padding: 8 16;" +
            "-fx-border-radius: 5;" +
            "-fx-background-radius: 5;"
        );
        chartBtn.setStyle(
            "-fx-background-color: #7C3AED;" +
            "-fx-text-fill: white;" +
            "-fx-padding: 8 16;" +
            "-fx-border-radius: 5;" +
            "-fx-background-radius: 5;"
        );
        clearBtn.setStyle(
            "-fx-background-color: #6B7280;" +
            "-fx-text-fill: white;" +
            "-fx-padding: 8 16;" +
            "-fx-border-radius: 5;" +
            "-fx-background-radius: 5;"
        );

        calculateBtn.setOnAction(e -> handleCalculate());
        historyBtn.setOnAction(e -> openHistory());
        chartBtn.setOnAction(e -> openCharts());
        clearBtn.setOnAction(e -> handleClear());

        spinner.setVisible(false);
        spinner.setMaxSize(25, 25);

        HBox buttonRow = new HBox(8,
            calculateBtn, historyBtn, chartBtn, clearBtn, spinner);
        buttonRow.setAlignment(Pos.CENTER);

        // ── Result Box ────────────────────────────────────────────
        Label resultTitle = new Label("Result");
        resultTitle.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        resultTitle.setTextFill(Color.web("#374151"));

        resultBox.setPadding(new Insets(15));
        resultBox.setStyle(
            "-fx-background-color: #F9FAFB;" +
            "-fx-border-color: #E5E7EB;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;"
        );

        percentageLabel.setFont(
            Font.font("Arial", FontWeight.BOLD, 14));
        riskLabel.setFont(
            Font.font("Arial", FontWeight.BOLD, 14));
        suggestionLabel.setFont(
            Font.font("Arial", FontWeight.NORMAL, 13));
        suggestionLabel.setWrapText(true);
        statusLabel.setFont(
            Font.font("Arial", FontWeight.NORMAL, 11));
        statusLabel.setTextFill(Color.GRAY);

        resultBox.getChildren().addAll(
            percentageLabel,
            riskLabel,
            new Separator(),
            suggestionLabel,
            statusLabel
        );

        layout.getChildren().addAll(
            inputTitle,
            inputGrid,
            buttonRow,
            resultTitle,
            resultBox
        );

        return layout;
    }

    // ── CALCULATE ─────────────────────────────────────────────────
    private void handleCalculate() {
        try {
            String subject  = subjectField.getText().trim();
            int total       = Integer.parseInt(
                totalField.getText().trim());
            int attended    = Integer.parseInt(
                attendedField.getText().trim());
            double required = Double.parseDouble(
                requiredField.getText().trim());

            if (!validateInputs(subject, total, attended, required))
                return;

            double percentage = AttendanceCalculator
                .calculate(attended, total);
            String risk       = RiskEvaluator
                .evaluate(percentage, required);
            String suggestion = SuggestionEngine
                .suggest(attended, total, required);

            percentageLabel.setText(
                String.format("Attendance %%:   %.2f%%", percentage));
            riskLabel.setText("Risk Level:       " + risk);
            suggestionLabel.setText("💡 " + suggestion);

            switch (risk) {
                case "SAFE" -> {
                    riskLabel.setTextFill(Color.web("#16A34A"));
                    updateResultStyle("#F0FDF4", "#86EFAC");
                }
                case "WARNING" -> {
                    riskLabel.setTextFill(Color.web("#D97706"));
                    updateResultStyle("#FFFBEB", "#FCD34D");
                }
                case "CRITICAL" -> {
                    riskLabel.setTextFill(Color.web("#DC2626"));
                    updateResultStyle("#FEF2F2", "#FCA5A5");
                }
            }

            AttendanceRecord record = new AttendanceRecord(
                subject, total, attended,
                required, percentage, risk, suggestion
            );

            spinner.setVisible(true);
            calculateBtn.setDisable(true);

            SaveWorker worker = new SaveWorker(record,
                new SaveWorker.SaveCallback() {
                    @Override
                    public void onSuccess() {
                        spinner.setVisible(false);
                        calculateBtn.setDisable(false);
                        statusLabel.setText("✅ Record saved.");
                    }
                    @Override
                    public void onFailure(String msg) {
                        spinner.setVisible(false);
                        calculateBtn.setDisable(false);
                        statusLabel.setText("❌ " + msg);
                    }
                });

            Thread thread = new Thread(worker);
            thread.setDaemon(true);
            thread.start();

        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter numbers only.");
        }
    }

    private void updateResultStyle(String bg, String border) {
        resultBox.setStyle(
            "-fx-background-color: " + bg + ";" +
            "-fx-border-color: " + border + ";" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;"
        );
    }

    // ── VALIDATION ───────────────────────────────────────────────
    private boolean validateInputs(String subject, int total,
                                    int attended, double required) {
        if (subject.isEmpty()) {
            showAlert("Invalid Input", "Please enter a subject name.");
            return false;
        }
        if (total <= 0) {
            showAlert("Invalid Input",
                "Total classes must be greater than 0.");
            return false;
        }
        if (total > MAX_CLASSES) {
            showAlert("Invalid Input",
                "Total classes cannot exceed " + MAX_CLASSES + ".");
            return false;
        }
        if (attended < 0) {
            showAlert("Invalid Input",
                "Attended classes cannot be negative.");
            return false;
        }
        if (attended > total) {
            showAlert("Invalid Input",
                "Attended classes cannot exceed total classes.");
            return false;
        }
        if (required <= 0 || required > 100) {
            showAlert("Invalid Input",
                "Required percentage must be between 1 and 100.");
            return false;
        }
        return true;
    }

    // ── CLEAR ────────────────────────────────────────────────────
    private void handleClear() {
        subjectField.clear();
        totalField.clear();
        attendedField.clear();
        requiredField.clear();
        percentageLabel.setText("Attendance %:   --");
        riskLabel.setText("Risk Level:       --");
        riskLabel.setTextFill(Color.BLACK);
        suggestionLabel.setText("Suggestion:      --");
        statusLabel.setText(" ");
        updateResultStyle("#F9FAFB", "#E5E7EB");
    }

    private void openHistory() {
        new HistoryPanel().show(new Stage());
    }

    private void openCharts() {
        new ChartPanel().show(new Stage());
    }

    private Label styledLabel(String text) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
        lbl.setTextFill(Color.web("#374151"));
        return lbl;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}