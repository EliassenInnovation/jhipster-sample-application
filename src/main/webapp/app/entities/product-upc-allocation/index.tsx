import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ProductUpcAllocation from './product-upc-allocation';
import ProductUpcAllocationDetail from './product-upc-allocation-detail';
import ProductUpcAllocationUpdate from './product-upc-allocation-update';
import ProductUpcAllocationDeleteDialog from './product-upc-allocation-delete-dialog';

const ProductUpcAllocationRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ProductUpcAllocation />} />
    <Route path="new" element={<ProductUpcAllocationUpdate />} />
    <Route path=":id">
      <Route index element={<ProductUpcAllocationDetail />} />
      <Route path="edit" element={<ProductUpcAllocationUpdate />} />
      <Route path="delete" element={<ProductUpcAllocationDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ProductUpcAllocationRoutes;
