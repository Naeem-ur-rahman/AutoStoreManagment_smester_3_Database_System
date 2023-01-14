/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AutoStoreFrontEnd;

import DatabaseConnection.SQLOracleConnection;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Naeem ur Rahman
 */
public class StoreEmployee extends javax.swing.JFrame {

    /**
     * Creates new form StoreEmployee
     */
    String user;
    private SQLOracleConnection sql = new SQLOracleConnection();
    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;
    private String Name;
    private String City;
    private String Post;
    private int Salary;
    private int id;
    private int Age;

    public StoreEmployee() {
        initComponents();
        txtUser.setText("Naeem Ur Rahman Sajid");
    }

    public StoreEmployee(String user) {
        initComponents();
        this.user = user;
        txtUser.setText(user);
        table_update();
        resetValues();
    }

    private boolean checkValues() {
        if (txtName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(StoreEmployee.this, "Enter Name", null, JOptionPane.ERROR_MESSAGE);
            txtName.grabFocus();
            return false;
        }
        if (txtCity.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(StoreEmployee.this, "Enter City", null, JOptionPane.ERROR_MESSAGE);
            txtCity.grabFocus();
            return false;
        }
        if (txtPost.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(StoreEmployee.this, "Enter Post", null, JOptionPane.ERROR_MESSAGE);
            txtPost.grabFocus();
            return false;
        }
        if (txtAge.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(StoreEmployee.this, "Enter Age", null, JOptionPane.ERROR_MESSAGE);
            txtAge.grabFocus();
            return false;
        }
        if (txtSalary.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(StoreEmployee.this, "Enter Salary", null, JOptionPane.ERROR_MESSAGE);
            txtSalary.grabFocus();
            return false;
        }
        return true;
    }

    private void resetValues() {
        txtName.setText("");
        txtAge.setText("");
        txtCity.setText("");
        txtPost.setText("");
        txtId.setText("");
        txtSalary.setText("");
        txtName.grabFocus();
    }

    private void table_update() {
        int c;
        try {
            con = sql.databaseConnection();
            pst = con.prepareStatement("select * from Storeemp");
            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rsd = (ResultSetMetaData) rs.getMetaData();
            c = rsd.getColumnCount();
            DefaultTableModel d = (DefaultTableModel) tableEmployee.getModel();
            d.setRowCount(0);

            while (rs.next()) {

                Vector v2 = new Vector();
                for (int i = 1; i <= c; i++) {
                    v2.add(rs.getInt("Id"));
                    v2.add(rs.getString("Name"));
                    v2.add(rs.getString("City"));
                    v2.add(rs.getString("Post"));
                    v2.add(rs.getString("Age"));
                    v2.add(rs.getString("Salary"));
                }
                d.addRow(v2);
            }
        } catch (SQLException ex) {
            Logger.getLogger(StoreLoginUsers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void tableSelect() {
        DefaultTableModel d1 = (DefaultTableModel) tableEmployee.getModel();
        int selectIndex = tableEmployee.getSelectedRow();
        txtId.setText(d1.getValueAt(selectIndex, 0).toString());
        txtName.setText(d1.getValueAt(selectIndex, 1).toString());
        txtCity.setText(d1.getValueAt(selectIndex, 2).toString());
        txtPost.setText(d1.getValueAt(selectIndex, 3).toString());
        txtAge.setText(d1.getValueAt(selectIndex, 4).toString());
        txtSalary.setText(d1.getValueAt(selectIndex, 5).toString());
        txtName.grabFocus();
    }
    /* private void selectEmployeeId(){
     id = 0;
     int c;
     try{
     con = sql.databaseConnection();
     pst = con.prepareStatement("Select id from storeemp");
     rs = pst.executeQuery();
     while(rs.next()){
     c = rs.getInt("Id");
     if(c>id){
     id = c;
     }
     }
     }
     catch(SQLException ex){
     System.out.println(ex);
     }
     }*/

    private void insert() {
        try {
            con = sql.databaseConnection();
            Name = txtName.getText().toUpperCase();
            City = txtCity.getText().toUpperCase();
            Post = txtPost.getText().toUpperCase();
            Age = Integer.parseInt(txtAge.getText());
            Salary = Integer.parseInt(txtSalary.getText());
            pst = con.prepareStatement("Insert into storeemp (Name,City,Post,Age,Salary) values(?,?,?,?,?)");
            pst.setString(1, Name);
            pst.setString(2, City);
            pst.setString(3, Post);
            pst.setInt(4, Age);
            pst.setInt(5, Salary);
            rs = pst.executeQuery();
            JOptionPane.showMessageDialog(this, "Employee Added");
            table_update();
            resetValues();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex);
            JOptionPane.showMessageDialog(this, "Employee not Added", "Error", JOptionPane.ERROR_MESSAGE);
            txtName.grabFocus();
        }
    }
    private void deleteCashiers(int employeeid){
         try {
                con = sql.databaseConnection();
                pst = con.prepareStatement("delete from StoreCashier where eid = ?");
                
                pst.setInt(1, employeeid);
                pst.executeUpdate();
                
                JOptionPane.showMessageDialog(null, "LoginUser Deleted....!");
                table_update();
                resetValues();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, ex);
                JOptionPane.showMessageDialog(this, "Employee not Deleted","Error",JOptionPane.ERROR_MESSAGE);
            }
    }
    private void delete() {
        DefaultTableModel d1 = (DefaultTableModel) tableEmployee.getModel();
        int selectIndex = tableEmployee.getSelectedRow();
        
        int Id = Integer.parseInt(d1.getValueAt(selectIndex, 0).toString());

        int ch = JOptionPane.showConfirmDialog(null, "Do you Want to delete LoginUser ?", "Warning", JOptionPane.YES_NO_OPTION);
        if (ch == JOptionPane.YES_OPTION) {
            deleteCashiers(Id);
            
            try {
                con = sql.databaseConnection();
                pst = con.prepareStatement("delete from Storeemp where Id= ?");
                pst.setInt(1, Id);
                pst.executeUpdate();
                
                JOptionPane.showMessageDialog(null, "Employee Deleted....!");
                table_update();
                resetValues();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, ex);
                JOptionPane.showMessageDialog(this, "Employee not Deleted","Error",JOptionPane.ERROR_MESSAGE);
                txtName.grabFocus();
            }
        }
    }
        private void edit(){
        try {
            tableSelect();
            Age = Integer.parseInt(txtAge.getText());
            City = txtCity.getText();
            id = Integer.parseInt(txtId.getText());
            Post = txtPost.getText();
            Name = txtName.getText();
            Salary  = Integer.parseInt(txtSalary.getText());
            con = sql.databaseConnection();
            pst = con.prepareStatement("update storeemp set Name = ?,City = ? , Age = ? ,Salary = ? ,Post = ? where id = ?");
            pst.setString(1, Name);
            pst.setString(2, City);
            pst.setInt(3, Age);
            pst.setInt(4, Salary);
            pst.setString(5, Post);
            pst.setInt(6,id);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Employee Updated....!");
            resetValues();
            table_update();
        } catch (SQLException ex) {
           JOptionPane.showMessageDialog(this, ex);
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtUser = new javax.swing.JLabel();
        panelSwictch = new javax.swing.JPanel();
        labelProducts = new javax.swing.JLabel();
        labelEmployee = new javax.swing.JLabel();
        labelLoginusers = new javax.swing.JLabel();
        labelOrder = new javax.swing.JLabel();
        labelCatagory = new javax.swing.JLabel();
        labelBrand = new javax.swing.JLabel();
        labelSalesRecord = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        panelTable = new javax.swing.JScrollPane();
        tableEmployee = new javax.swing.JTable();
        txtName = new javax.swing.JTextField();
        txtCity = new javax.swing.JTextField();
        txtPost = new javax.swing.JTextField();
        txtAge = new javax.swing.JTextField();
        txtSalary = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btnAdd = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        txtUser.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtUser.setForeground(new java.awt.Color(51, 51, 51));
        txtUser.setText("Name");

        panelSwictch.setBackground(new java.awt.Color(0, 102, 102));
        panelSwictch.setForeground(new java.awt.Color(0, 102, 102));

        labelProducts.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        labelProducts.setForeground(new java.awt.Color(255, 255, 255));
        labelProducts.setText("PRODUCTS");
        labelProducts.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelProductsMouseClicked(evt);
            }
        });

        labelEmployee.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        labelEmployee.setForeground(new java.awt.Color(255, 255, 255));
        labelEmployee.setText("EMPLOYEE ");
        labelEmployee.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelEmployeeMouseClicked(evt);
            }
        });

        labelLoginusers.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        labelLoginusers.setForeground(new java.awt.Color(255, 255, 255));
        labelLoginusers.setText("LOGIN USER");
        labelLoginusers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelLoginusersMouseClicked(evt);
            }
        });

        labelOrder.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        labelOrder.setForeground(new java.awt.Color(255, 255, 255));
        labelOrder.setText("ORDER");
        labelOrder.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelOrderMouseClicked(evt);
            }
        });

        labelCatagory.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        labelCatagory.setForeground(new java.awt.Color(255, 255, 255));
        labelCatagory.setText("CATAGORY");
        labelCatagory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelCatagoryMouseClicked(evt);
            }
        });

        labelBrand.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        labelBrand.setForeground(new java.awt.Color(255, 255, 255));
        labelBrand.setText("BRAND");
        labelBrand.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelBrandMouseClicked(evt);
            }
        });

        labelSalesRecord.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        labelSalesRecord.setForeground(new java.awt.Color(255, 255, 255));
        labelSalesRecord.setText("SALES RECORD");
        labelSalesRecord.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelSalesRecordMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelSwictchLayout = new javax.swing.GroupLayout(panelSwictch);
        panelSwictch.setLayout(panelSwictchLayout);
        panelSwictchLayout.setHorizontalGroup(
            panelSwictchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSwictchLayout.createSequentialGroup()
                .addGroup(panelSwictchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelSwictchLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(labelSalesRecord))
                    .addGroup(panelSwictchLayout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(panelSwictchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelProducts, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelSwictchLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(labelBrand, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(labelEmployee)))
                    .addGroup(panelSwictchLayout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(panelSwictchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelLoginusers)
                            .addComponent(labelCatagory, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(40, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelSwictchLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(labelOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
        );
        panelSwictchLayout.setVerticalGroup(
            panelSwictchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSwictchLayout.createSequentialGroup()
                .addGap(72, 72, 72)
                .addComponent(labelEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelProducts, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(labelLoginusers, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(labelOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelCatagory, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelBrand, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelSalesRecord, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tableEmployee.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "NAME", "CITY", "POST", "AGE", "SALARY"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Long.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tableEmployee.getTableHeader().setReorderingAllowed(false);
        tableEmployee.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableEmployeeMouseClicked(evt);
            }
        });
        panelTable.setViewportView(tableEmployee);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelTable, javax.swing.GroupLayout.DEFAULT_SIZE, 802, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelTable, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        txtName.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNameKeyPressed(evt);
            }
        });

        txtCity.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtCity.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCityKeyPressed(evt);
            }
        });

        txtPost.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtPost.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPostKeyPressed(evt);
            }
        });

        txtAge.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtAge.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAgeKeyPressed(evt);
            }
        });

        txtSalary.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("NAME:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setText("CITY:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setText("POST:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setText("AGE:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel5.setText("SALARY:");

        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnEdit.setText("Edit");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel6.setText("ID:");

        txtId.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Viner Hand ITC", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 102, 102));
        jLabel7.setText("EMPLOYEE DATA PAGE");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(panelSwictch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(46, 46, 46)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtName)
                            .addComponent(txtCity)
                            .addComponent(txtPost)
                            .addComponent(txtAge)
                            .addComponent(txtSalary, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnAdd)
                                .addGap(18, 18, 18)
                                .addComponent(btnEdit)
                                .addGap(18, 18, 18)
                                .addComponent(btnDelete))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(101, 101, 101)
                                .addComponent(jLabel6)
                                .addGap(32, 32, 32)
                                .addComponent(txtId)))
                        .addGap(113, 113, 113))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtUser, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(91, 91, 91)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelSwictch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtUser, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel6)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSalary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(btnAdd)
                    .addComponent(btnEdit)
                    .addComponent(btnDelete))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void labelEmployeeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelEmployeeMouseClicked
        // TODO add your handling code here:
        new StoreEmployee(user).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_labelEmployeeMouseClicked

    private void labelLoginusersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelLoginusersMouseClicked
        // TODO add your handling code here:
        new StoreLoginUsers(user).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_labelLoginusersMouseClicked

    private void labelProductsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelProductsMouseClicked
        // TODO add your handling code here:
        new StoreProduct(user).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_labelProductsMouseClicked

    private void labelOrderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelOrderMouseClicked
        // TODO add your handling code here:
        new StoreOrder(user).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_labelOrderMouseClicked

    private void labelCatagoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelCatagoryMouseClicked
        // TODO add your handling code here:
        new StoreCatogotyBrand(user).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_labelCatagoryMouseClicked

    private void labelBrandMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelBrandMouseClicked
        // TODO add your handling code here:
        new StoreCatogotyBrand(user).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_labelBrandMouseClicked

    private void labelSalesRecordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelSalesRecordMouseClicked
        // TODO add your handling code here:
        new StoreSalesRecord(user).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_labelSalesRecordMouseClicked

    private void txtNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNameKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txtName.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter Name", null, JOptionPane.ERROR_MESSAGE);
                txtName.grabFocus();
            } else {
                txtCity.grabFocus();
            }
        }
    }//GEN-LAST:event_txtNameKeyPressed

    private void txtCityKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCityKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txtCity.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter City", null, JOptionPane.ERROR_MESSAGE);
                txtCity.grabFocus();
            } else {
                txtPost.grabFocus();
            }
        }
    }//GEN-LAST:event_txtCityKeyPressed

    private void txtPostKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPostKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txtPost.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter Post", null, JOptionPane.ERROR_MESSAGE);
                txtPost.grabFocus();
            } else {
                txtAge.grabFocus();
            }
        }
    }//GEN-LAST:event_txtPostKeyPressed

    private void txtAgeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAgeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txtAge.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter Age", null, JOptionPane.ERROR_MESSAGE);
                txtAge.grabFocus();
            } else {
                txtSalary.grabFocus();
            }
        }
    }//GEN-LAST:event_txtAgeKeyPressed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        if (checkValues()) {
            insert();
        }
    }//GEN-LAST:event_btnAddActionPerformed

    private void tableEmployeeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableEmployeeMouseClicked
        // TODO add your handling code here:
        tableSelect();
    }//GEN-LAST:event_tableEmployeeMouseClicked

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowClosed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        delete();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        edit();
    }//GEN-LAST:event_btnEditActionPerformed
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(StoreEmployee.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StoreEmployee.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StoreEmployee.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StoreEmployee.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StoreEmployee().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel labelBrand;
    private javax.swing.JLabel labelCatagory;
    private javax.swing.JLabel labelEmployee;
    private javax.swing.JLabel labelLoginusers;
    private javax.swing.JLabel labelOrder;
    private javax.swing.JLabel labelProducts;
    private javax.swing.JLabel labelSalesRecord;
    private javax.swing.JPanel panelSwictch;
    private javax.swing.JScrollPane panelTable;
    private javax.swing.JTable tableEmployee;
    private javax.swing.JTextField txtAge;
    private javax.swing.JTextField txtCity;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPost;
    private javax.swing.JTextField txtSalary;
    private javax.swing.JLabel txtUser;
    // End of variables declaration//GEN-END:variables
}
