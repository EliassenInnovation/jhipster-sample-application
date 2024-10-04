import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MonthlyNumbers from './monthly-numbers';
import MonthlyNumbersDetail from './monthly-numbers-detail';
import MonthlyNumbersUpdate from './monthly-numbers-update';
import MonthlyNumbersDeleteDialog from './monthly-numbers-delete-dialog';

const MonthlyNumbersRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MonthlyNumbers />} />
    <Route path="new" element={<MonthlyNumbersUpdate />} />
    <Route path=":id">
      <Route index element={<MonthlyNumbersDetail />} />
      <Route path="edit" element={<MonthlyNumbersUpdate />} />
      <Route path="delete" element={<MonthlyNumbersDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MonthlyNumbersRoutes;
