import Component from 'metal-component';
import Soy from 'metal-soy';

import templates from './text.soy';

/**
 * Text Component
 */
class Text extends Component {}

// Register component
Soy.register(Text, templates, 'render');

export default Text;