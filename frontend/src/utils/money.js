// Java-Backend speichert Beträge als ganze Millirappen: CHF 1 = 100000.
export const MILLIRAPPEN_PER_FRANC = 100000;
export function formatCHF(value) {
  return new Intl.NumberFormat('de-CH', {
    style: 'currency', currency: 'CHF', minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  }).format(Number(value) / MILLIRAPPEN_PER_FRANC);
}
export function francsToInternal(input, { allowNegative = false } = {}) {
  const text = String(input).trim().replace(',', '.');
  if (!/^-?\d+(?:\.\d{1,2})?$/.test(text)) {
    throw new Error('Bitte einen CHF-Betrag mit höchstens 2 Dezimalstellen eingeben.');
  }
  const value = Number(text);
  if (!Number.isFinite(value) || (!allowNegative && value <= 0) || (allowNegative && value > 0)) {
    throw new Error(allowNegative
      ? 'Die Kreditlimite muss 0 oder negativ sein.'
      : 'Der Betrag muss grösser als 0 sein.');
  }
  const internal = Math.round(value * MILLIRAPPEN_PER_FRANC);
  if (!Number.isSafeInteger(internal)) {
    throw new Error('Der Betrag ist zu gross.');
  }
  return internal;
}
