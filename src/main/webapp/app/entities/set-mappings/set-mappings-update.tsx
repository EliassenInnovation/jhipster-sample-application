import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './set-mappings.reducer';

export const SetMappingsUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const setMappingsEntity = useAppSelector(state => state.setMappings.entity);
  const loading = useAppSelector(state => state.setMappings.loading);
  const updating = useAppSelector(state => state.setMappings.updating);
  const updateSuccess = useAppSelector(state => state.setMappings.updateSuccess);

  const handleClose = () => {
    navigate('/set-mappings');
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
    if (values.iD !== undefined && typeof values.iD !== 'number') {
      values.iD = Number(values.iD);
    }

    const entity = {
      ...setMappingsEntity,
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
          ...setMappingsEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dboApp.setMappings.home.createOrEditLabel" data-cy="SetMappingsCreateUpdateHeading">
            <Translate contentKey="dboApp.setMappings.home.createOrEditLabel">Create or edit a SetMappings</Translate>
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
                  id="set-mappings-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('dboApp.setMappings.iD')} id="set-mappings-iD" name="iD" data-cy="iD" type="text" />
              <ValidatedField
                label={translate('dboApp.setMappings.oneWorldValue')}
                id="set-mappings-oneWorldValue"
                name="oneWorldValue"
                data-cy="oneWorldValue"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.setMappings.productValue')}
                id="set-mappings-productValue"
                name="productValue"
                data-cy="productValue"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.setMappings.setName')}
                id="set-mappings-setName"
                name="setName"
                data-cy="setName"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/set-mappings" replace color="info">
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

export default SetMappingsUpdate;
