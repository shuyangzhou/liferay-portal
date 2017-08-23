import Component from 'metal-component';
import Soy from 'metal-soy';

import templates from './validation.soy';

/**
 * Validation Component
 */
class Validation extends Component {}

// Register component
Soy.register(Validation, templates, 'render');

export default Validation;