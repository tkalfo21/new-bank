import { describe, it, expect } from 'vitest';
import { formatCHF, francsToInternal } from './money.js';

describe('Geldbeträge und Bank-API-Einheiten', () => {
  it('wandelt CHF 1 in 100000 Millirappen um', () => {
    expect(francsToInternal('1')).toBe(100000);
  });
  it('wandelt CHF mit Rappen um', () => {
    expect(francsToInternal('19,95')).toBe(1995000);
  });
  it('erlaubt negative Kreditlimiten', () => {
    expect(francsToInternal('-500', { allowNegative: true })).toBe(-50000000);
  });
  it('verhindert negative Einzahlungen', () => {
    expect(() => francsToInternal('-2')).toThrow();
  });
  it('verhindert mehr als zwei Dezimalstellen', () => {
    expect(() => francsToInternal('2.999')).toThrow();
  });
  it('formatiert Rückgabebeträge als CHF', () => {
    expect(formatCHF(100000)).toContain('1.00');
  });
});
