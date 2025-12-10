import java.util.ArrayList;
import java.util.List;

public enum Command {

    ADD,
    LIST,
    EXIT;

    public static List<String> NAMES = collectNames();

    private static List<String> collectNames() {
        List<String> result = new ArrayList<>();
        for (Command command : values()) {
            result.add(command.name());
        }
        return result;
    }

    public static boolean doesNotContain(String value) {
        if (value == null) {
            return true;
        }
        return !NAMES.contains(value.toUpperCase().trim());
    }

    public static Command fromString(String value) {
        if (value == null) {
            return null;
        }
        return Command.valueOf(value.toUpperCase().trim());
    }
}