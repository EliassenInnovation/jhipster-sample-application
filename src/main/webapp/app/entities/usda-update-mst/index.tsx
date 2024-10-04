import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import USDAUpdateMst from './usda-update-mst';
import USDAUpdateMstDetail from './usda-update-mst-detail';
import USDAUpdateMstUpdate from './usda-update-mst-update';
import USDAUpdateMstDeleteDialog from './usda-update-mst-delete-dialog';

const USDAUpdateMstRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<USDAUpdateMst />} />
    <Route path="new" element={<USDAUpdateMstUpdate />} />
    <Route path=":id">
      <Route index element={<USDAUpdateMstDetail />} />
      <Route path="edit" element={<USDAUpdateMstUpdate />} />
      <Route path="delete" element={<USDAUpdateMstDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default USDAUpdateMstRoutes;
