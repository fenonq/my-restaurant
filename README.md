# MyRestaurant
Final project for EPAM Java courses

### Topic: "Restaurant"

There are roles: Client, Manager.
The client (authorized user) places an order from the menu - the catalog of dishes, and also has the opportunity to view the catalog taking into account the sorting:
- by name of the dish;
- by price;
- by category

and filter the list of dishes by categories. The customer, within one order, can order several dishes. The manager manages orders: receiving a new order, sends it for cooking. After cooking, the Manager passes the delivery order. After delivery and payment, the Manager will transfer the status of the order to "completed".

## Database Schema

<p align="center"><img src="https://imgur.com/9EE4XmJ.png" width="800"></p>

## Launching instruction
- Clone repository
- Set database settings ``` src/main/webapp/META-INF/context.xml ```
- Run ``` sql/db-create.sql ``` to set up database on your device
- Run app using servlet container (Preferred Tomcat v.9.0.56)
