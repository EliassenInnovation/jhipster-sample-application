import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Distributor from './distributor';
import DistributorDetail from './distributor-detail';
import DistributorUpdate from './distributor-update';
import DistributorDeleteDialog from './distributor-delete-dialog';

const DistributorRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Distributor />} />
    <Route path="new" element={<DistributorUpdate />} />
    <Route path=":id">
      <Route index element={<DistributorDetail />} />
      <Route path="edit" element={<DistributorUpdate />} />
      <Route path="delete" element={<DistributorDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DistributorRoutes;
