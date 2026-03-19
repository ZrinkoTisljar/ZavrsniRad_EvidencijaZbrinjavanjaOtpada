import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import {
  completeWorkOrder,
  fetchAllWorkOrders,
  scheduleWorkOrder,
  filterWorkOrders,
} from "../api/adminWorkOrders"
import { createManifestForWorkOrder } from "../api/adminManifests"

import { statusTranslations,unitTranslations, translate } from '../utils/translations';

/**
 * STRANICA: AdminWorkOrdersPage
 * SVRHA:
 * - prikaz svih radnih naloga u sustavu
 * - admin akcije: schedule, complete, generate manifest
 */
function AdminWorkOrdersPage() {
  const navigate = useNavigate()

  const [orders, setOrders] = useState([])
  const [loading, setLoading] = useState(true)
  const [busyId, setBusyId] = useState(null) // koji red trenutno "radi" akciju
  const [error, setError] = useState("")
  const [info, setInfo] = useState("")

  const [statusFilter, setStatusFilter] = useState("")
  const [wasteTypeCodeFilter, setWasteTypeCodeFilter] = useState("")
  const [cityFilter, setCityFilter] = useState("")
  const [userEmailFilter, setUserEmailFilter] = useState("")



  async function load() {
    setError("")
    setInfo("")
    setLoading(true)

    try {
      const data = await fetchAllWorkOrders()
      setOrders(data)
    } catch (err) {
      setError(err.message || "Greška kod dohvaćanja naloga.")
    } finally {
      setLoading(false)
    }
  }

    async function handleFilter() {
    setError("")
    setInfo("")
    setLoading(true)

    try {
      const data = await filterWorkOrders({
        status: statusFilter,
        wasteTypeCode: wasteTypeCodeFilter,
        city: cityFilter,
        userEmail: userEmailFilter,
      })

      setOrders(data)
    } catch (err) {
      setError(err.message || "Filtriranje nije uspjelo.")
    } finally {
      setLoading(false)
    }
  }

  async function handleResetFilters() {
    setStatusFilter("")
    setWasteTypeCodeFilter("")
    setCityFilter("")
    setUserEmailFilter("")

    await load()
  }

  useEffect(() => {
    load()
  }, [])

  async function handleSchedule(id) {
    setError("")
    setInfo("")
    setBusyId(id)

    try {
      await scheduleWorkOrder(id)
      setInfo(`Nalog ${id} je planiran (SCHEDULED).`)
      await load()
    } catch (err) {
      setError(err.message || "Schedule nije uspio.")
    } finally {
      setBusyId(null)
    }
  }

  async function handleComplete(id) {
    setError("")
    setInfo("")
    setBusyId(id)

    try {
      await completeWorkOrder(id)
      setInfo(`Nalog ${id} je završen (COMPLETED).`)
      await load()
    } catch (err) {
      setError(err.message || "Complete nije uspio.")
    } finally {
      setBusyId(null)
    }
  }

  async function handleGenerateManifest(order) {
    setError("")
    setInfo("")
    setBusyId(order.id)

    try {
      // manifest se smije generirati za SCHEDULED ili COMPLETED (po backend logici)
      // note se moze promjeniti, ali mi ovdje stavljamo fiksnu poruku koja jasno govori da je manifest generiran iz admin sučelja i za koji je nalog
      const manifest = await createManifestForWorkOrder(
        order.id,
        `Generirano iz admin sučelja za WorkOrder ${order.id}`
      )

      setInfo(`Manifest kreiran: ${manifest.manifestNumber} (id=${manifest.id}).`)
    } catch (err) {
      setError(err.message || "Generiranje manifesta nije uspjelo.")
    } finally {
      setBusyId(null)
    }
  }

  return (
    <div className="page">
      <div className="card wide-card">
        <h1>Svi radni nalozi</h1>
        {/* Gumbi za navigaciju */}
        <div className="button-row">
          <button onClick={() => navigate("/admin")}>Natrag</button>
          <button onClick={load} disabled={loading}>
            Osvježi
          </button>
        </div>

        {/* --- Filter forma --- */}
        <div className="filter-section" style={{ border: '1px solid #ccc', padding: '15px', marginBottom: '20px', borderRadius: '5px' }}>
          <h2>Filtriranje naloga</h2>

          <div className="filter-grid">
            <div>
              <label style={{ display: 'block', marginBottom: '5px' }}>Status</label>
              <select
                value={statusFilter}
                onChange={(e) => setStatusFilter(e.target.value)}
                style={{ padding: '5px' }}
              >
                <option value="">-- svi statusi --</option>
                <option value="CREATED">Kreiran</option>
                <option value="SCHEDULED">Planiran odvoz</option>
                <option value="COMPLETED">Završen</option>
                <option value="CANCELLED">Otkazan</option>
              </select>
            </div>

            <div>
              <label style={{ display: 'block', marginBottom: '5px' }}>Waste Type Code</label>
              <input
                type="text"
                value={wasteTypeCodeFilter}
                onChange={(e) => setWasteTypeCodeFilter(e.target.value)}
                placeholder="npr. PLASTIC"
                style={{ padding: '5px' }}
              />
            </div>

            <div>
              <label style={{ display: 'block', marginBottom: '5px' }}>Grad</label>
              <input
                type="text"
                value={cityFilter}
                onChange={(e) => setCityFilter(e.target.value)}
                placeholder="npr. Cakovec"
                style={{ padding: '5px' }}
              />
            </div>

            <div>
              <label style={{ display: 'block', marginBottom: '5px' }}>User Email</label>
              <input
                type="text"
                value={userEmailFilter}
                onChange={(e) => setUserEmailFilter(e.target.value)}
                placeholder="npr. user@test.com"
                style={{ padding: '5px' }}
              />
            </div>
          </div>

          <div className="button-row">
            <button onClick={handleFilter}>Filtriraj</button>
            <button onClick={handleResetFilters}>Resetiraj</button>
          </div>
        </div>
        {/* --- KRAJ BLOKA ZA FILTRE --- */}

        {/* Poruke o statusu (loading, error, info) */}
        {loading && <p>Učitavanje...</p>}
        {error && <div className="error">{error}</div>}
        {info && <div className="success">{info}</div>}

        {!loading && orders.length === 0 && <p>Nema naloga.</p>}

        {!loading && orders.length > 0 && (
          <table className="data-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Korisnik</th>
                <th>Vrsta otpada</th>
                <th>Lokacija</th>
                <th>Količina</th>
                <th>Status</th>
                <th>Akcije</th>
              </tr>
            </thead>

            <tbody>
              {orders.map((o) => {
                const isBusy = busyId === o.id

                return (
                  <tr key={o.id}>
                    <td>{o.id}</td>
                    <td>{o.userId}</td>
                    <td>{o.wasteTypeName}</td>
                    <td>
                      {o.collectionPointName} ({o.collectionPointCity})
                    </td>
                    <td>
                      {o.quantity} {translate(unitTranslations, o.unit)}
                    </td>
                    <td>{translate(statusTranslations, o.status)}</td>
                    <td>
                      <div className="button-row">
                        <button
                          onClick={() => handleSchedule(o.id)}
                          disabled={isBusy}
                        >
                          Kreiraj nalog 
                        </button>

                        <button
                          onClick={() => handleComplete(o.id)}
                          disabled={isBusy}
                        >
                          Završi nalog
                        </button>

                        <button
                          onClick={() => handleGenerateManifest(o)}
                          disabled={isBusy}
                        >
                          Generiraj prateći list
                        </button>
                      </div>
                    </td>
                  </tr>
                )
              })}
            </tbody>
          </table>
        )}
      </div>
    </div>
  )
}

export default AdminWorkOrdersPage