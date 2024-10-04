import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Col, Form, FormGroup, Input, InputGroup, Row, Table } from 'reactstrap';
import { Translate, getSortState, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities, searchEntities } from './product-h-7-old.reducer';

export const ProductH7Old = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [search, setSearch] = useState('');
  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const productH7OldList = useAppSelector(state => state.productH7Old.entities);
  const loading = useAppSelector(state => state.productH7Old.loading);

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
      <h2 id="product-h-7-old-heading" data-cy="ProductH7OldHeading">
        <Translate contentKey="dboApp.productH7Old.home.title">Product H 7 Olds</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="dboApp.productH7Old.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/product-h-7-old/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="dboApp.productH7Old.home.createLabel">Create new Product H 7 Old</Translate>
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
                  placeholder={translate('dboApp.productH7Old.home.search')}
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
        {productH7OldList && productH7OldList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="dboApp.productH7Old.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('gtinUpc')}>
                  <Translate contentKey="dboApp.productH7Old.gtinUpc">Gtin Upc</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('gtinUpc')} />
                </th>
                <th className="hand" onClick={sort('h7KeywordId')}>
                  <Translate contentKey="dboApp.productH7Old.h7KeywordId">H 7 Keyword Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('h7KeywordId')} />
                </th>
                <th className="hand" onClick={sort('iOCGroup')}>
                  <Translate contentKey="dboApp.productH7Old.iOCGroup">I OC Group</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('iOCGroup')} />
                </th>
                <th className="hand" onClick={sort('productH7Id')}>
                  <Translate contentKey="dboApp.productH7Old.productH7Id">Product H 7 Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('productH7Id')} />
                </th>
                <th className="hand" onClick={sort('productId')}>
                  <Translate contentKey="dboApp.productH7Old.productId">Product Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('productId')} />
                </th>
                <th className="hand" onClick={sort('productName')}>
                  <Translate contentKey="dboApp.productH7Old.productName">Product Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('productName')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {productH7OldList.map((productH7Old, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/product-h-7-old/${productH7Old.id}`} color="link" size="sm">
                      {productH7Old.id}
                    </Button>
                  </td>
                  <td>{productH7Old.gtinUpc}</td>
                  <td>{productH7Old.h7KeywordId}</td>
                  <td>{productH7Old.iOCGroup}</td>
                  <td>{productH7Old.productH7Id}</td>
                  <td>{productH7Old.productId}</td>
                  <td>{productH7Old.productName}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/product-h-7-old/${productH7Old.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/product-h-7-old/${productH7Old.id}/edit`}
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
                        onClick={() => (window.location.href = `/product-h-7-old/${productH7Old.id}/delete`)}
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
              <Translate contentKey="dboApp.productH7Old.home.notFound">No Product H 7 Olds found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default ProductH7Old;
