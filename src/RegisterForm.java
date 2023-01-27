import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.registry.Registry;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class RegisterForm extends JDialog {
    private JTextField tfPhone;
    private JTextField tfEmail;
    private JTextField tfName;
    private JTextField tfAddress;
    private JPasswordField pfPassword;
    private JPasswordField pfConfirmPassword;
    private JButton btnRegister;
    private JButton btnCancel;
    private JPanel registerPanel;

    public RegisterForm(JFrame parent){
        super(parent);
        setTitle("Create a new account");
        setContentPane(registerPanel);
        setMinimumSize(new Dimension(450,474));
        setModal(true);
        setLocationRelativeTo(parent);


        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();  // close
            }
        });
        setVisible(true);
    }

    private void registerUser() {
        String name = tfName.getText();
        String email = tfEmail.getText();
        String phone = tfPhone.getText();
        String address = tfAddress.getText();
        String password = String.valueOf(pfPassword.getPassword());
        String confirmPassword = String.valueOf(pfConfirmPassword.getPassword());


        if(name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || password.isEmpty()){
            JOptionPane.showMessageDialog(this,"Please enter all fileds", "Try again",JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(!password.equals(confirmPassword)){
            JOptionPane.showMessageDialog(this,"Confirm Pasword does not match", "Try again",JOptionPane.ERROR_MESSAGE);
            return;
        }


        // if failed to register new user
        if( user != null){
             final String DB_URL = "jdbc:mysql://localhost/MyStore?serverTimezone=UTC";
             final String USERNAME = "root  ";
             final String PASSWORD =" ";

            dispose();
        }else{
            JOptionPane.showMessageDialog(this,"Failed to register new user", "Try again",JOptionPane.ERROR_MESSAGE);
        }
    }



    public User user;    //
    private User addUserToDatabase(String name, String email, String phone, String address, String password) {  // this void change to Public User
        User user = null;
        final String DB_URL ="jdbc:mysql://localhost/MyStore?serverTimezone=UTC";
        final String USERNAME = "root";
        final String PASSWORD = " ";

        try{
            Connection conn= DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);

            Statement stmt = conn.createStatement();
            String sql ="INSERT INTO users( name, email,phone, address,password)" + "VALUES(?,?,?,?,?)";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,phone);
            preparedStatement.setString(3,phone);
            preparedStatement.setString(4,address);
            preparedStatement.setString(5,password);


            //insert row into the table
            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0){
                user = new User();
                user.name = name;
                user.email = email;
                user.phone = phone;
                user.address = address;
                user.password = password;

            }


        }catch(Exception e){
            e.printStackTrace();
        }


        return user;
    }


    public static void main(String[] args) {

        RegisterForm myForm  = new RegisterForm(null);

        User user = myForm.user;
        if(user != null){
            System.out.println("Successful registration of:" +  user.name);
        }
        else {
            System.out.println("Registration canceled");
        }

    }
}
