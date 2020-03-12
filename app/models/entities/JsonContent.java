package models.entities;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.twirl.api.Content;

import java.io.IOException;

/**
 * The type Json content.
 */
public class JsonContent implements Content {

    private final JsonNode json;
    private final String contentType = "application/json";

    /**
     * Instantiates a new Json content.
     *
     * @param content the content
     */
    public JsonContent(final String content) {
        super();
        final ObjectMapper mapper = new ObjectMapper();
        this.json = mapper.createObjectNode();
        ((ObjectNode) this.json).put("content", content);
    }

    /**
     * Body string.
     *
     * @return the string
     */
    @Override
    public String body() {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this.json);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Content type string.
     *
     * @return the string
     */
    @Override
    public String contentType() {
        return this.contentType;
    }
}
