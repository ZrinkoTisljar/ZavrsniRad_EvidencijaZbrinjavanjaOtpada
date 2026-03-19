import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { fetchMyWorkOrders } from '../api/workOrders'

// STRANICA: MyWorkOrdersPage
// SVRHA:
// - prikazuje sve naloge prijavljenog korisnika

function MyWorkOrdersPage() {
  const navigate = useNavigate()
  const [orders, setOrders] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    async function loadOrders() {
      try {
        const data = await fetchMyWorkOrders()
        setOrders(data)
      } catch (err) {
        setError('Ne mogu učitati tvoje radne naloge.')
      } finally {
        setLoading(false)
      }
    }

    loadOrders()
  }, [])

  return (
    <div className="page">
      <div className="card wide-card">
        <h1>Moji radni nalozi</h1>

        <button onClick={() => navigate('/user')}>Natrag</button>

        {loading && <p>Učitavanje...</p>}
        {error && <div className="error">{error}</div>}

        {!loading && !error && orders.length === 0 && <p>Nemaš još radnih naloga.</p>}

        {!loading && !error && orders.length > 0 && (
          <table className="data-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Vrsta otpada</th>
                <th>Lokacija</th>
                <th>Količina</th>
                <th>Jedinica</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              {orders.map((order) => (
                <tr key={order.id}>
                  <td>{order.id}</td>
                  <td>{order.wasteTypeName}</td>
                  <td>
                    {order.collectionPointName} ({order.collectionPointCity})
                  </td>
                  <td>{order.quantity}</td>
                  <td>{order.unit}</td>
                  <td>{order.status}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  )
}

export default MyWorkOrdersPage