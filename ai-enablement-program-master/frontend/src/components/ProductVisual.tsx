import type { ProductSummary } from '../api/types';

const labelByType: Record<string, string> = {
  SMARTPHONE: '5G',
  TABLET: 'TAB',
  SMARTWATCH: 'LTE',
  ROUTER: 'Wi-Fi',
  POSTPAID_PLAN: 'SIM',
  FIBER_PLAN: 'Fiber',
  ACCESSORY: 'Gear',
  SERVICE: 'eSIM'
};

export function ProductVisual({ product }: { product: ProductSummary }) {
  const typeClass = product.type.toLowerCase().replace(/_/g, '-');
  return (
    <div className={`product-visual ${typeClass}`}>
      <div className="product-visual__device">
        <span>{labelByType[product.type] || 'NT'}</span>
      </div>
    </div>
  );
}
