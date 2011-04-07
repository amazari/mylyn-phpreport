package com.igalia.phpreport.mylyn.internal.phpreport;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

final class PHPReportMethodResponseHandler extends PHPReportErrorResponseHandler {

	private StringBuilder characters;
	boolean success = false;

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (characters != null)
			characters.append(ch, start, length);
		super.characters(ch, start, length);
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if ("success".equals(qName))
			characters = new StringBuilder();
		super.startElement(uri, localName, qName, attributes);
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if ("success".equals(qName))
		{
			success = Boolean.parseBoolean(characters.toString());
			characters = null;
		}
		super.endElement(uri, localName, qName);

	}

	@Override
	public boolean isSucceed() {
		return success;
	}
}