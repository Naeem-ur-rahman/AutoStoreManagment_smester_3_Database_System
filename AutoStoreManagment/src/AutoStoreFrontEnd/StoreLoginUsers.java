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
public class StoreLoginUsers extends javax.swing.JFrame {

    private SQLOracleConnection sql = new SQLOracleConnection();
    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;
    String user;
    String username ;
    String password ;
    int id;
    int eid;
    public class EmployeeItem {

        int id;
        String name;

        public EmployeeItem(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public String toString() {
            return name;
        }
    }
    /**
     * Creates new form StoreEmployee
     */
 
    /**
     * Creates new form StoreEmployee
     */
    public StoreLoginUsers() {
        initComponents();
        employeeName();
        table_update();
        txtUser.setText("Naeem Ur Rahman Sajid");
        resetValues();
        
    }
    public StoreLoginUsers(String user){
        initComponents();
        employeeName();
        table_update();
        this.user = user;
        txtUser.setText(user);
        resetValues();
    }
    
    private void employeeName() {
        
        try{
        con = sql.databaseConnection();
        pst = con.prepareStatement("select * from storeemp");
        rs = pst.executeQuery();
        cmbemployee.removeAllItems();
        while (rs.next()) {    
            cmbemployee.addItem(new EmployeeItem(rs.getInt("id"),rs.getString("Name")));
        }
        }catch(SQLException ex){
            Logger.getLogger(StoreLoginUsers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     private void table_update() {
        int c;
        try {
            con = sql.databaseConnection();
            pst = con.prepareStatement("select a.id,b.name,a.username,a.password from storeCashier a,storeemp b where a.eid = b.id");
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
                    v2.add(rs.getString("username"));
                    v2.add(rs.getString("password"));
                    
                }
                d.addRow(v2);
            }
        } catch ( SQLException ex) {
            Logger.getLogger(StoreLoginUsers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     private void tableSelect(){
         DefaultTableModel d1 = (DefaultTableModel) tableEmployee.getModel();
        int selectIndex = tableEmployee.getSelectedRow();
        txtId.setText(d1.getValueAt(selectIndex, 0).toString());
        cmbemployee.setSelectedIndex(-1);
        txtusername.setText(d1.getValueAt(selectIndex, 2).toString());
        txtpassword.setText(d1.getValueAt(selectIndex, 3).toString());
        txtpassword.grabFocus();
     }
    private boolean checkValues() {
        if (txtusername.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(StoreLoginUsers.this, "Enter Username", null, JOptionPane.ERROR_MESSAGE);
            txtusername.grabFocus();
            return false;
        }
        if (txtpassword.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(StoreLoginUsers.this, "Enter Password", null, JOptionPane.ERROR_MESSAGE);
            txtpassword.grabFocus();
            return false;
        }
        if (cmbemployee.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(StoreLoginUsers.this, "Select Employee Option", null, JOptionPane.ERROR_MESSAGE);
            cmbemployee.grabFocus();
            return false;
        }
        return true;
    }
     private void resetValues(){
         txtusername.setText("");
         txtpassword.setText("");
         txtId.setText("");
         cmbemployee.setSelectedIndex(-1);
         txtusername.grabFocus();
     }
    private void insert() {
        try {
            selectCashierId();
            id = id+1;
            con = sql.databaseConnection();
            username = txtusername.getText();
            password =  txtpassword.getText();
            EmployeeItem Employee_Id =  (EmployeeItem) cmbemployee.getSelectedItem();
            pst = con.prepareStatement("Insert into storeCashier (id,eid,username,password) values(?,?,?,?)");
            pst.setInt(1, id);
            pst.setInt(2,Employee_Id.id );
            pst.setString(3,username );
            pst.setString(4,password );
            rs = pst.executeQuery();
            resetValues();
            txtusername.grabFocus();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
   private void selectCashierId(){
        id = 0;
        int c;
        try{
        con = sql.databaseConnection();
        pst = con.prepareStatement("Select id from storeCashier");
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
    }

    private void delete(){
                DefaultTableModel d1 = (DefaultTableModel) tableEmployee.getModel();
        int selectIndex = tableEmployee.getSelectedRow();

        int Id = Integer.parseInt(d1.getValueAt(selectIndex, 0).toString());

        int ch = JOptionPane.showConfirmDialog(null, "Do you Want to delete LoginUser ?", "Warning", JOptionPane.YES_NO_OPTION);
        if (ch == JOptionPane.YES_OPTION) {
            try {
                con = sql.databaseConnection();
                pst = con.prepareStatement("delete from StoreCashier where Id= ?");

                pst.setInt(1, Id);
                pst.executeUpdate();

                JOptionPane.showMessageDialog(null, "LoginUser Deleted....!");
                table_update();
                resetValues();
            } catch ( SQLException ex) {
                Logger.getLogger(StoreLoginUsers.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private void edit(){
        try {
            tableSelect();
            username = txtusername.getText();
            password = txtpassword.getText();
            id = Integer.parseInt(txtId.getText());
            con = sql.databaseConnection();
            pst = con.prepareStatement("update storecashier set username = ?,password = ? where id = ?");
            pst.setString(1, username);
            pst.setString(2, password);
            pst.setInt(3,id);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "LoginUser Updated....!");
            resetValues();
            table_update();
        } catch (SQLException ex) {
            Logger.getLogger(StoreLoginUsers.class.getName()).log(Level.SEVERE, null, ex);
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
        txtusername = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnAdd = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        cmbemployee = new javax.swing.JComboBox();
        txtpassword = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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
                "ID", "EMPLOYEE NAME", "USERNAME", "PASSWORD"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableEmployee.getTableHeader().setReorderingAllowed(false);
        tableEmployee.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableEmployeeMouseClicked(evt);
            }
        });
        panelTable.setViewportView(tableEmployee);
        if (tableEmployee.getColumnModel().getColumnCount() > 0) {
            tableEmployee.getColumnModel().getColumn(0).setResizable(false);
            tableEmployee.getColumnModel().getColumn(1).setResizable(false);
            tableEmployee.getColumnModel().getColumn(2).setResizable(false);
        }

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

        txtusername.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtusername.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtusernameKeyPressed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("NAME:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setText("USERNAME:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setText("PASSWORD:");

        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        btnAdd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnAddKeyPressed(evt);
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
        jLabel7.setText("LOGIN USERS TABLE");

        cmbemployee.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbemployeeKeyPressed(evt);
            }
        });

        txtpassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtpasswordKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(panelSwictch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(96, 96, 96)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtUser, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(91, 91, 91))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4))
                                .addGap(46, 46, 46)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtusername)
                                    .addComponent(txtpassword, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(72, 72, 72)
                                .addComponent(jLabel1)
                                .addGap(46, 46, 46)
                                .addComponent(cmbemployee, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(110, 110, 110)
                                .addComponent(btnAdd)
                                .addGap(18, 18, 18)
                                .addComponent(btnEdit)
                                .addGap(18, 18, 18)
                                .addComponent(btnDelete)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtUser, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(59, 59, 59)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)
                        .addComponent(cmbemployee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtusername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(40, 40, 40))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAdd)
                            .addComponent(btnEdit)
                            .addComponent(btnDelete))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtpassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(panelSwictch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        if(checkValues()){
             insert();
             table_update();
        }
    }//GEN-LAST:event_btnAddActionPerformed

    private void txtusernameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtusernameKeyPressed
        // TODO add your handling code here:
         if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txtusername.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter Username", null, JOptionPane.ERROR_MESSAGE);
                txtusername.grabFocus();
            } else {
                txtpassword.grabFocus();
            }
        }
    }//GEN-LAST:event_txtusernameKeyPressed

    private void txtpasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtpasswordKeyPressed
        // TODO add your handling code here:
           if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txtpassword.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter Password", null, JOptionPane.ERROR_MESSAGE);
                txtpassword.grabFocus();
            } else {
                cmbemployee.grabFocus();
            }
        }
    }//GEN-LAST:event_txtpasswordKeyPressed

    private void cmbemployeeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbemployeeKeyPressed
        // TODO add your handling code here:
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (cmbemployee.getSelectedIndex()==-1) {
                JOptionPane.showMessageDialog(this, "Select Employee", null, JOptionPane.ERROR_MESSAGE);
                cmbemployee.grabFocus();
            } else {
     
            }
        }
    }//GEN-LAST:event_cmbemployeeKeyPressed

    private void btnAddKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnAddKeyPressed
        // TODO add your handling code here:
         if(evt.getKeyCode() == KeyEvent.VK_ENTER){
             if(checkValues()){
                 insert();
                 table_update();
             }
         }
    }//GEN-LAST:event_btnAddKeyPressed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        delete();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        edit();
    }//GEN-LAST:event_btnEditActionPerformed

    private void tableEmployeeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableEmployeeMouseClicked
        // TODO add your handling code here:
        tableSelect();
    }//GEN-LAST:event_tableEmployeeMouseClicked

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
            java.util.logging.Logger.getLogger(StoreLoginUsers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StoreLoginUsers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StoreLoginUsers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StoreLoginUsers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StoreLoginUsers().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JComboBox cmbemployee;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
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
    private javax.swing.JTextField txtId;
    private javax.swing.JLabel txtUser;
    private javax.swing.JPasswordField txtpassword;
    private javax.swing.JTextField txtusername;
    // End of variables declaration//GEN-END:variables
}
