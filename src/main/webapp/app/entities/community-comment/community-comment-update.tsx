import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './community-comment.reducer';

export const CommunityCommentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const communityCommentEntity = useAppSelector(state => state.communityComment.entity);
  const loading = useAppSelector(state => state.communityComment.loading);
  const updating = useAppSelector(state => state.communityComment.updating);
  const updateSuccess = useAppSelector(state => state.communityComment.updateSuccess);

  const handleClose = () => {
    navigate('/community-comment');
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
    if (values.commentByUserId !== undefined && typeof values.commentByUserId !== 'number') {
      values.commentByUserId = Number(values.commentByUserId);
    }

    const entity = {
      ...communityCommentEntity,
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
          ...communityCommentEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dboApp.communityComment.home.createOrEditLabel" data-cy="CommunityCommentCreateUpdateHeading">
            <Translate contentKey="dboApp.communityComment.home.createOrEditLabel">Create or edit a CommunityComment</Translate>
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
                  id="community-comment-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dboApp.communityComment.comment')}
                id="community-comment-comment"
                name="comment"
                data-cy="comment"
                type="textarea"
              />
              <ValidatedField
                label={translate('dboApp.communityComment.commentByUserId')}
                id="community-comment-commentByUserId"
                name="commentByUserId"
                data-cy="commentByUserId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.communityComment.communityCommentId')}
                id="community-comment-communityCommentId"
                name="communityCommentId"
                data-cy="communityCommentId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.communityComment.communityPostId')}
                id="community-comment-communityPostId"
                name="communityPostId"
                data-cy="communityPostId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.communityComment.createdOn')}
                id="community-comment-createdOn"
                name="createdOn"
                data-cy="createdOn"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.communityComment.isActive')}
                id="community-comment-isActive"
                name="isActive"
                data-cy="isActive"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('dboApp.communityComment.lastUpdatedOn')}
                id="community-comment-lastUpdatedOn"
                name="lastUpdatedOn"
                data-cy="lastUpdatedOn"
                type="date"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/community-comment" replace color="info">
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

export default CommunityCommentUpdate;
