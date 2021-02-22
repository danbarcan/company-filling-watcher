package com.danb.discord.stocks.watcher.sec;

import com.apptastic.rssreader.Item;
import com.apptastic.rssreader.RssReader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ReadSecGovRSSFeed {
    private final String RSS_FEED_URL = "https://www.sec.gov/cgi-bin/browse-edgar?action=getcurrent&CIK=&type=425&company=&dateb=&owner=include&start=0&count=100&output=atom";

    private Stream<CompanyFillingEntity> readRssFeed() throws IOException {
        RssReader reader = new RssReader();
        Stream<Item> rssFeed = reader.read(RSS_FEED_URL);

        return rssFeed.map(CompanyFillingEntity::mapItemToCompanyFilling);
    }

    public CompanyFillingEntity getLastCompanyFilling() throws IOException {
        return readRssFeed().findFirst().get();
    }

    public List<CompanyFillingEntity> getCompanyFillingsAfter(CompanyFillingEntity lastCompanyFillingEntity) throws IOException {
        return readRssFeed()
                .filter(companyFillingEntity -> !companyFillingEntity.getTitle().equalsIgnoreCase(lastCompanyFillingEntity.getTitle()) &&
                        companyFillingEntity.getPubDate().isAfter(lastCompanyFillingEntity.getPubDate()))
                .collect(Collectors.toList());
    }
}
