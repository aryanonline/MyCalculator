

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
/*
Name: Aryan Singh
Date: 18 January 2019
Description: A simple utility class that has static methods that are called from other classes
Features: Use of GridBagLayout to allow for buttons and other components to be able to occupy more than one column and row
MajorSkills: Use of buffered image and ImageIcons. Use of try and catch statements
Areas of concern: None
 */
public final class CalcUtils {

    //common method to add components to GridBagLayout
    public static void addToGridBagLayout(JComponent panel, JComponent item, int gridx, int gridy, int gridwidth, int gridheight, int fillConstant){
        GridBagConstraints c = new GridBagConstraints();
        c.gridx=gridx; //col
        c.gridy=gridy; //row
        c.gridwidth=gridwidth; //column span
        c.gridheight=gridheight; // row span
        c.fill = fillConstant; //fill hint
        c.weightx=1; //occupy remaining space horizontal
        c.weighty=1; //occupy remaining space vertical
        panel.add(item, c);
    }

    //get buffered Image
    public static ImageIcon getTransparentIcon(String fileName, int width, int height){
        BufferedImage bf;
        ImageIcon ic = null;
        try{
            bf = ImageIO.read(new File("images/" + fileName));
            ic = new ImageIcon(bf);
            ic.setImage(ic.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
        }
        catch(IOException ex){
            System.out.println(ex.toString());
        }

        return ic;
    }

    public static boolean isNumeric(String strNum) {
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

}
