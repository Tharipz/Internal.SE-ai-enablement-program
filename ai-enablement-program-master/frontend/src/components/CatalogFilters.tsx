import { Button, Input, InputNumber, Select, Space } from 'antd';
import { ReloadOutlined } from '@ant-design/icons';
import { useTranslation } from 'react-i18next';

import type { ProductQuery } from '../api/types';

interface CatalogFiltersProps {
  query: ProductQuery;
  filters?: Record<string, string[]>;
  onChange: (query: ProductQuery) => void;
}

export function CatalogFilters({ query, filters, onChange }: CatalogFiltersProps) {
  const { t: translate } = useTranslation();
  const t = translate as unknown as (key: string) => string;
  const categories = filters?.category || [];
  const statuses = filters?.status || [];
  const types = filters?.type || [];

  return (
    <div className="filter-bar">
      <Input.Search
        allowClear
        placeholder={t('search')}
        value={query.search}
        onChange={(event) => onChange({ ...query, search: event.target.value, page: 0 })}
        onSearch={(value) => onChange({ ...query, search: value, page: 0 })}
      />
      <Select
        allowClear
        placeholder={t('category')}
        value={query.category}
        options={categories.map((value) => ({ label: value, value }))}
        onChange={(value) => onChange({ ...query, category: value, page: 0 })}
      />
      <Select
        allowClear
        placeholder={t('status')}
        value={query.status}
        options={statuses.map((value) => ({ label: value, value }))}
        onChange={(value) => onChange({ ...query, status: value, page: 0 })}
      />
      <Select
        allowClear
        placeholder={t('type')}
        value={query.type}
        options={types.map((value) => ({ label: value, value }))}
        onChange={(value) => onChange({ ...query, type: value, page: 0 })}
      />
      <Space.Compact>
        <InputNumber
          min={0}
          placeholder={t('priceFrom')}
          value={query.priceFrom}
          onChange={(value) => onChange({ ...query, priceFrom: typeof value === 'number' ? value : undefined, page: 0 })}
        />
        <InputNumber
          min={0}
          placeholder={t('priceTo')}
          value={query.priceTo}
          onChange={(value) => onChange({ ...query, priceTo: typeof value === 'number' ? value : undefined, page: 0 })}
        />
      </Space.Compact>
      <Button
        icon={<ReloadOutlined />}
        onClick={() => onChange({ page: 0, pageSize: query.pageSize || 10 })}
      >
        {t('reset')}
      </Button>
    </div>
  );
}
