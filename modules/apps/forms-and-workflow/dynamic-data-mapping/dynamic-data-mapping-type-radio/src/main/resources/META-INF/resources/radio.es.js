import Component from 'metal-component';
import Soy from 'metal-soy';

import templates from './radio.soy';

/**
 * Radio Component
 */
class Radio extends Component {}

// Register component
Soy.register(Radio, templates, 'render');

export default Radio;