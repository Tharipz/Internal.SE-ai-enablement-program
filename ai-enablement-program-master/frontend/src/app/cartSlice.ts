import { createSlice, PayloadAction } from '@reduxjs/toolkit';

import type { ProductSummary } from '../api/types';

export interface CartItem {
  id: string;
  name: string;
  code: string;
  type: string;
  price?: number;
  currency?: string;
  period?: string;
  quantity: number;
}

interface CartState {
  items: CartItem[];
}

const initialState: CartState = {
  items: []
};

const toCartItem = (product: ProductSummary): CartItem => ({
  id: product.id,
  name: product.name,
  code: product.code,
  type: product.type,
  price: product.price,
  currency: product.currency,
  period: product.period,
  quantity: 1
});

const cartSlice = createSlice({
  name: 'cart',
  initialState,
  reducers: {
    addToCart(state, action: PayloadAction<ProductSummary>) {
      const existing = state.items.find((item) => item.id === action.payload.id);
      if (existing) {
        existing.quantity += 1;
      } else {
        state.items.push(toCartItem(action.payload));
      }
    },
    removeFromCart(state, action: PayloadAction<string>) {
      state.items = state.items.filter((item) => item.id !== action.payload);
    },
    clearCart(state) {
      state.items = [];
    }
  }
});

export const { addToCart, removeFromCart, clearCart } = cartSlice.actions;
export const cartReducer = cartSlice.reducer;
