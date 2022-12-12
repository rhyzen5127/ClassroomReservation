package org.catcom.classreserver.form;

import jakarta.annotation.Nullable;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EditReservationForm
{

    @Nullable
    private Integer roomId;

    @Nullable
    private LocalDateTime startTime;

    @Nullable
    private LocalDateTime finishTime;

    @Nullable
    private String status;


}
