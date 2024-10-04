import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ProductDistrictAllocation from './product-district-allocation';
import ProductDistrictAllocationDetail from './product-district-allocation-detail';
import ProductDistrictAllocationUpdate from './product-district-allocation-update';
import ProductDistrictAllocationDeleteDialog from './product-district-allocation-delete-dialog';

const ProductDistrictAllocationRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ProductDistrictAllocation />} />
    <Route path="new" element={<ProductDistrictAllocationUpdate />} />
    <Route path=":id">
      <Route index element={<ProductDistrictAllocationDetail />} />
      <Route path="edit" element={<ProductDistrictAllocationUpdate />} />
      <Route path="delete" element={<ProductDistrictAllocationDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ProductDistrictAllocationRoutes;
