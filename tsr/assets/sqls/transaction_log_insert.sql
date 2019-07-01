INSERT INTO TransactionLog(ServiceName, ServiceInputName, ServiceInputType, ServiceOutputType, ServiceInputData, SyncStatus, TransactionDate, SyncDate) 
VALUES(?, ?, ?, ?, ?, ?, datetime('now'), null)