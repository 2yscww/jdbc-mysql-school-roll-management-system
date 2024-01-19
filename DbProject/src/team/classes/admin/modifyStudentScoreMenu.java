package team.classes.admin;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class modifyStudentScoreMenu extends JFrame {
    private DefaultTableModel tableModel;
    private JTable scoreTable;

    public modifyStudentScoreMenu(adminMenu parentFrame, Connection connection) {
        setTitle("学生成绩信息管理");
        setSize(800, 500);

        // Adding window listener to capture window closing event
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                parentFrame.setVisible(true); // Show the parent frame
            }
        });

        parentFrame.setVisible(false); // Hide the parent frame

        // Creating table model and table component
        tableModel = new DefaultTableModel(new Object[][] {}, new Object[] { "学号", "课程编号", "成绩" });
        scoreTable = new JTable(tableModel);

        // Adding table to scroll pane
        JScrollPane scrollPane = new JScrollPane(scoreTable);

        // Adding scroll pane to the window
        add(scrollPane);

        // Creating buttons
        JButton addScoreButton = new JButton("增加");
        JButton deleteScoreButton = new JButton("删除");
        JButton modifyScoreButton = new JButton("修改");

        // Creating a panel to place buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addScoreButton);
        buttonPanel.add(deleteScoreButton);
        buttonPanel.add(modifyScoreButton);

        // Adding button panel to the bottom of the window
        add(buttonPanel, BorderLayout.SOUTH);

        // <---------------------------Button Listeners---------------------------------->

        // Add button
        addScoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a panel to contain text fields
                JPanel addScorePanel = new JPanel();
                addScorePanel.setLayout(new GridLayout(3, 2));

                // Create text fields
                JTextField snoField = new JTextField();
                JTextField knoField = new JTextField();
                //JTextField tnoField = new JTextField();
                JTextField gradeField = new JTextField();

                // Add text fields to the panel
                addScorePanel.add(new JLabel("学号: "));
                addScorePanel.add(snoField);
                addScorePanel.add(new JLabel("课程编号: "));
                addScorePanel.add(knoField);
                // addScorePanel.add(new JLabel("教师编号: "));
                // addScorePanel.add(tnoField);
                addScorePanel.add(new JLabel("成绩: "));
                addScorePanel.add(gradeField);

                // Show message dialog
                int result = JOptionPane.showConfirmDialog(null, addScorePanel, "请输入成绩信息",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                // If the user clicks OK
                if (result == JOptionPane.OK_OPTION) {
                    // Get user input
                    String sno = snoField.getText();
                    String kno = knoField.getText();
                    // String tno = tnoField.getText();
                    String grade = gradeField.getText();

                    // Check if input is empty
                    if (sno.isEmpty() || kno.isEmpty() ) {
                        JOptionPane.showMessageDialog(null, "除成绩外都不能为空");
                        return; // Do not execute database insertion
                    }

                    // SQL statement for adding score
                    String addScoreSql = "INSERT INTO grade(sno, kno, grade) VALUES (?, ?, ?)";

                    try {
                        // Prepare the SQL statement
                        PreparedStatement addScoreStmt = connection.prepareStatement(addScoreSql);

                        // Set parameters
                        addScoreStmt.setString(1, sno);
                        addScoreStmt.setString(2, kno);
                        // addScoreStmt.setString(3, tno);
                        addScoreStmt.setInt(3, Integer.parseInt(grade));
                        //addScoreStmt.setByte(3, Byte.parseByte(grade));

                        // Execute the update
                        int rowsAffected = addScoreStmt.executeUpdate();

                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(null, "成功插入数据到数据库");
                            refreshViewScoreTable(connection);
                        } else {
                            JOptionPane.showMessageDialog(null, "插入数据到数据库失败");
                        }
                    } catch (SQLException e1) {
                        // Handle SQL exception
                        e1.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "取消操作");
                }
            }
        });

        // Delete button
        deleteScoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get selected row
                int selectedRow = scoreTable.getSelectedRow();

                // Check if a row is selected
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "请先选择要删除的行");
                    return;
                }

                // Get student number (assuming it is in the first column of the table)
                String sno = (String) tableModel.getValueAt(selectedRow, 0);

                // Show confirmation dialog
                int result = JOptionPane.showConfirmDialog(null, "确定要删除这条成绩记录吗？", "确认删除",
                        JOptionPane.YES_NO_OPTION);

                // If the user confirms deletion
                if (result == JOptionPane.YES_OPTION) {
                    // Implement logic to delete score record from the database
                    String deleteScoreSql = "DELETE FROM grade WHERE sno = ?";

                    try (PreparedStatement deleteScoreStatement = connection.prepareStatement(deleteScoreSql)) {
                        deleteScoreStatement.setString(1, sno);

                        // Execute deletion
                        int rowsAffected = deleteScoreStatement.executeUpdate();

                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(null, "成功删除成绩记录");
                            // Remove selected row data from the table model
                            tableModel.removeRow(selectedRow);
                            refreshViewScoreTable(connection);
                        } else {
                            JOptionPane.showMessageDialog(null, "删除成绩记录失败");
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        // Modify button
        modifyScoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get selected row
                int selectedRow = scoreTable.getSelectedRow();

                // Check if a row is selected
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "请先选择要修改的行");
                    return;
                }

                // Get student number
                String originalSno = (String) tableModel.getValueAt(selectedRow, 0);

                // Create a panel to contain text fields
                JPanel modifyScorePanel = new JPanel();
                modifyScorePanel.setLayout(new GridLayout(3, 2));

                // Create text fields
                JTextField snoField = new JTextField();
                JTextField knoField = new JTextField();
                //JTextField tnoField = new JTextField();
                JTextField gradeField = new JTextField();

                // Add text fields to the panel
                modifyScorePanel.add(new JLabel("学号: "));
                modifyScorePanel.add(snoField);
                modifyScorePanel.add(new JLabel("课程编号: "));
                modifyScorePanel.add(knoField);
                // modifyScorePanel.add(new JLabel("教师编号: "));
                // modifyScorePanel.add(tnoField);
                modifyScorePanel.add(new JLabel("成绩: "));
                modifyScorePanel.add(gradeField);

                // Set initial values for text fields based on selected row data
                snoField.setText(originalSno);
                knoField.setText((String) tableModel.getValueAt(selectedRow, 1));
                //tnoField.setText((String) tableModel.getValueAt(selectedRow, 2));
                gradeField.setText(tableModel.getValueAt(selectedRow, 2).toString());

                // Show message dialog
                int result = JOptionPane.showConfirmDialog(null, modifyScorePanel, "请输入成绩信息",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                // If the user clicks OK
                if (result == JOptionPane.OK_OPTION) {
                    // Get user input
                    String sno = snoField.getText();
                    String kno = knoField.getText();
                    // String tno = tnoField.getText();
                    String grade = gradeField.getText();

                    // Check if input is empty
                    if (sno.isEmpty() || kno.isEmpty()  || grade.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "输入框都不能为空");
                        return; // Do not execute update database operation
                    }

                    // Implement logic to update score record
                    String updateScoreSql = "UPDATE grade SET grade = ? WHERE sno = ? and kno = ?";

                    try (PreparedStatement updateScoreStatement = connection.prepareStatement(updateScoreSql)) {
                        //updateScoreStatement.setString(1, sno);
                        //updateScoreStatement.setString(2, kno);
                        // updateScoreStatement.setString(3, tno);
                        updateScoreStatement.setInt(1, Integer.parseInt(grade));
                        //updateScoreStatement.setByte(3, Byte.parseByte(grade));

                        updateScoreStatement.setString(2, sno);
                        updateScoreStatement.setString(3, kno);

                        // Execute update
                        int rowsAffected = updateScoreStatement.executeUpdate();

                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(null, "成功修改成绩记录");
                            refreshViewScoreTable(connection);
                        } else {
                            JOptionPane.showMessageDialog(null, "修改成绩记录失败");
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "取消操作");
                }
            }
        });

        // <--------------------------------------------------------------------->

        // Display score information
        String viewScoreSql = "SELECT * FROM grade";

        try (Statement statement = connection.createStatement()) {
            ResultSet viewScoreResult = statement.executeQuery(viewScoreSql);

            // Clear table model
            tableModel.setRowCount(0);

            // Iterate through query results and add data to table model
            while (viewScoreResult.next()) {
                String sno = viewScoreResult.getString("sno");
                String kno = viewScoreResult.getString("kno");
                // String tno = viewScoreResult.getString("tno");
                int grade = viewScoreResult.getInt("grade");

                // Add data to table model
                tableModel.addRow(new Object[] { sno, kno,  grade });
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Release window resources
        setLocationRelativeTo(null); // Display the window in the center of the screen
        setVisible(true); // Show the current window
    }

    private void refreshViewScoreTable(Connection connection) {
        // Refresh score information
        String viewScoreSql = "SELECT * FROM grade";

        try (Statement statement = connection.createStatement()) {
            ResultSet viewScoreResultSet = statement.executeQuery(viewScoreSql);

            // Clear table model
            tableModel.setRowCount(0);

            // Iterate through the query results and add data to the table model
            while (viewScoreResultSet.next()) {
                String sno = viewScoreResultSet.getString("sno");
                String kno = viewScoreResultSet.getString("kno");
                // String tno = viewScoreResultSet.getString("tno");
                int grade = viewScoreResultSet.getInt("grade");

                // Add data to table model
                tableModel.addRow(new Object[] { sno, kno, grade });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
