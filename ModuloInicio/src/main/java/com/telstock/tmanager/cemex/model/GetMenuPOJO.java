package com.telstock.tmanager.cemex.model;

import java.util.ArrayList;

import mx.com.tarjetasdelnoreste.realmdb.model.MenuDB;

/**
 * Created by czamora on 9/13/16.
 */
public class GetMenuPOJO {

    private ArrayList<MenuDB> MenuItem;

    public ArrayList<MenuDB> getMenuItem() {
        return MenuItem;
    }

    public void setMenuItem(ArrayList<MenuDB> menuItem) {
        MenuItem = menuItem;
    }

}
