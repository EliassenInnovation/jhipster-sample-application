import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SuggestedProducts from './suggested-products';
import SuggestedProductsDetail from './suggested-products-detail';
import SuggestedProductsUpdate from './suggested-products-update';
import SuggestedProductsDeleteDialog from './suggested-products-delete-dialog';

const SuggestedProductsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SuggestedProducts />} />
    <Route path="new" element={<SuggestedProductsUpdate />} />
    <Route path=":id">
      <Route index element={<SuggestedProductsDetail />} />
      <Route path="edit" element={<SuggestedProductsUpdate />} />
      <Route path="delete" element={<SuggestedProductsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SuggestedProductsRoutes;
