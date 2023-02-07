
***************************
*  MESSAGING APPLICATION  *
***************************

Version: 1.0
January 2023
Networked Software Development
Liam Hammond – C1022456

------------------
1.  INTRODUCTION
------------------
The program is a Java console program that employs a multithreaded client-server application. It allows a user to login, subscribe, and unsubscribe from channels, post messages to channels, and read messages to subscribed channels. The application also allows multiple users to connect concurrently to the server.

------------------
2.  DOCUMENTATION
------------------
This application is made by Liam Hammond and should only be used and/or distributed by those authorised by himself or Sheffield Hallam University.

------------------
3. DEPENDENCIES
------------------
This messaging application has the following dependencies:

(a) JRE/JDK 19.0.1 – The recommended Java Development Kit version for stable builds of the program, proceed on older version at your own risk of limited functionality.

	https://jdk.java.net/19/

(b)  json-simple-1.1.1.jar – A library to assist in the serialisation and deserialisation of JavaScript Object Notation (JSON). The complete source can be found at:

	https://cliftonlabs.github.io/json-simple/

------------------
4. INCLUDED FILES
------------------
Inside the MessagingApplication-1.0 folder should be the following contents:
README.txt
Json-simple-1.1.1.jar
src folder

If any of the above files are not in the folder, please retry downloading and unzipping, if still no success contact the author.

------------------
5. SETUP
------------------
The following setup is based upon IntelliJ IDEA 2022.2.3, 17.0.4.1+7-b469.62 x86_64
To use this application, the following setup process is required:

(a) Opening the project in IntelliJ IDEA:
Open > MessagingApplication-1.0/src

(b) Now opened in IntelliJ, we will set the development software:
File > Project Structure > Project Settings > Project > SDK: > openjdk-19

(c) Stay in the Project Structure window, now to setup the json-simple dependency:
Project Settings > Modules > MessagingApplication-1.0 > Dependencies > ‘+’ (plus button) > 1 JARs or Directories > MessagingApplicaiton-1.0/json-simple-1.1.1.jar > open

(d) Confirming our changes:
Apply > OK > close the window

(e) Build the program to ensure new Project structure works, if not retry steps (a) -> (d)

(f) To configure the program to concurrently run multiple clients:
Run > Edit Configurations > + (plus button) > Application 
Name it as you please, a good suggestion is “MultiClient”. Next to allow the multiple runs:
Build and run > where it says “Main class”, change to “Client.Client”
Modify options > Operating System > Allow multiple instances

(g) Confirming our changes:
Apply > OK > close the window

------------------
5. RUN
------------------
To then use the application simply open Server > Server and run the class. 
Next go to Client > Client and run as many instances as desired.

------------------
6.  SUPPORT
------------------
Following and further issues, please go through this readme file again, if the program still does not function get in touch with the author directly.
