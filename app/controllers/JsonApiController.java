package controllers;

import play.i18n.MessagesApi;
import play.libs.concurrent.HttpExecutionContext;

import javax.inject.Inject;

/**
 * The type Json api controller.
 */
public class JsonApiController extends ApplicationController  {
    /**
     * The Http execution context.
     */
    private final HttpExecutionContext httpExecutionContext;
    /**
     * The Messages api.
     */
    private final MessagesApi messagesApi;

    /**
     * Instantiates a new Json api controller.
     *
     * @param httpExecutionContext the http execution context
     * @param messagesApi          the messages api
     */
    @Inject
    public JsonApiController(HttpExecutionContext httpExecutionContext, MessagesApi messagesApi) {
        this.httpExecutionContext = httpExecutionContext;
        this.messagesApi = messagesApi;
    }
}
