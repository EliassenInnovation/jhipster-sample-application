import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CommunityPostTransactions from './community-post-transactions';
import CommunityPostTransactionsDetail from './community-post-transactions-detail';
import CommunityPostTransactionsUpdate from './community-post-transactions-update';
import CommunityPostTransactionsDeleteDialog from './community-post-transactions-delete-dialog';

const CommunityPostTransactionsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CommunityPostTransactions />} />
    <Route path="new" element={<CommunityPostTransactionsUpdate />} />
    <Route path=":id">
      <Route index element={<CommunityPostTransactionsDetail />} />
      <Route path="edit" element={<CommunityPostTransactionsUpdate />} />
      <Route path="delete" element={<CommunityPostTransactionsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CommunityPostTransactionsRoutes;
