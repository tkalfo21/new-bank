import { useState } from 'react';
import { formatCHF, francsToInternal } from '../utils/money.js';
import { SectionTitle } from './Common.jsx';

const ACCOUNT_NAMES = {
  savings: 'Sparkonto',
  'promo-youth': 'Jugendsparkonto',
  salary: 'Lohnkonto',
  SavingsAccount: 'Sparkonto',
  PromoYouthSavingsAccount: 'Jugendsparkonto',
  SalaryAccount: 'Lohnkonto',
};
export default function AccountOperations({ selectedId, setSelectedId, balance, accountType, busy, onLookup, onTransaction }) {
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
