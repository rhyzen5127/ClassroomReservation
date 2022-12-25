package org.catcom.classreserver.service;

import org.catcom.classreserver.model.reservation.ReservationRepos;
import org.catcom.classreserver.model.reservation.ReservationStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.springframework.data.jpa.domain.Specification.not;
import static org.catcom.classreserver.model.reservation.ReservationRepos.*;
import static org.catcom.classreserver.model.reservation.ReservationStatus.*;

@Component
public class ReservationTaskSchedules
{

    private final Logger log = LoggerFactory.getLogger(ReservationTaskSchedules.class);

    @Autowired
    private ReservationRepos reservationRepos;

    // Run every hour at 0 min, 0 sec
    @Scheduled(cron = "0 0 * * * *")
    public void deleteRejectedReservationPastUseTime()
    {

        log.info("Cleaning up rejected & canceled reservations...");

        var lastWeek = LocalDateTime.now().minus(1, ChronoUnit.WEEKS);

        var toBeDeleted = reservationRepos.findAll(
                hasStatus(REJECTED).or(hasStatus(CANCELED)).and(not(approvedSince(lastWeek)))
        );

        reservationRepos.deleteAll(toBeDeleted);

        log.info("Cleaned up rejected & canceled reservations.");

    }

    // Run every hour at 0 min, 0 sec
    @Scheduled(cron = "0 0 * * * *")
    public void rejectUnapprovedReservationPastUseTime()
    {

        log.info("Rejecting all pending reservations past their use time...");

        var now = LocalDateTime.now();
        var toBeRejected = reservationRepos.findAll(
                hasStatus(PENDING).and(roomUsedBefore(now))
        );

        for (var reservation : toBeRejected)
        {
            reservation.setRejected();
            reservation.setApproveNote("ตอนนี้เลยเวลาที่จะใช้ห้องเรียนในการจองครั้งนี้ไปแล้ว");
            reservation.setApproveTime(now);
        }

        reservationRepos.saveAll(toBeRejected);

        log.info("Rejected all pending reservations past their use time.");
    }

}
