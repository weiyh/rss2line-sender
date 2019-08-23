package com.weiyuhan.rss.sender.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

import com.weiyuhan.rss.sender.dto.FeedMessageDTO;
import com.weiyuhan.rss.sender.service.RSSFeedService;
import com.weiyuhan.rss.sender.util.RSSFeedParser;

@Component
public class RSSFeedServiceImpl implements RSSFeedService {

    private static final Logger logger = LoggerFactory.getLogger(RSSFeedServiceImpl.class);

    @Override
    public List<FeedMessageDTO> parseRSSFeed(String rssFeedUrl) throws IOException {

        RSSFeedParser rssFeedParser = new RSSFeedParser();
        return rssFeedParser.readFeed(rssFeedUrl);
    }
}
