import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import RoleMst from './role-mst';
import RoleMstDetail from './role-mst-detail';
import RoleMstUpdate from './role-mst-update';
import RoleMstDeleteDialog from './role-mst-delete-dialog';

const RoleMstRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<RoleMst />} />
    <Route path="new" element={<RoleMstUpdate />} />
    <Route path=":id">
      <Route index element={<RoleMstDetail />} />
      <Route path="edit" element={<RoleMstUpdate />} />
      <Route path="delete" element={<RoleMstDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default RoleMstRoutes;
