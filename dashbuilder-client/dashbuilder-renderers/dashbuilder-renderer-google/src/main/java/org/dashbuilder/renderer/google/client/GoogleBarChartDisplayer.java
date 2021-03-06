/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dashbuilder.renderer.google.client;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.dashbuilder.dataset.ColumnType;
import org.dashbuilder.dataset.DataSetLookupConstraints;
import org.dashbuilder.displayer.DisplayerAttributeDef;
import org.dashbuilder.displayer.DisplayerAttributeGroupDef;
import org.dashbuilder.displayer.DisplayerConstraints;
import org.dashbuilder.displayer.DisplayerSubType;

@Dependent
public class GoogleBarChartDisplayer extends GoogleCategoriesDisplayer<GoogleBarChartDisplayer.View> {

    public interface View extends GoogleCategoriesDisplayer.View<GoogleBarChartDisplayer> {

        void setIsBar(boolean isBar);

        void setIsStacked(boolean isStacked);
    }

    private View view;

    public GoogleBarChartDisplayer() {
        this(new GoogleBarChartDisplayerView());
    }

    @Inject
    public GoogleBarChartDisplayer(View view) {
        this.view = view;
        this.view.init(this);
    }

    @Override
    public View getView() {
        return view;
    }

    @Override
    public DisplayerConstraints createDisplayerConstraints() {

        DataSetLookupConstraints lookupConstraints = new DataSetLookupConstraints()
                .setGroupRequired(true)
                .setGroupColumn(true)
                .setMaxColumns(10)
                .setMinColumns(2)
                .setExtraColumnsAllowed(true)
                .setExtraColumnsType( ColumnType.NUMBER)
                .setGroupsTitle(view.getGroupsTitle())
                .setColumnsTitle(view.getColumnsTitle())
                .setColumnTypes(new ColumnType[] {
                        ColumnType.LABEL,
                        ColumnType.NUMBER});

        return new DisplayerConstraints(lookupConstraints)
                .supportsAttribute(DisplayerAttributeDef.TYPE)
                .supportsAttribute(DisplayerAttributeDef.SUBTYPE)
                .supportsAttribute(DisplayerAttributeDef.RENDERER)
                .supportsAttribute(DisplayerAttributeGroupDef.COLUMNS_GROUP)
                .supportsAttribute(DisplayerAttributeGroupDef.FILTER_GROUP)
                .supportsAttribute(DisplayerAttributeGroupDef.REFRESH_GROUP)
                .supportsAttribute(DisplayerAttributeGroupDef.GENERAL_GROUP)
                .supportsAttribute(DisplayerAttributeDef.CHART_WIDTH)
                .supportsAttribute(DisplayerAttributeDef.CHART_HEIGHT)
                .supportsAttribute(DisplayerAttributeDef.CHART_BGCOLOR)
                .supportsAttribute(DisplayerAttributeGroupDef.CHART_MARGIN_GROUP)
                .supportsAttribute(DisplayerAttributeGroupDef.CHART_LEGEND_GROUP)
                .supportsAttribute(DisplayerAttributeGroupDef.AXIS_GROUP);
    }

    public boolean isBarChart() {
        return displayerSettings.getSubtype() == null ||
                DisplayerSubType.BAR.equals(displayerSettings.getSubtype()) ||
                DisplayerSubType.BAR_STACKED.equals(displayerSettings.getSubtype());
    }

    public boolean isStacked() {
        return displayerSettings.getSubtype() != null &&
                (DisplayerSubType.BAR_STACKED.equals(displayerSettings.getSubtype()) ||
                DisplayerSubType.COLUMN_STACKED.equals(displayerSettings.getSubtype()));
    }

    @Override
    protected void createVisualization() {
        view.setIsBar(isBarChart());
        view.setIsStacked(isStacked());

        super.createVisualization();
    }
}
