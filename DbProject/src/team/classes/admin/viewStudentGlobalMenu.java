package team.classes.admin;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.sql.*;

public class viewStudentGlobalMenu extends JFrame {
    private DefaultTableModel tableModel;
    private JTable studentTable;

    public viewStudentGlobalMenu(adminMenu parentFrame, Connection connection) {
        setTitle("学生概况");
        setSize(800, 500);

        // Adding window listener to capture window closing event
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                parentFrame.setVisible(true); // Show the parent frame
            }
        });

        parentFrame.setVisible(false); // Hide the parent frame

        // Creating table model and table component
        tableModel = new DefaultTableModel(new Object[][] {}, new Object[] { "学号", "姓名", "院系", "专业", "班级" });
        studentTable = new JTable(tableModel);

        // Adding table to scroll pane
        JScrollPane scrollPane = new JScrollPane(studentTable);

        // Adding scroll pane to the window
        add(scrollPane);

        // Creating a panel to place buttons
        JPanel buttonPanel = new JPanel();

        // Refresh button
        JButton refreshButton = new JButton("刷新");
        buttonPanel.add(refreshButton);

        JButton printTranscriptButton = new JButton("打印成绩单");
        buttonPanel.add(printTranscriptButton);

        JButton informationRequest = new JButton("信息查询");
        buttonPanel.add(informationRequest);

        // Adding button panel to the bottom of the window
        add(buttonPanel, BorderLayout.SOUTH);

        // <---------------------------Button
        // Listeners---------------------------------->

        // Refresh button
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshViewStudentTable(connection);
            }
        });

        printTranscriptButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                int selectedRow = studentTable.getSelectedRow();
                if (selectedRow != -1) {
                    try {
                        String selectedStudentSno = (String) tableModel.getValueAt(selectedRow, 0);

                        // 调用存储过程
                        callGetStudentGrades(selectedStudentSno, connection);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }

        });

        informationRequest.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // 显示选项对话框
                String[] options = { "学号查询", "姓名查询" };
                int queryType = JOptionPane.showOptionDialog(null, "请选择查询类型", "查询类型",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                        null, options, options[0]);

                // 根据用户选择执行相应的查询操作
                if (queryType == 0) {
                    // 学号查询
                    String studentSno = JOptionPane.showInputDialog(null, "请输入学号", "学号查询",
                            JOptionPane.QUESTION_MESSAGE);
                    if (studentSno != null && !studentSno.trim().isEmpty()) {
                        // showTranscriptBySno(studentSno, connection);
                        showGlobalBySno(studentSno, connection);
                    } else {
                        JOptionPane.showMessageDialog(null, "请输入有效的学号", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (queryType == 1) {
                    // 姓名查询
                    String studentName = JOptionPane.showInputDialog(null, "请输入姓名", "姓名查询",
                            JOptionPane.QUESTION_MESSAGE);
                    if (studentName != null && !studentName.trim().isEmpty()) {
                        // showTranscriptByName(studentName, connection);
                        showGlobalBySname(studentName, connection);
                    } else {
                        JOptionPane.showMessageDialog(null, "请输入有效的姓名", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

        });

        // <--------------------------------------------------------------------->

        // Display student information
        refreshViewStudentTable(connection);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Release window resources
        setLocationRelativeTo(null); // Display the window in the center of the screen
        setVisible(true); // Show the current window
    }

    // 刷新表格
    private void refreshViewStudentTable(Connection connection) {

        // 该语句是查询视图
        String viewStudentSql = "select * from view_student_info;";

        try (Statement statement = connection.createStatement()) {
            ResultSet viewStudentResultSet = statement.executeQuery(viewStudentSql);

            // 清空表格
            tableModel.setRowCount(0);


            while (viewStudentResultSet.next()) {
                String sno = viewStudentResultSet.getString("学号");
                String sname = viewStudentResultSet.getString("姓名");
                String dname = viewStudentResultSet.getString("院系");
                String mname = viewStudentResultSet.getString("专业");
                String cname = viewStudentResultSet.getString("班级");

                // 将数据填充进表格内
                tableModel.addRow(new Object[] { sno, sname, dname, mname, cname });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // private void showTranscript(String studentSno, Connection connection) {
    // // Show transcript for the selected student
    // String transcriptSql = "SELECT * FROM grade WHERE sno = '" + studentSno +
    // "'";

    // try (Statement statement = connection.createStatement()) {
    // ResultSet transcriptResultSet = statement.executeQuery(transcriptSql);

    // // Create a new JFrame to display the transcript
    // JFrame transcriptFrame = new JFrame("学生成绩单 - " + studentSno);
    // transcriptFrame.setSize(500, 300);

    // // Creating table model and table component for the transcript
    // DefaultTableModel transcriptTableModel = new DefaultTableModel(new Object[][]
    // {},
    // new Object[] { "学号", "课程编号", "教师编号", "成绩" });
    // JTable transcriptTable = new JTable(transcriptTableModel);

    // // Adding table to scroll pane
    // JScrollPane transcriptScrollPane = new JScrollPane(transcriptTable);

    // // Adding scroll pane to the window
    // transcriptFrame.add(transcriptScrollPane);

    // // Populate the transcript table
    // while (transcriptResultSet.next()) {
    // String kno = transcriptResultSet.getString("kno");
    // String tno = transcriptResultSet.getString("tno");
    // int grade = transcriptResultSet.getInt("grade");

    // // Add data to transcript table model
    // transcriptTableModel.addRow(new Object[] { studentSno, kno, tno, grade });
    // }

    // transcriptFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    // transcriptFrame.setLocationRelativeTo(this);
    // transcriptFrame.setVisible(true);

    // } catch (SQLException e) {
    // e.printStackTrace();
    // }
    // }

    // 学号查询（精确查询）
    private void showGlobalBySno(String studentSno, Connection connection) {

        String query = "SELECT * FROM view_student_info WHERE 学号 = '" + studentSno + "'";
        executeQueryAndShowResults(query, connection);
    }

    // 姓名查询（模糊查询）
    private void showGlobalBySname(String studentName, Connection connection) {

        String query = "SELECT * FROM view_student_info WHERE 姓名 LIKE '%" + studentName + "%'";
        executeQueryAndShowResults(query, connection);
    }

    // 通用方法执行查询并显示结果
    private void executeQueryAndShowResults(String query, Connection connection) {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);

            // 清空表格模型
            tableModel.setRowCount(0);

            // 处理查询结果，可以根据需要进一步操作
            while (resultSet.next()) {
                String sno = resultSet.getString("学号");
                String sname = resultSet.getString("姓名");
                String dname = resultSet.getString("院系");
                String mname = resultSet.getString("专业");
                String cname = resultSet.getString("班级");

                // 添加数据到表格模型
                tableModel.addRow(new Object[] { sno, sname, dname, mname, cname });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 处理异常
        }
    }

    private void callGetStudentGrades(String studentSno, Connection connection) throws SQLException {
        // 使用 CallableStatement 调用存储过程
        try (CallableStatement cs = connection.prepareCall("{call GetStudentGrades(?)}")) {
            cs.setString(1, studentSno);
            boolean hasResults = cs.execute();

            // 如果有结果集，处理结果集
            while (hasResults) {
                try (ResultSet rs = cs.getResultSet()) {
                    // 创建新窗口并显示结果
                    showGradeResultsInNewWindow(rs, studentSno);
                }
                hasResults = cs.getMoreResults();
            }
        }
    }

    // 创建新窗口并显示成绩结果
    private void showGradeResultsInNewWindow(ResultSet resultSet, String studentSno) throws SQLException {
        // 创建一个新的 JFrame
        JFrame resultFrame = new JFrame("学生成绩单 - " + studentSno);
        resultFrame.setSize(600, 400);

        // 创建表格模型和表格组件
        DefaultTableModel resultTableModel = new DefaultTableModel(new Object[][] {},
                new Object[] { "学号", "姓名", "课程名称", "教师姓名", "成绩" });
        JTable resultTable = new JTable(resultTableModel);

        // 添加表格到滚动窗格
        JScrollPane resultScrollPane = new JScrollPane(resultTable);

        // 添加滚动窗格到窗口
        resultFrame.add(resultScrollPane);

        // 处理查询结果，可以根据需要进一步操作
        while (resultSet.next()) {
            String sno = resultSet.getString("StudentID");
            String sname = resultSet.getString("StudentName");
            String kno = resultSet.getString("CourseName");
            String tname = resultSet.getString("TeacherName");
            int grade = resultSet.getInt("Grade");

            // 添加数据到表格模型
            resultTableModel.addRow(new Object[] { sno, sname, kno, tname, grade });
        }

        resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        resultFrame.setLocationRelativeTo(this);
        resultFrame.setVisible(true);
    }

}
