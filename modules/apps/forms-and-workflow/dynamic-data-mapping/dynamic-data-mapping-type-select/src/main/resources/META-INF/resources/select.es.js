import Component from 'metal-component';
import Soy from 'metal-soy';

import templates from './select.soy';

let SelectTemplates = [];

for (let template in templates) {
	if (template !== 'templates') {
		class C extends Component {};
		Soy.register(C, templates, template);
		C.Soy = Soy;
		SelectTemplates.push({
			key: template,
			component: C
		});
	}
}

export default SelectTemplates;