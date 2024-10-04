import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ProductDistributorAllocation from './product-distributor-allocation';
import ProductDistributorAllocationDetail from './product-distributor-allocation-detail';
import ProductDistributorAllocationUpdate from './product-distributor-allocation-update';
import ProductDistributorAllocationDeleteDialog from './product-distributor-allocation-delete-dialog';

const ProductDistributorAllocationRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ProductDistributorAllocation />} />
    <Route path="new" element={<ProductDistributorAllocationUpdate />} />
    <Route path=":id">
      <Route index element={<ProductDistributorAllocationDetail />} />
      <Route path="edit" element={<ProductDistributorAllocationUpdate />} />
      <Route path="delete" element={<ProductDistributorAllocationDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ProductDistributorAllocationRoutes;
