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

import { getEntities, searchEntities } from './usda-update-mst.reducer';

export const USDAUpdateMst = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [search, setSearch] = useState('');
  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const uSDAUpdateMstList = useAppSelector(state => state.uSDAUpdateMst.entities);
  const loading = useAppSelector(state => state.uSDAUpdateMst.loading);

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
      <h2 id="usda-update-mst-heading" data-cy="USDAUpdateMstHeading">
        <Translate contentKey="dboApp.uSDAUpdateMst.home.title">USDA Update Msts</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="dboApp.uSDAUpdateMst.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/usda-update-mst/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="dboApp.uSDAUpdateMst.home.createLabel">Create new USDA Update Mst</Translate>
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
                  placeholder={translate('dboApp.uSDAUpdateMst.home.search')}
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
        {uSDAUpdateMstList && uSDAUpdateMstList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('addedSugarsgperServing')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.addedSugarsgperServing">Added Sugarsgper Serving</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('addedSugarsgperServing')} />
                </th>
                <th className="hand" onClick={sort('allAllergens')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.allAllergens">All Allergens</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('allAllergens')} />
                </th>
                <th className="hand" onClick={sort('brandName')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.brandName">Brand Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('brandName')} />
                </th>
                <th className="hand" onClick={sort('calciumCamgperServing')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.calciumCamgperServing">Calcium Camgper Serving</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('calciumCamgperServing')} />
                </th>
                <th className="hand" onClick={sort('calorieskcalperServing')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.calorieskcalperServing">Calorieskcalper Serving</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('calorieskcalperServing')} />
                </th>
                <th className="hand" onClick={sort('cholesterolmgperServing')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.cholesterolmgperServing">Cholesterolmgper Serving</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('cholesterolmgperServing')} />
                </th>
                <th className="hand" onClick={sort('cNCredited')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.cNCredited">C N Credited</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('cNCredited')} />
                </th>
                <th className="hand" onClick={sort('cNCrediting')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.cNCrediting">C N Crediting</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('cNCrediting')} />
                </th>
                <th className="hand" onClick={sort('cNExpirationDate')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.cNExpirationDate">C N Expiration Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('cNExpirationDate')} />
                </th>
                <th className="hand" onClick={sort('cNLabelDocument')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.cNLabelDocument">C N Label Document</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('cNLabelDocument')} />
                </th>
                <th className="hand" onClick={sort('cNLabelStatement')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.cNLabelStatement">C N Label Statement</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('cNLabelStatement')} />
                </th>
                <th className="hand" onClick={sort('cNProductIdentification')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.cNProductIdentification">C N Product Identification</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('cNProductIdentification')} />
                </th>
                <th className="hand" onClick={sort('cNQualified')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.cNQualified">C N Qualified</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('cNQualified')} />
                </th>
                <th className="hand" onClick={sort('cNQualifierCode')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.cNQualifierCode">C N Qualifier Code</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('cNQualifierCode')} />
                </th>
                <th className="hand" onClick={sort('dietaryFibergperServing')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.dietaryFibergperServing">Dietary Fibergper Serving</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('dietaryFibergperServing')} />
                </th>
                <th className="hand" onClick={sort('downloaded')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.downloaded">Downloaded</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('downloaded')} />
                </th>
                <th className="hand" onClick={sort('eggs')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.eggs">Eggs</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('eggs')} />
                </th>
                <th className="hand" onClick={sort('fish')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.fish">Fish</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('fish')} />
                </th>
                <th className="hand" onClick={sort('foodCategory')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.foodCategory">Food Category</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('foodCategory')} />
                </th>
                <th className="hand" onClick={sort('functionalname')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.functionalname">Functionalname</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('functionalname')} />
                </th>
                <th className="hand" onClick={sort('gtin')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.gtin">Gtin</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('gtin')} />
                </th>
                <th className="hand" onClick={sort('halal')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.halal">Halal</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('halal')} />
                </th>
                <th className="hand" onClick={sort('hierarchicalPlacement')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.hierarchicalPlacement">Hierarchical Placement</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('hierarchicalPlacement')} />
                </th>
                <th className="hand" onClick={sort('informationProvider')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.informationProvider">Information Provider</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('informationProvider')} />
                </th>
                <th className="hand" onClick={sort('ingredientsenglish')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.ingredientsenglish">Ingredientsenglish</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ingredientsenglish')} />
                </th>
                <th className="hand" onClick={sort('ironFemgperServing')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.ironFemgperServing">Iron Femgper Serving</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ironFemgperServing')} />
                </th>
                <th className="hand" onClick={sort('kosher')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.kosher">Kosher</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('kosher')} />
                </th>
                <th className="hand" onClick={sort('lastupdated')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.lastupdated">Lastupdated</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastupdated')} />
                </th>
                <th className="hand" onClick={sort('longdescription')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.longdescription">Longdescription</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('longdescription')} />
                </th>
                <th className="hand" onClick={sort('milk')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.milk">Milk</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('milk')} />
                </th>
                <th className="hand" onClick={sort('nutrientFormatTypeCodeReferenceCode')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.nutrientFormatTypeCodeReferenceCode">
                    Nutrient Format Type Code Reference Code
                  </Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('nutrientFormatTypeCodeReferenceCode')} />
                </th>
                <th className="hand" onClick={sort('nutrientQuantityBasisTypeCode')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.nutrientQuantityBasisTypeCode">Nutrient Quantity Basis Type Code</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('nutrientQuantityBasisTypeCode')} />
                </th>
                <th className="hand" onClick={sort('nutrientQuantityBasisUnitofMeasure')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.nutrientQuantityBasisUnitofMeasure">
                    Nutrient Quantity Basis Unitof Measure
                  </Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('nutrientQuantityBasisUnitofMeasure')} />
                </th>
                <th className="hand" onClick={sort('nutrientQuantityBasisValue')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.nutrientQuantityBasisValue">Nutrient Quantity Basis Value</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('nutrientQuantityBasisValue')} />
                </th>
                <th className="hand" onClick={sort('nutrientsperservingcalculatedfrombymeasurereportedamount')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.nutrientsperservingcalculatedfrombymeasurereportedamount">
                    Nutrientsperservingcalculatedfrombymeasurereportedamount
                  </Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('nutrientsperservingcalculatedfrombymeasurereportedamount')} />
                </th>
                <th className="hand" onClick={sort('peanuts')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.peanuts">Peanuts</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('peanuts')} />
                </th>
                <th className="hand" onClick={sort('pFSCreditableIngredientTypeCode')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.pFSCreditableIngredientTypeCode">
                    P FS Creditable Ingredient Type Code
                  </Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('pFSCreditableIngredientTypeCode')} />
                </th>
                <th className="hand" onClick={sort('pFSDocument')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.pFSDocument">P FS Document</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('pFSDocument')} />
                </th>
                <th className="hand" onClick={sort('pFSTotalCreditableIngredientAmount')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.pFSTotalCreditableIngredientAmount">
                    P FS Total Creditable Ingredient Amount
                  </Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('pFSTotalCreditableIngredientAmount')} />
                </th>
                <th className="hand" onClick={sort('pFSTotalPortionWeight')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.pFSTotalPortionWeight">P FS Total Portion Weight</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('pFSTotalPortionWeight')} />
                </th>
                <th className="hand" onClick={sort('potassiumKmgperServing')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.potassiumKmgperServing">Potassium Kmgper Serving</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('potassiumKmgperServing')} />
                </th>
                <th className="hand" onClick={sort('preparationStateCode')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.preparationStateCode">Preparation State Code</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('preparationStateCode')} />
                </th>
                <th className="hand" onClick={sort('productFormulationStatement')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.productFormulationStatement">Product Formulation Statement</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('productFormulationStatement')} />
                </th>
                <th className="hand" onClick={sort('proteingperServing')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.proteingperServing">Proteingper Serving</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('proteingperServing')} />
                </th>
                <th className="hand" onClick={sort('saturatedFatgperServing')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.saturatedFatgperServing">Saturated Fatgper Serving</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('saturatedFatgperServing')} />
                </th>
                <th className="hand" onClick={sort('servingDescription')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.servingDescription">Serving Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('servingDescription')} />
                </th>
                <th className="hand" onClick={sort('servingSize')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.servingSize">Serving Size</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('servingSize')} />
                </th>
                <th className="hand" onClick={sort('servingUnitofMeasure')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.servingUnitofMeasure">Serving Unitof Measure</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('servingUnitofMeasure')} />
                </th>
                <th className="hand" onClick={sort('servingsPerCase')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.servingsPerCase">Servings Per Case</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('servingsPerCase')} />
                </th>
                <th className="hand" onClick={sort('sesame')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.sesame">Sesame</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('sesame')} />
                </th>
                <th className="hand" onClick={sort('shellfish')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.shellfish">Shellfish</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('shellfish')} />
                </th>
                <th className="hand" onClick={sort('shortdescription')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.shortdescription">Shortdescription</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('shortdescription')} />
                </th>
                <th className="hand" onClick={sort('sodiummgperServing')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.sodiummgperServing">Sodiummgper Serving</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('sodiummgperServing')} />
                </th>
                <th className="hand" onClick={sort('soybeans')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.soybeans">Soybeans</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('soybeans')} />
                </th>
                <th className="hand" onClick={sort('sugarsgperServing')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.sugarsgperServing">Sugarsgper Serving</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('sugarsgperServing')} />
                </th>
                <th className="hand" onClick={sort('totalCarbohydrategperServing')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.totalCarbohydrategperServing">Total Carbohydrategper Serving</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('totalCarbohydrategperServing')} />
                </th>
                <th className="hand" onClick={sort('totalFatgperServing')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.totalFatgperServing">Total Fatgper Serving</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('totalFatgperServing')} />
                </th>
                <th className="hand" onClick={sort('tradeChannels')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.tradeChannels">Trade Channels</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('tradeChannels')} />
                </th>
                <th className="hand" onClick={sort('transFatgperServing')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.transFatgperServing">Trans Fatgper Serving</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('transFatgperServing')} />
                </th>
                <th className="hand" onClick={sort('treeNuts')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.treeNuts">Tree Nuts</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('treeNuts')} />
                </th>
                <th className="hand" onClick={sort('uSDAFoodsMaterialCode')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.uSDAFoodsMaterialCode">U SDA Foods Material Code</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('uSDAFoodsMaterialCode')} />
                </th>
                <th className="hand" onClick={sort('uSDAFoodsProductsDescription')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.uSDAFoodsProductsDescription">U SDA Foods Products Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('uSDAFoodsProductsDescription')} />
                </th>
                <th className="hand" onClick={sort('vendorName')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.vendorName">Vendor Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('vendorName')} />
                </th>
                <th className="hand" onClick={sort('vendorID')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.vendorID">Vendor ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('vendorID')} />
                </th>
                <th className="hand" onClick={sort('vitaminDmcgperServing')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.vitaminDmcgperServing">Vitamin Dmcgper Serving</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('vitaminDmcgperServing')} />
                </th>
                <th className="hand" onClick={sort('wheat')}>
                  <Translate contentKey="dboApp.uSDAUpdateMst.wheat">Wheat</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('wheat')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {uSDAUpdateMstList.map((uSDAUpdateMst, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/usda-update-mst/${uSDAUpdateMst.id}`} color="link" size="sm">
                      {uSDAUpdateMst.id}
                    </Button>
                  </td>
                  <td>{uSDAUpdateMst.addedSugarsgperServing}</td>
                  <td>{uSDAUpdateMst.allAllergens}</td>
                  <td>{uSDAUpdateMst.brandName}</td>
                  <td>{uSDAUpdateMst.calciumCamgperServing}</td>
                  <td>{uSDAUpdateMst.calorieskcalperServing}</td>
                  <td>{uSDAUpdateMst.cholesterolmgperServing}</td>
                  <td>{uSDAUpdateMst.cNCredited}</td>
                  <td>{uSDAUpdateMst.cNCrediting}</td>
                  <td>{uSDAUpdateMst.cNExpirationDate}</td>
                  <td>{uSDAUpdateMst.cNLabelDocument}</td>
                  <td>{uSDAUpdateMst.cNLabelStatement}</td>
                  <td>{uSDAUpdateMst.cNProductIdentification}</td>
                  <td>{uSDAUpdateMst.cNQualified}</td>
                  <td>{uSDAUpdateMst.cNQualifierCode}</td>
                  <td>{uSDAUpdateMst.dietaryFibergperServing}</td>
                  <td>
                    {uSDAUpdateMst.downloaded ? (
                      <TextFormat type="date" value={uSDAUpdateMst.downloaded} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{uSDAUpdateMst.eggs}</td>
                  <td>{uSDAUpdateMst.fish}</td>
                  <td>{uSDAUpdateMst.foodCategory}</td>
                  <td>{uSDAUpdateMst.functionalname}</td>
                  <td>{uSDAUpdateMst.gtin}</td>
                  <td>{uSDAUpdateMst.halal}</td>
                  <td>{uSDAUpdateMst.hierarchicalPlacement}</td>
                  <td>{uSDAUpdateMst.informationProvider}</td>
                  <td>{uSDAUpdateMst.ingredientsenglish}</td>
                  <td>{uSDAUpdateMst.ironFemgperServing}</td>
                  <td>{uSDAUpdateMst.kosher}</td>
                  <td>
                    {uSDAUpdateMst.lastupdated ? (
                      <TextFormat type="date" value={uSDAUpdateMst.lastupdated} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{uSDAUpdateMst.longdescription}</td>
                  <td>{uSDAUpdateMst.milk}</td>
                  <td>{uSDAUpdateMst.nutrientFormatTypeCodeReferenceCode}</td>
                  <td>{uSDAUpdateMst.nutrientQuantityBasisTypeCode}</td>
                  <td>{uSDAUpdateMst.nutrientQuantityBasisUnitofMeasure}</td>
                  <td>{uSDAUpdateMst.nutrientQuantityBasisValue}</td>
                  <td>{uSDAUpdateMst.nutrientsperservingcalculatedfrombymeasurereportedamount ? 'true' : 'false'}</td>
                  <td>{uSDAUpdateMst.peanuts}</td>
                  <td>{uSDAUpdateMst.pFSCreditableIngredientTypeCode}</td>
                  <td>{uSDAUpdateMst.pFSDocument}</td>
                  <td>{uSDAUpdateMst.pFSTotalCreditableIngredientAmount}</td>
                  <td>{uSDAUpdateMst.pFSTotalPortionWeight}</td>
                  <td>{uSDAUpdateMst.potassiumKmgperServing}</td>
                  <td>{uSDAUpdateMst.preparationStateCode}</td>
                  <td>{uSDAUpdateMst.productFormulationStatement}</td>
                  <td>{uSDAUpdateMst.proteingperServing}</td>
                  <td>{uSDAUpdateMst.saturatedFatgperServing}</td>
                  <td>{uSDAUpdateMst.servingDescription}</td>
                  <td>{uSDAUpdateMst.servingSize}</td>
                  <td>{uSDAUpdateMst.servingUnitofMeasure}</td>
                  <td>{uSDAUpdateMst.servingsPerCase}</td>
                  <td>{uSDAUpdateMst.sesame}</td>
                  <td>{uSDAUpdateMst.shellfish}</td>
                  <td>{uSDAUpdateMst.shortdescription}</td>
                  <td>{uSDAUpdateMst.sodiummgperServing}</td>
                  <td>{uSDAUpdateMst.soybeans}</td>
                  <td>{uSDAUpdateMst.sugarsgperServing}</td>
                  <td>{uSDAUpdateMst.totalCarbohydrategperServing}</td>
                  <td>{uSDAUpdateMst.totalFatgperServing}</td>
                  <td>{uSDAUpdateMst.tradeChannels}</td>
                  <td>{uSDAUpdateMst.transFatgperServing}</td>
                  <td>{uSDAUpdateMst.treeNuts}</td>
                  <td>{uSDAUpdateMst.uSDAFoodsMaterialCode}</td>
                  <td>{uSDAUpdateMst.uSDAFoodsProductsDescription}</td>
                  <td>{uSDAUpdateMst.vendorName}</td>
                  <td>{uSDAUpdateMst.vendorID}</td>
                  <td>{uSDAUpdateMst.vitaminDmcgperServing}</td>
                  <td>{uSDAUpdateMst.wheat}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/usda-update-mst/${uSDAUpdateMst.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/usda-update-mst/${uSDAUpdateMst.id}/edit`}
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
                        onClick={() => (window.location.href = `/usda-update-mst/${uSDAUpdateMst.id}/delete`)}
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
              <Translate contentKey="dboApp.uSDAUpdateMst.home.notFound">No USDA Update Msts found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default USDAUpdateMst;
