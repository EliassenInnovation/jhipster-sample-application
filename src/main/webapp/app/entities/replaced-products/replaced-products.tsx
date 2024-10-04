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

import { getEntities, searchEntities } from './replaced-products.reducer';

export const ReplacedProducts = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [search, setSearch] = useState('');
  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const replacedProductsList = useAppSelector(state => state.replacedProducts.entities);
  const loading = useAppSelector(state => state.replacedProducts.loading);

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
      <h2 id="replaced-products-heading" data-cy="ReplacedProductsHeading">
        <Translate contentKey="dboApp.replacedProducts.home.title">Replaced Products</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="dboApp.replacedProducts.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/replaced-products/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="dboApp.replacedProducts.home.createLabel">Create new Replaced Products</Translate>
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
                  placeholder={translate('dboApp.replacedProducts.home.search')}
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
        {replacedProductsList && replacedProductsList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="dboApp.replacedProducts.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('isActive')}>
                  <Translate contentKey="dboApp.replacedProducts.isActive">Is Active</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isActive')} />
                </th>
                <th className="hand" onClick={sort('productId')}>
                  <Translate contentKey="dboApp.replacedProducts.productId">Product Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('productId')} />
                </th>
                <th className="hand" onClick={sort('replacedByUserId')}>
                  <Translate contentKey="dboApp.replacedProducts.replacedByUserId">Replaced By User Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('replacedByUserId')} />
                </th>
                <th className="hand" onClick={sort('replacedId')}>
                  <Translate contentKey="dboApp.replacedProducts.replacedId">Replaced Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('replacedId')} />
                </th>
                <th className="hand" onClick={sort('replacedProductId')}>
                  <Translate contentKey="dboApp.replacedProducts.replacedProductId">Replaced Product Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('replacedProductId')} />
                </th>
                <th className="hand" onClick={sort('replacementDate')}>
                  <Translate contentKey="dboApp.replacedProducts.replacementDate">Replacement Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('replacementDate')} />
                </th>
                <th className="hand" onClick={sort('schoolDistrictId')}>
                  <Translate contentKey="dboApp.replacedProducts.schoolDistrictId">School District Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('schoolDistrictId')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {replacedProductsList.map((replacedProducts, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/replaced-products/${replacedProducts.id}`} color="link" size="sm">
                      {replacedProducts.id}
                    </Button>
                  </td>
                  <td>{replacedProducts.isActive ? 'true' : 'false'}</td>
                  <td>{replacedProducts.productId}</td>
                  <td>{replacedProducts.replacedByUserId}</td>
                  <td>{replacedProducts.replacedId}</td>
                  <td>{replacedProducts.replacedProductId}</td>
                  <td>
                    {replacedProducts.replacementDate ? (
                      <TextFormat type="date" value={replacedProducts.replacementDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{replacedProducts.schoolDistrictId}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/replaced-products/${replacedProducts.id}`}
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
                        to={`/replaced-products/${replacedProducts.id}/edit`}
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
                        onClick={() => (window.location.href = `/replaced-products/${replacedProducts.id}/delete`)}
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
              <Translate contentKey="dboApp.replacedProducts.home.notFound">No Replaced Products found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default ReplacedProducts;
