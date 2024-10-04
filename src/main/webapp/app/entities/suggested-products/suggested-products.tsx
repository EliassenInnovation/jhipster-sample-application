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

import { getEntities, searchEntities } from './suggested-products.reducer';

export const SuggestedProducts = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [search, setSearch] = useState('');
  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const suggestedProductsList = useAppSelector(state => state.suggestedProducts.entities);
  const loading = useAppSelector(state => state.suggestedProducts.loading);

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
      <h2 id="suggested-products-heading" data-cy="SuggestedProductsHeading">
        <Translate contentKey="dboApp.suggestedProducts.home.title">Suggested Products</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="dboApp.suggestedProducts.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/suggested-products/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="dboApp.suggestedProducts.home.createLabel">Create new Suggested Products</Translate>
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
                  placeholder={translate('dboApp.suggestedProducts.home.search')}
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
        {suggestedProductsList && suggestedProductsList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="dboApp.suggestedProducts.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('isActive')}>
                  <Translate contentKey="dboApp.suggestedProducts.isActive">Is Active</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isActive')} />
                </th>
                <th className="hand" onClick={sort('isApprove')}>
                  <Translate contentKey="dboApp.suggestedProducts.isApprove">Is Approve</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isApprove')} />
                </th>
                <th className="hand" onClick={sort('productId')}>
                  <Translate contentKey="dboApp.suggestedProducts.productId">Product Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('productId')} />
                </th>
                <th className="hand" onClick={sort('suggestedByDistrict')}>
                  <Translate contentKey="dboApp.suggestedProducts.suggestedByDistrict">Suggested By District</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('suggestedByDistrict')} />
                </th>
                <th className="hand" onClick={sort('suggestedByUserId')}>
                  <Translate contentKey="dboApp.suggestedProducts.suggestedByUserId">Suggested By User Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('suggestedByUserId')} />
                </th>
                <th className="hand" onClick={sort('suggestedProductId')}>
                  <Translate contentKey="dboApp.suggestedProducts.suggestedProductId">Suggested Product Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('suggestedProductId')} />
                </th>
                <th className="hand" onClick={sort('suggestionDate')}>
                  <Translate contentKey="dboApp.suggestedProducts.suggestionDate">Suggestion Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('suggestionDate')} />
                </th>
                <th className="hand" onClick={sort('suggestionId')}>
                  <Translate contentKey="dboApp.suggestedProducts.suggestionId">Suggestion Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('suggestionId')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {suggestedProductsList.map((suggestedProducts, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/suggested-products/${suggestedProducts.id}`} color="link" size="sm">
                      {suggestedProducts.id}
                    </Button>
                  </td>
                  <td>{suggestedProducts.isActive ? 'true' : 'false'}</td>
                  <td>{suggestedProducts.isApprove ? 'true' : 'false'}</td>
                  <td>{suggestedProducts.productId}</td>
                  <td>{suggestedProducts.suggestedByDistrict}</td>
                  <td>{suggestedProducts.suggestedByUserId}</td>
                  <td>{suggestedProducts.suggestedProductId}</td>
                  <td>
                    {suggestedProducts.suggestionDate ? (
                      <TextFormat type="date" value={suggestedProducts.suggestionDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{suggestedProducts.suggestionId}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/suggested-products/${suggestedProducts.id}`}
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
                        to={`/suggested-products/${suggestedProducts.id}/edit`}
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
                        onClick={() => (window.location.href = `/suggested-products/${suggestedProducts.id}/delete`)}
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
              <Translate contentKey="dboApp.suggestedProducts.home.notFound">No Suggested Products found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default SuggestedProducts;
