package team.classes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class teacherMenu extends JFrame {

    private DefaultTableModel tableModel;
    private JTable studentTable;
    private String selectedKno;
    private String selectedSno;

    public teacherMenu(String username, Connection connection) {
        setTitle("教师界面");
        setSize(800, 500);

        // 创建表格模型和表格组件
        tableModel = new DefaultTableModel(new Object[][] {}, new Object[] { "学号", "姓名", "班级", "课程编号","课程", "成绩" });
        studentTable = new JTable(tableModel);

        // 添加表格到滚动窗格
        JScrollPane scrollPane = new JScrollPane(studentTable);

        // 添加滚动窗格到窗口
        add(scrollPane);

        // 创建按钮
        JButton modifyButton = new JButton("修改");

        // 创建一个面板用于放置按钮
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(modifyButton);

        // 添加按钮面板到窗口底部
        add(buttonPanel, BorderLayout.SOUTH);

        // 修改按钮
        modifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 获取选中的行
                int selectedRow = studentTable.getSelectedRow();
        
                // 检查是否有选中的行
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "请先选中要修改的行");
                    return;
                }
        
                // 获取选中行的学号和课程号
                selectedSno = (String) tableModel.getValueAt(selectedRow, 0);
                selectedKno = (String) tableModel.getValueAt(selectedRow, 3);
        
                // 弹出消息框，包含一个输入框
                String inputGrade = JOptionPane.showInputDialog(null, "成绩:", tableModel.getValueAt(selectedRow, 5));
        
                // 处理用户输入
                if (inputGrade != null && !inputGrade.isEmpty()) {
                    try {
                        int newGrade = Integer.parseInt(inputGrade);
        
                        // 在这里执行更新数据库的逻辑
                        updateGradeInDatabase(username, connection, newGrade);
        
        
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "请输入有效的数字");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        

        // 显示学生信息
        refreshStudentInfo(username, connection);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 将窗体显示在屏幕中央
        setVisible(true);
    }

    private void updateGradeInDatabase(String username, Connection connection, int newGrade) throws SQLException {
        // 在这里执行更新数据库的逻辑
        String updateGradeSql = "UPDATE grade SET grade = ? WHERE sno = ? AND kno = ?";
        //TODO 修改失败，找原因

        try (PreparedStatement updateGradeStatement = connection.prepareStatement(updateGradeSql)) {
            updateGradeStatement.setInt(1, newGrade);
            updateGradeStatement.setString(2, selectedSno);
            updateGradeStatement.setString(3, selectedKno);

            int rowsAffected = updateGradeStatement.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "成绩修改成功");
                // 刷新表格
                refreshStudentInfo(username, connection);
            } else {
                JOptionPane.showMessageDialog(null, "成绩修改失败");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println("SQL Error: " + ex.getMessage());
        }
    }

    

    private void refreshStudentInfo(String username, Connection connection) {
        // 执行查询语句
        String query = "SELECT student.sno, student.sname, class.cname,course.kno, course.kname, grade.grade "
                + "FROM grade "
                + "JOIN course ON course.kno = grade.kno "
                + "JOIN student ON student.sno = grade.sno "
                + "JOIN major ON student.mno = major.mno "
                + "JOIN depart ON major.dno = depart.dno "
                + "JOIN class ON student.cno = class.cno "
                + "JOIN teacher ON course.tno = teacher.tno "
                + "WHERE teacher.tno = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);

            // 执行查询
            ResultSet resultSet = preparedStatement.executeQuery();

            // 清空表格模型
            tableModel.setRowCount(0);

            // 处理查询结果并在表格中显示
            while (resultSet.next()) {
                String sno = resultSet.getString("sno");
                String sname = resultSet.getString("sname");
                String cname = resultSet.getString("cname");
                String kno = resultSet.getString("kno");
                String kname = resultSet.getString("kname");
                int grade = resultSet.getInt("grade");

                // 将数据添加到表格模型
                tableModel.addRow(new Object[] { sno, sname, cname, kno,kname, grade });
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {

    }
}
