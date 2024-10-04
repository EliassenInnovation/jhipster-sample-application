import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './community-post.reducer';

export const CommunityPostUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const communityPostEntity = useAppSelector(state => state.communityPost.entity);
  const loading = useAppSelector(state => state.communityPost.loading);
  const updating = useAppSelector(state => state.communityPost.updating);
  const updateSuccess = useAppSelector(state => state.communityPost.updateSuccess);

  const handleClose = () => {
    navigate('/community-post');
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
    if (values.lastUpdatedBy !== undefined && typeof values.lastUpdatedBy !== 'number') {
      values.lastUpdatedBy = Number(values.lastUpdatedBy);
    }
    if (values.postTypeId !== undefined && typeof values.postTypeId !== 'number') {
      values.postTypeId = Number(values.postTypeId);
    }
    if (values.privacyTypeId !== undefined && typeof values.privacyTypeId !== 'number') {
      values.privacyTypeId = Number(values.privacyTypeId);
    }
    if (values.schoolDistrictId !== undefined && typeof values.schoolDistrictId !== 'number') {
      values.schoolDistrictId = Number(values.schoolDistrictId);
    }
    if (values.userId !== undefined && typeof values.userId !== 'number') {
      values.userId = Number(values.userId);
    }

    const entity = {
      ...communityPostEntity,
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
          ...communityPostEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dboApp.communityPost.home.createOrEditLabel" data-cy="CommunityPostCreateUpdateHeading">
            <Translate contentKey="dboApp.communityPost.home.createOrEditLabel">Create or edit a CommunityPost</Translate>
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
                  id="community-post-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dboApp.communityPost.communityPostId')}
                id="community-post-communityPostId"
                name="communityPostId"
                data-cy="communityPostId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.communityPost.createdBy')}
                id="community-post-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.communityPost.createdOn')}
                id="community-post-createdOn"
                name="createdOn"
                data-cy="createdOn"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.communityPost.date')}
                id="community-post-date"
                name="date"
                data-cy="date"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.communityPost.description')}
                id="community-post-description"
                name="description"
                data-cy="description"
                type="textarea"
              />
              <ValidatedField
                label={translate('dboApp.communityPost.isActive')}
                id="community-post-isActive"
                name="isActive"
                data-cy="isActive"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('dboApp.communityPost.lastUpdatedBy')}
                id="community-post-lastUpdatedBy"
                name="lastUpdatedBy"
                data-cy="lastUpdatedBy"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.communityPost.lastUpdatedOn')}
                id="community-post-lastUpdatedOn"
                name="lastUpdatedOn"
                data-cy="lastUpdatedOn"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.communityPost.postTypeId')}
                id="community-post-postTypeId"
                name="postTypeId"
                data-cy="postTypeId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.communityPost.privacyTypeId')}
                id="community-post-privacyTypeId"
                name="privacyTypeId"
                data-cy="privacyTypeId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.communityPost.schoolDistrictId')}
                id="community-post-schoolDistrictId"
                name="schoolDistrictId"
                data-cy="schoolDistrictId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.communityPost.userId')}
                id="community-post-userId"
                name="userId"
                data-cy="userId"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/community-post" replace color="info">
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

export default CommunityPostUpdate;
