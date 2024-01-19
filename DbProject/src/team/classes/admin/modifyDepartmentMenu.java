/*
 * 实际上已经弃用这个文件
 * 所有的内容都在 adminMenu.java
 * 
*/





// package team.classes.admin;

// import java.awt.*;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
// import java.awt.event.WindowAdapter;

// import java.awt.event.WindowEvent;

// import javax.swing.*;
// import javax.swing.table.DefaultTableModel;

// import java.sql.*;

// import team.application.*;
// import team.classes.*;

// public class modifyDepartmentMenu extends JFrame {

//     private CardLayout cardLayout;
//     private JPanel cardPanel;

//     private DefaultTableModel tableModel;
//     private JTable departmentTable;

    

//     public modifyDepartmentMenu(adminMenu parentFrame, Connection connection) {



//         setTitle("修改院系信息");
//         setSize(800, 500);

//         // 添加窗口监听器，捕捉窗口关闭事件
//         addWindowListener(new WindowAdapter() {
//             @Override
//             public void windowClosing(WindowEvent e) {
//                 // 显示父窗口
//                 parentFrame.setVisible(true);
//             }
//         });

//         // 隐藏父窗口
//         parentFrame.setVisible(false);

//         String showDepartmentInfo = "select * from depart";
//         try {

//             Statement statement = connection.createStatement();

//             ResultSet departResultSet = statement.executeQuery(showDepartmentInfo);

//             // 创建表格模型和表格组件
//             tableModel = new DefaultTableModel(new Object[][] {}, new Object[] { "院系代码", "院系名称" });
//             departmentTable = new JTable(tableModel);

//             // 从 ResultSet 中将数据填充到表格模型
//             while (departResultSet.next()) {
//                 String departmentCode = departResultSet.getString("dno");
//                 String departmentName = departResultSet.getString("dname");
//                 tableModel.addRow(new Object[] { departmentCode, departmentName });
//             }

//             // 创建一个面板以容纳表格
//             JPanel viewDepartmentPanel = new JPanel(new BorderLayout());
//             viewDepartmentPanel.add(new JLabel("院系信息管理页面"), BorderLayout.NORTH);
//             viewDepartmentPanel.add(new JScrollPane(departmentTable), BorderLayout.CENTER);

//             // 创建按钮
//             JButton addDepartButton = new JButton("增加");
//             JButton deleteDepartButton = new JButton("删除");
//             JButton modifyDepartButton = new JButton("修改");

//             // 创建一个面板以容纳按钮
//             JPanel buttonPanel = new JPanel();
//             buttonPanel.add(addDepartButton);
//             buttonPanel.add(deleteDepartButton);
//             buttonPanel.add(modifyDepartButton);

//             viewDepartmentPanel.add(buttonPanel, BorderLayout.SOUTH);

//             // 添加按钮操作
//             addDepartButton.addActionListener(new ActionListener() {
//                 @Override
//                 public void actionPerformed(ActionEvent e) {
//                     // 创建一个面板来容纳文本框
//                     JPanel addDepartpanel = new JPanel();
//                     addDepartpanel.setLayout(new GridLayout(2, 2));

//                     // 创建文本框
//                     JTextField departmentCodeField = new JTextField();
//                     JTextField departmentNameField = new JTextField();

//                     // 将文本框添加到面板
//                     addDepartpanel.add(new JLabel("院系代码: "));
//                     addDepartpanel.add(departmentCodeField);
//                     addDepartpanel.add(new JLabel("院系名称: "));
//                     addDepartpanel.add(departmentNameField);

//                     // 弹出消息框
//                     int result = JOptionPane.showConfirmDialog(null, addDepartpanel, "请输入院系信息",
//                             JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

//                     // 如果用户点击了OK按钮
//                     if (result == JOptionPane.OK_OPTION) {
//                         // 获取用户输入的内容
//                         String departmentCode = departmentCodeField.getText();
//                         String departmentName = departmentNameField.getText();

//                         // 检查输入是否为空
//                         if (departmentCode.isEmpty() || departmentName.isEmpty()) {
//                             JOptionPane.showMessageDialog(null, "院系代码和院系名称不能为空");
//                             return; // 不执行插入数据库的操作
//                         }

//                         String insertDepartSql = "INSERT INTO depart (dno, dname) VALUES (?,?)";

//                         try {
//                             PreparedStatement insertDepartSqlex = connection.prepareStatement(insertDepartSql);
//                             insertDepartSqlex.setString(1, departmentCode);
//                             insertDepartSqlex.setString(2, departmentName);

//                             // 执行更新
//                             int rowsAffected = insertDepartSqlex.executeUpdate();

//                             if (rowsAffected > 0) {

//                                 JOptionPane.showMessageDialog(null, "成功插入数据到数据库");
//                             } else {

//                                 JOptionPane.showMessageDialog(null, "插入数据到数据库失败");
//                             }
//                         } catch (SQLException e1) {

//                             e1.printStackTrace();
//                         }

//                     } else {
//                         JOptionPane.showMessageDialog(null, "取消操作");
//                     }
//                 }
//             });

//             // 删除按钮操作
//             deleteDepartButton.addActionListener(new ActionListener() {
//                 @Override
//                 public void actionPerformed(ActionEvent e) {
//                     // 获取选中的行
//                     int selectedRow = departmentTable.getSelectedRow();

//                     // 检查是否有选中的行
//                     if (selectedRow == -1) {
//                         JOptionPane.showMessageDialog(null, "请先选中要删除的行");
//                         return;
//                     }

//                     // 获取选中行的数据
//                     String departmentCode = (String) tableModel.getValueAt(selectedRow, 0);
//                     // String departmentName = (String) tableModel.getValueAt(selectedRow, 1);

//                     // 弹出确认对话框
//                     int result = JOptionPane.showConfirmDialog(null, "确定删除选中的院系信息吗？", "确认删除",
//                             JOptionPane.YES_NO_OPTION);

//                     // 如果用户确认删除
//                     if (result == JOptionPane.YES_OPTION) {
//                         // 执行删除操作
//                         String deleteDepartSql = "DELETE FROM depart WHERE dno = ?";

//                         try {
//                             PreparedStatement deleteDepartSqlex = connection.prepareStatement(deleteDepartSql);

//                             // 设置参数
//                             deleteDepartSqlex.setString(1, departmentCode);

//                             // 执行更新
//                             int rowsAffected = deleteDepartSqlex.executeUpdate();

//                             if (rowsAffected > 0) {
//                                 JOptionPane.showMessageDialog(null, "成功删除选中的院系信息");
//                                 // 从表格模型中移除选中的行数据
//                                 tableModel.removeRow(selectedRow);
//                             } else {
//                                 JOptionPane.showMessageDialog(null, "删除院系信息失败");
//                             }
//                         } catch (SQLException e1) {
//                             e1.printStackTrace();
//                         }
//                     }
//                 }
//             });

//             // 修改按钮操作
//             modifyDepartButton.addActionListener(new ActionListener() {
//                 @Override
//                 public void actionPerformed(ActionEvent e) {
//                     // 创建一个面板来容纳文本框
//                     JPanel addDepartpanel = new JPanel();
//                     addDepartpanel.setLayout(new GridLayout(2, 2));

//                     // 创建文本框
//                     JTextField departmentCodeField = new JTextField();
//                     JTextField departmentNameField = new JTextField();

//                     // 将文本框添加到面板
//                     addDepartpanel.add(new JLabel("院系代码: "));
//                     addDepartpanel.add(departmentCodeField);
//                     addDepartpanel.add(new JLabel("院系名称: "));
//                     addDepartpanel.add(departmentNameField);

//                     // 获取选中的行
//                     int selectedRow = departmentTable.getSelectedRow();
//                     String originalDepartmentCode = (String) tableModel.getValueAt(selectedRow, 0);

//                     // 检查是否有选中的行
//                     if (selectedRow == -1) {
//                         JOptionPane.showMessageDialog(null, "请先选中要修改的行");
//                         return;
//                     }

//                     // 弹出消息框
//                     int result = JOptionPane.showConfirmDialog(null, addDepartpanel, "请输入院系信息",
//                             JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

//                     // 如果用户点击了OK按钮
//                     if (result == JOptionPane.OK_OPTION) {
//                         // 获取用户输入的内容
//                         String departmentCode = departmentCodeField.getText();
//                         String departmentName = departmentNameField.getText();

//                         // 检查输入是否为空
//                         if (departmentCode.isEmpty() || departmentName.isEmpty()) {
//                             JOptionPane.showMessageDialog(null, "院系代码和院系名称不能为空");
//                             return; // 不执行更新表的操作
//                         }

//                         String updateDepartSql = "UPDATE depart SET dno = ? , dname = ? WHERE dno = ?";

//                         try {
//                             PreparedStatement updateDepartSqlex = connection.prepareStatement(updateDepartSql);
//                             updateDepartSqlex.setString(1, departmentCode);
//                             updateDepartSqlex.setString(2, departmentName);
//                             updateDepartSqlex.setString(3, originalDepartmentCode);

//                             // 执行更新
//                             int rowsAffected = updateDepartSqlex.executeUpdate();

//                             if (rowsAffected > 0) {

//                                 JOptionPane.showMessageDialog(null, "成功修改数据");
//                             } else {

//                                 JOptionPane.showMessageDialog(null, "修改数据失败");
//                             }
//                         } catch (SQLException e1) {

//                             e1.printStackTrace();
//                         }

//                     } else {
//                         JOptionPane.showMessageDialog(null, "取消操作");
//                     }
//                 }

//             });

//             // 添加到卡片面板
//             //cardPanel.add(viewDepartmentPanel, "viewDepartmentPanel");

//             cardLayout.show(cardPanel, "viewDepartmentPanel");

//         } catch (SQLException e1) {

//             e1.printStackTrace();
//         }

//         // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//         /*
//          * 设置当窗口关闭时执行 DISPOSE_ON_CLOSE 操作
//          * 释放窗口资源而不是终止程序
//          */

//         setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

//         setLocationRelativeTo(null); // 将窗体显示在屏幕中央
//         setVisible(true); // 显示当前窗口
//     }
// }
