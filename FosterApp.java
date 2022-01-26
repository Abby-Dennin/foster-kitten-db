import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Scanner;

import java.sql.CallableStatement;

import java.sql.ResultSet;
import java.util.ArrayList;

public class FosterApp {

  // get a new database connection
  public Connection getConnection() throws SQLException {
    Connection conn = null;
    Scanner sc = new Scanner(System.in); 
    System.out.println("Enter Username: ");
    String username = "root"; 
    System.out.println("Enter Password: ");
    String password = "w7mWNS21";
    System.out.println("Logging in...");

    conn = DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/foster_kittens?user=" + username, 
        username, password);

    return conn;
  }

  // run a SQL command which does not return a record set:
  // CREATE/INSERT/UPDATE/DELETE/DROP/etc. 
  public boolean executeUpdate(Connection conn, String command) throws SQLException {
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      stmt.executeUpdate(command);

      return true;
    }
    finally {
      if (stmt != null) {
        stmt.close();
      }
    }
  }

  //connect to MySQL and do stuff
  public void run() {
    // connect to MySQL
    Connection conn = null;

    try {
      conn = this.getConnection();
      System.out.println("Connected to database!");
    }
    catch (SQLException e) {
      System.out.println("ERROR: Could not connect to the database");
      e.printStackTrace();
      return;
    }

    loginMenu(conn);
  }

  // runs a statement and returns a result set
  public ResultSet runStmt(Connection conn, String sql) {
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery(sql);
    }
    catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }    
    return rs;
  }

  // ********************************************** //
  //              USER LOGIN METHODS
  // ********************************************** //

  // sign up as a user
  public void signUp(Connection conn) {
    System.out.println("Sorry, sign up feature is not yet available");
    loginMenu(conn);
  }

  // log in as a user
  public void logIn(Connection conn) {
    int choice = validLogIn(conn);

    switch (choice) {
    case -1: 
      System.out.println("Invalid Login");
      loginMenu(conn);
    case 0:
      coordinatorMenu(conn);
      break;
    case 1:
      parentMenu(conn);
      break;
    }
  }

  // ********************************************** //
  //                MENU METHODS 
  // ********************************************** //

  // login menu
  public void loginMenu(Connection conn) {
    Scanner sc = new Scanner(System.in);
    System.out.println("Welcome to FosterKittenDB");
    System.out.println("Select Option by Entering Number: ");
    System.out.println("1: Sign up");
    System.out.println("2: Login");

    int choice = sc.nextInt();

    switch (choice) {
    case 1:
      signUp(conn);
      break;
    case 2: 
      logIn(conn);
      break;
    }

    if (choice != 1 && choice != 2) {
      System.out.println("Invalid number option, please try again");
      choice = sc.nextInt();
    }
  }

  // foster parent's view of DB app
  public void parentMenu(Connection conn) {

    Scanner sc = new Scanner(System.in);
    System.out.println();
    System.out.println("Welcome Foster Parent!");
    System.out.println();
    System.out.println("Select function by entering the given number: ");
    System.out.println("1: View my current kittens");
    System.out.println("2: View all of my kittens");
    System.out.println("3: View kitten info");
    System.out.println("4: View kitten treatment");
    System.out.println("5: View available kittens");

    int choice = sc.nextInt();
    switch (choice) {
    case 1:
      viewCurrParentsKittens(conn, 1);
      break;
    case 2:
      viewParentsKittens(conn, 1);
      break;
    case 3:
      viewKittenInfo(conn, 1);
      break;
    case 4:
      viewKittenTreatment(conn, 1);
      break;
    case 5:
      viewAvailableKittens(conn, 1);
      break;
    }   
  }

  // coordinator's view of DB app
  public void coordinatorMenu(Connection conn) {
    System.out.println();
    System.out.println("Welcome Foster Coordinator!");
    System.out.println();
    Scanner sc = new Scanner(System.in);
    System.out.println("Select Menu by Entering Number: ");
    System.out.println("1: Create Menu");
    System.out.println("2: Read Menu");
    System.out.println("3: Update Menu");
    System.out.println("4: Delete Menu");

    int choice = sc.nextInt();

    switch (choice) {
    case 1:
      createMenu(conn);
      break;
    case 2:
      readMenu(conn);
      break;
    case 3:
      updateMenu(conn);
      break;
    case 4:
      deleteMenu(conn);
      break;
    }

    if(choice != 1 && choice != 2 && choice != 3 && choice != 4) {
      System.out.println("Please enter one of the number corresponding to the menus above");
      choice = sc.nextInt();
      sc.close();
    }
  }

  // select the create method to run
  public void createMenu(Connection conn) {
    Scanner sc = new Scanner(System.in);
    System.out.println("CREATE MENU");
    System.out.println("Select function by entering the given number: ");
    System.out.println("1: Add kitten");
    System.out.println("2: Add foster parent");
    System.out.println("3: Add a kitten bond");

    int choice = sc.nextInt();

    switch (choice) {
    case 1:
      addKitten(conn);
      break;
    case 2:
      addParent(conn);
      break;
    case 3:
      addBond(conn);
      break;
    }
  }

  // select the read method to run
  public void readMenu(Connection conn) {

    Scanner sc = new Scanner(System.in);
    System.out.println("READ MENU");
    System.out.println("Select function by entering the given number: ");
    System.out.println("1: View all kittens");
    System.out.println("2: View a kitten's information");
    System.out.println("3: View a kitten's treatment plan");
    System.out.println("4: View all adopted kittens");
    System.out.println("5: View kittens fostered by a parent");
    System.out.println("6: View kittens currently in foster with a parent");
    System.out.println("7: View all available (in shelter) kittens");
    System.out.println("8: Search kittens by name");
    System.out.println("9: View if a kitten is part of a bonded pair");
    int choice = sc.nextInt();
    switch (choice) {
    case 1:
      viewAllKittens(conn);
      break;
    case 2:
      viewKittenInfo(conn, 0);
      break;
    case 3:
      viewKittenTreatment(conn, 0);
      break;
    case 4:
      viewAdoptedKittens(conn);
      break;
    case 5:
      viewParentsKittens(conn, 0);
      break;
    case 6:
      viewCurrParentsKittens(conn, 0);
      break;
    case 7:
      viewAvailableKittens(conn, 0);
      break;
    case 8:
      findByName(conn);
      break;
    case 9:
      viewKittenBond(conn, 0);
      break;
    }   
  }

  // select the update method to run
  public void updateMenu(Connection conn) {
    Scanner sc = new Scanner(System.in);
    System.out.println("UPDATE MENU");
    System.out.println("Select function by entering the given number: ");
    System.out.println("1: Update Kitten Information");
    System.out.println("2: Update Foster Parent Information");
    System.out.println("3: Update Treatment");

    int choice = sc.nextInt();

    switch (choice) {
    case 1:
      updateKitten(conn);
      break;
    }

  }

  // select the delete method to run
  public void deleteMenu(Connection conn) {
    Scanner sc = new Scanner(System.in);
    System.out.println("DELETE MENU");
    System.out.println("Select function by entering the given number: ");
    System.out.println("1: Delete a kitten");
    System.out.println("2: Delete a foster parent");
    System.out.println("3: Delete a treatment");
    int choice = sc.nextInt();

    switch (choice) {
    case 1:
      deleteKitten(conn);
      break;
    }
  }

  // ********************************************** //
  //              VALIDATION METHODS
  // ********************************************** //

  // checks that a valid username and password is provided
  public int validLogIn(Connection conn) {
    Statement stmt = null;
    Statement stmt2 = null;
    ArrayList<String> usernames = new ArrayList<String>();
    ArrayList<String> passwords = new ArrayList<String>();
    ArrayList<Integer> usertypes = new ArrayList<Integer>();

    try {
      stmt = conn.createStatement();
      String sql = "SELECT username, user_password, user_type FROM users";
      ResultSet rs = stmt.executeQuery(sql);

      while (rs.next()) {
        String username = rs.getString("username");
        String password = rs.getString("user_password");
        int type = rs.getInt("user_type");
        usernames.add(username);
        passwords.add(password);
        usertypes.add(type);
      }
    }
    catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    Scanner sc = new Scanner(System.in);
    System.out.println("Enter Username: ");
    String user = sc.nextLine();

    int stopper = 0;
    while (stopper == 0) {
      if (usernames.contains(user)) {
        stopper = 1;
      }
      else {
        System.out.println("Whoops! Username isn't valid and this account does not exist, please try again or sign up for an account");
        user = sc.nextLine();
      }
    }

    System.out.println("Enter Password: ");
    String pass = sc.nextLine();

    int stopper2 = 0;
    while (stopper2 == 0) {
      if (passwords.contains(pass)) {
        stopper2 = 1;
      }
      else {
        System.out.println("Whoops! Password isn't valid, please try again or sign up for an account");
        pass = sc.nextLine();
      }
    }

    int checkUser = usernames.indexOf(user);
    int checkPass = passwords.indexOf(pass);
    int checkType;
    if (checkUser == checkPass) {
      checkType = usertypes.get(checkUser);
    }
    else {
      System.out.println("Incorrect login, returning to home page...");
      checkType = -1;
      loginMenu(conn);
    }

    return checkType;
  }

  // checks that a valid kitten id is provided and used
  public int validKittenId(Connection conn) {
    Statement stmt = null;
    ArrayList<Integer> ids = new ArrayList<Integer>();

    try {
      stmt = conn.createStatement();

      String sql = "SELECT kitten_id FROM kitten";
      ResultSet rs = stmt.executeQuery(sql);

      while (rs.next()) {
        int id = rs.getInt("kitten_id");
        ids.add(id);     
      }
    }
    catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    Scanner sc = new Scanner(System.in); 
    System.out.println("Enter a Kitten ID: ");
    int id = sc.nextInt();

    int stopper = 0;
    while(stopper == 0) {
      if (ids.contains(id)) {
        stopper = 1;
      }
      else {
        System.out.println("Whoops! Kitten ID isn't valid, please try again.");
        id = sc.nextInt();
      }
    }

    return id;
  }

  // checks that a valid parent id is provided and used
  public int validParentId(Connection conn) {
    Statement stmt = null;
    ArrayList<Integer> ids = new ArrayList<Integer>();

    try {
      stmt = conn.createStatement();

      String sql = "SELECT foster_parent_id FROM foster_parent";
      ResultSet rs = stmt.executeQuery(sql);

      while (rs.next()) {
        int id = rs.getInt("foster_parent_id");
        ids.add(id);     
      }
    }
    catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    // prompts the user to enter a character name
    Scanner sc = new Scanner(System.in); 
    System.out.println("Enter a Foster Parent ID: ");
    int id = sc.nextInt();

    // determines if the character name is valid
    int stopper = 0;
    while(stopper == 0) {
      if (ids.contains(id)) {
        stopper = 1;
      }
      else {
        System.out.println("Whoops! Foster Parent ID isn't valid, please try again.");
        id = sc.nextInt();
      }
    }

    return id;
  }

  // checks that a valid shelter id is provided and used
  public int validShelterId(Connection conn) {
    Statement stmt = null;
    ArrayList<Integer> ids = new ArrayList<Integer>();

    try {
      stmt = conn.createStatement();

      String sql = "SELECT shelter_id FROM shelter";
      ResultSet rs = stmt.executeQuery(sql);

      while (rs.next()) {
        int id = rs.getInt("shelter_id");
        ids.add(id);     
      }
    }
    catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    Scanner sc = new Scanner(System.in); 
    System.out.println("Enter a Shelter ID: ");
    int id = sc.nextInt();

    int stopper = 0;
    while(stopper == 0) {
      if (ids.contains(id)) {
        stopper = 1;
      }
      else {
        System.out.println("Whoops! Shelter ID isn't valid, please try again.");
        id = sc.nextInt();
      }
    }

    return id;
  }

  // checks that a valid treatment id is provided and used
  public int validTreatmentId(Connection conn) {
    Statement stmt = null;
    ArrayList<Integer> ids = new ArrayList<Integer>();

    try {
      stmt = conn.createStatement();

      String sql = "SELECT treatment_id FROM treatment";
      ResultSet rs = stmt.executeQuery(sql);

      while (rs.next()) {
        int id = rs.getInt("treatment_id");
        ids.add(id);     
      }
    }
    catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    // prompts the user to enter a character name
    Scanner sc = new Scanner(System.in); 
    System.out.println("Enter a Treatment ID: ");
    int id = sc.nextInt();

    // determines if the character name is valid
    int stopper = 0;
    while(stopper == 0) {
      if (ids.contains(id)) {
        stopper = 1;
      }
      else {
        System.out.println("Whoops! Treatment ID isn't valid, please try again.");
        id = sc.nextInt();
      }
    }

    return id;
  }


  // checks that a valid kitten name is provided and used
  public String validKittenName(Connection conn) {
    Statement stmt = null;
    ArrayList<String> names = new ArrayList<String>();

    try {
      stmt = conn.createStatement();

      String sql = "SELECT kitten_name FROM kitten";
      ResultSet rs = stmt.executeQuery(sql);

      while (rs.next()) {
        String currName = rs.getString("kitten_name");
        names.add(currName);     
      }
    }
    catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    // prompts the user to enter a character name
    Scanner sc = new Scanner(System.in); 
    System.out.println("Enter a Kitten Name: ");
    String name = sc.nextLine().toLowerCase();

    // determines if the character name is valid
    int stopper = 0;
    while(stopper == 0) {
      if (names.contains(name)) {
        stopper = 1;
      }
      else {
        System.out.println("Whoops! Kitten Name isn't valid, please try again.");
        name = sc.nextLine().toLowerCase();
      }
    }

    return name;
  }

  // ********************************************** //
  //                CREATE METHODS 
  // ********************************************** //

  public void addKitten(Connection conn) {
    Scanner sc = new Scanner(System.in); 
    System.out.println("Enter a kitten name: ");
    String name = sc.nextLine().toLowerCase();
    System.out.println("Enter a fur color: ");
    String color = sc.nextLine().toLowerCase();
    System.out.println("Enter a social status: ");
    String status = sc.nextLine().toLowerCase();
    System.out.println("Enter an approximate age: ");
    int age = sc.nextInt();
    System.out.println("Enter shelter id: ");
    int shelter = sc.nextInt();

    String sql = "CALL ADD_KITTEN(?, ?, ?, ?, ?)";
    try {
      CallableStatement stmt = conn.prepareCall(sql);
      stmt.setInt(1, shelter);
      stmt.setString(2,  name);
      stmt.setInt(3, age);
      stmt.setString(4, color);
      stmt.setString(5,  status);

      stmt.executeUpdate();
      System.out.println("1 kitten added");
    }   
    catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    coordinatorMenu(conn);
  }

  public void addParent(Connection conn) {
    Scanner sc = new Scanner(System.in); 
    System.out.println("Enter foster parent's full name: ");
    String name = sc.nextLine().toLowerCase();
    System.out.println("Enter foster parent's email: ");
    String email = sc.nextLine().toLowerCase();

    String sql = "CALL ADD_PARENT(?, ?)";
    try {
      CallableStatement stmt = conn.prepareCall(sql);    
      stmt.setString(1, name);
      stmt.setString(2, email); 
      int i = stmt.executeUpdate();
      System.out.println(i + " parent added");
    }   
    catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    coordinatorMenu(conn);
  }

  public void addBond(Connection conn) {
    Scanner sc = new Scanner(System.in); 
    System.out.println("Enter ID for kitten 1: ");
    int id1 = sc.nextInt();
    System.out.println("Enter ID for kitten 2: ");
    int id2 = sc.nextInt();

    String sql = "CALL ADD_BOND(?, ?)";
    try {
      CallableStatement stmt = conn.prepareCall(sql);    
      stmt.setInt(1, id1);
      stmt.setInt(2, id2); 
      int i = stmt.executeUpdate();
      System.out.println(i + " bond added");
    }   
    catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    coordinatorMenu(conn);
  }

  // ********************************************** //
  //                READ METHODS 
  // ********************************************** //

  // view a list of all kittens with IDs and names
  public void viewAllKittens(Connection conn) {
    String sql = "CALL VIEW_ALL_KITTENS()";
    try {
      CallableStatement stmt = conn.prepareCall(sql);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        System.out.println("ID: " + rs.getInt("kitten_id") + ", Name: " + rs.getString("kitten_name"));      
      }
    }
    catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    coordinatorMenu(conn);
  }

  // view available kittens (kittens in shelter)
  public void viewAvailableKittens(Connection conn, int type) {
    String sql = "CALL VIEW_AVAILABLE_KITTENS()";
    try {
      CallableStatement stmt = conn.prepareCall(sql);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        System.out.println("ID: " + rs.getInt("kitten_id") + ", Name: " + rs.getString("kitten_name"));      
      }
    }
    catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    System.out.println();
    if (type == 0) {
      coordinatorMenu(conn);
    }
    else {
      parentMenu(conn);
    }
  }

  // view adopted kittens
  public void viewAdoptedKittens(Connection conn) {
    String sql = "CALL VIEW_ADOPTED_KITTENS()";
    try {
      CallableStatement stmt = conn.prepareCall(sql);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        System.out.println("ID: " + rs.getInt("kitten_id") + ", Name: " + rs.getString("kitten_name"));      
      }
    }
    catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    coordinatorMenu(conn);
  }

  // view all kittens that have ever been fostered by a parent
  public void viewParentsKittens(Connection conn, int type) {
    int id = validParentId(conn);

    try {     
      String sql = "CALL VIEW_PARENTS_KITTENS(?)";
      CallableStatement stmt = conn.prepareCall(sql);
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        System.out.println("ID: " + rs.getInt("kitten_id") + ", Name: " + rs.getString("kitten_name"));      
      }
    }
    catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    System.out.println();
    if(type == 0) {
      coordinatorMenu(conn);
    }
    else {
      parentMenu(conn);
    }
  }

  // view a foster parents current kittens
  public void viewCurrParentsKittens(Connection conn, int type) {
    int id = validParentId(conn);

    try {     
      String sql = "CALL VIEW_PARENTS_CURR_KITTENS(?)";
      CallableStatement stmt = conn.prepareCall(sql);
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        System.out.println("ID: " + rs.getInt("kitten_id") + ", Name: " + rs.getString("kitten_name"));      
      }
    }
    catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    if(type == 0) {
      coordinatorMenu(conn);
    }
    else {
      parentMenu(conn);
    }
  }

  // view a given kitten's information 
  public void viewKittenInfo(Connection conn, int type) {
    int id = validKittenId(conn);
    String sql = "CALL VIEW_KITTEN_INFO(?)";

    try {
      CallableStatement stmt = conn.prepareCall(sql);
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) { 
        String kittenName = rs.getString("kitten_name");
        int age = rs.getInt("approx_age");
        String color = rs.getString("color");
        String status = rs.getString("social_status");
        String parentName = rs.getString("parent_name");
        String shelterName = rs.getString("shelter_name");

        System.out.println("Kitten Name: " + kittenName);
        System.out.println("Foster Parent: " + parentName);
        System.out.println("Shelter: " + shelterName);
        System.out.println("Approx Age: " + age + " Weeks");
        System.out.println("Color: " + color);
        System.out.println("Social Status: " + status);
      }

    }
    catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    System.out.println();
    if(type == 0) {
      coordinatorMenu(conn);
    }
    else {
      parentMenu(conn);
    }
  }

  // view a given kitten's treatment plan
  public void viewKittenTreatment(Connection conn, int type) {   
    int id = validKittenId(conn);   
    String sql = "CALL VIEW_KITTEN_TREATMENT(?)";

    try {
      CallableStatement stmt = conn.prepareCall(sql);
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) { 
        String kittenName = rs.getString("kitten_name");
        String medName = rs.getString("medicine_name");
        double medDose = rs.getDouble("medicine_dose");
        String medFreq = rs.getString("medicine_frequency");

        System.out.println("Kitten Name: " + kittenName);
        System.out.println("Prescription: " + medName + ", " + medDose + " mLs, " + medFreq);
      }
    }
    catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    System.out.println();
    if(type == 0) {
      coordinatorMenu(conn);
    }
    else {
      parentMenu(conn);
    }
  }

  // find a kitten using its name
  public void findByName(Connection conn) {
    String name = validKittenName(conn);
    String sql = "CALL FIND_KITTEN_BY_NAME(?)";

    try {
      CallableStatement stmt = conn.prepareCall(sql);
      stmt.setString(1, name);
      ResultSet rs = stmt.executeQuery();  
      while (rs.next()) {
        int kId = rs.getInt("kitten_id");
        String kittenName = rs.getString("kitten_name");
        System.out.println("ID: " + kId + " , Name: " + kittenName);        
      }
    }
    catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    System.out.println();
    coordinatorMenu(conn);
  }

  // view a given kitten's bond
  public void viewKittenBond(Connection conn, int type) {
    int id = validKittenId(conn);
    String sql = "CALL VIEW_BOND(?)";

    try {
      CallableStatement stmt = conn.prepareCall(sql);
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();

      if (!rs.next()) {
        System.out.println("Kitten is not bonded to any other kittens");
      }

      while (rs.next()) {
        int kId1 = rs.getInt("kitten1_id");
        int kId2 = rs.getInt("kitten2_id");
        System.out.println("Kitten ID: " + kId1 + " is bonded with Kitten ID: " + kId2);
      }
    }
    catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    if(type == 0) {
      coordinatorMenu(conn);
    }
    else {
      parentMenu(conn);
    }
  }

  //*********************************************** //
  //               UPDATE METHODS 
  // ********************************************** //

  // update kitten sub menu
  public void updateKitten(Connection conn) {
    Scanner sc = new Scanner(System.in);
    System.out.println("Kitten Upate Menu");
    System.out.println("Select function by entering the given number: ");
    System.out.println("1: Update Kitten Name");
    System.out.println("2: Update Kitten Age");
    System.out.println("3: Update Kitten to Adopted");
    System.out.println("4: Assign a Kitten to a Foster Parent");
    int choice = sc.nextInt();
    switch (choice) {
    case 1:
      updateKittenName(conn);
      break;
    case 2:
      updateKittenAge(conn);
      break;
    case 3:
      kittenAdopted(conn);
      break;
    case 4:
      assignParent(conn);
      break;
    }

    // returns to the main menu
    coordinatorMenu(conn);
  }

  // kitten has been adopted
  public void kittenAdopted(Connection conn) {
    int id = validKittenId(conn);
    String sql = "CALL KITTEN_ADOPTED(?)";

    try {
      CallableStatement stmt = conn.prepareCall(sql);
      stmt.setInt(1, id);
      int rowsUpdated = stmt.executeUpdate();
      if (rowsUpdated > 0) {
        System.out.println("An existing kitten was set to be adopted");
      }
    }
    catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    coordinatorMenu(conn);
  }

  // assign a foster parent
  public void assignParent(Connection conn) {
    int kId = validKittenId(conn);    
    int pId = validParentId(conn);
    String sql = "CALL ASSIGN_PARENT(?, ?)";

    try {
      CallableStatement stmt = conn.prepareCall(sql);
      stmt.setInt(1, kId);
      stmt.setInt(2, pId);
      int rowsUpdated = stmt.executeUpdate();
      if (rowsUpdated > 0) {
        System.out.println("An existing kitten was assigned a foster parent");
      }
    }
    catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    coordinatorMenu(conn);
  }

  // update the name of an existing kitten
  public void updateKittenName(Connection conn) {
    int id = validKittenId(conn);
    Scanner sc = new Scanner(System.in);
    System.out.println("Enter new name: " );
    String newName = sc.next();
    String sql = "CALL UPDATE_KNAME(?, ?)";

    try {
      CallableStatement stmt = conn.prepareCall(sql);
      stmt.setInt(1, id);
      stmt.setString(2, newName);    
      int rowsUpdated = stmt.executeUpdate();
      if (rowsUpdated > 0) {
        System.out.println("An existing kitten's name was updated successfully!");
      }
    }
    catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    coordinatorMenu(conn);
  }

  // update the age of an existing kitten
  public void updateKittenAge(Connection conn) {
    int id = validKittenId(conn);
    Scanner sc = new Scanner(System.in);
    System.out.println("Enter new name: " );
    int newAge = sc.nextInt();
    String sql = "CALL UPDATE_KAGE(?, ?)";

    try {
      CallableStatement stmt = conn.prepareCall(sql);
      stmt.setInt(1, id);
      stmt.setInt(2, newAge);    
      int rowsUpdated = stmt.executeUpdate();
      if (rowsUpdated > 0) {
        System.out.println("An existing kitten's age was updated successfully!");
      }
    }
    catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    coordinatorMenu(conn);
  }

  //*********************************************** //
  //               DELETE METHODS 
  // ********************************************** //

  public void deleteKitten(Connection conn) {
    String sql = "CALL DELETE_KITTEN(?)";
    int id = validKittenId(conn);
    
    try {
      // deletes the kitten with the given kitten id
      CallableStatement stmt = conn.prepareCall(sql);
      stmt.setInt(1, id);

      stmt.executeUpdate();
    }
    catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }   
    // returns to the main menu
    coordinatorMenu(conn);
  }

  public void deleteParent(Connection conn) {
    String sql = "CALL DELETE_PARENT(?)";
    int id = validParentId(conn);
    
    try {
      // deletes the kitten with the given kitten id
      CallableStatement stmt = conn.prepareCall(sql);
      stmt.setInt(1, id);

      int rowsDeleted = stmt.executeUpdate();
      if (rowsDeleted > 0) {
        System.out.println("A foster parent was deleted successfully!");
      }
    }
    catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }   
    // returns to the main menu
    coordinatorMenu(conn);
  }
  
  // Main method
  public static void main(String[] args) {
    FosterApp app = new FosterApp();
    app.run();
  }
}