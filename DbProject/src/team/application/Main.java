
package team.application;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.sql.*;

import team.classes.*;
import team.classes.admin.*;

class loginCheckFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    // Connection connection = null;

    public loginCheckFrame() {
        setTitle("欢迎登录学籍管理系统");
        setSize(800, 500);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel leftPanel = new JPanel(new BorderLayout());
        Font font = new Font("微软雅黑", Font.BOLD, 32); // 设置字体
        JLabel titleLabel = new JLabel("学籍管理系统", SwingConstants.CENTER);
        titleLabel.setFont(font);

        leftPanel.add(titleLabel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel usernameLabel = new JLabel("用户名:");
        usernameField = new JTextField();
        usernameField.setColumns(15); // 设置长度

        JLabel passwordLabel = new JLabel("密码:");
        passwordField = new JPasswordField();

        /*
         * 由于两个输入框的网格控制是一体的
         * 修改其中一个，另外一个也会被改变
         */

        // 用户标签
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 0, 5, 10);
        rightPanel.add(usernameLabel, gbc);

        // 用户输入框
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 5, 0);
        rightPanel.add(usernameField, gbc);

        // 密码标签
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 0, 0, 10);
        rightPanel.add(passwordLabel, gbc);

        // 密码输入框
        // gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 0);
        rightPanel.add(passwordField, gbc);

        JButton loginButton = new JButton("登录");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticateUser(usernameField.getText(), new String(passwordField.getPassword()));
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        rightPanel.add(loginButton, gbc);

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        add(mainPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 将窗体显示在屏幕中央
        setVisible(true);
    }

    private void authenticateUser(String username, String password) {

        Connection connection = null;
        try {

            // 获取数据库连接
            String url = "jdbc:mysql://your_ip:3306/school_roll_management_system?characterEncoding=utf8&useSSL=false";
            String user = "root";
            String passwd = "root";

            // 创建连接
            connection = DriverManager.getConnection(url, user, passwd);
            String query = "SELECT * FROM users WHERE username=? AND password=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {

                    if (resultSet.next()) {
                        JOptionPane.showMessageDialog(this, "登录成功");

                        // String identification = "SELECT role FROM users WHERE username=?";

                        String identification = resultSet.getString("role");

                        switch (identification) {
                            case "admin":
                                new adminMenu(connection);
                                break;
                            case "teacher":
                                String teacherUsername = resultSet.getString("username");
                                new teacherMenu(teacherUsername,connection);
                                break;
                            default:
                                String studentUsername = resultSet.getString("username");
                                new stuMenu(studentUsername,connection);
                                break;
                        }

                        this.setVisible(false); // 隐藏本页面

                    } else {
                        JOptionPane.showMessageDialog(this, "登录失败，请检查您的凭据。");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

public class Main {

    public static void main(String[] args) throws ClassNotFoundException {

        // 注册数据库驱动
        Class.forName("com.mysql.cj.jdbc.Driver");

        new loginCheckFrame();

    }
}
