package kg.musabaev.gschelper.swinggui;

public class MainViewModel {

    private Boolean darkModeEnabled = true;

    public Boolean darkModeEnabled() {
        return darkModeEnabled;
    }

    public void toggleDarkModeEnabled() {
        this.darkModeEnabled = !darkModeEnabled;
    }
}
