package com.roomescape.server.controller;

import com.roomescape.server.model.ReservationDto;
import com.roomescape.server.model.StoreDto;
import com.roomescape.server.model.ThemeDto;
import com.roomescape.server.service.CrawlingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/crawling")
public class CrawlingController {
    private static final Logger logger = LoggerFactory.getLogger(CrawlingController.class);
    private final CrawlingService crawlingService;

    @GetMapping("/store")
    public ResponseEntity<List<StoreDto>> getStore() {
        logger.debug("@CrawlingController: getStore");
        return new ResponseEntity<>(crawlingService.getStore(), HttpStatus.CREATED);
    }

    @GetMapping("/theme")
    public ResponseEntity<List<ThemeDto>> getTheme() {
        logger.debug("@CrawlingController: getTheme");
        return new ResponseEntity<>(crawlingService.getTheme(), HttpStatus.CREATED);
    }

    @GetMapping("/reservation")
    public ResponseEntity<List<ReservationDto>> getAllReservation(
            @RequestParam("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
            @RequestParam("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate) {
        logger.debug("@CrawlingController: getAllReservation");
        return new ResponseEntity<>(crawlingService.getAllReservation(fromDate, toDate), HttpStatus.CREATED);
    }

    @GetMapping("/reservation/{id}")
    public ResponseEntity<List<ReservationDto>> getReservationByThemeId(
            @PathVariable("id") String id,
            @RequestParam("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
            @RequestParam("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate) {
        logger.debug("@CrawlingController: getReservationByThemeId");
        return new ResponseEntity<>(crawlingService.getReservationByThemeId(id, fromDate, toDate), HttpStatus.CREATED);
    }
}
