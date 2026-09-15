import { Badge, Button, Layout, Space, Typography } from 'antd';
import { ShoppingCartOutlined } from '@ant-design/icons';
import type { ReactNode } from 'react';
import { Link } from 'react-router-dom';
import { useSelector } from 'react-redux';

import type { RootState } from '../app/store';

interface PageShellProps {
  title: string;
  subtitle?: string;
  extra?: ReactNode;
  children: ReactNode;
}

export function PageShell({ title, subtitle, extra, children }: PageShellProps) {
  const cartCount = useSelector((state: RootState) => state.cart.items.reduce((sum, item) => sum + item.quantity, 0));

  return (
    <Layout className="app-shell">
      <Layout.Header className="app-header">
        <div>
          <Typography.Title level={3}>Netcracker Telekom</Typography.Title>
          <Typography.Text>AI Enablement Catalog Portal</Typography.Text>
        </div>
        <Space>
          <Link to="/shop">Shop</Link>
          <Link to="/catalog">Catalog</Link>
          <Link to="/checkout">
            <Badge count={cartCount} size="small">
              <Button icon={<ShoppingCartOutlined />}>Cart</Button>
            </Badge>
          </Link>
        </Space>
      </Layout.Header>
      <Layout.Content className="app-content">
        <div className="page-title-row">
          <div>
            <Typography.Title level={2}>{title}</Typography.Title>
            {subtitle && <Typography.Text type="secondary">{subtitle}</Typography.Text>}
          </div>
          {extra}
        </div>
        {children}
      </Layout.Content>
    </Layout>
  );
}
