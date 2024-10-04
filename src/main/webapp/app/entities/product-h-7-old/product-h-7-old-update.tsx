import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './product-h-7-old.reducer';

export const ProductH7OldUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const productH7OldEntity = useAppSelector(state => state.productH7Old.entity);
  const loading = useAppSelector(state => state.productH7Old.loading);
  const updating = useAppSelector(state => state.productH7Old.updating);
  const updateSuccess = useAppSelector(state => state.productH7Old.updateSuccess);

  const handleClose = () => {
    navigate('/product-h-7-old');
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
      ...productH7OldEntity,
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
          ...productH7OldEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dboApp.productH7Old.home.createOrEditLabel" data-cy="ProductH7OldCreateUpdateHeading">
            <Translate contentKey="dboApp.productH7Old.home.createOrEditLabel">Create or edit a ProductH7Old</Translate>
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
                  id="product-h-7-old-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dboApp.productH7Old.gtinUpc')}
                id="product-h-7-old-gtinUpc"
                name="gtinUpc"
                data-cy="gtinUpc"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productH7Old.h7KeywordId')}
                id="product-h-7-old-h7KeywordId"
                name="h7KeywordId"
                data-cy="h7KeywordId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productH7Old.iOCGroup')}
                id="product-h-7-old-iOCGroup"
                name="iOCGroup"
                data-cy="iOCGroup"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productH7Old.productH7Id')}
                id="product-h-7-old-productH7Id"
                name="productH7Id"
                data-cy="productH7Id"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productH7Old.productId')}
                id="product-h-7-old-productId"
                name="productId"
                data-cy="productId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productH7Old.productName')}
                id="product-h-7-old-productName"
                name="productName"
                data-cy="productName"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/product-h-7-old" replace color="info">
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

export default ProductH7OldUpdate;
