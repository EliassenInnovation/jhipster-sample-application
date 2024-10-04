import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ProductImageBeforeApprove from './product-image-before-approve';
import ProductImageBeforeApproveDetail from './product-image-before-approve-detail';
import ProductImageBeforeApproveUpdate from './product-image-before-approve-update';
import ProductImageBeforeApproveDeleteDialog from './product-image-before-approve-delete-dialog';

const ProductImageBeforeApproveRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ProductImageBeforeApprove />} />
    <Route path="new" element={<ProductImageBeforeApproveUpdate />} />
    <Route path=":id">
      <Route index element={<ProductImageBeforeApproveDetail />} />
      <Route path="edit" element={<ProductImageBeforeApproveUpdate />} />
      <Route path="delete" element={<ProductImageBeforeApproveDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ProductImageBeforeApproveRoutes;
