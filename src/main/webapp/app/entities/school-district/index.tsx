import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SchoolDistrict from './school-district';
import SchoolDistrictDetail from './school-district-detail';
import SchoolDistrictUpdate from './school-district-update';
import SchoolDistrictDeleteDialog from './school-district-delete-dialog';

const SchoolDistrictRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SchoolDistrict />} />
    <Route path="new" element={<SchoolDistrictUpdate />} />
    <Route path=":id">
      <Route index element={<SchoolDistrictDetail />} />
      <Route path="edit" element={<SchoolDistrictUpdate />} />
      <Route path="delete" element={<SchoolDistrictDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SchoolDistrictRoutes;
