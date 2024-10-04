import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import StorageTypes from './storage-types';
import StorageTypesDetail from './storage-types-detail';
import StorageTypesUpdate from './storage-types-update';
import StorageTypesDeleteDialog from './storage-types-delete-dialog';

const StorageTypesRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<StorageTypes />} />
    <Route path="new" element={<StorageTypesUpdate />} />
    <Route path=":id">
      <Route index element={<StorageTypesDetail />} />
      <Route path="edit" element={<StorageTypesUpdate />} />
      <Route path="delete" element={<StorageTypesDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default StorageTypesRoutes;
