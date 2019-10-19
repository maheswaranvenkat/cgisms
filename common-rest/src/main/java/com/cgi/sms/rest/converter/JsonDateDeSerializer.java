package com.cgi.sms.rest.converter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class JsonDateDeSerializer extends JsonDeserializer<Date> {

    @Override
    public Date deserialize(final JsonParser parser, final DeserializationContext context) throws IOException {
        final DateFormat format = new ISO8601DateFormat();
        final String text = parser.getText();
        try {
            if(StringUtils.isNotEmpty(text)) {
                return format.parse(text);
            }
        } catch (final ParseException pe) {
            throw new IOException(pe);
        }
        return null;
    }

}
