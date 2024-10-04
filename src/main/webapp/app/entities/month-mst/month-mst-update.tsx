import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './month-mst.reducer';

export const MonthMstUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const monthMstEntity = useAppSelector(state => state.monthMst.entity);
  const loading = useAppSelector(state => state.monthMst.loading);
  const updating = useAppSelector(state => state.monthMst.updating);
  const updateSuccess = useAppSelector(state => state.monthMst.updateSuccess);

  const handleClose = () => {
    navigate('/month-mst');
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
    if (values.monthID !== undefined && typeof values.monthID !== 'number') {
      values.monthID = Number(values.monthID);
    }

    const entity = {
      ...monthMstEntity,
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
          ...monthMstEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dboApp.monthMst.home.createOrEditLabel" data-cy="MonthMstCreateUpdateHeading">
            <Translate contentKey="dboApp.monthMst.home.createOrEditLabel">Create or edit a MonthMst</Translate>
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
                  id="month-mst-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dboApp.monthMst.isActive')}
                id="month-mst-isActive"
                name="isActive"
                data-cy="isActive"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('dboApp.monthMst.monthID')}
                id="month-mst-monthID"
                name="monthID"
                data-cy="monthID"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.monthMst.monthName')}
                id="month-mst-monthName"
                name="monthName"
                data-cy="monthName"
                type="text"
              />
              <ValidatedField label={translate('dboApp.monthMst.year')} id="month-mst-year" name="year" data-cy="year" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/month-mst" replace color="info">
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

export default MonthMstUpdate;
