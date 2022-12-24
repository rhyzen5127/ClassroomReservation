package org.catcom.classreserver.form;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@ToString
public class ReserveForm
{
    private int roomId;

    @NonNull
    private ZonedDateTime startTime;

    @NonNull
    private ZonedDateTime finishTime;

    @Nullable
    private String reserveNote;

}
