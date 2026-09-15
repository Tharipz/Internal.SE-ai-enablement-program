import { Button, Card, Space, Tag, Typography } from 'antd';
import { ShoppingCartOutlined } from '@ant-design/icons';
import { Link } from 'react-router-dom';

import type { ProductSummary } from '../api/types';
import { ProductStatusTag } from './ProductStatusTag';
import { ProductVisual } from './ProductVisual';
import { formatPrice } from './formatters';

interface ShopOfferCardProps {
  product: ProductSummary;
  onAdd: (product: ProductSummary) => void;
}

export function ShopOfferCard({ product, onAdd }: ShopOfferCardProps) {
  const unavailable = product.status !== 'ACTIVE' || product.price === undefined || product.price === null;

  return (
    <Card className="shop-card" cover={<ProductVisual product={product} />}>
      <Space direction="vertical" size="small" className="shop-card__content">
        <Space wrap>
          <Tag>{product.category}</Tag>
          <ProductStatusTag status={product.status} />
        </Space>
        <Typography.Title level={4}>{product.name}</Typography.Title>
        <Typography.Paragraph type="secondary" ellipsis={{ rows: 2 }}>
          {product.shortDescription || product.code}
        </Typography.Paragraph>
        <Typography.Title level={3} className="price-line">
          {formatPrice(product.price, product.currency, product.period)}
        </Typography.Title>
        <Space>
          <Link to={`/shop/${product.id}`}>
            <Button type="primary">{product.type.includes('PLAN') ? 'Select plan' : 'View offer'}</Button>
          </Link>
          <Button
            icon={<ShoppingCartOutlined />}
            disabled={unavailable}
            onClick={() => onAdd(product)}
          >
            Add
          </Button>
        </Space>
      </Space>
    </Card>
  );
}
