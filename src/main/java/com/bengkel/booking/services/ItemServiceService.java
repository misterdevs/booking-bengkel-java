package com.bengkel.booking.services;

import java.util.List;
import java.util.stream.Collectors;

import com.bengkel.booking.models.ItemService;

public class ItemServiceService {

    public static List<ItemService> getServiceByVehicleType(List<ItemService> itemServices, String vehicleType) {
        return itemServices.stream().filter(srv -> srv.getVehicleType().equalsIgnoreCase(vehicleType))
                .collect(Collectors.toList());

    }

    public static ItemService findServiceById(List<ItemService> itemServices, String id) {
        return itemServices.stream().filter(srv -> srv.getServiceId().equalsIgnoreCase(id)).findFirst().orElse(null);

    }

}
