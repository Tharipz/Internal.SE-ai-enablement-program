import { Alert, Button, Form, Input, Select, Space, Typography } from 'antd';
import { CheckCircleOutlined } from '@ant-design/icons';
import { useDispatch, useSelector } from 'react-redux';

import { clearCart } from '../app/cartSlice';
import type { RootState } from '../app/store';
import { CartSummary } from '../components/CartSummary';
import { PageShell } from '../components/PageShell';

export function CheckoutPage() {
  const dispatch = useDispatch();
  const items = useSelector((state: RootState) => state.cart.items);

  return (
    <PageShell title="Checkout" subtitle="Demo purchase flow, no real order is submitted">
      <div className="checkout-layout">
        <section>
          <Alert
            type="warning"
            showIcon
            message="Training checkout only"
            description="This flow validates UX and API data usage. It does not create a real customer, payment or order."
          />
          <Typography.Title level={4}>Customer details</Typography.Title>
          <Form layout="vertical" className="checkout-form">
            <Form.Item label="Full name" required>
              <Input placeholder="Demo Customer" />
            </Form.Item>
            <Form.Item label="Mobile number" required>
              <Input placeholder="+1 555 0100" />
            </Form.Item>
            <Form.Item label="Delivery or installation address">
              <Input.TextArea rows={3} placeholder="Street, city, building" />
            </Form.Item>
            <Form.Item label="SIM preference">
              <Select
                placeholder="Select option"
                options={[
                  { label: 'eSIM', value: 'esim' },
                  { label: 'Physical SIM', value: 'sim' },
                  { label: 'Not applicable', value: 'none' }
                ]}
              />
            </Form.Item>
            <Space>
              <Button
                type="primary"
                icon={<CheckCircleOutlined />}
                disabled={items.length === 0}
                onClick={() => dispatch(clearCart())}
              >
                Place demo order
              </Button>
            </Space>
          </Form>
        </section>
        <aside>
          <CartSummary />
        </aside>
      </div>
    </PageShell>
  );
}
