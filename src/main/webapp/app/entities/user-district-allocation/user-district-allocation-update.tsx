import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './user-district-allocation.reducer';

export const UserDistrictAllocationUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userDistrictAllocationEntity = useAppSelector(state => state.userDistrictAllocation.entity);
  const loading = useAppSelector(state => state.userDistrictAllocation.loading);
  const updating = useAppSelector(state => state.userDistrictAllocation.updating);
  const updateSuccess = useAppSelector(state => state.userDistrictAllocation.updateSuccess);

  const handleClose = () => {
    navigate('/user-district-allocation');
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
    if (values.schoolDistrictId !== undefined && typeof values.schoolDistrictId !== 'number') {
      values.schoolDistrictId = Number(values.schoolDistrictId);
    }
    if (values.updatedBy !== undefined && typeof values.updatedBy !== 'number') {
      values.updatedBy = Number(values.updatedBy);
    }
    if (values.userId !== undefined && typeof values.userId !== 'number') {
      values.userId = Number(values.userId);
    }

    const entity = {
      ...userDistrictAllocationEntity,
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
          ...userDistrictAllocationEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dboApp.userDistrictAllocation.home.createOrEditLabel" data-cy="UserDistrictAllocationCreateUpdateHeading">
            <Translate contentKey="dboApp.userDistrictAllocation.home.createOrEditLabel">Create or edit a UserDistrictAllocation</Translate>
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
                  id="user-district-allocation-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dboApp.userDistrictAllocation.createdBy')}
                id="user-district-allocation-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.userDistrictAllocation.createdOn')}
                id="user-district-allocation-createdOn"
                name="createdOn"
                data-cy="createdOn"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.userDistrictAllocation.isAllocated')}
                id="user-district-allocation-isAllocated"
                name="isAllocated"
                data-cy="isAllocated"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('dboApp.userDistrictAllocation.schoolDistrictId')}
                id="user-district-allocation-schoolDistrictId"
                name="schoolDistrictId"
                data-cy="schoolDistrictId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.userDistrictAllocation.updatedBy')}
                id="user-district-allocation-updatedBy"
                name="updatedBy"
                data-cy="updatedBy"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.userDistrictAllocation.updatedOn')}
                id="user-district-allocation-updatedOn"
                name="updatedOn"
                data-cy="updatedOn"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.userDistrictAllocation.userDistrictAllocationId')}
                id="user-district-allocation-userDistrictAllocationId"
                name="userDistrictAllocationId"
                data-cy="userDistrictAllocationId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.userDistrictAllocation.userId')}
                id="user-district-allocation-userId"
                name="userId"
                data-cy="userId"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/user-district-allocation" replace color="info">
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

export default UserDistrictAllocationUpdate;
