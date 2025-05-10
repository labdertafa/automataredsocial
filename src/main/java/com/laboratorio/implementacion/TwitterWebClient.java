package com.laboratorio.implementacion;

import com.laboratorio.automata.RedSocialWebClient;
import com.laboratorio.exception.OperationException;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.nio.file.Paths;

/**
 *
 * author Rafael
 * version 1.0
 * created 03/05/2025
 * updated 10/05/2025
 */
public class TwitterWebClient extends BaseWebClient implements RedSocialWebClient {
    public TwitterWebClient(String url, String username, String password) {
        super(url, username, password);
    }

    @Override
    public boolean login() {
        try {
            this.page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Aceptar todas las cookies")).click();
            this.page.getByTestId("loginButton").click();
            this.page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Teléfono, correo electrónico")).fill(this.username);
            this.page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Siguiente")).click();
            this.page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Contraseña Mostrar contraseña")).fill(this.password);
            this.page.getByTestId("LoginForm_Login_Button").click();

            log.debug("Se ha hecho login en Twitter con la cuenta: {}", this.username);

            return true;
        } catch (Exception e) {
            this.logError("Error haciendo login en Twitter con el usuario: " + this.username, e);
            return false;
        }
    }

    @Override
    public boolean post(String text) {
        return this.postImage(text, null);
    }

    @Override
    public boolean postImage(String text, String imagePath) {
        try {
            this.page.getByTestId("AppTabBar_Home_Link").click();
            this.page.getByTestId("tweetTextarea_0").fill(text);
            if (imagePath != null) {
                this.page.locator("input[type='file']").setInputFiles(Paths.get(imagePath));
                Thread.sleep(10000);
            }
            this.page.getByTestId("tweetButtonInline").click();

            log.debug("Se ha posteado correctamente Twitter con el usuario: {}", this.username);

            return true;
        } catch (Exception e) {
            this.logError(String.format("Error posteando en Twitter con el usuario: %s", this.username), e);
            return false;
        }
    }

    @Override
    public boolean deleteFirstPost() {
        try {
            this.page.getByTestId("AppTabBar_Profile_Link").click();
            this.getXPathLocator("primer_post").click();
            this.page.getByRole(AriaRole.MENUITEM, new Page.GetByRoleOptions().setName("Eliminar").setExact(true)).locator("div").nth(2).click();
            this.page.getByTestId("confirmationSheetConfirm").click();

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean goToFirstSuggested() {
        try {
            this.page.getByTestId("AppTabBar_Home_Link").click();
            this.page.getByLabel("A quién seguir").getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("Mostrar más")).click();
            this.getXPathLocator("primera_sugerencia").click();

            log.debug("Se ha navegado a la primera sugerencia en Twitter para el usuario: {}", this.username);

            return true;
        } catch (Exception e) {
            this.logError(String.format("Error navegando a la primera sugerencia en Twitter para el usuario: %s", this.username), e);
            return false;
        }
    }

    @Override
    public boolean navigateToUserPage(String user) {
        try {
            String userUrl = this.url + "/" + user;
            this.page.navigate(userUrl);

            log.debug("Se ha navegado a la página del usuario: {}", user);

            return true;
        } catch (Exception e) {
            this.logError(String.format("Error navegando a la página del usuario: %s", user), e);
            return false;
        }
    }

    @Override
    public int getFollowings() {
        try {
            String text = this.getXPathLocator("numero_seguidos").textContent();
            int seguidos = Integer.parseInt(text.replace(".", ""));

            log.debug("Se han recuperado los seguidos por un usuario: {}", seguidos);

            return seguidos;
        } catch (Exception e) {
            this.logError("Error recuperando los seguidos por un usuario en Twitter", e);
            return -1;
        }
    }

    @Override
    public int getFollowers() {
        try {
            String text = this.getXPathLocator("numero_seguidores").textContent();
            int seguidores = Integer.parseInt(text.replace(".", ""));

            log.debug("Se han recuperado los seguidores de un usuario: {}", seguidores);

            return seguidores;
        } catch (Exception e) {
            this.logError("Error recuperando los seguidores de un usuario en Twitter", e);
            return -1;
        }
    }

    @Override
    public boolean isFollowedByMe() {
        try {
            String text = this.getXPathLocator("boton_seguir").textContent();
            return text.equalsIgnoreCase("dejar de seguir");
        } catch (Exception e) {
            String message = String.format("Error comprobando si %s sigue esta cuenta", this.username);
            this.logError(message, e);
            throw new OperationException(message);
        }
    }

    @Override
    public boolean isFollowingMe() {
        try {
            String text = this.getXPathLocator("follow_indicator").textContent();
            return text.equalsIgnoreCase("te sigue");
        } catch (Exception e) {
            this.logError(String.format("Error verificando si una cuenta sigue a: %s", this.username), e);
            return false;
        }
    }

    @Override
    public boolean follow() {
        if (!this.isFollowedByMe()) {
            try {
                this.getXPathLocator("boton_seguir").click();

                log.debug("El usuario {} ha seguido un nuevo usuario", this.username);

                return true;
            } catch (Exception e) {
                this.logError(String.format("Error siguiendo a un nuevo usuario con: %s", username), e);
                return false;
            }
        }

        return false;
    }

    @Override
    public boolean follow(String user) {
        if (this.navigateToUserPage(user)) {
            return this.follow();
        }

        return false;
    }

    @Override
    public boolean unfollow() {
        if (this.isFollowedByMe()) {
            try {
                this.getXPathLocator("boton_seguir").click();

                log.debug("El usuario {} ha dejado de seguir un usuario", this.username);

                return true;
            } catch (Exception e) {
                this.logError(String.format("Error dejando de seguir a un usuario con: %s", username), e);
                return false;
            }
        }

        return false;
    }

    @Override
    public boolean unfollow(String user) {
        if (this.navigateToUserPage(user)) {
            return this.unfollow();
        }

        return false;
    }
}