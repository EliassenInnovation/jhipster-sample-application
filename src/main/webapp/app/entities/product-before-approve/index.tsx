import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ProductBeforeApprove from './product-before-approve';
import ProductBeforeApproveDetail from './product-before-approve-detail';
import ProductBeforeApproveUpdate from './product-before-approve-update';
import ProductBeforeApproveDeleteDialog from './product-before-approve-delete-dialog';

const ProductBeforeApproveRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ProductBeforeApprove />} />
    <Route path="new" element={<ProductBeforeApproveUpdate />} />
    <Route path=":id">
      <Route index element={<ProductBeforeApproveDetail />} />
      <Route path="edit" element={<ProductBeforeApproveUpdate />} />
      <Route path="delete" element={<ProductBeforeApproveDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ProductBeforeApproveRoutes;
