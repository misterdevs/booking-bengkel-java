package com.bengkel.booking.services;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class AuthService {
    public static int suspendTime = 10; // in second

    public static void suspendCustomer(List<Object[]> suspendedCustomers, String customerId) {
        LocalTime suspendUntil = LocalTime.now().plusSeconds(suspendTime);
        suspendedCustomers.add(new Object[] { customerId, suspendUntil });
    }

    public static Object[] getSuspendedCustomerById(List<Object[]> suspendedCustomers, String id) {
        return suspendedCustomers.stream().filter(a -> ((String) a[0]).equalsIgnoreCase(id)).findFirst().orElse(null);
    }

    public static long getLeftTimeSuspend(LocalTime suspendedUserTime) {
        return LocalTime.now().until(suspendedUserTime, ChronoUnit.SECONDS);

    }

    public static void removeExpiredSuspend(List<Object[]> suspendedCustomers) {
        List<Object[]> temp = new ArrayList<Object[]>(suspendedCustomers);
        if (temp.size() > 0)
            for (int i = 0; i < temp.size(); i++) {
                if (LocalTime.now().compareTo(((LocalTime) temp.get(i)[1])) > 0) {
                    suspendedCustomers.remove(temp.get(i));
                }
            }
    }

}
