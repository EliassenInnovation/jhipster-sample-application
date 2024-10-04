import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import H7KeywordMst from './h-7-keyword-mst';
import H7KeywordMstDetail from './h-7-keyword-mst-detail';
import H7KeywordMstUpdate from './h-7-keyword-mst-update';
import H7KeywordMstDeleteDialog from './h-7-keyword-mst-delete-dialog';

const H7KeywordMstRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<H7KeywordMst />} />
    <Route path="new" element={<H7KeywordMstUpdate />} />
    <Route path=":id">
      <Route index element={<H7KeywordMstDetail />} />
      <Route path="edit" element={<H7KeywordMstUpdate />} />
      <Route path="delete" element={<H7KeywordMstDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default H7KeywordMstRoutes;
