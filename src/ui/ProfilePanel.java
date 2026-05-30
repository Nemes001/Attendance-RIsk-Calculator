package ui;

import database.StudentDAO;
import model.StudentProfile;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProfilePanel {

    private TextField nameField     = new TextField();
    private TextField studentIdField= new TextField();
    private TextField semesterField = new TextField();
    private Label     statusLabel   = new Label(" ");

    // Callback to update header after save
    public interface ProfileCallback {
        void onSaved(StudentProfile profile);
    }

    public void show(Stage owner, ProfileCallback callback) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(owner);
        stage.setTitle("Student Profile");
        stage.setResizable(false);

        // ── Load existing profile ─────────────────────────────────
        StudentDAO dao     = new StudentDAO();
        StudentProfile existing = dao.getProfile();
        if (existing != null) {
            nameField.setText(existing.getName());
            studentIdField.setText(existing.getStudentId());
            semesterField.setText(existing.getSemester());
        }

        // ── Title ─────────────────────────────────────────────────
        Label title = new Label("👤 Student Profile");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        title.setTextFill(Color.web("#1E3A5F"));

        // ── Form ──────────────────────────────────────────────────
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(12);
        grid.setPadding(new Insets(15));
        grid.setStyle(
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

        nameField.setStyle(fieldStyle);
        studentIdField.setStyle(fieldStyle);
        semesterField.setStyle(fieldStyle);
        nameField.setPromptText("e.g. John Doe");
        studentIdField.setPromptText("e.g. 21CS045");
        semesterField.setPromptText("e.g. 4th Semester");
        nameField.setPrefWidth(220);
        studentIdField.setPrefWidth(220);
        semesterField.setPrefWidth(220);

        grid.add(styledLabel("Full Name:"),   0, 0);
        grid.add(nameField,                    1, 0);
        grid.add(styledLabel("Student ID:"),  0, 1);
        grid.add(studentIdField,               1, 1);
        grid.add(styledLabel("Semester:"),    0, 2);
        grid.add(semesterField,                1, 2);

        // ── Buttons ───────────────────────────────────────────────
        Button saveBtn   = new Button("💾 Save Profile");
        Button cancelBtn = new Button("Cancel");

        saveBtn.setStyle(
            "-fx-background-color: #2563EB;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 8 20;" +
            "-fx-border-radius: 5;" +
            "-fx-background-radius: 5;"
        );
        cancelBtn.setStyle(
            "-fx-background-color: #6B7280;" +
            "-fx-text-fill: white;" +
            "-fx-padding: 8 20;" +
            "-fx-border-radius: 5;" +
            "-fx-background-radius: 5;"
        );

        saveBtn.setOnAction(e -> {
            String name      = nameField.getText().trim();
            String studentId = studentIdField.getText().trim();
            String semester  = semesterField.getText().trim();

            if (name.isEmpty() || studentId.isEmpty()
                    || semester.isEmpty()) {
                statusLabel.setText("❌ All fields are required.");
                statusLabel.setTextFill(Color.RED);
                return;
            }

            StudentProfile profile = new StudentProfile(
                name, studentId, semester);
            if (dao.saveProfile(profile)) {
                statusLabel.setText("✅ Profile saved!");
                statusLabel.setTextFill(Color.GREEN);
                if (callback != null) callback.onSaved(profile);
                stage.close();
            } else {
                statusLabel.setText("❌ Failed to save profile.");
                statusLabel.setTextFill(Color.RED);
            }
        });

        cancelBtn.setOnAction(e -> stage.close());

        HBox buttons = new HBox(10, saveBtn, cancelBtn);
        buttons.setAlignment(Pos.CENTER);

        statusLabel.setFont(
            Font.font("Arial", FontWeight.NORMAL, 11));

        VBox layout = new VBox(15,
            title, grid, buttons, statusLabel);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setStyle("-fx-background-color: #FFFFFF;");

        stage.setScene(new Scene(layout, 420, 300));
        stage.show();
    }

    private Label styledLabel(String text) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
        lbl.setTextFill(Color.web("#374151"));
        return lbl;
    }
}