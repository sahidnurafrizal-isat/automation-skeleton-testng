package com.inmarsat.dpi.test.customers;

import com.inmarsat.dpi.test.BaseTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

@Test(groups={"Test"})
public class TestRun extends BaseTest {
    private static Logger logger = LogManager.getLogger(TestRun.class.getName());

    @Test
    public void TestRun01(){
        logger.info("Test");
    }
}
