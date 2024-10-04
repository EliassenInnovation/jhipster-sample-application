import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ProductManufacturerAllocation from './product-manufacturer-allocation';
import ProductManufacturerAllocationDetail from './product-manufacturer-allocation-detail';
import ProductManufacturerAllocationUpdate from './product-manufacturer-allocation-update';
import ProductManufacturerAllocationDeleteDialog from './product-manufacturer-allocation-delete-dialog';

const ProductManufacturerAllocationRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ProductManufacturerAllocation />} />
    <Route path="new" element={<ProductManufacturerAllocationUpdate />} />
    <Route path=":id">
      <Route index element={<ProductManufacturerAllocationDetail />} />
      <Route path="edit" element={<ProductManufacturerAllocationUpdate />} />
      <Route path="delete" element={<ProductManufacturerAllocationDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ProductManufacturerAllocationRoutes;
