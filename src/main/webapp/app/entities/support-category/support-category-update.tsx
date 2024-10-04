import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './support-category.reducer';

export const SupportCategoryUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const supportCategoryEntity = useAppSelector(state => state.supportCategory.entity);
  const loading = useAppSelector(state => state.supportCategory.loading);
  const updating = useAppSelector(state => state.supportCategory.updating);
  const updateSuccess = useAppSelector(state => state.supportCategory.updateSuccess);

  const handleClose = () => {
    navigate('/support-category');
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
    if (values.supportCategoryId !== undefined && typeof values.supportCategoryId !== 'number') {
      values.supportCategoryId = Number(values.supportCategoryId);
    }

    const entity = {
      ...supportCategoryEntity,
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
          ...supportCategoryEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dboApp.supportCategory.home.createOrEditLabel" data-cy="SupportCategoryCreateUpdateHeading">
            <Translate contentKey="dboApp.supportCategory.home.createOrEditLabel">Create or edit a SupportCategory</Translate>
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
                  id="support-category-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dboApp.supportCategory.supportCategoryId')}
                id="support-category-supportCategoryId"
                name="supportCategoryId"
                data-cy="supportCategoryId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.supportCategory.supportCategoryName')}
                id="support-category-supportCategoryName"
                name="supportCategoryName"
                data-cy="supportCategoryName"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/support-category" replace color="info">
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

export default SupportCategoryUpdate;
