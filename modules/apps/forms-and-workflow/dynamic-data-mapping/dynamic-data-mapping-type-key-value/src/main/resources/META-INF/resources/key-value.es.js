import Component from 'metal-component';
import Soy from 'metal-soy';

import templates from './key-value.soy';

/**
 * KeyValue Component
 */
class KeyValue extends Component {}

// Register component
Soy.register(KeyValue, templates, 'render');

export default KeyValue;