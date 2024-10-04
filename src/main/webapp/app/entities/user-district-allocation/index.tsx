import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserDistrictAllocation from './user-district-allocation';
import UserDistrictAllocationDetail from './user-district-allocation-detail';
import UserDistrictAllocationUpdate from './user-district-allocation-update';
import UserDistrictAllocationDeleteDialog from './user-district-allocation-delete-dialog';

const UserDistrictAllocationRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<UserDistrictAllocation />} />
    <Route path="new" element={<UserDistrictAllocationUpdate />} />
    <Route path=":id">
      <Route index element={<UserDistrictAllocationDetail />} />
      <Route path="edit" element={<UserDistrictAllocationUpdate />} />
      <Route path="delete" element={<UserDistrictAllocationDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UserDistrictAllocationRoutes;
