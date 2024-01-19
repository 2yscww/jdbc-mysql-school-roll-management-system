package team.classes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class RewardPunishmentMenu extends JFrame {

    private DefaultTableModel tableModel;
    private JTable rewardPunishmentTable;

    public RewardPunishmentMenu(String username, Connection connection) {
        setTitle("奖惩信息");
        setSize(800, 500);

        // 创建表格模型和表格组件
        tableModel = new DefaultTableModel(new Object[][] {}, new Object[] { "奖惩编号", "奖惩方案", "奖惩下达日期" });
        rewardPunishmentTable = new JTable(tableModel);

        // 添加表格到滚动窗格
        JScrollPane scrollPane = new JScrollPane(rewardPunishmentTable);

        // 添加滚动窗格到窗口
        add(scrollPane);

        // 显示奖惩信息
        refreshRewardPunishmentInfo(username, connection);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 仅关闭当前窗口，不退出程序
        setLocationRelativeTo(null); // 将窗体显示在屏幕中央
        setVisible(true); // 显示当前窗口
    }

    private void refreshRewardPunishmentInfo(String username, Connection connection) {
        // 执行查询语句
        String query = "SELECT apno, aproject, adate FROM award_punish WHERE asno = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);

            // 执行查询
            ResultSet resultSet = preparedStatement.executeQuery();

            // 清空表格模型
            tableModel.setRowCount(0);

            // 处理查询结果并在表格中显示
            while (resultSet.next()) {
                String apno = resultSet.getString("apno");
                String aproject = resultSet.getString("aproject");
                Date adate = resultSet.getDate("adate");

                // 将数据添加到表格模型
                tableModel.addRow(new Object[] { apno, aproject, adate });
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
       
    }
}
