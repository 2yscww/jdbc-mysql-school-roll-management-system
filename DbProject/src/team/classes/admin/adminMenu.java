package team.classes.admin;

import javax.management.JMException;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import team.application.*;
import team.classes.*;

public class adminMenu extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private DefaultTableModel tableModel;
    private JTable departmentTable;

    public adminMenu(Connection connection) throws SQLException { // 参数 Connection connection

        Statement statement = connection.createStatement();

        setTitle("管理员界面");
        setSize(800, 500);

        // 添加菜单栏
        JMenuBar menuBar = new JMenuBar();

        // 构建菜单
        JMenu departmentManagement = new JMenu("院系管理");
        JMenu marjorManagement = new JMenu("专业管理"); // 专业管理，考虑跟班级管理合并
        JMenu courseManagement = new JMenu("课程管理");
        JMenu teacherManagement = new JMenu("教师管理");
        JMenu stuManagement = new JMenu("学生管理");

        // 将选项添加进菜单
        menuBar.add(departmentManagement); // 院系管理
        menuBar.add(marjorManagement);
        menuBar.add(courseManagement);
        menuBar.add(teacherManagement);
        menuBar.add(stuManagement);

        // 设定菜单栏
        setJMenuBar(menuBar);

        /*
         * 现在考虑把menubar 里面 填写 menu
         * 然后再写子菜单去完成操作
         * 事件监听
         * 
         * 完善子菜单的流程
         * 
         * 创建子菜单项
         * 将子菜单添加进主菜单的名下
         * 创建相应子菜单的面板
         * 创建相应子菜单的响应事件
         */
        // FIXME 提示选项

        // <--------------------------------构建子菜单项------------------------------------->

        // 院系信息管理
        JMenuItem viewDepartment = new JMenuItem("院系信息管理");
        // JMenuItem addDepartment = new JMenuItem("修改院系信息");

        // 专业管理
        // JMenuItem viewMajorInfo = new JMenuItem("查看专业信息");
        JMenuItem modifyMajor = new JMenuItem("专业信息管理");
        JMenuItem modifyClass = new JMenuItem("班级信息管理");

        // 教师管理
        JMenuItem modifyTeacher = new JMenuItem("教师信息管理");

        // 学生管理
        JMenuItem viewStudentInfo = new JMenuItem("学生信息管理");
        JMenuItem modifyStudentScore = new JMenuItem("学生课程成绩管理");
        // JMenuItem modifyStudentCourse = new JMenuItem("学生选课管理");
        // JMenuItem modifyStudentClass = new JMenuItem("学生班级管理");
        JMenuItem modifyStudentAwardPunish = new JMenuItem("学生奖惩管理");
        JMenuItem viewStudentGlobal = new JMenuItem("学生概况查看");

        // 课程管理
        JMenuItem viewCourseInfo = new JMenuItem("课程信息管理");

        // <------------------------为子菜单项添加事件监听器---------------------------------->

        // 查看院系信息
        viewDepartment.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // 在这里处理查看院系的操作
                // JOptionPane.showMessageDialog(adminMenu.this, "执行查看院系操作");
                String showDepartmentInfo = "select * from depart";
                try {
                    ResultSet departResultSet = statement.executeQuery(showDepartmentInfo);

                    // 创建表格模型和表格组件
                    tableModel = new DefaultTableModel(new Object[][] {}, new Object[] { "院系代码", "院系名称" });
                    departmentTable = new JTable(tableModel);

                    // 从 ResultSet 中将数据填充到表格模型
                    while (departResultSet.next()) {
                        String departmentCode = departResultSet.getString("dno");
                        String departmentName = departResultSet.getString("dname");
                        tableModel.addRow(new Object[] { departmentCode, departmentName });
                    }

                    // 创建一个面板以容纳表格
                    JPanel viewDepartmentPanel = new JPanel(new BorderLayout());
                    // viewDepartmentPanel.add(new JLabel("院系信息管理页面"), BorderLayout.NORTH);
                    viewDepartmentPanel.add(new JScrollPane(departmentTable), BorderLayout.CENTER);

                    // 创建按钮
                    JButton addDepartButton = new JButton("增加");
                    JButton deleteDepartButton = new JButton("删除");
                    JButton modifyDepartButton = new JButton("修改");

                    // 创建一个面板以容纳按钮
                    JPanel buttonPanel = new JPanel();
                    buttonPanel.add(addDepartButton);
                    buttonPanel.add(deleteDepartButton);
                    buttonPanel.add(modifyDepartButton);

                    viewDepartmentPanel.add(buttonPanel, BorderLayout.SOUTH);

                    // 添加按钮操作
                    addDepartButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // 创建一个面板来容纳文本框
                            JPanel addDepartpanel = new JPanel();
                            addDepartpanel.setLayout(new GridLayout(2, 2));

                            // 创建文本框
                            JTextField departmentCodeField = new JTextField();
                            JTextField departmentNameField = new JTextField();

                            // 将文本框添加到面板
                            addDepartpanel.add(new JLabel("院系代码: "));
                            addDepartpanel.add(departmentCodeField);
                            addDepartpanel.add(new JLabel("院系名称: "));
                            addDepartpanel.add(departmentNameField);

                            // 弹出消息框
                            int result = JOptionPane.showConfirmDialog(null, addDepartpanel, "请输入院系信息",
                                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                            // 如果用户点击了OK按钮
                            if (result == JOptionPane.OK_OPTION) {
                                // 获取用户输入的内容
                                String departmentCode = departmentCodeField.getText();
                                String departmentName = departmentNameField.getText();

                                // 检查输入是否为空
                                if (departmentCode.isEmpty() || departmentName.isEmpty()) {
                                    JOptionPane.showMessageDialog(null, "院系代码和院系名称不能为空");
                                    return; // 不执行插入数据库的操作
                                }

                                String insertDepartSql = "INSERT INTO depart (dno, dname) VALUES (?,?)";

                                try {
                                    PreparedStatement insertDepartSqlex = connection.prepareStatement(insertDepartSql);
                                    insertDepartSqlex.setString(1, departmentCode);
                                    insertDepartSqlex.setString(2, departmentName);

                                    // 执行更新
                                    int rowsAffected = insertDepartSqlex.executeUpdate();

                                    if (rowsAffected > 0) {

                                        JOptionPane.showMessageDialog(null, "成功插入数据到数据库");
                                        refreshDepartmentTable();
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

                    // 删除按钮操作
                    deleteDepartButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // 获取选中的行
                            int selectedRow = departmentTable.getSelectedRow();

                            // 检查是否有选中的行
                            if (selectedRow == -1) {
                                JOptionPane.showMessageDialog(null, "请先选中要删除的行");
                                return;
                            }

                            // 获取选中行的数据
                            String departmentCode = (String) tableModel.getValueAt(selectedRow, 0);
                            // String departmentName = (String) tableModel.getValueAt(selectedRow, 1);

                            // 弹出确认对话框
                            int result = JOptionPane.showConfirmDialog(null, "确定删除选中的院系信息吗？", "确认删除",
                                    JOptionPane.YES_NO_OPTION);

                            // 如果用户确认删除
                            if (result == JOptionPane.YES_OPTION) {
                                // 执行删除操作
                                String deleteDepartSql = "DELETE FROM depart WHERE dno = ?";

                                try {
                                    PreparedStatement deleteDepartSqlex = connection.prepareStatement(deleteDepartSql);

                                    // 设置参数
                                    deleteDepartSqlex.setString(1, departmentCode);

                                    // 执行更新
                                    int rowsAffected = deleteDepartSqlex.executeUpdate();

                                    if (rowsAffected > 0) {
                                        JOptionPane.showMessageDialog(null, "成功删除选中的院系信息");
                                        // 从表格模型中移除选中的行数据
                                        tableModel.removeRow(selectedRow);
                                        refreshDepartmentTable();
                                    } else {
                                        JOptionPane.showMessageDialog(null, "删除院系信息失败");
                                    }
                                } catch (SQLException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    });

                    // 修改按钮操作
                    modifyDepartButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // 创建一个面板来容纳文本框
                            JPanel addDepartpanel = new JPanel();
                            addDepartpanel.setLayout(new GridLayout(2, 2));

                            // 创建文本框
                            JTextField departmentCodeField = new JTextField();
                            JTextField departmentNameField = new JTextField();

                            // 将文本框添加到面板
                            addDepartpanel.add(new JLabel("院系代码: "));
                            addDepartpanel.add(departmentCodeField);
                            addDepartpanel.add(new JLabel("院系名称: "));
                            addDepartpanel.add(departmentNameField);

                            // 获取选中的行
                            int selectedRow = departmentTable.getSelectedRow();
                            String originalDepartmentCode = (String) tableModel.getValueAt(selectedRow, 0);

                            // 检查是否有选中的行
                            if (selectedRow == -1) {
                                JOptionPane.showMessageDialog(null, "请先选中要修改的行");
                                return;
                            }

                            // 获取选中的行
                            int selectedOrginRow = departmentTable.getSelectedRow();
                            String originalSelectedDepartmentCode = (String) tableModel.getValueAt(selectedOrginRow, 0);

                            // 设置文本框的初始值为选中行的数据
                            departmentCodeField.setText(originalSelectedDepartmentCode);
                            departmentNameField.setText((String) tableModel.getValueAt(selectedOrginRow, 1));

                            // 弹出消息框
                            int result = JOptionPane.showConfirmDialog(null, addDepartpanel, "请输入院系信息",
                                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                            // 如果用户点击了OK按钮
                            if (result == JOptionPane.OK_OPTION) {
                                // 获取用户输入的内容
                                String departmentCode = departmentCodeField.getText();
                                String departmentName = departmentNameField.getText();

                                // 检查输入是否为空
                                if (departmentCode.isEmpty() || departmentName.isEmpty()) {
                                    JOptionPane.showMessageDialog(null, "院系代码和院系名称不能为空");
                                    return; // 不执行更新表的操作
                                }

                                String updateDepartSql = "UPDATE depart SET dno = ? , dname = ? WHERE dno = ?";

                                try {
                                    PreparedStatement updateDepartSqlex = connection.prepareStatement(updateDepartSql);
                                    updateDepartSqlex.setString(1, departmentCode);
                                    updateDepartSqlex.setString(2, departmentName);
                                    updateDepartSqlex.setString(3, originalDepartmentCode);

                                    // 执行更新
                                    int rowsAffected = updateDepartSqlex.executeUpdate();

                                    if (rowsAffected > 0) {

                                        JOptionPane.showMessageDialog(null, "成功修改数据");
                                        refreshDepartmentTable();
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

                    // 添加到卡片面板
                    cardPanel.add(viewDepartmentPanel, "viewDepartmentPanel");

                } catch (SQLException e1) {

                    e1.printStackTrace();
                }
                cardLayout.show(cardPanel, "viewDepartmentPanel");
            }

            // 刷新表格
            private void refreshDepartmentTable() {
                try {
                    String showDepartmentInfo = "select * from depart";
                    ResultSet departResultSet = statement.executeQuery(showDepartmentInfo);

                    // 清空表格模型
                    tableModel.setRowCount(0);

                    // 从 ResultSet 中将数据填充到表格模型
                    while (departResultSet.next()) {
                        String departmentCode = departResultSet.getString("dno");
                        String departmentName = departResultSet.getString("dname");
                        tableModel.addRow(new Object[] { departmentCode, departmentName });
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

        });

        // 教师信息管理
        modifyTeacher.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new modifyTeacherMenu(adminMenu.this, connection);

            }
        });

        // 专业信息管理

        modifyMajor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // new modifyDepartmentMenu(adminMenu.this,connection); modifyMajorMenu
                new modifyMajorMenu(adminMenu.this, connection);
            }

        });

        // 班级信息管理，与专业管理合并
        modifyClass.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new modifyClassMenu(adminMenu.this, connection);

            }

        });

        // 查看学生信息
        viewStudentInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 在这里处理查看学生的操作
                // cardLayout.show(cardPanel, "viewStudentInfoPanel");
                new viewStudentInfoMenu(adminMenu.this, connection);

            }
        });

        // 学生概况
        viewStudentGlobal.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                

                new viewStudentGlobalMenu(adminMenu.this, connection);
            }

        });

        // 学生奖惩管理
        modifyStudentAwardPunish.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                new modifyStudentAwardPunishMenu(adminMenu.this, connection);
            }

        });

        modifyStudentScore.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new modifyStudentScoreMenu(adminMenu.this, connection);
            }

        });

        // 课程信息管理
        viewCourseInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // cardLayout.show(cardPanel, "viewCourseInfoPanel");
                new modifyCourseMenu(adminMenu.this, connection);

            }
        });

        // <----------------------------------将子菜单项添加到父菜单------------------------------------>
        // 院系
        departmentManagement.add(viewDepartment);
        // departmentManagement.add(addDepartment);

        // 专业
        // marjorManagement.add(viewMajorInfo);
        marjorManagement.add(modifyMajor);
        marjorManagement.add(modifyClass);

        // 教师管理
        teacherManagement.add(modifyTeacher);

        // 学生
        stuManagement.add(viewStudentInfo);
        stuManagement.add(modifyStudentScore);
        // stuManagement.add(modifyStudentCourse);
        // stuManagement.add(modifyStudentClass);
        stuManagement.add(modifyStudentAwardPunish);
        stuManagement.add(viewStudentGlobal);

        // 课程
        courseManagement.add(viewCourseInfo);

        // 创建面板
        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);

        // <----------------------------------向面板添加不同的页面------------------------------------------>

        // 院系信息
        JPanel viewDepartmentPanel = new JPanel();
        viewDepartmentPanel.add(new JLabel("欢迎登录，管理员!"));
        cardPanel.add(viewDepartmentPanel, "viewDepartmentPanel");

        JPanel addDepartmentPanel = new JPanel();
        // addDepartmentPanel.add(new JLabel("修改院系信息页面"));
        cardPanel.add(addDepartmentPanel, "addDepartmentPanel");

        // 专业信息
        JPanel viewMajorInfoPanel = new JPanel();

        // viewMajorInfoPanel.add(new JLabel("查看专业信息页面"));
        cardPanel.add(viewMajorInfoPanel, "viewMajorInfoPanel");
        // 修改专业信息
        // JPanel modifyMajorPanel = new JPanel();
        // cardPanel.add(modifyMajorPanel,"modifyMajor");

        // 学生信息
        //JPanel viewStudentInfoPanel = new JPanel();
        // viewStudentInfoPanel.add(new JLabel("查看学生信息页面"));
        // cardPanel.add(viewStudentInfoPanel, "viewStudentInfoPanel");

        // 课程信息
        // JPanel viewCourseInfoPanel = new JPanel();
        // viewCourseInfoPanel.add(new JLabel("查看课程信息页面"));
        // cardPanel.add(viewCourseInfoPanel, "viewCourseInfoPanel");

        // 将卡片面板添加到主窗口
        add(cardPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 将窗体显示在屏幕中央
        setVisible(true);
    }

    public static void main(String[] args) {
        // Connection connection = null;

        // // 获取数据库连接
        // String url = "jdbc:mysql://127.0.0.1:3306/school_roll_management_system?characterEncoding=utf8&useSSL=false";
        // String user = "root";
        // String passwd = "w86#qNwV";

        // // 创建连接
        // try {
        //     // FIXME 这里在完成后需要被删除
        //     connection = DriverManager.getConnection(url, user, passwd);
        //     new adminMenu(connection);

        // } catch (SQLException e) {

        //     e.printStackTrace();
        // }

        // 还要再注释掉
        // new adminMenu();

        /*
         * 最终还是要实现从Main那里调用
         */

    }
}
