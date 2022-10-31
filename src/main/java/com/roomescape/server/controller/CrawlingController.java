package com.roomescape.server.controller;

import com.roomescape.server.model.StoreDto;
import com.roomescape.server.model.ThemeDto;
import com.roomescape.server.service.CrawlingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/crawling")
public class CrawlingController {
    private static Logger logger = LoggerFactory.getLogger(CrawlingController.class);
    private final CrawlingService crawlingService;

    @GetMapping("/store")
    public ResponseEntity<List<StoreDto>> getStore() {
        return new ResponseEntity<>(crawlingService.getStore(), HttpStatus.CREATED);
    }

    @GetMapping("/theme")
    public ResponseEntity<List<ThemeDto>> getTheme() {
        return new ResponseEntity<>(crawlingService.getTheme(), HttpStatus.CREATED);
    }

    @GetMapping("/reservation")
    public void getReservation() {

    }
}
