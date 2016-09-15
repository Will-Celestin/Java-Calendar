package calendar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 * Handles the user set events
 * Created by Will. Celestin on 7/15/2015.
 */

public class Schedules {

    public static String event; // name of event being worked on

    /**
     * Displays a JPanel on user double click on non-empty calendar cell
     */
    public static JPanel displaySchedules(int month, int date, int year, String dir) {
        if (dir == null) {
            return makeAschedule(); // no user data file
        } else {
            return addEvent(month, date, year, dir);
        }
    }

    /**
     * JPanel that prompts user to create or open a schedule file
     */
    public static JPanel makeAschedule() {

        // create the prompt panel
        JPanel makeAschedule = new JPanel(null);
        makeAschedule.setBounds(250, 150, 400, 200);
        makeAschedule.setBorder(BorderFactory.createRaisedBevelBorder());
        // create the ok button
        JButton ok = new JButton("Ok");
        ok.setBounds(175, 130, 60, 30);
        ok.setBackground(new Color(255, 165, 79));
        ok.setBorder(BorderFactory.createRaisedBevelBorder());
        GuiUtils.changeHoverColor(ok); // change ok button color on hover

        ok.addActionListener(new ActionListener() { // exit makeAschedule panel on click
            @Override
            public void actionPerformed(ActionEvent e) {
                makeAschedule.setVisible(false);
            }
        });

        makeAschedule.add(ok);
        // create the prompt text
        String s;
        s = "<html><h2 align='center'>You do not have a schedule opened</h2>";
        s += "<html><h3 align='center'>Create or open one using the menu</h3>";
        // label the panel with the text
        JLabel words = new JLabel(s);
        words.setBounds(40, -80, 400, 300);
        makeAschedule.add(words);
        return makeAschedule;
    }

    /**
     * JPanel that lets user add an event or edit one
     */
    public static JPanel addEvent(int month, int date, int year, String dir) {

        // create the addEvent panel
        JPanel addEvent = new JPanel(null);
        addEvent.setBounds(175, 87, 550, 400);
        addEvent.setBorder(BorderFactory.createRaisedBevelBorder());
        // create the exit button
        JButton exit = new JButton("Close");
        addEvent.add(exit);
        exit.setBounds(120, 355, 60, 30);
        exit.setBackground(new Color(255, 165, 79));
        exit.setBorder(BorderFactory.createRaisedBevelBorder());
        // create the edit button
        JButton editEvent = new JButton("Edit");
        addEvent.add(editEvent);
        editEvent.setBounds(375, 355, 60, 30);
        editEvent.setBackground(new Color(255, 165, 79));
        editEvent.setBorder(BorderFactory.createRaisedBevelBorder());

        exit.addActionListener(new ActionListener() { // exit addEvent panel on click
            @Override
            public void actionPerformed(ActionEvent e) {
                addEvent.setVisible(false);
            }
        });

        GuiUtils.changeHoverColor(exit); // change exit color on hover
        GuiUtils.changeHoverColor(editEvent); // change editEvent color on hover
        // title for dates with events
        String formattedLine =
                "<html><h1>Events for " + month + " / " + date + " / " + year + "</h1>" + "<br/>";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(dir));
            String line; // the current line

            while ((line = reader.readLine()) != null) { // for all the events in user data
                if (!line.equals("")) {
                    if (line.split("@~###~@")[0].equals(
                            month + "/" + (date) + "/" + year)) { // event is for today
                        try {
                            formattedLine += "<html><h2 align='center'>" +
                                    line.split("@~###~@")[1] + "</h2>" + "<br/>";
                        } catch (ArrayIndexOutOfBoundsException err) {
                        }

                    }
                }
            }
            // if there were no events for the date say so
            if (formattedLine.equals("<html><h1>Events for "
                    + month + " / " + date + " / " + year + "</h1>" + "<br/>")) {
                formattedLine = "<html><h1>" + month + " / " + date + " / " + year +
                        " has no events</h1>" + "<br/>";
                formattedLine +=
                        "<html><h2 align='center'>Feel free to add an event</h2>" + "<br/>";
            }
            reader.close();
        } catch (IOException err) {
            err.printStackTrace();
        }

        // displays the events in a JScrollpane
        JScrollPane events = new JScrollPane();
        JPanel wordPanel = new JPanel();
        JLabel words = new JLabel(formattedLine);
        // add components
        wordPanel.setBackground(new Color(255, 255, 255));
        events.setBounds(75, 20, 400, 280);
        events.setViewportView(wordPanel);
        wordPanel.add(words);
        addEvent.add(events);

        // lets user add events in JTextfield
        JTextField search = new JTextField(30);
        search.setBorder(BorderFactory.createEtchedBorder());
        search.setBounds(75, 315, 400, 30);
        // use TextPrompt class to make custom text
        TextPrompt t = new TextPrompt(
                "Add an event here (press enter to save) ... Click edit to edit an event", search);
        t.changeStyle(Font.ITALIC);
        t.setForeground(new Color(205, 102, 0));
        addEvent.add(search);
        // event entered will be added on enter key press
        Action enter_key_pressed = new AbstractAction() { // get user input in add bar on enter key press
            @Override
            public void actionPerformed(ActionEvent e) {
                event = month + "/" + (date) + "/" + year + "@~###~@" + search.getText();
                if (!search.getText().isEmpty()) {
                    try {
                        BufferedWriter writer = new BufferedWriter(
                                new FileWriter(dir, true)); // write the input in txt
                        writer.write(event);
                        writer.newLine();

                        wordPanel.remove(words); // show the user the recently added events
                        // first event added
                        if (words.getText().equals(
                                "<html><h1>" + month + " / " + date + " / " + year + " has no events</h1>" +
                                        "<br/>" + "<html><h2 align='center'>Feel free to add an event</h2>" + "<br/>")) {

                            words.setText(
                                    "<html><h1>Just added for " + month + " / " + date + " / " + year + "</h1>" +
                                            "<br/>" + "<html><h2 align='center'><font color='orange'>" + search.getText() +
                                            "</font></h2>" + "</br>");

                        } else {
                            // just an other event added
                            words.setText(words.getText() + "<html><h2 align='center'><font color='orange'>"
                                    + search.getText() + "</font></h2>" + "</br>");

                        }
                        events.getVerticalScrollBar().setValue(wordPanel.getHeight()); // scroll to the bottom
                        wordPanel.add(words);
                        events.revalidate();
                        events.repaint();
                        writer.close();

                    } catch (IOException err) {
                        err.printStackTrace();
                    }
                }
                search.setText(""); // make the text field blank after renter key pressed
            }
        };
        search.addActionListener(enter_key_pressed);

        // create the edit scroll pane
        JScrollPane edit = new JScrollPane();
        JPanel items = new JPanel(null);
        items.setBackground(Color.WHITE);
        edit.setBounds(75, 20, 400, 320);
        edit.setViewportView(items);
        edit.setVisible(false);
        addEvent.add(edit);
        // create the instruction panel
        JPanel ip = new JPanel();
        ip.setBackground(Color.WHITE);
        JLabel instruction = new JLabel(
                "<html><p align='center'>Edit event(s) in the text field(s)" +
                        " and press enter to save</p>" + "<br/>");
        ip.add(instruction);
        ip.setBounds(0, 0, 400, 30);
        items.add(ip);

        try {
            // display every event for the selected date in editing text fields
            BufferedReader reader = new BufferedReader(new FileReader(dir));
            String line; // the current line
            int i = 0; // number of events
            int y = 30; // y coordinate of first edit text field

            while ((line = reader.readLine()) != null) {
                if (!line.equals("")) {
                    if (line.split("@~###~@")[0].equals(month + "/" + date + "/" + year)) {
                        // every event for the selected date
                        try {
                            String oldline = line.split("@~###~@")[1]; // current line soon to be old if edited
                            // create a text field
                            JTextField txtfld = new JTextField(oldline);
                            txtfld.setBounds(0, y, 400, 25);
                            y += 25; // increment y coordinate for next text field
                            txtfld.setBorder(BorderFactory.createEtchedBorder());

                            Action change = new AbstractAction() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    // read all lines to find the line that was edited on enter key pressed
                                    try {
                                        BufferedReader reader = new BufferedReader(new FileReader(dir));
                                        String theline;
                                        String page = ""; // the whole user data page
                                        while ((theline = reader.readLine()) != null) {
                                            if (theline.contains(oldline)) {
                                                theline = theline.replaceAll(oldline, txtfld.getText());
                                                page += theline + "/n";

                                            } else {
                                                page += theline + "/n";
                                            }
                                        }
                                        reader.close();

                                        // update with the user edit
                                        BufferedWriter writer = new BufferedWriter(new FileWriter(dir));
                                        String[] pageSplit = page.split("/n");
                                        for (String l : pageSplit) {
                                            writer.write(l);
                                            writer.newLine();
                                        }
                                        writer.close();

                                        txtfld.setForeground(new Color(205, 102, 0)); // change color after edit
                                    } catch (IOException err) {
                                    }
                                }
                            };
                            txtfld.addActionListener(change);
                            i++;
                            items.add(txtfld);
                        } catch (ArrayIndexOutOfBoundsException err) {
                        }
                    }
                }
            }
            if (i == 0) { // there were no events to be edited say so
                items.remove(ip); // remove instruction panel
                // create a panel telling user there's no event
                JPanel none = new JPanel();
                none.setBackground(Color.WHITE);
                JLabel labe = new JLabel(
                        "<html><h1 align='center'>" + month + " / " + date + " / " + year +
                                " has no events</h1>" + "<br/>" + "<html><h2 align='center'>Return and " +
                                "add events in the text field</h2>" + "<br/>", SwingConstants.CENTER);
                none.add(labe);
                none.setBounds(30, 0, 340, 150);
                items.add(none);
            }
            reader.close();
        } catch (IOException err) {
            err.printStackTrace();
        }

        editEvent.addActionListener(new ActionListener() { //toggle edit and done buttons
            @Override
            public void actionPerformed(ActionEvent e) {
                if (editEvent.getText().equals("Edit")) {
                    events.setVisible(false);
                    search.setVisible(false);
                    edit.setVisible(true);
                    editEvent.setText("Done");
                } else {
                    events.setVisible(true);
                    search.setVisible(true);
                    edit.setVisible(false);
                    editEvent.setText("Edit");
                }
            }
        });
        return addEvent;
    }
}