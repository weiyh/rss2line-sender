package com.weiyuhan.rss.sender.util;

import com.sun.xml.internal.stream.events.StartElementEvent;
import com.weiyuhan.rss.sender.dto.FeedMessageDTO;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class RSSFeedParser {
    private static final String ENTRY = "entry";

    private static final String TITLE = "title";
    private static final String LINK = "link";
    private static final String CONTENT = "content";
    private static final String PUBLISHED_DATE = "published";

    public List<FeedMessageDTO> readFeed(String url) {
        FeedMessageDTO feedMessageDTO = new FeedMessageDTO();
        List<FeedMessageDTO> feedMessageDTOs = new ArrayList<>();

        try {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = read(new URL(url));

            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement()) {
                    String localPart = event.asStartElement().getName().getLocalPart();
                    switch (localPart) {
                        case ENTRY:
                            feedMessageDTO = new FeedMessageDTO();
                            break;
                        case TITLE:
                            feedMessageDTO.setTitle(getCharacterData(event, eventReader));
                            break;
                        case LINK:
                            feedMessageDTO.setLink(getLinkCharacterData(event));
                            break;
                        case PUBLISHED_DATE:
                            try {
                                Date publishedDate = DateUtil.addHourOffsetToDate(DateUtil.parseToDate(getCharacterData(event, eventReader)), 8);
                                feedMessageDTO.setPublishedDate(DateUtil.formatDateToString(publishedDate));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            break;
                        case CONTENT:
                            feedMessageDTO.setContent(getCharacterData(event, eventReader));
                            break;
                    }
                } else if (event.isEndElement()) {
                    if (event.asEndElement().getName().getLocalPart() == (ENTRY)) {
                        FeedMessageDTO feedMessageDTOtoSave = new FeedMessageDTO();
                        feedMessageDTOtoSave.setTitle(feedMessageDTO.getTitle());
                        feedMessageDTOtoSave.setLink(feedMessageDTO.getLink());
                        feedMessageDTOtoSave.setPublishedDate(feedMessageDTO.getPublishedDate());
                        feedMessageDTOtoSave.setContent(feedMessageDTO.getContent());
                        feedMessageDTOs.add(feedMessageDTOtoSave);
                        continue;
                    }
                }
            }
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return feedMessageDTOs;
    }

    private String getCharacterData(XMLEvent event, XMLEventReader eventReader) throws XMLStreamException {
        String result = "";
        event = eventReader.nextEvent();
        if (event instanceof Characters) {
            result = event.asCharacters().getData();
        }
        return result;
    }

    private String getLinkCharacterData(XMLEvent event) throws XMLStreamException {
        String result = "";
        Iterator<Attribute> attributeIterator = ((StartElementEvent) event).getAttributes();
        while (attributeIterator.hasNext()) {
            result = attributeIterator.next().getValue();
        }
        return result;
    }

    private InputStream read(URL url) {
        try {
            return url.openStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}