import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './product-h-7.reducer';

export const ProductH7Update = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const productH7Entity = useAppSelector(state => state.productH7.entity);
  const loading = useAppSelector(state => state.productH7.loading);
  const updating = useAppSelector(state => state.productH7.updating);
  const updateSuccess = useAppSelector(state => state.productH7.updateSuccess);

  const handleClose = () => {
    navigate('/product-h-7');
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
    if (values.h7KeywordId !== undefined && typeof values.h7KeywordId !== 'number') {
      values.h7KeywordId = Number(values.h7KeywordId);
    }
    if (values.productH7Id !== undefined && typeof values.productH7Id !== 'number') {
      values.productH7Id = Number(values.productH7Id);
    }
    if (values.productId !== undefined && typeof values.productId !== 'number') {
      values.productId = Number(values.productId);
    }

    const entity = {
      ...productH7Entity,
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
          ...productH7Entity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dboApp.productH7.home.createOrEditLabel" data-cy="ProductH7CreateUpdateHeading">
            <Translate contentKey="dboApp.productH7.home.createOrEditLabel">Create or edit a ProductH7</Translate>
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
                  id="product-h-7-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dboApp.productH7.gtinUpc')}
                id="product-h-7-gtinUpc"
                name="gtinUpc"
                data-cy="gtinUpc"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productH7.h7KeywordId')}
                id="product-h-7-h7KeywordId"
                name="h7KeywordId"
                data-cy="h7KeywordId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productH7.iOCGroup')}
                id="product-h-7-iOCGroup"
                name="iOCGroup"
                data-cy="iOCGroup"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productH7.productH7Id')}
                id="product-h-7-productH7Id"
                name="productH7Id"
                data-cy="productH7Id"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productH7.productId')}
                id="product-h-7-productId"
                name="productId"
                data-cy="productId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productH7.productName')}
                id="product-h-7-productName"
                name="productName"
                data-cy="productName"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/product-h-7" replace color="info">
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

export default ProductH7Update;
