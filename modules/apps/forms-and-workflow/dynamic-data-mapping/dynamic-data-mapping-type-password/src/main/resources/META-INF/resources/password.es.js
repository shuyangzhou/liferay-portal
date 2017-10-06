import Component from 'metal-component';
import Soy from 'metal-soy';

import templates from './password.soy';

/**
 * Password Component
 */
class Password extends Component {}

// Register component
Soy.register(Password, templates, 'render');

export default Password;