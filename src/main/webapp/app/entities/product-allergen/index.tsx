import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ProductAllergen from './product-allergen';
import ProductAllergenDetail from './product-allergen-detail';
import ProductAllergenUpdate from './product-allergen-update';
import ProductAllergenDeleteDialog from './product-allergen-delete-dialog';

const ProductAllergenRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ProductAllergen />} />
    <Route path="new" element={<ProductAllergenUpdate />} />
    <Route path=":id">
      <Route index element={<ProductAllergenDetail />} />
      <Route path="edit" element={<ProductAllergenUpdate />} />
      <Route path="delete" element={<ProductAllergenDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ProductAllergenRoutes;
