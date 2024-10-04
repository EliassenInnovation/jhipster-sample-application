import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ProductH7 from './product-h-7';
import ProductH7Detail from './product-h-7-detail';
import ProductH7Update from './product-h-7-update';
import ProductH7DeleteDialog from './product-h-7-delete-dialog';

const ProductH7Routes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ProductH7 />} />
    <Route path="new" element={<ProductH7Update />} />
    <Route path=":id">
      <Route index element={<ProductH7Detail />} />
      <Route path="edit" element={<ProductH7Update />} />
      <Route path="delete" element={<ProductH7DeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ProductH7Routes;
