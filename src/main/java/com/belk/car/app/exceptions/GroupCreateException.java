package com.belk.car.app.exceptions;

import java.util.ArrayList;
import java.util.List;

public class GroupCreateException extends Exception {
    List<String> styleOrins = new ArrayList<String>();

    /**
     * Constructor for GroupCreateException.
     *
     * @param message exception message
     */
    public GroupCreateException(final String message, List<String> orins) {
        super(message);
        this.styleOrins.addAll(orins);
    }

    public List<String> getStyleOrins() {
        return styleOrins;
    }

    public void addStyleOrins(List<String> orins) {
        this.styleOrins.addAll(orins);
    }
}
