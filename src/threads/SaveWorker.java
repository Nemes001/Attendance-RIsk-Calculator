package threads;

import database.AttendanceDAO;
import model.AttendanceRecord;
import javafx.concurrent.Task;

public class SaveWorker extends Task<Boolean> {

    private final AttendanceRecord record;
    private final SaveCallback callback;

    public interface SaveCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }

    public SaveWorker(AttendanceRecord record, SaveCallback callback) {
        this.record   = record;
        this.callback = callback;

        setOnSucceeded(e -> {
            if (getValue()) {
                callback.onSuccess();
            } else {
                callback.onFailure("Record could not be saved.");
            }
        });

        setOnFailed(e -> {
            callback.onFailure("Error: " + getException().getMessage());
        });
    }

    @Override
    protected Boolean call() throws Exception {
        AttendanceDAO dao = new AttendanceDAO();
        return dao.saveRecord(record);
    }
}