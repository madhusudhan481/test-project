package com.fasterxml.jackson.failing;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.json.JsonFactory;

// For checking [databind#533]
public class LocationOffsets533Test extends com.fasterxml.jackson.core.BaseTest
{
    final JsonFactory JSON_F = new JsonFactory();

    public void testOffsetWithoutInputOffset() throws Exception
    {
        JsonLocation loc;
        JsonParser p;
        // 3 spaces before, 2 after, just for padding
        byte[] b = "   { }  ".getBytes("UTF-8");

        // and then peel them off
        p = JSON_F.createParser(ObjectReadContext.empty(), b);
        assertToken(JsonToken.START_OBJECT, p.nextToken());

        loc = p.getTokenLocation();
        assertEquals(3L, loc.getByteOffset());
        assertEquals(-1L, loc.getCharOffset());
        assertEquals(1, loc.getLineNr());
        assertEquals(4, loc.getColumnNr());

        loc = p.getCurrentLocation();
        assertEquals(4L, loc.getByteOffset());
        assertEquals(-1L, loc.getCharOffset());
        assertEquals(1, loc.getLineNr());
        assertEquals(5, loc.getColumnNr());

        p.close();
    }

    // for [core#533]
    public void testUtf8Bom() throws Exception
    {
        JsonLocation loc;
        JsonParser p;

        byte[] b = withUtf8Bom("{ }".getBytes());

        // and then peel them off
        p = JSON_F.createParser(ObjectReadContext.empty(), b);
        assertToken(JsonToken.START_OBJECT, p.nextToken());

        loc = p.getTokenLocation();
        assertEquals(3L, loc.getByteOffset());
        assertEquals(-1L, loc.getCharOffset());
        assertEquals(1, loc.getLineNr());
        assertEquals(4, loc.getColumnNr());

        loc = p.getCurrentLocation();
        assertEquals(4L, loc.getByteOffset());
        assertEquals(-1L, loc.getCharOffset());
        assertEquals(1, loc.getLineNr());
        assertEquals(5, loc.getColumnNr());

        p.close();
    }

    public void testUtf8BomWithPadding() throws Exception
    {
        JsonLocation loc;
        JsonParser p;

        byte[] b = withUtf8Bom("   { }".getBytes());

        // and then peel them off
        p = JSON_F.createParser(ObjectReadContext.empty(), b);
        assertToken(JsonToken.START_OBJECT, p.nextToken());

        loc = p.getTokenLocation();
        assertEquals(6L, loc.getByteOffset());
        assertEquals(-1L, loc.getCharOffset());
        assertEquals(1, loc.getLineNr());
        assertEquals(7, loc.getColumnNr());

        loc = p.getCurrentLocation();
        assertEquals(7L, loc.getByteOffset());
        assertEquals(-1L, loc.getCharOffset());
        assertEquals(1, loc.getLineNr());
        assertEquals(8, loc.getColumnNr());

        p.close();
    }

    public void testUtf8BomWithInputOffset() throws Exception
    {
        JsonLocation loc;
        JsonParser p;

        byte[] b = withUtf8Bom("   { }".getBytes());

        // and then peel them off
        p = JSON_F.createParser(ObjectReadContext.empty(), b);
        assertToken(JsonToken.START_OBJECT, p.nextToken());

        loc = p.getTokenLocation();
        assertEquals(6L, loc.getByteOffset());
        assertEquals(-1L, loc.getCharOffset());
        assertEquals(1, loc.getLineNr());
        assertEquals(7, loc.getColumnNr());

        loc = p.getCurrentLocation();
        assertEquals(7L, loc.getByteOffset());
        assertEquals(-1L, loc.getCharOffset());
        assertEquals(1, loc.getLineNr());
        assertEquals(8, loc.getColumnNr());

        p.close();
    }

    private byte[] withUtf8Bom(byte[] bytes) {
        byte[] arr = new byte[bytes.length + 3];
        // write UTF-8 BOM
        arr[0] = (byte) 0xEF;
        arr[1] = (byte) 0xBB;
        arr[2] = (byte) 0xBF;
        System.arraycopy(bytes, 0, arr, 3, bytes.length);
        return arr;
    }
}
