
# TelerikStudio Code 
----------
### About

Telerik Academy final project - An app that closely mimics the popular VisualStudio Marketplace web app.

[Demo](http://78.90.23.143:8080)

# BUILD & RUN

### Prerequisites
1. Register in the Google Cloud Platform
2. Create a new project
3. Enable SQL and Storage modules
4. Create SQL Instance
5. Create a DB named marketplace
6. Create 3 buckets in your Storage module:
7.	A bucket for the extension files
8.	A bucket for the extension pictures
9.	A bucket for the user profile pictures
10. Save the names of those buckets
11. Authenticate through the Google Cloud SDK
12. Add your IP to the allowed connections to be able to access the DB through HeidiSQL or similar tools.
13. Follow instructions to create a service account and generate credentials in JSON format

Go to GitHub and create a public API OAuth key
Open hibernate.cfg.xml in the project and set the following properties:
1.	hibernate.connection.url -> jdbc:mysql://[SQL INSTANCE IP]/marketplace?useSSL=false
2.	hibernate.connection.username -> Your root user
3.	hibernate.connection.password -> Your root user password

Open application.properties and set the following properties:
1.	spring.cloud.gcp.sql.instance-connection-name= -> [INSTANCE CONNECTION NAME]
2.	spring.datasource.username= -> [ROOT USER]
3.	spring.datasource.password= -> [ROOT USER PASSWORD]

Run the project once to generate the schema
Manually add the following in the Properties table to the row with ID 1:
1.	Credentials column -> paste your json file contents
2.	GitHub_key column -> paste your GitHub API key
3.	Project_id column -> paste your project’s ID

In CloudExtensionServiceImpl.java set the bucket names for the pics and extension
In CloudUserServiceImpl.java set the bucket for the user pics

#### You're good to go!

---

# FEATURES

---

### PUBLIC PART
- Homepage with Featured, Popular and Newest extensions. 
- Sorting all extensions
- Individual extension pages
- User Registration & Login
- User profile pages
- Searching by name, tag, user 
- Sorting results
- Downloading extensions
- RESTful access to extension and user information

Upon accessing the app, visitors are greeted by three lists of extensions: Featured extensions, selected by the administration. Only 5 extensions can be featured at any given time to appear on the front page. The most popular extensions, filtered by downloads and the newest extensions, sorted by date of upload. They can register an account and use it to access the private part of the application. However, they are not required to do so if they want to use most of the app’s functionality. Without needing an account, they can search for extensions by their name, tags or even search for individual users to view the extensions they have uploaded. They can view each extension individually to get more information, such as: Description, publisher, GitHub repository link, latest commit and the open issues for that repository. Number of times the extension has been uploaded. Tags which can be used to look up similar extensions and a download button that allows to download the extension to the user’s machine. Our RESTful service provides users with the ability to retrieve information about our extensions and users with various filters. No sensitive information is provided via the RESTful service.

---

# PRIVATE PART
- Personal user profile page
- Extensions: create, update and delete
- State of user’s extensions: approved / pending

After successful login, the user can access the extension creation page and their own profile page where they can manipulate the extensions they have published as well as edit their profile information.  As extensions are in a pending state after upload, only the owner can view extensions that have not yet been approved for the public to access.

---

# ADMINISTRATION PART
- Manipulate all uploaded extensions
- Approve pending extensions or revert extensions to pending on demand
- Ban and unban users
- Feature extensions and swap already featured extensions 
- Synchronize GitHub information for individual extensions on demand
- Synchronize GitHub information for all extensions on demand
- Set and change automatic synchronization interval on demand
- View last successful and last failed synchronization as well as error information
- Add and remove administrator rights from users

Owners are only created by the DB administrator to protect the integrity of the application. Administrators can be promoted or demoted by the owner. Administrators cannot ban other administrators but can, however, unban them. Admins can ban and unban user accounts, approve pending extensions and edit them as they see fit. GitHub information for all extensions is periodically synchronized in the background so all admins can either synchronize an individual extension or trigger a full synchronization for all extensions. They can also change the delay for the scheduled synchronization, which triggers an immediate synchronization for all extensions. All administrators and owners have access to these features as well as an information panel with the last successful & unsuccessful synchronization.

---

# INVISIBLE PART
- Cloud-based SQL
- Cloud-based Storage
- Cloud-based continuous Integration server
- Optimistic locking
- Automated synchronization for GitHub information

The backbone of the application is cloud based as this increases security and reliability. Utilizing the Google Cloud Platform our application has a stable and secure database available around the clock. The cloud storage module allows us to securely save user extensions and keep them safe as users only have read permission for those files and cannot modify them without the necessary permissions. The continuous integration server allows us to more reliably test our application as we make modifications to it. Optimistic locking allows us to have a faster and more responsive backend as calls are not explicitly synchronized, meaning requests do not have to wait for each other to finish except for when a conflict occurs. At that point the conflicting request is rolled back and needs to be re-initiated by the user. Synchronization allows us to have up-to-date information from the GitHub repository of the extension thereby delivering our users a better experience.

----
