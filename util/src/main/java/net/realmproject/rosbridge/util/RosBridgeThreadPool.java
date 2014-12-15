package net.realmproject.rosbridge.util;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Thread pool for asynchronous bits
 * 
 * @author NAS
 *
 */
public class RosBridgeThreadPool {

    private static ScheduledExecutorService pool = Executors.newScheduledThreadPool(5);

    public static ScheduledExecutorService getPool() {
        return pool;
    }

    public static void stop() {

        pool.shutdown();

        try {
            System.out.println(pool.awaitTermination(1, TimeUnit.SECONDS));
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        pool.shutdownNow();

        try {
            System.out.println(pool.awaitTermination(10, TimeUnit.SECONDS));
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
