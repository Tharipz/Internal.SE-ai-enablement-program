import { Link, useParams } from 'react-router-dom';
import { Alert, Button, Descriptions, Divider, List, Space, Table, Tag, Typography } from 'antd';
import { ArrowLeftOutlined } from '@ant-design/icons';
import { useTranslation } from 'react-i18next';

import { useGetProductQuery } from '../api/catalogApi';
import { PageShell } from '../components/PageShell';
import { ProductStatusTag } from '../components/ProductStatusTag';
import { StateView } from '../components/StateView';
import { formatPrice } from '../components/formatters';

export function ProductDetailsPage() {
  const { t: translate } = useTranslation();
  const t = translate as unknown as (key: string) => string;
  const { id } = useParams();
  const product = useGetProductQuery(id || '', { skip: !id });

  if (product.isLoading) {
    return (
      <PageShell title="Loading product">
        <StateView type="loading" title="Loading product" />
      </PageShell>
    );
  }

  if (product.isError || !product.data) {
    return (
      <PageShell title={t('notFound')} extra={<BackButton />}>
        <StateView type="notFound" title={t('notFound')} description="The requested product id is not present in catalog.json." />
      </PageShell>
    );
  }

  const item = product.data;

  return (
    <PageShell title={item.name} subtitle={`${item.code} · ${item.category}`} extra={<BackButton />}>
      <Space direction="vertical" size="large" className="details-layout">
        {item.validationIssues.length > 0 && (
          <Alert
            type="warning"
            showIcon
            message={`${item.validationIssues.length} validation issue(s) found for this product`}
          />
        )}
        <Descriptions bordered column={{ xs: 1, sm: 2, lg: 3 }}>
          <Descriptions.Item label="Status"><ProductStatusTag status={item.status} /></Descriptions.Item>
          <Descriptions.Item label="Type">{item.type}</Descriptions.Item>
          <Descriptions.Item label="Price">{formatPrice(item.price, item.currency, item.period)}</Descriptions.Item>
          <Descriptions.Item label="Available from">{item.availableFrom || 'n/a'}</Descriptions.Item>
          <Descriptions.Item label="Available to">{item.availableTo || 'open'}</Descriptions.Item>
          <Descriptions.Item label="Technical">{item.technical ? 'Yes' : 'No'}</Descriptions.Item>
          <Descriptions.Item label={t('source')} span={3}>
            {item.sourceFile} · {item.sourceSheet} row {item.sourceRow}
          </Descriptions.Item>
        </Descriptions>

        <section>
          <Typography.Title level={4}>Description</Typography.Title>
          <Typography.Paragraph>{item.description || 'No description provided.'}</Typography.Paragraph>
        </section>

        <section>
          <Typography.Title level={4}>{t('pricing')}</Typography.Title>
          <Table
            rowKey="id"
            size="small"
            pagination={false}
            dataSource={item.pricingOptions}
            columns={[
              { title: 'Sale type', dataIndex: 'saleType' },
              { title: 'Component', dataIndex: 'component' },
              { title: 'Value', render: (_, record) => formatPrice(record.value, record.currency, record.period) },
              { title: 'Default', render: (_, record) => (record.defaultPrice ? 'Yes' : 'No') },
              { title: 'Valid from', dataIndex: 'validFrom' }
            ]}
          />
        </section>

        <section>
          <Typography.Title level={4}>{t('parameters')}</Typography.Title>
          <List
            dataSource={item.parameters}
            renderItem={(parameter) => (
              <List.Item>
                <List.Item.Meta
                  title={parameter.name}
                  description={
                    <Space wrap>
                      {parameter.values.map((value) => <Tag key={`${parameter.name}-${value}`}>{value}</Tag>)}
                      {parameter.mandatory && <Tag color="red">mandatory</Tag>}
                      {parameter.modifiable && <Tag color="blue">modifiable</Tag>}
                    </Space>
                  }
                />
              </List.Item>
            )}
          />
        </section>

        {item.conditions.length > 0 && (
          <section>
            <Typography.Title level={4}>{t('conditions')}</Typography.Title>
            <List dataSource={item.conditions} renderItem={(condition) => <List.Item>{condition}</List.Item>} />
          </section>
        )}

        {item.dependencies.length > 0 && (
          <section>
            <Typography.Title level={4}>{t('dependencies')}</Typography.Title>
            <List
              dataSource={item.dependencies}
              renderItem={(dependency) => (
                <List.Item>
                  <Space wrap>
                    <Tag color="purple">{dependency.type}</Tag>
                    <span>{dependency.targetName}</span>
                    <Typography.Text type="secondary">
                      {dependency.sourceSheet} row {dependency.sourceRow}
                    </Typography.Text>
                  </Space>
                </List.Item>
              )}
            />
          </section>
        )}

        {item.validationIssues.length > 0 && (
          <section>
            <Divider />
            <Typography.Title level={4}>{t('validation')}</Typography.Title>
            <List
              dataSource={item.validationIssues}
              renderItem={(issue) => (
                <List.Item>
                  <Space direction="vertical" size={2}>
                    <Space>
                      <Tag color={issue.severity === 'ERROR' ? 'red' : 'gold'}>{issue.severity}</Tag>
                      <Typography.Text strong>{issue.code}</Typography.Text>
                    </Space>
                    <Typography.Text>{issue.message}</Typography.Text>
                    <Typography.Text type="secondary">
                      {issue.sourceSheet} row {issue.sourceRow} {issue.field ? `· ${issue.field}` : ''}
                    </Typography.Text>
                  </Space>
                </List.Item>
              )}
            />
          </section>
        )}
      </Space>
    </PageShell>
  );
}

function BackButton() {
  const { t: translate } = useTranslation();
  const t = translate as unknown as (key: string) => string;
  return (
    <Link to="/catalog">
      <Button icon={<ArrowLeftOutlined />}>{t('back')}</Button>
    </Link>
  );
}
