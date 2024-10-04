import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ReplacedProducts from './replaced-products';
import ReplacedProductsDetail from './replaced-products-detail';
import ReplacedProductsUpdate from './replaced-products-update';
import ReplacedProductsDeleteDialog from './replaced-products-delete-dialog';

const ReplacedProductsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ReplacedProducts />} />
    <Route path="new" element={<ReplacedProductsUpdate />} />
    <Route path=":id">
      <Route index element={<ReplacedProductsDetail />} />
      <Route path="edit" element={<ReplacedProductsUpdate />} />
      <Route path="delete" element={<ReplacedProductsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ReplacedProductsRoutes;
