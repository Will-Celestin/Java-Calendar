package calendar;

import javax.swing.*;

/**
 * Main class for application
 * Integrates main GUI components
 * Launches application
 * Created by Will. Celestin on 7/11/2015
 */

class Meeti extends JFrame {

    public static int dayShift = 0; // number days moved from today's date
    public static String season; // sets background picture based on season
    public JLayeredPane container; // contains all components of JFrame
    public JLabel bg; // contains background picture

    /**
     * Constructor: displays application GUI
     */
    public Meeti() {

        setUndecorated(true); // break away from default OS-based GUI


        container = new JLayeredPane(); // components are stacked
        container.setLayout(null); // components are absolutely positioned

        bg = new JLabel();
        bg.setBounds(0, 0, 900, 575);

        GuiUtils util = new GuiUtils(this);
        String date = util.getTodayDate(0, container, bg); // get date

        bg.setIcon(new ImageIcon(this.getClass().getResource("../images/" + season + ".jpg")));
        container.add(bg, 0, 0); // set background image based on season

        util.titleBar(); // set custom title bar for JFrame

        JPopupMenu pop = util.createPop(container, bg); // set the popup menu
        container.add(util.createmenu(pop, container), 2, 0); // set the menu button
        container.add(util.getCalendar(date, container, bg), 1, 0); // set the calendar

        add(container);
        setContentPane(container);
        util.styleFrame(); // style JFrame
    }

    /**
     * Launches application
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Meeti();
            }
        });
    }
}