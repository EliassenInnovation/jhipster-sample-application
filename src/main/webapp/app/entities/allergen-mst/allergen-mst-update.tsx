import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './allergen-mst.reducer';

export const AllergenMstUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const allergenMstEntity = useAppSelector(state => state.allergenMst.entity);
  const loading = useAppSelector(state => state.allergenMst.loading);
  const updating = useAppSelector(state => state.allergenMst.updating);
  const updateSuccess = useAppSelector(state => state.allergenMst.updateSuccess);

  const handleClose = () => {
    navigate('/allergen-mst');
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

    const entity = {
      ...allergenMstEntity,
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
          ...allergenMstEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dboApp.allergenMst.home.createOrEditLabel" data-cy="AllergenMstCreateUpdateHeading">
            <Translate contentKey="dboApp.allergenMst.home.createOrEditLabel">Create or edit a AllergenMst</Translate>
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
                  id="allergen-mst-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dboApp.allergenMst.allergenGroup')}
                id="allergen-mst-allergenGroup"
                name="allergenGroup"
                data-cy="allergenGroup"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.allergenMst.allergenId')}
                id="allergen-mst-allergenId"
                name="allergenId"
                data-cy="allergenId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.allergenMst.allergenName')}
                id="allergen-mst-allergenName"
                name="allergenName"
                data-cy="allergenName"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/allergen-mst" replace color="info">
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

export default AllergenMstUpdate;
