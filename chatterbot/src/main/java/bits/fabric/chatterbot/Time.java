package bits.fabric.chatterbot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Ashish on 12/16/2017.
 */

public class Time {

    public static final String UTC = "UTC";
    public static final String GMT = "GMT";

    public static final String STANDARD_DATE_FORMAT = "yyyy-MM-dd";
    public static final String STANDARD_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";


    public static final String ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static final String STANDARD_DATE_PATTERN = "^\\d{4}-\\d{2}-\\d{2}$";
    public static final String STANDARD_DATE_TIME_PATTERN = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$";


    public Calendar calendar = null;


    public Time(String dateStr, String tz) throws ParseException {
        DateFormat df = null;
        if (dateStr.matches(STANDARD_DATE_PATTERN)) {
            df = new SimpleDateFormat(STANDARD_DATE_FORMAT);
        } else if (dateStr.matches(STANDARD_DATE_TIME_PATTERN)) {
            df = new SimpleDateFormat(STANDARD_DATE_TIME_FORMAT);
        }
        if (tz != null)
            df.setTimeZone(TimeZone.getTimeZone(UTC));
        calendar = Calendar.getInstance();
        calendar.setTime(df.parse(dateStr));
    }

    public Time(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
    }

    public Time(Calendar calendar) {
        this.calendar =calendar;
    }







    //for utc
    public static Time load(String obj) {
        try {
            return new Time(obj, UTC);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Time load() {
        return new Time(Calendar.getInstance(TimeZone.getTimeZone(UTC)));
    }
    public static Time load(Date obj) {
        return new Time(obj);
    }
    //for local
    public static Time loads(String obj) {
        try {
            return new Time(obj, null);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Time loads() {
        return new Time(Calendar.getInstance());
    }
      public static Time loads(Date obj) {
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(obj);
        return new Time(calendar);
    }


    //for utc
    public String formatDate(String tz,String format) {
        DateFormat df = new SimpleDateFormat(format);
        if (tz != null)
            df.setTimeZone(TimeZone.getTimeZone(UTC));
        return df.format(calendar.getTime());
    }

    public String dump(String format) {
          return formatDate(UTC,format);
    }
    public String dump() {
        return formatDate(UTC,STANDARD_DATE_TIME_FORMAT);
    }


    //for local
    public String dumps(String format) {
        return formatDate(null,format);
    }
    public String dumps() {
        return formatDate(null,STANDARD_DATE_TIME_FORMAT);
    }



}
