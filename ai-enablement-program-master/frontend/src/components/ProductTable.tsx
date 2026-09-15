import { Button, Space, Table, Tag, Typography } from 'antd';
import { EyeOutlined } from '@ant-design/icons';
import type { ColumnsType } from 'antd/es/table';
import { Link } from 'react-router-dom';

import type { ProductSummary } from '../api/types';
import { ProductStatusTag } from './ProductStatusTag';
import { formatPrice } from './formatters';

interface ProductTableProps {
  products: ProductSummary[];
  loading: boolean;
  page: number;
  pageSize: number;
  total: number;
  onPageChange: (page: number, pageSize: number) => void;
}

export function ProductTable({ products, loading, page, pageSize, total, onPageChange }: ProductTableProps) {
  const columns: ColumnsType<ProductSummary> = [
    {
      title: 'Product',
      dataIndex: 'name',
      key: 'name',
      render: (_, record) => (
        <Space direction="vertical" size={2}>
          <Link to={`/catalog/${record.id}`}>
            <Typography.Text strong>{record.name}</Typography.Text>
          </Link>
          <Typography.Text type="secondary">{record.code}</Typography.Text>
        </Space>
      )
    },
    {
      title: 'Category',
      dataIndex: 'category',
      key: 'category',
      render: (value: string, record) => (
        <Space>
          <Tag>{value}</Tag>
          {record.technical && <Tag color="gold">TECHNICAL</Tag>}
        </Space>
      )
    },
    {
      title: 'Type',
      dataIndex: 'type',
      key: 'type',
      responsive: ['md']
    },
    {
      title: 'Status',
      dataIndex: 'status',
      key: 'status',
      render: (value: string) => <ProductStatusTag status={value} />
    },
    {
      title: 'Price',
      key: 'price',
      render: (_, record) => formatPrice(record.price, record.currency, record.period)
    },
    {
      title: 'Source',
      key: 'source',
      responsive: ['lg'],
      render: (_, record) => `${record.sourceSheet} #${record.sourceRow}`
    },
    {
      title: '',
      key: 'action',
      render: (_, record) => (
        <Button icon={<EyeOutlined />} href={`/catalog/${record.id}`}>
          Open
        </Button>
      )
    }
  ];

  return (
    <Table
      rowKey="id"
      columns={columns}
      dataSource={products}
      loading={loading}
      pagination={{
        current: page + 1,
        pageSize,
        total,
        showSizeChanger: true,
        pageSizeOptions: [5, 10, 20, 50],
        onChange: (nextPage, nextPageSize) => onPageChange(nextPage - 1, nextPageSize)
      }}
    />
  );
}
