import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './monthly-numbers.reducer';

export const MonthlyNumbersDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const monthlyNumbersEntity = useAppSelector(state => state.monthlyNumbers.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="monthlyNumbersDetailsHeading">
          <Translate contentKey="dboApp.monthlyNumbers.detail.title">MonthlyNumbers</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{monthlyNumbersEntity.id}</dd>
          <dt>
            <span id="actualMonthId">
              <Translate contentKey="dboApp.monthlyNumbers.actualMonthId">Actual Month Id</Translate>
            </span>
          </dt>
          <dd>{monthlyNumbersEntity.actualMonthId}</dd>
          <dt>
            <span id="createdOn">
              <Translate contentKey="dboApp.monthlyNumbers.createdOn">Created On</Translate>
            </span>
          </dt>
          <dd>
            {monthlyNumbersEntity.createdOn ? (
              <TextFormat value={monthlyNumbersEntity.createdOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="enrollment">
              <Translate contentKey="dboApp.monthlyNumbers.enrollment">Enrollment</Translate>
            </span>
          </dt>
          <dd>{monthlyNumbersEntity.enrollment}</dd>
          <dt>
            <span id="freeAndReducedPercent">
              <Translate contentKey="dboApp.monthlyNumbers.freeAndReducedPercent">Free And Reduced Percent</Translate>
            </span>
          </dt>
          <dd>{monthlyNumbersEntity.freeAndReducedPercent}</dd>
          <dt>
            <span id="iD">
              <Translate contentKey="dboApp.monthlyNumbers.iD">I D</Translate>
            </span>
          </dt>
          <dd>{monthlyNumbersEntity.iD}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="dboApp.monthlyNumbers.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{monthlyNumbersEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="mealsServed">
              <Translate contentKey="dboApp.monthlyNumbers.mealsServed">Meals Served</Translate>
            </span>
          </dt>
          <dd>{monthlyNumbersEntity.mealsServed}</dd>
          <dt>
            <span id="modifiedOn">
              <Translate contentKey="dboApp.monthlyNumbers.modifiedOn">Modified On</Translate>
            </span>
          </dt>
          <dd>
            {monthlyNumbersEntity.modifiedOn ? (
              <TextFormat value={monthlyNumbersEntity.modifiedOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="monthId">
              <Translate contentKey="dboApp.monthlyNumbers.monthId">Month Id</Translate>
            </span>
          </dt>
          <dd>{monthlyNumbersEntity.monthId}</dd>
          <dt>
            <span id="numberOfDistricts">
              <Translate contentKey="dboApp.monthlyNumbers.numberOfDistricts">Number Of Districts</Translate>
            </span>
          </dt>
          <dd>{monthlyNumbersEntity.numberOfDistricts}</dd>
          <dt>
            <span id="numberOfSites">
              <Translate contentKey="dboApp.monthlyNumbers.numberOfSites">Number Of Sites</Translate>
            </span>
          </dt>
          <dd>{monthlyNumbersEntity.numberOfSites}</dd>
          <dt>
            <span id="regDate">
              <Translate contentKey="dboApp.monthlyNumbers.regDate">Reg Date</Translate>
            </span>
          </dt>
          <dd>
            {monthlyNumbersEntity.regDate ? (
              <TextFormat value={monthlyNumbersEntity.regDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="schoolDistrictId">
              <Translate contentKey="dboApp.monthlyNumbers.schoolDistrictId">School District Id</Translate>
            </span>
          </dt>
          <dd>{monthlyNumbersEntity.schoolDistrictId}</dd>
          <dt>
            <span id="year">
              <Translate contentKey="dboApp.monthlyNumbers.year">Year</Translate>
            </span>
          </dt>
          <dd>{monthlyNumbersEntity.year}</dd>
        </dl>
        <Button tag={Link} to="/monthly-numbers" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/monthly-numbers/${monthlyNumbersEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MonthlyNumbersDetail;
