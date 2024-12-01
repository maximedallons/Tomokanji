package com.tomokanji.repositories;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class LogRepository {
    private static final List<String> logs = new ArrayList<>();

    public static void addLog(String log) {
        Time time = new Time(System.currentTimeMillis());
        String formattedLog = "["+time+"] "+log;
        logs.add(formattedLog);
    }

    public static List<String> getLogs() {
        List<String> reversedLogs = new ArrayList<>();
        for (int i = logs.size()-1; i >= 0; i--) {
            reversedLogs.add(logs.get(i));
        }
        return reversedLogs;
    }
}
