import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './community-like-mst.reducer';

export const CommunityLikeMstUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const communityLikeMstEntity = useAppSelector(state => state.communityLikeMst.entity);
  const loading = useAppSelector(state => state.communityLikeMst.loading);
  const updating = useAppSelector(state => state.communityLikeMst.updating);
  const updateSuccess = useAppSelector(state => state.communityLikeMst.updateSuccess);

  const handleClose = () => {
    navigate('/community-like-mst');
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
    if (values.likedByUserId !== undefined && typeof values.likedByUserId !== 'number') {
      values.likedByUserId = Number(values.likedByUserId);
    }

    const entity = {
      ...communityLikeMstEntity,
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
          ...communityLikeMstEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dboApp.communityLikeMst.home.createOrEditLabel" data-cy="CommunityLikeMstCreateUpdateHeading">
            <Translate contentKey="dboApp.communityLikeMst.home.createOrEditLabel">Create or edit a CommunityLikeMst</Translate>
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
                  id="community-like-mst-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dboApp.communityLikeMst.communityLikeId')}
                id="community-like-mst-communityLikeId"
                name="communityLikeId"
                data-cy="communityLikeId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.communityLikeMst.communityPostId')}
                id="community-like-mst-communityPostId"
                name="communityPostId"
                data-cy="communityPostId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.communityLikeMst.createdOn')}
                id="community-like-mst-createdOn"
                name="createdOn"
                data-cy="createdOn"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.communityLikeMst.isActive')}
                id="community-like-mst-isActive"
                name="isActive"
                data-cy="isActive"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('dboApp.communityLikeMst.isLiked')}
                id="community-like-mst-isLiked"
                name="isLiked"
                data-cy="isLiked"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('dboApp.communityLikeMst.likedByUserId')}
                id="community-like-mst-likedByUserId"
                name="likedByUserId"
                data-cy="likedByUserId"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/community-like-mst" replace color="info">
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

export default CommunityLikeMstUpdate;
