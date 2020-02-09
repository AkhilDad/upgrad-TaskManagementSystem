package com.upgrad.tms.meeting;

public class LocationLocator {

    private static LocationLocator locationLocator;

    private LocationLocator() {
    }

    public synchronized static LocationLocator getInstance() {
        if (locationLocator == null) {
            locationLocator = new LocationLocator();
        }

        return locationLocator;
    }

    public synchronized String getLocationDetails(String location) {
        if (location == null) {
            return "There are not details as there is no location";
        }
        return "The details of the location are :"+location;
    }
}
