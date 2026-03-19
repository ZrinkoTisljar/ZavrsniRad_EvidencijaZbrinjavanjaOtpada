// src/pages/AdminUsersPage.jsx
// STRANICA: AdminUsersPage
// SVRHA:
// - admin vidi sve korisnike koji su se registrirali i čekaju odobrenje
// - admin može odobriti ili odbiti registraciju korisnika
import React, { useEffect, useState } from 'react';
import { getPendingUsers, approveUser, deleteUser } from '../api/adminUsers';
import { useNavigate } from "react-router-dom"
import { userTypeTranslations, translate } from '../utils/translations';



const AdminUsersPage = () => {
    const [users, setUsers] = useState([]);
    const [error, setError] = useState('');
    const [message, setMessage] = useState('');

    const navigate = useNavigate()

    // Učitaj korisnike čim se stranica otvori
    useEffect(() => {
        loadUsers();
    }, []);

    const loadUsers = async () => {
        try {
            const data = await getPendingUsers();
            setUsers(data);
            setError('');
        } catch (err) {
            setError(err.message);
        }
    };

    const handleApprove = async (id) => {
        try {
            await approveUser(id);
            setMessage('Korisnik uspješno odobren!');
            // Nakon odobrenja, osvježi listu (ili samo izbaci tog korisnika iz state-a)
            setUsers(users.filter(u => u.id !== id));
        } catch (err) {
            setError(err.message);
        }
    };

    const handleDelete = async (id) => {
        if (!window.confirm('Jeste li sigurni da želite obrisati ovog korisnika?')) return;
        
        try {
            await deleteUser(id);
            setMessage('Korisnik uspješno obrisan!');
            setUsers(users.filter(u => u.id !== id));
        } catch (err) {
            setError(err.message);
        }
    };

    return (
        <div>
            <h2>Korisnici na čekanju za odobrenje</h2>
            
            <button onClick={() => navigate("/admin")}>
            Natrag
            </button>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            {message && <p style={{ color: 'green' }}>{message}</p>}

            {users.length === 0 ? (
                <p>Nema novih korisnika na čekanju.</p>
            ) : (
                <table border="1" cellPadding="5" style={{ width: '100%', marginTop: '20px', borderCollapse: 'collapse' }}>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Email</th>
                            <th>Tip</th>
                            <th>Naziv / Ime</th>
                            <th>Adresa</th>
                            <th>Akcije</th>
                        </tr>
                    </thead>
                    <tbody>
                        {users.map(user => (
                            <tr key={user.id}>
                                <td>{user.id}</td>
                                <td>{user.email}</td>
                                <td>{translate(userTypeTranslations, user.userType)}</td>
                                <td>{user.userType === 'COMPANY' ? user.companyName : user.fullName}</td>
                                <td>{user.address}</td>
                                <td>
                                    <button onClick={() => handleApprove(user.id)}>Odobri</button>
                                    <button onClick={() => handleDelete(user.id)}>Obriši</button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            )}
        </div>
    );
};

export default AdminUsersPage;