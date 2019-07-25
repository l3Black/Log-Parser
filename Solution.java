package com.javarush.task.task39.task3913;

import java.nio.file.Paths;


public class Solution {
    public static void main(String[] args) {
        LogParser logParser = new LogParser(Paths.get("C:\\Users\\13Black\\Desktop\\Projects\\JavaRushTasks\\4.JavaCollections\\src\\com\\javarush\\task\\task39\\task3913\\logs"));
        System.out.println(logParser.getNumberOfAllEvents( null, null));
        System.out.println(logParser.getAllEvents(null, null));
        System.out.println(logParser.getEventsForIP("192.168.100.2",null, null));
        System.out.println(logParser.getEventsForUser("Eduard Petrovich Morozko",null, null));
        System.out.println(logParser.getFailedEvents(null, null));
        System.out.println(logParser.getErrorEvents(null, null));
        System.out.println(logParser.getNumberOfAttemptToSolveTask(48, null, null));
        System.out.println(logParser.getNumberOfSuccessfulAttemptToSolveTask(48, null, null));
        System.out.println(logParser.getAllSolvedTasksAndTheirNumber(null, null));
        System.out.println(logParser.getAllDoneTasksAndTheirNumber(null, null));

    }
}