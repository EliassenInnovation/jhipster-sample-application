import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import StateInfo from './state-info';
import StateInfoDetail from './state-info-detail';
import StateInfoUpdate from './state-info-update';
import StateInfoDeleteDialog from './state-info-delete-dialog';

const StateInfoRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<StateInfo />} />
    <Route path="new" element={<StateInfoUpdate />} />
    <Route path=":id">
      <Route index element={<StateInfoDetail />} />
      <Route path="edit" element={<StateInfoUpdate />} />
      <Route path="delete" element={<StateInfoDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default StateInfoRoutes;
