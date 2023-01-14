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
public class StoreProduct extends javax.swing.JFrame {

    private SQLOracleConnection sql = new SQLOracleConnection();
    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;
    private String Title;
    private String Barcode;
    private int Id;
    private int RetailPrice;
    private int CostPrice;
    private int Quantity;
    private int CatId;
    private int Bid;
    /**
     * Creates new form StoreEmployee
     */
    String user;

    public StoreProduct() {
        initComponents();
        txtUser.setText("Naeem Ur Rahman Sajid");
        CatagoryName();
        BrandName();
        resetValues();
        table_update();
    }

    public StoreProduct(String user) {
        initComponents();
        this.user = user;
        txtUser.setText(user);
        CatagoryName();
        BrandName();
        resetValues();
        table_update();
    }

    private boolean checkValues() {
        if (txtTitle.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Title", null, JOptionPane.ERROR_MESSAGE);
            txtTitle.grabFocus();
            return false;
        }
        if (txtRetailPrice.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Retail Price", null, JOptionPane.ERROR_MESSAGE);
            txtRetailPrice.grabFocus();

            return false;
        }
        if (txtCostPrice.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Cost Price", null, JOptionPane.ERROR_MESSAGE);
            txtCostPrice.grabFocus();

            return false;
        }
        if (txtBarcode.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Barcode", null, JOptionPane.ERROR_MESSAGE);
            txtBarcode.grabFocus();
            return false;
        }
        if (cmbCatagory.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Select Catagory", null, JOptionPane.ERROR_MESSAGE);
            cmbCatagory.grabFocus();
            return false;
        }
        if (cmbBrand.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Select Brand", null, JOptionPane.ERROR_MESSAGE);
            cmbBrand.grabFocus();
            return false;
        }
        if (txtQuantity.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Quanity", null, JOptionPane.ERROR_MESSAGE);
            txtQuantity.grabFocus();
            return false;
        }
        return true;
    }

    private void tableSelect() {
        DefaultTableModel d1 = (DefaultTableModel) tableProduct.getModel();
        int selectIndex = tableProduct.getSelectedRow();
        txtId.setText(d1.getValueAt(selectIndex, 0).toString());
        cmbCatagory.setSelectedIndex(-1);
        cmbBrand.setSelectedIndex(-1);
        txtTitle.setText(d1.getValueAt(selectIndex, 1).toString());
        txtBarcode.setText(d1.getValueAt(selectIndex, 2).toString());
        txtRetailPrice.setText(d1.getValueAt(selectIndex, 5).toString());
        txtCostPrice.setText(d1.getValueAt(selectIndex, 6).toString());
        txtQuantity.setText(d1.getValueAt(selectIndex, 7).toString());
        txtTitle.grabFocus();
    }

    public class CatagoryItem {

        int id;
        String name;

        public CatagoryItem(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public String toString() {
            return name;
        }
    }

    private void CatagoryName() {

        try {
            con = sql.databaseConnection();
            pst = con.prepareStatement("select * from storeCatagory");
            rs = pst.executeQuery();
            cmbCatagory.removeAllItems();
            while (rs.next()) {
                cmbCatagory.addItem(new CatagoryItem(rs.getInt("id"), rs.getString("cTitle")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(StoreLoginUsers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public class BrandItem {

        int id;
        String name;

        public BrandItem(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public String toString() {
            return name;
        }
    }

    private void BrandName() {

        try {
            con = sql.databaseConnection();
            pst = con.prepareStatement("select * from storeBrand");
            rs = pst.executeQuery();
            cmbBrand.removeAllItems();
            while (rs.next()) {
                cmbBrand.addItem(new CatagoryItem(rs.getInt("id"), rs.getString("bTitle")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(StoreLoginUsers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void resetValues() {
        txtId.setText("");
        txtBarcode.setText("");
        txtCostPrice.setText("");
        txtQuantity.setText("");
        txtRetailPrice.setText("");
        txtTitle.setText("");
        cmbBrand.setSelectedIndex(-1);
        cmbCatagory.setSelectedIndex(-1);
        txtTitle.grabFocus();
    }

    private void table_update() {
        int c;
        try {
            con = sql.databaseConnection();
            pst = con.prepareStatement("select * from StoreProduct a, StoreCatagory b , StoreBrand c where a.catid = b.id AND a.bid = c.id");
            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rsd = (ResultSetMetaData) rs.getMetaData();
            c = rsd.getColumnCount();
            DefaultTableModel d = (DefaultTableModel) tableProduct.getModel();
            d.setRowCount(0);

            while (rs.next()) {

                Vector v2 = new Vector();
                for (int i = 1; i <= c; i++) {
                    v2.add(rs.getInt("pid"));
                    v2.add(rs.getString("title"));
                    v2.add(rs.getString("barcode"));
                    v2.add(rs.getString("ctitle"));
                    v2.add(rs.getString("btitle"));
                    v2.add(rs.getString("retailprice"));
                    v2.add(rs.getString("costprice"));
                    v2.add(rs.getString("quantity"));
                }
                d.addRow(v2);
            }
        } catch (SQLException ex) {
            Logger.getLogger(StoreLoginUsers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void selectProductId() {
        Id = 0;
        int c;
        try {
            con = sql.databaseConnection();
            pst = con.prepareStatement("Select pid from storeProduct");
            rs = pst.executeQuery();
            while (rs.next()) {
                c = rs.getInt("pId");
                if (c > Id) {
                    Id = c;
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    private int selectBrandId(String brandTitle) {
        int n = 0;
        try {
            con = sql.databaseConnection();
            pst = con.prepareStatement("Select id from storeBrand where btitle = ?");
            pst.setString(1, brandTitle);
            rs = pst.executeQuery();
            while (rs.next()) {

                n = rs.getInt("Id");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex);
        }
        return n;
    }

    private void insert() {
        try {
            selectProductId();
            Id = Id + 1;
            Title = txtTitle.getText().toUpperCase();
            Barcode = txtBarcode.getText().toUpperCase();
            RetailPrice = Integer.parseInt(txtRetailPrice.getText());
            CostPrice = Integer.parseInt(txtCostPrice.getText());
            Quantity = Integer.parseInt(txtQuantity.getText());
            CatagoryItem Catagory_Id = (CatagoryItem) cmbCatagory.getSelectedItem();

//            BrandItem Brand_Id = (BrandItem) cmbBrand.getSelectedItem();
            Bid = selectBrandId(cmbBrand.getSelectedItem().toString());
            con = sql.databaseConnection();
            pst = con.prepareStatement("Insert into storeProduct (pid,catid,bid,title,costprice,retailprice,quantity,barcode) values(?,?,?,?,?,?,?,?)");
            pst.setInt(1, Id);
            pst.setInt(2, Catagory_Id.id);
            pst.setInt(3, Bid);
            pst.setString(4, Title);
            pst.setInt(5, CostPrice);
            pst.setInt(6, RetailPrice);
            pst.setInt(7, Quantity);
            pst.setString(8, Barcode);
            rs = pst.executeQuery();
            JOptionPane.showMessageDialog(this, "Product Added");
            table_update();
            resetValues();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex);
            JOptionPane.showMessageDialog(this, "Product not Added", "Error", JOptionPane.ERROR_MESSAGE);
            txtTitle.grabFocus();
        }
    }

    private void delete() {
        DefaultTableModel d1 = (DefaultTableModel) tableProduct.getModel();
        int selectIndex = tableProduct.getSelectedRow();

        int Id = Integer.parseInt(d1.getValueAt(selectIndex, 0).toString());

        int ch = JOptionPane.showConfirmDialog(null, "Do you Want to delete Product ?", "Warning", JOptionPane.YES_NO_OPTION);
        if (ch == JOptionPane.YES_OPTION) {
            try {
                con = sql.databaseConnection();
                pst = con.prepareStatement("delete from StoreProduct where pId= ?");
                pst.setInt(1, Id);
                pst.executeUpdate();

                JOptionPane.showMessageDialog(null, "Product Deleted....!");
                table_update();
                resetValues();
            } catch (SQLException ex) {
                Logger.getLogger(StoreLoginUsers.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void edit() {
        try {
            tableSelect();
            Title = txtTitle.getText().toUpperCase();
            Barcode = txtBarcode.getText().toUpperCase();
            RetailPrice = Integer.parseInt(txtRetailPrice.getText());
            CostPrice = Integer.parseInt(txtCostPrice.getText());
            Quantity = Integer.parseInt(txtQuantity.getText());
            CatagoryItem Catagory_Id = (CatagoryItem) cmbCatagory.getSelectedItem();

//          BrandItem Brand_Id = (BrandItem) cmbBrand.getSelectedItem();
            Bid  = selectBrandId(cmbBrand.getSelectedItem().toString());
            Id = Integer.parseInt(txtId.getText());
            con = sql.databaseConnection();
            pst = con.prepareStatement("update storeProduct set  catid = ?, bid = ?, title = ?,costprice=?,retailprice=?,quantity =?,barcode=? where pid = ?");
           
            pst.setInt(1, Catagory_Id.id);
            pst.setInt(2, Bid);
            pst.setString(3, Title);
            pst.setInt(4, CostPrice);
            pst.setInt(5, RetailPrice);
            pst.setInt(6, Quantity);
            pst.setString(7, Barcode);
            pst.setInt(8, Id);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Product Updated....!");
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
        tableProduct = new javax.swing.JTable();
        txtTitle = new javax.swing.JTextField();
        txtRetailPrice = new javax.swing.JTextField();
        txtCostPrice = new javax.swing.JTextField();
        txtBarcode = new javax.swing.JTextField();
        txtQuantity = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnAdd = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        cmbBrand = new javax.swing.JComboBox();
        cmbCatagory = new javax.swing.JComboBox();

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

        tableProduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "TITLE", "BARCODE", "CATAGORY", "BRAND", "RETAIL PRICE", "COST PRICE", "QUANTITY"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tableProduct.getTableHeader().setReorderingAllowed(false);
        tableProduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableProductMouseClicked(evt);
            }
        });
        panelTable.setViewportView(tableProduct);

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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelTable, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        txtTitle.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtTitle.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTitleKeyPressed(evt);
            }
        });

        txtRetailPrice.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtRetailPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtRetailPriceKeyPressed(evt);
            }
        });

        txtCostPrice.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtCostPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCostPriceKeyPressed(evt);
            }
        });

        txtBarcode.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtBarcode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBarcodeKeyPressed(evt);
            }
        });

        txtQuantity.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtQuantity.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtQuantityKeyPressed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Title:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setText("Retail Price:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setText("Cost Price:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setText("Barcode:");

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
        txtId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtIdKeyPressed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Viner Hand ITC", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 102, 102));
        jLabel7.setText("PRODUCT DATA PAGE");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel8.setText("Catagory:");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel9.setText("Brand:");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel10.setText("Quantity:");

        cmbBrand.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " " }));
        cmbBrand.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbBrandKeyPressed(evt);
            }
        });

        cmbCatagory.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbCatagoryKeyPressed(evt);
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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(58, 58, 58)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtUser, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(91, 91, 91)))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel6)
                            .addComponent(jLabel4))
                        .addGap(63, 63, 63)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtCostPrice, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                            .addComponent(txtRetailPrice, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTitle, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtId, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtBarcode))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel8))
                                .addGap(38, 38, 38)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtQuantity)
                                    .addComponent(cmbBrand, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cmbCatagory, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(16, 16, 16))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(btnAdd)
                                .addGap(18, 18, 18)
                                .addComponent(btnEdit)
                                .addGap(18, 18, 18)
                                .addComponent(btnDelete)
                                .addGap(45, 45, 45))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelSwictch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtUser, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtRetailPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbCatagory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addComponent(cmbBrand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addComponent(jLabel9)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel10)))
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCostPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(txtBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAdd)
                            .addComponent(btnEdit)
                            .addComponent(btnDelete))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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

    private void txtIdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIdKeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_txtIdKeyPressed

    private void txtTitleKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTitleKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txtTitle.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter Title", null, JOptionPane.ERROR_MESSAGE);
                txtTitle.grabFocus();
            } else {
                txtRetailPrice.grabFocus();
            }
        }
    }//GEN-LAST:event_txtTitleKeyPressed

    private void txtRetailPriceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRetailPriceKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txtRetailPrice.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter Retail Price", null, JOptionPane.ERROR_MESSAGE);
                txtRetailPrice.grabFocus();
            } else {
                txtCostPrice.grabFocus();
            }
        }
    }//GEN-LAST:event_txtRetailPriceKeyPressed

    private void txtCostPriceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostPriceKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txtCostPrice.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter Cost Price", null, JOptionPane.ERROR_MESSAGE);
                txtCostPrice.grabFocus();
            } else {
                txtBarcode.grabFocus();
            }
        }
    }//GEN-LAST:event_txtCostPriceKeyPressed

    private void txtBarcodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBarcodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txtBarcode.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter Barcode", null, JOptionPane.ERROR_MESSAGE);
                txtBarcode.grabFocus();
            } else {
                cmbCatagory.grabFocus();
            }
        }
    }//GEN-LAST:event_txtBarcodeKeyPressed

    private void cmbCatagoryKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbCatagoryKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (cmbCatagory.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(this, "Select Catagory", null, JOptionPane.ERROR_MESSAGE);
                cmbCatagory.grabFocus();
            } else {
                cmbBrand.grabFocus();
            }
        }
    }//GEN-LAST:event_cmbCatagoryKeyPressed

    private void cmbBrandKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbBrandKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (cmbBrand.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(this, "Select Brand", null, JOptionPane.ERROR_MESSAGE);
                cmbBrand.grabFocus();
            } else {
                txtQuantity.grabFocus();
            }
        }
    }//GEN-LAST:event_cmbBrandKeyPressed

    private void txtQuantityKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQuantityKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txtQuantity.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter Quantity", null, JOptionPane.ERROR_MESSAGE);
                txtQuantity.grabFocus();
            } else {
                btnAdd.grabFocus();
            }
        }
    }//GEN-LAST:event_txtQuantityKeyPressed

    private void btnAddKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnAddKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (checkValues()) {
                insert();
            }
        }
    }//GEN-LAST:event_btnAddKeyPressed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        if (checkValues()) {
            insert();
        }
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Id", null, JOptionPane.ERROR_MESSAGE);
            txtId.grabFocus();
        } else {
            delete();
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        if (checkValues()) {
            edit();
        }
    }//GEN-LAST:event_btnEditActionPerformed

    private void tableProductMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableProductMouseClicked
        // TODO add your handling code here:
        tableSelect();
    }//GEN-LAST:event_tableProductMouseClicked

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
            java.util.logging.Logger.getLogger(StoreProduct.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StoreProduct.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StoreProduct.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StoreProduct.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StoreProduct().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JComboBox cmbBrand;
    private javax.swing.JComboBox cmbCatagory;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
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
    private javax.swing.JTable tableProduct;
    private javax.swing.JTextField txtBarcode;
    private javax.swing.JTextField txtCostPrice;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtQuantity;
    private javax.swing.JTextField txtRetailPrice;
    private javax.swing.JTextField txtTitle;
    private javax.swing.JLabel txtUser;
    // End of variables declaration//GEN-END:variables
}
