import Component from 'metal-component';
import Soy from 'metal-soy';

import templates from './checkbox.soy';

/**
 * Checkbox Component
 */
class Checkbox extends Component {}

// Register component
Soy.register(Checkbox, templates, 'render');

export default Checkbox;