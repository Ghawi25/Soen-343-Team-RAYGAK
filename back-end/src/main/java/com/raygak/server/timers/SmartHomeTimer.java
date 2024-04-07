package com.raygak.server.timers;

public class SmartHomeTimer extends Thread {
        private long startTime;
        private int numSeconds = 0;
        private boolean running;

        public int getNumSeconds() {
            return this.numSeconds;
        }

        @Override
        public void run() {
            startTime = System.currentTimeMillis();
            running = true;
            while (running) {
                if (System.currentTimeMillis() == startTime + 1000) {
                    startTime = System.currentTimeMillis();
                    numSeconds++;
                }
            }
        }

        public void stopTimer() {
            running = false;
//            System.out.println("Number of seconds: " + numSeconds);
        }
    }
