package com.example.wasteapp.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CONTROLLER: AdminUserController
 * SVRHA: Admin upravlja korisnicima (odobravanje i brisanje).
 */
@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final UserRepository userRepository;

    public AdminUserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 1) Dohvat svih korisnika koji ČEKAJU odobrenje
    @GetMapping("/pending")
    public List<User> getPendingUsers() {
        return userRepository.findByApprovedFalse();
    }

    // 2) Odobravanje korisnika
    @PatchMapping("/{id}/approve")
    public ResponseEntity<?> approveUser(@PathVariable Long id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Korisnik nije pronađen"));

        u.setApproved(true);
        userRepository.save(u);
        return ResponseEntity.ok("Korisnik je uspješno odobren.");
    }

    // 3) Brisanje korisnika (bilo odobrenog ili neodobrenog)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        // Provjera postoji li prije brisanja
        if (!userRepository.existsById(id)) {
            return ResponseEntity.badRequest().body("Korisnik ne postoji.");
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok("Korisnik je uspješno obrisan.");
    }
}