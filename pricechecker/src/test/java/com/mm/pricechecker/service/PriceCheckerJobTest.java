package com.mm.pricechecker.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.mm.rest.pricechecker.Application;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
@ContextConfiguration(locations="/testContext.xml")
public class PriceCheckerJobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
 
    @Test
    @Ignore
    public void testJob() throws Exception {
    	JobExecution jobExecution = jobLauncherTestUtils.launchJob();
    	
        assertNotNull(jobExecution);
 
        BatchStatus batchStatus = jobExecution.getStatus();
        assertEquals(BatchStatus.COMPLETED, batchStatus);
        assertFalse(batchStatus.isUnsuccessful());
 
        ExitStatus exitStatus = jobExecution.getExitStatus();
        assertEquals("COMPLETED", exitStatus.getExitCode());
        assertEquals("", exitStatus.getExitDescription());
 
        List<Throwable> failureExceptions = jobExecution.getFailureExceptions();
        assertNotNull(failureExceptions);
        assertTrue(failureExceptions.isEmpty());
    }
}
