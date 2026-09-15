import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';

import type { CatalogCategory, CatalogMetadata, ProductDetail, ProductPage, ProductQuery } from './types';

const toSearchParams = (query: ProductQuery) => {
  const params = new URLSearchParams();
  Object.entries(query).forEach(([key, value]) => {
    if (value !== undefined && value !== null && value !== '') {
      params.set(key, String(value));
    }
  });
  return params.toString();
};

export const catalogApi = createApi({
  reducerPath: 'catalogApi',
  baseQuery: fetchBaseQuery({ baseUrl: '/api/catalog' }),
  endpoints: (builder) => ({
    getProducts: builder.query<ProductPage, ProductQuery>({
      query: (query) => {
        const search = toSearchParams(query);
        return `products${search ? `?${search}` : ''}`;
      }
    }),
    getProduct: builder.query<ProductDetail, string>({
      query: (id) => `products/${encodeURIComponent(id)}`
    }),
    getCategories: builder.query<CatalogCategory[], void>({
      query: () => 'categories'
    }),
    getMetadata: builder.query<CatalogMetadata, void>({
      query: () => 'metadata'
    })
  })
});

export const {
  useGetProductsQuery,
  useGetProductQuery,
  useGetCategoriesQuery,
  useGetMetadataQuery
} = catalogApi;
