/*
    Copyright 2020-2021 Andrei Datcu.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */

package andreid;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class maclinuxutils {
    private JButton button1;
    private JPanel panel1;
    private JButton turboBoostSwitcherButton;
    private JButton aboutButton;
    private JButton issuesButton;
    private JButton updatesButton;
    private JButton attributionButton;
    private JButton preferencesButton;
    private JButton presetsButton;
    private JComboBox uiMgr;
    JFrame frame = new JFrame("MacLinuxUtils");

    public maclinuxutils() {
        button1.addActionListener(actionEvent -> {
            fancontrol fanObj = new fancontrol();
            fanObj.getFrame();
        });

        // Turbo Boost Switching

        turboBoostSwitcherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                turboboost turboObj = new turboboost();
                turboObj.getFrame();
            }
        });

        issuesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    String root = "xdg-open https://github.com/datcuandrei/MacLinuxUtils/issues";
                    Process p = Runtime.getRuntime().exec(root);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        updatesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    String root = "xdg-open https://github.com/datcuandrei/MacLinuxUtils/releases";
                    Process p = Runtime.getRuntime().exec(root);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        attributionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                about aboutButton = new about();
                aboutButton.getAbout();
            }
        });
        presetsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                presets presetsobj = new presets();
                try {
                    presetsobj.defaultConfig();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                presetsobj.getFrame();
            }
        });
        uiMgr.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String mode = Objects.requireNonNull(uiMgr.getSelectedItem()).toString();
                if (mode.equals("Dark")) {
                    try {
                        UIManager.setLookAndFeel(new FlatDarculaLaf());
                    } catch (UnsupportedLookAndFeelException lookAndFeelException) {
                        lookAndFeelException.printStackTrace();
                    }
                } else {
                    try {
                        UIManager.setLookAndFeel(new FlatLightLaf());
                    } catch (UnsupportedLookAndFeelException lookAndFeelException) {
                        lookAndFeelException.printStackTrace();
                    }
                }
                SwingUtilities.updateComponentTreeUI(panel1);
            }
        });
    }

    // Frame
    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public JFrame getFrame() {
        frame.setContentPane(new maclinuxutils().panel1);
        frame.setVisible(true);
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        return frame;
    }

    // Main method

    public static void main(String args[]) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException, IOException {
        UIManager.setLookAndFeel( new FlatLightLaf() ); // for dark mode = FlatDarculaLaf ; for light mode = FlatLightLaf.

        // Checking root permissions
        String[] cmdROOT = {"/bin/bash","-c","echo $USER"};
        Process checkROOT = Runtime.getRuntime().exec(cmdROOT);
        BufferedReader readUSER = new BufferedReader(new InputStreamReader(checkROOT.getInputStream()));
        String user = readUSER.readLine();
        System.out.println(user);
        if(!user.equals("root")){
            JOptionPane.showMessageDialog(null,"MacLinuxUtils needs to run as root to work properly.Please relaunch the program.","Error",JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // Checking if the computer is a Mac.
        boolean checkIfMac = new File("/sys/devices/platform/applesmc.768").exists();

        if (checkIfMac == false){
            boolean checkIfWindows = new File("C:\\Windows").exists();

            boolean checkIfOSX = new File("/System/Library").exists();

            if(checkIfOSX == true){
                JOptionPane.showMessageDialog(null, "This program was created for Linux!", "Oops!",JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }else if(checkIfWindows == true){
                JOptionPane.showMessageDialog(null, "This program was created for Linux!", "Oops!",JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }else{
                JOptionPane.showMessageDialog(null, "This program is for Intel-based Apple Macs only!", "Oops!",JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }
        else {
            // Create system folder.
            File sys = new File("/opt/maclinuxutils/");
            boolean checkSys = sys.exists();
            if(!checkSys){
                sys.mkdirs();
                URL url = new URL("https://raw.githubusercontent.com/datcuandrei/MacLinuxUtils/master/LICENSE");
                File out = new File("/opt/maclinuxutils/LICENSE.md");
                FileUtils.copyURLToFile(url,out);
            }
            // Create preferences file
            File prefs = new File("/opt/maclinuxutils/preferences/");
            boolean checkPrefs = prefs.exists();
            if (!checkPrefs){
                prefs.mkdirs();
                FileWriter startfile = new FileWriter("/opt/maclinuxutils/preferences/startup.txt");
                new presets().defaultConfig();
                startfile.write("system");
                startfile.close();
            }
            // Create presets folder
            File presets = new File("/opt/maclinuxutils/presets/");
            boolean checkPresets = presets.exists();
            if (!checkPresets){
                presets.mkdirs();
            }

            // Experimental mode
            JOptionPane.showMessageDialog(null, "<html><head></head><body><div align=\"center\"><h1>Welcome to experimental mode!</h1><h2>What is experimental mode?</h2><h3><br/>Experimental mode is a special mode created for users that want to test new features<br>that are still work-in-progress.Please note that this is a standalone build;it doesn't depend in<br>any way on the stable build(which was done in November).</h3><br/><h2>v2.3.2 Experimental Mode Changelog : </h2><h3>- Manual Mode</h3><br/><h2>Any issues you find,it is recommended that you report them by using the Issues button in the main menu.<br>Contribution to the code is always welcomed.<br>Thank you for testing MacLinuxUtils new features!</h2><br/><h2>Notice : As of June 2021, development of MacLinuxUtils has ended. MacLinuxUtils might receive minor updates for bugs,in case any are found.<br> The features that are under experimental mode will not end up in the final build. </h2></div></body></html>", "Experimental mode",JOptionPane.PLAIN_MESSAGE);

            // Frame
            JFrame frame = new JFrame("MacLinuxUtils");
            frame.setContentPane(new maclinuxutils().panel1);
            frame.setVisible(true);
            frame.setSize(600, 300);
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    if (JOptionPane.showConfirmDialog(null, "Do you want to exit MacLinuxUtils or leave it running in the background?", "Exit",
                            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        System.exit(0);
                    } else {
                        JOptionPane.showMessageDialog(null,"MacLinuxUtils will keep running in the background.You can access it at any time through the system tray.");
                        frame.dispose();
                    }
                }
            });
            frame.setLocationRelativeTo(null);

            // Startup preset
            BufferedReader readPreset = new BufferedReader(new FileReader("/opt/maclinuxutils/preferences/startup.txt"));
            startup first = new startup();
            String preset = readPreset.readLine();
            boolean checkPreset = new File("/opt/maclinuxutils/presets/"+preset).exists();
            if(checkPreset == true){
                first.load(preset);
            }else{
                System.out.println("Selected startup preset doesn't exist...\nWill load the default one.");
                first.load("system");
            }

            // System tray

            // Checking if DE supports system tray
            if(!SystemTray.isSupported()){
             JOptionPane.showMessageDialog(null,"The desktop environment you are using doesn't have support for system trays.As such, MacLinuxUtils disabled the system tray.","Notice",JOptionPane.INFORMATION_MESSAGE);
            }else{
                boolean checkIco = new File("/opt/maclinuxutls/fanico.png").exists();
                if(checkIco == false){
                    URL url = new URL("https://raw.githubusercontent.com/datcuandrei/MacLinuxUtils/master/resources/fanico.png");
                    File out = new File("/opt/maclinuxutils/fanico.png");
                    FileUtils.copyURLToFile(url,out);
                    systray tray = new systray();
                    tray.getSystray();
                }
                else{
                    systray tray = new systray();
                    tray.getSystray();
                }
            }
        }
    }
}
