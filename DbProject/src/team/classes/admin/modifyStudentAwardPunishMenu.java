package team.classes.admin;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

//modifyStudentAwardPunishMenu

public class modifyStudentAwardPunishMenu extends JFrame {
    private DefaultTableModel tableModel;
    private JTable awardPunishTable;

    public modifyStudentAwardPunishMenu(adminMenu parentFrame, Connection connection) {
        setTitle("学生奖惩信息管理");
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
        tableModel = new DefaultTableModel(new Object[][] {}, new Object[] { "奖惩编号", "学号", "奖惩等级", "奖惩方案", "下达日期" });
        awardPunishTable = new JTable(tableModel);

        // Adding table to scroll pane
        JScrollPane scrollPane = new JScrollPane(awardPunishTable);

        // Adding scroll pane to the window
        add(scrollPane);

        // Creating buttons
        JButton addAwardPunishButton = new JButton("增加");
        JButton deleteAwardPunishButton = new JButton("删除");
        JButton modifyAwardPunishButton = new JButton("修改");

        // Creating a panel to place buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addAwardPunishButton);
        buttonPanel.add(deleteAwardPunishButton);
        buttonPanel.add(modifyAwardPunishButton);

        // Adding button panel to the bottom of the window
        add(buttonPanel, BorderLayout.SOUTH);

        // <---------------------------Button
        // Listeners---------------------------------->

        // Add button
        // Add button
        addAwardPunishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a panel to contain text fields
                JPanel addAwardPunishPanel = new JPanel();
                addAwardPunishPanel.setLayout(new GridLayout(5, 2));

                // Create text fields
                JTextField apnoField = new JTextField();
                JTextField asnoField = new JTextField();
                JTextField aplevelField = new JTextField();
                JTextField aprojectField = new JTextField();
                JTextField adateField = new JTextField();

                // Add text fields to the panel
                addAwardPunishPanel.add(new JLabel("奖惩编号: "));
                addAwardPunishPanel.add(apnoField);
                addAwardPunishPanel.add(new JLabel("学号: "));
                addAwardPunishPanel.add(asnoField);
                addAwardPunishPanel.add(new JLabel("奖惩等级: "));
                addAwardPunishPanel.add(aplevelField);
                addAwardPunishPanel.add(new JLabel("奖惩方案: "));
                addAwardPunishPanel.add(aprojectField);
                addAwardPunishPanel.add(new JLabel("下达日期: "));
                addAwardPunishPanel.add(adateField);

                // Show message dialog
                int result = JOptionPane.showConfirmDialog(null, addAwardPunishPanel, "请输入奖惩信息",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                // If the user clicks OK
                if (result == JOptionPane.OK_OPTION) {
                    // Get user input
                    String apno = apnoField.getText();
                    String asno = asnoField.getText();
                    String aplevel = aplevelField.getText();
                    String aproject = aprojectField.getText();
                    String adate = adateField.getText();

                    // Check if input is empty
                    if (apno.isEmpty() || asno.isEmpty() || aplevel.isEmpty() || aproject.isEmpty()
                            || adate.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "输入框都不能为空");
                        return; // Do not execute database insertion
                    }

                    // SQL statement for adding award/punish
                    String addAwardPunishSql = "INSERT INTO award_punish(apno, asno, aplevel, aproject, adate) VALUES (?, ?, ?, ?, ?)";

                    try {
                        // Prepare the SQL statement
                        PreparedStatement addAwardPunishStmt = connection.prepareStatement(addAwardPunishSql);

                        // Set parameters
                        addAwardPunishStmt.setString(1, apno);
                        addAwardPunishStmt.setString(2, asno);
                        addAwardPunishStmt.setString(3, aplevel);
                        addAwardPunishStmt.setString(4, aproject);
                        addAwardPunishStmt.setDate(5, Date.valueOf(adate)); // Assuming adate is a string in the format
                                                                            // 'YYYY-MM-DD'

                        // Execute the update//
                        int rowsAffected = addAwardPunishStmt.executeUpdate();

                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(null, "成功插入数据到数据库");
                            refreshViewAwardPunishTable(connection);
                        } else {
                            JOptionPane.showMessageDialog(null, "插入数据到数据库失败");
                        }
                    } catch (SQLException e1) {
                        // Handle SQL exception
                        e1.printStackTrace();
                    }
                } else {
                    
                }
            }
        });

        // Delete button
        deleteAwardPunishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 获取选中的行
                int selectedRow = awardPunishTable.getSelectedRow();

                // 检查是否有选中的行
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "请先选择要删除的行");
                    return;
                }

                // 获取奖惩编号（假设在表格的第一列）
                String apno = (String) tableModel.getValueAt(selectedRow, 0);

                // 弹出确认对话框
                int result = JOptionPane.showConfirmDialog(null, "确定要删除这条奖惩记录吗？", "确认删除",
                        JOptionPane.YES_NO_OPTION);

                // 如果用户确认删除
                if (result == JOptionPane.YES_OPTION) {
                    // 实现从数据库中删除奖惩记录的逻辑
                    String deleteAwardPunishSql = "DELETE FROM award_punish WHERE apno = ?";

                    try (PreparedStatement deleteAwardPunishStatement = connection
                            .prepareStatement(deleteAwardPunishSql)) {
                        deleteAwardPunishStatement.setString(1, apno);

                        // 执行删除操作
                        int rowsAffected = deleteAwardPunishStatement.executeUpdate();

                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(null, "成功删除奖惩记录");
                            // 从表格模型中移除选中的行数据
                            tableModel.removeRow(selectedRow);
                        } else {
                            JOptionPane.showMessageDialog(null, "删除奖惩记录失败");
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        // Modify button
        modifyAwardPunishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 获取选中的行
                int selectedRow = awardPunishTable.getSelectedRow();

                // 检查是否有选中的行
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "请先选择要修改的行");
                    return;
                }

                // 获取奖惩编号
                String originalApno = (String) tableModel.getValueAt(selectedRow, 0);

                // 创建面板来容纳文本框
                JPanel modifyAwardPunishPanel = new JPanel();
                modifyAwardPunishPanel.setLayout(new GridLayout(5, 2));

                // 创建文本框
                JTextField apnoField = new JTextField();
                JTextField asnoField = new JTextField();
                JTextField aplevelField = new JTextField();
                JTextField aprojectField = new JTextField();
                JTextField adateField = new JTextField();

                // 将文本框添加到面板
                modifyAwardPunishPanel.add(new JLabel("奖惩编号: "));
                modifyAwardPunishPanel.add(apnoField);
                modifyAwardPunishPanel.add(new JLabel("学号: "));
                modifyAwardPunishPanel.add(asnoField);
                modifyAwardPunishPanel.add(new JLabel("奖惩等级: "));
                modifyAwardPunishPanel.add(aplevelField);
                modifyAwardPunishPanel.add(new JLabel("奖惩方案: "));
                modifyAwardPunishPanel.add(aprojectField);
                modifyAwardPunishPanel.add(new JLabel("下达日期: "));
                modifyAwardPunishPanel.add(adateField);

                // 设置文本框的初始值为选中行的数据
                apnoField.setText(originalApno);
                asnoField.setText((String) tableModel.getValueAt(selectedRow, 1));
                aplevelField.setText((String) tableModel.getValueAt(selectedRow, 2));
                aprojectField.setText((String) tableModel.getValueAt(selectedRow, 3));
                adateField.setText(tableModel.getValueAt(selectedRow, 4).toString());

                // 弹出消息框
                int result = JOptionPane.showConfirmDialog(null, modifyAwardPunishPanel, "请输入奖惩信息",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                // 如果用户点击了OK按钮
                if (result == JOptionPane.OK_OPTION) {
                    // 获取用户输入的内容
                    String apno = apnoField.getText();
                    String asno = asnoField.getText();
                    String aplevel = aplevelField.getText();
                    String aproject = aprojectField.getText();
                    String adate = adateField.getText(); // 注意：这里可能需要转换为日期类型

                    // 检查输入是否为空
                    if (apno.isEmpty() || asno.isEmpty() || aplevel.isEmpty() || aproject.isEmpty()
                            || adate.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "输入框都不能为空");
                        return; // 不执行更新数据库的操作
                    }

                    // 实现修改奖惩记录的逻辑
                    String updateAwardPunishSql = "UPDATE award_punish SET apno = ?, asno = ?, aplevel = ?, aproject = ?, adate = ? WHERE apno = ?";

                    try (PreparedStatement updateAwardPunishStatement = connection
                            .prepareStatement(updateAwardPunishSql)) {
                        updateAwardPunishStatement.setString(1, apno);
                        updateAwardPunishStatement.setString(2, asno);
                        updateAwardPunishStatement.setString(3, aplevel);
                        updateAwardPunishStatement.setString(4, aproject);
                        updateAwardPunishStatement.setString(5, adate);
                        updateAwardPunishStatement.setString(6, originalApno);

                        // 执行更新
                        int rowsAffected = updateAwardPunishStatement.executeUpdate();

                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(null, "成功修改奖惩记录");
                            refreshViewAwardPunishTable(connection);
                        } else {
                            JOptionPane.showMessageDialog(null, "修改奖惩记录失败");
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    
                }
            }
        });

        // <--------------------------------------------------------------------->

        // Display award/punish information
        String viewAwardPunishSql = "SELECT * FROM award_punish";

        try (Statement statement = connection.createStatement()) {
            ResultSet viewAwardPunishResult = statement.executeQuery(viewAwardPunishSql);

            // Clear table model
            tableModel.setRowCount(0);

            // Iterate through query results and add data to table model
            while (viewAwardPunishResult.next()) {
                String apno = viewAwardPunishResult.getString("apno");
                String asno = viewAwardPunishResult.getString("asno");
                String aplevel = viewAwardPunishResult.getString("aplevel");
                String aproject = viewAwardPunishResult.getString("aproject");
                Date adate = viewAwardPunishResult.getDate("adate");

                // Add data to table model
                tableModel.addRow(new Object[] { apno, asno, aplevel, aproject, adate });
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Release window resources
        setLocationRelativeTo(null); // Display the window in the center of the screen
        setVisible(true); // Show the current window
    }

    private void refreshViewAwardPunishTable(Connection connection) {
        // Refresh award/punish information
        String viewAwardPunishSql = "SELECT * FROM award_punish";

        try (Statement statement = connection.createStatement()) {
            ResultSet viewAwardPunishResultSet = statement.executeQuery(viewAwardPunishSql);

            // Clear table model
            tableModel.setRowCount(0);

            // Iterate through the query results and add data to the table model
            while (viewAwardPunishResultSet.next()) {
                String apno = viewAwardPunishResultSet.getString("apno");
                String asno = viewAwardPunishResultSet.getString("asno");
                String aplevel = viewAwardPunishResultSet.getString("aplevel");
                String aproject = viewAwardPunishResultSet.getString("aproject");
                Date adate = viewAwardPunishResultSet.getDate("adate");

                // Add data to table model
                tableModel.addRow(new Object[] { apno, asno, aplevel, aproject, adate });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
