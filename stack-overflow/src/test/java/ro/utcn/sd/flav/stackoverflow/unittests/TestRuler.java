package ro.utcn.sd.flav.stackoverflow.unittests;

import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class TestRuler {
    @Rule
    public TestRule listen = new TestWatcher() {
        public void failed(Throwable t, Description description) {
            System.out.println("Test " + description.getMethodName() + " has failed");
        }

        public void succeeded(Description description) {
            System.out.println("Test " + description.getMethodName() + " has passed");
        }
    };

}
