import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './suggested-products.reducer';

export const SuggestedProductsUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const suggestedProductsEntity = useAppSelector(state => state.suggestedProducts.entity);
  const loading = useAppSelector(state => state.suggestedProducts.loading);
  const updating = useAppSelector(state => state.suggestedProducts.updating);
  const updateSuccess = useAppSelector(state => state.suggestedProducts.updateSuccess);

  const handleClose = () => {
    navigate('/suggested-products');
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
    if (values.suggestedByDistrict !== undefined && typeof values.suggestedByDistrict !== 'number') {
      values.suggestedByDistrict = Number(values.suggestedByDistrict);
    }
    if (values.suggestedByUserId !== undefined && typeof values.suggestedByUserId !== 'number') {
      values.suggestedByUserId = Number(values.suggestedByUserId);
    }
    if (values.suggestedProductId !== undefined && typeof values.suggestedProductId !== 'number') {
      values.suggestedProductId = Number(values.suggestedProductId);
    }
    if (values.suggestionId !== undefined && typeof values.suggestionId !== 'number') {
      values.suggestionId = Number(values.suggestionId);
    }

    const entity = {
      ...suggestedProductsEntity,
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
          ...suggestedProductsEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dboApp.suggestedProducts.home.createOrEditLabel" data-cy="SuggestedProductsCreateUpdateHeading">
            <Translate contentKey="dboApp.suggestedProducts.home.createOrEditLabel">Create or edit a SuggestedProducts</Translate>
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
                  id="suggested-products-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dboApp.suggestedProducts.isActive')}
                id="suggested-products-isActive"
                name="isActive"
                data-cy="isActive"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('dboApp.suggestedProducts.isApprove')}
                id="suggested-products-isApprove"
                name="isApprove"
                data-cy="isApprove"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('dboApp.suggestedProducts.productId')}
                id="suggested-products-productId"
                name="productId"
                data-cy="productId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.suggestedProducts.suggestedByDistrict')}
                id="suggested-products-suggestedByDistrict"
                name="suggestedByDistrict"
                data-cy="suggestedByDistrict"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.suggestedProducts.suggestedByUserId')}
                id="suggested-products-suggestedByUserId"
                name="suggestedByUserId"
                data-cy="suggestedByUserId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.suggestedProducts.suggestedProductId')}
                id="suggested-products-suggestedProductId"
                name="suggestedProductId"
                data-cy="suggestedProductId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.suggestedProducts.suggestionDate')}
                id="suggested-products-suggestionDate"
                name="suggestionDate"
                data-cy="suggestionDate"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.suggestedProducts.suggestionId')}
                id="suggested-products-suggestionId"
                name="suggestionId"
                data-cy="suggestionId"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/suggested-products" replace color="info">
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

export default SuggestedProductsUpdate;
