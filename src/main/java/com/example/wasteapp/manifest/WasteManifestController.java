package com.example.wasteapp.manifest;

import com.example.wasteapp.manifest.dto.WasteManifestResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CONTROLLER: WasteManifestController
 * SVRHA: USER pregledava svoje prateće listove i PDF verzije.
 * ADMIN također može pristupiti svim dokumentima.
 */
@RestController
@RequestMapping("/api/manifests")
public class WasteManifestController {

    private final WasteManifestService service;
    private final WasteManifestPdfService pdfService;

    public WasteManifestController(WasteManifestService service, WasteManifestPdfService pdfService) {
        this.service = service;
        this.pdfService = pdfService;
    }

    /**
     * USER: lista vlastitih manifesta.
     */
    @GetMapping("/mine")
    public List<WasteManifestResponse> mine(Authentication auth) {
        String email = auth.getName();
        return service.listMine(email).stream().map(WasteManifestResponse::new).toList();
    }

    /**
     * USER/ADMIN: dohvat jednog manifesta uz provjeru pristupa.
     */
    @GetMapping("/{id}")
    public WasteManifestResponse getById(@PathVariable Long id, Authentication auth) {
        String email = auth.getName();

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        WasteManifest wm = service.getAccessibleManifest(id, email, isAdmin);
        return new WasteManifestResponse(wm);
    }

    /**
     * USER/ADMIN: dohvat PDF verzije manifesta uz provjeru pristupa.
     */
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> pdf(@PathVariable Long id, Authentication auth) {
        String email = auth.getName();

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        WasteManifest wm = service.getAccessibleManifest(id, email, isAdmin);
        byte[] pdfBytes = pdfService.generatePdf(wm);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + wm.getManifestNumber() + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}