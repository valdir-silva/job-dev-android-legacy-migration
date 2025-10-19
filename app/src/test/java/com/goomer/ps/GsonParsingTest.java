package com.goomer.ps;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.goomer.ps.domain.model.MenuItem;

import org.junit.Test;

import java.lang.reflect.Type;
import java.util.List;

import static org.junit.Assert.*;

public class GsonParsingTest {

    @Test
    public void parseMenuJson_shouldReturnItems() {
        // Given
        String json = "[\n  {\"id\":1,\"name\":\"Item\",\"description\":\"Desc\",\"price\":10.5,\"imageUrl\":\"\"}\n]";
        Gson gson = new Gson();
        Type listType = new TypeToken<List<MenuItem>>(){}.getType();
        
        // When
        List<MenuItem> items = gson.fromJson(json, listType);
        
        // Then
        assertNotNull("Lista de itens não deve ser nula", items);
        assertEquals("Deve ter exatamente 1 item", 1, items.size());
        
        MenuItem item = items.get(0);
        assertNotNull("Item não deve ser nulo", item);
        assertEquals("ID deve ser 1", 1, item.id);
        assertEquals("Nome deve ser 'Item'", "Item", item.name);
        assertEquals("Descrição deve ser 'Desc'", "Desc", item.description);
        assertEquals("Preço deve ser 10.5", 10.5, item.price, 0.001);
        assertEquals("URL da imagem deve ser vazia", "", item.imageUrl);
    }
}
