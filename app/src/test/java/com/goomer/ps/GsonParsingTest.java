package com.goomer.ps;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.List;

import static org.junit.Assert.*;

public class GsonParsingTest {

    @Test
    public void parseMenuJson_shouldReturnItems() {
        String json = "[\n  {\"id\":1,\"name\":\"Item\",\"description\":\"Desc\",\"price\":10.5,\"imageUrl\":\"\"}\n]";
        Gson gson = new Gson();
        Type listType = new TypeToken<List<MenuItem>>(){}.getType();
        List<MenuItem> items = gson.fromJson(json, listType);
        assertNotNull(items);
        assertEquals(1, items.size());
        MenuItem item = items.get(0);
        assertEquals(1, item.id);
        assertEquals("Item", item.name);
        assertEquals("Desc", item.description);
        assertEquals(10.5, item.price, 0.001);
    }
}
