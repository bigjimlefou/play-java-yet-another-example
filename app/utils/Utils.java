package utils;

/**
 * The type Utils.
 */
public class Utils {

    /**
     * Random int.
     *
     * @param max the max
     * @return the int
     */
    public static int random(final int max) {
        return (int) (Math.random() * (double) max);
    }

    /**
     * Wait.
     *
     * @param millis the millis
     */
    public static void wait(final int millis) {
        try {
            Thread.sleep(millis);
        } catch (final Exception e) {}
    }

    /**
     * Block int.
     *
     * @return the int
     */
    public static int block() {
        final int wait = 10 + random(20);
        wait(wait);
        return wait;
    }
}
