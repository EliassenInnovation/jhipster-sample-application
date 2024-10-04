import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CommunityComment from './community-comment';
import CommunityCommentDetail from './community-comment-detail';
import CommunityCommentUpdate from './community-comment-update';
import CommunityCommentDeleteDialog from './community-comment-delete-dialog';

const CommunityCommentRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CommunityComment />} />
    <Route path="new" element={<CommunityCommentUpdate />} />
    <Route path=":id">
      <Route index element={<CommunityCommentDetail />} />
      <Route path="edit" element={<CommunityCommentUpdate />} />
      <Route path="delete" element={<CommunityCommentDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CommunityCommentRoutes;
