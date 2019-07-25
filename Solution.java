package com.javarush.task.task39.task3913;

import java.nio.file.Paths;


public class Solution {
    public static void main(String[] args) {
        LogParser logParser = new LogParser(Paths.get("C:\\Users\\13Black\\Desktop\\Projects\\JavaRushTasks\\4.JavaCollections\\src\\com\\javarush\\task\\task39\\task3913\\logs"));
        System.out.println(logParser.execute("get ip for user = \"Vasya\""));
        System.out.println(logParser.execute("get user for event = \"DONE_TASK\""));
        System.out.println(logParser.execute("get event for date = \"30.01.2014 12:56:22\""));
    }
}