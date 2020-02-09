package com.upgrad.tms.meeting;

import com.upgrad.tms.entities.Meeting;

import java.util.concurrent.Semaphore;

public class MeetingLocationUrlWorker implements Runnable {

    private final Meeting meeting;
    private final LocationLocator locationLocator;
    private final UrlLocator urlLocator;
    private Semaphore semaphore;

    public MeetingLocationUrlWorker(Meeting meeting, LocationLocator locationLocator, UrlLocator urlLocator, Semaphore semaphore) {
        this.meeting = meeting;
        this.locationLocator = locationLocator;
        this.urlLocator = urlLocator;
        this.semaphore = semaphore;
    }
    @Override
    public void run() {
        System.out.println("Processing meeting id " + meeting.getId());
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Processing meeting id after locks " + meeting.getId());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Detailed location: " + locationLocator.getLocationDetails(meeting.getLocation()));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Detailed url: " + urlLocator.getUrlDetails(meeting.getUrl()));
        semaphore.release();
    }
}
