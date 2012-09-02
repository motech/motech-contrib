package org.motechproject.performance.tests;

import com.clarkware.junitperf.LoadTest;
import com.clarkware.junitperf.RandomTimer;
import com.clarkware.junitperf.TestMethodFactory;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.joda.time.DateTime;
import org.junit.runners.model.FrameworkMethod;

public class TestSuiteUtils {

    public static TestSuite createTestSuite(int maxUsers, FrameworkMethod method) {
        TestSuite testSuite = new TestSuite();
        CustomTest customTest = getCustomTest(method.getMethod().getDeclaringClass(), method.getMethod().getName());
        Test loadTest = new LoadTest(customTest, maxUsers);
        testSuite.addTest(loadTest);
        return testSuite;
    }

    public static TestSuite createStaggeredLoadTestSuite(LoadPerfStaggered loadPerfStaggered, FrameworkMethod method) {
        TestSuite testSuite = new TestSuite();
        CustomTest customTest = getCustomTest(method.getMethod().getDeclaringClass(), method.getMethod().getName());
        int variation = loadPerfStaggered.delayVariation();
        RandomTimer randomTimer = new RandomTimer(loadPerfStaggered.minDelayInMillis(), variation);

        String[] batchSizes = loadPerfStaggered.minMaxRandomBatchSizes();
        int minBatchSize = Integer.parseInt(batchSizes[0]);
        int maxBatchSize = Integer.parseInt(batchSizes[1]);
        Test loadTest = new StaggeredLoadTest(customTest, loadPerfStaggered.totalNumberOfUsers(),randomTimer,minBatchSize,maxBatchSize);
        testSuite.addTest(loadTest);
        return testSuite;
    }

    private static CustomTest getCustomTest(Class testClass, String methodName) {
        Test testCase = new TestMethodFactory(testClass, methodName);
        IndividualTestTimeResultWriter resultWriter = new IndividualTestTimeResultWriter(methodName+"-PerformanceTestLog" + DateTime.now() + ".csv");
        return new CustomTest(testCase, resultWriter);
    }


}

