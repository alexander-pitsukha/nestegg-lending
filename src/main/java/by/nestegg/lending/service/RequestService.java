package by.nestegg.lending.service;

import by.nestegg.lending.dto.PullRequestDto;
import by.nestegg.lending.dto.RequestDto;

public interface RequestService {

    PullRequestDto getPullBorrowingRequests(RequestDto requestDto);

    PullRequestDto getPullLendingRequests(RequestDto requestDto);

    RequestDto saveBorrowingRequest(RequestDto requestDto);

    RequestDto saveLendingRequest(RequestDto requestDto);

    RequestDto updateBorrowingRequest(RequestDto requestDto);

    RequestDto updateLendingRequest(RequestDto requestDto);

    void deleteBorrowingRequest(Long requestId);

    void deleteLendingRequest(Long requestId);

}
