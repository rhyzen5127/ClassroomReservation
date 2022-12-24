package org.catcom.classreserver.model.classroom;

import java.util.Set;

public class ClassroomStatus
{
    public static final String READY = "ready";
    public static final String REPAIRING = "repairing";

    private static final Set<String> ALL = Set.of(READY, REPAIRING);

    public static Set<String> values()
    {
        return ALL;
    }

    public static boolean isValid(String status)
    {
        return ALL.contains(status);
    }
}
