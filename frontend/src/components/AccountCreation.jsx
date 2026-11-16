import { useState } from 'react';
import { francsToInternal } from '../utils/money.js';
import { SectionTitle } from './Common.jsx';

export default function AccountCreation({ busy, onCreate }) {
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
