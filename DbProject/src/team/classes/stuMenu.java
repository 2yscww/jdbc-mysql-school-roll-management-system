package team.classes;

import team.classes.RewardPunishmentMenu;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class stuMenu extends JFrame {

    private DefaultTableModel tableModel;
    private JTable studentTable;
    private String selectedKno;
    private String selectedSno;

    public stuMenu(String username, Connection connection) {
        setTitle("学生界面");
        setSize(800, 500);

        // 创建表格模型和表格组件
        tableModel = new DefaultTableModel(new Object[][] {}, new Object[] { "课程编号", "课程", "成绩" });
        studentTable = new JTable(tableModel);

        // 添加表格到滚动窗格
        JScrollPane scrollPane = new JScrollPane(studentTable);

        // 添加滚动窗格到窗口
        add(scrollPane);

        // 创建菜单栏
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // 创建“查询”菜单
        JMenu queryMenu = new JMenu("信息查看");
        menuBar.add(queryMenu);

        // 创建“课程成绩”菜单项
        JMenuItem viewGradeMenuItem = new JMenuItem("课程成绩");
        viewGradeMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 在这里添加查看课程成绩的逻辑
                // 示例：showGradeInformation(username, connection);
                refreshStudentInfo(username, connection);
            }
        });
        queryMenu.add(viewGradeMenuItem);

        // 创建“奖惩信息”菜单项
        JMenuItem viewRewardPunishmentMenuItem = new JMenuItem("奖惩信息");
        viewRewardPunishmentMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new RewardPunishmentMenu(username, connection);
                // 在这里添加查看奖惩信息的逻辑
                // 示例：showRewardPunishmentInformation(username, connection);
            }
        });
        queryMenu.add(viewRewardPunishmentMenuItem);

        // 显示学生信息（默认执行课程成绩的查询）
        refreshStudentInfo(username, connection);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 将窗体显示在屏幕中央
        setVisible(true);
    }

    // 以下是略作修改的 refreshStudentInfo 方法，仅展示了部分信息
    private void refreshStudentInfo(String username, Connection connection) {
        // 执行查询语句
        String query = "SELECT course.kno, course.kname, grade.grade "
                + "FROM grade "
                + "JOIN course ON course.kno = grade.kno "
                + "JOIN student ON student.sno = grade.sno "
                + "WHERE student.sno = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);

            // 执行查询
            ResultSet resultSet = preparedStatement.executeQuery();

            // 清空表格模型
            tableModel.setRowCount(0);

            // 处理查询结果并在表格中显示
            while (resultSet.next()) {
                String kno = resultSet.getString("kno");
                String kname = resultSet.getString("kname");
                int grade = resultSet.getInt("grade");

                // 将数据添加到表格模型
                tableModel.addRow(new Object[] { kno, kname, grade });
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        
    }
}
