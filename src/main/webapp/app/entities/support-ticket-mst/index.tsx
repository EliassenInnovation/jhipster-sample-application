import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SupportTicketMst from './support-ticket-mst';
import SupportTicketMstDetail from './support-ticket-mst-detail';
import SupportTicketMstUpdate from './support-ticket-mst-update';
import SupportTicketMstDeleteDialog from './support-ticket-mst-delete-dialog';

const SupportTicketMstRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SupportTicketMst />} />
    <Route path="new" element={<SupportTicketMstUpdate />} />
    <Route path=":id">
      <Route index element={<SupportTicketMstDetail />} />
      <Route path="edit" element={<SupportTicketMstUpdate />} />
      <Route path="delete" element={<SupportTicketMstDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SupportTicketMstRoutes;
