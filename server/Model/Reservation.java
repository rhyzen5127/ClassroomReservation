package model;

import java.util.Date;

public class Reservation
{

    // region private
    private String reservationID;
    private Date date;
    private String subject = "";
    private String lecturer = "";

    /* 
     * Status type : 0 -> Normal
     * Status type : 1 -> Input Error
     * ...
     */
    private int statusCode = 0;

    // region constructer
    public Reservation(String id, int dd, int mm, int yyyy, int hr, int min, String subject, String lecturer)
    {
        checkDate(dd, mm, yyyy);
        checkTime(hr, min);
        if(statusCode == 1) return;
        this.reservationID = id;
        this.date = new Date();
        this.subject = subject;
        this.lecturer = lecturer;
    }

    // region public methods
    public void checkDate(int day, int month, int year)
    {
        if(day > 31 || month > 12) {
            /* Display Err. */
            this.statusCode = 1;
            return;
        }
    }

    public void checkTime(int hour, int minute)
    {
        if(hour > 24 || minute > 60) {
            /* Display Err. */
            this.statusCode = 1;
            return;
        }
    }

    // region setter
    /*
    public void setDate(int dd, int mm, int yyyy)
    {
        checkDate(dd, mm, yyyy);
        if(statusCode == 1) return;
        this.date[0] = dd;
        this.date[1] = mm;
        this.date[2] = yyyy;
    }

    public void setTime(int hr, int min)
    {
        checkTime(hr, min);
        if(statusCode == 1) return;
        this.time[0] = hr;
        this.time[1] = min;
    }
    */
    
    // region getter

    public String getID() {
        return reservationID;
    }

    /*
    public int[] getDate()
    {
        return date;
    }

    public int[] getTime()
    { 
        return time;
    }

    public int getDay()
    {
        return date[0];
    }

    public int getMonth()
    {
        return date[1];
    }

    public int getYear()
    {
        return date[2];
    }

    public int getHour()
    {
        return time[0];
    }

    public int getMinute()
    {
        return time[0];
    }
    */

    public String getSubject()
    {
        return subject;
    }

    public String getLecturer()
    {
        return lecturer;
    }

    public int getStatusCode()
    {
        return statusCode;
    }
}