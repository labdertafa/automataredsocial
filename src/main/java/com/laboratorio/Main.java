package com.laboratorio;

import com.laboratorio.automata.RedSocialWebClient;
import com.laboratorio.implementacion.TwitterWebClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    private final static Logger log = LogManager.getLogger(Main.class);

    private static RedSocialWebClient redSocialWebClient;

    public static void main(String[] args) {
        log.info("Empezando la prueba de la automatización");

        redSocialWebClient = new TwitterWebClient("https://x.com", "humordespierto", "vielma03");

        redSocialWebClient.login();

        // redSocialWebClient.pauseInteraction();

        // redSocialWebClient.post("Tweet de prueba enviado desde Playwright");
        redSocialWebClient.postImage("Tweet de prueba con imagen enviado desde Playwright", "Monza_1955.jpg");

        // irAprimeraSugerencia();

        redSocialWebClient.navigateToUserPage("Atoninonino");

        if (redSocialWebClient.isFollowingMe()) {
            log.info("El usuario me sigue");
        } else {
            log.info("El usuario no me sigue");
        }

        irAprimeraSugerencia();

        redSocialWebClient.closeBrowser();

        log.info("Finalizando la prueba de la automatización");
    }

    private static void irAprimeraSugerencia() {
        redSocialWebClient.goToFirstSuggested();
        String pagina = redSocialWebClient.getPageLink();
        log.info("EL link de la página del usuario es: {}", pagina);
        int seguidos = redSocialWebClient.getFollowings();
        int seguidores = redSocialWebClient.getFollowers();

        if (seguidos >= seguidores) {
            log.info("Es un buen seguidor potencial");
        } else {
            log.info("El usuario es un mal polvo");
        }

        if (redSocialWebClient.isFollowedByMe()) {
            log.info("Estoy siguiendo a este usuario");
        } else {
            log.info("No estoy siguiendo a este usuario");
            redSocialWebClient.follow();
        }

        if (redSocialWebClient.isFollowingMe()) {
            log.info("El usuario me sigue");
        } else {
            log.info("El usuario no me sigue");
        }
    }
}