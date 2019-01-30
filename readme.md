Red Had Process Automation Manager - Order Management Demo Repository
=====================================================================


**Red Had Process Automation Manager** is 100% open source and the upstream effort is known as [jbpm](http://www.jbpm.org).


How to import this demo project
-----------------------------------

This repository can be imported in your Business Central following these steps:

1. From Home page *click* **Design**
2. In header section where is the space name (e.g. myteam) *select the kebab icon* (the 3 vertical dots icon)
3. *Select* **Import Project**
4. *Fill the* **Repository URL** field with this githup repository URL and, finally, *select* **Import**


Task Assignment
-----------------------------------

- Task: **Request Offer** - User Group: `rest-all`
- Task: **Prepare Offer** - Dynamically assigned to the `actorId = #{supplierInfo.user}`
- Task: **Approve** - User Group: `Administrators`
- Task: **OrderRejected** - User Group: `rest-all` (Routed to the Swimlane Actor)


Change Log
-----------------------------------

 - 2019-01-29: changed user group to match existing one in OCP standard image.
