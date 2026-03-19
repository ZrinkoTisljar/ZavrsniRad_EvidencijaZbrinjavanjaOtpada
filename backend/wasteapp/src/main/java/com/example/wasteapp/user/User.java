package com.example.wasteapp.user;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * KLASA: User (Persistent Entity)
 * SVRHA: Mapiranje Java objekta u tablicu baze podataka pomoću JPA (Hibernate).
 * TABLICA: users
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK (AUTO_INCREMENT)

    /**
     * email mora biti jedinstven (unique) i obavezan (NOT NULL)
     */
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /**
     * Lozinka se NE sprema u plain text, nego BCrypt hash.
     * Mapira se na stupac password_hash iz SQL migracije.
     */
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    /**
     * Uloga: ADMIN ili USER
     * Sprema se kao tekst (EnumType.STRING) u stupac role.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    /**
     * Tip korisnika: CITIZEN ili COMPANY
     * Mapira se na stupac user_type.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    private UserType userType;

    // CITIZEN
    @Column(name = "full_name")
    private String fullName;

    // COMPANY
    @Column(name = "company_name")
    private String companyName;

    @Column(name = "oib")
    private String oib;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "phone")
    private String phone;

    // Polje koje označava je li admin odobrio korisnika
    @Column(name = "is_approved", nullable = false)
    private boolean approved = false; // Po defaultu nitko nije odobren pri registraciji

    /**
     * Datum kreiranja korisnika (postavlja se u konstruktoru)
     * Mapira se na created_at stupac.
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public User() {
        // PROGRAMSKI ENTITET: konstruktor
        // SVRHA: automatski postavi createdAt kod kreiranja objekta (prije spremanja u bazu)
        this.createdAt = LocalDateTime.now();
    }

    // --- GETTERI/SETTERI ---

    public Long getId() { return id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public UserType getUserType() { return userType; }
    public void setUserType(UserType userType) { this.userType = userType; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getOib() { return oib; }
    public void setOib(String oib) { this.oib = oib; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { this.approved = approved; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
