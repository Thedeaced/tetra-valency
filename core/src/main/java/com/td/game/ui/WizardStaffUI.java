package com.td.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import com.td.game.elements.Element;

public class WizardStaffUI implements Disposable {
    private Element equippedElement;
    private int x;
    private int y;
    private int width;
    private int height;

    public WizardStaffUI(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Element equipOrb(Element newElement) {
        Element old = equippedElement;
        equippedElement = newElement;
        return old;
    }

    public Element removeOrb() {
        return equipOrb(null);
    }

    public Element getEquippedElement() {
        return equippedElement;
    }

    public boolean hasOrb() {
        return equippedElement != null;
    }

    public boolean contains(int screenX, int screenY) {
        int flippedY = Gdx.graphics.getHeight() - screenY;
        return screenX >= x && screenX <= x + width &&
                flippedY >= y && flippedY <= y + height;
    }

    public void setBounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void update(float delta) {
    }

    @Override
    public void dispose() {
    }
}

