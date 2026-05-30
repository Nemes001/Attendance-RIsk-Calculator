package logic;

public class RiskEvaluator {

    // ── Named constant — easy to change later ───────────────────
    private static final double WARNING_BUFFER = 10.0;

    public static String evaluate(double attendancePercent,
                                  double requiredPercent) {

        if (attendancePercent >= requiredPercent) {
            return "SAFE";

        } else if (attendancePercent >= requiredPercent - WARNING_BUFFER) {
            return "WARNING";

        } else {
            return "CRITICAL";
        }
    }
}