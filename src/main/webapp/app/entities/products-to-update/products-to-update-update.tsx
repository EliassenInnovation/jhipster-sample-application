import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './products-to-update.reducer';

export const ProductsToUpdateUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const productsToUpdateEntity = useAppSelector(state => state.productsToUpdate.entity);
  const loading = useAppSelector(state => state.productsToUpdate.loading);
  const updating = useAppSelector(state => state.productsToUpdate.updating);
  const updateSuccess = useAppSelector(state => state.productsToUpdate.updateSuccess);

  const handleClose = () => {
    navigate('/products-to-update');
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
    if (values.maxManufacturerID !== undefined && typeof values.maxManufacturerID !== 'number') {
      values.maxManufacturerID = Number(values.maxManufacturerID);
    }
    if (values.productId !== undefined && typeof values.productId !== 'number') {
      values.productId = Number(values.productId);
    }

    const entity = {
      ...productsToUpdateEntity,
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
          ...productsToUpdateEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dboApp.productsToUpdate.home.createOrEditLabel" data-cy="ProductsToUpdateCreateUpdateHeading">
            <Translate contentKey="dboApp.productsToUpdate.home.createOrEditLabel">Create or edit a ProductsToUpdate</Translate>
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
                  id="products-to-update-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dboApp.productsToUpdate.maxGLNCode')}
                id="products-to-update-maxGLNCode"
                name="maxGLNCode"
                data-cy="maxGLNCode"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productsToUpdate.maxManufacturerID')}
                id="products-to-update-maxManufacturerID"
                name="maxManufacturerID"
                data-cy="maxManufacturerID"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productsToUpdate.productId')}
                id="products-to-update-productId"
                name="productId"
                data-cy="productId"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/products-to-update" replace color="info">
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

export default ProductsToUpdateUpdate;
