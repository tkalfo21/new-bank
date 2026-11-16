import { useEffect, useState } from 'react';
import { getBookings } from '../api/bankApi.js';
import { formatCHF } from '../utils/money.js';
import { Notice, SectionTitle } from './Common.jsx';

export default function BookingHistory({ accountId, reloadKey }) {
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  useEffect(() => {
    if (!accountId) { setBookings([]); return; }
    let active = true;
    setLoading(true);
    getBookings(accountId)
      .then(rows => { if (active) { setBookings(rows); setError(''); } })
      .catch(err => { if (active) { setError(err.message); setBookings([]); } })
      .finally(() => { if (active) setLoading(false); });
    return () => { active = false; };
  }, [accountId, reloadKey]);
  return <section className="panel panel--history">
    <SectionTitle kicker="04 / TRANSAKTIONEN" title="Buchungshistorie"
      subtitle={accountId ? `Alle Buchungen von ${accountId}` : 'Wähle zuerst ein Konto aus.'}/>
    {error && <Notice type="error">{error}</Notice>}
    {loading && <p className="muted" role="status">Buchungen werden geladen …</p>}
    {!loading && bookings.length === 0 && !error && <p className="empty-state">Noch keine Buchungen vorhanden.</p>}
    {!loading && bookings.length > 0 && <div className="booking-list">
      {bookings.map((item, i) => <div className="booking-row" key={`${item.date}-${i}`}>
        <div className={`booking-icon ${item.amount < 0 ? 'negative' : 'positive'}`}>
          {item.amount < 0 ? '↗' : '↙'}
        </div>
        <div className="booking-description"><strong>{item.text}</strong>
          <span>{new Date(item.date).toLocaleString('de-CH')}</span></div>
        <strong className={item.amount < 0 ? 'money-negative' : 'money-positive'}>
          {item.amount > 0 ? '+' : ''}{formatCHF(item.amount)}
        </strong>
      </div>)}
    </div>}
  </section>;
}
