package com.laboratorio.model;

import com.laboratorio.automata.RedSocialWebClient;
import com.laboratorio.implementacion.TwitterWebClient;
import com.laboratorio.util.ConfigReader;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * author Rafael
 * version 1.0
 * created 10/05/2025
 * updated 10/05/2025
 */

@Getter
public class AutomataSession {
    protected final static Logger log = LogManager.getLogger(AutomataSession.class);
    private final NombreRedSocial redSocial;
    private final String user;
    private final RedSocialWebClient redSocialWebClient;

    public AutomataSession(NombreRedSocial redSocial, String user, String password) {
        this.redSocial = redSocial;
        this.user = user;
        this.redSocialWebClient = this.createSession(password);
    }

    private RedSocialWebClient createSession(String password) {
        switch (this.redSocial) {
            case TWITTER -> {
                ConfigReader config = new ConfigReader("config//twitter_conf.properties");
                String url = config.getProperty("twitter_url");
                RedSocialWebClient webClient = new TwitterWebClient(url, this.user, password);
                if (!webClient.login()) {
                    log.error("No se pudo hacer login con el usuario {} para la red social: {}", this.user, this.redSocial);
                    return null;
                }
                log.debug("Se ha creado una sesión del usuario {} para la red social: {}", this.user, this.redSocial);
                return webClient;
            }
            default -> {
                log.error("Imposible crear sesión del usuario {} para la red social: {}", this.user, this.redSocial);
                return null;
            }
        }
    }

    public void releaseSession() {
        if (this.redSocialWebClient != null) {
            this.redSocialWebClient.closeBrowser();
        }
    }
}