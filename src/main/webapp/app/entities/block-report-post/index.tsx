import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import BlockReportPost from './block-report-post';
import BlockReportPostDetail from './block-report-post-detail';
import BlockReportPostUpdate from './block-report-post-update';
import BlockReportPostDeleteDialog from './block-report-post-delete-dialog';

const BlockReportPostRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<BlockReportPost />} />
    <Route path="new" element={<BlockReportPostUpdate />} />
    <Route path=":id">
      <Route index element={<BlockReportPostDetail />} />
      <Route path="edit" element={<BlockReportPostUpdate />} />
      <Route path="delete" element={<BlockReportPostDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BlockReportPostRoutes;
