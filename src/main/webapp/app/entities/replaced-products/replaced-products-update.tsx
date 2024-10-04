import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './replaced-products.reducer';

export const ReplacedProductsUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const replacedProductsEntity = useAppSelector(state => state.replacedProducts.entity);
  const loading = useAppSelector(state => state.replacedProducts.loading);
  const updating = useAppSelector(state => state.replacedProducts.updating);
  const updateSuccess = useAppSelector(state => state.replacedProducts.updateSuccess);

  const handleClose = () => {
    navigate('/replaced-products');
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
    if (values.productId !== undefined && typeof values.productId !== 'number') {
      values.productId = Number(values.productId);
    }
    if (values.replacedByUserId !== undefined && typeof values.replacedByUserId !== 'number') {
      values.replacedByUserId = Number(values.replacedByUserId);
    }
    if (values.replacedId !== undefined && typeof values.replacedId !== 'number') {
      values.replacedId = Number(values.replacedId);
    }
    if (values.replacedProductId !== undefined && typeof values.replacedProductId !== 'number') {
      values.replacedProductId = Number(values.replacedProductId);
    }
    if (values.schoolDistrictId !== undefined && typeof values.schoolDistrictId !== 'number') {
      values.schoolDistrictId = Number(values.schoolDistrictId);
    }

    const entity = {
      ...replacedProductsEntity,
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
          ...replacedProductsEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dboApp.replacedProducts.home.createOrEditLabel" data-cy="ReplacedProductsCreateUpdateHeading">
            <Translate contentKey="dboApp.replacedProducts.home.createOrEditLabel">Create or edit a ReplacedProducts</Translate>
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
                  id="replaced-products-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dboApp.replacedProducts.isActive')}
                id="replaced-products-isActive"
                name="isActive"
                data-cy="isActive"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('dboApp.replacedProducts.productId')}
                id="replaced-products-productId"
                name="productId"
                data-cy="productId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.replacedProducts.replacedByUserId')}
                id="replaced-products-replacedByUserId"
                name="replacedByUserId"
                data-cy="replacedByUserId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.replacedProducts.replacedId')}
                id="replaced-products-replacedId"
                name="replacedId"
                data-cy="replacedId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.replacedProducts.replacedProductId')}
                id="replaced-products-replacedProductId"
                name="replacedProductId"
                data-cy="replacedProductId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.replacedProducts.replacementDate')}
                id="replaced-products-replacementDate"
                name="replacementDate"
                data-cy="replacementDate"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.replacedProducts.schoolDistrictId')}
                id="replaced-products-schoolDistrictId"
                name="schoolDistrictId"
                data-cy="schoolDistrictId"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/replaced-products" replace color="info">
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

export default ReplacedProductsUpdate;
