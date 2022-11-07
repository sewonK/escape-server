package com.roomescape.server.service;

import com.roomescape.server.config.ChromeDriverContext;
import com.roomescape.server.entity.EscapeCafe;
import com.roomescape.server.entity.Store;
import com.roomescape.server.entity.Theme;
import com.roomescape.server.model.ReservationDto;
import com.roomescape.server.model.StoreDto;
import com.roomescape.server.model.ThemeDto;
import com.roomescape.server.repository.ReservationRepository;
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

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CrawlingServiceImpl implements CrawlingService {
    private static final Logger logger = LoggerFactory.getLogger(CrawlingServiceImpl.class);
    private final String nowDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    private final StoreRepository storeRepository;
    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    private final ChromeDriverContext context;

    @Override
    public List<StoreDto> getStore() {
        logger.debug("@CrawlingServiceImpl: getStore");
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
            logger.debug("Error : msg={}, cause={}", e.getMessage(), e.getCause());
        }

        driver.get(EscapeCafe.KEYESCAPE.getUrl() + "make");
        webDriverWait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector(pattern))
        );
        return driver.findElements(By.cssSelector(pattern));
    }

    @Override
    public List<ThemeDto> getTheme() {
        logger.debug("@CrawlingServiceImpl: getTheme");
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

    @Override
    public List<ReservationDto> getAllReservation() {
        logger.debug("@CrawlingServiceImpl: getAllReservation");
        List<Theme> themeList = themeRepository.findAll();
        List<ReservationDto> reservationDtoList = new ArrayList<>();
        for (Theme theme : themeList) {
            makeReservationDtoListByTheme(reservationDtoList, theme);
        }
        return reservationDtoList;
    }

    @Override
    public List<ReservationDto> getReservationByThemeId(String themeId) {
        logger.debug("@CrawlingServiceImpl: getReservationByThemeId");
        Theme theme = themeRepository.findById(Long.parseLong(themeId)).orElseThrow(IllegalArgumentException::new);
        List<ReservationDto> reservationDtoList = new ArrayList<>();
        makeReservationDtoListByTheme(reservationDtoList, theme);
        return reservationDtoList;
    }

    private void makeReservationDtoListByTheme(List<ReservationDto> reservationDtoList, Theme theme) {
        Document doc = Jsoup.parse(getTimeDetail(Long.toString(theme.getStoreId()), Long.toString(theme.getId())));
        List<Element> elements = doc.select("#contents").select(".s_contents").select(".in_Layer").select("li");
        for (Element element : elements) {
            String flag = element.attr("class");
            ReservationDto reservationDto = ReservationDto.builder()
                    .time(LocalDateTime.parse(nowDate + " " + element.text(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                    .possibleFlag((flag.equals("possible")))
                    .themeId(theme.getId())
                    .build();
            reservationDto.setId(reservationRepository.save(reservationDto.toEntity(theme)).getId());
            reservationDtoList.add(reservationDto);
        }
    }

    private String getIdBySubstr(String idInfo) {
        int firstIdx = idInfo.indexOf("'") + 1;
        int secondIdx = idInfo.indexOf("'", firstIdx);
        return idInfo.substring(firstIdx, secondIdx);
    }

    private String getThemeDetail(String storeId) {
        logger.debug("@CrawlingServiceImpl: getThemeDetail");
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("zizum_num", storeId);
        parameters.add("rev_days", nowDate);

        String url = EscapeCafe.KEYESCAPE.getUrl() + "theme";
        RestTemplate restTemplate = new RestTemplate();
        return getResponseBody(parameters, url, restTemplate);
    }

    private String getStoreDetail(String storeId) {
        logger.debug("@CrawlingServiceImpl: getStoreDetail");
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("zizum_num", storeId);
        parameters.add("rev_days", nowDate);

        String url = EscapeCafe.KEYESCAPE.getUrl() + "zizum_info";
        RestTemplate restTemplate = new RestTemplate();
        return getResponseBody(parameters, url, restTemplate);
    }


    private String getTimeDetail(String storeId, String themeId) {
        logger.debug("@CrawlingServiceImpl: getTimeDetail");
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("zizum_num", storeId);
        parameters.add("rev_days", nowDate);
        parameters.add("theme_num", themeId);

        String url = EscapeCafe.KEYESCAPE.getUrl() + "theme_time";
        RestTemplate restTemplate = new RestTemplate();
        return getResponseBody(parameters, url, restTemplate);
    }

    private String getResponseBody(MultiValueMap<String, String> parameters, String url, RestTemplate restTemplate) {
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate.postForEntity(url, parameters, String.class).getBody();
    }
}
