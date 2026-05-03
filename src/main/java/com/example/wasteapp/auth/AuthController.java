package com.example.wasteapp.auth;

import com.example.wasteapp.user.Role;
import com.example.wasteapp.user.User;
import com.example.wasteapp.user.UserRepository;
import com.example.wasteapp.user.UserType;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.example.wasteapp.security.JwtService;




/**
 * PROGRAMSKI ENTITET: REST controller class
 *
 * SVRHA:
 * - Endpointi za autentifikaciju (register/login kasnije).
 * - Trenutno: /api/auth/register
 */
@RestController // označava da je ovo REST API kontroler, sve metode vraćaju JSON (ne HTML)
@RequestMapping("/api/auth") /**Definira bazni URL put za sve metode u ovom kontroleru*/
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /**
     * Registracija korisnika :
     * - korisnik se sam registrira kao CITIZEN ili COMPANY
     * - uloga se postavlja na USER (admin se radi posebno)
     */
    @PostMapping("/register") // API endpoint, metoda POST, URL:/api/auth
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {// @Valid pokrece Bean Validation

        // 1) unique email check
        if (userRepository.existsByEmail(req.getEmail())) {  // koristi Spring Data JPA metodu existByEmail
            return ResponseEntity.badRequest().body("Email already exists.");
        }

        // 2) business validation by userType
        if (req.getUserType() == UserType.CITIZEN) {
            if (req.getFullName() == null || req.getFullName().isBlank()) {
                return ResponseEntity.badRequest().body("fullName is required for CITIZEN.");
            }
        } else if (req.getUserType() == UserType.COMPANY) {
            if (req.getCompanyName() == null || req.getCompanyName().isBlank()) {
                return ResponseEntity.badRequest().body("companyName is required for COMPANY.");
            }
            if (req.getOib() == null || req.getOib().isBlank()) {
                return ResponseEntity.badRequest().body("oib is required for COMPANY.");
            }
        }

        // 3) hash password (NE sprema plain password)
        String hash = passwordEncoder.encode(req.getPassword());

        // 4) map DTO -> Entity , DTO sluzi za prijenos podataka, Entitet predstavlja tablicu u bazi
        User u = new User();
        u.setEmail(req.getEmail());
        u.setPasswordHash(hash);
        u.setRole(Role.USER);
        u.setUserType(req.getUserType());
        u.setFullName(req.getFullName());
        u.setCompanyName(req.getCompanyName());
        u.setOib(req.getOib());
        u.setAddress(req.getAddress());
        u.setPhone(req.getPhone());

        // 5) save
        // Persistencija (spremanje u bazu)
        // Spring Data JPA generira SQL INSERT naredbu.
        userRepository.save(u);

        // Odgovor klijenu o uspješnoj operaciji, vraca HTTP 200 OK, frontend dobiva poruku da je registracija uspjesna
        return ResponseEntity.ok("Registered.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {

        // 1) nađi korisnika po emailu
        User u = userRepository.findByEmail(req.getEmail()).orElse(null);
        if (u == null) {
            // ne otkrivamo je li email postoji (sigurnosna praksa)
            return ResponseEntity.status(401).body("Invalid credentials.");
        }

        // 2) usporedi raw password s hashom iz baze
        boolean ok = passwordEncoder.matches(req.getPassword(), u.getPasswordHash());
        if (!ok) {
            return ResponseEntity.status(401).body("Invalid credentials.");
        }

        // NOVO: 2.5) Provjera je li korisnik odobren
        if (!u.isApproved()) {
            return ResponseEntity.status(403).body("Vaš račun još nije odobren od strane administratora.");
        }

        /** // 3) ako je ok, vrati osnovne podatke (bez lozinke!)
        return ResponseEntity.ok(
                "Login OK. userId=" + u.getId() + ", role=" + u.getRole() + ", type=" + u.getUserType()
        );*/

       /** String token = jwtService.generateToken(u.getId(), u.getEmail(), u.getRole().name());
        return ResponseEntity.ok(token);
        */
        String token = jwtService.generateToken(u.getId(), u.getEmail(), u.getRole().name());
        return ResponseEntity.ok(new AuthResponse(token, u.getEmail(), u.getRole().name(), u.getUserType().name()));
    }

}
