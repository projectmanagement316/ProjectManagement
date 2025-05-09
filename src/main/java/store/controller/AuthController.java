package store.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import store.service.AuthService;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestParam String mail, @RequestParam String password) {
        try {
            Map<String, String> userMap = authService.authenticateUser(mail, password);
            return ResponseEntity.ok(userMap);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "error", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Unexpected server error",
                    "details", e.getMessage()
            ));
        }
    }

    @PostMapping("/validate-token")
    public boolean validateToken(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return authService.isTokenValid(token);
    }

    @PostMapping("/register_student")
    public ResponseEntity<?> registerStudent(
            @RequestParam String mail,
            @RequestParam String name,
            @RequestParam String faculty,
            @RequestParam String department,
            @RequestParam String password) {

        Map<String, String> response = authService.register_student(mail, name, faculty, department, password);

        if ("True".equals(response.get("Successful"))) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/register_staff")
    public ResponseEntity<?> registerStaff(
            @RequestParam String mail,
            @RequestParam String name,
            @RequestParam String title,
            @RequestParam String faculty,
            @RequestParam String department,
            @RequestParam String password) {

        Map<String, String> response = authService.register_staff(mail, name, title, faculty, department, password);

        if ("True".equals(response.get("Successful"))) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

}
