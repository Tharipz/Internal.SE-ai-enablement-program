import { Button, Descriptions, List, Space, Tag, Typography } from 'antd';
import { ShoppingCartOutlined } from '@ant-design/icons';
import { Link, useParams } from 'react-router-dom';
import { useDispatch } from 'react-redux';

import { useGetProductQuery } from '../api/catalogApi';
import { addToCart } from '../app/cartSlice';
import { PageShell } from '../components/PageShell';
import { ProductStatusTag } from '../components/ProductStatusTag';
import { ProductVisual } from '../components/ProductVisual';
import { StateView } from '../components/StateView';
import { formatPrice } from '../components/formatters';

export function ShopProductPage() {
  const { id } = useParams();
  const dispatch = useDispatch();
  const product = useGetProductQuery(id || '', { skip: !id });

  if (product.isLoading) {
    return <PageShell title="Loading offer"><StateView type="loading" title="Loading offer" /></PageShell>;
  }

  if (product.isError || !product.data || product.data.technical) {
    return <PageShell title="Offer not found"><StateView type="notFound" title="Offer not found" /></PageShell>;
  }

  const item = product.data;
  const unavailable = item.status !== 'ACTIVE' || item.price === undefined || item.price === null;

  return (
    <PageShell title={item.name} subtitle={item.category}>
      <div className="offer-detail">
        <ProductVisual product={item} />
        <section>
          <Space wrap>
            <Tag>{item.type}</Tag>
            <ProductStatusTag status={item.status} />
          </Space>
          <Typography.Title>{formatPrice(item.price, item.currency, item.period)}</Typography.Title>
          <Typography.Paragraph>{item.description || item.shortDescription}</Typography.Paragraph>
          <Space>
            <Button
              type="primary"
              size="large"
              icon={<ShoppingCartOutlined />}
              disabled={unavailable}
              onClick={() => dispatch(addToCart(item))}
            >
              Add to cart
            </Button>
            <Link to="/checkout">
              <Button size="large" disabled={unavailable}>Checkout</Button>
            </Link>
          </Space>
        </section>
      </div>

      <Descriptions bordered column={{ xs: 1, md: 2 }} className="offer-specs">
        <Descriptions.Item label="Code">{item.code}</Descriptions.Item>
        <Descriptions.Item label="Availability">{item.status}</Descriptions.Item>
        <Descriptions.Item label="Source">{item.sourceSheet} row {item.sourceRow}</Descriptions.Item>
        <Descriptions.Item label="Imported">{item.importedAt}</Descriptions.Item>
      </Descriptions>

      <section>
        <Typography.Title level={4}>Plan and device details</Typography.Title>
        <List
          dataSource={item.parameters}
          renderItem={(parameter) => (
            <List.Item>
              <List.Item.Meta
                title={parameter.name}
                description={parameter.values.join(', ')}
              />
            </List.Item>
          )}
        />
      </section>
    </PageShell>
  );
}
