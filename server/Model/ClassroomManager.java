package model;

import java.util.HashMap;

public class ClassroomManager
{

    // region private variable.
    private HashMap<String, Classroom> classrooms = new HashMap<String, Classroom>();

    // region public
    public void readClassroomFile()
    {
        // Excel input here.
        addRoom(new Classroom("219", "SC-04", 5 , 10, 10));
    }

    public String toString(String room)
    {
        return "Room : "+classrooms.get(room).getRoomID()+
                "\nBuilding : "+classrooms.get(room).getBuildingID()+
                "\nSize : "+classrooms.get(room).getSize()+
                "\nSeats : "+classrooms.get(room).getSeats();
    }

    //region getter
    public Classroom getRoom(String id) 
    {
        return classrooms.get(id);
    }

    public String getBuilding(String id)
    {
        return classrooms.get(id).getBuildingID();
    }

    public int[] getSize(String id)
    {
        return classrooms.get(id).getSize();
    }

    public int getSeats(String id)
    {
        return classrooms.get(id).getSeats();
    }

    //region setter
    public void setRoomID(String id, String roomID)
    {
        classrooms.get(id).setRoomID(roomID);
    }

    public void setBuildingID(String id, String buildingID)
    {
        classrooms.get(id).setBuildingID(buildingID);
    }

    public void setSize(String id, int width, int length)
    {
        classrooms.get(id).setSize(width, length);
    }

    public void setSeats(String id, int seats)
    {
        classrooms.get(id).setSeats(seats);
    }

    // register helper
    private void addRoom(Classroom classroom) {
        classrooms.put(classroom.getRoomID(), classroom);
    }
}
