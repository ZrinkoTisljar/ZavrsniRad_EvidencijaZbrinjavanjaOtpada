package com.example.wasteapp.user;

/**
 PROGRAMSKI ENTITET: interface (Spring Data JPA Repository)

 SVRHA:
 - Sloj pristupa bazi za entitet User.
 - Spring automatski generira implementaciju (ne pišemo SQL ručno).

 BITNO:
 - JpaRepository<User, Long> daje gotove CRUD metode (save, findById, findAll, deleteById...)
 - Metode tipa findByEmail Spring generira po nazivu (Query method naming).
*/

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /** Dohvaća korisnika po emailu (koristi se kod login-a).*/
    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    List<User> findAll();

    List<User> findByRole(Role role);

    List<User> findByUserType(UserType userType);

    Optional<User> findByEmailAndRole(String email, Role role);

    List<User> findByEmailContaining(String text);

    List<User> findByCompanyNameIsNotNull();

    /** Provjerava postoji li korisnik s tim emailom (koristi se kod registracije). */
    boolean existsByEmail(String email);

    // Dohvaća sve korisnike koji čekaju odobrenje
    List<User> findByApprovedFalse();
}
