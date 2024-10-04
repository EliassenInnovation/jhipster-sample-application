import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ProductActivityHistory from './product-activity-history';
import ProductActivityHistoryDetail from './product-activity-history-detail';
import ProductActivityHistoryUpdate from './product-activity-history-update';
import ProductActivityHistoryDeleteDialog from './product-activity-history-delete-dialog';

const ProductActivityHistoryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ProductActivityHistory />} />
    <Route path="new" element={<ProductActivityHistoryUpdate />} />
    <Route path=":id">
      <Route index element={<ProductActivityHistoryDetail />} />
      <Route path="edit" element={<ProductActivityHistoryUpdate />} />
      <Route path="delete" element={<ProductActivityHistoryDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ProductActivityHistoryRoutes;
