package com.igalia.phpreport.mylyn.internal.phpreport;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

class PHPReportErrorResponseHandler extends DefaultHandler
{
	private String errorMessage = null;
	private StringBuilder error_characters = null;
	private boolean success = true;

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (error_characters != null)
		error_characters.append(ch, start, length);
	}
	
	@Override
	public void startElement(String uri, String localName,
			String qName, Attributes attributes)
			throws SAXException {
		if ("error".equals(qName))
		{
			error_characters = new StringBuilder();
			success = false;
		}
	}
	@Override
	public void endElement(String uri, String localName,
			String qName) throws SAXException {
		if ("error".equals(qName))
			errorMessage = error_characters.toString();

		error_characters = null;
	}
	
	public String getErrorMessage()
	{
		return errorMessage;
	}
	
	public boolean isSucceed ()
	{
		return success;
	}
}