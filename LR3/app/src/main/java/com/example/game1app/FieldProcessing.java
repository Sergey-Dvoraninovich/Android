package com.example.game1app;

import java.util.ArrayList;

public class FieldProcessing {
    ArrayList<String> field;
    String[][] land;

    public FieldProcessing()
    {
        this.land = new String[10][10];
    }

    public FieldProcessing(ArrayList<String> field)
    {
        this.field = field;
        this.land = new String[10][10];
        for (int i = 0; i < 100; i++)
        {
            land[i/10][i%10] = field.get(i);
        }
    }

    public boolean isWinner(ArrayList<String> field)
    {
        boolean ans = true;
        for (int i = 0; i < 100; i++)
            if(field.get(i).equals("ship"))
                ans = false;

        return ans;
    }

    public ArrayList<String> ShipRound(ArrayList<String> field, int position)
    {
        boolean ans = true;
        this.field = field;
        for (int i = 0; i < 100; i++)
        {
            land[i/10][i%10] = field.get(i);
        }
        int i= 0;
        if (position != 0)
            i = position/10;
        int j = 0;
        if (position != 0)
            j = position%10;
        while (i < 10)
            if (land[i][j].equals("shooted"))
                i++;
            else
                break;
        if (i != 10)
            if (land[i][j].equals("ship"))
                ans = false;

        i = 0;
        if (position != 0)
            i = position/10;
        while (i >= 0)
            if (land[i][j].equals("shooted"))
                i--;
            else
                break;
        if (i >= 0)
            if (land[i][j].equals("ship"))
                ans = false;

        i = 0;
        if (position != 0)
            i = position/10;
        while (j < 10)
            if (land[i][j].equals("shooted"))
                j++;
            else
                break;
        if (j != 10)
            if (land[i][j].equals("ship"))
                ans = false;

        j = 0;
        if (position != 0)
            j = position%10;
        while (j >= 0)
            if (land[i][j].equals("shooted"))
                j--;
            else
                break;
        if (j >= 0)
            if (land[i][j].equals("ship"))
                ans = false;

        if (ans)
        {
            i= 0;
            if (position != 0)
                i = position/10;
            j = 0;
            if (position != 0)
                j = position%10;
            SetAround(i, j);
            while (i < 10)
            {
                if (land[i][j].equals("shooted")) {
                    SetAround(i, j);
                    i++;
                }
                else
                    break;
            }

            i = 0;
            if (position != 0)
                i = position/10;
            while (i >= 0)
            {
                if (land[i][j].equals("shooted")) {
                    SetAround(i, j);
                    i--;
                }
                else
                    break;
            }

            i = 0;
            if (position != 0)
                i = position/10;
            while (j < 10)
            {
                if (land[i][j].equals("shooted")) {
                    SetAround(i, j);
                    j++;
                }
                else
                    break;
            }

            j = 0;
            if (position != 0)
                j = position%10;
            while (j >= 0)
            {
                if (land[i][j].equals("shooted")) {
                    SetAround(i, j);
                    j--;
                }
                else
                    break;
            }
        }

        return this.field;
    }

    private void SetAround(int i, int j)
    {
        if ((i - 1 >= 0)&&(j - 1 >= 0))
            if (!land[i-1][j-1].equals("shooted"))
            {
                land[i-1][j-1] = "used";
                field.set((i-1)*10 + j-1, "used");
            }

        if ((i - 1 >= 0))
            if (!land[i-1][j].equals("shooted"))
            {
                land[i-1][j] = "used";
                field.set((i-1)*10 + j, "used");
            }

        if ((i - 1 >= 0)&&(j + 1 < 10))
            if (!land[i-1][j+1].equals("shooted"))
            {
                land[i-1][j+1] = "used";
                field.set((i-1)*10 + j+1, "used");
            }



        if ((j - 1  >= 0))
            if (!land[i][j-1].equals("shooted"))
            {
                land[i][j-1] = "used";
                field.set((i)*10 + j - 1, "used");
            }

        if ((j + 1 < 10))
            if (!land[i][j+1].equals("shooted"))
            {
                land[i][j+1] = "used";
                field.set((i)*10 + j+1, "used");
            }



        if ((i + 1 < 10)&&(j - 1 >= 0))
            if (!land[i+1][j-1].equals("shooted"))
            {
                land[i+1][j-1] = "used";
                field.set((i+1)*10 + j-1, "used");
            }

        if ((i + 1 < 10))
            if (!land[i+1][j].equals("shooted"))
            {
                land[i+1][j] = "used";
                field.set((i+1)*10 + j, "used");
            }

        if ((i + 1 < 10)&&(j + 1 < 10))
            if (!land[i+1][j+1].equals("shooted"))
            {
                land[i+1][j+1] = "used";
                field.set((i+1)*10 + j+1, "used");
            }
    }
}
