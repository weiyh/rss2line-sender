package com.weiyuhan.rss.sender.service;

import java.io.IOException;
import java.util.List;

import com.weiyuhan.rss.sender.dto.FeedMessageDTO;

public interface RSSFeedService {

    List<FeedMessageDTO> parseRSSFeed(String rssFeedUrl) throws IOException;

}
