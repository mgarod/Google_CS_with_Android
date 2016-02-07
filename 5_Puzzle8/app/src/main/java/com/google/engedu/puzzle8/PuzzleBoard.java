package com.google.engedu.puzzle8;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;


public class PuzzleBoard {

    private static final int NUM_TILES = 3;
    private static final int[][] NEIGHBOUR_COORDS = {
            { -1, 0 },
            { 1, 0 },
            { 0, -1 },
            { 0, 1 }
    };
    private ArrayList<PuzzleTile> tiles;

    PuzzleBoard(Bitmap bitmap, int parentWidth) {
        bitmap = Bitmap.createScaledBitmap(bitmap, parentWidth, parentWidth, false);
        tiles = new ArrayList<>();
        int tile_counter = 1;
        int tile_width = bitmap.getWidth() / NUM_TILES;
        int tile_height = bitmap.getHeight() / NUM_TILES;

        int X = 0;
        int Y = 0;

        for (int i = 0; i < NUM_TILES; i++) {
            for (int j = 0; j < NUM_TILES; j++) {
                Bitmap tile = Bitmap.createBitmap(bitmap, X, Y, tile_width, tile_height);

                if(tile_counter == 1) {
                    tiles.add(null);
                }
                else {
                    tiles.add(new PuzzleTile(tile, tile_counter));
                }
                tile_counter += 1;
                X += tile_width;
            }

            Y += tile_height;
            X = 0;
        }
    }

    PuzzleBoard(PuzzleBoard otherBoard) {
        tiles = (ArrayList<PuzzleTile>) otherBoard.tiles.clone();
    }

    public void reset() {
        // Nothing for now but you may have things to reset once you implement the solver.
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        return tiles.equals(((PuzzleBoard) o).tiles);
    }

    public void draw(Canvas canvas) {
        if (tiles == null) {
            return;
        }
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                tile.draw(canvas, i % NUM_TILES, i / NUM_TILES);
            }
        }
    }

    public boolean click(float x, float y) {
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                if (tile.isClicked(x, y, i % NUM_TILES, i / NUM_TILES)) {
                    return tryMoving(i % NUM_TILES, i / NUM_TILES);
                }
            }
        }
        return false;
    }

    public boolean resolved() {
        for (int i = 0; i < NUM_TILES * NUM_TILES - 1; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile == null || tile.getNumber() != i)
                return false;
        }
        return true;
    }

    private int XYtoIndex(int x, int y) {
        return x + y * NUM_TILES;
    }

    protected void swapTiles(int i, int j) {
        PuzzleTile temp = tiles.get(i);
        tiles.set(i, tiles.get(j));
        tiles.set(j, temp);
    }

    private boolean tryMoving(int tileX, int tileY) {
        for (int[] delta : NEIGHBOUR_COORDS) {
            int nullX = tileX + delta[0];
            int nullY = tileY + delta[1];
            if (nullX >= 0 && nullX < NUM_TILES && nullY >= 0 && nullY < NUM_TILES &&
                    tiles.get(XYtoIndex(nullX, nullY)) == null) {
                swapTiles(XYtoIndex(nullX, nullY), XYtoIndex(tileX, tileY));
                return true;
            }

        }
        return false;
    }

    public ArrayList<PuzzleBoard> neighbours() {
        ArrayList<PuzzleBoard> neighbors = new ArrayList<PuzzleBoard>();
        int bound = NUM_TILES * NUM_TILES;

        int i = 0;
        for( ; i < bound; i++){
            if (tiles.get(i) == null){
                break;
            }
        }

        if (i + 3 < NUM_TILES){
            PuzzleBoard down = new PuzzleBoard(this);
            down.swapTiles(i+3, i);
            neighbors.add(down);
        }

        if (i + 1 < NUM_TILES){
            PuzzleBoard right = new PuzzleBoard(this);
            right.swapTiles(i+1, i);
            neighbors.add(right);
        }

        if (i - 3 >= 0){
            PuzzleBoard up = new PuzzleBoard(this);
            up.swapTiles(i - 3, i);
            neighbors.add(up);
        }

        if (i - 1 >= 0){
            PuzzleBoard left = new PuzzleBoard(this);
            left.swapTiles(i-1, i);
            neighbors.add(left);
        }

        return neighbors;
    }

    public int priority() {
        return 0;
    }
}
