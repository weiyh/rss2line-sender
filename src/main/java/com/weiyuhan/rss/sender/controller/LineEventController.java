package com.weiyuhan.rss.sender.controller;

import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.AudioMessageContent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.event.message.StickerMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.message.VideoMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import com.weiyuhan.rss.sender.service.LineService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@LineMessageHandler
public class LineEventController {

    private static final Logger logger = LoggerFactory.getLogger(LineEventController.class);

    @Autowired
    private LineService lineService;

    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        String replyToken = event.getReplyToken();
        TextMessageContent message = event.getMessage();
        String text = message.getText();

        logger.info("Got text message from replyToken:{}: text:{}", replyToken, text);

        switch (text) {
            case "hi":
                lineService.replyText(replyToken, "Hi!");
                break;
        }
    }

    @EventMapping
    public Message handleStickerMessage(MessageEvent<StickerMessageContent> event) {
        return new TextMessage("スタンプ送信ありがとうございます！");
    }

    @EventMapping
    public Message handleImageMessage(MessageEvent<ImageMessageContent> event) {
        return new TextMessage("画像送信ありがとうございます！");
    }

    @EventMapping
    public Message handleVideoMessage(MessageEvent<VideoMessageContent> event) {
        return new TextMessage("動画送信ありがとうございます！");
    }

    @EventMapping
    public Message handleAudioMessage(MessageEvent<AudioMessageContent> event) {
        return new TextMessage("音声送信ありがとうございます！");
    }

    @EventMapping
    public Message handleFollowEvent(FollowEvent event) {
        return new TextMessage("友達追加ありがとうございます！");
    }
}
