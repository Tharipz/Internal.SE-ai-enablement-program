import { Button, List, Space, Typography } from 'antd';
import { DeleteOutlined } from '@ant-design/icons';
import { useDispatch, useSelector } from 'react-redux';
import { Link } from 'react-router-dom';

import { removeFromCart } from '../app/cartSlice';
import type { RootState } from '../app/store';
import { formatPrice } from './formatters';

export function CartSummary() {
  const dispatch = useDispatch();
  const items = useSelector((state: RootState) => state.cart.items);

  const total = items.reduce((sum, item) => sum + (item.price || 0) * item.quantity, 0);

  return (
    <div className="cart-summary">
      <Typography.Title level={4}>Order summary</Typography.Title>
      <List
        locale={{ emptyText: 'Cart is empty' }}
        dataSource={items}
        renderItem={(item) => (
          <List.Item
            actions={[
              <Button
                key="remove"
                icon={<DeleteOutlined />}
                onClick={() => dispatch(removeFromCart(item.id))}
              />
            ]}
          >
            <List.Item.Meta
              title={item.name}
              description={`${item.quantity} x ${formatPrice(item.price, item.currency, item.period)}`}
            />
          </List.Item>
        )}
      />
      <Space direction="vertical" className="cart-summary__footer">
        <Typography.Title level={3}>{formatPrice(total, 'USD')}</Typography.Title>
        <Link to="/checkout">
          <Button type="primary" block disabled={items.length === 0}>
            Continue checkout
          </Button>
        </Link>
      </Space>
    </div>
  );
}
