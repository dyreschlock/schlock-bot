package setup;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class CreateCropLegendsShinyIcons
{
    private static final String LEGENDS_FOLDER = "web/img/legends/";
    private static final String ICONS_ALL = "icons_all.png";

    private static final Integer HEIGHT_WIDTH = 128;

    private static final Integer BORDER = 1;

    private static final Integer FULL_HEIGHT_WIDTH = HEIGHT_WIDTH + BORDER*2;

    private static final Integer COLUMNS = 14;
    private static final Integer ROWS = 27;


    public void run() throws Exception
    {
        BufferedImage iconsAll = ImageIO.read(new File(LEGENDS_FOLDER + ICONS_ALL));

        System.out.println(iconsAll.getWidth() + " : " + iconsAll.getHeight());

        int index = 0;

        for (int r = 0; r < ROWS; r++)
        {
            for (int c = 0; c < COLUMNS; c++)
            {
                int x_position = c*FULL_HEIGHT_WIDTH + BORDER;
                int y_position = r*FULL_HEIGHT_WIDTH + BORDER;

                BufferedImage icon = iconsAll.getSubimage(x_position, y_position, HEIGHT_WIDTH, HEIGHT_WIDTH);

                String indexString = getIndexFilename(index);
                File output = new File(LEGENDS_FOLDER + indexString);

                ImageIO.write(icon, "png", output);

                index++;
            }
        }
        System.out.println("done");
    }

    private String getIndexFilename(int index)
    {
        String indexString = Integer.toString(index);
        while (indexString.length() < 3)
        {
            indexString = "0" + indexString;
        }

        return indexString + ".png";
    }







    public static void main(String[] args) throws Exception
    {
//        new CreateCropLegendsShinyIcons().run();
    }
}
