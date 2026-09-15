import { Alert, Descriptions, Tag } from 'antd';
import dayjs from 'dayjs';

import type { CatalogMetadata as CatalogMetadataModel } from '../api/types';

interface CatalogMetadataProps {
  metadata?: CatalogMetadataModel;
}

export function CatalogMetadata({ metadata }: CatalogMetadataProps) {
  if (!metadata) {
    return null;
  }

  return (
    <div className="metadata-panel">
      {metadata.warningCount > 0 && (
        <Alert
          type="warning"
          showIcon
          message={`${metadata.warningCount} catalog validation warning(s) detected during import`}
        />
      )}
      <Descriptions size="small" column={1}>
        <Descriptions.Item label="Imported">
          {dayjs(metadata.importedAt).format('YYYY-MM-DD HH:mm')}
        </Descriptions.Item>
        <Descriptions.Item label="Products">{metadata.productCount}</Descriptions.Item>
        <Descriptions.Item label="Source">{metadata.sourceFile}</Descriptions.Item>
        <Descriptions.Item label="Currency">
          {(metadata.availableFilters.currency || []).map((value) => (
            <Tag key={value}>{value}</Tag>
          ))}
        </Descriptions.Item>
        <Descriptions.Item label="Sheets">
          {metadata.sourceSheets.map((sheet) => (
            <Tag key={sheet}>{sheet}</Tag>
          ))}
        </Descriptions.Item>
      </Descriptions>
    </div>
  );
}
