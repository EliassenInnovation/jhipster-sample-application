import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AllergenMst from './allergen-mst';
import AllergenMstDetail from './allergen-mst-detail';
import AllergenMstUpdate from './allergen-mst-update';
import AllergenMstDeleteDialog from './allergen-mst-delete-dialog';

const AllergenMstRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<AllergenMst />} />
    <Route path="new" element={<AllergenMstUpdate />} />
    <Route path=":id">
      <Route index element={<AllergenMstDetail />} />
      <Route path="edit" element={<AllergenMstUpdate />} />
      <Route path="delete" element={<AllergenMstDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AllergenMstRoutes;
