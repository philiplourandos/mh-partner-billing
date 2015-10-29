package com.mhgad.za.vitel.billing.batch.extract;

import com.mhgad.za.vitel.billing.batch.extract.model.Cdr;
import com.mhgad.za.vitel.billing.batch.common.repo.TestRepo;
import com.mhgad.za.vitel.billing.batch.extract.tasklet.DatasourceSupplierTasklet;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author plourand
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class, PartnerBillingConfig.class})
public class RetrieveCdrTest {
    private static final Logger LOG = LogManager.getLogger(RetrieveCdrTest.class);

    private static final int EXPECTED_INITIAL_DATASOURCE_COUNT = 3;
    private static final Integer EXPECTED_ROW_COUNT = 60;
    private static final String CDR_DST_CHANNEL_WITH_NO_UUID = "IAX2/jhblvgw05_is_voip_out-29085";

    private static final String START_DATE = "2000-01-01";
    private static final String END_DATE = "2016-01-10";

    @Autowired
    private DatasourceSupplierTasklet dsTasklet;

    @Autowired
    private Job retrieveCdrsJob;

    @Autowired
    private JobLauncher launcher;
    
    @Autowired
    private TestRepo testRepo;

    @Test
    public void success() throws JobExecutionException {
        assertEquals(EXPECTED_INITIAL_DATASOURCE_COUNT, dsTasklet.getDatasources().size());

        JobParametersBuilder paramBuilder = new JobParametersBuilder();
        paramBuilder.addString(PartnerBillingConst.PARAM_START_DATE, START_DATE);
        paramBuilder.addString(PartnerBillingConst.PARAM_END_DATE, END_DATE);

        JobExecution jobRun = launcher.run(retrieveCdrsJob, paramBuilder.toJobParameters());
        assertEquals(BatchStatus.COMPLETED, jobRun.getStatus());

        assertTrue(dsTasklet.getDatasources().isEmpty());
        
        final Integer count = testRepo.countCdrs();
        assertEquals(EXPECTED_ROW_COUNT, count);
        
        List<Cdr> cdrWithNoUuidInitially = testRepo.findByDestChannel(CDR_DST_CHANNEL_WITH_NO_UUID);
        assertNotNull(cdrWithNoUuidInitially);
        assertFalse(cdrWithNoUuidInitially.isEmpty());
        assertNotNull(cdrWithNoUuidInitially.get(0).getUniqueid());
    }
}