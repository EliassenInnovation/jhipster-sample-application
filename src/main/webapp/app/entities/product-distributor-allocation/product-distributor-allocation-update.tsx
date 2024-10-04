import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './product-distributor-allocation.reducer';

export const ProductDistributorAllocationUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const productDistributorAllocationEntity = useAppSelector(state => state.productDistributorAllocation.entity);
  const loading = useAppSelector(state => state.productDistributorAllocation.loading);
  const updating = useAppSelector(state => state.productDistributorAllocation.updating);
  const updateSuccess = useAppSelector(state => state.productDistributorAllocation.updateSuccess);

  const handleClose = () => {
    navigate('/product-distributor-allocation');
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
    if (values.distributorId !== undefined && typeof values.distributorId !== 'number') {
      values.distributorId = Number(values.distributorId);
    }
    if (values.productId !== undefined && typeof values.productId !== 'number') {
      values.productId = Number(values.productId);
    }
    if (values.updatedBy !== undefined && typeof values.updatedBy !== 'number') {
      values.updatedBy = Number(values.updatedBy);
    }

    const entity = {
      ...productDistributorAllocationEntity,
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
          ...productDistributorAllocationEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dboApp.productDistributorAllocation.home.createOrEditLabel" data-cy="ProductDistributorAllocationCreateUpdateHeading">
            <Translate contentKey="dboApp.productDistributorAllocation.home.createOrEditLabel">
              Create or edit a ProductDistributorAllocation
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
                  id="product-distributor-allocation-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dboApp.productDistributorAllocation.createdBy')}
                id="product-distributor-allocation-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productDistributorAllocation.createdOn')}
                id="product-distributor-allocation-createdOn"
                name="createdOn"
                data-cy="createdOn"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.productDistributorAllocation.distributorId')}
                id="product-distributor-allocation-distributorId"
                name="distributorId"
                data-cy="distributorId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productDistributorAllocation.isAllocated')}
                id="product-distributor-allocation-isAllocated"
                name="isAllocated"
                data-cy="isAllocated"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('dboApp.productDistributorAllocation.productDistributorAllocationId')}
                id="product-distributor-allocation-productDistributorAllocationId"
                name="productDistributorAllocationId"
                data-cy="productDistributorAllocationId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productDistributorAllocation.productId')}
                id="product-distributor-allocation-productId"
                name="productId"
                data-cy="productId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productDistributorAllocation.updatedBy')}
                id="product-distributor-allocation-updatedBy"
                name="updatedBy"
                data-cy="updatedBy"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productDistributorAllocation.updatedOn')}
                id="product-distributor-allocation-updatedOn"
                name="updatedOn"
                data-cy="updatedOn"
                type="date"
              />
              <Button
                tag={Link}
                id="cancel-save"
                data-cy="entityCreateCancelButton"
                to="/product-distributor-allocation"
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

export default ProductDistributorAllocationUpdate;
