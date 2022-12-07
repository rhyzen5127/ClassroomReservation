package model;

import java.util.HashMap;
import java.util.Date;

class ReservationManager {

	private final HashMap<String, Reservation> reservations = new HashMap<>();
	private final ClassroomManager classroomManager;

	public ReservationManager(ClassroomManager classroomManager)
    {
		this.classroomManager = classroomManager;
	}

	public boolean reserve(String roomID, User reserver, Date reserveDateFrom, Date reserveDateTo)
    {
		
		return false;
	}

	private void addReservation(Reservation reservation)
    {
		reservations.put(reservation.getID(), reservation);
	}

	private boolean isRoomAvailable(String roomID, Date date)
    {
		var room = classroomManager.getRoom(roomID);

		if (!room.isAvailable())
		{
			return false;
		}

		for (var reservation : reservations.values())
        {
            /* code */
		}

        return Math.random()>0.5 ? false : true;
	}

}
    /*
    private void removeReservation()
    {
        Reservation reservation        reservation.remdel.sets.remove()reservationg,g.getDIID)()));reservationreservationreservation.getID(), reservation
    }
    */
    
    