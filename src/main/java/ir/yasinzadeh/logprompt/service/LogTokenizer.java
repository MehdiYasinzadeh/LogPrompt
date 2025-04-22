package ir.yasinzadeh.logprompt.service;

public class LogTokenizer {
    public static class LogToken {
        String date;
        String time;
        String id;
        String level;
        String clazz;
        String message;

        @Override
        public String toString() {
            return "[date=" + date + ", time=" + time + ", id=" + id + ", level=" + level +
                    ", class=" + clazz + ", message=" + message + "]";
        }
    }

    public static LogToken parseLine(String line) {
        String[] parts = line.split(" ", 6);
        if (parts.length < 6) return null;

        LogToken token = new LogToken();
        token.date = parts[0];
        token.time = parts[1];
        token.id = parts[2];
        token.level = parts[3];
        token.clazz = parts[4].replace(":", "");
        token.message = parts[5];
        return token;
    }


}

