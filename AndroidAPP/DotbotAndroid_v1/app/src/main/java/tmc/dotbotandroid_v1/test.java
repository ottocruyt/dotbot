package tmc.dotbotandroid_v1;

/**
 * Created by Steven on 23/06/2016.
 */
public class test {
    private static test ourInstance = new test();

    public static test getInstance() {
        return ourInstance;
    }

    private test() {
    }
}
