import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './product-change-history.reducer';

export const ProductChangeHistoryUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const productChangeHistoryEntity = useAppSelector(state => state.productChangeHistory.entity);
  const loading = useAppSelector(state => state.productChangeHistory.loading);
  const updating = useAppSelector(state => state.productChangeHistory.updating);
  const updateSuccess = useAppSelector(state => state.productChangeHistory.updateSuccess);

  const handleClose = () => {
    navigate('/product-change-history');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.createdBy !== undefined && typeof values.createdBy !== 'number') {
      values.createdBy = Number(values.createdBy);
    }
    if (values.iocCategoryId !== undefined && typeof values.iocCategoryId !== 'number') {
      values.iocCategoryId = Number(values.iocCategoryId);
    }
    if (values.productId !== undefined && typeof values.productId !== 'number') {
      values.productId = Number(values.productId);
    }
    if (values.schoolDistrictId !== undefined && typeof values.schoolDistrictId !== 'number') {
      values.schoolDistrictId = Number(values.schoolDistrictId);
    }

    const entity = {
      ...productChangeHistoryEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...productChangeHistoryEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dboApp.productChangeHistory.home.createOrEditLabel" data-cy="ProductChangeHistoryCreateUpdateHeading">
            <Translate contentKey="dboApp.productChangeHistory.home.createOrEditLabel">Create or edit a ProductChangeHistory</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="product-change-history-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dboApp.productChangeHistory.createdBy')}
                id="product-change-history-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productChangeHistory.dateCreated')}
                id="product-change-history-dateCreated"
                name="dateCreated"
                data-cy="dateCreated"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.productChangeHistory.historyId')}
                id="product-change-history-historyId"
                name="historyId"
                data-cy="historyId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productChangeHistory.iocCategoryId')}
                id="product-change-history-iocCategoryId"
                name="iocCategoryId"
                data-cy="iocCategoryId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productChangeHistory.isActive')}
                id="product-change-history-isActive"
                name="isActive"
                data-cy="isActive"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('dboApp.productChangeHistory.productId')}
                id="product-change-history-productId"
                name="productId"
                data-cy="productId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productChangeHistory.schoolDistrictId')}
                id="product-change-history-schoolDistrictId"
                name="schoolDistrictId"
                data-cy="schoolDistrictId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productChangeHistory.selectionType')}
                id="product-change-history-selectionType"
                name="selectionType"
                data-cy="selectionType"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/product-change-history" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ProductChangeHistoryUpdate;
