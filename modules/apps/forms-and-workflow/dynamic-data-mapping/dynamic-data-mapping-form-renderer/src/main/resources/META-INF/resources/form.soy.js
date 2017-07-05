// This file was automatically generated from form.soy.
// Please don't edit this file by hand.

/**
 * @fileoverview Templates in namespace ddm.
 * @hassoydeltemplate {ddm.field}
 * @hassoydelcall {ddm.field}
 * @public
 */

if (typeof ddm == 'undefined') { var ddm = {}; }


ddm.__deltemplate_s2_86478705 = function(opt_data, opt_ignored) {
  return '';
};
if (goog.DEBUG) {
  ddm.__deltemplate_s2_86478705.soyTemplateName = 'ddm.__deltemplate_s2_86478705';
}
soy.$$registerDelegateFn(soy.$$getDelTemplateId('ddm.field'), '', 0, ddm.__deltemplate_s2_86478705);


ddm.fields = function(opt_data, opt_ignored) {
  var output = '';
  var fieldList12 = opt_data.fields;
  var fieldListLen12 = fieldList12.length;
  for (var fieldIndex12 = 0; fieldIndex12 < fieldListLen12; fieldIndex12++) {
    var fieldData12 = fieldList12[fieldIndex12];
    var variant__soy4 = fieldData12.type;
    output += '<div class="clearfix ' + ((! fieldData12.visible) ? 'hide' : '') + ' lfr-ddm-form-field-container">' + soy.$$getDelegateFn(soy.$$getDelTemplateId('ddm.field'), variant__soy4, true)(fieldData12) + '</div>';
  }
  return output;
};
if (goog.DEBUG) {
  ddm.fields.soyTemplateName = 'ddm.fields';
}


ddm.form_renderer_js = function(opt_data, opt_ignored) {
  return '<script type="text/javascript">AUI().use( \'liferay-ddm-form-renderer\', \'liferay-ddm-form-renderer-field\', function(A) {Liferay.DDM.Renderer.FieldTypes.register(' + soy.$$filterNoAutoescape(opt_data.fieldTypes) + '); Liferay.component( \'' + soy.$$escapeJsString(opt_data.containerId) + 'DDMForm\', new Liferay.DDM.Renderer.Form({container: \'#' + soy.$$escapeJsString(opt_data.containerId) + '\', context: ' + soy.$$filterNoAutoescape(opt_data.context) + ', evaluatorURL: \'' + soy.$$escapeJsString(opt_data.evaluatorURL) + '\', portletNamespace: \'' + soy.$$escapeJsString(opt_data.portletNamespace) + '\', readOnly: ' + soy.$$escapeJsValue(opt_data.readOnly) + '}).render() ); var destroyFormHandle = function(event) {var form = Liferay.component(\'' + soy.$$escapeJsString(opt_data.containerId) + 'DDMForm\'); var portlet = event.portlet; if (portlet && portlet.contains(form.get(\'container\'))) {form.destroy(); Liferay.detach(\'destroyPortlet\', destroyFormHandle);}}; Liferay.on(\'destroyPortlet\', destroyFormHandle);});<\/script>';
};
if (goog.DEBUG) {
  ddm.form_renderer_js.soyTemplateName = 'ddm.form_renderer_js';
}


ddm.form_rows = function(opt_data, opt_ignored) {
  var output = '';
  var rowList39 = opt_data.rows;
  var rowListLen39 = rowList39.length;
  for (var rowIndex39 = 0; rowIndex39 < rowListLen39; rowIndex39++) {
    var rowData39 = rowList39[rowIndex39];
    output += '<div class="row">' + ddm.form_row_columns(soy.$$augmentMap(opt_data, {columns: rowData39.columns})) + '</div>';
  }
  return output;
};
if (goog.DEBUG) {
  ddm.form_rows.soyTemplateName = 'ddm.form_rows';
}


ddm.form_row_column = function(opt_data, opt_ignored) {
  return '<div class="col-md-' + soy.$$escapeHtmlAttribute(opt_data.column.size) + '">' + ddm.fields(soy.$$augmentMap(opt_data, {fields: opt_data.column.fields})) + '</div>';
};
if (goog.DEBUG) {
  ddm.form_row_column.soyTemplateName = 'ddm.form_row_column';
}


ddm.form_row_columns = function(opt_data, opt_ignored) {
  var output = '';
  var columnList51 = opt_data.columns;
  var columnListLen51 = columnList51.length;
  for (var columnIndex51 = 0; columnIndex51 < columnListLen51; columnIndex51++) {
    var columnData51 = columnList51[columnIndex51];
    output += ddm.form_row_column(soy.$$augmentMap(opt_data, {column: columnData51}));
  }
  return output;
};
if (goog.DEBUG) {
  ddm.form_row_columns.soyTemplateName = 'ddm.form_row_columns';
}


ddm.required_warning_message = function(opt_data, opt_ignored) {
  return '' + ((opt_data.showRequiredFieldsWarning) ? soy.$$filterNoAutoescape(opt_data.requiredFieldsWarningMessageHTML) : '');
};
if (goog.DEBUG) {
  ddm.required_warning_message.soyTemplateName = 'ddm.required_warning_message';
}


ddm.wizard_form = function(opt_data, opt_ignored) {
  var output = '<div class="lfr-ddm-form-container" id="' + soy.$$escapeHtmlAttribute(opt_data.containerId) + '"><div class="lfr-ddm-form-content">';
  if (opt_data.pages.length > 1) {
    output += '<div class="lfr-ddm-form-wizard"><ul class="multi-step-progress-bar multi-step-progress-bar-collapse">';
    var pageList75 = opt_data.pages;
    var pageListLen75 = pageList75.length;
    for (var pageIndex75 = 0; pageIndex75 < pageListLen75; pageIndex75++) {
      var pageData75 = pageList75[pageIndex75];
      output += '<li ' + ((pageIndex75 == 0) ? 'class="active"' : '') + '><div class="progress-bar-title">' + soy.$$filterNoAutoescape(pageData75.title) + '</div><div class="divider"></div><div class="progress-bar-step">' + soy.$$escapeHtml(pageIndex75 + 1) + '</div></li>';
    }
    output += '</ul></div>';
  }
  output += '<div class="lfr-ddm-form-pages">';
  var pageList102 = opt_data.pages;
  var pageListLen102 = pageList102.length;
  for (var pageIndex102 = 0; pageIndex102 < pageListLen102; pageIndex102++) {
    var pageData102 = pageList102[pageIndex102];
    output += '<div class="' + ((pageIndex102 == 0) ? 'active' : '') + ' lfr-ddm-form-page">' + ((pageData102.title) ? '<h3 class="lfr-ddm-form-page-title">' + soy.$$filterNoAutoescape(pageData102.title) + '</h3>' : '') + ((pageData102.description) ? '<h4 class="lfr-ddm-form-page-description">' + soy.$$filterNoAutoescape(pageData102.description) + '</h4>' : '') + ddm.required_warning_message(soy.$$augmentMap(opt_data, {showRequiredFieldsWarning: pageData102.showRequiredFieldsWarning, requiredFieldsWarningMessageHTML: opt_data.requiredFieldsWarningMessageHTML})) + ddm.form_rows(soy.$$augmentMap(opt_data, {rows: pageData102.rows})) + '</div>';
  }
  output += '</div></div><div class="lfr-ddm-form-pagination-controls"><button class="btn btn-lg btn-primary hide lfr-ddm-form-pagination-prev" type="button"><i class="icon-angle-left"></i> ' + soy.$$escapeHtml(opt_data.strings.previous) + '</button><button class="btn btn-lg btn-primary' + ((opt_data.pages.length == 1) ? ' hide' : '') + ' lfr-ddm-form-pagination-next pull-right" type="button">' + soy.$$escapeHtml(opt_data.strings.next) + ' <i class="icon-angle-right"></i></button>' + ((opt_data.showSubmitButton) ? '<button class="btn btn-lg btn-primary' + ((opt_data.pages.length > 1) ? ' hide' : '') + ' lfr-ddm-form-submit pull-right" disabled type="submit">' + soy.$$escapeHtml(opt_data.submitLabel) + '</button>' : '') + '</div></div>';
  return output;
};
if (goog.DEBUG) {
  ddm.wizard_form.soyTemplateName = 'ddm.wizard_form';
}


ddm.paginated_form = function(opt_data, opt_ignored) {
  var output = '<div class="lfr-ddm-form-container" id="' + soy.$$escapeHtmlAttribute(opt_data.containerId) + '"><div class="lfr-ddm-form-content"><div class="lfr-ddm-form-pages">';
  var pageList150 = opt_data.pages;
  var pageListLen150 = pageList150.length;
  for (var pageIndex150 = 0; pageIndex150 < pageListLen150; pageIndex150++) {
    var pageData150 = pageList150[pageIndex150];
    output += '<div class="' + ((pageIndex150 == 0) ? 'active' : '') + ' lfr-ddm-form-page">' + ((pageData150.title) ? '<h3 class="lfr-ddm-form-page-title">' + soy.$$filterNoAutoescape(pageData150.title) + '</h3>' : '') + ((pageData150.description) ? '<h4 class="lfr-ddm-form-page-description">' + soy.$$filterNoAutoescape(pageData150.description) + '</h4>' : '') + ddm.required_warning_message(soy.$$augmentMap(opt_data, {showRequiredFieldsWarning: pageData150.showRequiredFieldsWarning, requiredFieldsWarningMessageHTML: opt_data.requiredFieldsWarningMessageHTML})) + ddm.form_rows(soy.$$augmentMap(opt_data, {rows: pageData150.rows})) + '</div>';
  }
  output += '</div>';
  if (opt_data.pages.length > 1) {
    output += '<div class="lfr-ddm-form-paginated"><ul class="pagination pagination-content">';
    var pageList163 = opt_data.pages;
    var pageListLen163 = pageList163.length;
    for (var pageIndex163 = 0; pageIndex163 < pageListLen163; pageIndex163++) {
      var pageData163 = pageList163[pageIndex163];
      output += '<li ' + ((pageIndex163 == 0) ? 'class="active"' : '') + '><a href="#">' + soy.$$escapeHtml(pageIndex163 + 1) + '</a></li>';
    }
    output += '</ul></div>';
  }
  output += '</div><div class="lfr-ddm-form-pagination-controls"><button class="btn btn-lg btn-primary hide lfr-ddm-form-pagination-prev" type="button"><i class="icon-angle-left"></i> ' + soy.$$escapeHtml(opt_data.strings.previous) + '</button><button class="btn btn-lg btn-primary' + ((opt_data.pages.length == 1) ? ' hide' : '') + ' lfr-ddm-form-pagination-next pull-right" type="button">' + soy.$$escapeHtml(opt_data.strings.next) + ' <i class="icon-angle-right"></i></button>' + ((opt_data.showSubmitButton) ? '<button class="btn btn-lg btn-primary' + ((opt_data.pages.length > 1) ? ' hide' : '') + ' lfr-ddm-form-submit pull-right" disabled type="submit">' + soy.$$escapeHtml(opt_data.submitLabel) + '</button>' : '') + '</div></div>';
  return output;
};
if (goog.DEBUG) {
  ddm.paginated_form.soyTemplateName = 'ddm.paginated_form';
}


ddm.simple_form = function(opt_data, opt_ignored) {
  var output = '<div class="lfr-ddm-form-container" id="' + soy.$$escapeHtmlAttribute(opt_data.containerId) + '"><div class="lfr-ddm-form-fields">';
  var pageList194 = opt_data.pages;
  var pageListLen194 = pageList194.length;
  for (var pageIndex194 = 0; pageIndex194 < pageListLen194; pageIndex194++) {
    var pageData194 = pageList194[pageIndex194];
    output += ddm.required_warning_message(soy.$$augmentMap(opt_data, {showRequiredFieldsWarning: pageData194.showRequiredFieldsWarning, requiredFieldsWarningMessageHTML: opt_data.requiredFieldsWarningMessageHTML})) + ddm.form_rows(soy.$$augmentMap(opt_data, {rows: pageData194.rows}));
  }
  output += '</div></div>';
  return output;
};
if (goog.DEBUG) {
  ddm.simple_form.soyTemplateName = 'ddm.simple_form';
}


ddm.tabbed_form = function(opt_data, opt_ignored) {
  var output = '<div class="lfr-ddm-form-container" id="' + soy.$$escapeHtmlAttribute(opt_data.containerId) + '"><div class="lfr-ddm-form-tabs"><ul class="nav navbar-nav">';
  var pageList204 = opt_data.pages;
  var pageListLen204 = pageList204.length;
  for (var pageIndex204 = 0; pageIndex204 < pageListLen204; pageIndex204++) {
    var pageData204 = pageList204[pageIndex204];
    output += '<li><a href="javascript:;">' + soy.$$escapeHtml(pageData204.title) + '</a></li>';
  }
  output += '</ul><div class="tab-content">';
  var pageList218 = opt_data.pages;
  var pageListLen218 = pageList218.length;
  for (var pageIndex218 = 0; pageIndex218 < pageListLen218; pageIndex218++) {
    var pageData218 = pageList218[pageIndex218];
    output += '<div class="lfr-ddm-form-page tab-pane ' + ((pageIndex218 == 0) ? 'active' : '') + '">' + ddm.required_warning_message(soy.$$augmentMap(opt_data, {showRequiredFieldsWarning: pageData218.showRequiredFieldsWarning, requiredFieldsWarningMessageHTML: opt_data.requiredFieldsWarningMessageHTML})) + ddm.form_rows(soy.$$augmentMap(opt_data, {rows: pageData218.rows})) + '</div>';
  }
  output += '</div></div></div>';
  return output;
};
if (goog.DEBUG) {
  ddm.tabbed_form.soyTemplateName = 'ddm.tabbed_form';
}


ddm.tabbed_form_frame = function(opt_data, opt_ignored) {
  opt_data = opt_data || {};
  return '<div class="lfr-ddm-form-page tab-pane ' + ((opt_data.active) ? 'active' : '') + '"></div>';
};
if (goog.DEBUG) {
  ddm.tabbed_form_frame.soyTemplateName = 'ddm.tabbed_form_frame';
}


ddm.settings_form = function(opt_data, opt_ignored) {
  var output = '<div class="lfr-ddm-form-container" id="' + soy.$$escapeHtmlAttribute(opt_data.containerId) + '"><div class="lfr-ddm-settings-form">';
  var pageList242 = opt_data.pages;
  var pageListLen242 = pageList242.length;
  for (var pageIndex242 = 0; pageIndex242 < pageListLen242; pageIndex242++) {
    var pageData242 = pageList242[pageIndex242];
    output += '<div class="lfr-ddm-form-page' + ((pageIndex242 == 0) ? ' active basic' : '') + ((pageIndex242 == pageListLen242 - 1) ? ' advanced' : '') + '">' + ddm.form_rows(soy.$$augmentMap(opt_data, {rows: pageData242.rows})) + '</div>';
  }
  output += '</div></div>';
  return output;
};
if (goog.DEBUG) {
  ddm.settings_form.soyTemplateName = 'ddm.settings_form';
}
