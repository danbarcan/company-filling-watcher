package com.danb.discord.stocks.watcher.bot;

import com.danb.discord.stocks.watcher.sec.CompanyFillingEntity;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.channel.MessageChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.format.DateTimeFormatter;

@Service
public class DiscordNotifier {
    private static final long STOCK_NEWS_CHANNEL_ID = 810544983303651398L;

    private Mono<MessageChannel> stockNewsChannel;
    private Mono<MessageChannel> errorsChannel;

    @Autowired
    public DiscordNotifier(final GatewayDiscordClient discordClient) {
        stockNewsChannel = discordClient.getChannelById(Snowflake.of(STOCK_NEWS_CHANNEL_ID)).cast(MessageChannel.class);
    }

    public void sendNewCompanyFillingMessage(CompanyFillingEntity companyFillingEntity) {
        String message = String.format("Found new SEC filling for %s at %s. More info: %n%s",
                companyFillingEntity.getTitle(),
                companyFillingEntity.getPubDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                companyFillingEntity.getLink());

        sendMessageToStockChannel(message);
    }

    private void sendMessageToStockChannel(String message) {
        sendMessage(stockNewsChannel, message);
    }

    private void sendMessage(Mono<MessageChannel> channel, String message) {
        channel.flatMap(c -> c.createMessage(message)).subscribe();
    }
}
