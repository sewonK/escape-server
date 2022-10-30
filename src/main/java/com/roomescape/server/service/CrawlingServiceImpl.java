package com.roomescape.server.service;

import com.roomescape.server.config.ChromeDriverContext;
import com.roomescape.server.entity.EscapeCafe;
import com.roomescape.server.model.StoreDto;
import com.roomescape.server.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
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
    private static Logger logger = LoggerFactory.getLogger(CrawlingServiceImpl.class);

    private final StoreRepository storeRepository;

    private final ChromeDriverContext context;
    private WebDriver driver;

    @Override
    public List<StoreDto> getStore() {
        logger.info("@CrawlingServiceImpl: getStore start");
        driver = context.chromeDriver();
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            logger.info("Error : msg={}, cause={}", e.getMessage(), e.getCause());
        }

        driver.get(EscapeCafe.KEYESCAPE.getUrl());
        webDriverWait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("#zizum_data"))
        );
        List<WebElement> elements = driver.findElements(By.cssSelector("#zizum_data > a"));

        List<StoreDto> storeDtoList = new ArrayList<>();
        for (WebElement element : elements) {
            String idInfo = element.getAttribute("href");
            int firstIdx = idInfo.indexOf("'") + 1;
            int secondIdx = idInfo.indexOf("'", firstIdx);
            String storeId = idInfo.substring(firstIdx, secondIdx);

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

    private String getStoreDetail(String storeId) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("zizum_num", storeId);
        parameters.add("rev_days", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        String url = "https://keyescape.co.kr/web/home.php?go=rev.zizum_info";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        ResponseEntity<String> res = restTemplate.postForEntity(url, parameters, String.class);
        return res.getBody();
    }
}
