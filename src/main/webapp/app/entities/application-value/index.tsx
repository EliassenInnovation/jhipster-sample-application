import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ApplicationValue from './application-value';
import ApplicationValueDetail from './application-value-detail';
import ApplicationValueUpdate from './application-value-update';
import ApplicationValueDeleteDialog from './application-value-delete-dialog';

const ApplicationValueRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ApplicationValue />} />
    <Route path="new" element={<ApplicationValueUpdate />} />
    <Route path=":id">
      <Route index element={<ApplicationValueDetail />} />
      <Route path="edit" element={<ApplicationValueUpdate />} />
      <Route path="delete" element={<ApplicationValueDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ApplicationValueRoutes;
