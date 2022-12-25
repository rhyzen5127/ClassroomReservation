package org.catcom.classreserver.form;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class EditClassroomForm
{

    @Nullable
    private String status;

    @Nullable
    private Integer width;

    @Nullable
    private Integer length;

    @Nullable
    private Integer seats;

    @Nullable
    private String note;

}
