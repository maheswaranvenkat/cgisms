package com.cgi.sms.dao.converter;

import com.cgi.sms.helper.TimeService;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class ZonedDateTimeConverterTest {

    ZonedDateTimeConverter zonedDateTimeConverter = new ZonedDateTimeConverter();

    private Clock clock;
    private Instant instant;
    private ZoneId utc = ZoneId.of("Europe/Paris");
    private TimeService timeService;

    @Before
    public void setUp() {
        instant = Instant.parse("2016-01-23T12:34:56Z");
        clock = Clock.fixed(instant, utc);
        timeService = new TimeService();
        timeService.setClock(clock);
    }

    @Test
    public void should_return_timestamp_as_localDateTime() {
        //Given
        ZonedDateTime localDateTime = ZonedDateTime.now(clock);
        //When
        Timestamp timestamp = zonedDateTimeConverter.convertToDatabaseColumn(localDateTime);
        // Then
        assertThat(timestamp.getTime()).isEqualTo(1453552496000L);
    }

    @Test
    public void should_return_null_when_localDateTime_is_null() {
        //Given
        ZonedDateTime zonedDateTime = null;
        //When
        Timestamp timestamp = zonedDateTimeConverter.convertToDatabaseColumn(zonedDateTime);
        //Then
        assertThat(timestamp).isNull();
    }

    @Test
    public void should_return_localDateTime_as_timestamp() {
        //Given
        ZonedDateTime zonedDateTime = timeService.getCurrentDateTime();
        Timestamp timestamp = Timestamp.from(zonedDateTime.toInstant());
        //when
        ZonedDateTime actual = zonedDateTimeConverter.convertToEntityAttribute(timestamp);
        //then
        ZonedDateTime expected = zonedDateTime;
        assertThat(actual.toString()).isEqualTo(expected.toString());
    }
}
