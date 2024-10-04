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

import { getEntities, searchEntities } from './one-world-sync-product.reducer';

export const OneWorldSyncProduct = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [search, setSearch] = useState('');
  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const oneWorldSyncProductList = useAppSelector(state => state.oneWorldSyncProduct.entities);
  const loading = useAppSelector(state => state.oneWorldSyncProduct.loading);

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
      <h2 id="one-world-sync-product-heading" data-cy="OneWorldSyncProductHeading">
        <Translate contentKey="dboApp.oneWorldSyncProduct.home.title">One World Sync Products</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="dboApp.oneWorldSyncProduct.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/one-world-sync-product/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="dboApp.oneWorldSyncProduct.home.createLabel">Create new One World Sync Product</Translate>
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
                  placeholder={translate('dboApp.oneWorldSyncProduct.home.search')}
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
        {oneWorldSyncProductList && oneWorldSyncProductList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('addedSugars')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.addedSugars">Added Sugars</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('addedSugars')} />
                </th>
                <th className="hand" onClick={sort('addedSugarUom')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.addedSugarUom">Added Sugar Uom</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('addedSugarUom')} />
                </th>
                <th className="hand" onClick={sort('allergenKeyword')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.allergenKeyword">Allergen Keyword</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('allergenKeyword')} />
                </th>
                <th className="hand" onClick={sort('allergens')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.allergens">Allergens</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('allergens')} />
                </th>
                <th className="hand" onClick={sort('brandName')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.brandName">Brand Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('brandName')} />
                </th>
                <th className="hand" onClick={sort('calories')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.calories">Calories</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('calories')} />
                </th>
                <th className="hand" onClick={sort('caloriesUom')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.caloriesUom">Calories Uom</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('caloriesUom')} />
                </th>
                <th className="hand" onClick={sort('carbohydrates')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.carbohydrates">Carbohydrates</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('carbohydrates')} />
                </th>
                <th className="hand" onClick={sort('carbohydratesUom')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.carbohydratesUom">Carbohydrates Uom</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('carbohydratesUom')} />
                </th>
                <th className="hand" onClick={sort('categoryName')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.categoryName">Category Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('categoryName')} />
                </th>
                <th className="hand" onClick={sort('cholesterol')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.cholesterol">Cholesterol</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('cholesterol')} />
                </th>
                <th className="hand" onClick={sort('cholesterolUOM')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.cholesterolUOM">Cholesterol UOM</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('cholesterolUOM')} />
                </th>
                <th className="hand" onClick={sort('createdOn')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.createdOn">Created On</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdOn')} />
                </th>
                <th className="hand" onClick={sort('dataForm')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.dataForm">Data Form</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('dataForm')} />
                </th>
                <th className="hand" onClick={sort('dietaryFiber')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.dietaryFiber">Dietary Fiber</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('dietaryFiber')} />
                </th>
                <th className="hand" onClick={sort('dietaryFiberUom')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.dietaryFiberUom">Dietary Fiber Uom</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('dietaryFiberUom')} />
                </th>
                <th className="hand" onClick={sort('distributor')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.distributor">Distributor</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('distributor')} />
                </th>
                <th className="hand" onClick={sort('doNotConsiderProduct')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.doNotConsiderProduct">Do Not Consider Product</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('doNotConsiderProduct')} />
                </th>
                <th className="hand" onClick={sort('extendedModel')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.extendedModel">Extended Model</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('extendedModel')} />
                </th>
                <th className="hand" onClick={sort('gLNNumber')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.gLNNumber">G LN Number</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('gLNNumber')} />
                </th>
                <th className="hand" onClick={sort('gTIN')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.gTIN">G TIN</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('gTIN')} />
                </th>
                <th className="hand" onClick={sort('h7')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.h7">H 7</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('h7')} />
                </th>
                <th className="hand" onClick={sort('image')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.image">Image</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('image')} />
                </th>
                <th className="hand" onClick={sort('ingredients')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.ingredients">Ingredients</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ingredients')} />
                </th>
                <th className="hand" onClick={sort('isActive')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.isActive">Is Active</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isActive')} />
                </th>
                <th className="hand" onClick={sort('isApprove')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.isApprove">Is Approve</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isApprove')} />
                </th>
                <th className="hand" onClick={sort('isMerge')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.isMerge">Is Merge</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isMerge')} />
                </th>
                <th className="hand" onClick={sort('isProductSync')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.isProductSync">Is Product Sync</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isProductSync')} />
                </th>
                <th className="hand" onClick={sort('manufacturer')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.manufacturer">Manufacturer</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('manufacturer')} />
                </th>
                <th className="hand" onClick={sort('manufacturerId')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.manufacturerId">Manufacturer Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('manufacturerId')} />
                </th>
                <th className="hand" onClick={sort('manufacturerText1Ws')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.manufacturerText1Ws">Manufacturer Text 1 Ws</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('manufacturerText1Ws')} />
                </th>
                <th className="hand" onClick={sort('modifiedOn')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.modifiedOn">Modified On</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('modifiedOn')} />
                </th>
                <th className="hand" onClick={sort('productDescription')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.productDescription">Product Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('productDescription')} />
                </th>
                <th className="hand" onClick={sort('productId')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.productId">Product Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('productId')} />
                </th>
                <th className="hand" onClick={sort('productName')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.productName">Product Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('productName')} />
                </th>
                <th className="hand" onClick={sort('protein')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.protein">Protein</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('protein')} />
                </th>
                <th className="hand" onClick={sort('proteinUom')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.proteinUom">Protein Uom</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('proteinUom')} />
                </th>
                <th className="hand" onClick={sort('saturatedFat')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.saturatedFat">Saturated Fat</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('saturatedFat')} />
                </th>
                <th className="hand" onClick={sort('serving')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.serving">Serving</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('serving')} />
                </th>
                <th className="hand" onClick={sort('servingUom')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.servingUom">Serving Uom</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('servingUom')} />
                </th>
                <th className="hand" onClick={sort('sodium')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.sodium">Sodium</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('sodium')} />
                </th>
                <th className="hand" onClick={sort('sodiumUom')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.sodiumUom">Sodium Uom</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('sodiumUom')} />
                </th>
                <th className="hand" onClick={sort('storageTypeId')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.storageTypeId">Storage Type Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('storageTypeId')} />
                </th>
                <th className="hand" onClick={sort('storageTypeName')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.storageTypeName">Storage Type Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('storageTypeName')} />
                </th>
                <th className="hand" onClick={sort('subCategory1Name')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.subCategory1Name">Sub Category 1 Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('subCategory1Name')} />
                </th>
                <th className="hand" onClick={sort('subCategory2Name')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.subCategory2Name">Sub Category 2 Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('subCategory2Name')} />
                </th>
                <th className="hand" onClick={sort('sugar')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.sugar">Sugar</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('sugar')} />
                </th>
                <th className="hand" onClick={sort('sugarUom')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.sugarUom">Sugar Uom</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('sugarUom')} />
                </th>
                <th className="hand" onClick={sort('syncEffective')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.syncEffective">Sync Effective</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('syncEffective')} />
                </th>
                <th className="hand" onClick={sort('syncHeaderLastChange')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.syncHeaderLastChange">Sync Header Last Change</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('syncHeaderLastChange')} />
                </th>
                <th className="hand" onClick={sort('syncItemReferenceId')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.syncItemReferenceId">Sync Item Reference Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('syncItemReferenceId')} />
                </th>
                <th className="hand" onClick={sort('syncLastChange')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.syncLastChange">Sync Last Change</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('syncLastChange')} />
                </th>
                <th className="hand" onClick={sort('syncPublication')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.syncPublication">Sync Publication</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('syncPublication')} />
                </th>
                <th className="hand" onClick={sort('totalFat')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.totalFat">Total Fat</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('totalFat')} />
                </th>
                <th className="hand" onClick={sort('transFat')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.transFat">Trans Fat</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('transFat')} />
                </th>
                <th className="hand" onClick={sort('uPC')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.uPC">U PC</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('uPC')} />
                </th>
                <th className="hand" onClick={sort('vendor')}>
                  <Translate contentKey="dboApp.oneWorldSyncProduct.vendor">Vendor</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('vendor')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {oneWorldSyncProductList.map((oneWorldSyncProduct, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/one-world-sync-product/${oneWorldSyncProduct.id}`} color="link" size="sm">
                      {oneWorldSyncProduct.id}
                    </Button>
                  </td>
                  <td>{oneWorldSyncProduct.addedSugars}</td>
                  <td>{oneWorldSyncProduct.addedSugarUom}</td>
                  <td>{oneWorldSyncProduct.allergenKeyword}</td>
                  <td>{oneWorldSyncProduct.allergens}</td>
                  <td>{oneWorldSyncProduct.brandName}</td>
                  <td>{oneWorldSyncProduct.calories}</td>
                  <td>{oneWorldSyncProduct.caloriesUom}</td>
                  <td>{oneWorldSyncProduct.carbohydrates}</td>
                  <td>{oneWorldSyncProduct.carbohydratesUom}</td>
                  <td>{oneWorldSyncProduct.categoryName}</td>
                  <td>{oneWorldSyncProduct.cholesterol}</td>
                  <td>{oneWorldSyncProduct.cholesterolUOM}</td>
                  <td>
                    {oneWorldSyncProduct.createdOn ? (
                      <TextFormat type="date" value={oneWorldSyncProduct.createdOn} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{oneWorldSyncProduct.dataForm}</td>
                  <td>{oneWorldSyncProduct.dietaryFiber}</td>
                  <td>{oneWorldSyncProduct.dietaryFiberUom}</td>
                  <td>{oneWorldSyncProduct.distributor}</td>
                  <td>{oneWorldSyncProduct.doNotConsiderProduct ? 'true' : 'false'}</td>
                  <td>{oneWorldSyncProduct.extendedModel}</td>
                  <td>{oneWorldSyncProduct.gLNNumber}</td>
                  <td>{oneWorldSyncProduct.gTIN}</td>
                  <td>{oneWorldSyncProduct.h7}</td>
                  <td>{oneWorldSyncProduct.image}</td>
                  <td>{oneWorldSyncProduct.ingredients}</td>
                  <td>{oneWorldSyncProduct.isActive ? 'true' : 'false'}</td>
                  <td>{oneWorldSyncProduct.isApprove ? 'true' : 'false'}</td>
                  <td>{oneWorldSyncProduct.isMerge ? 'true' : 'false'}</td>
                  <td>{oneWorldSyncProduct.isProductSync ? 'true' : 'false'}</td>
                  <td>{oneWorldSyncProduct.manufacturer}</td>
                  <td>{oneWorldSyncProduct.manufacturerId}</td>
                  <td>{oneWorldSyncProduct.manufacturerText1Ws}</td>
                  <td>
                    {oneWorldSyncProduct.modifiedOn ? (
                      <TextFormat type="date" value={oneWorldSyncProduct.modifiedOn} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{oneWorldSyncProduct.productDescription}</td>
                  <td>{oneWorldSyncProduct.productId}</td>
                  <td>{oneWorldSyncProduct.productName}</td>
                  <td>{oneWorldSyncProduct.protein}</td>
                  <td>{oneWorldSyncProduct.proteinUom}</td>
                  <td>{oneWorldSyncProduct.saturatedFat}</td>
                  <td>{oneWorldSyncProduct.serving}</td>
                  <td>{oneWorldSyncProduct.servingUom}</td>
                  <td>{oneWorldSyncProduct.sodium}</td>
                  <td>{oneWorldSyncProduct.sodiumUom}</td>
                  <td>{oneWorldSyncProduct.storageTypeId}</td>
                  <td>{oneWorldSyncProduct.storageTypeName}</td>
                  <td>{oneWorldSyncProduct.subCategory1Name}</td>
                  <td>{oneWorldSyncProduct.subCategory2Name}</td>
                  <td>{oneWorldSyncProduct.sugar}</td>
                  <td>{oneWorldSyncProduct.sugarUom}</td>
                  <td>
                    {oneWorldSyncProduct.syncEffective ? (
                      <TextFormat type="date" value={oneWorldSyncProduct.syncEffective} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {oneWorldSyncProduct.syncHeaderLastChange ? (
                      <TextFormat type="date" value={oneWorldSyncProduct.syncHeaderLastChange} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{oneWorldSyncProduct.syncItemReferenceId}</td>
                  <td>
                    {oneWorldSyncProduct.syncLastChange ? (
                      <TextFormat type="date" value={oneWorldSyncProduct.syncLastChange} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {oneWorldSyncProduct.syncPublication ? (
                      <TextFormat type="date" value={oneWorldSyncProduct.syncPublication} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{oneWorldSyncProduct.totalFat}</td>
                  <td>{oneWorldSyncProduct.transFat}</td>
                  <td>{oneWorldSyncProduct.uPC}</td>
                  <td>{oneWorldSyncProduct.vendor}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/one-world-sync-product/${oneWorldSyncProduct.id}`}
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
                        to={`/one-world-sync-product/${oneWorldSyncProduct.id}/edit`}
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
                        onClick={() => (window.location.href = `/one-world-sync-product/${oneWorldSyncProduct.id}/delete`)}
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
              <Translate contentKey="dboApp.oneWorldSyncProduct.home.notFound">No One World Sync Products found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default OneWorldSyncProduct;
