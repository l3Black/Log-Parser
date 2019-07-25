package com.javarush.task.task39.task3913;

import com.javarush.task.task39.task3913.query.IPQuery;
import com.javarush.task.task39.task3913.query.UserQuery;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogParser implements IPQuery, UserQuery {
    private Path logDir;

    public LogParser(Path logDir) {
        this.logDir = logDir;
    }

    @Override
    public int getNumberOfUniqueIPs(Date after, Date before) {
        return getUniqueIPs(after, before).size();
    }

    @Override
    public Set<String> getUniqueIPs(Date after, Date before) {
        List<String> list = getLogsBetweenDate(after, before);
        Set<String> result = new HashSet<>();
        for (String s : list){
            result.add(getIpFromLog(s));
        }
        return result;
    }

    @Override
    public Set<String> getIPsForUser(String user, Date after, Date before) {
        List<String> list = getLogsBetweenDate(after, before);
        Set<String> result = new HashSet<>();
        for (String s : list){
            if (getUserNameFromLog(s).equals(user))
                result.add(getIpFromLog(s));
        }
        return result;
    }

    @Override
    public Set<String> getIPsForEvent(Event event, Date after, Date before) {
        List<String> list = getLogsBetweenDate(after, before);
        Set<String> result = new HashSet<>();
        for (String s : list){
            if (getEventFromLog(s).equals(event.toString()))
                result.add(getIpFromLog(s));
        }
        return result;
    }

    @Override
    public Set<String> getIPsForStatus(Status status, Date after, Date before) {
        List<String> list = getLogsBetweenDate(after, before);
        Set<String> result = new HashSet<>();
        for (String s : list){
            if (getStatusFromLog(s).equals(status.toString()))
                result.add(getIpFromLog(s));
        }
        return result;
    }

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
        for (String s : getLogsBetweenDate(after, before)){
            result.add(getUserNameFromLog(s));
        }
        return result.size();
    }

    @Override
    public int getNumberOfUserEvents(String user, Date after, Date before) {
        Set<String> result = new HashSet<>();
        for (String s : getLogsBetweenDate(after, before)){
            if (user.equals(getUserNameFromLog(s))){
                result.add(getEventFromLog(s));
            }
        }
        return result.size();
    }

    @Override
    public Set<String> getUsersForIP(String ip, Date after, Date before) {
        Set<String> result = new HashSet<>();
        for (String s : getLogsBetweenDate(after, before)){
            if (ip.equals(getIpFromLog(s))){
                result.add(getUserNameFromLog(s));
            }
        }
        return result;
    }

    @Override
    public Set<String> getLoggedUsers(Date after, Date before) {
        Set<String> result = new HashSet<>();
        for (String s : getLogsBetweenDate(after, before)){
            if (Event.LOGIN.toString().equals(getEventFromLog(s)))
                result.add(getUserNameFromLog(s));
        }
        return result;
    }

    @Override
    public Set<String> getDownloadedPluginUsers(Date after, Date before) {
        Set<String> result = new HashSet<>();
        for (String s : getLogsBetweenDate(after, before)){
            if (Event.DOWNLOAD_PLUGIN.toString().equals(getEventFromLog(s))){
                result.add(getUserNameFromLog(s));
            }
        }
        return result;
    }

    @Override
    public Set<String> getWroteMessageUsers(Date after, Date before) {
        Set<String> result = new HashSet<>();
        for (String s : getLogsBetweenDate(after, before)){
            if (Event.WRITE_MESSAGE.toString().equals(getEventFromLog(s))){
                result.add(getUserNameFromLog(s));
            }
        }
        return result;
    }

    @Override
    public Set<String> getSolvedTaskUsers(Date after, Date before) {
        Set<String> result = new HashSet<>();
        for (String s : getLogsBetweenDate(after, before)){
            if (Event.SOLVE_TASK.toString().equals(getEventFromLog(s))){
                result.add(getUserNameFromLog(s));
            }
        }
        return result;
    }

    @Override
    public Set<String> getSolvedTaskUsers(Date after, Date before, int task) {
        Set<String> result = new HashSet<>();
        for (String s : getLogsBetweenDate(after, before)){
            Pattern p = Pattern.compile("SOLVE_TASK\\s\\d+");
            Matcher m = p.matcher(s);
            if (m.find()){
                if (task == Integer.parseInt(m.group().split("\\s")[1])){
                    result.add(getUserNameFromLog(s));
                }
            }
        }
        return result;
    }

    @Override
    public Set<String> getDoneTaskUsers(Date after, Date before) {
        Set<String> result = new HashSet<>();
        for (String s : getLogsBetweenDate(after, before)){
            if (Event.DONE_TASK.toString().equals(getEventFromLog(s))){
                result.add(getUserNameFromLog(s));
            }
        }
        return result;
    }

    @Override
    public Set<String> getDoneTaskUsers(Date after, Date before, int task) {
        Set<String> result = new HashSet<>();
        for (String s : getLogsBetweenDate(after, before)){
            Pattern p = Pattern.compile("DONE_TASK\\s\\d+");
            Matcher m = p.matcher(s);
            if (m.find()){
                if (task == Integer.parseInt(m.group().split("\\s")[1])){
                    result.add(getUserNameFromLog(s));
                }
            }
        }
        return result;
    }

    //Methods for help
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
        return null;
    }

    private String getEventFromLog(String log){
        Pattern p = Pattern.compile("LOGIN|DOWNLOAD_PLUGIN|WRITE_MESSAGE|SOLVE_TASK|DONE_TASK");
        Matcher m = p.matcher(log);
        m.find();
        return m.group();
    }

    private String getStatusFromLog(String log){
        Pattern p = Pattern.compile("OK|FAILED|ERROR");
        Matcher m = p.matcher(log);
        m.find();
        return m.group();
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

    private List<String> getLogsBetweenDate(Date after, Date before){
        List<String> list = getLogsFromPath();
        List<String> result = new ArrayList<>();
        for (String s : list){
            Date date = getDateFromLog(s);
            if ((after == null || date.getTime() >= after.getTime()) && (before == null || date.getTime() <= before.getTime())){
                result.add(s);
            }
        }
        return result;
    }


}