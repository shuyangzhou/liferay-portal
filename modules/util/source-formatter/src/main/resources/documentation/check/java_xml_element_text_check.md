## JavaXMLElementTextCheck

Parsing XML builds a new parser for every call, which costs far more than reading a few texts from a small document. The check rewrites code that parses XML only to read the texts of child elements of its root into `XMLUtil.getElementText` calls on the XML string, and removes the `Document` and `Element` variables it no longer needs.

It rewrites a `Document` that `SAXReaderUtil.read`, `UnsecureSAXReaderUtil.read`, or the `read` method of a `SAXReader` reads from a single `String` variable, when the document is used only for its root element and the root element only for `elementText` or `element(name).getText()` with a string literal. It leaves alone a validating parse, a parse whose elements are walked or whose attributes are read, `elementTextTrim`, which also collapses whitespace inside the text, and files that mention `DocumentException`, where removing the parse could leave a `catch` block that no longer compiles. Tests are not checked.

`XMLUtil.getElementText` returns the text of the first element with the name anywhere in the XML, decodes the entity references that dom4j writes in text (`&amp;`, `&lt;`, and `&gt;`), and returns an empty string for an empty element, so it fits small documents whose elements hold plain text, without attributes or CDATA sections.

### Example

Incorrect:

```java
Document document = SAXReaderUtil.read(assetEntryXml);

Element rootElement = document.getRootElement();

String assetEntryUuid = rootElement.elementText("asset-entry-uuid");
```

Correct:

```java
String assetEntryUuid = XMLUtil.getElementText(
	assetEntryXml, "asset-entry-uuid");
```