import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SetMappings from './set-mappings';
import SetMappingsDetail from './set-mappings-detail';
import SetMappingsUpdate from './set-mappings-update';
import SetMappingsDeleteDialog from './set-mappings-delete-dialog';

const SetMappingsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SetMappings />} />
    <Route path="new" element={<SetMappingsUpdate />} />
    <Route path=":id">
      <Route index element={<SetMappingsDetail />} />
      <Route path="edit" element={<SetMappingsUpdate />} />
      <Route path="delete" element={<SetMappingsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SetMappingsRoutes;
