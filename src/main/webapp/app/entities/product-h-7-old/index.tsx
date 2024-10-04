import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ProductH7Old from './product-h-7-old';
import ProductH7OldDetail from './product-h-7-old-detail';
import ProductH7OldUpdate from './product-h-7-old-update';
import ProductH7OldDeleteDialog from './product-h-7-old-delete-dialog';

const ProductH7OldRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ProductH7Old />} />
    <Route path="new" element={<ProductH7OldUpdate />} />
    <Route path=":id">
      <Route index element={<ProductH7OldDetail />} />
      <Route path="edit" element={<ProductH7OldUpdate />} />
      <Route path="delete" element={<ProductH7OldDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ProductH7OldRoutes;
