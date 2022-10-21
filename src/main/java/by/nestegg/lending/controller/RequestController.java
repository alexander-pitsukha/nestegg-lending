package by.nestegg.lending.controller;

import by.nestegg.lending.dto.PullRequestDto;
import by.nestegg.lending.dto.RequestDto;
import by.nestegg.lending.service.RequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Tag(name = "Request Controller")
@RestController
@RequestMapping("requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @GetMapping("borrowing")
    public ResponseEntity<PullRequestDto> getPullBorrowingRequests(@RequestBody @Validated RequestDto requestDto) {
        return ResponseEntity.ok(requestService.getPullBorrowingRequests(requestDto));
    }

    @GetMapping("lending")
    public ResponseEntity<PullRequestDto> getPullLendingRequests(@RequestBody @Validated RequestDto requestDto) {
        return ResponseEntity.ok(requestService.getPullLendingRequests(requestDto));
    }

    @Operation(summary = "Save borrowing request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Return RequestDto",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RequestDto.class))}),
            @ApiResponse(responseCode = "400", description = "Sum cannot be empty<br>" +
                    "Revenue in mouth cannot be empty<br>Term cannot be empty<br>" +
                    "Term cannot be empty<br>Request type cannot be empty", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    @PostMapping("borrowing")
    public ResponseEntity<RequestDto> saveBorrowingRequest(@RequestBody @Validated RequestDto requestDto) {
        var entryDto = requestService.saveBorrowingRequest(requestDto);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(entryDto.getId()).toUri()).body(entryDto);
    }

    @Operation(summary = "Save lending request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Return RequestDto",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RequestDto.class))}),
            @ApiResponse(responseCode = "400", description = "Sum cannot be empty<br>" +
                    "Revenue in mouth cannot be empty<br>Term cannot be empty<br>" +
                    "Term cannot be empty<br>Request type cannot be empty", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    @PostMapping("lending")
    public ResponseEntity<RequestDto> saveLendingRequest(@RequestBody @Validated RequestDto requestDto) {
        var entryDto = requestService.saveLendingRequest(requestDto);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(entryDto.getId()).toUri()).body(entryDto);
    }

    @Operation(summary = "Update borrowing request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return RequestDto",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RequestDto.class))}),
            @ApiResponse(responseCode = "400", description = "Sum cannot be empty<br>" +
                    "Revenue in mouth cannot be empty<br>Term cannot be empty<br>" +
                    "Term cannot be empty<br>Request type cannot be empty", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Request by id not found", content = @Content)})
    @PutMapping("borrowing/{requestId}")
    public ResponseEntity<RequestDto> updateBorrowingRequest(@PathVariable Long requestId, @RequestBody @Validated RequestDto requestDto) {
        if (!requestId.equals(requestDto.getId())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(requestService.updateBorrowingRequest(requestDto));
    }

    @Operation(summary = "Update lending request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return RequestDto",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RequestDto.class))}),
            @ApiResponse(responseCode = "400", description = "Sum cannot be empty<br>" +
                    "Revenue in mouth cannot be empty<br>Term cannot be empty<br>" +
                    "Term cannot be empty<br>Request type cannot be empty", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Request by id not found", content = @Content)})
    @PutMapping("lending/{requestId}")
    public ResponseEntity<RequestDto> updateLendingRequest(@PathVariable Long requestId, @RequestBody @Validated RequestDto requestDto) {
        if (!requestId.equals(requestDto.getId())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(requestService.updateLendingRequest(requestDto));
    }

    @Operation(summary = "Delete borrowing request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Return HttpStatus",
                    content = {@Content(mediaType = "application/json", schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    @DeleteMapping("borrowing/{requestId}")
    public ResponseEntity<HttpStatus> deleteBorrowingRequest(@PathVariable Long requestId) {
        requestService.deleteBorrowingRequest(requestId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete lending request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Return HttpStatus",
                    content = {@Content(mediaType = "application/json", schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    @DeleteMapping("lending/{requestId}")
    public ResponseEntity<HttpStatus> deleteLendingRequest(@PathVariable Long requestId) {
        requestService.deleteLendingRequest(requestId);
        return ResponseEntity.noContent().build();
    }

}
