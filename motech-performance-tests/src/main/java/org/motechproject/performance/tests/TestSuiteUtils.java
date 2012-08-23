package org.motechproject.performance.tests;

import com.clarkware.junitperf.TestMethodFactory;
import com.clarkware.junitperf.LoadTest;
import junit.framework.Test;
import junit.framework.TestSuite;

public class TestSuiteUtils {

    public static TestSuite createTestSuite(int maxUsers, Class testClass, String methodName) {
        TestSuite testSuite = new TestSuite();
        Test testCase = new TestMethodFactory(testClass, methodName);
        Test loadTest = new LoadTest(testCase, maxUsers);
        testSuite.addTest(loadTest);
        return testSuite;
    }
}
