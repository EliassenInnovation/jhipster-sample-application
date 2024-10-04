import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import IocCategory from './ioc-category';
import IocCategoryDetail from './ioc-category-detail';
import IocCategoryUpdate from './ioc-category-update';
import IocCategoryDeleteDialog from './ioc-category-delete-dialog';

const IocCategoryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<IocCategory />} />
    <Route path="new" element={<IocCategoryUpdate />} />
    <Route path=":id">
      <Route index element={<IocCategoryDetail />} />
      <Route path="edit" element={<IocCategoryUpdate />} />
      <Route path="delete" element={<IocCategoryDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default IocCategoryRoutes;
