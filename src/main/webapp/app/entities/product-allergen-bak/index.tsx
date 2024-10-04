import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ProductAllergenBak from './product-allergen-bak';
import ProductAllergenBakDetail from './product-allergen-bak-detail';
import ProductAllergenBakUpdate from './product-allergen-bak-update';
import ProductAllergenBakDeleteDialog from './product-allergen-bak-delete-dialog';

const ProductAllergenBakRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ProductAllergenBak />} />
    <Route path="new" element={<ProductAllergenBakUpdate />} />
    <Route path=":id">
      <Route index element={<ProductAllergenBakDetail />} />
      <Route path="edit" element={<ProductAllergenBakUpdate />} />
      <Route path="delete" element={<ProductAllergenBakDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ProductAllergenBakRoutes;
