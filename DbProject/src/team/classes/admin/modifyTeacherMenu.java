package team.classes.admin;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;

import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.sql.*;

public class modifyTeacherMenu extends JFrame {

    private DefaultTableModel tableModel;
    private JTable teacherTable;

    public modifyTeacherMenu(adminMenu parentFrame, Connection connection) {
        setTitle("教师信息管理");
        setSize(800, 500);

        // 添加窗口监听器，捕捉窗口关闭事件
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // 显示父窗口
                parentFrame.setVisible(true);
            }
        });

        // 隐藏父窗口
        parentFrame.setVisible(false);

        // 创建表格模型和表格组件
        tableModel = new DefaultTableModel(new Object[][] {}, new Object[] { "教师编号", "教师姓名", "教师职称" });
        teacherTable = new JTable(tableModel);

        // 添加表格到滚动窗格
        JScrollPane scrollPane = new JScrollPane(teacherTable);

        // 添加滚动窗格到窗口
        add(scrollPane);

        // 创建按钮
        JButton addTeacherButton = new JButton("增加");
        JButton deleteTeacherButton = new JButton("删除");
        JButton modifyTeacherButton = new JButton("修改");

        // 创建一个面板用于放置按钮
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addTeacherButton);
        buttonPanel.add(deleteTeacherButton);
        buttonPanel.add(modifyTeacherButton);

        // 将按钮面板添加到窗口底部
        add(buttonPanel, BorderLayout.SOUTH);

        // <---------------------------按钮监听器---------------------------------->

        // 增加按钮
        addTeacherButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                // 创建面板来容纳文本框
                JPanel addTeacherPanel = new JPanel();
                addTeacherPanel.setLayout(new GridLayout(3, 2));

                // "教师编号", "教师姓名", "教师职称"

                // 创建文本框
                JTextField teacherCodeField = new JTextField();
                JTextField teacherNameField = new JTextField();
                JTextField teacherTitleField = new JTextField();

                // 将文本框添加到面板
                addTeacherPanel.add(new JLabel("教师编号： "));
                addTeacherPanel.add(teacherCodeField);
                addTeacherPanel.add(new JLabel("教师姓名: "));
                addTeacherPanel.add(teacherNameField);
                addTeacherPanel.add(new JLabel("教师职称: "));
                addTeacherPanel.add(teacherTitleField);

                // 弹出消息框
                int result = JOptionPane.showConfirmDialog(null, addTeacherPanel, "请输入教师信息",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                // 如果用户点击了OK按钮
                if (result == JOptionPane.OK_OPTION) {
                    // 获取用户输入的内容
                    String teacherCode = teacherCodeField.getText();
                    String teacherName = teacherNameField.getText();
                    String teacherTitle = teacherTitleField.getText();

                    // 检查输入是否为空
                    if (teacherCode.isEmpty() || teacherName.isEmpty() || teacherTitle.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "输入框都不能为空");
                        return; // 不执行修改数据库的操作
                    }

                    String addTeacherSql = " INSERT INTO teacher(tno,tname,ttitle) VALUES(?,?,?)";

                    try {
                        PreparedStatement addTeacherSqlex = connection.prepareStatement(addTeacherSql);
                        // insertDepartSqlex.setString(1, departmentCode);
                        // insertDepartSqlex.setString(2, departmentName);

                        addTeacherSqlex.setString(1, teacherCode);
                        addTeacherSqlex.setString(2, teacherName);
                        addTeacherSqlex.setString(3, teacherTitle);

                        // 执行更新
                        int rowsAffected = addTeacherSqlex.executeUpdate();

                        if (rowsAffected > 0) {

                            JOptionPane.showMessageDialog(null, "成功插入数据到数据库");
                            refreshViewTeacherTable(connection);
                            // refreshViewTeacherTable(connection);
                        } else {

                            JOptionPane.showMessageDialog(null, "插入数据到数据库失败");
                        }
                    } catch (SQLException e1) {

                        e1.printStackTrace();
                    }

                } else {
                    
                }

            }

        });

        // 删除按钮
        deleteTeacherButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // 获取选中的行
                int selectedRow = teacherTable.getSelectedRow();

                // 检查是否有选中的行
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "请先选中要删除的行");
                    return;
                }

                // 获取选中行的数据
                String teacherCode = (String) tableModel.getValueAt(selectedRow, 0);

                // 弹出确认对话框
                int result = JOptionPane.showConfirmDialog(null, "确定删除选中的课程信息吗？", "确认删除",
                        JOptionPane.YES_NO_OPTION);

                // 如果用户确认删除
                if (result == JOptionPane.YES_OPTION) {
                    // 执行删除操作
                    String deleteTeacherSql = "DELETE FROM Teacher where tno = ?";

                    try {
                        PreparedStatement deleteTeacherSqlex = connection.prepareStatement(deleteTeacherSql);

                        // 设置参数
                        deleteTeacherSqlex.setString(1, teacherCode);

                        // 执行更新
                        int rowsAffected = deleteTeacherSqlex.executeUpdate();

                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(null, "成功删除选中的教师信息");
                            // 从表格模型中移除选中的行数据
                            tableModel.removeRow(selectedRow);
                            refreshViewTeacherTable(connection);
                        } else {
                            JOptionPane.showMessageDialog(null, "删除教师信息失败");
                        }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    
                }
            }

        });

        // 修改按钮
        modifyTeacherButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // 创建一个面板来容纳文本框
                JPanel modifyTeacherPanel = new JPanel();
                modifyTeacherPanel.setLayout(new GridLayout(3, 2));

                // 创建文本框
                JTextField teacherCodeField = new JTextField();
                JTextField teacherNameField = new JTextField();
                JTextField teacherTitleField = new JTextField();

                // 获取选中的行
                int selectedRow = teacherTable.getSelectedRow();
                String originalTeacherCode = (String) tableModel.getValueAt(selectedRow, 0);

                // 从选中行获取当前值
                String currentTeacherCode = (String) tableModel.getValueAt(selectedRow, 0);
                String currentTeacherName = (String) tableModel.getValueAt(selectedRow, 1);
                String currentTeacherTitle = (String) tableModel.getValueAt(selectedRow, 2);

                // 设置文本框的初始值为当前值
                teacherCodeField.setText(currentTeacherCode);
                teacherNameField.setText(currentTeacherName);
                teacherTitleField.setText(currentTeacherTitle);

                // 将文本框添加到面板
                modifyTeacherPanel.add(new JLabel("教师编号： "));
                modifyTeacherPanel.add(teacherCodeField);
                modifyTeacherPanel.add(new JLabel("教师姓名: "));
                modifyTeacherPanel.add(teacherNameField);
                modifyTeacherPanel.add(new JLabel("教师职称: "));
                modifyTeacherPanel.add(teacherTitleField);

                // 检查是否有选中的行
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "请先选中要修改的行");
                    return;
                }

                // 弹出消息框
                int result = JOptionPane.showConfirmDialog(null, modifyTeacherPanel, "请输入教师信息",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                // 如果用户点击了OK按钮
                if (result == JOptionPane.OK_OPTION) {
                    // 获取用户输入的内容
                    String TeacherCode = teacherCodeField.getText();
                    String TeacherName = teacherNameField.getText();
                    String teacherTitle = teacherTitleField.getText();

                    // 检查输入是否为空
                    if (TeacherCode.isEmpty() || TeacherName.isEmpty() || teacherTitle.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "输入框都不能为空");
                        return; // 不执行更新表的操作
                    }

                    String updateTeacherSql = "UPDATE teacher SET tno = ? , tname = ? , ttitle = ? WHERE tno = ?";

                    try {
                        PreparedStatement updateTeacherSqlex = connection.prepareStatement(updateTeacherSql);
                        updateTeacherSqlex.setString(1, TeacherCode);
                        updateTeacherSqlex.setString(2, TeacherName);
                        updateTeacherSqlex.setString(3, teacherTitle);
                        updateTeacherSqlex.setString(4, originalTeacherCode);

                        // 执行更新
                        int rowsAffected = updateTeacherSqlex.executeUpdate();

                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(null, "成功修改数据");
                            refreshViewTeacherTable(connection);
                        } else {
                            JOptionPane.showMessageDialog(null, "修改数据失败");
                        }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }

                } else {
                    
                }
            }
        });

        // <-------------------------------------------------------------->

        // 显示教师信息
        String viewTeacherSql = "SELECT * FROM teacher";

        try (Statement statement = connection.createStatement()) {
            ResultSet viewTeacherSqlex = statement.executeQuery(viewTeacherSql);

            // 清空表格模型
            tableModel.setRowCount(0);

            // 遍历查询结果，将数据添加到表格模型中
            while (viewTeacherSqlex.next()) {
                String tno = viewTeacherSqlex.getString("tno");
                String tname = viewTeacherSqlex.getString("tname");
                String ttitle = viewTeacherSqlex.getString("ttitle");

                // 将数据添加到表格模型
                tableModel.addRow(new Object[] { tno, tname, ttitle });

            }

        } catch (SQLException e1) {

            e1.printStackTrace();
        }

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null); // 将窗体显示在屏幕中央
        setVisible(true); // 显示当前窗口
    }

    private void refreshViewTeacherTable(Connection connection) {
        // 显示教师信息
        String viewTeacherSql = "SELECT * FROM teacher";

        try (Statement statement = connection.createStatement()) {
            ResultSet viewTeacherSqlex = statement.executeQuery(viewTeacherSql);

            // 清空表格模型
            tableModel.setRowCount(0);

            // 遍历查询结果，将数据添加到表格模型中
            while (viewTeacherSqlex.next()) {
                String tno = viewTeacherSqlex.getString("tno");
                String tname = viewTeacherSqlex.getString("tname");
                String ttitle = viewTeacherSqlex.getString("ttitle");

                // 将数据添加到表格模型
                tableModel.addRow(new Object[] { tno, tname, ttitle });

            }

        } catch (SQLException e1) {

            e1.printStackTrace();
        }
    }

}
