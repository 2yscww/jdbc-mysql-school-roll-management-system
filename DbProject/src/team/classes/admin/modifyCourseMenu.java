package team.classes.admin;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;

import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.sql.*;

public class modifyCourseMenu extends JFrame {
    private DefaultTableModel tableModel;
    private JTable courseTable;

    public modifyCourseMenu(adminMenu parentFrame, Connection connection) {
        setTitle("课程信息管理");
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
        tableModel = new DefaultTableModel(new Object[][] {}, new Object[] { "课程编号", "课程名称", "教师编号" ,"学时", "学分"});

        courseTable = new JTable(tableModel);

        // 添加表格到滚动窗格
        JScrollPane scrollPane = new JScrollPane(courseTable);

        // 添加滚动窗格到窗口
        add(scrollPane);

        // 创建按钮
        JButton addCourseButton = new JButton("增加");
        JButton deleteCourseButton = new JButton("删除");
        JButton modifyCourseButton = new JButton("修改");

        // 创建一个面板用于放置按钮
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addCourseButton);
        buttonPanel.add(deleteCourseButton);
        buttonPanel.add(modifyCourseButton);

        // 将按钮面板添加到窗口底部
        add(buttonPanel, BorderLayout.SOUTH);

        // <---------------------------按钮监听器---------------------------------->

        addCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // 创建一个面板来容纳文本框
                JPanel modifyCoursepanel = new JPanel();
                modifyCoursepanel.setLayout(new GridLayout(5, 2));

                // 创建文本框
                JTextField CourseCodeField = new JTextField();
                JTextField CourseNameField = new JTextField();
                JTextField CoursePeriodField = new JTextField();
                JTextField CourseCreditField = new JTextField();
                JTextField teacherCodeField = new JTextField();


                // 将文本框添加到面板
                // 将文本框添加到面板
                modifyCoursepanel.add(new JLabel("课程编号: "));
                modifyCoursepanel.add(CourseCodeField);
                modifyCoursepanel.add(new JLabel("课程名称: "));
                modifyCoursepanel.add(CourseNameField);
                modifyCoursepanel.add(new JLabel("教师编号: "));
                modifyCoursepanel.add(teacherCodeField);
                modifyCoursepanel.add(new JLabel("学时： "));
                modifyCoursepanel.add(CoursePeriodField);
                modifyCoursepanel.add(new JLabel("学分： "));
                modifyCoursepanel.add(CourseCreditField);

                // 弹出消息框
                int result = JOptionPane.showConfirmDialog(null, modifyCoursepanel, "请输入课程信息",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                // 如果用户点击了OK按钮
                if (result == JOptionPane.OK_OPTION) {
                    // 获取用户输入的内容
                    String CourseCode = CourseCodeField.getText();
                    String CourseName = CourseNameField.getText();
                    String CourseTeacherCode = teacherCodeField.getText();
                    String CoursePeriod = CoursePeriodField.getText();
                    String CourseCredit = CourseCreditField.getText();
                    

                    // 检查输入是否为空
                    if (CourseCode.isEmpty() || CourseName.isEmpty() || CoursePeriod.isEmpty()
                            || CourseCredit.isEmpty() || CourseTeacherCode.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "输入框都不能为空");
                        return; // 不执行修改数据库的操作
                    }

                    String addCourseSql = "INSERT INTO course(kno,kname,tno,kperiod,kcredit) VALUES(?, ?,?, ?, ?)";

                    try {
                        PreparedStatement addCourseSqlex = connection.prepareStatement(addCourseSql);
                        // insertDepartSqlex.setString(1, departmentCode);
                        // insertDepartSqlex.setString(2, departmentName);

                        addCourseSqlex.setString(1, CourseCode);
                        addCourseSqlex.setString(2, CourseName);
                        addCourseSqlex.setString(3,CourseTeacherCode);
                        addCourseSqlex.setInt(4, Integer.parseInt(CoursePeriod));
                        addCourseSqlex.setInt(5, Integer.parseInt(CourseCredit));

                        // 执行更新
                        int rowsAffected = addCourseSqlex.executeUpdate();

                        if (rowsAffected > 0) {

                            JOptionPane.showMessageDialog(null, "成功插入数据到数据库");
                            // refreshViewMajorTable(connection);
                            refreshViewCourseTable(connection);
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

        deleteCourseButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                // 获取选中的行
                int selectedRow = courseTable.getSelectedRow();

                // 检查是否有选中的行
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "请先选中要删除的行");
                    return;
                }

                // 获取选中行的数据
                String CourseCode = (String) tableModel.getValueAt(selectedRow, 0);

                // 弹出确认对话框
                int result = JOptionPane.showConfirmDialog(null, "确定删除选中的课程信息吗？", "确认删除",
                        JOptionPane.YES_NO_OPTION);

                // 如果用户确认删除
                if (result == JOptionPane.YES_OPTION) {
                    // 执行删除操作
                    String deleteCourseSql = "DELETE FROM course where kno = ?";

                    try {
                        PreparedStatement deleteCourseSqlex = connection.prepareStatement(deleteCourseSql);

                        // 设置参数
                        deleteCourseSqlex.setString(1, CourseCode);

                        // 执行更新
                        int rowsAffected = deleteCourseSqlex.executeUpdate();

                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(null, "成功删除选中的课程信息");
                            // 从表格模型中移除选中的行数据
                            tableModel.removeRow(selectedRow);
                            // refreshViewMajorTable(connection);
                            refreshViewCourseTable(connection);
                        } else {
                            JOptionPane.showMessageDialog(null, "删除课程信息失败");
                        }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    
                }

            }

        });

        // 修改按钮
        modifyCourseButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                // 创建一个面板来容纳文本框
                JPanel modifyCoursePanel = new JPanel();
                modifyCoursePanel.setLayout(new GridLayout(5, 2));

                // 创建文本框
                JTextField CourseCodeField = new JTextField();
                JTextField CourseNameField = new JTextField();
                JTextField CoursePeriodField = new JTextField();
                JTextField CourseCreditField = new JTextField();
                JTextField teacherCodeField = new JTextField();


                // 获取选中的行
                int selectedRow = courseTable.getSelectedRow();
                String originalCourseCode = (String) tableModel.getValueAt(selectedRow, 0);

                // 从选中行获取当前值
                String currentCourseCode = (String) tableModel.getValueAt(selectedRow, 0);
                String currentCourseName = (String) tableModel.getValueAt(selectedRow, 1);
                String currentTeacherCode = (String) tableModel.getValueAt(selectedRow, 2);
                int currentCoursePeriod = (int) tableModel.getValueAt(selectedRow, 3);
                int currentCourseCredit = (int) tableModel.getValueAt(selectedRow, 4);

                // 设置文本框的初始值为当前值
                CourseCodeField.setText(currentCourseCode);
                CourseNameField.setText(currentCourseName);
                teacherCodeField.setText(currentTeacherCode);
                CoursePeriodField.setText(String.valueOf(currentCoursePeriod));
                CourseCreditField.setText(String.valueOf(currentCourseCredit));

                // 将文本框添加到面板
                modifyCoursePanel.add(new JLabel("课程编号： "));
                modifyCoursePanel.add(CourseCodeField);
                modifyCoursePanel.add(new JLabel("课程名称: "));
                modifyCoursePanel.add(CourseNameField);
                modifyCoursePanel.add(new JLabel("教师编号: "));
                modifyCoursePanel.add(teacherCodeField);
                modifyCoursePanel.add(new JLabel("学时： "));
                modifyCoursePanel.add(CoursePeriodField);
                modifyCoursePanel.add(new JLabel("学分： "));
                modifyCoursePanel.add(CourseCreditField);

                // 检查是否有选中的行
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "请先选中要修改的行");
                    return;
                }

                // 弹出消息框
                int result = JOptionPane.showConfirmDialog(null, modifyCoursePanel, "请输入课程信息",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                // 如果用户点击了OK按钮
                if (result == JOptionPane.OK_OPTION) {
                    // 获取用户输入的内容
                    String CourseCode = CourseCodeField.getText();
                    String CourseName = CourseNameField.getText();
                    String CourseTeacherCode = teacherCodeField.getText();
                    String CoursePeriod = CoursePeriodField.getText();
                    String CourseCredit = CourseCreditField.getText();

                    // 检查输入是否为空
                    if (CourseCode.isEmpty() || CourseName.isEmpty() || CoursePeriod.isEmpty()
                            || CourseCredit.isEmpty() || CourseTeacherCode.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "输入框都不能为空");
                        return; // 不执行更新表的操作
                    }

                    String updateCourseSql = "UPDATE course SET kno = ?,kname = ?,tno = ?,kperiod = ?,kcredit = ? where kno = ?";

                    try {
                        PreparedStatement updateCourseSqlex = connection.prepareStatement(updateCourseSql);
                        updateCourseSqlex.setString(1, CourseCode);
                        updateCourseSqlex.setString(2, CourseName);
                        updateCourseSqlex.setString(3,CourseTeacherCode);
                        updateCourseSqlex.setInt(4, Integer.parseInt(CoursePeriod));
                        updateCourseSqlex.setInt(5, Integer.parseInt(CourseCredit));
                        updateCourseSqlex.setString(6, originalCourseCode);

                        // 执行更新
                        int rowsAffected = updateCourseSqlex.executeUpdate();

                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(null, "成功修改数据");
                            refreshViewCourseTable(connection);
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

        // <---------------------------------------------------------------------->

        // 显示课程信息
        String viewCourseSql = "SELECT * FROM course";

        try (Statement statement = connection.createStatement()) {
            ResultSet viewCourseSqlex = statement.executeQuery(viewCourseSql);

            // 清空表格模型
            tableModel.setRowCount(0);

            // 遍历查询结果，将数据添加到表格模型中
            while (viewCourseSqlex.next()) {
                String kno = viewCourseSqlex.getString("kno");
                String kname = viewCourseSqlex.getString("kname");
                String tno = viewCourseSqlex.getString("tno"); 
                int kperiod = viewCourseSqlex.getInt("kperiod");
                int kcredit = viewCourseSqlex.getInt("kcredit");

                // 将数据添加到表格模型
                tableModel.addRow(new Object[] { kno, kname,tno, kperiod, kcredit });

            }

        } catch (SQLException e1) {

            e1.printStackTrace();
        }

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null); // 将窗体显示在屏幕中央
        setVisible(true); // 显示当前窗口
    }

    private void refreshViewCourseTable(Connection connection) {
        // 显示课程信息
        String viewCourseSql = "SELECT * FROM course";

        try (Statement statement = connection.createStatement()) {
            ResultSet viewCourseSqlex = statement.executeQuery(viewCourseSql);

            // 清空表格模型
            tableModel.setRowCount(0);

            // 遍历查询结果，将数据添加到表格模型中
            while (viewCourseSqlex.next()) {
                String kno = viewCourseSqlex.getString("kno");
                String kname = viewCourseSqlex.getString("kname");
                String tno = viewCourseSqlex.getString("tno"); 
                int kperiod = viewCourseSqlex.getInt("kperiod");
                int kcredit = viewCourseSqlex.getInt("kcredit");

                // 将数据添加到表格模型
                tableModel.addRow(new Object[] { kno, kname,tno, kperiod, kcredit });

            }

        } catch (SQLException e1) {

            e1.printStackTrace();
        }
    }

}
