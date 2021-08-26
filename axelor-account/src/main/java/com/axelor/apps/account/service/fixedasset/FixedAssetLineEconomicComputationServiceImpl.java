/*
 * Axelor Business Solutions
 *
 * Copyright (C) 2021 Axelor (<http://axelor.com>).
 *
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axelor.apps.account.service.fixedasset;

import com.axelor.apps.account.db.FixedAsset;
import com.axelor.apps.account.db.FixedAssetLine;
import com.axelor.apps.account.db.repo.FixedAssetRepository;
import com.axelor.apps.account.service.AnalyticFixedAssetService;
import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RequestScoped
public class FixedAssetLineEconomicComputationServiceImpl
    extends AbstractFixedAssetLineComputationServiceImpl {

  protected AnalyticFixedAssetService analyticFixedAssetService;
  protected FixedAssetDerogatoryLineService fixedAssetDerogatoryLineService;
  protected FixedAssetDerogatoryLineMoveService fixedAssetDerogatoryLineMoveService;

  @Inject
  public FixedAssetLineEconomicComputationServiceImpl(
      AnalyticFixedAssetService analyticFixedAssetService,
      FixedAssetDerogatoryLineService fixedAssetDerogatoryLineService,
      FixedAssetDerogatoryLineMoveService fixedAssetDerogatoryLineMoveService) {
    this.analyticFixedAssetService = analyticFixedAssetService;
    this.fixedAssetDerogatoryLineService = fixedAssetDerogatoryLineService;
    this.fixedAssetDerogatoryLineMoveService = fixedAssetDerogatoryLineMoveService;
  }

  @Override
  protected LocalDate computeStartDepreciationDate(FixedAsset fixedAsset) {
    if (!fixedAsset.getIsEqualToFiscalDepreciation()) {
      return analyticFixedAssetService.computeFirstDepreciationDate(
          fixedAsset, fixedAsset.getFirstServiceDate());
    }
    return fixedAsset.getFirstDepreciationDate();
  }

  @Override
  protected BigDecimal computeInitialDepreciationBase(FixedAsset fixedAsset) {
    if (!fixedAsset.getIsEqualToFiscalDepreciation()
        && fixedAsset
            .getComputationMethodSelect()
            .equals(FixedAssetRepository.COMPUTATION_METHOD_LINEAR)) {
      return fixedAsset.getGrossValue().subtract(fixedAsset.getResidualValue());
    }
    return fixedAsset.getGrossValue();
  }

  @Override
  protected LocalDate computeProrataTemporisFirstDepreciationDate(FixedAsset fixedAsset) {
    if (!fixedAsset.getIsEqualToFiscalDepreciation()) {
      return analyticFixedAssetService.computeFirstDepreciationDate(
          fixedAsset, fixedAsset.getFirstServiceDate());
    }
    return fixedAsset.getFirstDepreciationDate();
  }

  @Override
  protected LocalDate computeProrataTemporisAcquisitionDate(FixedAsset fixedAsset) {
    if (!fixedAsset.getIsEqualToFiscalDepreciation()) {
      return fixedAsset.getFirstServiceDate();
    }
    return fixedAsset.getAcquisitionDate();
  }

  @Override
  protected List<FixedAssetLine> getFixedAssetLineList(FixedAsset fixedAsset) {
    return fixedAsset.getFixedAssetLineList();
  }

  @Override
  protected Integer getNumberOfDepreciation(FixedAsset fixedAsset) {
    return fixedAsset.getNumberOfDepreciation();
  }

  @Override
  protected String getComputationMethodSelect(FixedAsset fixedAsset) {
    return fixedAsset.getComputationMethodSelect();
  }

  @Override
  protected BigDecimal getDegressiveCoef(FixedAsset fixedAsset) {
    return fixedAsset.getDegressiveCoef();
  }

  @Override
  protected Integer getPeriodicityInMonth(FixedAsset fixedAsset) {
    return fixedAsset.getPeriodicityInMonth();
  }
}
