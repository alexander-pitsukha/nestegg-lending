package by.nestegg.lending.service.impl;

import by.nestegg.lending.dto.PullRequestDto;
import by.nestegg.lending.dto.RequestDto;
import by.nestegg.lending.dto.UserRequestDto;
import by.nestegg.lending.entity.BorrowingRequest;
import by.nestegg.lending.entity.LendingRequest;
import by.nestegg.lending.mapper.RequestMapper;
import by.nestegg.lending.repository.BorrowingRequestRepository;
import by.nestegg.lending.repository.LendingRequestRepository;
import by.nestegg.lending.repository.UserRepository;
import by.nestegg.lending.service.RequestService;
import by.nestegg.lending.util.MessageCodeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {

    private final BorrowingRequestRepository borrowingRequestRepository;
    private final LendingRequestRepository lendingRequestRepository;
    private final UserRepository userRepository;
    private final RequestMapper requestMapper;
    private final MessageCodeUtil messageCodeUtil;

    // TODO: 30.04.2021  
    @Override
    public PullRequestDto getPullBorrowingRequests(RequestDto requestDto) {
        List<BorrowingRequest> borrowingRequests = borrowingRequestRepository
                .findBySumLessThanEqualAndRevenueInMouthAndTermCountAndTermType(requestDto.getSum(),
                        requestDto.getRevenueInMouth(), requestDto.getTermCount(), requestDto.getTermType());
        var pullRequestDto = new PullRequestDto();
        Map<Integer, List<UserRequestDto>> requestDtoMap = new HashMap<>();
        requestDtoMap.put(1, borrowingRequests.stream().map(requestMapper::toUserRequestDto)
                .collect(Collectors.toList()));
        pullRequestDto.setRequestDtoMap(requestDtoMap);
        return pullRequestDto;
    }

    // TODO: 30.04.2021
    @Override
    public PullRequestDto getPullLendingRequests(RequestDto requestDto) {
        List<LendingRequest> lendingRequests = lendingRequestRepository
                .findAllBySumLessThanEqualAndRevenueInMouthAndTermCountAndTermType(requestDto.getSum(),
                        requestDto.getRevenueInMouth(), requestDto.getTermCount(), requestDto.getTermType());
        var pullRequestDto = new PullRequestDto();
        Map<Integer, List<UserRequestDto>> requestDtoMap = new HashMap<>();
        requestDtoMap.put(1, lendingRequests.stream().map(requestMapper::toUserRequestDto)
                .collect(Collectors.toList()));
        pullRequestDto.setRequestDtoMap(requestDtoMap);
        return pullRequestDto;
    }

    @Override
    @Transactional
    public RequestDto saveBorrowingRequest(RequestDto requestDto) {
        var borrowingRequest = requestMapper.toBorrowingRequestEntity(requestDto);
        borrowingRequest.getRequest().setUser(userRepository.getById(requestDto.getUserId()));
        return requestMapper.toDto(borrowingRequestRepository.saveAndFlush(borrowingRequest));
    }

    @Override
    @Transactional
    public RequestDto saveLendingRequest(RequestDto requestDto) {
        var lendingRequest = requestMapper.toLendingRequestEntity(requestDto);
        lendingRequest.getRequest().setUser(userRepository.getById(requestDto.getUserId()));
        return requestMapper.toDto(lendingRequestRepository.saveAndFlush(lendingRequest));
    }

    @Override
    @Transactional
    public RequestDto updateBorrowingRequest(RequestDto requestDto) {
        var borrowingRequest = borrowingRequestRepository.findById(requestDto.getId()).orElseThrow(() ->
                new NoSuchElementException(messageCodeUtil.getFullErrorMessageByBundleCode(
                        "error.message.request.by.id.not.found", new Object[]{requestDto.getId()})));
        var request = requestMapper.toRequest(requestDto);
        request.setUser(borrowingRequest.getRequest().getUser());
        borrowingRequest.setRequest(request);
        return requestDto;
    }

    @Override
    @Transactional
    public RequestDto updateLendingRequest(RequestDto requestDto) {
        var lendingRequest = lendingRequestRepository.findById(requestDto.getId()).orElseThrow(() ->
                new NoSuchElementException(messageCodeUtil.getFullErrorMessageByBundleCode(
                        "error.message.request.by.id.not.found", new Object[]{requestDto.getId()})));
        var request = requestMapper.toRequest(requestDto);
        request.setUser(lendingRequest.getRequest().getUser());
        lendingRequest.setRequest(request);
        return requestDto;
    }

    @Override
    @Transactional
    public void deleteBorrowingRequest(Long requestId) {
        borrowingRequestRepository.deleteById(requestId);
    }

    @Override
    @Transactional
    public void deleteLendingRequest(Long requestId) {
        lendingRequestRepository.deleteById(requestId);
    }

}
