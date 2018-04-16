## Upgrade Schema

Table schema changes should be done in `UpgradeSchema.java`. The separation of table
schema changes and data migration changes is done to optimize the upgrade process for
customers.