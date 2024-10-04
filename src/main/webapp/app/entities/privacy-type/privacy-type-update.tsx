import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './privacy-type.reducer';

export const PrivacyTypeUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const privacyTypeEntity = useAppSelector(state => state.privacyType.entity);
  const loading = useAppSelector(state => state.privacyType.loading);
  const updating = useAppSelector(state => state.privacyType.updating);
  const updateSuccess = useAppSelector(state => state.privacyType.updateSuccess);

  const handleClose = () => {
    navigate('/privacy-type');
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
    if (values.privacyTypeId !== undefined && typeof values.privacyTypeId !== 'number') {
      values.privacyTypeId = Number(values.privacyTypeId);
    }

    const entity = {
      ...privacyTypeEntity,
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
          ...privacyTypeEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dboApp.privacyType.home.createOrEditLabel" data-cy="PrivacyTypeCreateUpdateHeading">
            <Translate contentKey="dboApp.privacyType.home.createOrEditLabel">Create or edit a PrivacyType</Translate>
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
                  id="privacy-type-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dboApp.privacyType.privacyTypeId')}
                id="privacy-type-privacyTypeId"
                name="privacyTypeId"
                data-cy="privacyTypeId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.privacyType.privacyTypeName')}
                id="privacy-type-privacyTypeName"
                name="privacyTypeName"
                data-cy="privacyTypeName"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/privacy-type" replace color="info">
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

export default PrivacyTypeUpdate;
