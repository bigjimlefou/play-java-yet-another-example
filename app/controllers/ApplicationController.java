package controllers;

import com.google.inject.Inject;
import com.timgroup.jgravatar.Gravatar;
import com.timgroup.jgravatar.GravatarDefaultImage;
import com.timgroup.jgravatar.GravatarRating;
import controllers.user.routes;
import org.pac4j.core.config.Config;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.play.PlayWebContext;
import org.pac4j.play.store.PlaySessionStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import play.routing.JavaScriptReverseRouter;

import java.util.List;
import java.util.Optional;

/**
 * The type Application controller.
 */
public class ApplicationController extends Controller {
    /**
     * The Log.
     */
    final protected Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * The Config.
     */
    @Inject
    protected Config pac4jConfig;

    /**
     * The Play session store.
     */
    @Inject
    protected PlaySessionStore playSessionStore;
    /**
     * The Play config.
     */
    @Inject
    protected com.typesafe.config.Config playConfig;

    /**
     * This result directly redirect to application home.
     */
    private Result GO_HOME = Results.redirect(
            controllers.user.routes.UserController.myPage()
    );

    /**
     * Gets profiles.
     *
     * @return the profiles
     */
    protected List<CommonProfile> getProfiles(Http.Request request) {
        final PlayWebContext context = new PlayWebContext(request, playSessionStore);
        final ProfileManager<CommonProfile> profileManager = new ProfileManager(context);
        return profileManager.getAll(true);
    }

    /**
     * Gets user profile.
     *
     * @return the user profile
     */
    protected Optional<CommonProfile> getUserProfile(Http.Request request) {
        PlayWebContext webContext = new PlayWebContext(request, playSessionStore);
        ProfileManager profileManager = new ProfileManager(webContext);
        Optional<CommonProfile> profile = profileManager.get(true);

        if (profile.isPresent()) {
            CommonProfile keycloakOidcProfile = profile.get();

            if (keycloakOidcProfile.getEmail() != null && !keycloakOidcProfile.getEmail().isEmpty()) {
                Gravatar gravatar = new Gravatar().setRating(GravatarRating.GENERAL_AUDIENCES)
                        .setDefaultImage(GravatarDefaultImage.IDENTICON);
                String url = gravatar.getUrl(keycloakOidcProfile.getEmail());
                keycloakOidcProfile.addAttribute("gravatarUrl", url);
            }
        }

        return profile;
    }

    /**
     * Handle default path requests, redirect to computers list
     *
     * @return the result
     */
    public Result index(Http.Request request) {
        return GO_HOME;
    }

    public Result javascriptRoutes(Http.Request request) {
        return ok(
                // routes) instead
                JavaScriptReverseRouter.create(
                        "jsRoutes",
                        "jQuery.ajax",
                        request.host(),

                        //// Management Routes
                        // Pointages
                        controllers.routes.javascript.ApplicationController.index()
                )
        ).as(Http.MimeTypes.JAVASCRIPT);
    }
}
