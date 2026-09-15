import { createBrowserRouter, Navigate } from 'react-router-dom';

import { CatalogPage } from '../pages/CatalogPage';
import { CheckoutPage } from '../pages/CheckoutPage';
import { ProductDetailsPage } from '../pages/ProductDetailsPage';
import { ShopPage } from '../pages/ShopPage';
import { ShopProductPage } from '../pages/ShopProductPage';

export const router = createBrowserRouter([
  {
    path: '/',
    element: <Navigate to="/shop" replace />
  },
  {
    path: '/shop',
    element: <ShopPage />
  },
  {
    path: '/shop/:id',
    element: <ShopProductPage />
  },
  {
    path: '/checkout',
    element: <CheckoutPage />
  },
  {
    path: '/catalog',
    element: <CatalogPage />
  },
  {
    path: '/catalog/:id',
    element: <ProductDetailsPage />
  }
]);
