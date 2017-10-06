import Component from 'metal-component';
import Soy from 'metal-soy';

import templates from './options.soy';

/**
 * Options Component
 */
class Options extends Component {}

// Register component
Soy.register(Options, templates, 'render');

export default Options;