package org.catcom.classreserver.form;

import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
public class ReserveForm
{
    @NonNull
    private int roomId;

    @NonNull
    private LocalDateTime startTime;

    @NonNull
    private LocalDateTime finishTime;

}
