import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MonthMst from './month-mst';
import MonthMstDetail from './month-mst-detail';
import MonthMstUpdate from './month-mst-update';
import MonthMstDeleteDialog from './month-mst-delete-dialog';

const MonthMstRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MonthMst />} />
    <Route path="new" element={<MonthMstUpdate />} />
    <Route path=":id">
      <Route index element={<MonthMstDetail />} />
      <Route path="edit" element={<MonthMstUpdate />} />
      <Route path="delete" element={<MonthMstDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MonthMstRoutes;
