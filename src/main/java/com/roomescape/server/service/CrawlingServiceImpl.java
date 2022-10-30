package com.roomescape.server.service;

import com.roomescape.server.config.ChromeDriverContext;
import com.roomescape.server.entity.EscapeCafe;
import com.roomescape.server.entity.Store;
import com.roomescape.server.model.StoreDto;
import com.roomescape.server.model.ThemeDto;
import com.roomescape.server.repository.StoreRepository;
import com.roomescape.server.repository.ThemeRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CrawlingServiceImpl implements CrawlingService {
    private static final Logger logger = LoggerFactory.getLogger(CrawlingServiceImpl.class);
    private final String nowDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    private final StoreRepository storeRepository;
    private final ThemeRepository themeRepository;

    private final ChromeDriverContext context;

    @Override
    public List<StoreDto> getStore() {
        logger.info("@CrawlingServiceImpl: getStore start");
        List<WebElement> elements = getElementsByCssSelector("#zizum_data > a");
        List<StoreDto> storeDtoList = new ArrayList<>();
        for (WebElement element : elements) {
            String storeId = getIdBySubstr(element.getAttribute("href"));
            Document doc = Jsoup.parse(getStoreDetail(storeId));
            String tel = doc.select(".notice").select("dd").get(0).text();
            String location = doc.select(".notice").select("dd").get(3).text();
            if (tel.isBlank() || location.isBlank()) continue;

            StoreDto storeDto = StoreDto.builder()
                    .id(Long.parseLong(storeId))
                    .name(element.getText())
                    .tel(tel)
                    .location(location)
                    .escapeCafe(EscapeCafe.KEYESCAPE)
                    .build();
            storeDtoList.add(storeDto);
            storeRepository.save(storeDto.toEntity());
        }
        return storeDtoList;
    }

    private List<WebElement> getElementsByCssSelector(String pattern) {
        WebDriver driver = context.chromeDriver();
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            logger.info("Error : msg={}, cause={}", e.getMessage(), e.getCause());
        }

        driver.get(EscapeCafe.KEYESCAPE.getUrl() + "make");
        webDriverWait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector(pattern))
        );
        return driver.findElements(By.cssSelector(pattern));
    }

    @Override
    public List<ThemeDto> getTheme() {
        logger.info("@CrawlingServiceImpl: getTheme start");
        List<Store> storeList = storeRepository.findAll();
        List<ThemeDto> themeDtoList = new ArrayList<>();
        for (Store store : storeList) {
            Document doc = Jsoup.parse(getThemeDetail(Long.toString(store.getId())));
            List<Element> elements = doc.select("#contents").select(".s_contents").select(".in_Layer").select("a");
            for (Element element : elements) {
                String themeId = getIdBySubstr(element.attr("href"));
                ThemeDto themeDto = ThemeDto.builder()
                        .id(Long.parseLong(themeId))
                        .name(element.text())
                        .storeId(store.getId())
                        .build();
                themeDtoList.add(themeDto);
                themeRepository.save(themeDto.toEntity(store));
            }
        }
        return themeDtoList;
    }

    private String getIdBySubstr(String idInfo) {
        int firstIdx = idInfo.indexOf("'") + 1;
        int secondIdx = idInfo.indexOf("'", firstIdx);
        return idInfo.substring(firstIdx, secondIdx);
    }

    private String getThemeDetail(String storeId) {
        logger.info("@CrawlingServiceImpl: getThemeDetail start");
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("zizum_num", storeId);
        parameters.add("rev_days", nowDate);

        String url = EscapeCafe.KEYESCAPE.getUrl() + "theme";
        RestTemplate restTemplate = new RestTemplate();
        return getResponseBody(parameters, url, restTemplate);
    }

    private String getStoreDetail(String storeId) {
        logger.info("@CrawlingServiceImpl: getStoreDetail start");
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("zizum_num", storeId);
        parameters.add("rev_days", nowDate);

        String url = EscapeCafe.KEYESCAPE.getUrl() + "zizum_info";
        RestTemplate restTemplate = new RestTemplate();
        return getResponseBody(parameters, url, restTemplate);
    }

    private String getResponseBody(MultiValueMap<String, String> parameters, String url, RestTemplate restTemplate) {
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate.postForEntity(url, parameters, String.class).getBody();
    }
}
