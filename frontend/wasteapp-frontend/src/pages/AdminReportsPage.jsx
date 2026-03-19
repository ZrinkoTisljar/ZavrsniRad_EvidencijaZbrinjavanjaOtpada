import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import {
  fetchWasteByCity,
  fetchWasteByType,
  fetchWorkOrdersByStatus
} from "../api/reports"

/**
 * STRANICA: AdminReportsPage
 *
 * SVRHA:
 * - prikazuje sva izvješća sustava
 */

function AdminReportsPage() {

  const navigate = useNavigate()

  const [wasteByType, setWasteByType] = useState([])
  const [statusReport, setStatusReport] = useState([])
  const [wasteByCity, setWasteByCity] = useState([])

  const [loading, setLoading] = useState(true)
  const [error, setError] = useState("")

  useEffect(() => {

    async function loadReports() {

      try {

        const [typeData, statusData, cityData] = await Promise.all([
          fetchWasteByType(),
          fetchWorkOrdersByStatus(),
          fetchWasteByCity()
        ])

        setWasteByType(typeData)
        setStatusReport(statusData)
        setWasteByCity(cityData)

      } catch (err) {

        setError("Greška kod dohvaćanja izvješća.")

      } finally {

        setLoading(false)

      }

    }

    loadReports()

  }, [])

  return (

    <div className="page">

      <div className="card wide-card">

        <h1>Admin izvješća</h1>

        <button onClick={() => navigate("/admin")}>
          Natrag
        </button>

        {loading && <p>Učitavanje...</p>}
        {error && <div className="error">{error}</div>}

        {!loading && (

          <>
            <h2>Ukupna količina otpada po vrsti</h2>

            <table className="data-table">

              <thead>
                <tr>
                  <th>Code</th>
                  <th>Name</th>
                  <th>Total Quantity</th>
                </tr>
              </thead>

              <tbody>
                {wasteByType.map(r => (
                  <tr key={r.wasteTypeCode}>
                    <td>{r.wasteTypeCode}</td>
                    <td>{r.wasteTypeName}</td>
                    <td>{r.totalQuantity}</td>
                  </tr>
                ))}
              </tbody>

            </table>


            <h2>Broj naloga po statusu</h2>

            <table className="data-table">

              <thead>
                <tr>
                  <th>Status</th>
                  <th>Count</th>
                </tr>
              </thead>

              <tbody>
                {statusReport.map(r => (
                  <tr key={r.status}>
                    <td>{r.status}</td>
                    <td>{r.totalCount}</td>
                  </tr>
                ))}
              </tbody>

            </table>


            <h2>Ukupna količina otpada po gradu</h2>

            <table className="data-table">

              <thead>
                <tr>
                  <th>City</th>
                  <th>Total Quantity</th>
                </tr>
              </thead>

              <tbody>
                {wasteByCity.map(r => (
                  <tr key={r.city}>
                    <td>{r.city}</td>
                    <td>{r.totalQuantity}</td>
                  </tr>
                ))}
              </tbody>

            </table>

          </>
        )}

      </div>

    </div>

  )

}

export default AdminReportsPage