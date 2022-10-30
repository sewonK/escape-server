package com.roomescape.server.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EscapeCafe {
    KEYESCAPE("키이스케이프", "http://www.keyescape.co.kr/web/home.php?go=rev.");

    private String name;
    private String url;
}
