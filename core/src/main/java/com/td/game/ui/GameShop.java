package com.td.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.td.game.elements.Element;


public class GameShop implements Disposable {
    private static final float ORB_CARD_WIDTH = 55f;
    private static final float ORB_CARD_HEIGHT = 70f;
    private static final float ORB_CARD_START_X = 10f;
    private static final float ORB_CARD_GAP = 5f;

    private float x, y, width, height;
    private SpriteBatch batch;
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;

    
    private final int[] orbPrices = { 25, 25, 25, 25 }; 
    private final Element[] orbElements = { Element.FIRE, Element.WATER, Element.EARTH, Element.AIR };
    private float orbLayoutStartX;
    private float orbLayoutStartY;
    private float orbLayoutSlotSize;
    private float orbLayoutGap;

    public GameShop(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        this.shapeRenderer = new ShapeRenderer();
        this.orbLayoutStartX = x + ORB_CARD_START_X;
        this.orbLayoutStartY = getOrbRowY();
        this.orbLayoutSlotSize = ORB_CARD_WIDTH;
        this.orbLayoutGap = ORB_CARD_GAP;
    }

    public void render(int playerGold) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        
        shapeRenderer.setColor(0.08f, 0.08f, 0.12f, 0.98f);
        shapeRenderer.rect(x, y, width, height);

        
        shapeRenderer.setColor(0.15f, 0.12f, 0.2f, 1f);
        shapeRenderer.rect(x, y + height - 35, width, 35);

        
        shapeRenderer.setColor(0.12f, 0.1f, 0.15f, 1f);
        shapeRenderer.rect(x, y + height - 65, width, 25);

        
        float cardWidth = ORB_CARD_WIDTH;
        float cardHeight = ORB_CARD_HEIGHT;
        float startY = getOrbRowY();

        for (int i = 0; i < 4; i++) {
            float cardX = x + 10 + i * (cardWidth + 5);
            boolean canAfford = playerGold >= orbPrices[i];

            shapeRenderer.setColor(canAfford ? 0.15f : 0.08f, canAfford ? 0.15f : 0.08f, canAfford ? 0.18f : 0.1f, 1f);
            shapeRenderer.rect(cardX, startY, cardWidth, cardHeight);

            
            Element e = orbElements[i];
            shapeRenderer.setColor(e.getR() * (canAfford ? 1f : 0.5f), e.getG() * (canAfford ? 1f : 0.5f),
                    e.getB() * (canAfford ? 1f : 0.5f), 1f);
            shapeRenderer.circle(cardX + cardWidth / 2, startY + cardHeight - 25, 18);
        }

        
        float mergeSectionY = startY - 35;
        shapeRenderer.setColor(0.12f, 0.1f, 0.15f, 1f);
        shapeRenderer.rect(x, mergeSectionY, width, 25);

        
        float mergeStartY = mergeSectionY - 60;
        Element[] hybrids = { Element.STEAM, Element.GOLD, Element.LIGHT, Element.POISON, Element.ICE, Element.LIFE };

        for (int i = 0; i < 6; i++) {
            float cardX = x + 10 + (i % 3) * 80;
            float cardY = mergeStartY - (i / 3) * 50;

            shapeRenderer.setColor(0.1f, 0.1f, 0.12f, 1f);
            shapeRenderer.rect(cardX, cardY, 70, 45);

            Element h = hybrids[i];
            shapeRenderer.setColor(h.getR(), h.getG(), h.getB(), 1f);
            shapeRenderer.circle(cardX + 22, cardY + 22, 12);
        }

        shapeRenderer.end();

        
        batch.begin();

        
        font.setColor(Color.GOLD);
        font.draw(batch, "SHOP", x + width / 2 - 30, y + height - 8);
        font.setColor(Color.YELLOW);
        font.draw(batch, playerGold + " G", x + width - 55, y + height - 8);

        
        font.setColor(Color.ORANGE);
        font.draw(batch, "ORBS [CLICK]", x + 10, y + height - 45);

        
        for (int i = 0; i < 4; i++) {
            float cardX = x + 10 + i * 60;
            boolean canAfford = playerGold >= orbPrices[i];

            font.setColor(canAfford ? Color.GOLD : Color.RED);
            font.draw(batch, orbPrices[i] + "G", cardX + 15, startY + 15);

            font.setColor(Color.WHITE);
            font.draw(batch, "[" + (i + 1) + "]", cardX + 20, startY + cardHeight - 5);
        }

        
        font.setColor(Color.MAGENTA);
        font.draw(batch, "MERGE TABLE", x + 10, mergeSectionY + 18);

        String[] mergeFormulas = { "F+W", "F+E", "F+A", "W+E", "W+A", "E+A" };
        for (int i = 0; i < 6; i++) {
            float cardX = x + 10 + (i % 3) * 80;
            float cardY = mergeStartY - (i / 3) * 50;

            font.setColor(Color.LIGHT_GRAY);
            font.draw(batch, mergeFormulas[i], cardX + 40, cardY + 28);
        }

        batch.end();
    }

    public int getOrbPrice(int index) {
        if (index >= 0 && index < 4)
            return orbPrices[index];
        return -1;
    }

    public Element getOrbElement(int index) {
        if (index >= 0 && index < 4)
            return orbElements[index];
        return null;
    }

    public int getOrbIndexAt(float screenX, float screenY) {
        for (int i = 0; i < 4; i++) {
            float cardX = orbLayoutStartX + i * (orbLayoutSlotSize + orbLayoutGap);
            if (screenX >= cardX && screenX <= cardX + orbLayoutSlotSize &&
                    screenY >= orbLayoutStartY && screenY <= orbLayoutStartY + orbLayoutSlotSize) {
                return i;
            }
        }
        return -1;
    }

    public void setOrbLayout(float startX, float startY, float slotSize, float slotGap) {
        this.orbLayoutStartX = startX;
        this.orbLayoutStartY = startY;
        this.orbLayoutSlotSize = slotSize;
        this.orbLayoutGap = slotGap;
    }

    public boolean contains(float screenX, float screenY) {
        return screenX >= x && screenX <= x + width && screenY >= y && screenY <= y + height;
    }

    private float getOrbRowY() {
        return y + height - 145f;
    }

    public void resize(int screenWidth, int screenHeight) {
        batch.getProjectionMatrix().setToOrtho2D(0, 0, screenWidth, screenHeight);
        shapeRenderer.getProjectionMatrix().setToOrtho2D(0, 0, screenWidth, screenHeight);
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        shapeRenderer.dispose();
    }
}

