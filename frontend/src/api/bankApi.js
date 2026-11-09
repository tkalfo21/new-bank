// Alle HTTP-Aufrufe liegen hier – nicht in React-Komponenten.
const BASE_URL = import.meta.env.VITE_API_BASE_URL;

if (!BASE_URL) {
  throw new Error('VITE_API_BASE_URL fehlt. Bitte frontend/.env konfigurieren.');
}

async function request(path, options = {}) {
  let response;
  try {
    response = await fetch(`${BASE_URL}${path}`, {
      ...options,
      headers: { 'Content-Type': 'application/json', ...options.headers },
    });
  } catch {
    throw new Error('Backend nicht erreichbar. Läuft Spring Boot auf Port 8080?');
  }

  const result = await response.json().catch(() => null);
  if (!response.ok) {
    throw new Error(result?.error || `HTTP-Fehler ${response.status}`);
  }
  return result;
}

const accountPath = (id) => `/accounts/${encodeURIComponent(id.trim())}`;

export function createAccount(type, creditLimit = 0) {
  const body = type === 'salary' ? { creditLimit } : undefined;
  return request(`/accounts/${type}`, {
    method: 'POST',
    ...(body ? { body: JSON.stringify(body) } : {}),
  });
}
export const getAccount = (id) => request(accountPath(id));
export const getBalance = (id) => request(`${accountPath(id)}/balance`);
export const getBookings = (id) => request(`${accountPath(id)}/bookings`);
export const getTotalBalance = () => request('/bank/balance');
export const getTop5 = (direction) => request(`/bank/top5/${direction}`);

export function deposit(id, amount, text = '') {
  return request(`${accountPath(id)}/deposit`, {
    method: 'POST',
    body: JSON.stringify({ amount, text }),
  });
}
export function withdraw(id, amount, text = '') {
  return request(`${accountPath(id)}/withdraw`, {
    method: 'POST',
    body: JSON.stringify({ amount, text }),
  });
}
