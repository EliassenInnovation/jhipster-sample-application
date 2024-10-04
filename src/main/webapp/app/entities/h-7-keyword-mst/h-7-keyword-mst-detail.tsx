import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './h-7-keyword-mst.reducer';

export const H7KeywordMstDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const h7KeywordMstEntity = useAppSelector(state => state.h7KeywordMst.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="h7KeywordMstDetailsHeading">
          <Translate contentKey="dboApp.h7KeywordMst.detail.title">H7KeywordMst</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{h7KeywordMstEntity.id}</dd>
          <dt>
            <span id="h7Group">
              <Translate contentKey="dboApp.h7KeywordMst.h7Group">H 7 Group</Translate>
            </span>
          </dt>
          <dd>{h7KeywordMstEntity.h7Group}</dd>
          <dt>
            <span id="h7Keyword">
              <Translate contentKey="dboApp.h7KeywordMst.h7Keyword">H 7 Keyword</Translate>
            </span>
          </dt>
          <dd>{h7KeywordMstEntity.h7Keyword}</dd>
          <dt>
            <span id="h7keywordId">
              <Translate contentKey="dboApp.h7KeywordMst.h7keywordId">H 7 Keyword Id</Translate>
            </span>
          </dt>
          <dd>{h7KeywordMstEntity.h7keywordId}</dd>
          <dt>
            <span id="iocGroup">
              <Translate contentKey="dboApp.h7KeywordMst.iocGroup">Ioc Group</Translate>
            </span>
          </dt>
          <dd>{h7KeywordMstEntity.iocGroup}</dd>
        </dl>
        <Button tag={Link} to="/h-7-keyword-mst" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/h-7-keyword-mst/${h7KeywordMstEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default H7KeywordMstDetail;
