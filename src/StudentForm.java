import javax.swing.*;
import javax.swing.plaf.nimbus.State;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;


class Department {
    private String id;
    private String name;

    public Department(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + " (" + id + ")";
    }
}

public class StudentForm extends JFrame {
    // GUI Components
    private JTextField studentNumberField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField phoneNumberField;
    private JTextField birthdateField;
    private JTextField streetField;
    private JTextField zipcodeField;
    private JTextField departmentField;
    private JComboBox<Department> departmentComboBox;
    private JButton btnSubmit;
    private JButton btnView;
    private JButton btnSearch;
    private JButton btnDelete;
    static DatabaseConnection db;

    public StudentForm() {
        // Set up the frame
        setTitle("Student Information Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 450);
        setLocationRelativeTo(null);

        // Create a panel to hold the form components
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(10, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add labels and text fields for each student information
        panel.add(new JLabel("Student Number:"));
        studentNumberField = new JTextField(20);
        panel.add(studentNumberField);

        panel.add(new JLabel("First Name:"));
        firstNameField = new JTextField(20);
        panel.add(firstNameField);

        panel.add(new JLabel("Last Name:"));
        lastNameField = new JTextField(20);
        panel.add(lastNameField);

        panel.add(new JLabel("Phone Number:"));
        phoneNumberField = new JTextField(20);
        panel.add(phoneNumberField);

        panel.add(new JLabel("Birthdate:"));
        birthdateField = new JTextField(20);
        panel.add(birthdateField);

        panel.add(new JLabel("Street:"));
        streetField = new JTextField(20);
        panel.add(streetField);

        panel.add(new JLabel("Zipcode:"));
        zipcodeField = new JTextField(20);
        panel.add(zipcodeField);

        panel.add(new JLabel("Department:"));
        Department[] departments = {
                new Department("DCS", "Department of Computer Science"),
                new Department("DEE", "Department of Electrical Engineering")
                // Add more departments if needed
        };
        departmentComboBox = new JComboBox<>(departments);
        panel.add(departmentComboBox);

        // Create a button to submit the form
        btnSubmit = new JButton("Submit");
        submitInfo();

        // Add the submit button to the panel
        panel.add(btnSubmit);

        btnView = new JButton("View All");
        btnView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAll();
            }
        });
        panel.add(btnView);

        btnSearch = new JButton("Search");
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchStudent();
            }
        });
        panel.add(btnSearch);

        btnDelete = new JButton("Delete");
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteStudent();
            }
        });
        panel.add(btnDelete);


        // Add the panel to the frame
        add(panel);

        // Make the frame visible
        setVisible(true);
    }

    public void deleteStudent() {
        String ssn = JOptionPane.showInputDialog(this, "Please insert the ssn of the student that you would like to delete");
        try {
            PreparedStatement preparedStatement = db.getConnection().prepareStatement("DELETE FROM students WHERE ssn = ?");
            preparedStatement.setString(1, ssn);
            int successful = preparedStatement.executeUpdate();

            System.out.println(successful);
            String message = "";
            if(successful > 0) {
                message = "The student has been deleted!";
            } else {
                message = "Oppps, unable to delete the student!";
            }

            JOptionPane.showMessageDialog(this, message);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
    public void searchStudent() {
        String ssn = JOptionPane.showInputDialog(this, "Please insert the ssn of the student that you're looking for");

        try {
            PreparedStatement preparedStatement = db.getConnection().prepareStatement("SELECT ssn, firstname, lastname, deptname FROM students LEFT JOIN departments ON students.deptid = departments.deptid WHERE ssn = ?");

            preparedStatement.setString(1, ssn);
            ResultSet resultSet = preparedStatement.executeQuery();

            String message = "";
            if(resultSet.next()) {
                message += resultSet.getString("ssn") + " " + resultSet.getString("firstname") + " " + resultSet.getString("lastname") + " " + resultSet.getString("deptname");
            } else {
                message += "Student not found!";
            }

            JOptionPane.showMessageDialog(this, message);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void viewAll() {
        int count = 0;
        ArrayList<String> rowlist = new ArrayList<>();
        try {
            Statement statement = db.getConnection().createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT ssn, firstname, lastname, deptname FROM students LEFT JOIN departments ON students.deptid = departments.deptid");


            while (resultSet.next()){
                count++;
                String data = resultSet.getString("ssn") + " " + resultSet.getString("firstname") + " " + resultSet.getString("lastname") + " " + resultSet.getString("deptname");
                rowlist.add(data);
            }

            String message = "There is " + count + " students!";
            for(String row : rowlist) {
                message += "\n" + row;
            }

            JOptionPane.showMessageDialog(this, message);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void submitInfo() {
        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the student information from the text fields
                String studentNumber = studentNumberField.getText();
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String phoneNumber = phoneNumberField.getText();
                String birthdate = birthdateField.getText();
                String street = streetField.getText();
                String zipcode = zipcodeField.getText();
                Department selectedDepartment = (Department) departmentComboBox.getSelectedItem();
                String department = selectedDepartment.getId();

                // TODO: You can process the extracted information here, such as saving it to a database or displaying it.
                // For this example, we'll simply print the information to the console.
                System.out.println("Student Number: " + studentNumber);
                System.out.println("First Name: " + firstName);
                System.out.println("Last Name: " + lastName);
                System.out.println("Phone Number: " + phoneNumber);
                System.out.println("Birthdate: " + birthdate);
                System.out.println("Street: " + street);
                System.out.println("Zipcode: " + zipcode);
                System.out.println("Department: " + department);

                try {

                    PreparedStatement preparedStatement = db.getConnection().prepareStatement("INSERT INTO students VALUES (?,?,?,?,?,?,?,?)");
                    preparedStatement.setString(1, studentNumber);
                    preparedStatement.setString(2, firstName);
                    preparedStatement.setString(3, lastName);
                    preparedStatement.setString(4, phoneNumber);
                    preparedStatement.setString(5, birthdate);
                    preparedStatement.setString(6, street);
                    preparedStatement.setString(7, zipcode);
                    preparedStatement.setString(8, department);

                    preparedStatement.executeUpdate();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                db = new DatabaseConnection();
                new StudentForm();
            }
        });
    }
}