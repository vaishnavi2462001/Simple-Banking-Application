This project is a Java-based Test Automation Framework built using Maven and TestNG.
It provides modular test cases organized by packages and can be executed either from Eclipse IDE or directly via the command line (Maven).

1.**Clone the repository**
git clone <your-repository-link>
Navigate to the project directory
cd <your-project-folder>

2.**Install dependencies**
Use Maven to install all required dependencies:
**mvn clean install**

3. **Setting up TestNG in Eclipse**
1)Open Eclipse
2)Go to Help → Eclipse Marketplace
3)Search for TestNG and install the plugin
4)Restart Eclipse after installation

4. **Running Tests**
**Option 1 - From Eclipse**
1) Open testng.xml in your project
2)Right-click → Run As → TestNG Suite
3)All tests listed in testng.xml will be executed

**Option 2 — From Command Line (Maven)**
You can also run the TestNG suite using Maven:
**mvn test**

5. **Viewing Test Results**
In Eclipse: Results appear in the TestNG Results window.
From Maven: Results are printed in the terminal and also stored under: target/surefire-reports/
