import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './ioc-category.reducer';

export const IocCategoryUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const iocCategoryEntity = useAppSelector(state => state.iocCategory.entity);
  const loading = useAppSelector(state => state.iocCategory.loading);
  const updating = useAppSelector(state => state.iocCategory.updating);
  const updateSuccess = useAppSelector(state => state.iocCategory.updateSuccess);

  const handleClose = () => {
    navigate('/ioc-category');
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
    if (values.iocCategoryId !== undefined && typeof values.iocCategoryId !== 'number') {
      values.iocCategoryId = Number(values.iocCategoryId);
    }

    const entity = {
      ...iocCategoryEntity,
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
          ...iocCategoryEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dboApp.iocCategory.home.createOrEditLabel" data-cy="IocCategoryCreateUpdateHeading">
            <Translate contentKey="dboApp.iocCategory.home.createOrEditLabel">Create or edit a IocCategory</Translate>
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
                  id="ioc-category-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dboApp.iocCategory.iocCategoryColor')}
                id="ioc-category-iocCategoryColor"
                name="iocCategoryColor"
                data-cy="iocCategoryColor"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.iocCategory.iocCategoryId')}
                id="ioc-category-iocCategoryId"
                name="iocCategoryId"
                data-cy="iocCategoryId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.iocCategory.iocCategoryName')}
                id="ioc-category-iocCategoryName"
                name="iocCategoryName"
                data-cy="iocCategoryName"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/ioc-category" replace color="info">
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

export default IocCategoryUpdate;
