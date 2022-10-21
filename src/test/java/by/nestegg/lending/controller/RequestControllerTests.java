package by.nestegg.lending.controller;

import by.nestegg.lending.dto.RequestDto;
import by.nestegg.lending.service.RequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.format.DateTimeFormatter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RequestController.class)
@WithMockUser
class RequestControllerTests extends BasicControllerTests {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RequestService requestService;

    @BeforeEach
    void setUp() throws Exception {
        super.setUp();
    }

    @Test
    void testSaveBorrowingRequest() throws Exception {
        RequestDto requestDto = getObjectFromJson("response/borrowing_request_dto.json", RequestDto.class);

        given(requestService.saveBorrowingRequest(any(RequestDto.class))).willReturn(requestDto);

        mockMvc.perform(post("/requests/borrowing")
                        .with(csrf())
                        .headers(httpHeaders())
                        .content(asJsonString(getObjectFromJson("request/borrowing_request_dto.json",
                                RequestDto.class)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, containsString(requestDto.getId().toString())))
                .andExpect(jsonPath("$.id").value(requestDto.getId()))
                .andExpect(jsonPath("$.sum").value(requestDto.getSum()))
                .andExpect(jsonPath("$.revenueInMouth", is(requestDto.getRevenueInMouth())))
                .andExpect(jsonPath("$.termCount", is(requestDto.getTermCount())))
                .andExpect(jsonPath("$.termType", is(requestDto.getTermType().name())))
                .andExpect(jsonPath("$.termDate", is(requestDto.getTermDate().format(formatter))))
                .andExpect(jsonPath("$.userId").value(requestDto.getUserId()));

        verify(requestService).saveBorrowingRequest(any(RequestDto.class));
    }

    @Test
    void testSaveLendingRequest() throws Exception {
        RequestDto requestDto = getObjectFromJson("response/lending_request_dto.json", RequestDto.class);

        given(requestService.saveLendingRequest(any(RequestDto.class))).willReturn(requestDto);

        mockMvc.perform(post("/requests/lending")
                        .with(csrf())
                        .headers(httpHeaders())
                        .content(asJsonString(getObjectFromJson("request/lending_request_dto.json",
                                RequestDto.class)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, containsString(requestDto.getId().toString())))
                .andExpect(jsonPath("$.id").value(requestDto.getId()))
                .andExpect(jsonPath("$.sum").value(requestDto.getSum()))
                .andExpect(jsonPath("$.revenueInMouth", is(requestDto.getRevenueInMouth())))
                .andExpect(jsonPath("$.termCount", is(requestDto.getTermCount())))
                .andExpect(jsonPath("$.termType", is(requestDto.getTermType().name())))
                .andExpect(jsonPath("$.termDate", is(requestDto.getTermDate().format(formatter))))
                .andExpect(jsonPath("$.userId").value(requestDto.getUserId()));

        verify(requestService).saveLendingRequest(any(RequestDto.class));
    }

    @Test
    void testUpdateBorrowingRequest() throws Exception {
        RequestDto requestDto = getObjectFromJson("request/borrowing_request_dto.json", RequestDto.class);

        given(requestService.updateBorrowingRequest(any(RequestDto.class))).willReturn(requestDto);

        mockMvc.perform(put("/requests/borrowing/{requestId}", requestDto.getId())
                        .with(csrf())
                        .headers(httpHeaders())
                        .content(asJsonString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(requestDto.getId()))
                .andExpect(jsonPath("$.sum").value(requestDto.getSum()))
                .andExpect(jsonPath("$.revenueInMouth", is(requestDto.getRevenueInMouth())))
                .andExpect(jsonPath("$.termCount", is(requestDto.getTermCount())))
                .andExpect(jsonPath("$.termType", is(requestDto.getTermType().name())))
                .andExpect(jsonPath("$.termDate", is(requestDto.getTermDate().format(formatter))))
                .andExpect(jsonPath("$.userId").value(requestDto.getUserId()));

        verify(requestService).updateBorrowingRequest(any(RequestDto.class));
    }

    @Test
    void testUpdateBorrowingRequest_BadRequest() throws Exception {
        RequestDto requestDto = getObjectFromJson("request/borrowing_request_dto.json", RequestDto.class);

        given(requestService.updateBorrowingRequest(any(RequestDto.class))).willReturn(requestDto);

        mockMvc.perform(put("/requests/borrowing/{requestId}", Long.MAX_VALUE)
                        .with(csrf())
                        .headers(httpHeaders())
                        .content(asJsonString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateLendingRequest() throws Exception {
        RequestDto requestDto = getObjectFromJson("request/lending_request_dto.json", RequestDto.class);

        given(requestService.updateLendingRequest(any(RequestDto.class))).willReturn(requestDto);

        mockMvc.perform(put("/requests/lending/{requestId}", requestDto.getId())
                        .with(csrf())
                        .headers(httpHeaders())
                        .content(asJsonString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(requestDto.getId()))
                .andExpect(jsonPath("$.sum").value(requestDto.getSum()))
                .andExpect(jsonPath("$.revenueInMouth", is(requestDto.getRevenueInMouth())))
                .andExpect(jsonPath("$.termCount", is(requestDto.getTermCount())))
                .andExpect(jsonPath("$.termType", is(requestDto.getTermType().name())))
                .andExpect(jsonPath("$.termDate", is(requestDto.getTermDate().format(formatter))))
                .andExpect(jsonPath("$.userId").value(requestDto.getUserId()));

        verify(requestService).updateLendingRequest(any(RequestDto.class));
    }

    @Test
    void testUpdateLendingRequest_BadRequest() throws Exception {
        RequestDto requestDto = getObjectFromJson("request/lending_request_dto.json", RequestDto.class);

        given(requestService.updateLendingRequest(any(RequestDto.class))).willReturn(requestDto);

        mockMvc.perform(put("/requests/lending/{requestId}", Long.MAX_VALUE)
                        .with(csrf())
                        .headers(httpHeaders())
                        .content(asJsonString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteBorrowingRequest() throws Exception {
        willDoNothing().given(requestService).deleteBorrowingRequest(isA(Long.class));

        mockMvc.perform(delete("/requests/borrowing/1")
                        .with(csrf())
                        .headers(httpHeaders())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(requestService).deleteBorrowingRequest(anyLong());
    }

    @Test
    void testDeleteLendingRequest() throws Exception {
        willDoNothing().given(requestService).deleteLendingRequest(isA(Long.class));

        mockMvc.perform(delete("/requests/lending/1")
                        .with(csrf())
                        .headers(httpHeaders())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(requestService).deleteLendingRequest(anyLong());
    }

}
