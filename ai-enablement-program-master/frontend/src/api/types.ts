export interface ProductSummary {
  id: string;
  code: string;
  name: string;
  category: string;
  type: string;
  status: string;
  price?: number;
  currency?: string;
  period?: string;
  shortDescription: string;
  technical: boolean;
  sourceSheet: string;
  sourceRow: number;
}

export interface ProductDetail extends ProductSummary {
  description?: string;
  availableFrom?: string;
  availableTo?: string;
  pricingOptions: PriceOption[];
  parameters: ProductParameter[];
  conditions: string[];
  dependencies: ProductDependency[];
  sourceFile: string;
  importedAt: string;
  validationIssues: ValidationIssue[];
}

export interface PriceOption {
  id: string;
  component: string;
  saleType: string;
  value: number;
  currency: string;
  defaultPrice: boolean;
  basePrice: boolean;
  period?: string;
  validFrom?: string;
  validTo?: string;
}

export interface ProductParameter {
  name: string;
  values: string[];
  visible: boolean;
  mandatory: boolean;
  modifiable: boolean;
}

export interface ProductDependency {
  type: string;
  targetId?: string;
  targetName: string;
  min?: number;
  max?: number;
  sourceSheet: string;
  sourceRow: number;
}

export interface ValidationIssue {
  severity: string;
  code: string;
  message: string;
  sourceSheet: string;
  sourceRow: number;
  field?: string;
}

export interface CatalogCategory {
  id: string;
  name: string;
  parent?: string;
  productCount: number;
}

export interface CatalogMetadata {
  operatorName: string;
  sourceFile: string;
  importedAt: string;
  productCount: number;
  warningCount: number;
  sourceSheets: string[];
  availableFilters: Record<string, string[]>;
}

export interface ProductPage {
  items: ProductSummary[];
  page: number;
  pageSize: number;
  total: number;
}

export interface ProductQuery {
  search?: string;
  category?: string;
  status?: string;
  type?: string;
  priceFrom?: number;
  priceTo?: number;
  page?: number;
  pageSize?: number;
}
