package by.nestegg.lending.mapper;

import by.nestegg.lending.dto.RequestDto;
import by.nestegg.lending.dto.UserRequestDto;
import by.nestegg.lending.entity.BorrowingRequest;
import by.nestegg.lending.entity.LendingRequest;
import by.nestegg.lending.entity.Request;
import by.nestegg.lending.util.Constants;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = Constants.COMPONENT_MODEL)
public interface RequestMapper {

    RequestMapper INSTANCE = Mappers.getMapper(RequestMapper.class);

    Request toRequest(RequestDto requestDto);

    @Mapping(target = "request", source = "requestDto")
    BorrowingRequest toBorrowingRequestEntity(RequestDto requestDto);

    @Mapping(target = "request", source = "requestDto")
    LendingRequest toLendingRequestEntity(RequestDto requestDto);

    @Mapping(target = "sum", source = "request.sum")
    @Mapping(target = "revenueInMouth", source = "request.revenueInMouth")
    @Mapping(target = "termCount", source = "request.termCount")
    @Mapping(target = "termType", source = "request.termType")
    @Mapping(target = "termDate", source = "request.termDate")
    @Mapping(target = "userId", source = "request.user.id")
    RequestDto toDto(BorrowingRequest borrowingRequest);

    @Mapping(target = "sum", source = "request.sum")
    @Mapping(target = "revenueInMouth", source = "request.revenueInMouth")
    @Mapping(target = "termCount", source = "request.termCount")
    @Mapping(target = "termType", source = "request.termType")
    @Mapping(target = "termDate", source = "request.termDate")
    @Mapping(target = "userId", source = "request.user.id")
    RequestDto toDto(LendingRequest lendingRequest);

    @Mapping(target = "sum", source = "request.sum")
    @Mapping(target = "revenueInMouth", source = "request.revenueInMouth")
    @Mapping(target = "termCount", source = "request.termCount")
    @Mapping(target = "termType", source = "request.termType")
    @Mapping(target = "termDate", source = "request.termDate")
    @Mapping(target = "userId", source = "request.user.id")
    @Mapping(target = "nickname", source = "request.user.nickname")
    UserRequestDto toUserRequestDto(BorrowingRequest borrowingRequest);

    @Mapping(target = "sum", source = "request.sum")
    @Mapping(target = "revenueInMouth", source = "request.revenueInMouth")
    @Mapping(target = "termCount", source = "request.termCount")
    @Mapping(target = "termType", source = "request.termType")
    @Mapping(target = "termDate", source = "request.termDate")
    @Mapping(target = "userId", source = "request.user.id")
    @Mapping(target = "nickname", source = "request.user.nickname")
    UserRequestDto toUserRequestDto(LendingRequest lendingRequest);

}
