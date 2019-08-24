package com.weiyuhan.rss.sender.scheduler;

import com.weiyuhan.rss.sender.dto.FeedMessageDTO;
import com.weiyuhan.rss.sender.service.LineService;
import com.weiyuhan.rss.sender.service.RSSFeedService;
import com.weiyuhan.rss.sender.util.DateUtil;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Component
public class RSSFeedScheduler {

    private static final String URL_1 = "https://www.google.com/alerts/feeds/01805223035926696686/13582058751037210457"; //暖化
    private static final String URL_2 = "https://www.google.com/alerts/feeds/01805223035926696686/18140882255345228665"; //氣候
    private static final String URL_3 = "https://www.google.com/alerts/feeds/01805223035926696686/12792900969870785419"; //氣候變遷
    private static final String URL_4 = "https://www.google.com/alerts/feeds/01805223035926696686/6065830801246775709"; //減碳

    private static final String UUID = "Uadd579112ca5db174262efcd24a8143f";

    @Autowired
    private RSSFeedService rssFeedService;

    @Autowired
    private LineService lineService;

    @Scheduled(cron = "${cron.rss.feed.sender}")
    public void findAndPushRSSFeed() throws IOException {
        List<FeedMessageDTO> feedMessageDTOs = new ArrayList<>();
        feedMessageDTOs.addAll(rssFeedService.parseRSSFeed(URL_1));
        feedMessageDTOs.addAll(rssFeedService.parseRSSFeed(URL_2));
        feedMessageDTOs.addAll(rssFeedService.parseRSSFeed(URL_3));
        feedMessageDTOs.addAll(rssFeedService.parseRSSFeed(URL_4));
        if (hasNewFeedMessage(feedMessageDTOs)) {
            for (FeedMessageDTO feedMessageDTO : feedMessageDTOs) {
                String message = generateMessage(feedMessageDTO);
                lineService.pushText(UUID, message);
            }
        }
    }

    private String generateMessage(FeedMessageDTO feedMessageDTO) {
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("Title: ").append(feedMessageDTO.getTitle()).append("\n")
                .append("Time: ").append(feedMessageDTO.getPublishedDate()).append("\n")
                .append("Content: ").append(feedMessageDTO.getContent()).append("\n")
                .append("Link:: ").append(feedMessageDTO.getContent()).append("\n");
        return messageBuilder.toString();
    }

    private Boolean hasNewFeedMessage(List<FeedMessageDTO> feedMessageDTOs) {
        Boolean result = Boolean.FALSE;
        Date oneHourBeforeNow = DateUtil.addHourOffsetToDate(new Date(), -1);

        Iterator iterator = feedMessageDTOs.iterator();
        while (iterator.hasNext()) {
            FeedMessageDTO feedMessageDTO = (FeedMessageDTO) iterator.next();
            try {
                Date feedMessagePublishedDate = DateUtil.parseToDate(feedMessageDTO.getPublishedDate());
                if (feedMessagePublishedDate.before(oneHourBeforeNow)) {
                    iterator.remove();
                }
            } catch (ParseException e) {
                throw new RuntimeException();
            }
        }

        if (CollectionUtils.isNotEmpty(feedMessageDTOs)) {
            result = Boolean.TRUE;
        }

        return result;
    }
}
