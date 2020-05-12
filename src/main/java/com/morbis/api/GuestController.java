package com.morbis.api;

import com.goterl.lazycode.lazysodium.exceptions.SodiumException;
import com.morbis.api.dto.LoginDTO;
import com.morbis.api.dto.RegisterDTO;
import com.morbis.service.GuestService;
import com.morbis.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController()
@RequestMapping("api")
public class GuestController {

    private final GuestService guestService;
    private final AuthService authService;

    public GuestController(GuestService guestService, AuthService authService) {
        this.guestService = guestService;
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(summary = "register a new member to the system")
    @ApiResponse(responseCode = "202", description = "successfully registered")
    public ResponseEntity<?> register(@RequestBody RegisterDTO register) throws SodiumException {
        if (!authService.register(register.asMember()))
            throw new IllegalArgumentException("user already registered");

        return ResponseEntity.accepted().build();
    }

    @PostMapping("/login")
    @Operation(
            summary = "log in to the system",
            description = "logs the user into the system and returns an authentication token" +
                    "that will be used to authenticate the user in later requests, add the Authorization header" +
                    "and put the token in it to have the user authenticated in later requests." +
                    "Note: the authentication lasts for 15 minutes, after which a new token must be requested."
    )
    public ResponseEntity<String> login(@RequestBody LoginDTO login) {
        Optional<String> token = authService.login(login.username, login.password);
        if (token.isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        return ResponseEntity.ok(token.get());
    }

    @GetMapping("/logout/{token}")
    @Operation(
            summary = "log out of the system",
            description = "the token will be invalidated, and future requests using it will not be authorized."
    )
    public void logout(@PathVariable String token) {
        authService.logout(token);
    }
}
