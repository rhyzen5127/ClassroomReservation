package org.catcom.classreserver.form;

import lombok.Data;
import lombok.NonNull;
import org.springframework.data.web.SortDefault;

@Data
public class ExportScheduleForm
{

    private NotedReservation[] reservations;

}
