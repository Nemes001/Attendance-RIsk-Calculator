package logic;

public class SuggestionEngine {

    // ── Semester limit — max classes in a semester ───────────────
    private static final int MAX_CLASSES_PER_SEMESTER = 40;

    public static String suggest(int attended, int total,
                                  double required) {

        // Already safe
        double current = ((double) attended / total) * 100;
        if (current >= required) {
            return "You are safe! Keep attending regularly.";
        }

        // ── How many classes are left in semester ─────────────────
        int classesLeft = MAX_CLASSES_PER_SEMESTER - total;

        // No classes left in semester
        if (classesLeft <= 0) {
            return "⚠ No classes left this semester. " +
                   "Talk to your professor immediately.";
        }

        // ── Direct formula to find extra classes needed ───────────
        // x = (required * total - 100 * attended) / (100 - required)
        double x = (required * total - 100.0 * attended)
                 / (100.0 - required);
        int extraClasses = (int) Math.ceil(x);

        // ── Check if recovery is possible within semester ─────────
        if (extraClasses > classesLeft) {
            return "⚠ Cannot reach safe zone this semester. " +
                   "You need " + extraClasses + " classes but only " +
                   classesLeft + " are left. " +
                   "Contact your professor.";
        }

        return "Attend next " + extraClasses +
               " class(es) to reach safe zone. " +
               "(" + classesLeft + " classes remaining this semester)";
    }
}