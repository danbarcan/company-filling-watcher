package com.danb.discord.stocks.watcher.sec;

import com.apptastic.rssreader.Item;
import lombok.Builder;
import lombok.Data;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
public class CompanyFillingEntity {
    private String title;
    private String link;
    private ZonedDateTime pubDate;

    public static CompanyFillingEntity mapItemToCompanyFilling(Item item) {
        return CompanyFillingEntity.builder()
                .link(item.getLink().orElse(null))
                .title(item.getTitle().get())
                .pubDate(ZonedDateTime.from(DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(item.getPubDate().get())).withZoneSameInstant(ZoneId.systemDefault()))
                .build();
    }

}
