package com.orasi.utils;

import org.apache.commons.lang3.time.StopWatch;

public class Sleeper {
    public static void sleep(long millis) {
        TestReporter.logTrace("Sleeping for [ " + millis + " ] milliseconds");
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        do {

        } while (stopwatch.getTime() < millis);
        stopwatch.stop();
        stopwatch.reset();
    }
}
