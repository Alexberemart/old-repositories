package com.alexberemart.basketscraping.services;

import com.alexberemart.basketscraping.dto.BasketJobDto;
import com.alexberemart.basketscraping.entities.BasketJobEntity;
import com.alexberemart.basketscraping.model.BasketJob;
import com.alexberemart.basketscraping.model.BasketJobState;
import com.alexberemart.basketscraping.model.OriginType;
import com.alexberemart.basketscraping.repositories.BasketJobRepository;
import com.alexberemart.basketscraping.services.BasketJobServices;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BasketJobServicesTest {

    @Mock
    protected BasketJobRepository basketJobRepository;
    protected BasketJobServices basketJobServices;

    @Before
    public void setUo() {
        basketJobServices = new BasketJobServices(basketJobRepository);
    }

    @Test
    public void findByDownloaded() throws IOException, ParseException {

        when(basketJobRepository.findByState(BasketJobState.DOWNLOADED.getCode()))
                .thenReturn(Arrays.asList(new BasketJobEntity()));

        List<BasketJob> downloaded = basketJobServices.findDownloaded();
        Assert.assertEquals(1, downloaded.size());
    }

    @Test
    public void restartJobs() {

        List<BasketJobEntity> basketJobList = new ArrayList<>();

        BasketJob basketJob = BasketJob.builder()
                .restartJob(Boolean.TRUE)
                .state(BasketJobState.EVENTS_FINISHED)
                .build();

        basketJobList.add(basketJob.toEntity());

        when(basketJobRepository.findByRestartJob(Boolean.TRUE))
                .thenReturn(basketJobList);

        List<BasketJob> basketJobs = basketJobServices.restartJobs();
        Assert.assertEquals(BasketJobState.INIT, basketJobs.get(0).getState());
    }

    @Test
    public void createJob() {

        String webKey = "webkey";
        BasketJobDto basketJobDto = BasketJobDto.builder()
                .webKey(webKey)
                .originType(OriginType.BASKET_REFERENCE.getCode())
                .build();

        doAnswer(getBasketJobSaveMock()).when(basketJobRepository).save(any(BasketJobEntity.class));

        BasketJob basketJob = basketJobServices.createJob(basketJobDto);
        Assert.assertEquals(webKey, basketJob.getWebKey());
    }

    @Test
    public void createJob_1() {

        String webKey = "webkey";
        Integer priority = 100;
        Boolean restartJob = Boolean.TRUE;

        doAnswer(getBasketJobSaveMock()).when(basketJobRepository).save(any(BasketJobEntity.class));

        BasketJob basketJob = basketJobServices.createJob(webKey, priority, restartJob);
        Assert.assertEquals(webKey, basketJob.getWebKey());
        Assert.assertEquals(priority, basketJob.getPriority());
        Assert.assertEquals(restartJob, basketJob.getRestartJob());
    }

    @Test
    public void findNonDownloaded() throws IOException, ParseException {

        when(basketJobRepository.findByState(BasketJobState.INIT.getCode()))
                .thenReturn(Arrays.asList(new BasketJobEntity()));

        List<BasketJob> nonDownloaded = basketJobServices.findNonDownloaded();
        Assert.assertEquals(1, nonDownloaded.size());
    }

    @Test
    public void findNonFinished() throws IOException, ParseException {

        when(basketJobRepository.findByStateNotOrderByPriorityDesc(BasketJobState.EVENTS_FINISHED.getCode()))
                .thenReturn(Arrays.asList(new BasketJobEntity()));

        List<BasketJob> nonFinished = basketJobServices.findNonFinished();
        Assert.assertEquals(1, nonFinished.size());
    }

    @Test
    public void save() throws IOException, ParseException {

        when(basketJobRepository.save(any(BasketJobEntity.class)))
                .thenReturn(new BasketJobEntity());

        BasketJob basketJob = BasketJob.builder()
                .webKey("key")
                .build();
        basketJobServices.save(basketJob);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAErrorBecauseWebKeyIsNotReported() throws IOException, ParseException {
        BasketJob basketJob = BasketJob.builder().build();
        basketJobServices.save(basketJob);
    }

    @Test
    public void findOneByWebKey() {

        when(basketJobRepository.findByWebKey("key"))
                .thenReturn(Arrays.asList(new BasketJobEntity()));

        when(basketJobRepository.findByWebKey("key1"))
                .thenReturn(null);

        BasketJob basketJob = basketJobServices.findOneByWebKey("key");
        Assert.assertNotNull(basketJob);
        basketJob = basketJobServices.findOneByWebKey("key1");
        Assert.assertNull(basketJob);
    }

    @Test
    public void markAsDownloaded() {

        BasketJobEntity basketJobEntity = BasketJobEntity.builder()
                .webKey("key")
                .build();

        when(basketJobRepository.findByWebKey("key"))
                .thenReturn(Arrays.asList(basketJobEntity));

        doAnswer(getBasketJobSaveMock()).when(basketJobRepository).save(any(BasketJobEntity.class));

        BasketJob basketJob = basketJobServices.markAsDownloaded("key");
        Assert.assertNotNull(basketJob);
        Assert.assertEquals(BasketJobState.DOWNLOADED, basketJob.getState());
    }

    @Test(expected = RuntimeException.class)
    public void markAsDownloadedException() {

        when(basketJobRepository.findByWebKey("key1"))
                .thenReturn(null);

        BasketJob basketJob = basketJobServices.markAsDownloaded("key1");
        Assert.assertNull(basketJob);
    }

    @Test(expected = RuntimeException.class)
    public void markAsDownloadedException_1() {

        when(basketJobRepository.findByWebKey("key2"))
                .thenReturn(Arrays.asList());

        BasketJob basketJob = basketJobServices.markAsDownloaded("key2");
        Assert.assertNull(basketJob);
    }

    @Test
    public void existBasketJobByWebKeyLikeNonFinished() {
        String webKey = "key";
        String webKey1 = "key1";
        BasketJobEntity basketJobEntity = BasketJobEntity.builder()
                .webKey(webKey)
                .build();

        when(basketJobRepository.findByWebKeyLikeAndStateNot(webKey, BasketJobState.EVENTS_FINISHED.getCode()))
                .thenReturn(Arrays.asList(basketJobEntity));

        Boolean result = basketJobServices.existBasketJobByWebKeyLikeNonFinished(webKey);
        Assert.assertEquals(Boolean.TRUE, result);
        result = basketJobServices.existBasketJobByWebKeyLikeNonFinished(webKey1);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    protected Answer<BasketJobEntity> getBasketJobSaveMock() {
        return new Answer<BasketJobEntity>() {
            public BasketJobEntity answer(InvocationOnMock invocation) throws Throwable {
                BasketJobEntity basketJobEntity = invocation.getArgument(0);
                basketJobEntity.setId("id");
                return basketJobEntity;
            }
        };
    }

}
