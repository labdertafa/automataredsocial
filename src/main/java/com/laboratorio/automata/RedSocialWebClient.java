package com.laboratorio.automata;

/**
 *
 * author Rafael
 * version 1.0
 * created 03/05/2025
 * updated 04/05/2025
 */
public interface RedSocialWebClient {
    public boolean login();

    public boolean post(String text);
    public boolean postImage(String text, String imagePath);

    // Funciones para seguir nuevos usuarios
    public boolean goToFirstSuggested();
    public boolean navigateToUserPage(String user);
    public int getFollowings();
    public int getFollowers();
    public boolean isFollowedByMe();
    public boolean isFollowingMe();
    public boolean follow();
    public boolean follow(String user);
    public boolean unfollow();
    public boolean unfollow(String user);

    // Funciones implementadas en BaseWebClent
    public String getPageLink();
    public String getUsername();
    public void pauseInteraction();
    public void closeBrowser();
}