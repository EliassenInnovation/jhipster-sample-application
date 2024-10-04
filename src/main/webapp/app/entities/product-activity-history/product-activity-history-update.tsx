import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './product-activity-history.reducer';

export const ProductActivityHistoryUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const productActivityHistoryEntity = useAppSelector(state => state.productActivityHistory.entity);
  const loading = useAppSelector(state => state.productActivityHistory.loading);
  const updating = useAppSelector(state => state.productActivityHistory.updating);
  const updateSuccess = useAppSelector(state => state.productActivityHistory.updateSuccess);

  const handleClose = () => {
    navigate('/product-activity-history');
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
    if (values.activityId !== undefined && typeof values.activityId !== 'number') {
      values.activityId = Number(values.activityId);
    }
    if (values.createdBy !== undefined && typeof values.createdBy !== 'number') {
      values.createdBy = Number(values.createdBy);
    }
    if (values.productId !== undefined && typeof values.productId !== 'number') {
      values.productId = Number(values.productId);
    }
    if (values.suggestedProductId !== undefined && typeof values.suggestedProductId !== 'number') {
      values.suggestedProductId = Number(values.suggestedProductId);
    }
    if (values.updatedBy !== undefined && typeof values.updatedBy !== 'number') {
      values.updatedBy = Number(values.updatedBy);
    }

    const entity = {
      ...productActivityHistoryEntity,
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
          ...productActivityHistoryEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dboApp.productActivityHistory.home.createOrEditLabel" data-cy="ProductActivityHistoryCreateUpdateHeading">
            <Translate contentKey="dboApp.productActivityHistory.home.createOrEditLabel">Create or edit a ProductActivityHistory</Translate>
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
                  id="product-activity-history-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dboApp.productActivityHistory.activityId')}
                id="product-activity-history-activityId"
                name="activityId"
                data-cy="activityId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productActivityHistory.activityType')}
                id="product-activity-history-activityType"
                name="activityType"
                data-cy="activityType"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productActivityHistory.createdBy')}
                id="product-activity-history-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productActivityHistory.createdOn')}
                id="product-activity-history-createdOn"
                name="createdOn"
                data-cy="createdOn"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.productActivityHistory.date')}
                id="product-activity-history-date"
                name="date"
                data-cy="date"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.productActivityHistory.isActive')}
                id="product-activity-history-isActive"
                name="isActive"
                data-cy="isActive"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('dboApp.productActivityHistory.productId')}
                id="product-activity-history-productId"
                name="productId"
                data-cy="productId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productActivityHistory.suggestedProductId')}
                id="product-activity-history-suggestedProductId"
                name="suggestedProductId"
                data-cy="suggestedProductId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productActivityHistory.updatedBy')}
                id="product-activity-history-updatedBy"
                name="updatedBy"
                data-cy="updatedBy"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productActivityHistory.updatedOn')}
                id="product-activity-history-updatedOn"
                name="updatedOn"
                data-cy="updatedOn"
                type="date"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/product-activity-history" replace color="info">
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

export default ProductActivityHistoryUpdate;
