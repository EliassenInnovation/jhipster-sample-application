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

import { getEntities, searchEntities } from './product-change-history.reducer';

export const ProductChangeHistory = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [search, setSearch] = useState('');
  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const productChangeHistoryList = useAppSelector(state => state.productChangeHistory.entities);
  const loading = useAppSelector(state => state.productChangeHistory.loading);

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
      <h2 id="product-change-history-heading" data-cy="ProductChangeHistoryHeading">
        <Translate contentKey="dboApp.productChangeHistory.home.title">Product Change Histories</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="dboApp.productChangeHistory.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/product-change-history/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="dboApp.productChangeHistory.home.createLabel">Create new Product Change History</Translate>
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
                  placeholder={translate('dboApp.productChangeHistory.home.search')}
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
        {productChangeHistoryList && productChangeHistoryList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="dboApp.productChangeHistory.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="dboApp.productChangeHistory.createdBy">Created By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                </th>
                <th className="hand" onClick={sort('dateCreated')}>
                  <Translate contentKey="dboApp.productChangeHistory.dateCreated">Date Created</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('dateCreated')} />
                </th>
                <th className="hand" onClick={sort('historyId')}>
                  <Translate contentKey="dboApp.productChangeHistory.historyId">History Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('historyId')} />
                </th>
                <th className="hand" onClick={sort('iocCategoryId')}>
                  <Translate contentKey="dboApp.productChangeHistory.iocCategoryId">Ioc Category Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('iocCategoryId')} />
                </th>
                <th className="hand" onClick={sort('isActive')}>
                  <Translate contentKey="dboApp.productChangeHistory.isActive">Is Active</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isActive')} />
                </th>
                <th className="hand" onClick={sort('productId')}>
                  <Translate contentKey="dboApp.productChangeHistory.productId">Product Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('productId')} />
                </th>
                <th className="hand" onClick={sort('schoolDistrictId')}>
                  <Translate contentKey="dboApp.productChangeHistory.schoolDistrictId">School District Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('schoolDistrictId')} />
                </th>
                <th className="hand" onClick={sort('selectionType')}>
                  <Translate contentKey="dboApp.productChangeHistory.selectionType">Selection Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('selectionType')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {productChangeHistoryList.map((productChangeHistory, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/product-change-history/${productChangeHistory.id}`} color="link" size="sm">
                      {productChangeHistory.id}
                    </Button>
                  </td>
                  <td>{productChangeHistory.createdBy}</td>
                  <td>
                    {productChangeHistory.dateCreated ? (
                      <TextFormat type="date" value={productChangeHistory.dateCreated} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{productChangeHistory.historyId}</td>
                  <td>{productChangeHistory.iocCategoryId}</td>
                  <td>{productChangeHistory.isActive ? 'true' : 'false'}</td>
                  <td>{productChangeHistory.productId}</td>
                  <td>{productChangeHistory.schoolDistrictId}</td>
                  <td>{productChangeHistory.selectionType}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/product-change-history/${productChangeHistory.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/product-change-history/${productChangeHistory.id}/edit`}
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
                        onClick={() => (window.location.href = `/product-change-history/${productChangeHistory.id}/delete`)}
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
              <Translate contentKey="dboApp.productChangeHistory.home.notFound">No Product Change Histories found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default ProductChangeHistory;
