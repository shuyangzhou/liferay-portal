import Component from 'metal-component';
import Soy from 'metal-soy';

import templates from './captcha.soy';

/**
 * Captcha Component
 */
class Captcha extends Component {}

Captcha.STATE = {
	html: {
		isHtml: true,
		value: ''
	}
};

// Register component
Soy.register(Captcha, templates, 'render');

Captcha.Soy = Soy;

export default Captcha;