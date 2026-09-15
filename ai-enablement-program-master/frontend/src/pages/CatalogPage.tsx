import { useMemo, useState } from 'react';
import { Alert, Typography } from 'antd';
import { useTranslation } from 'react-i18next';

import type { ProductQuery } from '../api/types';
import { useGetMetadataQuery, useGetProductsQuery } from '../api/catalogApi';
import { CatalogFilters } from '../components/CatalogFilters';
import { CatalogMetadata } from '../components/CatalogMetadata';
import { PageShell } from '../components/PageShell';
import { ProductTable } from '../components/ProductTable';
import { StateView } from '../components/StateView';

export function CatalogPage() {
  const { t: translate } = useTranslation();
  const t = translate as unknown as (key: string) => string;
  const [query, setQuery] = useState<ProductQuery>({ page: 0, pageSize: 10 });
  const productQuery = useMemo(() => query, [query]);
  const products = useGetProductsQuery(productQuery);
  const metadata = useGetMetadataQuery();

  const backendUnavailable = products.isError || metadata.isError;

  return (
    <PageShell title={t('catalog')} subtitle="Excel-driven demo catalog for AI Enablement training">
      {backendUnavailable && (
        <StateView
          type="error"
          title={t('backendUnavailable')}
          description="Start the Quarkus backend on port 8080 and retry the catalog request."
        />
      )}
      {!backendUnavailable && (
        <>
          <CatalogMetadata metadata={metadata.data} />
          <CatalogFilters
            query={query}
            filters={metadata.data?.availableFilters}
            onChange={setQuery}
          />
          {products.isLoading && <StateView type="loading" title="Loading catalog" />}
          {products.data && products.data.total === 0 && (
            <StateView title={t('empty')} description="Try another search term or remove filters." />
          )}
          {products.data && products.data.total > 0 && (
            <>
              <div className="section-heading">
                <Typography.Title level={4}>{t('products')}</Typography.Title>
                <Typography.Text type="secondary">
                  {products.data.total} item(s), sourced from Excel rows
                </Typography.Text>
              </div>
              {products.data.items.some((item) => item.technical) && (
                <Alert
                  type="info"
                  showIcon
                  message="Technical offerings are visible in this demo so learners can inspect catalog edge cases."
                />
              )}
              <ProductTable
                products={products.data.items}
                loading={products.isFetching}
                page={products.data.page}
                pageSize={products.data.pageSize}
                total={products.data.total}
                onPageChange={(page, pageSize) => setQuery((current) => ({ ...current, page, pageSize }))}
              />
            </>
          )}
        </>
      )}
    </PageShell>
  );
}
