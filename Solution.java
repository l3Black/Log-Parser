package com.javarush.task.task39.task3913;

import java.nio.file.Paths;


public class Solution {
    public static void main(String[] args) {
        LogParser logParser = new LogParser(Paths.get("C:\\Users\\13Black\\Desktop\\Projects\\JavaRushTasks\\4.JavaCollections\\src\\com\\javarush\\task\\task39\\task3913\\logs"));
        System.out.println(logParser.getDatesForUserAndEvent("Eduard Petrovich Morozko", Event.WRITE_MESSAGE, null, null));
        System.out.println(logParser.getDatesWhenSomethingFailed(null, null));
        System.out.println(logParser.getDatesWhenErrorHappened(null, null));
        System.out.println(logParser.getDateWhenUserLoggedFirstTime("Eduard Petrovich Morozko",null, null));
        System.out.println(logParser.getDateWhenUserSolvedTask("Eduard Petrovich Morozko", 48, null, null));
        System.out.println(logParser.getDateWhenUserDoneTask("Eduard Petrovich Morozko", 48, null, null));
        System.out.println(logParser.getDatesWhenUserWroteMessage("Eduard Petrovich Morozko",null, null));
        System.out.println(logParser.getDatesWhenUserDownloadedPlugin("Eduard Petrovich Morozko",null,null));
    }
}