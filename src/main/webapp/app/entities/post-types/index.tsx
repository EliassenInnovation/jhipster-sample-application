import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PostTypes from './post-types';
import PostTypesDetail from './post-types-detail';
import PostTypesUpdate from './post-types-update';
import PostTypesDeleteDialog from './post-types-delete-dialog';

const PostTypesRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PostTypes />} />
    <Route path="new" element={<PostTypesUpdate />} />
    <Route path=":id">
      <Route index element={<PostTypesDetail />} />
      <Route path="edit" element={<PostTypesUpdate />} />
      <Route path="delete" element={<PostTypesDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PostTypesRoutes;
