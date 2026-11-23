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

  it('verbietet den Einzahlungsbetrag null', () => {
    expect(() => francsToInternal('0')).toThrow('grösser als 0');
  });
  it('erlaubt eine Kreditlimite von null', () => {
    expect(francsToInternal('0', { allowNegative: true })).toBe(0);
  });
  it('verhindert eine positive Kreditlimite', () => {
    expect(() => francsToInternal('20', { allowNegative: true })).toThrow();
  });
  it('verhindert zu grosse Zahlen ausserhalb der sicheren Ganzzahl', () => {
    expect(() => francsToInternal('999999999999')).toThrow('zu gross');
  });
  it('zeigt negative Salden als CHF an', () => {
    expect(formatCHF(-30000000)).toContain('300.00');
  });
});
