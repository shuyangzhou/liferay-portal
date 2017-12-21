AUI.add(
	'liferay-workflow-web',
	function(A) {
		var WorkflowWeb = {
			previewBeforeRevert: function(event, renderUrl, actionUrl, title) {
				var instance = this;

				var form = A.Node.create('<form />');

				form.setAttribute('action', actionUrl);
				form.setAttribute('method', 'POST');

				var dialog = Liferay.Util.Window.getWindow(
					{
						dialog: {
							destroyOnHide: true,
							modal: true,
							toolbars: {
								footer: [
									{
										cssClass: 'btn btn-secondary',
										discardDefaultButtonCssClasses: true,
										label: Liferay.Language.get('cancel'),
										on: {
											click: function() {
												dialog.destroy();
											}
										}
									},
									{
										cssClass: 'btn btn-primary',
										discardDefaultButtonCssClasses: true,
										label: Liferay.Language.get('restore'),
										on: {
											click: function() {
												submitForm(form);
											}
										}
									}
								],
								header: [
									{
										cssClass: 'close',
										discardDefaultButtonCssClasses: true,
										labelHTML: '<svg class="lexicon-icon" focusable="false"><use data-href="' + Liferay.ThemeDisplay.getPathThemeImages() + '/lexicon/icons.svg#times" /><title>' + Liferay.Language.get('close') + '</title></svg>',
										on: {
											click: function(event) {
												dialog.destroy();

												event.domEvent.stopPropagation();
											}
										}
									}
								]
							}
						},
						title: title,
						uri: renderUrl
					}
				);
			},

			showSuccessMessage: function() {
				var instance = this;

				var successMessage = Liferay.Language.get('definition-imported-sucessfully');

				var alert = instance._alert;

				if (alert) {
					alert.destroy();
				}

				alert = new Liferay.Alert(
					{
						closeable: true,
						delay: {
							hide: 3000,
							show: 0
						},
						icon: 'check',
						message: successMessage,
						type: 'success'
					}
				);

				if (!alert.get('rendered')) {
					alert.render('.portlet-column');
				}

				alert.show();

				instance._alert = alert;
			},

			_alert: null
		};

		Liferay.WorkflowWeb = WorkflowWeb;
	},
	'',
	{
		requires: ['liferay-alert', 'liferay-util-window']
	}
);