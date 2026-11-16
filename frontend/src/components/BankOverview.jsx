import { useEffect, useState } from 'react';
import { getTotalBalance, getTop5 } from '../api/bankApi.js';
import { formatCHF } from '../utils/money.js';
import { Notice, SectionTitle } from './Common.jsx';

export default function BankOverview({ reloadKey }) {
  const [overview, setOverview] = useState({ total: null, highest: [], lowest: [] });
  const [pending, setPending] = useState(true);
  const [error, setError] = useState('');
  useEffect(() => {
    let active = true;
    setPending(true);
    Promise.all([getTotalBalance(), getTop5('highest'), getTop5('lowest')])
      .then(([total, highest, lowest]) => {
        if (active) { setOverview({ total: total.balance, highest, lowest }); setError(''); }
      })
      .catch(err => { if (active) setError(err.message); })
      .finally(() => { if (active) setPending(false); });
    return () => { active = false; };
  }, [reloadKey]);

  return <section className="panel panel--overview">
    <SectionTitle kicker="03 / BANKÜBERSICHT" title="Alle Konten" subtitle="Aktuelle Zahlen direkt aus dem Backend."/>
    {error && <Notice type="error">{error}</Notice>}
    {pending && <p className="muted" role="status">Bankdaten werden geladen …</p>}
    {!pending && overview.total !== null && <>
      <div className="total-card"><span>Gesamtsaldo der Bank</span>
        <strong>{formatCHF(overview.total)}</strong></div>
      <div className="rankings">
        <Ranking title="Höchste Salden" items={overview.highest}/>
        <Ranking title="Niedrigste Salden" items={overview.lowest}/>
      </div>
    </>}
  </section>;
}

function Ranking({ title, items }) {
  return <div className="ranking">
    <h3>{title}</h3>
    {items.length === 0 && <p className="muted">Noch keine Konten vorhanden.</p>}
    {items.map((item, i) => <div className="ranking-row" key={item.id}>
      <span className="rank-number">{String(i + 1).padStart(2, '0')}</span>
      <span className="rank-id">{item.id}</span>
      <strong>{formatCHF(item.balance)}</strong>
    </div>)}
  </div>;
}
