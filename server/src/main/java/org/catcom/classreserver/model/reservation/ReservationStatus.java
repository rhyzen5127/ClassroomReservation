package org.catcom.classreserver.model.reservation;

import java.util.Set;

public class ReservationStatus
{
    public static final String PENDING = "pending";
    public static final String APPROVED = "approved";
    public static final String REJECTED = "rejected";
    public static final String CANCELED = "canceled";

    private static final Set<String> ALL = Set.of(PENDING, APPROVED, REJECTED, CANCELED);

    public static Set<String> values()
    {
        return ALL;
    }

    public static boolean isValid(String status)
    {
        if (status == null) return false;
        return ALL.contains(status);
    }

}
