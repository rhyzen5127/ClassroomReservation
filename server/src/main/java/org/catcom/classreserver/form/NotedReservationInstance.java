package org.catcom.classreserver.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.catcom.classreserver.model.reservation.Reservation;

@Data
@AllArgsConstructor
public class NotedReservationInstance {

    private Reservation reservation;
    private String note;
}
