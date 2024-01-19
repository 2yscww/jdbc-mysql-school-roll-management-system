package team.classes.admin;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;

import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.sql.*;

/*
 *  mysql> describe class;
 +---------+-------------+------+-----+---------+-------+
 | Field   | Type        | Null | Key | Default | Extra |
 +---------+-------------+------+-----+---------+-------+
 | cno     | char(8)     | NO   | PRI | NULL    |       |
 | cname   | varchar(40) | NO   |     | NULL    |       |
 | cnumber | smallint    | NO   |     | NULL    |       |
 | mno     | char(8)     | YES  | MUL | NULL    |       |
 +---------+-------------+------+-----+---------+-------+
 4 rows in set (0.00 sec)
*/

public class modifyClassMenu extends JFrame {
    private DefaultTableModel tableModel;
    private JTable classTable;

    public modifyClassMenu(adminMenu parentFrame, Connection connection) {
        setTitle("班级信息管理");
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
        tableModel = new DefaultTableModel(new Object[][] {}, new Object[] { "班级编号", "班级名称", "班级人数", "专业代码" });
        classTable = new JTable(tableModel);

        // 添加表格到滚动窗格
        JScrollPane scrollPane = new JScrollPane(classTable);

        // 添加滚动窗格到窗口
        add(scrollPane);

        // 创建按钮
        JButton addClassButton = new JButton("增加");
        JButton deleteClassButton = new JButton("删除");
        JButton modifyClassButton = new JButton("修改");

        // 创建一个面板用于放置按钮
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addClassButton);
        buttonPanel.add(deleteClassButton);
        buttonPanel.add(modifyClassButton);

        // 将按钮面板添加到窗口底部
        add(buttonPanel, BorderLayout.SOUTH);

        // <---------------------------按钮监听器---------------------------------->

        // 增加按钮
        addClassButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // 创建面板来容纳文本框
                JPanel addClassPanel = new JPanel();
                addClassPanel.setLayout(new GridLayout(4, 2));

                // 创建文本框
                JTextField ClassCodeField = new JTextField();
                JTextField ClassNameField = new JTextField();
                JTextField ClassCnumberField = new JTextField();
                JTextField majorCodeField = new JTextField();

                // "班级编号", "班级名称", "班级人数", "专业代码"

                // 将文本框添加到面板
                addClassPanel.add(new JLabel("班级编号: "));
                addClassPanel.add(ClassCodeField);
                addClassPanel.add(new JLabel("班级名称: "));
                addClassPanel.add(ClassNameField);
                addClassPanel.add(new JLabel("班级人数: "));
                addClassPanel.add(ClassCnumberField);
                addClassPanel.add(new JLabel("专业代码: "));
                addClassPanel.add(majorCodeField);

                // 弹出消息框
                int result = JOptionPane.showConfirmDialog(null, addClassPanel, "请输入班级信息",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                // 如果用户点击了OK按钮
                if (result == JOptionPane.OK_OPTION) {
                    // 获取用户输入的内容
                    String ClassCode = ClassCodeField.getText();
                    String ClassName = ClassNameField.getText();
                    String ClassCnumber = ClassCnumberField.getText();
                    String majorCode = majorCodeField.getText();

                    // 检查输入是否为空
                    if (ClassCode.isEmpty() || ClassName.isEmpty() || ClassCnumber.isEmpty() || majorCode.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "输入框都不能为空");
                        return; // 不执行修改数据库的操作
                    }

                    String addClassSql = "INSERT INTO Class(cno,cname,cnumber,mno) VALUES(?,?,?,?);";

                    try {
                        PreparedStatement addClassSqlex = connection.prepareStatement(addClassSql);
                        // insertDepartSqlex.setString(1, departmentCode);
                        // insertDepartSqlex.setString(2, departmentName);

                        addClassSqlex.setString(1, ClassCode);
                        addClassSqlex.setString(2, ClassName);
                        addClassSqlex.setString(3, ClassCnumber);
                        addClassSqlex.setString(4, majorCode);

                        // 执行更新
                        int rowsAffected = addClassSqlex.executeUpdate();

                        if (rowsAffected > 0) {

                            JOptionPane.showMessageDialog(null, "成功插入数据到数据库");
                            refreshViewClassTable(connection);
                        } else {

                            JOptionPane.showMessageDialog(null, "插入数据到数据库失败");
                        }
                    } catch (SQLException e1) {

                        //TODO 应当完善各个界面的错误处理
                        if (e1.getErrorCode() == 1062) { // MySQL错误码，表示主键冲突
                            JOptionPane.showMessageDialog(null, "主键重复，无法插入重复的数据。", "错误", JOptionPane.ERROR_MESSAGE);
                        } else {
                            // 其他数据库错误处理
                            JOptionPane.showMessageDialog(null, "数据库错误：" + e1.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                        }

                        e1.printStackTrace();
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "取消操作");
                }
            }

        });

        // 删除按钮
        deleteClassButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // 获取选中的行
                int selectedRow = classTable.getSelectedRow();

                // 检查是否有选中的行
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "请先选中要删除的行");
                    return;
                }

                // 获取选中行的数据
                String ClassCode = (String) tableModel.getValueAt(selectedRow, 0);

                // 弹出确认对话框
                int result = JOptionPane.showConfirmDialog(null, "确定删除选中的班级信息吗？", "确认删除",
                        JOptionPane.YES_NO_OPTION);

                // 如果用户确认删除
                if (result == JOptionPane.YES_OPTION) {
                    // 执行删除操作
                    String deleteClassSql = "DELETE FROM class WHERE cno = ?";

                    try {
                        PreparedStatement deleteClassSqlex = connection.prepareStatement(deleteClassSql);

                        // 设置参数
                        deleteClassSqlex.setString(1, ClassCode);

                        // 执行更新
                        int rowsAffected = deleteClassSqlex.executeUpdate();

                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(null, "成功删除选中的班级信息");
                            // 从表格模型中移除选中的行数据
                            tableModel.removeRow(selectedRow);
                            //refreshViewclassTable(connection);
                            refreshViewClassTable(connection);
                        } else {
                            JOptionPane.showMessageDialog(null, "删除课程信息失败");
                        }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "取消操作");
                }

            }

        });

        modifyClassButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                 // 创建一个面板来容纳文本框
                 JPanel modifyClassPanel = new JPanel();
                 modifyClassPanel.setLayout(new GridLayout(4, 2));
 
                 // 创建文本框
                JTextField ClassCodeField = new JTextField();
                JTextField ClassNameField = new JTextField();
                JTextField ClassCnumberField = new JTextField();
                JTextField majorCodeField = new JTextField();

                // "班级编号", "班级名称", "班级人数", "专业代码"
 

                 // 将文本框添加到面板
                 modifyClassPanel.add(new JLabel("班级编号: "));
                 modifyClassPanel.add(ClassCodeField);
                 modifyClassPanel.add(new JLabel("班级名称: "));
                 modifyClassPanel.add(ClassNameField);
                 modifyClassPanel.add(new JLabel("班级人数: "));
                 modifyClassPanel.add(ClassCnumberField);
                 modifyClassPanel.add(new JLabel("专业代码: "));
                  modifyClassPanel.add(majorCodeField);

 
                 // 获取选中的行
                 int selectedRow = classTable.getSelectedRow();
                 String originalClassCode = (String) tableModel.getValueAt(selectedRow, 0);
 
                 // 检查是否有选中的行
                 if (selectedRow == -1) {
                     JOptionPane.showMessageDialog(null, "请先选中要修改的行");
                     return;
                 }

                  // 设置文本框的初始值为选中行的数据
        ClassCodeField.setText(originalClassCode);
        ClassNameField.setText((String) tableModel.getValueAt(selectedRow, 1));
        ClassCnumberField.setText(tableModel.getValueAt(selectedRow, 2).toString());
        majorCodeField.setText((String) tableModel.getValueAt(selectedRow, 3));
 
                 // 弹出消息框
                 int result = JOptionPane.showConfirmDialog(null, modifyClassPanel, "请输入班级信息",
                         JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
 
                 // 如果用户点击了OK按钮
                 if (result == JOptionPane.OK_OPTION) {
                     // 获取用户输入的内容
                     String ClassCode = ClassCodeField.getText();
                     String ClassName = ClassNameField.getText();
                    String ClassCnumber = ClassCnumberField.getText();
                     String majorCode = majorCodeField.getText();


 
 
                     // 检查输入是否为空
                     if (ClassCode.isEmpty() || ClassName.isEmpty() || ClassCnumber.isEmpty() || majorCode.isEmpty()) {
                         JOptionPane.showMessageDialog(null, "输入框都不能为空");
                         return; // 不执行更新表的操作
                     }
 
                     String updateClassSql = "UPDATE class SET cno = ? , cname = ? , cnumber = ?,mno = ? WHERE cno = ?";
 
                     try {
                         PreparedStatement updateClassSqlex = connection.prepareStatement(updateClassSql);
                         updateClassSqlex.setString(1, ClassCode);
                         updateClassSqlex.setString(2, ClassName);
                         updateClassSqlex.setInt(3, Integer.parseInt(ClassCnumber));
                            updateClassSqlex.setString(4, majorCode);
                         updateClassSqlex.setString(5, originalClassCode);
 
                         // 执行更新
                         int rowsAffected = updateClassSqlex.executeUpdate();
 
                         if (rowsAffected > 0) {
 
                             JOptionPane.showMessageDialog(null, "成功修改数据");
                             refreshViewClassTable(connection);
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

        

        // <--------------------------------------------------------------------->

        // 显示班级信息
        String viewClassSql = "SELECT * FROM class";

        try (Statement statement = connection.createStatement()) {
            ResultSet viewClassSqlex = statement.executeQuery(viewClassSql);

            // 清空表格模型
            tableModel.setRowCount(0);

            // 遍历查询结果，将数据添加到表格模型中
            while (viewClassSqlex.next()) {
                String cno = viewClassSqlex.getString("cno");
                String cname = viewClassSqlex.getString("cname");
                int cnumber = viewClassSqlex.getInt("cnumber");
                String mno = viewClassSqlex.getString("mno");

                // 将数据添加到表格模型
                tableModel.addRow(new Object[] { cno, cname, cnumber, mno });

            }

        } catch (SQLException e1) {

            e1.printStackTrace();
        }

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 释放窗口资源

        setLocationRelativeTo(null); // 将窗体显示在屏幕中央
        setVisible(true); // 显示当前窗口

    }

    private void refreshViewClassTable(Connection connection) {
        // 显示班级信息
        String viewClassSql = "SELECT * FROM class";

        try (Statement statement = connection.createStatement()) {
            ResultSet viewClassSqlex = statement.executeQuery(viewClassSql);

            // 清空表格模型
            tableModel.setRowCount(0);

            // 遍历查询结果，将数据添加到表格模型中
            while (viewClassSqlex.next()) {
                String cno = viewClassSqlex.getString("cno");
                String cname = viewClassSqlex.getString("cname");
                int cnumber = viewClassSqlex.getInt("cnumber");
                String mno = viewClassSqlex.getString("mno");

                // 将数据添加到表格模型
                tableModel.addRow(new Object[] { cno, cname, cnumber, mno });

            }

        } catch (SQLException e1) {



            e1.printStackTrace();
        }

    }

}
