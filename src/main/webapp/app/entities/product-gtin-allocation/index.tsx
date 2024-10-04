import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ProductGtinAllocation from './product-gtin-allocation';
import ProductGtinAllocationDetail from './product-gtin-allocation-detail';
import ProductGtinAllocationUpdate from './product-gtin-allocation-update';
import ProductGtinAllocationDeleteDialog from './product-gtin-allocation-delete-dialog';

const ProductGtinAllocationRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ProductGtinAllocation />} />
    <Route path="new" element={<ProductGtinAllocationUpdate />} />
    <Route path=":id">
      <Route index element={<ProductGtinAllocationDetail />} />
      <Route path="edit" element={<ProductGtinAllocationUpdate />} />
      <Route path="delete" element={<ProductGtinAllocationDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ProductGtinAllocationRoutes;
