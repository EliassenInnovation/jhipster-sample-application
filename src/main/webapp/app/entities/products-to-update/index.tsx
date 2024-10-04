import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ProductsToUpdate from './products-to-update';
import ProductsToUpdateDetail from './products-to-update-detail';
import ProductsToUpdateUpdate from './products-to-update-update';
import ProductsToUpdateDeleteDialog from './products-to-update-delete-dialog';

const ProductsToUpdateRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ProductsToUpdate />} />
    <Route path="new" element={<ProductsToUpdateUpdate />} />
    <Route path=":id">
      <Route index element={<ProductsToUpdateDetail />} />
      <Route path="edit" element={<ProductsToUpdateUpdate />} />
      <Route path="delete" element={<ProductsToUpdateDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ProductsToUpdateRoutes;
