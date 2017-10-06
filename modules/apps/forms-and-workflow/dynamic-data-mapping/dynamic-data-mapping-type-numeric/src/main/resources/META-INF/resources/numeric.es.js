import Component from 'metal-component';
import Soy from 'metal-soy';

import templates from './numeric.soy';

/**
 * Numeric Component
 */
class Numeric extends Component {}

// Register component
Soy.register(Numeric, templates, 'render');

export default Numeric;