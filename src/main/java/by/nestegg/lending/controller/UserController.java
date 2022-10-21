package by.nestegg.lending.controller;

import by.nestegg.lending.controller.request.UserLikeRequest;
import by.nestegg.lending.controller.view.LikeCountView;
import by.nestegg.lending.controller.view.UserLikeResultView;
import by.nestegg.lending.dto.UserCommentDto;
import by.nestegg.lending.dto.UserDataDto;
import by.nestegg.lending.dto.UserDto;
import by.nestegg.lending.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

@Tag(name = "User Controller")
@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Update user profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return UserDTO",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))}),
            @ApiResponse(responseCode = "400", description = "Nickname cannot be empty<br>" +
                    "PushActive cannot be empty<br>User by id not found", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "User by id not found", content = @Content)})
    @PutMapping("profile/{userId}")
    public ResponseEntity<UserDto> updateUserProfile(@PathVariable Long userId, @RequestBody @Validated UserDto userDto) {
        if (!userId.equals(userDto.getId())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(userService.updateUserProfile(userDto));
    }

    @Operation(summary = "Get user profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return UserDTO",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    @GetMapping("profile")
    public ResponseEntity<UserDto> getUserProfile(Authentication authentication) {
        return ResponseEntity.ok(userService.getUserProfile(authentication.getName()));
    }

    @Operation(summary = "Remove facebook data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Return HttpStatus",
                    content = {@Content(mediaType = "application/json", schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    @PutMapping("{userId}/facebook")
    public ResponseEntity<HttpStatus> removeFacebook(@PathVariable Long userId) {
        userService.removeFacebook(userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Remove linkedin data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Return HttpStatus",
                    content = {@Content(mediaType = "application/json", schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    @PutMapping("{userId}/linkedin")
    public ResponseEntity<HttpStatus> removeLinkedIn(@PathVariable Long userId) {
        userService.removeLinkedIn(userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Remove instagram data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Return HttpStatus",
                    content = {@Content(mediaType = "application/json", schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    @PutMapping("{userId}/instagram")
    public ResponseEntity<HttpStatus> removeInstagram(@PathVariable Long userId) {
        userService.removeInstagram(userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Remove vkontakte data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Return HttpStatus",
                    content = {@Content(mediaType = "application/json", schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    @PutMapping("{userId}/vkontakte")
    public ResponseEntity<HttpStatus> removeVkontakte(@PathVariable Long userId) {
        userService.removeVkontakte(userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get User Data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return UserDataDto",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDataDto.class))}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "User by id not found", content = @Content)})
    @GetMapping("{userOwnerId}/data/{userId}")
    public ResponseEntity<UserDataDto> getUserData(@PathVariable Long userOwnerId,
                                                   @PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserData(userId, userOwnerId));
    }

    @Operation(summary = "Get user likes and dislikes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return UserLikeResultView",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserLikeResultView.class))}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    @GetMapping("{userOwnerId}/like/{userId}")
    public ResponseEntity<UserLikeResultView> getUserLikesDislikes(@PathVariable Long userOwnerId,
                                                                   @PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserLikeResultView(userId, userOwnerId));
    }

    @Operation(summary = "Set user like")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return LikeCountView",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = LikeCountView.class))}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    @PutMapping("like")
    public ResponseEntity<LikeCountView> saveLike(@RequestBody @Validated UserLikeRequest userLikeRequest) {
        userService.saveUserLike(userLikeRequest.getIsLike(), userLikeRequest.getUserId(),
                userLikeRequest.getUserOwnerId());
        return ResponseEntity.ok(userService.getLikeCountView(userLikeRequest.getUserId()));
    }

    @Operation(summary = "Delete user like")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return LikeCountView",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = LikeCountView.class))}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    @DeleteMapping("{userOwnerId}/like/{userId}")
    public ResponseEntity<LikeCountView> deleteLike(@PathVariable Long userOwnerId, @PathVariable Long userId) {
        userService.deleteUserLike(userId, userOwnerId);
        return ResponseEntity.ok(userService.getLikeCountView(userId));
    }

    @Operation(summary = "Save user comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return UserCommentDto",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserCommentDto.class))}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "User by id not found", content = @Content)})
    @PostMapping("comment")
    public ResponseEntity<UserCommentDto> saveComment(@RequestBody @Validated UserCommentDto userCommentDto) {
        return ResponseEntity.ok(userService.saveUserComment(userCommentDto));
    }

    @Operation(summary = "Update user comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return UserCommentDto",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserCommentDto.class))}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    @PutMapping("comment/{id}")
    public ResponseEntity<UserCommentDto> updateComment(@PathVariable Long id, @RequestBody @NotBlank String comment) {
        return ResponseEntity.ok(userService.updateUserComment(id, comment));
    }

    @Operation(summary = "Delete user comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Return HttpStatus",
                    content = {@Content(mediaType = "application/json", schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    @DeleteMapping("comment/{id}")
    public ResponseEntity<HttpStatus> deleteComment(@PathVariable Long id) {
        userService.deleteUserComment(id);
        return ResponseEntity.noContent().build();
    }

}
