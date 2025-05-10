package com.laboratorio.util;

import com.laboratorio.automata.RedSocialWebClient;
import com.laboratorio.model.AutomataSession;
import com.laboratorio.model.NombreRedSocial;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * author Rafael
 * version 1.0
 * created 10/05/2025
 * updated 10/05/2025
 */
public class AutomataSessionManager {
    protected final static Logger log = LogManager.getLogger(AutomataSessionManager.class);

    private final List<AutomataSession> sessiones;

    public AutomataSessionManager() {
        this.sessiones = new ArrayList<>();
    }

    public RedSocialWebClient getSession(NombreRedSocial redSocial, String user, String password) {
        // Se devuelve la sessión si está creada para la red social y el usuario
        Optional<AutomataSession> session = this.sessiones.stream()
                .filter(s -> s.getRedSocial().equals(redSocial) && s.getUser().equals(user))
                .findFirst();
        if (session.isPresent()) {
            log.debug("Se ha devuelto una sesión existente del usuario {} para la red social: {}", user, redSocial);
            return session.get().getRedSocialWebClient();
        }

        log.debug("No existe sesión del usuario {} para la red social: {}. Se creará una.", user, redSocial);

        //  Si no existe, se crea la sesión y se agrega a la lista
        AutomataSession nuevaSession = new AutomataSession(redSocial,user, password);
        if (nuevaSession.getRedSocialWebClient() != null) {
            this.sessiones.add(nuevaSession);
        }

        return nuevaSession.getRedSocialWebClient();
    }

    public void removeSession(NombreRedSocial redSocial, String user) {
        this.sessiones.removeIf(s -> s.getRedSocial().equals(redSocial) && s.getUser().equals(user));
    }

    public void releaseSessions() {
        for (AutomataSession session : this.sessiones) {
            session.releaseSession();
        }
    }
}