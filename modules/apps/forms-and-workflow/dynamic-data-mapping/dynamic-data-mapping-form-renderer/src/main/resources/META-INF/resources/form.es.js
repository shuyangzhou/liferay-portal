import Component from 'metal-component';
import Soy from 'metal-soy';

import templates from './form.soy';

let FormTemplates = [];

if (!window.ddm) {
	window.ddm = {

	};
}

for (let template in templates) {
	if (template !== 'templates') {
		class C extends Component {};
		Soy.register(C, templates, template);
		FormTemplates.push(
			{
				key: template,
				component: C
			}
		);
		window.ddm[template] = C;
	}
}

export default FormTemplates;