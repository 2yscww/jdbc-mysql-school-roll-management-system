package team.classes.admin;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;

import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.sql.*;

public class viewStudentInfoMenu extends JFrame {

    private DefaultTableModel tableModel;
    private JTable studentTable;
    public viewStudentInfoMenu(adminMenu parentFrame, Connection connection) {
        setTitle("学生信息管理");
        setSize(800, 500);

        // 添加窗口监听器，捕捉窗口关闭事件
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // 显示父窗口
                parentFrame.setVisible(true);
            }
        });




            /*
 * mysql> describe student;
+------------+-------------+------+-----+---------+-------+
| Field      | Type        | Null | Key | Default | Extra |
+------------+-------------+------+-----+---------+-------+
| sno        | char(10)    | NO   | PRI | NULL    |       |
| sname      | varchar(10) | NO   |     | NULL    |       |
| ssex       | char(2)     | NO   |     | NULL    |       |
| sage       | tinyint     | NO   |     | NULL    |       |
| sbirth     | date        | NO   |     | NULL    |       |
| sadmission | date        | NO   |     | NULL    |       |
| cno        | char(8)     | YES  | MUL | NULL    |       |
| mno        | char(8)     | YES  | MUL | NULL    |       |
+------------+-------------+------+-----+---------+-------+
8 rows in set (0.00 sec)
*/






        // 隐藏父窗口
        parentFrame.setVisible(false);

         // 创建表格模型和表格组件
         tableModel = new DefaultTableModel(new Object[][] {}, new Object[] { "学号", "姓名", "性别", "年龄","出生日期","入学日期","班级编号","专业代码" });
         studentTable = new JTable(tableModel);
 
         // 添加表格到滚动窗格
         JScrollPane scrollPane = new JScrollPane(studentTable);
 
         // 添加滚动窗格到窗口
         add(scrollPane);
 
         // 创建按钮
         JButton addStudentButton = new JButton("增加");
         JButton deleteStudentButton = new JButton("删除");
         JButton modifyStudentButton = new JButton("修改");
 
         // 创建一个面板用于放置按钮
         JPanel buttonPanel = new JPanel();
         buttonPanel.add(addStudentButton);
         buttonPanel.add(deleteStudentButton);
         buttonPanel.add(modifyStudentButton);
 
         // 将按钮面板添加到窗口底部
         add(buttonPanel, BorderLayout.SOUTH);

           // <---------------------------按钮监听器---------------------------------->

           addStudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 创建面板来容纳文本框
                JPanel addStudentPanel = new JPanel();
                addStudentPanel.setLayout(new GridLayout(8, 2));
        
                // 创建文本框 "学号", "姓名", "性别", "年龄","出生日期","入学日期","班级编号","专业代码"
                JTextField studentNumberField = new JTextField();
                JTextField studentNameField = new JTextField();
                JTextField studentSexField = new JTextField();
                JTextField studentAgeField = new JTextField();
                JTextField studentSbirthField = new JTextField();
                JTextField studentAdmissionField = new JTextField();
                JTextField classCodeField = new JTextField();
                JTextField majorCodeField = new JTextField();
        
                // 将文本框添加到面板
                addStudentPanel.add(new JLabel("学号: "));
                addStudentPanel.add(studentNumberField);
                addStudentPanel.add(new JLabel("姓名: "));
                addStudentPanel.add(studentNameField);
                addStudentPanel.add(new JLabel("性别: "));
                addStudentPanel.add(studentSexField);
                addStudentPanel.add(new JLabel("年龄: "));
                addStudentPanel.add(studentAgeField);
                addStudentPanel.add(new JLabel("出生日期: "));
                addStudentPanel.add(studentSbirthField);
                addStudentPanel.add(new JLabel("入学日期: "));
                addStudentPanel.add(studentAdmissionField);
                addStudentPanel.add(new JLabel("班级编号: "));
                addStudentPanel.add(classCodeField);
                addStudentPanel.add(new JLabel("专业代码: "));
                addStudentPanel.add(majorCodeField);
        
                // 弹出消息框
                int result = JOptionPane.showConfirmDialog(null, addStudentPanel, "请输入学生信息",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
                // 如果用户点击了OK按钮
                if (result == JOptionPane.OK_OPTION) {
                    // 获取用户输入的内容
                    String studentNumber = studentNumberField.getText();
                    String studentName = studentNameField.getText();
                    String studentSex = studentSexField.getText();
                    String studentAge = studentAgeField.getText();
                    String studentSbirth = studentSbirthField.getText();
                    String studentAdmission = studentAdmissionField.getText();
                    String classCode = classCodeField.getText();
                    String majorCode = majorCodeField.getText();
        
                    // 检查输入是否为空
                    if (studentNumber.isEmpty() || studentName.isEmpty() || studentSex.isEmpty() ||
                            studentAge.isEmpty() || studentSbirth.isEmpty() || studentAdmission.isEmpty() ||
                            classCode.isEmpty() || majorCode.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "除班级编号及专业代码外都不能为空");
                        return; // 不执行修改数据库的操作
                    }
        
                    String addStudentSql = "INSERT INTO student(sno,sname,ssex,sage,sbirth,sadmission,cno,mno) VALUES(?,?,?,?,?,?,?,?)";
        
                    try {
                        // 创建 PreparedStatement 对象
                        PreparedStatement addStudentSqlex = connection.prepareStatement(addStudentSql);
        
                        // 设置每个占位符的值
                        addStudentSqlex.setString(1, studentNumber);
                        addStudentSqlex.setString(2, studentName);
                        addStudentSqlex.setString(3, studentSex);
                        addStudentSqlex.setInt(4, Integer.parseInt(studentAge));
                        // 设置日期类型，注意这里需要将字符串转换为 java.sql.Date
                        addStudentSqlex.setDate(5, java.sql.Date.valueOf(studentSbirth));
                        addStudentSqlex.setDate(6, java.sql.Date.valueOf(studentAdmission));
                        addStudentSqlex.setString(7, classCode);
                        addStudentSqlex.setString(8, majorCode);
        
                        // 执行更新
                        int rowsAffected = addStudentSqlex.executeUpdate();
        
                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(null, "成功插入数据到数据库");
                            refreshStudentTable(connection);
                        } else {
                            JOptionPane.showMessageDialog(null, "插入数据到数据库失败");
                        }
                    } catch (SQLException e1) {
                        // 在这里处理 SQL 异常
                        e1.printStackTrace();
        
                        // 获取异常的错误码
                        int errorCode = e1.getErrorCode();
        
                        // 根据错误码进行不同的处理
                        if (errorCode == 1062) {
                            // 处理唯一键冲突异常
                            JOptionPane.showMessageDialog(null, "学号重复，无法插入数据");
                        } else {
                            // 处理其他 SQL 异常
                            JOptionPane.showMessageDialog(null, "发生数据库错误");
                        }
                    }
                }
            }
        });

        // 删除按钮
deleteStudentButton.addActionListener(new ActionListener() {

    @Override
    public void actionPerformed(ActionEvent e) {
        // 获取选中的行
        int selectedRow = studentTable.getSelectedRow();

        // 检查是否有选中的行
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "请先选中要删除的行");
            return;
        }

        // 获取选中行的数据
        String studentNumber = (String) tableModel.getValueAt(selectedRow, 0);

        // 弹出确认对话框
        int result = JOptionPane.showConfirmDialog(null, "确定删除选中的学生信息吗？", "确认删除",
                JOptionPane.YES_NO_OPTION);

        // 如果用户确认删除
        if (result == JOptionPane.YES_OPTION) {
            // 执行删除操作
            String deleteStudentSql = "DELETE FROM student WHERE sno = ?";

            try {
                PreparedStatement deleteStudentSqlex = connection.prepareStatement(deleteStudentSql);

                // 设置参数
                deleteStudentSqlex.setString(1, studentNumber);

                // 执行更新
                int rowsAffected = deleteStudentSqlex.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "成功删除选中的学生信息");
                    // 从表格模型中移除选中的行数据
                    tableModel.removeRow(selectedRow);
                    refreshStudentTable(connection);
                } else {
                    JOptionPane.showMessageDialog(null, "删除学生信息失败");
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
modifyStudentButton.addActionListener(new ActionListener() {

    @Override
    public void actionPerformed(ActionEvent e) {

        // 创建一个面板来容纳文本框
        JPanel modifyStudentPanel = new JPanel();
        modifyStudentPanel.setLayout(new GridLayout(8, 2));

        // 创建文本框
        JTextField studentNumberField = new JTextField();
        JTextField studentNameField = new JTextField();
        JTextField studentSexField = new JTextField();
        JTextField studentAgeField = new JTextField();
        JTextField studentSbirthField = new JTextField();
        JTextField studentAdmissionField = new JTextField();
        JTextField classCodeField = new JTextField();
        JTextField majorCodeField = new JTextField();

        // "学号", "姓名", "性别", "年龄", "出生日期", "入学日期", "班级编号", "专业代码"

        // 将文本框添加到面板
        modifyStudentPanel.add(new JLabel("学号: "));
        modifyStudentPanel.add(studentNumberField);
        modifyStudentPanel.add(new JLabel("姓名: "));
        modifyStudentPanel.add(studentNameField);
        modifyStudentPanel.add(new JLabel("性别: "));
        modifyStudentPanel.add(studentSexField);
        modifyStudentPanel.add(new JLabel("年龄: "));
        modifyStudentPanel.add(studentAgeField);
        modifyStudentPanel.add(new JLabel("出生日期: "));
        modifyStudentPanel.add(studentSbirthField);
        modifyStudentPanel.add(new JLabel("入学日期: "));
        modifyStudentPanel.add(studentAdmissionField);
        modifyStudentPanel.add(new JLabel("班级编号: "));
        modifyStudentPanel.add(classCodeField);
        modifyStudentPanel.add(new JLabel("专业代码: "));
        modifyStudentPanel.add(majorCodeField);

        // 获取选中的行
        int selectedRow = studentTable.getSelectedRow();
        String originalStudentNumber = (String) tableModel.getValueAt(selectedRow, 0);

        // 检查是否有选中的行
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "请先选中要修改的行");
            return;
        }

         // 设置文本框的初始值为选中行的数据
         studentNumberField.setText(originalStudentNumber);
         studentNameField.setText((String) tableModel.getValueAt(selectedRow, 1));
         studentSexField.setText((String) tableModel.getValueAt(selectedRow, 2));
         studentAgeField.setText(tableModel.getValueAt(selectedRow, 3).toString());
         studentSbirthField.setText(tableModel.getValueAt(selectedRow, 4).toString());
         studentAdmissionField.setText(tableModel.getValueAt(selectedRow, 5).toString());
         classCodeField.setText((String) tableModel.getValueAt(selectedRow, 6));
         majorCodeField.setText((String) tableModel.getValueAt(selectedRow, 7));

        


        // 弹出消息框
        int result = JOptionPane.showConfirmDialog(null, modifyStudentPanel, "请输入学生信息",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        // 如果用户点击了OK按钮
        if (result == JOptionPane.OK_OPTION) {
            // 获取用户输入的内容
            String studentNumber = studentNumberField.getText();
            String studentName = studentNameField.getText();
            String studentSex = studentSexField.getText();
            String studentAge = studentAgeField.getText();
            String studentSbirth = studentSbirthField.getText();
            String studentAdmission = studentAdmissionField.getText();
            String classCode = classCodeField.getText();
            String majorCode = majorCodeField.getText();

            // 检查输入是否为空
            if (studentNumber.isEmpty() || studentName.isEmpty() || studentSex.isEmpty() || studentAge.isEmpty()
                    || studentSbirth.isEmpty() || studentAdmission.isEmpty() || classCode.isEmpty()
                    || majorCode.isEmpty()) {
                JOptionPane.showMessageDialog(null, "所有字段不能为空");
                return; // 不执行更新表的操作
            }

            String updateStudentSql = "UPDATE student SET sno = ?, sname = ?, ssex = ?, sage = ?, sbirth = ?, sadmission = ?, cno = ?, mno = ? WHERE sno = ?";

            try {
                PreparedStatement updateStudentSqlex = connection.prepareStatement(updateStudentSql);
                updateStudentSqlex.setString(1, studentNumber);
                updateStudentSqlex.setString(2, studentName);
                updateStudentSqlex.setString(3, studentSex);
                updateStudentSqlex.setInt(4, Integer.parseInt(studentAge));
                updateStudentSqlex.setDate(5, Date.valueOf(studentSbirth)); // 假设 studentSbirth 是 java.sql.Date 类型
                updateStudentSqlex.setDate(6, Date.valueOf(studentAdmission)); // 假设 studentAdmission 是 java.sql.Date 类型
                updateStudentSqlex.setString(7, classCode);
                updateStudentSqlex.setString(8, majorCode);
                updateStudentSqlex.setString(9, originalStudentNumber);

                // 执行更新
                int rowsAffected = updateStudentSqlex.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "成功修改数据");
                    refreshStudentTable(connection);
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


        

           // <---------------------------------------------------------------------->




        // 显示学生信息
        String viewStudentSql = "SELECT * FROM student";

        try (Statement statement = connection.createStatement()) {
            ResultSet viewStudentSqlex = statement.executeQuery(viewStudentSql);

            // 清空表格模型
            tableModel.setRowCount(0);

            // 遍历查询结果，将数据添加到表格模型中
            while (viewStudentSqlex.next()) {
                String sno = viewStudentSqlex.getString("sno");
                String sname = viewStudentSqlex.getString("sname");
                String ssex = viewStudentSqlex.getString("ssex");
                int sage = viewStudentSqlex.getInt("sage");
                Date sbirth = viewStudentSqlex.getDate("sbirth");
Date sadmission = viewStudentSqlex.getDate("sadmission");
String cno = viewStudentSqlex.getString("cno");
String mno = viewStudentSqlex.getString("mno");
                

                // int cnumber = viewStudentSqlex.getInt("cnumber");
                // String mno = viewStudentSqlex.getString("mno");

                // 将数据添加到表格模型
                tableModel.addRow(new Object[] { sno, sname, ssex, sage,sbirth,sadmission ,cno,mno});

            }

        } catch (SQLException e1) {

            e1.printStackTrace();
        }

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 释放窗口资源

        setLocationRelativeTo(null); // 将窗体显示在屏幕中央
        setVisible(true); // 显示当前窗口




    }

    private void refreshStudentTable(Connection connection){
         // 显示学生信息
        String viewStudentSql = "SELECT * FROM student";

        try (Statement statement = connection.createStatement()) {
            ResultSet viewStudentSqlex = statement.executeQuery(viewStudentSql);

            // 清空表格模型
            tableModel.setRowCount(0);

            // 遍历查询结果，将数据添加到表格模型中
            while (viewStudentSqlex.next()) {
                String sno = viewStudentSqlex.getString("sno");
                String sname = viewStudentSqlex.getString("sname");
                String ssex = viewStudentSqlex.getString("ssex");
                int sage = viewStudentSqlex.getInt("sage");
                Date sbirth = viewStudentSqlex.getDate("sbirth");
Date sadmission = viewStudentSqlex.getDate("sadmission");
String cno = viewStudentSqlex.getString("cno");
String mno = viewStudentSqlex.getString("mno");
                

                // int cnumber = viewStudentSqlex.getInt("cnumber");
                // String mno = viewStudentSqlex.getString("mno");

                // 将数据添加到表格模型
                tableModel.addRow(new Object[] { sno, sname, ssex, sage,sbirth,sadmission ,cno,mno});

            }

        } catch (SQLException e1) {

            e1.printStackTrace();
        }


    }
}
