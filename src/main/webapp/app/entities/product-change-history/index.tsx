import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ProductChangeHistory from './product-change-history';
import ProductChangeHistoryDetail from './product-change-history-detail';
import ProductChangeHistoryUpdate from './product-change-history-update';
import ProductChangeHistoryDeleteDialog from './product-change-history-delete-dialog';

const ProductChangeHistoryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ProductChangeHistory />} />
    <Route path="new" element={<ProductChangeHistoryUpdate />} />
    <Route path=":id">
      <Route index element={<ProductChangeHistoryDetail />} />
      <Route path="edit" element={<ProductChangeHistoryUpdate />} />
      <Route path="delete" element={<ProductChangeHistoryDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ProductChangeHistoryRoutes;
