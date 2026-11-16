import { useCallback, useState } from 'react';
import { createAccount, getAccount, getBalance, deposit, withdraw } from '../api/bankApi.js';
import AccountCreation from '../components/AccountCreation.jsx';
import AccountOperations from '../components/AccountOperations.jsx';
import BankOverview from '../components/BankOverview.jsx';
import BookingHistory from '../components/BookingHistory.jsx';
import { Notice } from '../components/Common.jsx';

export default function Dashboard() {
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
