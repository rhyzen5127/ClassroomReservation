package org.catcom.classreserver.model.reservation;

import java.sql.Timestamp;

public class Reservation {
    private Integer id;
    private Integer owner; // ref -> User.id
    private Integer room; // ref -> Classroom.id
    private Timestamp bookingTime; // time when user make a reservation
    private Timestamp timeStart; // time to start using room
    private Timestamp timeFinish; // time to finish using room
    private String status; // ( pending | approved | rejected )
}
