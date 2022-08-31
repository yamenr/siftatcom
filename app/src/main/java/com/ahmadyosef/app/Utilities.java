package com.ahmadyosef.app;

import static com.ahmadyosef.app.data.ShiftType.*;

import android.app.Notification;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.ahmadyosef.app.data.ShiftType;
import com.ahmadyosef.app.data.ShiftUser;
import com.ahmadyosef.app.data.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

enum ErrorCodes
{
    IncorrectAuth, FieldsEmpty, True, False
}

public class Utilities {
    private static Utilities instance;
    private final String restsStr = "restaurants";
    private final String usersStr = "users";
    private static final String TAG = "Utilities";

    public Utilities() {
    }

    public static Utilities getInstance() {
        if (instance == null)
            instance = new Utilities();

        return instance;
    }

    public Date convertDate(String dateStr)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = null;
        try {
            date = format.parse(dateStr);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public LocalDate convertLocalDate(String dateStr)
    {
        String[] str = dateStr.split("-");
        LocalDate date2 = LocalDate.of(Integer.parseInt(str[0]), Integer.parseInt(str[1]), Integer.parseInt(str[2]));
        return date2;


        /*
        LocalDate date = null;
        String regex = " at:(\\d{4}-\\d{2}-\\d{2}) Notes:";
        Matcher m = Pattern.compile(regex).matcher(dateStr);
        if (m.find()) {
            date = LocalDate.parse(m.group(1));
        } else {
            Log.e(TAG, "Date error string!");
        }

        return date;*/
    }

    public long getMilliSecsForCalendar(LocalDate date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, date.getYear());
        calendar.set(Calendar.MONTH, date.getMonthValue());
        calendar.set(Calendar.DAY_OF_MONTH, date.getDayOfMonth());

        return calendar.getTimeInMillis();
    }

    public ArrayList<String> usersList(ArrayList<User> users) {
        ArrayList<String> userList = new ArrayList<>();

        for(User user: users)
        {
            if (!userList.contains(user.getUsername()))
                userList.add(user.getUsername());
        }

        return userList;
    }

    public String sayHello()
    {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 4 && timeOfDay < 12)
        {
            return "Good Morning";
        }
        else if(timeOfDay >= 12 && timeOfDay < 18){
            return "Good Afternoon";
        }
        else if(timeOfDay >= 18 && timeOfDay < 23){
            return "Good Evening";
        }
        else
            return "Good Night";
    }

    public LocalTime getShiftStartTime(ShiftType type)
    {
        LocalTime time = null;
        switch (type)
        {
            case Morning:
                time = LocalTime.of(8, 0);
                break;
            case Afternoon:
                time = LocalTime.of(16, 0);
                break;
            case Night:
                time = LocalTime.of(0, 0);
                break;
        }

        return time;
    }
}