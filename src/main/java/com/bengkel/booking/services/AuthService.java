package com.bengkel.booking.services;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class AuthService {
    public static Object[] getSuspendedUserById(List<Object[]> suspendedUsers, String id) {
        return suspendedUsers.stream().filter(a -> ((String) a[0]).equalsIgnoreCase(id)).findFirst().orElse(null);
    }

    public static long getLeftTimeSuspend(LocalTime suspendedUserTime) {
        return LocalTime.now().until(suspendedUserTime, ChronoUnit.SECONDS);

    }

    public static void removeExpiredSuspend(List<Object[]> suspendedUsers) {
        List<Object[]> temp = new ArrayList<Object[]>(suspendedUsers);
        if (temp.size() > 0)
            for (int i = 0; i < temp.size(); i++) {
                if (LocalTime.now().compareTo(((LocalTime) temp.get(i)[1])) > 0) {
                    suspendedUsers.remove(temp.get(i));
                }
            }
    }

}
