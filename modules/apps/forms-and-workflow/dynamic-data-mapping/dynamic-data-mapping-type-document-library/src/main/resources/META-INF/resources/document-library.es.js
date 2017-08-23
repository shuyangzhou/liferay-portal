import Component from 'metal-component';
import Soy from 'metal-soy';

import templates from './document-library.soy';

/**
 * DocumentLibrary Component
 */
class DocumentLibrary extends Component {}

// Register component
Soy.register(DocumentLibrary, templates, 'render');

export default DocumentLibrary;