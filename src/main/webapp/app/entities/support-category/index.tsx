import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SupportCategory from './support-category';
import SupportCategoryDetail from './support-category-detail';
import SupportCategoryUpdate from './support-category-update';
import SupportCategoryDeleteDialog from './support-category-delete-dialog';

const SupportCategoryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SupportCategory />} />
    <Route path="new" element={<SupportCategoryUpdate />} />
    <Route path=":id">
      <Route index element={<SupportCategoryDetail />} />
      <Route path="edit" element={<SupportCategoryUpdate />} />
      <Route path="delete" element={<SupportCategoryDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SupportCategoryRoutes;
