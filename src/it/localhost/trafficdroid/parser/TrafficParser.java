package it.localhost.trafficdroid.parser;

import it.localhost.trafficdroid.common.Const;
import it.localhost.trafficdroid.dao.TrafficDAO;
import it.localhost.trafficdroid.dto.MainDTO;
import it.localhost.trafficdroid.dto.StreetDTO;
import it.localhost.trafficdroid.dto.ZoneDTO;
import it.localhost.trafficdroid.exception.BadConfException;
import it.localhost.trafficdroid.exception.ConnectionException;
import it.localhost.trafficdroid.exception.GenericException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class TrafficParser extends DefaultHandler {
	private MainDTO dto;
	private String url;
	private StreetDTO street;
	private int zoneCounter;
	private StringBuilder buf;
	private boolean inSector = false;
	private ZoneDTO currZone;

	public TrafficParser(MainDTO dto, String url) {
		this.dto = dto;
		this.url = url;
	}

	public void parse() throws GenericException, BadConfException, ConnectionException {
		try {
			XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
			xmlReader.setContentHandler(this);
			for (StreetDTO street : dto.getStreets()) {
				this.street = street;
				zoneCounter = 0;
				try {
					xmlReader.parse(TrafficDAO.getData(street.getId(), url));
				} catch (SAXException e) {
				}
			}
		} catch (ParserConfigurationException e) {
			throw new GenericException(e);
		} catch (SAXException e) {
			throw new ConnectionException(e);
		} catch (IOException e) {
			throw new ConnectionException(e);
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		buf = new StringBuilder();
		if (zoneCounter == street.getZonesSize())
			throw new SAXException();
		if (localName.equals(Const.SECTOR_ELEMENT))
			inSector = true;
	}

	@Override
	public void characters(char[] ch, int start, int length) {
		buf.append(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String qName) {
		if (buf.length() != 0) {
			if (inSector) {
				if (localName.equals(Const.LABEL_ELEMENT))
					currZone = street.getZone(buf.toString());
				else if (currZone != null && localName.equals(Const.KM_ELEMENT))
					currZone.setKm(buf.toString());
				else if (currZone != null && localName.equals(Const.DIRA_ELEMENT))
					currZone.setSpeedLeft(buf.toString());
				else if (currZone != null && localName.equals(Const.DIRB_ELEMENT))
					currZone.setSpeedRight(buf.toString());
				else if (localName.equals(Const.SECTOR_ELEMENT)) {
					inSector = false;
					if (currZone != null)
						zoneCounter++;
				}
			} else {
				if (localName.equals(Const.STARTDIR_ELEMENT))
					street.setDirectionLeft(buf.toString());
				else if (localName.equals(Const.ENDDIR_ELEMENT))
					street.setDirectionRight(buf.toString());
			}
		}
	}
}