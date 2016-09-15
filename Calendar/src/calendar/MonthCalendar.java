package calendar;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * This class creates a monthly calendar
 * using a customized JTable and a date.
 * Created by Will. Celestin on 7/13/2015.
 */

public class MonthCalendar extends JPanel {

    public int[] splitDate; // the date in a convenient int array
    public JPanel monthCalendar; // JPanel containing the calendar
    public JPanel Schedule; // pop up panel to show user events for date

    /**
     * Constructor: creates a monthly calendar
     */
    public MonthCalendar(String date, GuiUtils guiUtils, JLayeredPane container) {

        // create uneditable table model
        DefaultTableModel tablemodel = new DefaultTableModel() {
            public boolean isCellEditable(int rowIndex, int mColIndex) {
                return false;
            }
        };
        // label the table with days
        String[] headers = {"Sunday", "Monday", "Tuesday",
                "Wednesday", "Thursday", "Friday", "Saturday"};

        for (int i = 0; i < 7; i++) {
            tablemodel.addColumn(headers[i]);
        }
        // set table dimensions
        splitDate = splitDate(date);
        tablemodel.setColumnCount(7);
        tablemodel.setRowCount(6);

        try {
            // calendar object corresponding to date
            GregorianCalendar cal = new GregorianCalendar(splitDate[1], splitDate[0], 1);
            // first day in month i.e. monday,friday
            int month_start = cal.get(GregorianCalendar.DAY_OF_WEEK);
            // number of days in month
            int month_len = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

            // fill model based on mathematical formula
            for (int i = 1; i <= month_len; i++) {
                int row = (i + month_start - 2) / 7;
                int col = (i + month_start - 2) % 7;
                tablemodel.setValueAt(i, row, col);
            }
            // create calendar table
            JTable table = new JTable(tablemodel);
            // style table
            table.setRowHeight(86);
            table.setOpaque(false);
            table.setRowSelectionAllowed(true);
            table.setColumnSelectionAllowed(true);
            table.setFont(new Font("Arial", Font.PLAIN, 22));
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            table.setDefaultRenderer(table.getColumnClass(0), new calendarCellRender());

            // check for double clicks to prompt for event
            table.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {

                        JTable target = (JTable) e.getSource();
                        // location clicked at (cell)
                        int row = target.getSelectedRow();
                        int col = target.getSelectedColumn();

                        if (target.getValueAt(row, col) != null) { // only cells with a number

                            int year = splitDate[1];
                            int month = splitDate[0];
                            int date = ((Integer) target.getValueAt(row, col)).intValue();
                            // display the schedules panel
                            JPanel schedule = Schedules.displaySchedules(
                                    month + 1, date, year, guiUtils.getdataDir());
                            if (Schedule != null) {
                                container.remove(Schedule);
                                container.add(schedule, 2, 0);
                                Schedule = schedule;
                            } else {
                                container.add(schedule, 2, 0);
                                Schedule = schedule;
                            }
                        }
                    }
                }
            });
            // style table header
            JTableHeader header = table.getTableHeader();
            header.setResizingAllowed(false);
            header.setReorderingAllowed(false);
            header.setBackground(new Color(255, 177, 81));
            header.setBorder(BorderFactory.createEtchedBorder());
            // add every component to JPanel
            monthCalendar = new JPanel(new BorderLayout());
            monthCalendar.add(header, BorderLayout.NORTH);
            monthCalendar.add(table, BorderLayout.CENTER);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }

    /**
     * Returns date in convenient int array
     */
    public int[] splitDate(String date) {
        try {
            String[] split = date.split(" ");

            String[] months = {"January", "February", "March", "April", "May", "June",
                    "July", "August", "September", "October", "November", "December"};
            ArrayList<String> MONTHS = new ArrayList<>(12);
            for (String m : months) {
                MONTHS.add(m);
            }

            int year = Integer.parseInt(split[3]);
            int month = MONTHS.indexOf(split[1]);
            int num = Integer.parseInt(split[2]); // day number i.e 1,24,17

            int[] splitDate = {month, year, num};
            return splitDate;
        } catch (NullPointerException e) {
        }
        return new int[0];
    }

    /**
     * Class that renders the calendar table cells
     */
    private class calendarCellRender extends DefaultTableCellRenderer {

        /**
         * Function that renders the calendar table cells
         */
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean selected, boolean focused, int row, int column) {

            super.getTableCellRendererComponent(table, value, selected, focused, row, column);
            setBackground(new Color(255, 162, 74, 0)); // default cell color

            if (value != null) { // change the current date's cell's color
                if (Integer.parseInt(value.toString()) == splitDate[2]) {
                    setBackground(new Color(255, 162, 74, 150));
                }
            }
            if (selected) { // change selected cell color
                setBackground(new Color(255, 162, 74, 150));
                setBorder(null);
            }
            return this;
        }
    }
}