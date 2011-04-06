package com.igalia.phpreport.mylyn;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

final class PHPReportAuthenticationResponseHandler extends
PHPReportErrorResponseHandler {

	private StringBuilder characters = null;
	private String token = null;

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (characters != null)
			characters.append(ch, start, length);
		super.characters(ch, start, length);
	}

	@Override
	public void startElement(String uri, String localName,
			String qName, Attributes attributes)
			throws SAXException {
		if ("sessionId".equals(qName))
			characters = new StringBuilder();
		super.startElement(uri, localName, qName, attributes);
	}

	@Override
	public void endElement(String uri, String localName,
			String qName) throws SAXException {
		if ("sessionId".equals(qName))
		{
			token = characters.toString();
			characters = null;
		}
		super.endElement(uri, localName, qName);
	}
	
	public String getToken()
	{
		return token;
	}
}