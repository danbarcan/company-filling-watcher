package com.danb.discord.stocks.watcher.sec;

import com.danb.discord.stocks.watcher.bot.DiscordNotifier;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class ReadSecGovScheduler {
    private static CompanyFillingEntity lastCompanyFilling = null;

    private final ReadSecGovRSSFeed readSecGovRSSFeed;
    private final DiscordNotifier discordNotifier;

    @Scheduled(cron = "0 59 9 * * MON-FRI")
//    @Scheduled(initialDelay = 1000L)
    public void checkSecGovForLastCompanyFilling() {
        try {
            lastCompanyFilling = readSecGovRSSFeed.getLastCompanyFilling();
            discordNotifier.sendNewCompanyFillingMessage(lastCompanyFilling);
            log.info("Sending: {}", lastCompanyFilling);
        } catch (IOException e) {
            log.error("Failed to get last company filling.", e);
            //todo: send error message on discord
        }
    }

    @Scheduled(cron = "0 */1 10-23 * * MON-FRI")
//    @Scheduled(initialDelay = 5000L, fixedRate = 60000L)
    public void checkSecGovAndSendDiscordNotifications() {
        try {
            List<CompanyFillingEntity> latestCompanyFillings = readSecGovRSSFeed.getCompanyFillingsAfter(lastCompanyFilling);
            latestCompanyFillings.stream().forEach(discordNotifier::sendNewCompanyFillingMessage);
            if (!latestCompanyFillings.isEmpty()) {
                lastCompanyFilling = latestCompanyFillings.get(latestCompanyFillings.size() - 1);
                log.info("Sending: {}", latestCompanyFillings);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Failed to get company fillings made after: " + lastCompanyFilling, e);
            //todo: send error message on discord
        }
    }

    //@Scheduled(initialDelay = 1000L, fixedRate = 60000L)
    public void getLastFilling() {
        log.info("Getting last company filling.");
        try {
            CompanyFillingEntity lastFilling = readSecGovRSSFeed.getLastCompanyFilling();
            discordNotifier.sendNewCompanyFillingMessage(lastFilling);
            log.info("Company filling sent to discord: {}", lastFilling);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getLocalizedMessage());
        }
    }
}
