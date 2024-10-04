import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CommunityLikeMst from './community-like-mst';
import CommunityLikeMstDetail from './community-like-mst-detail';
import CommunityLikeMstUpdate from './community-like-mst-update';
import CommunityLikeMstDeleteDialog from './community-like-mst-delete-dialog';

const CommunityLikeMstRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CommunityLikeMst />} />
    <Route path="new" element={<CommunityLikeMstUpdate />} />
    <Route path=":id">
      <Route index element={<CommunityLikeMstDetail />} />
      <Route path="edit" element={<CommunityLikeMstUpdate />} />
      <Route path="delete" element={<CommunityLikeMstDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CommunityLikeMstRoutes;
