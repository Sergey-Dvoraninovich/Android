package com.example.game1app;

import java.util.ArrayList;

public class FieldSet {

    ArrayList<Integer> ships;
    boolean is_horisontal;
    ArrayList<String> field;
    boolean[][] land;

    public FieldSet()
    {
        ships = new ArrayList<Integer>();
        ships.add(4);
        ships.add(3); ships.add(3);
        ships.add(2); ships.add(2); ships.add(2);
        ships.add(1); ships.add(1); ships.add(1); ships.add(1);
        field = new ArrayList<String>();
        for (int i = 0; i < 100; i++)
            field.add("blank");
        is_horisontal = true;
        land = new boolean[10][10];
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++)
                land[i][j] = true;
    }

    public int getShip()
    {
        int num;
        if (ships.size() != 0) {
            num = ships.get(0);
            ships.remove(0);
        }
        else {
            num = -1;
        }
        return num;
    }

    public boolean Check(ArrayList<String> state, int position, int len, boolean orientation)
    {
        boolean ans = false;
        int pos = 0;
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++)
            {
                if (state.get(pos) == "blank")
                    land[i][j] = true;
                else
                    land[i][j] = false;
                pos++;
            }

        boolean barrier = false;
        int line = position / 10;
        int base = position % 10;
        if (land[line][base]) {
            if (orientation) //horizontal
            {
                pos = base;
                int pos_l = 0;
                while ((pos < 10) && (pos_l < len)) {
                    if (!land[line][pos])
                        barrier = true;
                    pos++;
                    pos_l++;
                }
                if ((pos_l == len) && (!barrier))
                    ans = true;
            }
            else //vertical
            {
                pos = line;
                int pos_l = 0;
                while ((pos < 10) && (pos_l < len)) {
                    if (!land[pos][base])
                        barrier = true;
                    pos++;
                    pos_l++;
                }
                if ((pos_l == len) && (!barrier))
                    ans = true;
            }
        }
        return ans;
    }

    public ArrayList<String> setShip(ArrayList<String> state, int position, int len, boolean orientation)
    {
        int pos = 0;
        int line = position / 10;
        int base = position % 10;
        if (land[line][base]) {
            if (orientation) //horizontal
            {
                pos = base;
                int pos_l = 0;
                while (pos_l < len) {
                    land[line][pos] = false;
                    state.set(line * 10 + pos, "ship");
                    state = SetPoint(state, line, pos);
                    pos++;
                    pos_l++;
                }
            }
            else //vertical
            {
                pos = line;
                int pos_l = 0;
                while ((pos < 10) && (pos_l < len)) {
                    land[pos][base] = false;
                    state.set(pos * 10 + base, "ship");
                    state = SetPoint(state, pos, base);
                    pos++;
                    pos_l++;
                }
            }
        }
        return state;
    }

    private ArrayList<String> SetPoint(ArrayList<String> state, int line, int pos)
    {
        if (line - 1 >= 0)
        {
            if (pos - 1 >= 0)
                if (land[line-1][pos-1])
                {
                    land[line-1][pos-1] = false;
                    state.set((line-1) * 10 + pos - 1, "used");
                }
            if (land[line-1][pos])
            {
                land[line-1][pos] = false;
                state.set((line-1) * 10 + pos, "used");
            }
            if (pos + 1 < 10)
                if (land[line-1][pos+1])
                {
                    land[line-1][pos+1] = false;
                    state.set((line-1) * 10 + pos + 1, "used");
                }
        }
        if (line + 1 < 10)
        {
            if (pos - 1 >= 0)
                if (land[line+1][pos-1])
                {
                    land[line+1][pos-1] = false;
                    state.set((line+1) * 10 + pos - 1, "used");
                }
            if (land[line+1][pos])
            {
                land[line+1][pos] = false;
                state.set((line+1) * 10 + pos, "used");
            }
            if (pos + 1 < 10)
                if (land[line+1][pos+1])
                {
                    land[line+1][pos+1] = false;
                    state.set((line+1) * 10 + pos + 1, "used");
                }
        }
        if (pos - 1 >= 0)
            if (land[line][pos-1])
            {
                land[line][pos-1] = false;
                state.set((line) * 10 + pos - 1, "used");
            }
        if (pos + 1 < 10)
            if (land[line][pos+1])
            {
                land[line][pos+1] = false;
                state.set((line) * 10 + pos + 1, "used");
            }
        return state;
    }
}
