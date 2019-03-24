package ro.utcn.sd.flav.stackoverflow.unittests;

import org.junit.Assume;

public class Skip {
    public Skip() {
    }

    public static void IF(Boolean condition) {
        Assume.assumeTrue(condition);
    }

    public static void UNLESS(Boolean condition) {
        Assume.assumeFalse(condition);
    }
}