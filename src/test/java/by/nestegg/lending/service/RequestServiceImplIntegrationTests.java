package by.nestegg.lending.service;

import by.nestegg.lending.BasicTests;
import by.nestegg.lending.NesteggLendingApplication;
import by.nestegg.lending.dto.RequestDto;
import by.nestegg.lending.mapper.RequestMapper;
import by.nestegg.lending.repository.BorrowingRequestRepository;
import by.nestegg.lending.repository.LendingRequestRepository;
import by.nestegg.lending.repository.UserRepository;
import by.nestegg.lending.service.impl.RequestServiceImpl;
import by.nestegg.lending.util.MessageCodeUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = NesteggLendingApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
class RequestServiceImplIntegrationTests extends BasicTests {

    @Autowired
    private RequestService requestService;
    @Autowired
    private BorrowingRequestRepository borrowingRequestRepository;
    @Autowired
    private LendingRequestRepository lendingRequestRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RequestMapper requestMapper;
    @Autowired
    private MessageCodeUtil messageCodeUtil;

    @TestConfiguration
    static class TestContextConfiguration {

        @Bean
        @Autowired
        public RequestService requestService(BorrowingRequestRepository borrowingRequestRepository,
                                             LendingRequestRepository lendingRequestRepository,
                                             UserRepository userRepository, RequestMapper requestMapper,
                                             MessageCodeUtil messageCodeUtil) {
            return new RequestServiceImpl(borrowingRequestRepository, lendingRequestRepository, userRepository,
                    requestMapper, messageCodeUtil);
        }
    }

    @Test
    void testSaveBorrowingRequest() throws Exception {
        RequestDto requestDto = getObjectFromJson("request/borrowing_request_dto.json", RequestDto.class);

        RequestDto result = requestService.saveBorrowingRequest(requestDto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(requestDto.getSum(), result.getSum());
        assertEquals(requestDto.getRevenueInMouth(), result.getRevenueInMouth());
        assertEquals(requestDto.getTermCount(), result.getTermCount());
        assertEquals(requestDto.getTermType(), result.getTermType());
    }

    @Test
    void testSaveLendingRequest() throws Exception {
        RequestDto requestDto = getObjectFromJson("request/lending_request_dto.json", RequestDto.class);

        RequestDto result = requestService.saveLendingRequest(requestDto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(requestDto.getSum(), result.getSum());
        assertEquals(requestDto.getRevenueInMouth(), result.getRevenueInMouth());
        assertEquals(requestDto.getTermCount(), result.getTermCount());
        assertEquals(requestDto.getTermType(), result.getTermType());
    }

    @Test
    @Sql("classpath:request/sql/insert_requests.sql")
    @SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
    void testUpdateBorrowingRequest() throws Exception {
        RequestDto requestDto = getObjectFromJson("request/update_request_borrowing.json", RequestDto.class);

        RequestDto result = requestService.updateBorrowingRequest(requestDto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(requestDto.getSum(), result.getSum());
        assertEquals(requestDto.getRevenueInMouth(), result.getRevenueInMouth());
        assertEquals(requestDto.getTermCount(), result.getTermCount());
        assertEquals(requestDto.getTermType(), result.getTermType());
    }

    @Test
    @Sql("classpath:request/sql/insert_requests.sql")
    @SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
    void testUpdateLendingRequest() throws Exception {
        RequestDto requestDto = getObjectFromJson("request/update_request_lending.json", RequestDto.class);

        RequestDto result = requestService.updateLendingRequest(requestDto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(requestDto.getSum(), result.getSum());
        assertEquals(requestDto.getRevenueInMouth(), result.getRevenueInMouth());
        assertEquals(requestDto.getTermCount(), result.getTermCount());
        assertEquals(requestDto.getTermType(), result.getTermType());
    }

    @Test
    @Sql("classpath:request/sql/insert_requests.sql")
    @SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
    void testDeleteBorrowingRequest() throws Exception {
        long requestId = 2L;

        requestService.deleteBorrowingRequest(requestId);

        boolean result = borrowingRequestRepository.findById(requestId).isEmpty();
        assertTrue(result);
    }

    @Test
    @Sql("classpath:request/sql/insert_requests.sql")
    @SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
    void testDeleteLendingRequest() throws Exception {
        long requestId = 2L;

        requestService.deleteLendingRequest(requestId);

        boolean result = lendingRequestRepository.findById(requestId).isEmpty();
        assertTrue(result);
    }

}
