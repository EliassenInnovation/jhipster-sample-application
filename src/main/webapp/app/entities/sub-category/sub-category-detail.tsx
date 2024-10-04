import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './sub-category.reducer';

export const SubCategoryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const subCategoryEntity = useAppSelector(state => state.subCategory.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="subCategoryDetailsHeading">
          <Translate contentKey="dboApp.subCategory.detail.title">SubCategory</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{subCategoryEntity.id}</dd>
          <dt>
            <span id="categoryId">
              <Translate contentKey="dboApp.subCategory.categoryId">Category Id</Translate>
            </span>
          </dt>
          <dd>{subCategoryEntity.categoryId}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="dboApp.subCategory.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{subCategoryEntity.createdBy}</dd>
          <dt>
            <span id="createdOn">
              <Translate contentKey="dboApp.subCategory.createdOn">Created On</Translate>
            </span>
          </dt>
          <dd>
            {subCategoryEntity.createdOn ? (
              <TextFormat value={subCategoryEntity.createdOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="dboApp.subCategory.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{subCategoryEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="subCategoryCode">
              <Translate contentKey="dboApp.subCategory.subCategoryCode">Sub Category Code</Translate>
            </span>
          </dt>
          <dd>{subCategoryEntity.subCategoryCode}</dd>
          <dt>
            <span id="subCategoryId">
              <Translate contentKey="dboApp.subCategory.subCategoryId">Sub Category Id</Translate>
            </span>
          </dt>
          <dd>{subCategoryEntity.subCategoryId}</dd>
          <dt>
            <span id="subCategoryName">
              <Translate contentKey="dboApp.subCategory.subCategoryName">Sub Category Name</Translate>
            </span>
          </dt>
          <dd>{subCategoryEntity.subCategoryName}</dd>
          <dt>
            <span id="updatedBy">
              <Translate contentKey="dboApp.subCategory.updatedBy">Updated By</Translate>
            </span>
          </dt>
          <dd>{subCategoryEntity.updatedBy}</dd>
          <dt>
            <span id="updatedOn">
              <Translate contentKey="dboApp.subCategory.updatedOn">Updated On</Translate>
            </span>
          </dt>
          <dd>
            {subCategoryEntity.updatedOn ? (
              <TextFormat value={subCategoryEntity.updatedOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/sub-category" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/sub-category/${subCategoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SubCategoryDetail;
