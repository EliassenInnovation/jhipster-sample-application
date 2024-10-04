import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ProductH7New from './product-h-7-new';
import ProductH7NewDetail from './product-h-7-new-detail';
import ProductH7NewUpdate from './product-h-7-new-update';
import ProductH7NewDeleteDialog from './product-h-7-new-delete-dialog';

const ProductH7NewRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ProductH7New />} />
    <Route path="new" element={<ProductH7NewUpdate />} />
    <Route path=":id">
      <Route index element={<ProductH7NewDetail />} />
      <Route path="edit" element={<ProductH7NewUpdate />} />
      <Route path="delete" element={<ProductH7NewDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ProductH7NewRoutes;
