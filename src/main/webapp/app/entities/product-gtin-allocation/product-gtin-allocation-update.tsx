import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './product-gtin-allocation.reducer';

export const ProductGtinAllocationUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const productGtinAllocationEntity = useAppSelector(state => state.productGtinAllocation.entity);
  const loading = useAppSelector(state => state.productGtinAllocation.loading);
  const updating = useAppSelector(state => state.productGtinAllocation.updating);
  const updateSuccess = useAppSelector(state => state.productGtinAllocation.updateSuccess);

  const handleClose = () => {
    navigate('/product-gtin-allocation');
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
    if (values.productGtinId !== undefined && typeof values.productGtinId !== 'number') {
      values.productGtinId = Number(values.productGtinId);
    }
    if (values.productId !== undefined && typeof values.productId !== 'number') {
      values.productId = Number(values.productId);
    }
    if (values.updatedBy !== undefined && typeof values.updatedBy !== 'number') {
      values.updatedBy = Number(values.updatedBy);
    }

    const entity = {
      ...productGtinAllocationEntity,
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
          ...productGtinAllocationEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dboApp.productGtinAllocation.home.createOrEditLabel" data-cy="ProductGtinAllocationCreateUpdateHeading">
            <Translate contentKey="dboApp.productGtinAllocation.home.createOrEditLabel">Create or edit a ProductGtinAllocation</Translate>
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
                  id="product-gtin-allocation-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dboApp.productGtinAllocation.createdBy')}
                id="product-gtin-allocation-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productGtinAllocation.createdOn')}
                id="product-gtin-allocation-createdOn"
                name="createdOn"
                data-cy="createdOn"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.productGtinAllocation.gTIN')}
                id="product-gtin-allocation-gTIN"
                name="gTIN"
                data-cy="gTIN"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productGtinAllocation.isActive')}
                id="product-gtin-allocation-isActive"
                name="isActive"
                data-cy="isActive"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('dboApp.productGtinAllocation.productGtinId')}
                id="product-gtin-allocation-productGtinId"
                name="productGtinId"
                data-cy="productGtinId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productGtinAllocation.productId')}
                id="product-gtin-allocation-productId"
                name="productId"
                data-cy="productId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productGtinAllocation.updatedBy')}
                id="product-gtin-allocation-updatedBy"
                name="updatedBy"
                data-cy="updatedBy"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productGtinAllocation.updatedOn')}
                id="product-gtin-allocation-updatedOn"
                name="updatedOn"
                data-cy="updatedOn"
                type="date"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/product-gtin-allocation" replace color="info">
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

export default ProductGtinAllocationUpdate;
