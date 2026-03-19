import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { fetchCollectionPoints, fetchWasteTypes } from '../api/reference'
import { createWorkOrder } from '../api/workOrders'

// STRANICA: CreateWorkOrderPage
// SVRHA:
// - korisnik bira vrstu otpada i lokaciju
// - unosi količinu, jedinicu i napomenu
// - kreira novi work order

function CreateWorkOrderPage() {
  const navigate = useNavigate()

  const [wasteTypes, setWasteTypes] = useState([])
  const [collectionPoints, setCollectionPoints] = useState([])

  const [wasteTypeId, setWasteTypeId] = useState('')
  const [collectionPointId, setCollectionPointId] = useState('')
  const [quantity, setQuantity] = useState('')
  const [unit, setUnit] = useState('KG')
  const [note, setNote] = useState('')

  const [loading, setLoading] = useState(true)
  const [submitting, setSubmitting] = useState(false)
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')

  useEffect(() => {
    async function loadData() {
      try {
        const [wasteTypesData, collectionPointsData] = await Promise.all([
          fetchWasteTypes(),
          fetchCollectionPoints(),
        ])

        setWasteTypes(wasteTypesData)
        setCollectionPoints(collectionPointsData)
      } catch (err) {
        setError('Ne mogu učitati vrste otpada i lokacije.')
      } finally {
        setLoading(false)
      }
    }

    loadData()
  }, [])

  async function handleSubmit(e) {
    e.preventDefault()
    setError('')
    setSuccess('')
    setSubmitting(true)

    try {
      await createWorkOrder({
        wasteTypeId: Number(wasteTypeId),
        collectionPointId: Number(collectionPointId),
        quantity: Number(quantity),
        unit,
        note,
      })

      setSuccess('Radni nalog je uspješno kreiran.')
      setWasteTypeId('')
      setCollectionPointId('')
      setQuantity('')
      setUnit('KG')
      setNote('')
    } catch (err) {
      setError(err.message || 'Greška kod kreiranja naloga.')
    } finally {
      setSubmitting(false)
    }
  }

  if (loading) {
    return (
      <div className="page">
        <div className="card">Učitavanje...</div>
      </div>
    )
  }

  return (
    <div className="page">
      <div className="card wide-card">
        <h1>Novi radni nalog</h1>

        <form onSubmit={handleSubmit} className="form">
          <label>Vrsta otpada</label>
          <select value={wasteTypeId} onChange={(e) => setWasteTypeId(e.target.value)} required>
            <option value="">-- odaberi vrstu otpada --</option>
            {wasteTypes.map((wt) => (
              <option key={wt.id} value={wt.id}>
                {wt.name} ({wt.code})
              </option>
            ))}
          </select>

          <label>Lokacija</label>
          <select value={collectionPointId} onChange={(e) => setCollectionPointId(e.target.value)} required>
            <option value="">-- odaberi lokaciju --</option>
            {collectionPoints.map((cp) => (
              <option key={cp.id} value={cp.id}>
                {cp.name} - {cp.city}
              </option>
            ))}
          </select>

          <label>Količina</label>
          <input
            type="number"
            step="0.001"
            min="0.001"
            value={quantity}
            onChange={(e) => setQuantity(e.target.value)}
            required
          />

          <label>Jedinica</label>
          <select value={unit} onChange={(e) => setUnit(e.target.value)}>
            <option value="KG">KG</option>
            <option value="T">T</option>
            <option value="M3">M3</option>
          </select>

          <label>Napomena</label>
          <textarea
            value={note}
            onChange={(e) => setNote(e.target.value)}
            rows="4"
            placeholder="upiši dodatnu napomenu"
          />

          {error && <div className="error">{error}</div>}
          {success && <div className="success">{success}</div>}

          <div className="button-row">
            <button type="button" onClick={() => navigate('/user')}>
              Natrag
            </button>

            <button type="submit" disabled={submitting}>
              {submitting ? 'Spremanje...' : 'Kreiraj nalog'}
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}

export default CreateWorkOrderPage