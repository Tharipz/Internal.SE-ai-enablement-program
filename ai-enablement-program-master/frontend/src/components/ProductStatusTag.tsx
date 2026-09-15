import { Tag } from 'antd';

const statusColor: Record<string, string> = {
  ACTIVE: 'green',
  FUTURE: 'blue',
  EXPIRED: 'default',
  ELIMINATED: 'orange',
  ARCHIVED: 'red',
  INVALID: 'volcano'
};

export function ProductStatusTag({ status }: { status: string }) {
  return <Tag color={statusColor[status] || 'default'}>{status}</Tag>;
}
