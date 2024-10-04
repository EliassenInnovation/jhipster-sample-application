import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './product-manufacturer-allocation.reducer';

export const ProductManufacturerAllocationUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const productManufacturerAllocationEntity = useAppSelector(state => state.productManufacturerAllocation.entity);
  const loading = useAppSelector(state => state.productManufacturerAllocation.loading);
  const updating = useAppSelector(state => state.productManufacturerAllocation.updating);
  const updateSuccess = useAppSelector(state => state.productManufacturerAllocation.updateSuccess);

  const handleClose = () => {
    navigate('/product-manufacturer-allocation');
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
    if (values.manufactureId !== undefined && typeof values.manufactureId !== 'number') {
      values.manufactureId = Number(values.manufactureId);
    }
    if (values.productId !== undefined && typeof values.productId !== 'number') {
      values.productId = Number(values.productId);
    }
    if (values.updatedBy !== undefined && typeof values.updatedBy !== 'number') {
      values.updatedBy = Number(values.updatedBy);
    }

    const entity = {
      ...productManufacturerAllocationEntity,
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
          ...productManufacturerAllocationEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dboApp.productManufacturerAllocation.home.createOrEditLabel" data-cy="ProductManufacturerAllocationCreateUpdateHeading">
            <Translate contentKey="dboApp.productManufacturerAllocation.home.createOrEditLabel">
              Create or edit a ProductManufacturerAllocation
            </Translate>
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
                  id="product-manufacturer-allocation-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dboApp.productManufacturerAllocation.createdBy')}
                id="product-manufacturer-allocation-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productManufacturerAllocation.createdOn')}
                id="product-manufacturer-allocation-createdOn"
                name="createdOn"
                data-cy="createdOn"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.productManufacturerAllocation.isAllocated')}
                id="product-manufacturer-allocation-isAllocated"
                name="isAllocated"
                data-cy="isAllocated"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('dboApp.productManufacturerAllocation.manufactureId')}
                id="product-manufacturer-allocation-manufactureId"
                name="manufactureId"
                data-cy="manufactureId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productManufacturerAllocation.productId')}
                id="product-manufacturer-allocation-productId"
                name="productId"
                data-cy="productId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productManufacturerAllocation.productManufacturerAllocationId')}
                id="product-manufacturer-allocation-productManufacturerAllocationId"
                name="productManufacturerAllocationId"
                data-cy="productManufacturerAllocationId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productManufacturerAllocation.updatedBy')}
                id="product-manufacturer-allocation-updatedBy"
                name="updatedBy"
                data-cy="updatedBy"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productManufacturerAllocation.updatedOn')}
                id="product-manufacturer-allocation-updatedOn"
                name="updatedOn"
                data-cy="updatedOn"
                type="date"
              />
              <Button
                tag={Link}
                id="cancel-save"
                data-cy="entityCreateCancelButton"
                to="/product-manufacturer-allocation"
                replace
                color="info"
              >
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

export default ProductManufacturerAllocationUpdate;
