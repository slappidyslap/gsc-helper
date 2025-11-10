package kg.musabaev.gschelper.swinggui.view.contract;

import kg.musabaev.gschelper.swinggui.listener.MenuBarItemsClickListener;

public interface MenuBarPresenterViewContract {

    void addMenuBarItemsClickListener(MenuBarItemsClickListener l);

    void removeMenuBarItemsClickListener(MenuBarItemsClickListener l);
}
