package com.upgrad.tms.meeting;

import com.upgrad.tms.entities.Meeting;

public class MeetingLocationUrlWorker implements Runnable {

    private final Meeting meeting;
    private final LocationLocator locationLocator;
    private final UrlLocator urlLocator;

    public MeetingLocationUrlWorker(Meeting meeting, LocationLocator locationLocator, UrlLocator urlLocator) {
        this.meeting = meeting;
        this.locationLocator = locationLocator;
        this.urlLocator = urlLocator;
    }
    @Override
    public void run() {
        {
            System.out.println("Processing meeting id "+meeting.getId());
            synchronized (locationLocator) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Detailed location: "+locationLocator.getLocationDetails(meeting.getLocation()));
                System.out.println("Getting url locator");
                synchronized (urlLocator) {
                    System.out.println("Got url locator");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Detailed url: "+urlLocator.getUrlDetails(meeting.getUrl()));
                }
            }
        }
    }
}
