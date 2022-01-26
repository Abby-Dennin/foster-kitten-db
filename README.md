# foster-kitten-db
Uses SQL and Java to create a database for foster kittens.

Welcome to FosterKittenDB!
Here is a quick overview of how to properly set up and interact with the
database application:

Note: Some features are still in development!

SET UP (Using Eclipse):
- Extract the JavaApp zip file
- Open the java file called "FosterApp" which is located in the src file
  and open it in eclipse
- Once in eclipse, click file -> new -> java project
- Name the project FosterApp and click next, be sure to uncheck create module
  -info.jar before clicking finish to create the project
- Once you click finish the project will show up as "FosterApp" in the eclipse
  project menu
- Click the carrot next to FosterApp to see the contents and scroll down to
  the src file
- Right click the src file and click new -> file  and name it FosterApp.java
- Note: Make sure you add the .java so it is not just a text file
- Once you have created the FosterApp.java file, double click on it and you
  should see a blank java file
- Now go back to the FosterApp that you originally opened in eclipse, do CTRL A
  to select everything and copy and paste it into your FosterApp.java file
- Save your file and click run, refer to the terminal for further instructions
  on how to use the app

LOG IN:
- There are two phases to logging in to the application. The first is to
  login to the MySQL database using your MySQL username and password. 
- Once you are successfully connected to the database, you will be brought
  to the login menu (viewed via the terminal) where you are prompted to sign
  up for an account or login
- The application has two main views, the view for a Foster Coordinator and 
  the view for a Foster Parent
- To login as an example Foster Coordinator, use the username setkin and the
  password is password0
- To login as an example Foster Parent, use the username adennin and the
  password is password1

INTERACTIONS:
- The entire user interaction for the database application takes place in the
  terminal. You will be prompted throughout the process to enter numbers to 
  take you to different menus and select different functions. When user input
  is needed you will always be prompted and you should be alerted if there are
  any errors in the input provided.

TECHNICAL SPECS: 
- The application was written and tested using eclipse, and while in theory it
  should work fine with other Java IDEs, I don't know if there are specific 
  set up specifications for running it in other IDEs
