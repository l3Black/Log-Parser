package com.javarush.task.task39.task3913;

import com.javarush.task.task39.task3913.query.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogParser implements IPQuery, UserQuery, DateQuery, EventQuery, QLQuery {
    private Path logDir;

    public LogParser(Path logDir) {
        this.logDir = logDir;
    }

    //Methods from IPQuery
    @Override
    public int getNumberOfUniqueIPs(Date after, Date before) {
        return getUniqueIPs(after, before).size();
    }

    @Override
    public Set<String> getUniqueIPs(Date after, Date before) {
        List<String> list = getLogsBetweenDate(after, before, true);
        Set<String> result = new HashSet<>();
        for (String s : list){
            result.add(getIpFromLog(s));
        }
        return result;
    }

    @Override
    public Set<String> getIPsForUser(String user, Date after, Date before) {
        List<String> list = getLogsBetweenDate(after, before, true);
        Set<String> result = new HashSet<>();
        for (String s : list){
            if (getUserNameFromLog(s).equals(user))
                result.add(getIpFromLog(s));
        }
        return result;
    }

    @Override
    public Set<String> getIPsForEvent(Event event, Date after, Date before) {
        List<String> list = getLogsBetweenDate(after, before, true);
        Set<String> result = new HashSet<>();
        for (String s : list){
            if (getEventFromLog(s) == event)
                result.add(getIpFromLog(s));
        }
        return result;
    }

    @Override
    public Set<String> getIPsForStatus(Status status, Date after, Date before) {
        List<String> list = getLogsBetweenDate(after, before, true);
        Set<String> result = new HashSet<>();
        for (String s : list){
            if (getStatusFromLog(s) == status)
                result.add(getIpFromLog(s));
        }
        return result;
    }

    //Methods from UserQuery
    @Override
    public Set<String> getAllUsers() {
        Set<String> result = new HashSet<>();
        for (String s : getLogsFromPath()){
            result.add(getUserNameFromLog(s));
        }
        return result;
    }

    @Override
    public int getNumberOfUsers(Date after, Date before) {
        Set<String> result = new HashSet<>();
        for (String s : getLogsBetweenDate(after, before, true)){
            result.add(getUserNameFromLog(s));
        }
        return result.size();
    }

    @Override
    public int getNumberOfUserEvents(String user, Date after, Date before) {
        Set<Event> result = new HashSet<>();
        for (String s : getLogsBetweenDate(after, before, true)){
            if (user.equals(getUserNameFromLog(s))){
                result.add(getEventFromLog(s));
            }
        }
        return result.size();
    }

    @Override
    public Set<String> getUsersForIP(String ip, Date after, Date before) {
        Set<String> result = new HashSet<>();
        for (String s : getLogsBetweenDate(after, before, true)){
            if (ip.equals(getIpFromLog(s))){
                result.add(getUserNameFromLog(s));
            }
        }
        return result;
    }

    @Override
    public Set<String> getLoggedUsers(Date after, Date before) {
        Set<String> result = new HashSet<>();
        for (String s : getLogsBetweenDate(after, before, true)){
            if (Event.LOGIN == getEventFromLog(s))
                result.add(getUserNameFromLog(s));
        }
        return result;
    }

    @Override
    public Set<String> getDownloadedPluginUsers(Date after, Date before) {
        Set<String> result = new HashSet<>();
        for (String s : getLogsBetweenDate(after, before, true)){
            if (Event.DOWNLOAD_PLUGIN == getEventFromLog(s)){
                result.add(getUserNameFromLog(s));
            }
        }
        return result;
    }

    @Override
    public Set<String> getWroteMessageUsers(Date after, Date before) {
        Set<String> result = new HashSet<>();
        for (String s : getLogsBetweenDate(after, before, true)){
            if (Event.WRITE_MESSAGE == getEventFromLog(s)){
                result.add(getUserNameFromLog(s));
            }
        }
        return result;
    }

    @Override
    public Set<String> getSolvedTaskUsers(Date after, Date before) {
        Set<String> result = new HashSet<>();
        for (String s : getLogsBetweenDate(after, before, true)){
            if (Event.SOLVE_TASK == getEventFromLog(s)){
                result.add(getUserNameFromLog(s));
            }
        }
        return result;
    }

    @Override
    public Set<String> getSolvedTaskUsers(Date after, Date before, int task) {
        Set<String> result = new HashSet<>();
        for (String s : getLogsBetweenDate(after, before, true)){
            if (getEventFromLog(s) == Event.SOLVE_TASK && task == getTaskFromLog(s)){
                result.add(getUserNameFromLog(s));
            }
        }
        return result;
    }

    @Override
    public Set<String> getDoneTaskUsers(Date after, Date before) {
        Set<String> result = new HashSet<>();
        for (String s : getLogsBetweenDate(after, before, true)){
            if (Event.DONE_TASK == getEventFromLog(s)){
                result.add(getUserNameFromLog(s));
            }
        }
        return result;
    }

    @Override
    public Set<String> getDoneTaskUsers(Date after, Date before, int task) {
        Set<String> result = new HashSet<>();
        for (String s : getLogsBetweenDate(after, before, true)){
            if (getEventFromLog(s) == Event.DONE_TASK && task == getTaskFromLog(s)){
                result.add(getUserNameFromLog(s));
            }
        }
        return result;
    }

    //Methods from DateQuery
    @Override
    public Set<Date> getDatesForUserAndEvent(String user, Event event, Date after, Date before) {
        Set<Date> result = new HashSet<>();
        for (String s : getLogsBetweenDate(after, before, true)){
            if (user.equals(getUserNameFromLog(s)) && event == getEventFromLog(s)){
                result.add(getDateFromLog(s));
            }
        }
        return result;
    }

    @Override
    public Set<Date> getDatesWhenSomethingFailed(Date after, Date before) {
        Set<Date> result = new HashSet<>();
        for (String s : getLogsBetweenDate(after, before, true)){
            if (Status.FAILED == getStatusFromLog(s)){
                result.add(getDateFromLog(s));
            }
        }
        return result;
    }

    @Override
    public Set<Date> getDatesWhenErrorHappened(Date after, Date before) {
        Set<Date> result = new HashSet<>();
        for (String s : getLogsBetweenDate(after, before, true)){
            if (Status.ERROR == getStatusFromLog(s)){
                result.add(getDateFromLog(s));
            }
        }
        return result;
    }

    @Override
    public Date getDateWhenUserLoggedFirstTime(String user, Date after, Date before) {
        Date result = null;
        for (String s : getLogsBetweenDate(after, before, true)){
            if (user.equals(getUserNameFromLog(s)) && Event.LOGIN == getEventFromLog(s)){
                Date date = getDateFromLog(s);
                if (result == null || result.getTime() > date.getTime())
                    result = date;
            }
        }
        return result;
    }

    @Override
    public Date getDateWhenUserSolvedTask(String user, int task, Date after, Date before) {
        Date result = null;
        for (String s : getLogsBetweenDate(after, before, true)){
            if (user.equals(getUserNameFromLog(s))
                    && Event.SOLVE_TASK == getEventFromLog(s)
                    && task == getTaskFromLog(s)){
                Date date = getDateFromLog(s);
                if (result == null || result.getTime() > date.getTime()){
                    result = date;
                }
            }
        }
        return result;
    }

    @Override
    public Date getDateWhenUserDoneTask(String user, int task, Date after, Date before) {
        Date result = null;
        for (String s : getLogsBetweenDate(after, before, true)){
            if (user.equals(getUserNameFromLog(s))
                    && Event.DONE_TASK == getEventFromLog(s)
                    && task == getTaskFromLog(s)){
                Date date = getDateFromLog(s);
                if (result == null || result.getTime() > date.getTime()){
                    result = date;
                }
            }
        }
        return result;
    }

    @Override
    public Set<Date> getDatesWhenUserWroteMessage(String user, Date after, Date before) {
        Set<Date> result = new HashSet<>();
        for (String s : getLogsBetweenDate(after, before, true)){
            if (Event.WRITE_MESSAGE == getEventFromLog(s) && user.equals(getUserNameFromLog(s)))
                result.add(getDateFromLog(s));
        }
        return result;
    }

    @Override
    public Set<Date> getDatesWhenUserDownloadedPlugin(String user, Date after, Date before) {
        Set<Date> result = new HashSet<>();
        for (String s : getLogsBetweenDate(after, before, true)){
            if (Event.DOWNLOAD_PLUGIN == getEventFromLog(s) && user.equals(getUserNameFromLog(s)))
                result.add(getDateFromLog(s));
        }
        return result;
    }

    //Methods for EventQuery
    @Override
    public int getNumberOfAllEvents(Date after, Date before) {
        return getAllEvents(after, before).size();
    }

    @Override
    public Set<Event> getAllEvents(Date after, Date before) {
        Set<Event> result = new HashSet<>();
        for (String s : getLogsBetweenDate(after, before,true)){
            result.add(getEventFromLog(s));
        }
        return result;
    }

    @Override
    public Set<Event> getEventsForIP(String ip, Date after, Date before) {
        Set<Event> result = new HashSet<>();
        for (String s : getLogsBetweenDate(after, before, true)){
            if (ip.equals(getIpFromLog(s)))
                result.add(getEventFromLog(s));
        }
        return result;
    }

    @Override
    public Set<Event> getEventsForUser(String user, Date after, Date before) {
        Set<Event> result = new HashSet<>();
        for (String s : getLogsBetweenDate(after, before, true)){
            if (user.equals(getUserNameFromLog(s)))
                result.add(getEventFromLog(s));
        }
        return result;
    }

    @Override
    public Set<Event> getFailedEvents(Date after, Date before) {
        Set<Event> result = new HashSet<>();
        for (String s : getLogsBetweenDate(after, before, true)){
            if (Status.FAILED == getStatusFromLog(s))
                result.add(getEventFromLog(s));
        }
        return result;
    }

    @Override
    public Set<Event> getErrorEvents(Date after, Date before) {
        Set<Event> result = new HashSet<>();
        for (String s : getLogsBetweenDate(after, before, true)){
            if (Status.ERROR == getStatusFromLog(s))
                result.add(getEventFromLog(s));
        }
        return result;
    }

    @Override
    public int getNumberOfAttemptToSolveTask(int task, Date after, Date before) {
        int count = 0;
        for (String s : getLogsBetweenDate(after, before, true)){
            if (Event.SOLVE_TASK == getEventFromLog(s) && task == getTaskFromLog(s)){
                count++;
            }
        }
        return count;
    }

    @Override
    public int getNumberOfSuccessfulAttemptToSolveTask(int task, Date after, Date before) {
        int count = 0;
        for (String s : getLogsBetweenDate(after, before, true)){
            if (Event.DONE_TASK == getEventFromLog(s) && task == getTaskFromLog(s)){
                count++;
            }
        }
        return count;
    }

    @Override
    public Map<Integer, Integer> getAllSolvedTasksAndTheirNumber(Date after, Date before) {
        Map<Integer, Integer> result = new HashMap<>();
        for (String s : getLogsBetweenDate(after, before, true)){
            if (Event.SOLVE_TASK == getEventFromLog(s)){
                int task = getTaskFromLog(s);
                result.put(task, getNumberOfAttemptToSolveTask(task, after, before));
            }
        }
        return result;
    }

    @Override
    public Map<Integer, Integer> getAllDoneTasksAndTheirNumber(Date after, Date before) {
        Map<Integer, Integer> result = new HashMap<>();
        for (String s : getLogsBetweenDate(after, before, true)){
            if (Event.DONE_TASK == getEventFromLog(s)){
                int task = getTaskFromLog(s);
                result.put(task, getNumberOfSuccessfulAttemptToSolveTask(task, after, before));
            }
        }
        return result;
    }

    //Methods for QLQuery
    @Override
    public Set<Object> execute(String query) {
        Set<Object> result = new HashSet<>();
        switch (query.toLowerCase()){
            case "get ip":
                return new HashSet<>(getUniqueIPs(null, null));
            case "get user":
                return new HashSet<>(getAllUsers());
            case "get date":
                Set<Object> resultDate = new HashSet<>();
                for (String s : getLogsFromPath()){
                    resultDate.add(getDateFromLog(s));
                }
                return resultDate;
            case "get event":
                return new HashSet<>(getAllEvents(null, null));
            case "get status":
                Set<Object> resultStatus = new HashSet<>();
                for (String s : getLogsFromPath()){
                    resultStatus.add(getStatusFromLog(s));
                }
                return resultStatus;
        }

        String field1 = query.split("\\s")[1].trim();
        String field2 = query.split("\\s")[3].trim();
        String value = query.split("=")[1].trim();
        Date after = null;
        Date before = null;
        if (value.contains("and date between")){
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                after = sdf.parse(value.split(" \"|\" ")[2].trim());
                before = sdf.parse(value.split(" \"|\" ")[4].trim());
                value = value.split(" \"|\" ")[0].replaceAll("\"", "").trim();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else value = value.replaceAll("\"", "").trim();

        switch (field2.toLowerCase()){
            case "ip":
                for (String s : getLogsBetweenDate(after, before, false)){
                    if (getIpFromLog(s).trim().equals(value))
                        result.add(getField(s, field1));
                }
                return result;
            case "user" :
                for (String s : getLogsBetweenDate(after, before, false)){
                    if (getUserNameFromLog(s).trim().equals(value))
                        result.add(getField(s, field1));
                }
                return result;
            case "date" :
                try {
                    Date date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(value);
                    for (String s : getLogsBetweenDate(after, before, false)){
                        if (getDateFromLog(s).equals(date))
                            result.add(getField(s, field1));
                    }
                    return result;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            case "event" :
                for (String s : getLogsBetweenDate(after, before, false)){
                    if (getEventFromLog(s) == Event.valueOf(value))
                        result.add(getField(s, field1));
                }
                return result;
            case "status" :
                for (String s : getLogsBetweenDate(after, before, false)){
                    if (getStatusFromLog(s) == Status.valueOf(value))
                        result.add(getField(s, field1));
                }
                return result;
        }
        return result;
    }

    //Methods for help
    private Object getField(String log, String field){
        switch (field.toLowerCase()){
            case "ip" :
                return getIpFromLog(log);
            case "user" :
                return getUserNameFromLog(log);
            case "date" :
                return getDateFromLog(log);
            case "event" :
                return getEventFromLog(log);
            case "status" :
                return getStatusFromLog(log);
                default:
                    return null;
        }
    }

    private String getIpFromLog(String log){
        String[] strings = log.split("\\s", 2);
        return strings[0];
    }

    private String getUserNameFromLog(String log){
        Pattern p = Pattern.compile("[a-zA-Z ]+");
        Matcher m = p.matcher(log);
        m.find();
        return m.group();
    }

    private Date getDateFromLog(String log){
        Pattern p = Pattern.compile("[\\d]+.[\\d]+.[\\d]+ [\\d]+:[\\d]+:[\\d]+");
        Matcher m = p.matcher(log);
        m.find();
        String s = m.group();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(s);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private Event getEventFromLog(String log){
        Pattern p = Pattern.compile("LOGIN|DOWNLOAD_PLUGIN|WRITE_MESSAGE|SOLVE_TASK|DONE_TASK");
        Matcher m = p.matcher(log);
        m.find();
        return Event.valueOf(m.group());
    }

    private Status getStatusFromLog(String log){
        Pattern p = Pattern.compile("OK|FAILED|ERROR");
        Matcher m = p.matcher(log);
        m.find();
        return Status.valueOf(m.group());
    }

    private List<String> getLogsFromPath(){
        List<String> result = new ArrayList<>();
        File[] files = logDir.toFile().listFiles(p -> p.getName().endsWith(".log"));
        for (File f : files){
            try {
                result.addAll(Files.readAllLines(f.toPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private List<String> getLogsBetweenDate(Date after, Date before, boolean include){
        List<String> list = getLogsFromPath();
        List<String> result = new ArrayList<>();
        for (String s : list){
            Date date = getDateFromLog(s);
            if (include){
                if ((after == null || date.getTime() >= after.getTime()) && (before == null || date.getTime() <= before.getTime())){
                    result.add(s);
                }
            } else if ((after == null || date.getTime() > after.getTime()) && (before == null || date.getTime() < before.getTime())){
                result.add(s);
            }
        }
        return result;
    }

    private int getTaskFromLog(String log){
        Pattern p = Pattern.compile("DONE_TASK\\s\\d+|SOLVE_TASK\\s\\d+");
        Matcher m = p.matcher(log);
        m.find();
        return Integer.parseInt(m.group().split("\\s")[1]);
    }

}