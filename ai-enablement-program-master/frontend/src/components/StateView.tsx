import { Empty, Result, Spin } from 'antd';

interface StateViewProps {
  type?: 'loading' | 'empty' | 'error' | 'notFound';
  title: string;
  description?: string;
}

export function StateView({ type = 'empty', title, description }: StateViewProps) {
  if (type === 'loading') {
    return (
      <div className="state-view">
        <Spin size="large" />
        <div>{title}</div>
      </div>
    );
  }

  if (type === 'error' || type === 'notFound') {
    return <Result status={type === 'notFound' ? '404' : 'warning'} title={title} subTitle={description} />;
  }

  return <Empty className="state-view" description={description || title} />;
}
