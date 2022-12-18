package org.catcom.classreserver.form;

import jakarta.annotation.Nullable;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
public class EditReservationForm
{

    @Nullable
    private Integer roomId;

    @Nullable
    private ZonedDateTime startTime;

    @Nullable
    private ZonedDateTime finishTime;


}
