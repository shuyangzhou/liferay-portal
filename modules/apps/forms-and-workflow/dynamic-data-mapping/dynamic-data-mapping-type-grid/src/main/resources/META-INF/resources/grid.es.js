import Component from 'metal-component';
import Soy from 'metal-soy';

import templates from './grid.soy';

/**
 * Grid Component
 */
class Grid extends Component {}

// Register component
Soy.register(Grid, templates, 'render');

export default Grid;