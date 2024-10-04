import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './product-image-before-approve.reducer';

export const ProductImageBeforeApproveUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const productImageBeforeApproveEntity = useAppSelector(state => state.productImageBeforeApprove.entity);
  const loading = useAppSelector(state => state.productImageBeforeApprove.loading);
  const updating = useAppSelector(state => state.productImageBeforeApprove.updating);
  const updateSuccess = useAppSelector(state => state.productImageBeforeApprove.updateSuccess);

  const handleClose = () => {
    navigate('/product-image-before-approve');
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
    if (values.productImageId !== undefined && typeof values.productImageId !== 'number') {
      values.productImageId = Number(values.productImageId);
    }

    const entity = {
      ...productImageBeforeApproveEntity,
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
          ...productImageBeforeApproveEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dboApp.productImageBeforeApprove.home.createOrEditLabel" data-cy="ProductImageBeforeApproveCreateUpdateHeading">
            <Translate contentKey="dboApp.productImageBeforeApprove.home.createOrEditLabel">
              Create or edit a ProductImageBeforeApprove
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
                  id="product-image-before-approve-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dboApp.productImageBeforeApprove.createdBy')}
                id="product-image-before-approve-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productImageBeforeApprove.createdOn')}
                id="product-image-before-approve-createdOn"
                name="createdOn"
                data-cy="createdOn"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.productImageBeforeApprove.imageURL')}
                id="product-image-before-approve-imageURL"
                name="imageURL"
                data-cy="imageURL"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productImageBeforeApprove.isActive')}
                id="product-image-before-approve-isActive"
                name="isActive"
                data-cy="isActive"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('dboApp.productImageBeforeApprove.productId')}
                id="product-image-before-approve-productId"
                name="productId"
                data-cy="productId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productImageBeforeApprove.productImageId')}
                id="product-image-before-approve-productImageId"
                name="productImageId"
                data-cy="productImageId"
                type="text"
              />
              <Button
                tag={Link}
                id="cancel-save"
                data-cy="entityCreateCancelButton"
                to="/product-image-before-approve"
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

export default ProductImageBeforeApproveUpdate;
