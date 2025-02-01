### Applying the business rules of the company MA Locação de Máquinas e Equipamentos em um CRUD. Using the Spring framework to develop a REST API with proper error handling and good design patterns.


Some of the features of the software include:

* Even if the value of a product changes later, the total value of previously made dispatches or inbound transactions remains unchanged
* A product can have multiple associated dispatches and inbounds, but once associated, none of them can be deleted to prevent the loss of important records for the company's inventory and tax departments (It can be further developed in the future)
* Each inbound or dispatch can be associated with only one specific product
* The number of units of a product can only be changed through inbound and dispatch transactions to prevent counting errors

Other error handling features:

* Deletion of products with associated inbound and dispatch transactions is not allowed
* Removal of products that exceed the available stock is not allowed
* Negative values are not allowed
* Duplicate products cannot be created


