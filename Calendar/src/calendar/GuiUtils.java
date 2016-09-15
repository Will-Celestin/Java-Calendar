package calendar;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.Date;

/**
 * Supports Meeti class
 * Create main GUI components
 * Created by Will. Celestin on 7/11/2015.
 */

public class GuiUtils extends JFrame {

    public String aot = "Always on Top    (off)"; // always on top identifier
    public JFrame frame; // JFrame of application
    public JPanel Cal; // JPanel containing calendar
    public String dateCurr; // current date

    // JPanels that display dialog windows
    public JPanel About;
    public JPanel Reset;
    public JPanel Event;
    public JPanel MakeSchedule;


    int dragx, dragy; // mouse coordinate on the title_bar for draggability
    private String dataDir; // directory path for user's schedule

    /**
     * Constructor: passes Meeti class' JFrame
     */
    public GuiUtils(JFrame frame) {
        this.frame = frame;
    }

    /**
     * Changes a button's color on hover
     */
    public static void changeHoverColor(JButton btn) {
        btn.addMouseListener(new MouseAdapter() { // change exit button color on hover
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(255, 127, 36));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(255, 165, 79));
            }
        });
    }

    /**
     * Styles the minimize and exit buttons
     */
    public static JButton styleminmax(JButton btn) {
        btn.setFocusPainted(false);
        btn.setBackground(new Color(255, 127, 36));
        btn.setFont(new Font("Arial", Font.PLAIN, 20));
        btn.setBorder(BorderFactory.
                createEtchedBorder(EtchedBorder.LOWERED));
        return btn;
    }

    /**
     * Styles menu items i.e. save,open
     */
    public static JMenuItem styleJmenu(JMenuItem tab) {
        tab.setBackground(new Color(255, 177, 81));
        tab.setPreferredSize(new Dimension(100, 30));
        tab.setFont(new Font("Arial", Font.BOLD, 12));
        tab.setBorder(BorderFactory.
                createMatteBorder(1, 0, 0, 0, new Color(205, 133, 63)));
        return tab;
    }

    /**
     * Returns dir of user's schedule
     */
    public String getdataDir() {
        return dataDir;
    }

    /**
     * Styles the JFrame
     */
    public void styleFrame() {
        frame.setSize(900, 602);
        frame.setVisible(true);
        frame.setIconImage(Toolkit.getDefaultToolkit().
                getImage(getClass().getResource("../images/logo.png")));
        frame.setLocationRelativeTo(null); //starts at screen's center
    }

    /**
     * Initializes JFrame's title bar
     */
    public JMenuBar titleBar() {
        // title_bar containing:
        // JLabel for logo and JPanel for minimize/close buttons
        JMenuBar title_bar = new JMenuBar();
        title_bar.setBorder(null);
        title_bar.setLayout(new BorderLayout());
        title_bar.setBackground(new Color(255, 127, 36));
        frame.setJMenuBar(title_bar);

        //create and add JLabel to title_bar
        JLabel label = new JLabel("MeeTi");
        label.setFont(new Font("Arial", Font.PLAIN, 20));
        title_bar.add(label);

        // JPanel containing close and minimize buttons
        JPanel top = new JPanel(new GridLayout(0, 2));

        // creating and styling buttons
        JButton min = new JButton("-");
        JButton close = new JButton(" x ");
        min.addMouseListener(new MouseAdapter() { // turn minimize button blue on hover
            @Override
            public void mouseEntered(MouseEvent e) {
                min.setBackground(new Color(72, 118, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                min.setBackground(new Color(255, 127, 36));
            }
        });
        close.addMouseListener(new MouseAdapter() { // turn close button red on hover
            @Override
            public void mouseEntered(MouseEvent e) {
                close.setBackground(new Color(255, 28, 28));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                close.setBackground(new Color(255, 127, 36));
            }
        });
        top.add(styleminmax(min));
        top.add(styleminmax(close));
        title_bar.add(top, BorderLayout.EAST); // move buttons to top right of JFrame

        // iconify JFrame on "-" mouse event
        min.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                frame.setState(ICONIFIED);
            }
        });
        // dispose frame on "x" mouse event
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                frame.dispose();
            }
        });

        // get coordinates of mouse pressed on title_bar (select title_bar)
        title_bar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                dragx = me.getX();
                dragy = me.getY();
            }
        });
        // set location of JFrame dynamically based on mouse movement (drag title_bar)
        title_bar.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent me) {
                frame.setLocation(
                        frame.getLocation().x + me.getX() - dragx,
                        frame.getLocation().y + me.getY() - dragy);
            }
        });
        return title_bar;
    }

    /**
     * Creates about-us dialog
     */
    public JPanel showAbout(JLayeredPane container) {
        // create the about panel
        JPanel about = new JPanel(null);
        about.setBounds(180, 80, 550, 400);
        about.setBorder(BorderFactory.createRaisedBevelBorder());

        // create exit button
        JButton exit = new JButton("Close");
        about.add(exit);
        exit.setBounds(250, 330, 60, 30);
        exit.setBackground(new Color(255, 165, 79));
        exit.setBorder(BorderFactory.createRaisedBevelBorder());

        // use html to format about page paragraph
        String s;
        s = "<html><h1 align='center'>MeeTi v1.1</h1>";
        s += "<html><h2 align='center'>Developed by Will. Celestin</h2>";
        s += "<br/>";
        s += "<html><p align='center'>This piece of software was created as a learning assignment</p>";
        s += "<br/>";
        s += "<html><p align='center'>Feedback is very much appreciated at wc424@cornell.edu</p>";
        s += "<br/>";
        s += "<html><p align='center'>Credits to Tips4Java for the text-prompt seen in the search bar</p>";
        s += "<br/>";
        s += "<html><p align='center'>Credits to hdwallpaper1080.net for the background images</p>";
        s += "<br/>";
        s += "<html><p align='center'>&copy; 2015</p>";
        // create about text
        JLabel words = new JLabel(s);
        words.setBounds(100, 10, 400, 300);
        about.add(words);

        changeHoverColor(exit); // change exit color on hover

        exit.addActionListener(new ActionListener() { // exit about panel on click
            @Override
            public void actionPerformed(ActionEvent e) {
                container.remove(about);
                container.revalidate();
                container.repaint();
            }
        });
        return about;
    }

    /**
     * Creates the menu button
     */
    public JButton createmenu(JPopupMenu pop, JLayeredPane container) {
        // create menu button with scaled striped icon
        JButton menu = new JButton(new ImageIcon(((new ImageIcon(
                this.getClass().getResource("../images/menu.png")).getImage()
                .getScaledInstance(38, 38,
                        java.awt.Image.SCALE_SMOOTH)))));
        menu.setBorder(null);
        menu.setBounds(0, 0, 38, 38);
        menu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        menu.setToolTipText(
                "<html><p>Double click calendar cell<br/> to add, view, or edit events</p>");
        container.add(menu, 1, 0);

        // menu button opens pop up menu
        menu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                pop.show(menu, 0, 0);
                pop.setBorder(null);
            }
        });
        return menu;
    }

    /**
     * Creates the popup menu containing open,new,about...
     */
    public JPopupMenu createPop(JLayeredPane container, JLabel bg) {
        // create text field (search bar)
        JTextField search = new JTextField(12);
        search.setBorder(null);
        // use TextPrompt class to create customized text prompt
        TextPrompt t = new TextPrompt("MM/dd/yyyy or event", search);
        t.setIcon(new ImageIcon(((new ImageIcon(
                this.getClass().getResource("../images/search.png")).getImage()
                .getScaledInstance(16, 16,
                        java.awt.Image.SCALE_SMOOTH)))));
        t.changeStyle(Font.ITALIC);
        t.setForeground(new Color(205, 102, 0));

        // create menu items
        JMenuItem New = new JMenuItem("New");
        JMenuItem open = new JMenuItem("Open");
        JMenuItem create = new JMenuItem("Create Schedule");
        JMenuItem saveas = new JMenuItem("Save As");
        JMenuItem today = new JMenuItem("Go To Today");
        JMenuItem reset = new JMenuItem("Reset Schedules");
        JMenuItem alwaysontop = new JMenuItem(aot);
        JMenuItem about = new JMenuItem("About");

        // add text field and styled menu items to popup menu
        JPopupMenu pop = new JPopupMenu();
        pop.add(search);
        pop.add(styleJmenu(New));
        New.setBorder(BorderFactory.createMatteBorder(
                0, 0, 0, 0, new Color(255, 165, 79))); // border consistency
        pop.add(styleJmenu(open));
        pop.add(styleJmenu(saveas));
        pop.add(styleJmenu(create));
        pop.add(styleJmenu(today));
        pop.add(styleJmenu(reset));
        pop.add(styleJmenu(alwaysontop));
        pop.add(styleJmenu(about));

        // get user input in search bar on enter key press
        Action enter_key_pressed = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userIquiry = search.getText(); // the user input

                if (Search.isDate(userIquiry)) {
                    userIquiry = Search.getCleanDate(userIquiry, dateCurr);
                    try {
                        // displays the date entered by user if it is not already being displayed
                        if (!userIquiry.equals(dateCurr)) {
                            dateCurr = userIquiry;
                            container.remove(Cal);
                            bg.setIcon(new ImageIcon(this.getClass().
                                    getResource("../images/" + Meeti.season + ".jpg")));
                            container.add(getCalendar(userIquiry, container, bg), 1, 0);
                            container.revalidate();
                            container.repaint();
                        }
                    } catch (NullPointerException err) {
                    }
                } else { // user input is not a date
                    try {
                        // display the panel with possible results for user search
                        JPanel event = getEvent(userIquiry, dataDir, container, bg);
                        if (Event != null) {
                            container.remove(Event);
                            container.add(event, 2, 0);
                            container.revalidate();
                            container.repaint();
                            Event = event;
                        } else {
                            container.add(event, 2, 0);
                            container.revalidate();
                            container.repaint();
                            Event = event;
                        }
                    } catch (NullPointerException err) {
                    }
                }
            }
        };
        search.addActionListener(enter_key_pressed);

        today.addActionListener(new ActionListener() { // gives today's calendar
            @Override
            public void actionPerformed(ActionEvent e) {
                String thedate = getTodayDate(0, container, bg);
                Meeti.dayShift = 0;
                container.remove(Cal);
                container.add(getCalendar(thedate, container, bg), 1, 0);
                container.revalidate();
                container.repaint();
            }
        });

        alwaysontop.addActionListener(new ActionListener() { // toggle always-on
            @Override
            public void actionPerformed(ActionEvent e) {
                if (aot == "Always on Top    (off)") {
                    frame.setAlwaysOnTop(true);
                    aot = "Always on Top    (on)";
                    alwaysontop.setText(aot);
                } else if (aot == "Always on Top    (on)") {
                    frame.setAlwaysOnTop(false);
                    aot = "Always on Top    (off)";
                    alwaysontop.setText(aot);
                }
            }
        });
        about.addActionListener(new ActionListener() { // show about panel
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel abt = showAbout(container);
                if (About != null) {
                    container.remove(About);
                    container.add(abt, 2, 0);
                    container.revalidate();
                    container.repaint();
                    About = abt;
                } else {
                    container.add(abt, 2, 0);
                    container.revalidate();
                    container.repaint();
                    About = abt;
                }
            }
        });
        New.addActionListener(new ActionListener() { // create new window (JFrame)
            @Override
            public void actionPerformed(ActionEvent e) {
                Meeti newMeeti = new Meeti();
                newMeeti.setLocation(frame.getX() + 38, frame.getY() + 66);
            }
        });
        open.addActionListener(new ActionListener() { // open new txt file with user data
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    if (file == null) {
                        return;
                    }
                    dataDir = chooser.getSelectedFile().getAbsolutePath();
                }
            }
        });
        reset.addActionListener(new ActionListener() { // reset txt file with user data
            @Override
            public void actionPerformed(ActionEvent e) {
                if (dataDir == null) { // no file do reset
                    JPanel makeSchedule = Schedules.makeAschedule();
                    if (MakeSchedule != null) {
                        container.remove(MakeSchedule);
                        container.add(makeSchedule, 2, 0);
                        container.revalidate();
                        container.repaint();
                        MakeSchedule = makeSchedule;
                    } else {
                        container.add(makeSchedule, 2, 0);
                        container.revalidate();
                        container.repaint();
                        MakeSchedule = makeSchedule;
                    }
                } else { // show the reset panel
                    JPanel reset = resetPanel(container);
                    if (Reset != null) {
                        container.remove(Reset);
                        container.add(reset, 2, 0);
                        container.revalidate();
                        container.repaint();
                        Reset = reset;
                    } else {
                        container.add(reset, 2, 0);
                        container.revalidate();
                        container.repaint();
                        Reset = reset;
                    }
                }
            }
        });
        create.addActionListener(new ActionListener() { // create new txt file with user data
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.showSaveDialog(null);
                try {
                    String path = chooser.getSelectedFile().getAbsolutePath();
                    File user_data = new File(path + ".txt");
                    try {
                        user_data.createNewFile(); // create the new file
                    } catch (IOException err) {
                    }
                    dataDir = user_data.getAbsolutePath(); // update user data directory
                } catch (NullPointerException err) {
                }
            }
        });
        saveas.addActionListener(new ActionListener() { // save txt file with user data with new name or path
            @Override
            public void actionPerformed(ActionEvent e) {
                String olddir = getdataDir(); // the current dir soon to be the old one
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.showSaveDialog(null);
                try {
                    // create the new file
                    String path = chooser.getSelectedFile().getAbsolutePath();
                    File user_data = new File(path + ".txt");
                    try {
                        user_data.createNewFile();
                    } catch (IOException err) {
                    }
                    dataDir = user_data.getAbsolutePath(); // update the user data directory

                    try { // copy the txt from old txt into new save as txt

                        BufferedReader reader = new BufferedReader(new FileReader(olddir)); //from old file
                        BufferedWriter writer = new BufferedWriter(new FileWriter(dataDir)); //to new file
                        String line; // the current line

                        while ((line = reader.readLine()) != null) {
                            writer.write(line); // write the current line
                            writer.newLine();
                        }
                        reader.close();
                        writer.close();
                    } catch (IOException err) {
                        err.printStackTrace();
                    }
                } catch (NullPointerException err) {
                }
            }
        });
        return pop;
    }

    /**
     * Creates the reset panel
     */
    public JPanel resetPanel(JLayeredPane container) {
        // create the reset JPanel with prompt for user to confirm reset
        JPanel reset = new JPanel(null);
        reset.setBounds(250, 150, 400, 200);
        reset.setBorder(BorderFactory.createRaisedBevelBorder());

        // create the ok button
        JButton ok = new JButton("Ok");
        ok.setBounds(250, 130, 60, 30);
        ok.setBackground(new Color(255, 165, 79));
        ok.setBorder(BorderFactory.createRaisedBevelBorder());
        reset.add(ok);
        // create the cancel button
        JButton cancel = new JButton("Cancel");
        cancel.setBounds(95, 130, 60, 30);
        cancel.setBorder(BorderFactory.createRaisedBevelBorder());
        cancel.setBackground(new Color(255, 165, 79));
        reset.add(cancel);
        // instructional text
        String s;
        s = "<html><h2 align='center'>Current Schedule data will be deleted</h2>";
        s += "<html><h3 align='center'>Do you wish to continue?</h3>";
        // the text the JLabel
        JLabel words = new JLabel(s);
        words.setBounds(40, -80, 400, 300);
        reset.add(words);

        cancel.addActionListener(new ActionListener() { // remove reset panel
            @Override
            public void actionPerformed(ActionEvent e) {
                container.remove(reset);
                container.revalidate();
                container.repaint();
            }
        });
        ok.addActionListener(new ActionListener() { // reset data and remove reset panel
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new PrintWriter(dataDir).close();
                } catch (IOException err) {
                } catch (NullPointerException err) {
                }
                container.remove(reset);
                container.revalidate();
                container.repaint();
            }
        });
        changeHoverColor(ok); // change ok color on hover
        changeHoverColor(cancel); // change cancel color on hover
        return reset;
    }

    /**
     * Creates the calendar to display in container
     */
    public JPanel getCalendar(String dateString, JLayeredPane container, JLabel bg) {
        // create the calendar box
        Cal = new JPanel(new BorderLayout());
        Cal.setBorder(null);
        Cal.setOpaque(false);
        Cal.setBounds(0, 0, 900, 574);

        // create the date sub panel on top of actual calendar
        JPanel date = new JPanel();
        date.setOpaque(false);

        JLabel text = new JLabel(
                "<html><p align='center'><font size='6'>" + dateString + "</font></p>");
        text.setBorder(null);

        // create previous month button
        JButton prev = new JButton("<<");
        prev.setBorder(null);
        prev.setBackground(new Color(255, 177, 81));
        prev.setPreferredSize(new Dimension(30, 20));
        prev.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // create next month button
        JButton next = new JButton(">>");
        next.setBorder(null);
        next.setBackground(new Color(255, 177, 81));
        next.setPreferredSize(new Dimension(30, 20));
        next.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // add the date text and buttons to date sub panel
        date.add(prev);
        date.add(text);
        date.add(next);

        dateCurr = dateString; // keep invariant

        // create the actual calendar
        try {
            JPanel calendar = new MonthCalendar(dateString, this, container).monthCalendar;
            calendar.setOpaque(false);
            Cal.add(date, BorderLayout.NORTH);
            Cal.add(calendar, BorderLayout.SOUTH);
        } catch (NullPointerException e) {
            getCalendar(getTodayDate(0, container, bg), container, bg); // show today's calendar
        }

        changeHoverColor(prev); // change prev color on hover
        changeHoverColor(next); // change next color on hover

        prev.addActionListener(new ActionListener() { // display last month's date and calendar
            @Override
            public void actionPerformed(ActionEvent e) {
                String thedate = getTodayDate(-30, container, bg);
                container.remove(Cal);
                container.add(getCalendar(thedate, container, bg), 1, 0);
                container.revalidate();
                container.repaint();
            }
        });
        next.addActionListener(new ActionListener() { // display next month's date and calendar
            @Override
            public void actionPerformed(ActionEvent e) {
                String thedate = getTodayDate(30, container, bg);
                container.remove(Cal);
                container.add(getCalendar(thedate, container, bg), 1, 0);
                container.revalidate();
                container.repaint();
            }
        });
        return Cal;
    }

    /**
     * Gets date and maintain season and dayShift invariants from Meeti class
     */
    public String getTodayDate(int num, JLayeredPane container, JLabel bg) {

        Meeti.dayShift += num; // keep invariant to date moved to
        // get the date
        Date now = new Date();
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(now);
        if (num == 0) { // always open new JFrame with current date not date of previous JFrame
            c.add(java.util.Calendar.DAY_OF_YEAR, num);
        } else {
            c.add(java.util.Calendar.DAY_OF_YEAR, Meeti.dayShift);
        }

        // refine date into a friendly string
        int day = c.get(java.util.Calendar.DAY_OF_WEEK);
        int month = c.get(java.util.Calendar.MONTH);
        int year = c.get(java.util.Calendar.YEAR);
        int date = c.get(java.util.Calendar.DATE);

        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        String[] months = {"January", "February", "March", "April", "May", "June", "July",
                "August", "September", "October", "November", "December"};

        String thedate = days[day - 1] + ", ";
        thedate += months[month] + " ";
        thedate += date + " ";
        thedate += year;

        // set the season
        String season_now = Meeti.season;
        if (month + 1 == 12 || (month + 1 > 0 && month + 1 < 3)) {
            Meeti.season = "winter";
        } else if (month + 1 > 2 && month + 1 < 6) {
            Meeti.season = "spring";
        } else if (month + 1 > 5 && month + 1 < 9) {
            Meeti.season = "summer";
        } else if (month + 1 > 8 && month + 1 < 12) {
            Meeti.season = "fall";
        }

        if (season_now != null && !season_now.equals(Meeti.season)) {
            bg.setIcon(new ImageIcon(this.getClass().
                    getResource("../images/" + Meeti.season + ".jpg")));

            container.revalidate();
            container.repaint();
        }
        dateCurr = thedate; // keep invariant
        return thedate;
    }

    /**
     * Gets all matches for user event search in user data txt
     */
    public JPanel getEvent(String userInput, String dir, JLayeredPane container, JLabel bg) {

        if (dir == null) {
            return Schedules.makeAschedule(); // can't search with out file
        } else {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(dir));
                String line; // the current line
                String dates = "";

                // get all event's date that matched users inquiry
                while ((line = reader.readLine()) != null) {
                    if (line.split("@~###~@")[1].trim().toLowerCase().
                            contains(userInput.trim().toLowerCase()) &&
                            userInput.trim().length() != 0) {

                        dates += line.split("@~###~@")[0] + "/n";
                    }
                }
                return results(dates, container, bg);

            } catch (IOException e) {
            } catch (NullPointerException e) {
            } catch (IndexOutOfBoundsException e) {
            }
            return null;
        }
    }

    /**
     * Returns JPanel with the search results
     */
    public JPanel results(String dates, JLayeredPane container, JLabel bg) {
        // create results panel
        JPanel results = new JPanel(null);
        String num;
        if (dates.length() == 0) { // no results
            num = "0";
        } else {
            num = dates.split("/n").length + "";
        }
        // display number of results found
        JLabel title = new JLabel("<html><h2 align='center'>" + num + " Results found" + "</h2>", SwingConstants.CENTER);
        title.setBounds(10, 10, 400, 20);
        title.setFont(new Font("Arial", Font.PLAIN, 18));
        // creates exit button
        JButton exit = new JButton("Close");
        exit.setBackground(new Color(255, 165, 79));
        exit.setBounds(170, 355, 60, 30);
        exit.setBorder(BorderFactory.createRaisedBevelBorder());
        changeHoverColor(exit); // change exit button color on hover

        exit.addActionListener(new ActionListener() { // exit results panel on click
            @Override
            public void actionPerformed(ActionEvent e) {
                container.remove(results);
                container.revalidate();
                container.repaint();
            }
        });
        results.add(exit);

        // create scroll pane for all the results
        JScrollPane links = new JScrollPane();
        JPanel items = new JPanel(null);
        items.setBackground(Color.WHITE);
        links.setBounds(50, 40, 300, 300);
        links.setViewportView(items);

        // add components
        results.add(links);
        results.add(title);
        results.setBounds(250, 87, 400, 400);
        results.setBorder(BorderFactory.createRaisedBevelBorder());

        String[] dateList = dates.split("/n"); // all the results
        int y = 0;// first results y coordinate
        for (String s : dateList) {
            // button displaying result
            JButton link = new JButton(s);
            link.setBounds(0, y, 300, 25);
            y += 26; // move next result button down
            link.setBackground(new Color(255, 195, 105));
            link.setBorder(null);

            link.addMouseListener(new MouseAdapter() { // change link button color on hover
                @Override
                public void mouseEntered(MouseEvent e) {
                    link.setBackground(new Color(255, 165, 79));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    link.setBackground(new Color(255, 195, 105));
                }
            });

            link.setCursor(new Cursor(Cursor.HAND_CURSOR));
            link.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) { // refresh calendar to clicked date button
                    String userIquiry = Search.getCleanDate(s, dateCurr);
                    dateCurr = userIquiry;
                    container.remove(Cal);
                    bg.setIcon(new ImageIcon(this.getClass().getResource("../images/" + Meeti.season + ".jpg")));
                    container.add(getCalendar(userIquiry, container, bg), 1, 0);
                    container.revalidate();
                    container.repaint();
                    results.setVisible(false);
                }
            });
            items.add(link); // all link button to the panel
        }
        return results;
    }
}