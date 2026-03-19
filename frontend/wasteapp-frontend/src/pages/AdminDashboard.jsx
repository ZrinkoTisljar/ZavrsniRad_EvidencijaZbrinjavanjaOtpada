import { useNavigate } from 'react-router-dom'
import { clearAuth, getAuth } from '../utils/auth'

import { Link } from 'react-router-dom'



// STRANICA: AdminDashboard
// SVRHA: početni ekran za administratora

function AdminDashboard() {
  const navigate = useNavigate()
  const auth = getAuth()

  function handleLogout() {
    clearAuth()
    navigate('/login')
  }

   return (
    <div className="page">
      <div className="card wide-card">
        <h1>Admin Dashboard</h1>
        <p>Prijavljen admin: {auth?.email}</p>

        <div className="menu">
          <Link className="menu-link" to="/admin/work-orders">
            Svi radni nalozi
          </Link>

          <Link className="menu-link" to="/admin/reports">
            Izvješća
          </Link>

          <Link className="menu-link" to="/admin/manifests">
            Prateći listovi
           
          </Link>
        </div>
        <Link className="menu-link" to="/admin/users">
          Upravljanje korisnicima
        </Link>

        <button onClick={handleLogout}>Odjava</button>
      </div>
    </div>
  )
  
}

export default AdminDashboard