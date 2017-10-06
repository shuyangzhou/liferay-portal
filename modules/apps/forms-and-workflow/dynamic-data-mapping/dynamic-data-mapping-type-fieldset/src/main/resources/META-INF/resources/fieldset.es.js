import Component from 'metal-component';
import Soy from 'metal-soy';

import templates from './fieldset.soy';

/**
 * Fieldset Component
 */
class Fieldset extends Component {}

// Register component
Soy.register(Fieldset, templates, 'render');

export default Fieldset;