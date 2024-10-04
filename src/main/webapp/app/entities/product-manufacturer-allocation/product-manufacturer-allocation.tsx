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

import { getEntities, searchEntities } from './product-manufacturer-allocation.reducer';

export const ProductManufacturerAllocation = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [search, setSearch] = useState('');
  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const productManufacturerAllocationList = useAppSelector(state => state.productManufacturerAllocation.entities);
  const loading = useAppSelector(state => state.productManufacturerAllocation.loading);

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
      <h2 id="product-manufacturer-allocation-heading" data-cy="ProductManufacturerAllocationHeading">
        <Translate contentKey="dboApp.productManufacturerAllocation.home.title">Product Manufacturer Allocations</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="dboApp.productManufacturerAllocation.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/product-manufacturer-allocation/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="dboApp.productManufacturerAllocation.home.createLabel">
              Create new Product Manufacturer Allocation
            </Translate>
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
                  placeholder={translate('dboApp.productManufacturerAllocation.home.search')}
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
        {productManufacturerAllocationList && productManufacturerAllocationList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="dboApp.productManufacturerAllocation.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="dboApp.productManufacturerAllocation.createdBy">Created By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                </th>
                <th className="hand" onClick={sort('createdOn')}>
                  <Translate contentKey="dboApp.productManufacturerAllocation.createdOn">Created On</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdOn')} />
                </th>
                <th className="hand" onClick={sort('isAllocated')}>
                  <Translate contentKey="dboApp.productManufacturerAllocation.isAllocated">Is Allocated</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isAllocated')} />
                </th>
                <th className="hand" onClick={sort('manufactureId')}>
                  <Translate contentKey="dboApp.productManufacturerAllocation.manufactureId">Manufacture Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('manufactureId')} />
                </th>
                <th className="hand" onClick={sort('productId')}>
                  <Translate contentKey="dboApp.productManufacturerAllocation.productId">Product Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('productId')} />
                </th>
                <th className="hand" onClick={sort('productManufacturerAllocationId')}>
                  <Translate contentKey="dboApp.productManufacturerAllocation.productManufacturerAllocationId">
                    Product Manufacturer Allocation Id
                  </Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('productManufacturerAllocationId')} />
                </th>
                <th className="hand" onClick={sort('updatedBy')}>
                  <Translate contentKey="dboApp.productManufacturerAllocation.updatedBy">Updated By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('updatedBy')} />
                </th>
                <th className="hand" onClick={sort('updatedOn')}>
                  <Translate contentKey="dboApp.productManufacturerAllocation.updatedOn">Updated On</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('updatedOn')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {productManufacturerAllocationList.map((productManufacturerAllocation, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/product-manufacturer-allocation/${productManufacturerAllocation.id}`} color="link" size="sm">
                      {productManufacturerAllocation.id}
                    </Button>
                  </td>
                  <td>{productManufacturerAllocation.createdBy}</td>
                  <td>
                    {productManufacturerAllocation.createdOn ? (
                      <TextFormat type="date" value={productManufacturerAllocation.createdOn} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{productManufacturerAllocation.isAllocated ? 'true' : 'false'}</td>
                  <td>{productManufacturerAllocation.manufactureId}</td>
                  <td>{productManufacturerAllocation.productId}</td>
                  <td>{productManufacturerAllocation.productManufacturerAllocationId}</td>
                  <td>{productManufacturerAllocation.updatedBy}</td>
                  <td>
                    {productManufacturerAllocation.updatedOn ? (
                      <TextFormat type="date" value={productManufacturerAllocation.updatedOn} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/product-manufacturer-allocation/${productManufacturerAllocation.id}`}
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
                        to={`/product-manufacturer-allocation/${productManufacturerAllocation.id}/edit`}
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
                        onClick={() =>
                          (window.location.href = `/product-manufacturer-allocation/${productManufacturerAllocation.id}/delete`)
                        }
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
              <Translate contentKey="dboApp.productManufacturerAllocation.home.notFound">
                No Product Manufacturer Allocations found
              </Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default ProductManufacturerAllocation;
