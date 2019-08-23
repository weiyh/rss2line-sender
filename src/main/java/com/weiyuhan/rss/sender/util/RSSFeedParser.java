package com.weiyuhan.rss.sender.util;

import com.weiyuhan.rss.sender.dto.FeedMessageDTO;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RSSFeedParser {
    private static final String ENTRY = "entry";

    private static final String TITLE = "title";
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
                    String characterData = getCharacterData(event, eventReader);
                    switch (localPart) {
                        case ENTRY:
                            feedMessageDTO = new FeedMessageDTO();
                            break;
                        case TITLE:
                            feedMessageDTO.setTitle(characterData);
//                            appendBoldStringToPreviousString(event, eventReader, feedMessageDTO);
                            break;
                        case PUBLISHED_DATE:
                            feedMessageDTO.setPublishedDate(characterData);
                            break;
                        case CONTENT:
                            feedMessageDTO.setContent(characterData);
//                            appendBoldStringToPreviousString(event, eventReader, feedMessageDTO);
                            break;
                    }
                } else if (event.isEndElement()) {
                    if (event.asEndElement().getName().getLocalPart() == (ENTRY)) {
                        FeedMessageDTO feedMessageDTOtoSave = new FeedMessageDTO();
                        feedMessageDTOtoSave.setTitle(feedMessageDTO.getTitle());
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

    private InputStream read(URL url) {
        try {
            return url.openStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
        This method is to append the string between and after <b>...</b> to previous string.
        For example A<b>B</b> will be AB.
     */
//    private void appendBoldStringToPreviousString(XMLEvent event, XMLEventReader eventReader, FeedMessageDTO feedMessageDTO) {
//        try {
//            StringBuilder boldString = new StringBuilder();
//            while (eventReader.hasNext() && !event.isEndElement()) {
//                if (eventReader.nextEvent().getEventType() == 4) {
//                    if (getCharacterData(event, eventReader) == "<"
//                            || getCharacterData(event, eventReader) == "b"
//                            || getCharacterData(event, eventReader) == "/>") {
//                        event = eventReader.nextEvent();
//                        continue;
//                    }
//                    boldString.append(getCharacterData(event, eventReader));
//                }
//                event = eventReader.nextEvent();
//            }
//            String localPart = event.asEndElement().getName().getLocalPart();
//            switch (localPart) {
//                case TITLE:
//                    feedMessageDTO.setTitle(boldString.insert(0, feedMessageDTO.getTitle()).toString());
//                    break;
//                case CONTENT:
//                    feedMessageDTO.setContent(boldString.insert(0, feedMessageDTO.getContent()).toString());
//                    break;
//            }
//        } catch (XMLStreamException e) {
//            throw new RuntimeException(e);
//        }
//    }
}