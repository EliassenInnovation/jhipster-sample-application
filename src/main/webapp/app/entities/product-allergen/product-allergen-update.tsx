import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './product-allergen.reducer';

export const ProductAllergenUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const productAllergenEntity = useAppSelector(state => state.productAllergen.entity);
  const loading = useAppSelector(state => state.productAllergen.loading);
  const updating = useAppSelector(state => state.productAllergen.updating);
  const updateSuccess = useAppSelector(state => state.productAllergen.updateSuccess);

  const handleClose = () => {
    navigate('/product-allergen');
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
    if (values.allergenId !== undefined && typeof values.allergenId !== 'number') {
      values.allergenId = Number(values.allergenId);
    }
    if (values.productAllergenId !== undefined && typeof values.productAllergenId !== 'number') {
      values.productAllergenId = Number(values.productAllergenId);
    }
    if (values.productId !== undefined && typeof values.productId !== 'number') {
      values.productId = Number(values.productId);
    }

    const entity = {
      ...productAllergenEntity,
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
          ...productAllergenEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dboApp.productAllergen.home.createOrEditLabel" data-cy="ProductAllergenCreateUpdateHeading">
            <Translate contentKey="dboApp.productAllergen.home.createOrEditLabel">Create or edit a ProductAllergen</Translate>
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
                  id="product-allergen-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dboApp.productAllergen.allergenId')}
                id="product-allergen-allergenId"
                name="allergenId"
                data-cy="allergenId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productAllergen.allergenGroup')}
                id="product-allergen-allergenGroup"
                name="allergenGroup"
                data-cy="allergenGroup"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productAllergen.allergenName')}
                id="product-allergen-allergenName"
                name="allergenName"
                data-cy="allergenName"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productAllergen.description')}
                id="product-allergen-description"
                name="description"
                data-cy="description"
                type="textarea"
              />
              <ValidatedField
                label={translate('dboApp.productAllergen.gTIN')}
                id="product-allergen-gTIN"
                name="gTIN"
                data-cy="gTIN"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productAllergen.gTINUPC')}
                id="product-allergen-gTINUPC"
                name="gTINUPC"
                data-cy="gTINUPC"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productAllergen.productAllergenId')}
                id="product-allergen-productAllergenId"
                name="productAllergenId"
                data-cy="productAllergenId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productAllergen.productId')}
                id="product-allergen-productId"
                name="productId"
                data-cy="productId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productAllergen.uPC')}
                id="product-allergen-uPC"
                name="uPC"
                data-cy="uPC"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/product-allergen" replace color="info">
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

export default ProductAllergenUpdate;
