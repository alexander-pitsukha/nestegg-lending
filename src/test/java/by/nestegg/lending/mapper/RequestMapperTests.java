package by.nestegg.lending.mapper;

import by.nestegg.lending.AbstractTests;
import by.nestegg.lending.dto.RequestDto;
import by.nestegg.lending.dto.UserRequestDto;
import by.nestegg.lending.entity.BorrowingRequest;
import by.nestegg.lending.entity.LendingRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
class RequestMapperTests extends AbstractTests {

    @Autowired
    private RequestMapper requestMapper;

    @TestConfiguration
    static class TestContextConfiguration {

        @Bean
        public RequestMapper requestMapper() {
            return new RequestMapperImpl();
        }
    }

    @Test
    void testToBorrowingRequestEntity() throws Exception {
        RequestDto requestDto = getObjectFromJson("request/borrowing_request_dto.json", RequestDto.class);

        BorrowingRequest borrowingRequest = requestMapper.toBorrowingRequestEntity(requestDto);

        assertNotNull(borrowingRequest);
        assertEquals(requestDto.getSum(), borrowingRequest.getRequest().getSum());
        assertEquals(requestDto.getRevenueInMouth(), borrowingRequest.getRequest().getRevenueInMouth());
        assertEquals(requestDto.getTermCount(), borrowingRequest.getRequest().getTermCount());
        assertEquals(requestDto.getTermType(), borrowingRequest.getRequest().getTermType());
        assertEquals(requestDto.getTermDate(), borrowingRequest.getRequest().getTermDate());
        assertEquals(requestDto.getTermDate(), borrowingRequest.getRequest().getTermDate());
    }

    @Test
    void testToLendingRequestEntity() throws Exception {
        RequestDto requestDto = getObjectFromJson("request/lending_request_dto.json", RequestDto.class);

        LendingRequest lendingRequest = requestMapper.toLendingRequestEntity(requestDto);

        assertNotNull(lendingRequest);
        assertEquals(requestDto.getSum(), lendingRequest.getRequest().getSum());
        assertEquals(requestDto.getRevenueInMouth(), lendingRequest.getRequest().getRevenueInMouth());
        assertEquals(requestDto.getTermCount(), lendingRequest.getRequest().getTermCount());
        assertEquals(requestDto.getTermType(), lendingRequest.getRequest().getTermType());
        assertEquals(requestDto.getTermDate(), lendingRequest.getRequest().getTermDate());
        assertEquals(requestDto.getTermDate(), lendingRequest.getRequest().getTermDate());
    }

    @Test
    void testToBorrowingRequestDto() throws Exception {
        BorrowingRequest borrowingRequest = getObjectFromJson("request/borrowing_request.json",
                BorrowingRequest.class);

        RequestDto requestDto = requestMapper.toDto(borrowingRequest);

        assertNotNull(requestDto);
        assertEquals(borrowingRequest.getRequest().getSum(), requestDto.getSum());
        assertEquals(borrowingRequest.getRequest().getRevenueInMouth(), requestDto.getRevenueInMouth());
        assertEquals(borrowingRequest.getRequest().getTermCount(), requestDto.getTermCount());
        assertEquals(borrowingRequest.getRequest().getTermType(), requestDto.getTermType());
        assertEquals(borrowingRequest.getRequest().getTermDate(), requestDto.getTermDate());
        assertEquals(borrowingRequest.getRequest().getTermDate(), requestDto.getTermDate());
        assertEquals(borrowingRequest.getRequest().getUser().getId(), requestDto.getUserId());
    }

    @Test
    void testToLendingRequestDto() throws Exception {
        LendingRequest lendingRequest = getObjectFromJson("request/lending_request.json",
                LendingRequest.class);

        RequestDto requestDto = requestMapper.toDto(lendingRequest);

        assertNotNull(requestDto);
        assertEquals(lendingRequest.getRequest().getSum(), requestDto.getSum());
        assertEquals(lendingRequest.getRequest().getRevenueInMouth(), requestDto.getRevenueInMouth());
        assertEquals(lendingRequest.getRequest().getTermCount(), requestDto.getTermCount());
        assertEquals(lendingRequest.getRequest().getTermType(), requestDto.getTermType());
        assertEquals(lendingRequest.getRequest().getTermDate(), requestDto.getTermDate());
        assertEquals(lendingRequest.getRequest().getTermDate(), requestDto.getTermDate());
        assertEquals(lendingRequest.getRequest().getUser().getId(), requestDto.getUserId());
    }

    @Test
    void testToBorrowingUserRequestDto() throws Exception {
        BorrowingRequest borrowingRequest = getObjectFromJson("request/borrowing_request.json",
                BorrowingRequest.class);

        UserRequestDto userRequestDto = requestMapper.toUserRequestDto(borrowingRequest);

        assertNotNull(userRequestDto);
        assertEquals(borrowingRequest.getRequest().getSum(), userRequestDto.getSum());
        assertEquals(borrowingRequest.getRequest().getRevenueInMouth(), userRequestDto.getRevenueInMouth());
        assertEquals(borrowingRequest.getRequest().getTermCount(), userRequestDto.getTermCount());
        assertEquals(borrowingRequest.getRequest().getTermType(), userRequestDto.getTermType());
        assertEquals(borrowingRequest.getRequest().getTermDate(), userRequestDto.getTermDate());
        assertEquals(borrowingRequest.getRequest().getTermDate(), userRequestDto.getTermDate());
        assertEquals(borrowingRequest.getRequest().getUser().getId(), userRequestDto.getUserId());
        assertEquals(borrowingRequest.getRequest().getUser().getNickname(), userRequestDto.getNickname());
    }

    @Test
    void testToLendingUserRequestDto() throws Exception {
        LendingRequest lendingRequest = getObjectFromJson("request/lending_request.json",
                LendingRequest.class);

        UserRequestDto userRequestDto = requestMapper.toUserRequestDto(lendingRequest);

        assertNotNull(userRequestDto);
        assertEquals(lendingRequest.getRequest().getSum(), userRequestDto.getSum());
        assertEquals(lendingRequest.getRequest().getRevenueInMouth(), userRequestDto.getRevenueInMouth());
        assertEquals(lendingRequest.getRequest().getTermCount(), userRequestDto.getTermCount());
        assertEquals(lendingRequest.getRequest().getTermType(), userRequestDto.getTermType());
        assertEquals(lendingRequest.getRequest().getTermDate(), userRequestDto.getTermDate());
        assertEquals(lendingRequest.getRequest().getTermDate(), userRequestDto.getTermDate());
        assertEquals(lendingRequest.getRequest().getUser().getId(), userRequestDto.getUserId());
        assertEquals(lendingRequest.getRequest().getUser().getNickname(), userRequestDto.getNickname());
    }

}
