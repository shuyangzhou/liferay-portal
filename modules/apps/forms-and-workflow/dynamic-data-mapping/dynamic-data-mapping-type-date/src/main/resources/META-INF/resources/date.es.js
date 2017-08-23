import Component from 'metal-component';
import Soy from 'metal-soy';

import templates from './date.soy';

/**
 * Date Component
 */
class Date extends Component {}

// Register component
Soy.register(Date, templates, 'render');

export default Date;