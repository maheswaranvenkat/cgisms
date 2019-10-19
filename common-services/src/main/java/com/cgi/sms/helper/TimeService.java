package com.cgi.sms.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class TimeService {
    private Clock clock;

    @Value("${ereca.time.zone:Europe/Paris")
    private String zonedId = "Europe/Paris";

    @PostConstruct
    private void setUp() {
        clock = Clock.system(ZoneId.of(zonedId));
    }

    public ZonedDateTime getCurrentDateTime() {
        return ZonedDateTime.now(clock);
    }

    public OffsetDateTime getCurrentOffsetDateTime() {
        return OffsetDateTime.now(clock);
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }

}
