package com.github.drizzleme.client.ui;

import com.eva.epc.common.util.OS;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import javax.swing.*;
import java.awt.*;

/**
 * Created with drizzleIM
 *
 * @author ; DRIZZLEME
 *         DATE : 2016/9/22
 */
public class Launch {
    private static void initUserInterface()
    {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "MobileIMSDK4jDemo");
        try
        {
            UIManager.put("RootPane.setupButtonVisible", false);
            BeautyEyeLNFHelper.translucencyAtFrameInactive = false;
            BeautyEyeLNFHelper.launchBeautyEyeLNF();

            if(!(OS.isWindowsXP() || OS.isWindows2003()))
            {
                /** UIManager中UI字体相关的key */
                String[] DEFAULT_FONT  = new String[]{
                        "Table.font"
                        ,"TableHeader.font"
                        ,"CheckBox.font"
                        ,"Tree.font"
                        ,"Viewport.font"
                        ,"ProgressBar.font"
                        ,"RadioButtonMenuItem.font"
                        ,"ToolBar.font"
                        ,"ColorChooser.font"
                        ,"ToggleButton.font"
                        ,"Panel.font"
                        ,"TextArea.font"
                        ,"Menu.font"
                        ,"TableHeader.font"
                        // ,"TextField.font"
                        ,"OptionPane.font"
                        ,"MenuBar.font"
                        ,"Button.font"
                        ,"Label.font"
                        ,"PasswordField.font"
                        ,"ScrollPane.font"
                        ,"MenuItem.font"
                        ,"ToolTip.font"
                        ,"List.font"
                        ,"EditorPane.font"
                        ,"Table.font"
                        ,"TabbedPane.font"
                        ,"RadioButton.font"
                        ,"CheckBoxMenuItem.font"
                        ,"TextPane.font"
                        ,"PopupMenu.font"
                        ,"TitledBorder.font"
                        ,"ComboBox.font"
                };
                // 调整默认字体
                for (int i = 0; i < DEFAULT_FONT.length; i++)
                    UIManager.put(DEFAULT_FONT[i],new Font("微软雅黑", Font.PLAIN,12));
            }
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedLookAndFeelException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void main(final String... args)
    {
//		// init MobileIMSDK first
//		IMClientManager.getInstance().initMobileIMSDK();
        // init gui properties
        Launch.initUserInterface();
        // startup GUI
        SwingUtilities.invokeLater(() -> {
            LoginGUI loginFrame = new LoginGUI();
            loginFrame.setLocationRelativeTo(null);
            loginFrame.setVisible(true);
        });
    }
}
