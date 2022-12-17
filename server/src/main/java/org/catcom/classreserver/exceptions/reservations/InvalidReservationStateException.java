package org.catcom.classreserver.exceptions.reservations;

public class InvalidReservationStateException extends ReservationException {
    public InvalidReservationStateException(String message) {
        super(message);
    }
}
