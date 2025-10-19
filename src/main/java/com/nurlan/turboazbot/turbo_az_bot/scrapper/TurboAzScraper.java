package com.nurlan.turboazbot.turbo_az_bot.scrapper;

import com.nurlan.turboazbot.turbo_az_bot.entity.CarAd;
import com.nurlan.turboazbot.turbo_az_bot.repo.CarAddRepo;
import jakarta.transaction.Transactional;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Component
public class TurboAzScraper {

    List<CarAd> list=new ArrayList<>();
    private final CarAddRepo carAddRepo;

    public TurboAzScraper(CarAddRepo carAddRepo) {
        this.carAddRepo = carAddRepo;
    }

    @Transactional //bu trancatinalida aarasdirarsan
    public List<CarAd> scrapeAndSave() throws IOException {
        List<CarAd> list = new ArrayList<>();
        Set<String> existingIds = new HashSet<>(carAddRepo.findAllExternalIds());//bazada olan id ler
        Set<String> newIds = new HashSet<>();// listde olan id ler

            String url = "https://turbo.az/autos";

            try {
                Document document = Jsoup.connect(url).get();
                Elements elements = document.select(".products-i");

                for (Element element : elements) {


                    String title = element.select(".products-i__name").text();
                    Integer price =Integer.parseInt(element.select(".products-i__price").text().replaceAll("[^0-9]", ""));
                    Integer il = Integer.parseInt(element.select(".products-i__attributes ").text().split(",")[0].trim());
                    LocalTime time = LocalTime.parse(element.select(".products-i__datetime").text().replaceAll(".*?(\\d{2}:\\d{2}).*", "$1"));

                    String link = "https://turbo.az" + element.select(".products-i__link").attr("href");
                    String externalId = link.substring(link.lastIndexOf("/") + 1).split("-")[0];


                    if (existingIds.contains(externalId) || newIds.contains(externalId)) {
                        continue;
                    }

                    CarAd carAd = new CarAd();
                    carAd.setTitle(title);
                    carAd.setPrice(price);
                    carAd.setCreatedAt(time);
                    carAd.setYear(il);
                    carAd.setLink(link);
                    carAd.setExternalId(externalId);

                    list.add(carAd);
                    newIds.add(externalId);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        carAddRepo.saveAll(list);
        return list;
    }
}
