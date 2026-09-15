import { useMemo, useState } from 'react';
import { Alert, Button, Segmented, Space, Typography } from 'antd';
import { useDispatch } from 'react-redux';

import { useGetMetadataQuery, useGetProductsQuery } from '../api/catalogApi';
import type { ProductSummary } from '../api/types';
import { addToCart } from '../app/cartSlice';
import { CartSummary } from '../components/CartSummary';
import { PageShell } from '../components/PageShell';
import { ShopOfferCard } from '../components/ShopOfferCard';
import { StateView } from '../components/StateView';

const segmentToTypes: Record<string, string[]> = {
  Featured: ['POSTPAID_PLAN', 'FIBER_PLAN', 'SMARTPHONE', 'ROUTER'],
  'Mobile plans': ['POSTPAID_PLAN'],
  Fiber: ['FIBER_PLAN'],
  Devices: ['SMARTPHONE', 'TABLET', 'SMARTWATCH', 'ROUTER'],
  Accessories: ['ACCESSORY', 'SERVICE']
};

export function ShopPage() {
  const dispatch = useDispatch();
  const [segment, setSegment] = useState<string>('Featured');
  const products = useGetProductsQuery({ page: 0, pageSize: 100 });
  const metadata = useGetMetadataQuery();

  const visibleProducts = useMemo(() => {
    const allowed = segmentToTypes[segment] || [];
    return (products.data?.items || [])
      .filter((product) => !product.technical)
      .filter((product) => allowed.includes(product.type))
      .filter((product) => segment !== 'Featured' || product.status === 'ACTIVE');
  }, [products.data?.items, segment]);

  const add = (product: ProductSummary) => dispatch(addToCart(product));

  return (
    <PageShell title="Shop" subtitle="Buy mobile plans, fiber home internet and devices">
      <section className="shop-hero">
        <div className="shop-hero__copy">
          <Typography.Title>Choose your next connection</Typography.Title>
          <Typography.Paragraph>
            Shop postpaid mobile plans, fiber home internet and connected devices powered by the Excel catalog.
          </Typography.Paragraph>
          <Space wrap>
            <Button type="primary" size="large" onClick={() => setSegment('Mobile plans')}>Mobile plans</Button>
            <Button size="large" onClick={() => setSegment('Fiber')}>Fiber internet</Button>
            <Button size="large" onClick={() => setSegment('Devices')}>Devices</Button>
          </Space>
        </div>
        <div className="shop-hero__visual">
          <span>5G</span>
          <span>Fiber</span>
          <span>eSIM</span>
        </div>
      </section>

      {metadata.data && (
        <Alert
          type="info"
          showIcon
          message={`${metadata.data.productCount} catalog offers loaded from ${metadata.data.sourceFile}`}
        />
      )}

      {products.isLoading && <StateView type="loading" title="Loading shop offers" />}
      {products.isError && (
        <StateView type="error" title="Backend unavailable" description="Start Quarkus backend on port 8080." />
      )}
      {products.data && (
        <div className="shop-layout">
          <main>
            <div className="shop-toolbar">
              <Segmented
                value={segment}
                onChange={(value) => setSegment(String(value))}
                options={Object.keys(segmentToTypes)}
              />
              <Typography.Text type="secondary">{visibleProducts.length} offer(s)</Typography.Text>
            </div>
            <div className="shop-grid">
              {visibleProducts.map((product) => (
                <ShopOfferCard key={product.id} product={product} onAdd={add} />
              ))}
            </div>
          </main>
          <aside>
            <CartSummary />
          </aside>
        </div>
      )}
    </PageShell>
  );
}
