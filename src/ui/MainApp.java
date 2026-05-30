package ui;

import database.DBInitializer;
import database.DatabaseConnection;
import database.StudentDAO;
import model.StudentProfile;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class MainApp extends Application {

    private Label studentInfoLabel = new Label("");

    @Override
    public void start(Stage primaryStage) {
        DBInitializer.initialize();

        // ── Load existing profile ─────────────────────────────────
        StudentDAO     dao     = new StudentDAO();
        StudentProfile profile = dao.getProfile();

        // ── Header ────────────────────────────────────────────────
        Label title = new Label("🎓 Attendance Risk Calculator");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        title.setTextFill(Color.web("#1E3A5F"));

        Label subtitle = new Label(
            "Track your attendance and stay ahead of risk");
        subtitle.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        subtitle.setTextFill(Color.web("#6B7280"));

        // ── Student info display ──────────────────────────────────
        updateStudentInfo(profile);
        studentInfoLabel.setFont(
            Font.font("Arial", FontWeight.NORMAL, 11));
        studentInfoLabel.setTextFill(Color.web("#374151"));

        // ── Profile button ────────────────────────────────────────
        Button profileBtn = new Button("👤 My Profile");
        profileBtn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #2563EB;" +
            "-fx-font-size: 11px;" +
            "-fx-cursor: hand;"
        );
        profileBtn.setOnAction(e -> {
            new ProfilePanel().show(primaryStage, savedProfile -> {
                updateStudentInfo(savedProfile);
            });
        });

        HBox topRow = new HBox(10,
            studentInfoLabel, profileBtn);
        topRow.setAlignment(Pos.CENTER);

        VBox header = new VBox(4, title, subtitle, topRow);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(12, 0, 8, 0));
        header.setStyle(
            "-fx-background-color: #EFF6FF;" +
            "-fx-border-color: #BFDBFE;" +
            "-fx-border-width: 0 0 1 0;"
        );

        // ── Main content ──────────────────────────────────────────
        VBox content = new MainController().buildUI();

        // ── Footer ────────────────────────────────────────────────
        Label footer = new Label(
            "Semester max: 40 classes  |  v2.0.0");
        footer.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
        footer.setTextFill(Color.GRAY);

        VBox root = new VBox(0, header, content, footer);
        root.setStyle("-fx-background-color: #FFFFFF;");

        Scene scene = new Scene(root, 540, 620);
        primaryStage.setTitle("Attendance Risk Calculator");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void updateStudentInfo(StudentProfile profile) {
        if (profile != null) {
            studentInfoLabel.setText(
                "👤 " + profile.getName() +
                "  |  🆔 " + profile.getStudentId() +
                "  |  📚 " + profile.getSemester()
            );
        } else {
            studentInfoLabel.setText(
                "No profile set — click 'My Profile' to add yours");
            studentInfoLabel.setTextFill(Color.GRAY);
        }
    }

    @Override
    public void stop() throws Exception {
        DatabaseConnection.closeConnection();
        System.out.println("✅ App closed cleanly.");
    }

    public static void main(String[] args) {
        launch(args);
    }
}