import Component from 'metal-component';
import Soy from 'metal-soy';

import templates from './captcha.soy';

/**
 * Captcha Component
 */
class Captcha extends Component {}

// Register component
Soy.register(Captcha, templates, 'render');

export default Captcha;