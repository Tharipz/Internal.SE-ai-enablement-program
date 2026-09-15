export function formatPrice(price?: number, currency?: string, period?: string) {
  if (price === undefined || price === null) {
    return 'No price';
  }
  const formatted = new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: currency || 'USD',
    maximumFractionDigits: price % 1 === 0 ? 0 : 2
  }).format(price);
  return period && period !== 'one-time' ? `${formatted} / ${period}` : formatted;
}
