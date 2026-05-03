package com.example.wasteapp.auth;

import com.example.wasteapp.user.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * PROGRAMSKI ENTITET: DTO class   (Data Transfer Object)
 *
 * SVRHA:
 * - Definira strukturu JSON objekta koji klijent šalje prilikom registracije.
 * - Validacija inputa putem Bean Validation anotacija.
 * Napomena: Koristi Jakarta Bean Validation za automatsku provjeru sintaksne iispravnosti podataka.
 */
public class RegisterRequest {

    // Provjerava je li string prazan i je li ispravnog emaila formata (npr. ime@domena.com)
    @NotBlank(message = "Email je obvezan.")
    @Email(message = "Email mora biti u ispravnom formatu.")
    private String email;

    // Obraničava duljinu lozinke. 72 znaka je limit koji BCrypt algoritam obrađuje
    @NotBlank(message = "Lozinka ne smije biti prazna")
    @Size(min = 8, max = 72, message = "Lozinka mora imati između 8 i 72 znaka.")
    private String password;

    // Osigurava da je tip korisnika (Enum) poslan u zahtjevu.
    @NotNull(message = "Tip korisnika je obvezan (CITIZEN ili COMPANY).")
    private UserType userType; // CITIZEN ili COMPANY

    // Polja specifična za građane (nije stavljen @NotBlank jer ovisi o userType-u)
    private String fullName;

    // Polja specifična za tvrtke
    private String companyName;
    private String oib;

    // Adresa je obvezna za oba tipa korisnika radi evidencije zbrinjavanja otpada
    @NotBlank(message = "Adressa je obvezna za potrebe evidencije.")
    private String address;

    private String phone;

    //---GETTERI I SETTERI---
    // Standardne metode koje Spring koristi za mapiranje JSON-a u Java objekt (Deserialization)
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

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
}
