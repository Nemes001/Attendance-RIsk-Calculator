package threads;

import database.AttendanceDAO;
import model.AttendanceRecord;
import javafx.concurrent.Task;
import java.util.List;

public class LoadWorker extends Task<List<AttendanceRecord>> {

    private final LoadCallback callback;

    public interface LoadCallback {
        void onLoaded(List<AttendanceRecord> records);
        void onFailure(String errorMessage);
    }

    public LoadWorker(LoadCallback callback) {
        this.callback = callback;

        setOnSucceeded(e -> {
            callback.onLoaded(getValue());
        });

        setOnFailed(e -> {
            callback.onFailure("Failed to load: " +
                getException().getMessage());
        });
    }

    @Override
    protected List<AttendanceRecord> call() throws Exception {
        AttendanceDAO dao = new AttendanceDAO();
        return dao.getAllRecords();
    }
}