import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AllergenMst from './allergen-mst';
import ApplicationValue from './application-value';
import BlockReportPost from './block-report-post';
import Category from './category';
import CommunityComment from './community-comment';
import CommunityLikeMst from './community-like-mst';
import CommunityPost from './community-post';
import CommunityPostTransactions from './community-post-transactions';
import Distributor from './distributor';
import ErrorLog from './error-log';
import H7KeywordMst from './h-7-keyword-mst';
import IocCategory from './ioc-category';
import LoginHistory from './login-history';
import Manufacturer from './manufacturer';
import MonthlyNumbers from './monthly-numbers';
import MonthMst from './month-mst';
import Notification from './notification';
import OneWorldSyncProduct from './one-world-sync-product';
import PostTypes from './post-types';
import PrivacyType from './privacy-type';
import ProductActivityHistory from './product-activity-history';
import ProductAllergen from './product-allergen';
import ProductAllergenBak from './product-allergen-bak';
import ProductBeforeApprove from './product-before-approve';
import ProductChangeHistory from './product-change-history';
import ProductDistributorAllocation from './product-distributor-allocation';
import ProductDistrictAllocation from './product-district-allocation';
import ProductGtinAllocation from './product-gtin-allocation';
import ProductH7 from './product-h-7';
import ProductH7New from './product-h-7-new';
import ProductH7Old from './product-h-7-old';
import ProductImage from './product-image';
import ProductImageBeforeApprove from './product-image-before-approve';
import ProductManufacturerAllocation from './product-manufacturer-allocation';
import ProductMst from './product-mst';
import ProductsToUpdate from './products-to-update';
import ProductUpcAllocation from './product-upc-allocation';
import ReplacedProducts from './replaced-products';
import RoleMst from './role-mst';
import SchoolDistrict from './school-district';
import SetMappings from './set-mappings';
import StateInfo from './state-info';
import StorageTypes from './storage-types';
import SubCategory from './sub-category';
import SuggestedProducts from './suggested-products';
import SupportCategory from './support-category';
import SupportTicketMst from './support-ticket-mst';
import SupportTicketTransaction from './support-ticket-transaction';
import USDAUpdateMst from './usda-update-mst';
import UserDistrictAllocation from './user-district-allocation';
import UserMst from './user-mst';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="allergen-mst/*" element={<AllergenMst />} />
        <Route path="application-value/*" element={<ApplicationValue />} />
        <Route path="block-report-post/*" element={<BlockReportPost />} />
        <Route path="category/*" element={<Category />} />
        <Route path="community-comment/*" element={<CommunityComment />} />
        <Route path="community-like-mst/*" element={<CommunityLikeMst />} />
        <Route path="community-post/*" element={<CommunityPost />} />
        <Route path="community-post-transactions/*" element={<CommunityPostTransactions />} />
        <Route path="distributor/*" element={<Distributor />} />
        <Route path="error-log/*" element={<ErrorLog />} />
        <Route path="h-7-keyword-mst/*" element={<H7KeywordMst />} />
        <Route path="ioc-category/*" element={<IocCategory />} />
        <Route path="login-history/*" element={<LoginHistory />} />
        <Route path="manufacturer/*" element={<Manufacturer />} />
        <Route path="monthly-numbers/*" element={<MonthlyNumbers />} />
        <Route path="month-mst/*" element={<MonthMst />} />
        <Route path="notification/*" element={<Notification />} />
        <Route path="one-world-sync-product/*" element={<OneWorldSyncProduct />} />
        <Route path="post-types/*" element={<PostTypes />} />
        <Route path="privacy-type/*" element={<PrivacyType />} />
        <Route path="product-activity-history/*" element={<ProductActivityHistory />} />
        <Route path="product-allergen/*" element={<ProductAllergen />} />
        <Route path="product-allergen-bak/*" element={<ProductAllergenBak />} />
        <Route path="product-before-approve/*" element={<ProductBeforeApprove />} />
        <Route path="product-change-history/*" element={<ProductChangeHistory />} />
        <Route path="product-distributor-allocation/*" element={<ProductDistributorAllocation />} />
        <Route path="product-district-allocation/*" element={<ProductDistrictAllocation />} />
        <Route path="product-gtin-allocation/*" element={<ProductGtinAllocation />} />
        <Route path="product-h-7/*" element={<ProductH7 />} />
        <Route path="product-h-7-new/*" element={<ProductH7New />} />
        <Route path="product-h-7-old/*" element={<ProductH7Old />} />
        <Route path="product-image/*" element={<ProductImage />} />
        <Route path="product-image-before-approve/*" element={<ProductImageBeforeApprove />} />
        <Route path="product-manufacturer-allocation/*" element={<ProductManufacturerAllocation />} />
        <Route path="product-mst/*" element={<ProductMst />} />
        <Route path="products-to-update/*" element={<ProductsToUpdate />} />
        <Route path="product-upc-allocation/*" element={<ProductUpcAllocation />} />
        <Route path="replaced-products/*" element={<ReplacedProducts />} />
        <Route path="role-mst/*" element={<RoleMst />} />
        <Route path="school-district/*" element={<SchoolDistrict />} />
        <Route path="set-mappings/*" element={<SetMappings />} />
        <Route path="state-info/*" element={<StateInfo />} />
        <Route path="storage-types/*" element={<StorageTypes />} />
        <Route path="sub-category/*" element={<SubCategory />} />
        <Route path="suggested-products/*" element={<SuggestedProducts />} />
        <Route path="support-category/*" element={<SupportCategory />} />
        <Route path="support-ticket-mst/*" element={<SupportTicketMst />} />
        <Route path="support-ticket-transaction/*" element={<SupportTicketTransaction />} />
        <Route path="usda-update-mst/*" element={<USDAUpdateMst />} />
        <Route path="user-district-allocation/*" element={<UserDistrictAllocation />} />
        <Route path="user-mst/*" element={<UserMst />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
