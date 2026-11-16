export function Notice({ type, children, onClose }) {
  if (!children) return null;
  return <div role="alert" className={`notice notice--${type}`}>
    <span aria-hidden="true">{type === 'error' ? '!' : '✓'}</span>
    <span>{children}</span>
    {onClose && <button className="notice__close" onClick={onClose} aria-label="Meldung schliessen">×</button>}
  </div>;
}

export function SectionTitle({ kicker, title, subtitle }) {
  return <div className="section-heading">
    <span className="eyebrow">{kicker}</span><h2>{title}</h2>
    {subtitle && <p>{subtitle}</p>}
  </div>;
}
