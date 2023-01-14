/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AutoStoreFrontEnd;

import DatabaseConnection.SQLOracleConnection;
import com.sun.jmx.snmp.BerDecoder;
import java.awt.event.KeyEvent;
import java.awt.print.PrinterException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Naeem ur Rahman
 */
public class StoreOrder extends javax.swing.JFrame {

    /**
     * Creates new form StoreEmployee
     */
    String user;
    DefaultTableModel Model = new DefaultTableModel();
    private SQLOracleConnection sql = new SQLOracleConnection();
    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;
    private String Productcode;
    private String ProductTitle;
    private int ProductPrice;
    private int ProductQuantity;
    private int ProductRealQuantity;
    private int SubTotal;
    private int Pay;
    private int Balance;
    private int OId;

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    LocalDateTime now = LocalDateTime.now();

    Date d = new Date();
    SimpleDateFormat s = new SimpleDateFormat("hh-mm-ss a");

    public StoreOrder() {
        initComponents();
        txtUser.setText("Naeem Ur Rahman Sajid");

        txtSubTotal.setEditable(false);
        txtBalance.setEditable(false);
        txtPrice.setEditable(false);
        txtProductTitle.setEditable(false);
    }

    public StoreOrder(String user) {
        initComponents();
        this.user = user;
        txtUser.setText(user);
        txtSubTotal.setEditable(false);
        txtBalance.setEditable(false);
        txtPrice.setEditable(false);
        txtProductTitle.setEditable(false);
    }

    private int selectCashierId(String CashierUsername) {
        int n = 0;
        try {
            con = sql.databaseConnection();
            pst = con.prepareStatement("Select id from storeCashier where username = ?");
            pst.setString(1, CashierUsername);
            rs = pst.executeQuery();
            while (rs.next()) {

                n = rs.getInt("Id");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex);
        }
        return n;
    }

    private void selectOrderId() {
        OId = 0;
        int c;
        try {
            con = sql.databaseConnection();
            pst = con.prepareStatement("Select id from storeOrder");
            rs = pst.executeQuery();
            while (rs.next()) {
                c = rs.getInt("Id");
                if (c > OId) {
                    OId = c;
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex);
        }
    }

    private int selectProductId(String barcode) {
        int n = 0;
        try {
            con = sql.databaseConnection();
            pst = con.prepareStatement("Select pid from storeProduct where barcode = ?");
            pst.setString(1, barcode);
            rs = pst.executeQuery();
            while(rs.next()){
            n = rs.getInt("pid");
             return n;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex);
        }
        return n;
       
    }

    private void sales() {

        try {
            selectOrderId();
            int Cashier = selectCashierId(user);
            SubTotal = Integer.parseInt(txtSubTotal.getText());
            Pay = Integer.parseInt(txtPay.getText());
            Balance = Integer.parseInt(txtBalance.getText());
            OId = OId + 1;
            con = sql.databaseConnection();
            String q = "insert into storeOrder (id,Cid,Subtotal,Pay,Balance) values(?,?,?,?,?) ";
            pst = con.prepareStatement(q);
            pst.setInt(1, OId);
            pst.setInt(2, Cashier);
            pst.setInt(3, SubTotal);
            pst.setInt(4, Pay);
            pst.setInt(5, Balance);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Sales Record Generated at Id : " + OId);
            con = sql.databaseConnection();
            String query = "insert into StoreOrderLine (oid,pid,p_price,quantity,total) values(?,?,?,?,?) ";
            pst = con.prepareStatement(query);
            int Product_Id = 0;
            int Sell_Price = 0;
            int Quantity = 0;
            int Total = 0;
            System.out.println("insert");
            DefaultTableModel modelt = (DefaultTableModel) tableOrder.getModel();
            for (int i = 0; i < modelt.getRowCount(); i++) {
                System.out.println(i);
                Productcode = modelt.getValueAt(i, 0).toString();
                Sell_Price = Integer.parseInt( modelt.getValueAt(i, 2).toString());
                Quantity = Integer.parseInt( modelt.getValueAt(i, 3).toString());
                Total = Integer.parseInt( modelt.getValueAt(i, 4).toString());

                Product_Id = selectProductId(Productcode);
               
                System.out.println("insert end");

            }
            con = sql.databaseConnection();
            String qu = "update StoreProduct set Quantity = Quantity - ? where Barcode=?";
            pst = con.prepareStatement(qu);
            System.out.println("update");
            for (int i = 0; i <  modelt.getRowCount(); i++) {
                System.out.println(i);
                Productcode =  modelt.getValueAt(i, 0).toString();
                Quantity = Integer.parseInt( modelt.getValueAt(i, 3).toString());

                pst.setInt(1, Quantity);
                pst.setString(2, Productcode);
                pst.execute();

            }
            pst.addBatch();
            JOptionPane.showMessageDialog(this, "Record is Added....!");

            DefaultTableModel model = (DefaultTableModel) tableOrder.getModel();
            model.setRowCount(0);
            txtProductCode.setText("");
            txtProductTitle.setText("");
            txtPrice.setText("");
            txtQuantity.setText("");
            txtSubTotal.setText("");
            txtPay.setText("");
            txtBalance.setText("");
            txtProductCode.requestFocus();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex);
        }

    }

    private void order() {
        Productcode = txtProductCode.getText();
        try {

            con = sql.databaseConnection();
            pst = con.prepareStatement("select * from Storeproduct where Barcode = ?");
            pst.setString(1, Productcode);
            rs = pst.executeQuery();

            while (rs.next()) {

                int currentQuantity = rs.getInt("Quantity");
                int Price = Integer.parseInt(txtPrice.getText());
                int demandQuantity = Integer.parseInt(txtQuantity.getText());

                long Total = Price * demandQuantity;
                if (demandQuantity > currentQuantity) {
                    JOptionPane.showMessageDialog(this, "Available : " + currentQuantity, "Products", JOptionPane.INFORMATION_MESSAGE);
                    JOptionPane.showMessageDialog(this, "Available Product are not enough !", "Erorr", JOptionPane.ERROR_MESSAGE);

                    if (currentQuantity == 0) {
                        txtQuantity.setText("");
                        txtPrice.setText("");
                        txtProductTitle.setText("");
                        txtProductCode.setText("");
                        txtProductCode.requestFocus();
                    } else {
                        txtQuantity.requestFocus();
                    }
                } else {
                    Model = (DefaultTableModel) tableOrder.getModel();
                    Model.addRow(new Object[]{
                        txtProductCode.getText(),
                        txtProductTitle.getText(),
                        txtPrice.getText(),
                        txtQuantity.getText(),
                        Total,});
                    long SubTotal = 0;
                    for (int i = 0; i < tableOrder.getRowCount(); i++) {

                        SubTotal += Integer.parseInt(tableOrder.getValueAt(i, 4).toString());
                    }
                    txtSubTotal.setText(Long.toString(SubTotal));
                    txtProductCode.setText("");
                    txtProductTitle.setText("");
                    txtPrice.setText("");
                    txtQuantity.setText("");
                    txtProductCode.requestFocus();

                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex);
        }
    }

    public void print() {

        String sub = txtSubTotal.getText();
        String pay = txtPay.getText();
        String bal = txtBalance.getText();
        String Date = dtf.format(now);
        String Time = s.format(d);
        String Cashier = user;
        try {
            new Print(sub, pay, bal, Date, Time, Cashier, tableOrder.getModel()).setVisible(true);
        } catch (PrinterException ex) {
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
        tableOrder = new javax.swing.JTable();
        txtProductCode = new javax.swing.JTextField();
        txtProductTitle = new javax.swing.JTextField();
        txtPrice = new javax.swing.JTextField();
        txtQuantity = new javax.swing.JTextField();
        txtSubTotal = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btnAdd = new javax.swing.JButton();
        btnPay = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        txtPay = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtBalance = new javax.swing.JTextField();

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

        tableOrder.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "PRODUCT CODE", "TITLE", "PRICE", "QUANTITY", "TOTAL"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableOrder.getTableHeader().setReorderingAllowed(false);
        panelTable.setViewportView(tableOrder);

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

        txtProductCode.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtProductCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtProductCodeKeyPressed(evt);
            }
        });

        txtProductTitle.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        txtPrice.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        txtQuantity.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtQuantity.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtQuantityKeyPressed(evt);
            }
        });

        txtSubTotal.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("PRODUCT CODE:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setText("PRODUCT TITLE:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setText("PRICE:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setText("QUANTITY:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel5.setText("SUBTOTAL:");

        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnPay.setText("PayInvoice");
        btnPay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPayActionPerformed(evt);
            }
        });

        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel6.setText("PAY:");

        txtPay.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtPay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPayActionPerformed(evt);
            }
        });
        txtPay.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPayKeyPressed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Viner Hand ITC", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 102, 102));
        jLabel7.setText("ORDER DATA PAGE");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel8.setText("BALANCE:");

        txtBalance.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtBalance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBalanceActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(panelSwictch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtUser, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(106, 106, 106))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(21, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2))
                                .addGap(46, 46, 46)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtProductCode, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                                    .addComponent(txtProductTitle))
                                .addGap(25, 25, 25)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtPrice, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                                    .addComponent(txtQuantity)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(jLabel5)
                                .addGap(18, 18, 18)
                                .addComponent(txtSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(28, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(123, 123, 123)
                        .addComponent(btnAdd)
                        .addGap(43, 43, 43)
                        .addComponent(btnDelete)
                        .addGap(33, 33, 33)
                        .addComponent(btnPay)
                        .addGap(98, 98, 98)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel6))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtBalance)
                            .addComponent(txtPay))
                        .addGap(34, 34, 34))))
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtProductTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))))
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtPay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdd)
                    .addComponent(btnPay)
                    .addComponent(btnDelete)
                    .addComponent(jLabel8)
                    .addComponent(txtBalance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(32, Short.MAX_VALUE))
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

    private void txtPayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPayActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPayActionPerformed

    private void txtBalanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBalanceActionPerformed

    }//GEN-LAST:event_txtBalanceActionPerformed

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

    private void txtProductCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProductCodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            Productcode = txtProductCode.getText().trim().toUpperCase();
            try {

                con = sql.databaseConnection();
                pst = con.prepareStatement("select * from storeproduct where Barcode = ?");
                pst.setString(1, Productcode);
                rs = pst.executeQuery();

                if (rs.next() == false) {
                    JOptionPane.showMessageDialog(this, "Barcode Not Found", "Warning", JOptionPane.WARNING_MESSAGE);
                    txtProductTitle.setText("");
                    txtPrice.setText("");
                } else {
                    String ProductName = rs.getString("title");
                    String Price = rs.getString("RetailPrice");

                    txtProductTitle.setText(ProductName.trim());
                    txtPrice.setText(Price.trim());
                    txtQuantity.requestFocus();
                }
            } catch (SQLException ex) {

                System.out.println(ex);
            }

        }
    }//GEN-LAST:event_txtProductCodeKeyPressed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:

        Model.removeRow(tableOrder.getSelectedRow());
        long SubTotal = 0;
        for (int i = 0; i < tableOrder.getRowCount(); i++) {

            SubTotal += Integer.parseInt(tableOrder.getValueAt(i, 4).toString());
        }
        txtSubTotal.setText(Long.toString(SubTotal));
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void txtQuantityKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQuantityKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txtQuantity.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter the Quantity", null, JOptionPane.WARNING_MESSAGE);
                txtQuantity.grabFocus();
            } else {
                order();
            }
        }
    }//GEN-LAST:event_txtQuantityKeyPressed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        order();
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnPayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPayActionPerformed
        // TODO add your handling code here:
        Pay = Integer.parseInt(txtPay.getText());
        SubTotal = Integer.parseInt(txtSubTotal.getText());
        Balance = Pay - SubTotal;
        txtBalance.setText(String.valueOf(Balance));
        print();
        sales();
    }//GEN-LAST:event_btnPayActionPerformed

    private void txtPayKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPayKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txtPay.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter the Pay Amount", null, JOptionPane.WARNING_MESSAGE);
                txtPay.grabFocus();
            } else {
                Pay = Integer.parseInt(txtPay.getText());
                SubTotal = Integer.parseInt(txtSubTotal.getText());
                Balance = Pay - SubTotal;
                txtBalance.setText(String.valueOf(Balance));
                print();
                sales();
            }
        }
    }//GEN-LAST:event_txtPayKeyPressed

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
            java.util.logging.Logger.getLogger(StoreOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StoreOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StoreOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StoreOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StoreOrder().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnPay;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
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
    private javax.swing.JTable tableOrder;
    private javax.swing.JTextField txtBalance;
    private javax.swing.JTextField txtPay;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JTextField txtProductCode;
    private javax.swing.JTextField txtProductTitle;
    private javax.swing.JTextField txtQuantity;
    private javax.swing.JTextField txtSubTotal;
    private javax.swing.JLabel txtUser;
    // End of variables declaration//GEN-END:variables
}
