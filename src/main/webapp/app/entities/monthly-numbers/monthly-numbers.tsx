import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Col, Form, FormGroup, Input, InputGroup, Row, Table } from 'reactstrap';
import { TextFormat, Translate, getSortState, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities, searchEntities } from './monthly-numbers.reducer';

export const MonthlyNumbers = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [search, setSearch] = useState('');
  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const monthlyNumbersList = useAppSelector(state => state.monthlyNumbers.entities);
  const loading = useAppSelector(state => state.monthlyNumbers.loading);

  const getAllEntities = () => {
    if (search) {
      dispatch(
        searchEntities({
          query: search,
          sort: `${sortState.sort},${sortState.order}`,
        }),
      );
    } else {
      dispatch(
        getEntities({
          sort: `${sortState.sort},${sortState.order}`,
        }),
      );
    }
  };

  const startSearching = e => {
    if (search) {
      dispatch(
        searchEntities({
          query: search,
          sort: `${sortState.sort},${sortState.order}`,
        }),
      );
    }
    e.preventDefault();
  };

  const clear = () => {
    setSearch('');
    dispatch(getEntities({}));
  };

  const handleSearch = event => setSearch(event.target.value);

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort, search]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="monthly-numbers-heading" data-cy="MonthlyNumbersHeading">
        <Translate contentKey="dboApp.monthlyNumbers.home.title">Monthly Numbers</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="dboApp.monthlyNumbers.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/monthly-numbers/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="dboApp.monthlyNumbers.home.createLabel">Create new Monthly Numbers</Translate>
          </Link>
        </div>
      </h2>
      <Row>
        <Col sm="12">
          <Form onSubmit={startSearching}>
            <FormGroup>
              <InputGroup>
                <Input
                  type="text"
                  name="search"
                  defaultValue={search}
                  onChange={handleSearch}
                  placeholder={translate('dboApp.monthlyNumbers.home.search')}
                />
                <Button className="input-group-addon">
                  <FontAwesomeIcon icon="search" />
                </Button>
                <Button type="reset" className="input-group-addon" onClick={clear}>
                  <FontAwesomeIcon icon="trash" />
                </Button>
              </InputGroup>
            </FormGroup>
          </Form>
        </Col>
      </Row>
      <div className="table-responsive">
        {monthlyNumbersList && monthlyNumbersList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="dboApp.monthlyNumbers.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('actualMonthId')}>
                  <Translate contentKey="dboApp.monthlyNumbers.actualMonthId">Actual Month Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('actualMonthId')} />
                </th>
                <th className="hand" onClick={sort('createdOn')}>
                  <Translate contentKey="dboApp.monthlyNumbers.createdOn">Created On</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdOn')} />
                </th>
                <th className="hand" onClick={sort('enrollment')}>
                  <Translate contentKey="dboApp.monthlyNumbers.enrollment">Enrollment</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('enrollment')} />
                </th>
                <th className="hand" onClick={sort('freeAndReducedPercent')}>
                  <Translate contentKey="dboApp.monthlyNumbers.freeAndReducedPercent">Free And Reduced Percent</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('freeAndReducedPercent')} />
                </th>
                <th className="hand" onClick={sort('iD')}>
                  <Translate contentKey="dboApp.monthlyNumbers.iD">I D</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('iD')} />
                </th>
                <th className="hand" onClick={sort('isActive')}>
                  <Translate contentKey="dboApp.monthlyNumbers.isActive">Is Active</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isActive')} />
                </th>
                <th className="hand" onClick={sort('mealsServed')}>
                  <Translate contentKey="dboApp.monthlyNumbers.mealsServed">Meals Served</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('mealsServed')} />
                </th>
                <th className="hand" onClick={sort('modifiedOn')}>
                  <Translate contentKey="dboApp.monthlyNumbers.modifiedOn">Modified On</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('modifiedOn')} />
                </th>
                <th className="hand" onClick={sort('monthId')}>
                  <Translate contentKey="dboApp.monthlyNumbers.monthId">Month Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('monthId')} />
                </th>
                <th className="hand" onClick={sort('numberOfDistricts')}>
                  <Translate contentKey="dboApp.monthlyNumbers.numberOfDistricts">Number Of Districts</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('numberOfDistricts')} />
                </th>
                <th className="hand" onClick={sort('numberOfSites')}>
                  <Translate contentKey="dboApp.monthlyNumbers.numberOfSites">Number Of Sites</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('numberOfSites')} />
                </th>
                <th className="hand" onClick={sort('regDate')}>
                  <Translate contentKey="dboApp.monthlyNumbers.regDate">Reg Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('regDate')} />
                </th>
                <th className="hand" onClick={sort('schoolDistrictId')}>
                  <Translate contentKey="dboApp.monthlyNumbers.schoolDistrictId">School District Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('schoolDistrictId')} />
                </th>
                <th className="hand" onClick={sort('year')}>
                  <Translate contentKey="dboApp.monthlyNumbers.year">Year</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('year')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {monthlyNumbersList.map((monthlyNumbers, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/monthly-numbers/${monthlyNumbers.id}`} color="link" size="sm">
                      {monthlyNumbers.id}
                    </Button>
                  </td>
                  <td>{monthlyNumbers.actualMonthId}</td>
                  <td>
                    {monthlyNumbers.createdOn ? (
                      <TextFormat type="date" value={monthlyNumbers.createdOn} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{monthlyNumbers.enrollment}</td>
                  <td>{monthlyNumbers.freeAndReducedPercent}</td>
                  <td>{monthlyNumbers.iD}</td>
                  <td>{monthlyNumbers.isActive ? 'true' : 'false'}</td>
                  <td>{monthlyNumbers.mealsServed}</td>
                  <td>
                    {monthlyNumbers.modifiedOn ? (
                      <TextFormat type="date" value={monthlyNumbers.modifiedOn} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{monthlyNumbers.monthId}</td>
                  <td>{monthlyNumbers.numberOfDistricts}</td>
                  <td>{monthlyNumbers.numberOfSites}</td>
                  <td>
                    {monthlyNumbers.regDate ? (
                      <TextFormat type="date" value={monthlyNumbers.regDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{monthlyNumbers.schoolDistrictId}</td>
                  <td>{monthlyNumbers.year}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/monthly-numbers/${monthlyNumbers.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/monthly-numbers/${monthlyNumbers.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/monthly-numbers/${monthlyNumbers.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="dboApp.monthlyNumbers.home.notFound">No Monthly Numbers found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default MonthlyNumbers;
