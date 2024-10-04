import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import OneWorldSyncProduct from './one-world-sync-product';
import OneWorldSyncProductDetail from './one-world-sync-product-detail';
import OneWorldSyncProductUpdate from './one-world-sync-product-update';
import OneWorldSyncProductDeleteDialog from './one-world-sync-product-delete-dialog';

const OneWorldSyncProductRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<OneWorldSyncProduct />} />
    <Route path="new" element={<OneWorldSyncProductUpdate />} />
    <Route path=":id">
      <Route index element={<OneWorldSyncProductDetail />} />
      <Route path="edit" element={<OneWorldSyncProductUpdate />} />
      <Route path="delete" element={<OneWorldSyncProductDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default OneWorldSyncProductRoutes;
