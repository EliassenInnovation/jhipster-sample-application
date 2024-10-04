import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './product-district-allocation.reducer';

export const ProductDistrictAllocationUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const productDistrictAllocationEntity = useAppSelector(state => state.productDistrictAllocation.entity);
  const loading = useAppSelector(state => state.productDistrictAllocation.loading);
  const updating = useAppSelector(state => state.productDistrictAllocation.updating);
  const updateSuccess = useAppSelector(state => state.productDistrictAllocation.updateSuccess);

  const handleClose = () => {
    navigate('/product-district-allocation');
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
    if (values.productId !== undefined && typeof values.productId !== 'number') {
      values.productId = Number(values.productId);
    }
    if (values.schoolDistrictId !== undefined && typeof values.schoolDistrictId !== 'number') {
      values.schoolDistrictId = Number(values.schoolDistrictId);
    }
    if (values.updatedBy !== undefined && typeof values.updatedBy !== 'number') {
      values.updatedBy = Number(values.updatedBy);
    }

    const entity = {
      ...productDistrictAllocationEntity,
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
          ...productDistrictAllocationEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dboApp.productDistrictAllocation.home.createOrEditLabel" data-cy="ProductDistrictAllocationCreateUpdateHeading">
            <Translate contentKey="dboApp.productDistrictAllocation.home.createOrEditLabel">
              Create or edit a ProductDistrictAllocation
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
                  id="product-district-allocation-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dboApp.productDistrictAllocation.createdBy')}
                id="product-district-allocation-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productDistrictAllocation.createdOn')}
                id="product-district-allocation-createdOn"
                name="createdOn"
                data-cy="createdOn"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.productDistrictAllocation.isAllocated')}
                id="product-district-allocation-isAllocated"
                name="isAllocated"
                data-cy="isAllocated"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('dboApp.productDistrictAllocation.productDistrictAllocationId')}
                id="product-district-allocation-productDistrictAllocationId"
                name="productDistrictAllocationId"
                data-cy="productDistrictAllocationId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productDistrictAllocation.productId')}
                id="product-district-allocation-productId"
                name="productId"
                data-cy="productId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productDistrictAllocation.schoolDistrictId')}
                id="product-district-allocation-schoolDistrictId"
                name="schoolDistrictId"
                data-cy="schoolDistrictId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productDistrictAllocation.updatedBy')}
                id="product-district-allocation-updatedBy"
                name="updatedBy"
                data-cy="updatedBy"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productDistrictAllocation.updatedOn')}
                id="product-district-allocation-updatedOn"
                name="updatedOn"
                data-cy="updatedOn"
                type="date"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/product-district-allocation" replace color="info">
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

export default ProductDistrictAllocationUpdate;
