import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PrivacyType from './privacy-type';
import PrivacyTypeDetail from './privacy-type-detail';
import PrivacyTypeUpdate from './privacy-type-update';
import PrivacyTypeDeleteDialog from './privacy-type-delete-dialog';

const PrivacyTypeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PrivacyType />} />
    <Route path="new" element={<PrivacyTypeUpdate />} />
    <Route path=":id">
      <Route index element={<PrivacyTypeDetail />} />
      <Route path="edit" element={<PrivacyTypeUpdate />} />
      <Route path="delete" element={<PrivacyTypeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PrivacyTypeRoutes;
