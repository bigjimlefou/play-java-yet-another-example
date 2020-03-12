package controllers.user;

import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.typesafe.config.Config;
import controllers.ApplicationController;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.play.java.Secure;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;

/**
 * The type User controller.
 */
public class UserController extends ApplicationController {
    /**
     * The Form factory.
     */
    private final FormFactory formFactory;
    /**
     * The Http execution context.
     */
    private final HttpExecutionContext httpExecutionContext;
    /**
     * The Messages api.
     */
    private final MessagesApi messagesApi;
    /**
     * The Monprofil.
     */
    private final views.html.user.mypage mypage;

    /**
     * Instantiates a new User controller.
     *
     * @param config               the config
     * @param formFactory          the form factory
     * @param httpExecutionContext the http execution context
     * @param messagesApi          the messages api
     * @param mypage               the mypage
     */
    @Inject
    public UserController(Config config, FormFactory formFactory, HttpExecutionContext httpExecutionContext, MessagesApi messagesApi, views.html.user.mypage mypage) {
        this.mypage = mypage;
        this.playConfig = config;
        this.formFactory = formFactory;
        this.httpExecutionContext = httpExecutionContext;
        this.messagesApi = messagesApi;
    }

    /**
     * My profile result.
     *
     * @param request the request
     * @return the result
     */
    @SubjectPresent(handlerKey = "KeycloakOidcClient", forceBeforeAuthCheck = true)
    @Secure(clients = "KeycloakOidcClient", authorizers = "Login")
    public Result myPage(Http.Request request) {
        CommonProfile oidcProfile = (CommonProfile) getUserProfile(request).get();

        return ok(mypage.render(oidcProfile));
    }
}
