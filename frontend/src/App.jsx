import { useCallback, useEffect, useState } from 'react';
import {
  createAccount, getAccount, getBalance, deposit, withdraw,
  getBookings, getTotalBalance, getTop5,
} from './api/bankApi.js';
import { formatCHF, francsToInternal } from './utils/money.js';

const ACCOUNT_NAMES = {
  savings: 'Sparkonto',
  'promo-youth': 'Jugendsparkonto',
  salary: 'Lohnkonto',
  SavingsAccount: 'Sparkonto',
  PromoYouthSavingsAccount: 'Jugendsparkonto',
  SalaryAccount: 'Lohnkonto',
};

function Notice({ type, children, onClose }) {
  if (!children) return null;
  return <div role="alert" className={`notice notice--${type}`}>
    <span aria-hidden="true">{type === 'error' ? '!' : '✓'}</span>
    <span>{children}</span>
    {onClose && <button className="notice__close" onClick={onClose} aria-label="Meldung schliessen">×</button>}
  </div>;
}

function SectionTitle({ kicker, title, subtitle }) {
  return <div className="section-heading">
    <span className="eyebrow">{kicker}</span><h2>{title}</h2>
    {subtitle && <p>{subtitle}</p>}
  </div>;
}

function AccountCreation({ busy, onCreate }) {
  const [type, setType] = useState('savings');
  const [limit, setLimit] = useState('-500');
  const submit = (event) => {
    event.preventDefault();
    try {
      onCreate(type, type === 'salary' ? francsToInternal(limit, { allowNegative: true }) : 0);
    } catch (error) {
      onCreate(null, null, error.message);
    }
  };
  return <section className="panel">
    <SectionTitle kicker="01 / KONTOERÖFFNUNG" title="Neues Konto" subtitle="Wähle eine Kontoart und lege ein neues Konto an."/>
    <form onSubmit={submit} className="form-stack">
      <label htmlFor="account-type">Kontoart</label>
      <select id="account-type" value={type} onChange={e => setType(e.target.value)} disabled={busy}>
        <option value="savings">Sparkonto · kein Überzug</option>
        <option value="promo-youth">Jugendsparkonto · CHF 200 / Monat</option>
        <option value="salary">Lohnkonto · Kreditlimite</option>
      </select>
      {type === 'salary' && <>
        <label htmlFor="credit-limit">Kreditlimite in CHF</label>
        <input id="credit-limit" value={limit} onChange={e => setLimit(e.target.value)}
          inputMode="decimal" placeholder="-500.00" disabled={busy}/>
        <small>Zum Beispiel −500.00. Das Lohnkonto darf bis zu dieser Grenze ins Minus.</small>
      </>}
      <button className="button button--primary" type="submit" disabled={busy}>
        {busy ? 'Konto wird erstellt …' : 'Konto eröffnen'} <span aria-hidden="true">→</span>
      </button>
    </form>
  </section>;
}

function AccountOperations({ selectedId, setSelectedId, balance, accountType, busy, onLookup, onTransaction }) {
  const [amount, setAmount] = useState('');
  const [text, setText] = useState('');
  function submitTransaction(direction) {
    try {
      const value = francsToInternal(amount);
      onTransaction(direction, value, text, () => { setAmount(''); setText(''); });
    } catch (error) {
      onTransaction(null, null, null, null, error.message);
    }
  }
  return <section className="panel panel--wide">
    <SectionTitle kicker="02 / KONTO VERWALTEN" title="Bankgeschäfte"
      subtitle="Kontostand abfragen, Geld einzahlen oder abheben."/>
    <form onSubmit={e => { e.preventDefault(); onLookup(); }} className="lookup-row">
      <div className="form-field">
        <label htmlFor="account-id">Kontonummer</label>
        <input id="account-id" placeholder="z. B. S-1000" value={selectedId}
          onChange={e => setSelectedId(e.target.value)} disabled={busy}/>
      </div>
      <button className="button button--outline" type="submit" disabled={busy || !selectedId.trim()}>
        {busy ? 'Lädt …' : 'Saldo prüfen'}
      </button>
    </form>
    {balance !== null && <div className="balance-card">
      <div><span className="balance-label">Aktueller Kontostand</span>
        <strong>{formatCHF(balance)}</strong>
        <span className="balance-id">{selectedId} · {ACCOUNT_NAMES[accountType] || 'Konto'}</span>
      </div>
      <span className="balance-symbol" aria-hidden="true">↗</span>
    </div>}
    <div className="transaction-form">
      <div className="form-field">
        <label htmlFor="amount">Betrag in CHF</label>
        <input id="amount" placeholder="z. B. 150.00" inputMode="decimal"
          value={amount} onChange={e => setAmount(e.target.value)} disabled={busy}/>
      </div>
      <div className="form-field">
        <label htmlFor="booking-text">Buchungstext (optional)</label>
        <input id="booking-text" placeholder="z. B. Taschengeld" value={text}
          onChange={e => setText(e.target.value)} disabled={busy}/>
      </div>
      <div className="transaction-actions">
        <button type="button" className="button button--primary" disabled={busy || !selectedId.trim()}
          onClick={() => submitTransaction('deposit')}>{busy ? 'Bitte warten …' : '+ Einzahlen'}</button>
        <button type="button" className="button button--dark" disabled={busy || !selectedId.trim()}
          onClick={() => submitTransaction('withdraw')}>{busy ? 'Bitte warten …' : '− Auszahlen'}</button>
      </div>
      <p className="hint">Beträge werden in CHF eingegeben und für das Backend in Millirappen umgerechnet.</p>
    </div>
  </section>;
}

function BankOverview({ reloadKey }) {
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

function BookingHistory({ accountId, reloadKey }) {
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

export default function App() {
  const [selectedId, setSelectedId] = useState('');
  const [loadedId, setLoadedId] = useState('');
  const [accountType, setAccountType] = useState('');
  const [balance, setBalance] = useState(null);
  const [pending, setPending] = useState(false);
  const [notice, setNotice] = useState(null);
  const [reloadKey, setReloadKey] = useState(0);

  const fail = useCallback((error) => {
    setNotice({ type: 'error', message: typeof error === 'string' ? error : error.message });
  }, []);

  async function handleCreate(type, creditLimit, validationError) {
    if (validationError) { fail(validationError); return; }
    setPending(true); setNotice(null);
    try {
      const result = await createAccount(type, creditLimit);
      setSelectedId(result.id); setLoadedId(result.id);
      setAccountType(type); setBalance(0);
      setNotice({ type: 'success', message: `Konto ${result.id} wurde erfolgreich eröffnet.` });
      setReloadKey(n => n + 1);
    } catch (error) { fail(error); }
    finally { setPending(false); }
  }

  async function handleLookup() {
    if (!selectedId.trim()) return;
    setPending(true); setNotice(null);
    try {
      const [details, result] = await Promise.all([getAccount(selectedId), getBalance(selectedId)]);
      setLoadedId(details.id); setAccountType(details.type); setBalance(result.balance);
      setNotice({ type: 'success', message: 'Kontostand erfolgreich aktualisiert.' });
    } catch (error) { setLoadedId(''); setBalance(null); fail(error); }
    finally { setPending(false); }
  }

  async function handleTransaction(direction, amount, text, onSuccess, validationError) {
    if (validationError) { fail(validationError); return; }
    setPending(true); setNotice(null);
    try {
      const action = direction === 'deposit' ? deposit : withdraw;
      const result = await action(selectedId, amount, text);
      setLoadedId(selectedId.trim()); setBalance(result.balance);
      setNotice({ type: 'success', message: direction === 'deposit' ? 'Einzahlung erfolgreich gebucht.' : 'Auszahlung erfolgreich gebucht.' });
      setReloadKey(n => n + 1);
      onSuccess?.();
    } catch (error) { fail(error); }
    finally { setPending(false); }
  }

  return <div className="app-shell">
    <header className="topbar">
      <div className="brand"><div className="brand-icon">N<span>·</span></div>
        <div><strong>newbank</strong><span>DEMO BANKING</span></div></div>
      <div className="topbar-right"><span className="live-dot"/> Spring Boot + React <span className="project-tag">IMS · M320</span></div>
    </header>
    <main className="main">
      <div className="welcome">
        <div><span className="eyebrow">BANKING DASHBOARD / ÜBUNG 11</span>
          <h1>Deine Bank.<br/><span>Alles im Blick.</span></h1>
          <p>Eröffne Konten, verwalte Ein- und Auszahlungen und kontrolliere deine Finanzen – direkt verbunden mit eurem Java-Backend.</p>
        </div>
        <div className="welcome-decoration" aria-hidden="true"><span>NB</span><div className="decoration-lines"/></div>
      </div>
      {notice && <Notice type={notice.type} onClose={() => setNotice(null)}>{notice.message}</Notice>}
      <div className="dashboard-grid">
        <AccountCreation busy={pending} onCreate={handleCreate}/>
        <AccountOperations selectedId={selectedId} setSelectedId={(id) => {
          setSelectedId(id);
          if (id.trim() !== loadedId) { setBalance(null); setAccountType(''); }
        }} balance={balance} accountType={accountType} busy={pending}
          onLookup={handleLookup} onTransaction={handleTransaction}/>
        <BankOverview reloadKey={reloadKey}/>
        <BookingHistory accountId={loadedId} reloadKey={reloadKey}/>
      </div>
      <footer><span>newbank © 2026 · Lernprojekt</span><span>API: {import.meta.env.VITE_API_BASE_URL}</span></footer>
    </main>
  </div>;
}
