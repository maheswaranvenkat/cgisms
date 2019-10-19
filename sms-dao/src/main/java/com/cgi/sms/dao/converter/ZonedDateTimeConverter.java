package com.cgi.sms.dao.converter;

import org.springframework.beans.factory.annotation.Value;

import javax.persistence.AttributeConverter;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.util.Optional;

public class ZonedDateTimeConverter implements AttributeConverter<ZonedDateTime, Timestamp> {

    @Value("${ereca.time.zone:Europe/Paris}")
    private String zoneId = "Europe/Paris";


    @Override
    public Timestamp convertToDatabaseColumn(ZonedDateTime zonedDateTime) {
        return Optional.ofNullable(zonedDateTime)
                .map(ChronoZonedDateTime::toInstant)
                .map(Timestamp::from)
                .orElse(null);

    }

    @Override
    public ZonedDateTime convertToEntityAttribute(Timestamp timestamp) {
        return Optional.ofNullable(timestamp)
                .map(Timestamp::toInstant)
                .map(t -> t.atZone(ZoneId.of(zoneId)))
                .orElse(null);
    }
}
