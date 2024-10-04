import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import LoginHistory from './login-history';
import LoginHistoryDetail from './login-history-detail';
import LoginHistoryUpdate from './login-history-update';
import LoginHistoryDeleteDialog from './login-history-delete-dialog';

const LoginHistoryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<LoginHistory />} />
    <Route path="new" element={<LoginHistoryUpdate />} />
    <Route path=":id">
      <Route index element={<LoginHistoryDetail />} />
      <Route path="edit" element={<LoginHistoryUpdate />} />
      <Route path="delete" element={<LoginHistoryDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default LoginHistoryRoutes;
