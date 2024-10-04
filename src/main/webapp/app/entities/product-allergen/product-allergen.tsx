import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Col, Form, FormGroup, Input, InputGroup, Row, Table } from 'reactstrap';
import { Translate, getSortState, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities, searchEntities } from './product-allergen.reducer';

export const ProductAllergen = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [search, setSearch] = useState('');
  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const productAllergenList = useAppSelector(state => state.productAllergen.entities);
  const loading = useAppSelector(state => state.productAllergen.loading);

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
      <h2 id="product-allergen-heading" data-cy="ProductAllergenHeading">
        <Translate contentKey="dboApp.productAllergen.home.title">Product Allergens</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="dboApp.productAllergen.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/product-allergen/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="dboApp.productAllergen.home.createLabel">Create new Product Allergen</Translate>
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
                  placeholder={translate('dboApp.productAllergen.home.search')}
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
        {productAllergenList && productAllergenList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="dboApp.productAllergen.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('allergenId')}>
                  <Translate contentKey="dboApp.productAllergen.allergenId">Allergen Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('allergenId')} />
                </th>
                <th className="hand" onClick={sort('allergenGroup')}>
                  <Translate contentKey="dboApp.productAllergen.allergenGroup">Allergen Group</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('allergenGroup')} />
                </th>
                <th className="hand" onClick={sort('allergenName')}>
                  <Translate contentKey="dboApp.productAllergen.allergenName">Allergen Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('allergenName')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="dboApp.productAllergen.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th className="hand" onClick={sort('gTIN')}>
                  <Translate contentKey="dboApp.productAllergen.gTIN">G TIN</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('gTIN')} />
                </th>
                <th className="hand" onClick={sort('gTINUPC')}>
                  <Translate contentKey="dboApp.productAllergen.gTINUPC">G TINUPC</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('gTINUPC')} />
                </th>
                <th className="hand" onClick={sort('productAllergenId')}>
                  <Translate contentKey="dboApp.productAllergen.productAllergenId">Product Allergen Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('productAllergenId')} />
                </th>
                <th className="hand" onClick={sort('productId')}>
                  <Translate contentKey="dboApp.productAllergen.productId">Product Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('productId')} />
                </th>
                <th className="hand" onClick={sort('uPC')}>
                  <Translate contentKey="dboApp.productAllergen.uPC">U PC</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('uPC')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {productAllergenList.map((productAllergen, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/product-allergen/${productAllergen.id}`} color="link" size="sm">
                      {productAllergen.id}
                    </Button>
                  </td>
                  <td>{productAllergen.allergenId}</td>
                  <td>{productAllergen.allergenGroup}</td>
                  <td>{productAllergen.allergenName}</td>
                  <td>{productAllergen.description}</td>
                  <td>{productAllergen.gTIN}</td>
                  <td>{productAllergen.gTINUPC}</td>
                  <td>{productAllergen.productAllergenId}</td>
                  <td>{productAllergen.productId}</td>
                  <td>{productAllergen.uPC}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/product-allergen/${productAllergen.id}`}
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
                        to={`/product-allergen/${productAllergen.id}/edit`}
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
                        onClick={() => (window.location.href = `/product-allergen/${productAllergen.id}/delete`)}
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
              <Translate contentKey="dboApp.productAllergen.home.notFound">No Product Allergens found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default ProductAllergen;
