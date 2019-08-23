Red Had Process Automation Manager - Order Management Demo Repository
=====================================================================


**Red Had Process Automation Manager** is the Business Process Management product from Red Hat, based on the open source project [jBPM](http://www.jbpm.org).

This demo aims to show some of the core capabilities of this powerful product.

Order Management Process
-----------------------------------

Any organization has a procurement process similar to this one: 

  - the process is started by an employee requiring a business asset (i.e. a phone, a laptop, etc)
  - the purchase department engages a set of suppliers to get the best offer (`Request Offer` task)
  - suppliers provides their best offer (`Prepare Offer` task)
  - the cheapest offer is selected
  - the offer can be automatically accepted whether a set of *Business Rules* are met (implemented by a DMN decision `Auto Approve Decision`)
  - whether the offer does not satisfy the Business Rules, a manager is involved for the final decision (`Approve` task)
  - finally the order is finalized sending it to the ERP system (`Place Order in ERP` task) or is rejected and passed back to the purchase department for acknowledgement (`Order Rejected` task)

In the following picture the BPMN process design:

![BPMN diagram](docs/order-management-process.png)

How to deploy this demo project
-----------------------------------

From version 7.3, the order management process requires a DMN decision that is implemented in a [DMN external project](https://github.com/dmarrazzo/rhpam7-order-management-dmn-demo) , for this reason the first step is to import the DMN decision project and build it:

  1. From Home page *click* **Design**
  2. In header section where is the space name (e.g. myteam) *select the kebab icon* (the 3 vertical dots icon)
  3. *Select* **Import Project**
  4. *Fill the* **Repository URL** field with DMN repository URL (`https://github.com/dmarrazzo/rhpam7-order-management-dmn-demo.git`)
  5. Select *order-management-dmn* and click **Ok**
  6. Switch to **v7.4** branch
  7. Click **Build and Install**, in such way the DMN project (*kjar*) will be installed in the Maven repository and ready to be dynamically loaded by the process at runtime.  

Import the This repository can be imported in your Business Central following these steps:

  1. From Home page *click* **Design**
  2. In header section where is the space name (e.g. myteam) *select the kebab icon* (the 3 vertical dots icon)
  3. *Select* **Import Project**
  4. *Fill the* **Repository URL** field with this github repository URL (`https://github.com/jbossdemocentral/rhpam7-order-management-demo-repo.git`)
  5. Select *Order Management* and click **Ok**
  6. Switch to **v7.4** branch


Run the demo process
-----------------------------------

1. An employee create an order for a required item. To start the process, s/he has to select the *item name* and the *urgency*.

  - Click **Diagram** to show the BPMN diagram of the process instance just started (BPMN is the well known notation to describe business processes where Business Analysts and Developers can share a common view). In this picture, the business stakeholder can recognize the process where s/he is involved in and get visibility on it.
  - From the **Menu** bar, select the **Process instances**: users can understand and search process instances and easily investigate the status.

2. Open **Track > Task Inbox**, there is the task **Request Offer** that wait for the *purchase expert* action.

  - On the top right of the task list, there is an icon to show more information about the tasks, click **Description**
  - From the kebab menu select **Claim and Work**. In the task view, click **Start**
  - Select a **category** for the order, a **target price**, the **suppliers list** who will be asked to provide an offer.
  - Click **Complete**

3. Now, it's the turn of the suppliers to carry on the **Prepare Offer** task. In the list, there is a task for each supplier selected at previous step. The task description point out for which supplier is the task: e.g. `supplier1 prepares offer for Laptop Dell XPS 15`

  - For each task, select **Claim and Work** and in the task view, click **Start**.
  - The supplier has to define a **delivery date** and the **best offer**
  - **Notice:** by design, if the offer match the target price or is lower, other suppliers will not be able to provide their offer (the other tasks are exited)

4. If there are no other tasks for that process instance, the process is completed. In fact, the next task is the *Auto Approve Decision*: it calls a DMN logic that based on the order features decides whether a manager approval is required to confirm the order.

  - if the process instance is completed: open the process instances list
  - in the filter pane, select **Completed** to show the completed instances
  - select the completed instance and check the process history opening the diagram.

5. If a manager is involved, s/he has to take charge of the *Approve* task, where s/he can approve or reject the order providing a motive.

6. If the manager rejects the order, the purchase expert receives the *Order Reject* task to acknowledge the order rejection reason.

7. The process happy path completes approving the order, the final step is an integration task that send the order information to the **ERP system** in order to finalize the order.

### Before the demo

- in order to create a list of running instances that make the demo more realistic. Start **OM-demo-init** process, do not change any value in the form.


Task Assignment
-----------------------------------

All the task are assigned to the group `rest-all` in order to easily deploy the demo in OpenShift Container Platform.
If in your environment, you have multiple users and roles you can change the assignment accordingly.

- Task: **Request Offer** - User Group: `rest-all`
- Task: **Prepare Offer** - User Group: `rest-all`, this task is supposed to be assigned dynamically to the suppliers selected at previous task (*Request Offer*). If in your environment you can define these users: supplier1, supplier2, supplier3, then you can remove the group and assign the actor to `#{supplier}`
- Task: **Approve** - User Group: `rest-all`
- Task: **OrderRejected** - User Group: `rest-all` 


Change Log
-----------------------------------

- Branch 7.4 (PAM version 7.4): 

    - Version 7.4.1 update, task normalization, version update and documentation
    - Version 7.4 update

- Branch 7.3 (PAM version 7.3): 

    - Version 7.3 multiple suppliers in parallel, DMN decision, demo initialization process

- Branch master (PAM version 7.0)

    - 2019-03-15: process description with order item
    - 2019-03-13: documentation improved
    - 2019-01-29: changed user group to match existing one in OCP standard image.

TODO
-----------------------------------

- SLA
- documents
- signal
- compensation
- error handling