package com.tw.models;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MessageMetadataTest {

    @Test
    public void shouldReturnStringWithMetadataAndPayload() {
        long ingestionUnixTime = 1483246800000L;
        String producerId = "Producer-id";
        String messageUUID = "123e4567-e89b-12d3-a456-426655440000";
        long size = 12;

        MessageMetadata metadata = new MessageMetadata(ingestionUnixTime, producerId, messageUUID, size);

        String expected = "{\"producer_id\": \"Producer-id\", " +
                "\"size\": 12, " +
                "\"message_id\": \"123e4567-e89b-12d3-a456-426655440000\", " +
                "\"ingestion_time\": 1483246800000}";

        assertEquals(expected, metadata.toString());
    }
}
