import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SupportTicketTransaction from './support-ticket-transaction';
import SupportTicketTransactionDetail from './support-ticket-transaction-detail';
import SupportTicketTransactionUpdate from './support-ticket-transaction-update';
import SupportTicketTransactionDeleteDialog from './support-ticket-transaction-delete-dialog';

const SupportTicketTransactionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SupportTicketTransaction />} />
    <Route path="new" element={<SupportTicketTransactionUpdate />} />
    <Route path=":id">
      <Route index element={<SupportTicketTransactionDetail />} />
      <Route path="edit" element={<SupportTicketTransactionUpdate />} />
      <Route path="delete" element={<SupportTicketTransactionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SupportTicketTransactionRoutes;
