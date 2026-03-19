// src/api/adminUsers.js
// ovjde definirane tri funkcije za dohvatanje pending korisnika, odobravanje i brisanje korisnika. 
// Svaka funkcija koristi fetch API za komunikaciju s backendom, a token za autorizaciju se dohvaća iz localStorage-a. 
// U slučaju neuspješnog odgovora, baca se greška s odgovarajućom porukom.

// API POZIVI ZA ADMINISTRACIJU KORISNIKA
// SVRHA:
// - dohvati sve korisnike koji čekaju odobrenje
// - odobri korisnika
// - odbaci (obriši) korisnika
const API_URL = 'http://localhost:8080/api/admin/users';

// Pomoćna funkcija za dohvat tokena
const getAuthHeaders = () => {
    
    // 1. Dohvaća string iz localStorage-a pod točnim ključem
    const authDataString = localStorage.getItem('wasteapp_auth');
    let token = '';

    // 2. Ako podaci postoje, parsira JSON i izvlačimo samo token
    if (authDataString) {
        const authData = JSON.parse(authDataString);
        token = authData.token;
    }return {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
    };
};

export const getPendingUsers = async () => {
    const response = await fetch(`${API_URL}/pending`, {
        method: 'GET',
        headers: getAuthHeaders(),
    });
    if (!response.ok) throw new Error('Greška pri dohvatu korisnika');
    return response.json();
};

export const approveUser = async (id) => {
    const response = await fetch(`${API_URL}/${id}/approve`, {
        method: 'PATCH',
        headers: getAuthHeaders(),
    });
    if (!response.ok) throw new Error('Greška pri odobravanju korisnika');
    return response.text();
};

export const deleteUser = async (id) => {
    const response = await fetch(`${API_URL}/${id}`, {
        method: 'DELETE',
        headers: getAuthHeaders(),
    });
    if (!response.ok) throw new Error('Greška pri brisanju korisnika');
    return response.text();
};