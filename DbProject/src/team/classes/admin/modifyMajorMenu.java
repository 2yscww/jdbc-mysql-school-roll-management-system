package team.classes.admin;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;

import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.sql.*;

public class modifyMajorMenu extends JFrame {

    private DefaultTableModel tableModel;
    private JTable majorTable;

    public modifyMajorMenu(adminMenu parentFrame, Connection connection) {
        setTitle("专业信息管理");
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
        tableModel = new DefaultTableModel(new Object[][] {}, new Object[] { "专业代码", "专业名称", "所属院系代码" });
        majorTable = new JTable(tableModel);

        // 添加表格到滚动窗格
        JScrollPane scrollPane = new JScrollPane(majorTable);

        // 添加滚动窗格到窗口
        add(scrollPane);

        // 创建按钮
        JButton addMajorButton = new JButton("增加");
        JButton deleteMajorButton = new JButton("删除");
        JButton modifyMajorButton = new JButton("修改");

        // 创建一个面板用于放置按钮
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addMajorButton);
        buttonPanel.add(deleteMajorButton);
        buttonPanel.add(modifyMajorButton);

        // 将按钮面板添加到窗口底部
        add(buttonPanel, BorderLayout.SOUTH);

        // <---------------------------按钮监听器---------------------------------->

        // 增加按钮
        addMajorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // 创建面板来容纳文本框
                JPanel addMajorpanel = new JPanel();
                addMajorpanel.setLayout(new GridLayout(3, 2));

                // 创建文本框
                JTextField majorCodeField = new JTextField();
                JTextField majorNameField = new JTextField();
                JTextField departCodeField = new JTextField();

                // 将文本框添加到面板
                addMajorpanel.add(new JLabel("专业代码： "));
                addMajorpanel.add(majorCodeField);
                addMajorpanel.add(new JLabel("专业名称: "));
                addMajorpanel.add(majorNameField);
                addMajorpanel.add(new JLabel("所属院系代码： "));
                addMajorpanel.add(departCodeField);

                // 弹出消息框
                int result = JOptionPane.showConfirmDialog(null, addMajorpanel, "请输入专业信息",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                // 如果用户点击了OK按钮
                if (result == JOptionPane.OK_OPTION) {
                    // 获取用户输入的内容
                    String majorCode = majorCodeField.getText();
                    String majorName = majorNameField.getText();
                    String departCode = departCodeField.getText();

                    // 检查输入是否为空
                    if (majorCode.isEmpty() || majorName.isEmpty() || departCode.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "院系代码和院系名称不能为空");
                        return; // 不执行修改数据库的操作
                    }

                    String addMajorSql = "INSERT INTO major(mno,mname,dno) VALUES(?,?,?);";

                    try {
                        PreparedStatement addMajorSqlex = connection.prepareStatement(addMajorSql);
                        // insertDepartSqlex.setString(1, departmentCode);
                        // insertDepartSqlex.setString(2, departmentName);

                        addMajorSqlex.setString(1, majorCode);
                        addMajorSqlex.setString(2, majorName);
                        addMajorSqlex.setString(3, departCode);

                        // 执行更新
                        int rowsAffected = addMajorSqlex.executeUpdate();

                        if (rowsAffected > 0) {

                            JOptionPane.showMessageDialog(null, "成功插入数据到数据库");
                            refreshViewMajorTable(connection);
                        } else {

                            JOptionPane.showMessageDialog(null, "插入数据到数据库失败");
                        }
                    } catch (SQLException e1) {

                        e1.printStackTrace();
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "取消操作");
                }

            }
        });

        // 删除按钮
        deleteMajorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // 获取选中的行
                int selectedRow = majorTable.getSelectedRow();

                // 检查是否有选中的行
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "请先选中要删除的行");
                    return;
                }

                // 获取选中行的数据
                String majorCode = (String) tableModel.getValueAt(selectedRow, 0);

                // 弹出确认对话框
                int result = JOptionPane.showConfirmDialog(null, "确定删除选中的专业信息吗？", "确认删除",
                        JOptionPane.YES_NO_OPTION);

                // 如果用户确认删除
                if (result == JOptionPane.YES_OPTION) {
                    // 执行删除操作
                    String deleteMajorSql = "DELETE FROM major WHERE mno = ?";

                    try {
                        PreparedStatement deleteMajorSqlex = connection.prepareStatement(deleteMajorSql);

                        // 设置参数
                        deleteMajorSqlex.setString(1, majorCode);

                        // 执行更新
                        int rowsAffected = deleteMajorSqlex.executeUpdate();

                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(null, "成功删除选中的专业信息");
                            // 从表格模型中移除选中的行数据
                            tableModel.removeRow(selectedRow);
                            refreshViewMajorTable(connection);
                        } else {
                            JOptionPane.showMessageDialog(null, "删除专业信息失败");
                        }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "取消操作");
                }

            }
        });

        // 修改按钮
        modifyMajorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 创建一个面板来容纳文本框
                JPanel modifyMajorpanel = new JPanel();
                modifyMajorpanel.setLayout(new GridLayout(3, 2));

                // 创建文本框
                JTextField majorCodeField = new JTextField();
                JTextField majorNameField = new JTextField();
                JTextField departCodeField = new JTextField();

                // 获取选中的行
                int selectedRow = majorTable.getSelectedRow();
                String originalMajorCode = (String) tableModel.getValueAt(selectedRow, 0);

                // 从选中行获取当前值
                String currentMajorCode = (String) tableModel.getValueAt(selectedRow, 0);
                String currentMajorName = (String) tableModel.getValueAt(selectedRow, 1);
                String currentDepartCode = (String) tableModel.getValueAt(selectedRow, 2);

                // 设置文本框的初始值为当前值
                majorCodeField.setText(currentMajorCode);
                majorNameField.setText(currentMajorName);
                departCodeField.setText(currentDepartCode);

                // 将文本框添加到面板
                modifyMajorpanel.add(new JLabel("专业代码： "));
                modifyMajorpanel.add(majorCodeField);
                modifyMajorpanel.add(new JLabel("专业名称: "));
                modifyMajorpanel.add(majorNameField);
                modifyMajorpanel.add(new JLabel("所属院系代码： "));
                modifyMajorpanel.add(departCodeField);

                // 检查是否有选中的行
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "请先选中要修改的行");
                    return;
                }

                // 弹出消息框
                int result = JOptionPane.showConfirmDialog(null, modifyMajorpanel, "请输入专业信息",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                // 如果用户点击了OK按钮
                if (result == JOptionPane.OK_OPTION) {
                    // 获取用户输入的内容
                    String majorCode = majorCodeField.getText();
                    String majorName = majorNameField.getText();
                    String departCode = departCodeField.getText();

                    // 检查输入是否为空
                    if (majorCode.isEmpty() || majorName.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "专业代码和专业名称不能为空");
                        return; // 不执行更新表的操作
                    }

                    String updateMajorSql = "UPDATE major SET mno = ? , mname = ? , dno = ? WHERE mno = ?";

                    try {
                        PreparedStatement updateMajorSqlex = connection.prepareStatement(updateMajorSql);
                        updateMajorSqlex.setString(1, majorCode);
                        updateMajorSqlex.setString(2, majorName);
                        updateMajorSqlex.setString(3, departCode);
                        updateMajorSqlex.setString(4, originalMajorCode);

                        // 执行更新
                        int rowsAffected = updateMajorSqlex.executeUpdate();

                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(null, "成功修改数据");
                            refreshViewMajorTable(connection);
                        } else {
                            JOptionPane.showMessageDialog(null, "修改数据失败");
                        }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "取消操作");
                }
            }
        });

        // <------------------------------------------------------------------------------------>

        // 显示专业信息
        String viewMajorsql = "SELECT * FROM major";

        try (Statement statement = connection.createStatement()) {
            ResultSet viewMajorsqlex = statement.executeQuery(viewMajorsql);

            // 清空表格模型
            tableModel.setRowCount(0);

            // 遍历查询结果，将数据添加到表格模型中
            while (viewMajorsqlex.next()) {
                String mno = viewMajorsqlex.getString("mno");
                String mname = viewMajorsqlex.getString("mname");
                String dno = viewMajorsqlex.getString("dno");

                // 将数据添加到表格模型
                tableModel.addRow(new Object[] { mno, mname, dno });

            }

        } catch (SQLException e1) {

            e1.printStackTrace();
        }

        /*
         * 设置当窗口关闭时执行 DISPOSE_ON_CLOSE 操作
         * 释放窗口资源而不是终止程序
         */

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null); // 将窗体显示在屏幕中央
        setVisible(true); // 显示当前窗口

    }

    private void refreshViewMajorTable(Connection connection) {
        // 显示专业信息
        String viewMajorsql = "SELECT * FROM major";

        try (Statement statement = connection.createStatement()) {
            ResultSet viewMajorsqlex = statement.executeQuery(viewMajorsql);

            // 清空表格模型
            tableModel.setRowCount(0);

            // 遍历查询结果，将数据添加到表格模型中
            while (viewMajorsqlex.next()) {
                String mno = viewMajorsqlex.getString("mno");
                String mname = viewMajorsqlex.getString("mname");
                String dno = viewMajorsqlex.getString("dno");

                // 将数据添加到表格模型
                tableModel.addRow(new Object[] { mno, mname, dno });

            }

        } catch (SQLException e1) {

            e1.printStackTrace();
        }
    }

}
