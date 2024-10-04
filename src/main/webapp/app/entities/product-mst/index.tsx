import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ProductMst from './product-mst';
import ProductMstDetail from './product-mst-detail';
import ProductMstUpdate from './product-mst-update';
import ProductMstDeleteDialog from './product-mst-delete-dialog';

const ProductMstRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ProductMst />} />
    <Route path="new" element={<ProductMstUpdate />} />
    <Route path=":id">
      <Route index element={<ProductMstDetail />} />
      <Route path="edit" element={<ProductMstUpdate />} />
      <Route path="delete" element={<ProductMstDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ProductMstRoutes;
