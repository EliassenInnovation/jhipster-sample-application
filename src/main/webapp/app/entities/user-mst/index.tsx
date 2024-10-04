import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserMst from './user-mst';
import UserMstDetail from './user-mst-detail';
import UserMstUpdate from './user-mst-update';
import UserMstDeleteDialog from './user-mst-delete-dialog';

const UserMstRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<UserMst />} />
    <Route path="new" element={<UserMstUpdate />} />
    <Route path=":id">
      <Route index element={<UserMstDetail />} />
      <Route path="edit" element={<UserMstUpdate />} />
      <Route path="delete" element={<UserMstDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UserMstRoutes;
