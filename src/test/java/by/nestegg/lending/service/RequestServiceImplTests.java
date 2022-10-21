package by.nestegg.lending.service;

import by.nestegg.lending.AbstractTests;
import by.nestegg.lending.configuration.MessageSourceConfiguration;
import by.nestegg.lending.dto.PullRequestDto;
import by.nestegg.lending.dto.RequestDto;
import by.nestegg.lending.dto.UserRequestDto;
import by.nestegg.lending.entity.BorrowingRequest;
import by.nestegg.lending.entity.LendingRequest;
import by.nestegg.lending.entity.enums.TermType;
import by.nestegg.lending.mapper.RequestMapper;
import by.nestegg.lending.repository.BorrowingRequestRepository;
import by.nestegg.lending.repository.LendingRequestRepository;
import by.nestegg.lending.repository.UserRepository;
import by.nestegg.lending.service.impl.RequestServiceImpl;
import by.nestegg.lending.util.MessageCodeUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@MockBean({BorrowingRequestRepository.class, LendingRequestRepository.class, UserRepository.class, RequestMapper.class})
@Import(MessageSourceConfiguration.class)
class RequestServiceImplTests extends AbstractTests {

    @Autowired
    private RequestService requestService;
    @Autowired
    private BorrowingRequestRepository borrowingRequestRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LendingRequestRepository lendingRequestRepository;
    @Autowired
    private RequestMapper requestMapper;
    @Autowired
    private MessageCodeUtil messageCodeUtil;
    @Autowired
    private ObjectMapper objectMapper;

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
    void testGetPullBorrowingRequests() throws Exception {
        RequestDto requestDto = getObjectFromJson("request/request_borrowing.json", RequestDto.class);
        List<BorrowingRequest> borrowingRequests = objectMapper.readValue(new ClassPathResource(
                "request/borrowing_requests.json").getInputStream(), new TypeReference<>() {
        });
        List<UserRequestDto> borrowingRequestDtos = objectMapper.readValue(new ClassPathResource(
                "request/borrowing_request_dtos.json").getInputStream(), new TypeReference<>() {
        });
        when(borrowingRequestRepository
                .findBySumLessThanEqualAndRevenueInMouthAndTermCountAndTermType(any(BigDecimal.class),
                        anyDouble(), anyInt(), any(TermType.class))).thenReturn(borrowingRequests);
        when(requestMapper.toUserRequestDto(any(BorrowingRequest.class))).thenReturn(borrowingRequestDtos.get(0),
                borrowingRequestDtos.get(1));

        PullRequestDto pullRequestDto = requestService.getPullBorrowingRequests(requestDto);

        assertNotNull(pullRequestDto);
        assertNotNull(pullRequestDto.getRequestDtoMap());
        assertNotNull(pullRequestDto.getRequestDtoMap().get(1));
        assertEquals(borrowingRequestDtos.size(), pullRequestDto.getRequestDtoMap().get(1).size());

        verify(borrowingRequestRepository)
                .findBySumLessThanEqualAndRevenueInMouthAndTermCountAndTermType(any(BigDecimal.class),
                        anyDouble(), anyInt(), any(TermType.class));
        verify(requestMapper, times(2)).toUserRequestDto(any(BorrowingRequest.class));
    }

    @Test
    void testGetPullLendingRequests() throws Exception {
        RequestDto requestDto = getObjectFromJson("request/request_lending.json", RequestDto.class);
        List<LendingRequest> lendingRequests = objectMapper.readValue(new ClassPathResource(
                "request/lending_requests.json").getInputStream(), new TypeReference<>() {
        });
        List<UserRequestDto> lendingRequestDtos = objectMapper.readValue(new ClassPathResource(
                "request/lending_request_dtos.json").getInputStream(), new TypeReference<>() {
        });
        when(lendingRequestRepository
                .findAllBySumLessThanEqualAndRevenueInMouthAndTermCountAndTermType(any(BigDecimal.class),
                        anyDouble(), anyInt(), any(TermType.class))).thenReturn(lendingRequests);
        when(requestMapper.toUserRequestDto(any(LendingRequest.class))).thenReturn(lendingRequestDtos.get(0),
                lendingRequestDtos.get(1));

        PullRequestDto pullRequestDto = requestService.getPullLendingRequests(requestDto);

        assertNotNull(pullRequestDto);
        assertNotNull(pullRequestDto.getRequestDtoMap());
        assertNotNull(pullRequestDto.getRequestDtoMap().get(1));
        assertEquals(lendingRequestDtos.size(), pullRequestDto.getRequestDtoMap().get(1).size());

        verify(lendingRequestRepository)
                .findAllBySumLessThanEqualAndRevenueInMouthAndTermCountAndTermType(any(BigDecimal.class),
                        anyDouble(), anyInt(), any(TermType.class));
        verify(requestMapper, times(2)).toUserRequestDto(any(LendingRequest.class));
    }

    @Test
    void testUpdateBorrowingRequest_NoSuchElementException() throws Exception {
        RequestDto requestDto = getObjectFromJson("request/request_borrowing.json", RequestDto.class);

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            requestService.updateBorrowingRequest(requestDto);
        });
        assertEquals(messageCodeUtil.getFullErrorMessageByBundleCode(
                        "error.message.request.by.id.not.found", new Object[]{requestDto.getId()}),
                exception.getMessage());

        verify(borrowingRequestRepository, times(0)).findById(anyLong());
    }

    @Test
    void testUpdateLendingRequest_NoSuchElementException() throws Exception {
        RequestDto requestDto = getObjectFromJson("request/request_lending.json", RequestDto.class);

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            requestService.updateLendingRequest(requestDto);
        });
        assertEquals(messageCodeUtil.getFullErrorMessageByBundleCode(
                        "error.message.request.by.id.not.found", new Object[]{requestDto.getId()}),
                exception.getMessage());

        verify(lendingRequestRepository, times(0)).findById(anyLong());
    }

}
