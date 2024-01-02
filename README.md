**About the project:**

This is a budgeting tool that is intended to help understand an individual’s income and expenses. Calculating the amount of discretionary income available having accounted for taxes, living expenses, hobbies etc.
If a user is looking to identify how much they are spending on subscription services per year or how a salary change might impact their spending cash, this tool is a quick and easy way to get some clarity and conduct some rapid testing.


**Running the program:**

The full project source code has been uploaded here. In order to use the project in a purely standalone functional fashion you only require the compiled files (Stored in: out\production\budgeting_tool)
Once you have located/downloaded the budgeting_tool folder described above: simply run the BudgetingTool.jar file with the latest version of Java (by selecting "Open With -> Java" or via a terminal with the command "java -jar BudgetingTool.jar" when inside the budgeting_tool directory. 


**Usage / Instructions:**

There are three areas to input data: Income, Tax and Expenses. Each of these fields can be left blank if so desired.

•	Income – Single Field (Should be a whole number without additional formatting or symbols e.g. 30000, 24200, 513204, …)

•	Tax – Table (Each tax band / row of data inside the table will apply the specified tax rate to any income between the upper and lower fields. All values should be positive whole numbers)

•	Expenses – Table (Each row represents a monthly expense and should be input as a positive number, you may choose to include decimal values here where required)

Once all fields have been filled out to your liking simply click on the summary tab and a full report will be generated. From this page you may also save all field entries by clicking "Save All Tables".


**Why this was created:**

This project was a challenge that I set for myself to design and implement a real-world tool. It was my first major Java/OOP undertaking in a while so I wanted to test a range of strategies and structures.
I took the opportunity to use Java's wide array of native libraries and GUI tools (Swing) in order to make something quite challenging in a number of ways (e.g. Developing a GUI with event listeners, using existing libraries and creating my own, writing to files and checking the validity of inputs).
Designing this project has improved my skills in both back and front end development.  
The project is far from perfect but it is fit for purpose and has been a great learning tool, and demonstrates some of my ability and understanding. 
While the class structure I have used is quite unorthodox, it has drastically improved my understanding for future projects and is a tool I will make personal use of in the future.


**Proposed improvements / revisions:**

Breaking apart the Summary page update method into more paletable and reusable methods.
Using padding around the main frame/panel to avoid individually padding elements in such a crude way.
Improving naming conventions and reusability of functions. Class structure was unusual and slightly unintuitive. Make better usage of OOP.
Improving the overall look (Very few visual/style changes were made as I wish to begin on another project asap. Improving the aesthetics of the design and fonts would increase readability and marketability for industry applications)


**Note:** 

If you have any advice on how I might have approached this differently or improved the design please feel free to reach out, I welcome constructive criticism.
