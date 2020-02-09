package com.upgrad.tms.meeting;

import com.upgrad.tms.entities.Meeting;

public class MeetingUrlLocationWorker implements Runnable {

    private final Meeting meeting;
    private final LocationLocator locationLocator;
    private final UrlLocator urlLocator;

    public MeetingUrlLocationWorker(Meeting meeting, LocationLocator locationLocator, UrlLocator urlLocator) {
        this.meeting = meeting;
        this.locationLocator = locationLocator;
        this.urlLocator = urlLocator;
    }

    @Override
    public void run() {
        System.out.println("Processing meeting id " + meeting.getId());
        synchronized (locationLocator) {
            synchronized (urlLocator) {
                System.out.println("Processing meeting id after locks " + meeting.getId());
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Detailed url: " + urlLocator.getUrlDetails(meeting.getUrl()));
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Detailed location: " + locationLocator.getLocationDetails(meeting.getLocation()));
            }
        }
    }
}
