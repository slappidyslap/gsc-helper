package kg.musabaev.gschelper.swinggui.model;

public class MenuBarModel {

    private boolean isDarkMode;
    private boolean isAuthenticatedInGoogle;

    public MenuBarModel(boolean isDarkMode, boolean isAuthenticatedInGoogle) {
        this.isDarkMode = isDarkMode;
        this.isAuthenticatedInGoogle = isAuthenticatedInGoogle;
    }

    public boolean isDarkMode() {
        return isDarkMode;
    }

    public void setIsDarkMode(boolean isDarkMode) {
        this.isDarkMode = isDarkMode;
    }

    public boolean isAuthenticatedInGoogle() {
        return isAuthenticatedInGoogle;
    }

    public void setIsAuthenticatedInGoogle(boolean isAuthenticatedInGoogle) {
        this.isAuthenticatedInGoogle = isAuthenticatedInGoogle;
    }
}
