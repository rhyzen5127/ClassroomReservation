package model;

public class Classroom
{
    
    // region private
    private String roomID;
    private String buildingID;
    
    private int[] size = new int[2];
    private int seats;
    private Status status = Status.AVAILABLE;

    /* 
     * Status type : 0 -> Normal
     * Status type : 1 -> Input Error
     * ...
     */
    private int statusCode = 0;
    
    // region constructer
    public Classroom(String roomID, String buildingID, int width, int length, int seats)
    {
        this.roomID = roomID;
        this.buildingID = buildingID;
        this.size[0] = width;
        this.size[1] = length;
        this.seats = seats;
    }
    
    // region setter
    public void setRoomID(String roomID)
    {
        this.roomID = roomID;
    }

    public void setBuildingID(String buildingID)
    {
        this.buildingID = buildingID;
    }

    public void setSize(int width, int length)
    {
        this.size[0] = width;
        this.size[1] = length;
    }

    public void setSeats(int seats)
    {
        if(seats < 0) {
            /* Display Err. */
            this.statusCode = 1;
            return;
        }
        else this.seats = seats;
    }

    // region getter
    public String getRoomID()
    {
        return this.roomID;
    }

    public String getBuildingID()
    {
        return this.buildingID;
    }

    public int[] getSize()
    {
        return size;
    }
    
    public int getSeats()
    {
        return this.seats;
    }

    public int getStatusCode()
    {
        return statusCode;
    }

    public boolean isAvailable() {
        return this.status == Status.AVAILABLE;
    }
    public enum Status {
        AVAILABLE,
        ON_MAINTAINANCE
    }
}