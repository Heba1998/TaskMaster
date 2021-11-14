# **TaskMaster**

## **Lab26**
# Homepage:

*The main page have a heading at the top of the page, an image to mock the `my task` view, and buttons at the bottom of the page to allow going to the `add tasks` and `all tasks` page.*

![Homepage](screenshots/HomePage.png)


# Add a Task:

*On the `Add a Task` page, allow users to type in details about a new task, specifically a title and a body. When users click the “submit” button, show a `submitted!` label on the page.*

![Add a Task](screenshots/AddTask.png)

# All Tasks:

*The `all tasks` page should just be an image with a back button*

![All Tasks](screenshots/AllTask.png)


## **Lab27**

# Homepage:

*The main page contain three buttons with hardcoded task titles. When a user taps one of the titles, it should go to the Task Detail page, and the title at the top of the page should match the task title that was tapped on the previous page.*

*The homepage also contain a button to visit the Settings page, and once the user has entered their username, it should display `{username}’s tasks` above the three task buttons.*



![Homepage](screenshots/HomePage27.png)


# Settings Page:

*Create a Settings page. It should allow users to enter their username and hit save.*

![Settings Page](screenshots/Settings.png)

# Task Detail Page:

*Task Detail page have a title at the top of the page, and description.*

![Task Detail Page](screenshots/TaskDetails.png)


## **Lab28**

# Homepage:

*Refactor the homepage to use a RecyclerView for displaying Task data.*

*you can tap on any one of the Tasks in the RecyclerView, and it will appropriately launch the detail page with the correct Task title displayed.*



![Homepage](screenshots/HomePage28.png)


## **Lab29**

# Homepage:

*Refactor your homepage’s RecyclerView to display all Task entities in your database.*


![Homepage](screenshots/HomePage29.png)


# Add a Task:


![Add a Task](screenshots/AddTask29.png)


## **Lab31**

# Espresso Testing:

*Add Espresso to application*


![test](screenshots/Test31.png)


## **Lab32**

* Tasks Are Cloudy
Using the amplify add api command, create a Task resource that replicates our existing Task schema. Update all references to the Task data to instead use AWS Amplify to access your data in DynamoDB instead of in Room.

* Add Task Form
Modify your Add Task form to save the data entered in as a Task to DynamoDB.

* Homepage
Refactor your homepage’s RecyclerView to display all Task entities in DynamoDB.

![lab32](screenshots/lab32.png)


## **Lab33**

* Settings Page

In addition to a username, allow the user to choose their team on the Settings page. Use that Team to display only that team’s tasks on the homepage.

![lab33](screenshots/setting33.png)


* Homepage

Refactor your homepage’s RecyclerView to display all Task entities in DynamoDB.

![lab33](screenshots/homepage33.png)


* Add Task Form

Modify your Add Task form to include either a Spinner or Radio Buttons for which team that task belongs to.

![lab33](screenshots/AddTask33.png)




