package logic;

public class AttendanceCalculator {

    public static double calculate(int attended, int total) {
        if (total == 0) return 0.0;
        return Math.round(((double) attended / total) * 10000.0) / 100.0;
        //     ↑ rounds to 2 decimal places properly
    }
}