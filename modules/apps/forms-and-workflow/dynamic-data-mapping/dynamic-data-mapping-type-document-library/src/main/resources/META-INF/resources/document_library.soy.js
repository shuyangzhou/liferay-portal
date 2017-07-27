// This file was automatically generated from document_library.soy.
// Please don't edit this file by hand.

/**
 * @fileoverview Templates in namespace ddm.
 * @hassoydeltemplate {ddm.field}
 * @public
 */

if (typeof ddm == 'undefined') { var ddm = {}; }


ddm.__deltemplate_s2_118789fc = function(opt_data, opt_ignored) {
  return '' + ddm.document_library(opt_data);
};
if (goog.DEBUG) {
  ddm.__deltemplate_s2_118789fc.soyTemplateName = 'ddm.__deltemplate_s2_118789fc';
}
soy.$$registerDelegateFn(soy.$$getDelTemplateId('ddm.field'), 'document_library', 0, ddm.__deltemplate_s2_118789fc);


ddm.document_library = function(opt_data, opt_ignored) {
  return '<div class="form-group' + soy.$$escapeHtmlAttribute(opt_data.visible ? '' : ' hide') + '" data-fieldname="' + soy.$$escapeHtmlAttribute(opt_data.name) + '">' + ((opt_data.showLabel) ? '<label class="control-label">' + soy.$$escapeHtml(opt_data.label) + ((opt_data.required) ? '<span class="icon-asterisk text-warning"></span>' : '') + '</label>' + ((opt_data.tip) ? '<p class="liferay-ddm-form-field-tip">' + soy.$$escapeHtml(opt_data.tip) + '</p>' : '') : '') + '<div class="form-builder-document-library-field">' + ((opt_data.fileEntryURL) ? ddm.card_item(opt_data) : '<div class="input-group"><input class="field form-control" dir="' + soy.$$escapeHtmlAttribute(opt_data.dir) + '" id="inputFile" type="text" value="' + soy.$$escapeHtmlAttribute(opt_data.fileEntryTitle ? opt_data.fileEntryTitle : '') + '" readonly>' + ddm.button_group(opt_data) + '</div>') + '<input class="field form-control" name="' + soy.$$escapeHtmlAttribute(opt_data.name) + '" type="hidden" value="' + soy.$$escapeHtmlAttribute(opt_data.value ? opt_data.value : '') + '"></div></div>';
};
if (goog.DEBUG) {
  ddm.document_library.soyTemplateName = 'ddm.document_library';
}


ddm.button_group = function(opt_data, opt_ignored) {
  return '<div class="input-group-btn"><button class="btn btn-default select-button" ' + ((opt_data.readOnly) ? 'disabled' : '') + ' type="button"><span class="lfr-btn-label">' + soy.$$escapeHtml(opt_data.strings.select) + '</span></button><button class="btn btn-default clear-button ' + ((! opt_data.clearButtonVisible) ? 'hide' : '') + '" ' + ((opt_data.readOnly) ? 'disabled' : '') + ' ' + ((! opt_data.clearButtonVisible) ? 'hidden="hidden" style="display: none;" type="button"' : '') + '><svg class="lexicon-icon"><use xlink:href="' + soy.$$escapeHtmlAttribute(soy.$$filterNormalizeUri(opt_data.lexiconIconsPath)) + 'times" /></svg></button></div>';
};
if (goog.DEBUG) {
  ddm.button_group.soyTemplateName = 'ddm.button_group';
}


ddm.card_item = function(opt_data, opt_ignored) {
  return '<div class="card card-horizontal"><div class="card-row card-row-padded"><div class="card-col-content card-col-gutters"><h4 class="truncate-text" title="' + soy.$$escapeHtmlAttribute(opt_data.fileEntryTitle) + '">' + soy.$$escapeHtml(opt_data.fileEntryTitle) + '</h4></div><div class="card-col-field"><a href="' + soy.$$escapeHtmlAttribute(soy.$$filterNormalizeUri(opt_data.fileEntryURL)) + '" download="' + soy.$$escapeHtmlAttribute(opt_data.fileEntryTitle) + '"><svg class="lexicon-icon"><use xlink:href="' + soy.$$escapeHtmlAttribute(soy.$$filterNormalizeUri(opt_data.lexiconIconsPath)) + 'download" /></svg></a></div></div></div>';
};
if (goog.DEBUG) {
  ddm.card_item.soyTemplateName = 'ddm.card_item';
}
