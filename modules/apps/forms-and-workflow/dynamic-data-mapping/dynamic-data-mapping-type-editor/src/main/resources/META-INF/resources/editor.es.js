import Component from 'metal-component';
import Soy from 'metal-soy';

import templates from './editor.soy';

/**
 * Editor Component
 */
class Editor extends Component {}

// Register component
Soy.register(Editor, templates, 'render');

export default Editor;