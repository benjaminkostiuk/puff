# Keycloak setup steps
The following steps were executed to setup the keycloak server. A copy of the _Puff_ realm settings can be found at [puff-keycloak-config.json](./puff-keycloak-config.json).

### Setup client configurations
* Create a new realm named **puff**
* Navigate to `clients` and click `Create`
* Create a new client for the course management microservice called `course-management`. Set the protocol to `openid-connect` and the access type to `bearer-only`.
* Create another client for front-end authorization called `user-auth`. Set the protocol to `openid-connect` and the access to `confidential`.

### Setup user roles
* Navigate to the `Roles` page
* Create a new `user` role. This role will be assigned users of the puff platform.
* Create a new `admin` role. This role will be assigned to administrators of the puff platform. Enable the `Composite Roles` switch and add the `user` role.
* Under `Roles` > `Default Roles` add the `user` roles to the Realm Default Roles. This means any new user will automatically obtain the user role.
* Create two test users on the `Users` page and set their passwords to `test`. You should be able to login to their account pages from http://localhost:8180/auth/realms/puff/account/.

### Setup groups
* Create a new `Users` group and assign to it the `user` role. Set the `Users` group as the default group.
* Assign a user to the `Users` group.
* Create a new `Administrators` group and assign to it the `admin` role. Assign a user to the `Administrators` group.
