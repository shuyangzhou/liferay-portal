import Component from 'metal-component';
import Soy from 'metal-soy';

import templates from './paragraph.soy';

/**
 * Paragraph Component
 */
class Paragraph extends Component {}

// Register component
Soy.register(Paragraph, templates, 'render');

export default Paragraph;