BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS `Products_Inbound` (
	`Product_ID`	text NOT NULL,
	`Inbound_ID`	int,
	FOREIGN KEY(`Inbound_ID`) REFERENCES `Inbound`(`Inbound_ID`),
	PRIMARY KEY(`Product_ID`)
);
CREATE TABLE IF NOT EXISTS `Products` (
	`Description`	text NOT NULL,
	`Freight_Type`	text NOT NULL,
	`Quantity`	int NOT NULL,
	`Weight`	int NOT NULL,
	`Width`	int NOT NULL,
	`Length`	int NOT NULL,
	`Height`	int NOT NULL,
	`Product_ID`	text,
	`Customer_ID`	text,
	FOREIGN KEY(`Customer_ID`) REFERENCES `Customer`(`Customer_ID`),
	FOREIGN KEY(`Product_ID`) REFERENCES `Bin_Products`(`Product_ID`),
	FOREIGN KEY(`Product_ID`) REFERENCES `Products_Inbound`(`Product_ID`),
	FOREIGN KEY(`Product_ID`) REFERENCES `Product_Orders`(`Product_ID`)
);
CREATE TABLE IF NOT EXISTS `Product_Orders` (
	`Product_ID`	text NOT NULL,
	`Order_ID`	int,
	FOREIGN KEY(`Order_ID`) REFERENCES `Orders`(`Order_ID`),
	PRIMARY KEY(`Product_ID`)
);
CREATE TABLE IF NOT EXISTS `Owner` (
	`username`	TEXT,
	`password`	TEXT
);
INSERT INTO `Owner` VALUES ('TFoster','Fowlers94');
CREATE TABLE IF NOT EXISTS `Outbound` (
	`Outbound_ID`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`Payor`	text NOT NULL,
	`Ship_Date`	text NOT NULL,
	`Truck_Num`	int NOT NULL
);
CREATE TABLE IF NOT EXISTS `Orders` (
	`Order_ID`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`Quantity_Ordered`	int NOT NULL,
	`Date_Ordered`	text NOT NULL,
	`Date_Required`	text NOT NULL,
	`Outbound_ID`	int,
	`Customer_ID`	text,
	FOREIGN KEY(`Outbound_ID`) REFERENCES `Outbound`(`Outbound_ID`),
	FOREIGN KEY(`Customer_ID`) REFERENCES `Customer`(`Customer_ID`)
);
CREATE TABLE IF NOT EXISTS `Invoice` (
	`Invoice_ID`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`Days_Stored`	int NOT NULL,
	`Storage_Cost_Per_Day`	Float ( 3 , 2 ) NOT NULL,
	`Shipping_Cost`	Float ( 5 , 2 ) NOT NULL,
	`Payment_Type`	Varchar ( 80 ) NOT NULL,
	`Outbound_ID`	int,
	`Customer_ID`	text,
	FOREIGN KEY(`Outbound_ID`) REFERENCES `Outbound`(`Outbound_ID`),
	FOREIGN KEY(`Customer_ID`) REFERENCES `Customer`(`Customer_ID`)
);
CREATE TABLE IF NOT EXISTS `Inbound` (
	`Inbound_ID`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`Date_Received`	varchar ( 12 ) NOT NULL,
	`Quantity_Received`	int NOT NULL,
	`Truck_Num`	int NOT NULL
);
INSERT INTO `Inbound` VALUES (1,'',0,0);
CREATE TABLE IF NOT EXISTS `Customer_Inbound` (
	`Customer_ID`	INTEGER NOT NULL,
	`Inbound_ID`	INTEGER NOT NULL,
	CONSTRAINT `PK_Customer_Inbound` PRIMARY KEY(`Customer_ID`,`Inbound_ID`),
	FOREIGN KEY(`Customer_ID`) REFERENCES `Customer`(`Customer_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
	FOREIGN KEY(`Inbound_ID`) REFERENCES `Inbound`(`Inbound_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
);
CREATE TABLE IF NOT EXISTS `Customer` (
	`Customer_ID`	text NOT NULL,
	`Name`	Varchar ( 60 ) NOT NULL,
	`City`	Varchar ( 20 ) NOT NULL,
	`State`	Varchar ( 11 ) NOT NULL,
	`ZipCode`	int NOT NULL,
	`Country`	Varchar ( 40 ) NOT NULL,
	`Email`	text NOT NULL,
	`Phone`	text NOT NULL,
	`Fax`	text NOT NULL,
	PRIMARY KEY(`Customer_ID`)
);
INSERT INTO `Customer` VALUES ('1','','','',0,'','','','');
INSERT INTO `Customer` VALUES ('AB210','ABS','Brockton','MA',2302,'US','ABS@gmail.com','5085852134','');
INSERT INTO `Customer` VALUES ('3','','','',0,'','','','');
INSERT INTO `Customer` VALUES ('4','','','',0,'','','','');
CREATE TABLE IF NOT EXISTS `Bin_Products` (
	`Product_ID`	text NOT NULL,
	`Bin_ID`	int,
	PRIMARY KEY(`Product_ID`),
	FOREIGN KEY(`Bin_ID`) REFERENCES `Bin`(`Bin_ID`)
);
INSERT INTO `Bin_Products` VALUES ('1',1);
INSERT INTO `Bin_Products` VALUES ('2',54);
CREATE TABLE IF NOT EXISTS `Bin` (
	`Bin_ID`	int NOT NULL,
	`Quantity`	int NOT NULL,
	`Aisle`	TEXT NOT NULL,
	PRIMARY KEY(`Bin_ID`)
);
INSERT INTO `Bin` VALUES (1,1,'A');
INSERT INTO `Bin` VALUES (54,0,'A');
CREATE UNIQUE INDEX IF NOT EXISTS `IPK_ProductOrders_ID` ON `Product_Orders` (
	`Product_ID`
);
CREATE UNIQUE INDEX IF NOT EXISTS `IPK_ProductInbound_ID` ON `Products_Inbound` (
	`Product_ID`
);
CREATE UNIQUE INDEX IF NOT EXISTS `IPK_Outbound_ID` ON `Outbound` (
	`Outbound_ID`
);
CREATE UNIQUE INDEX IF NOT EXISTS `IPK_Order_ID` ON `Orders` (
	`Order_ID`
);
CREATE UNIQUE INDEX IF NOT EXISTS `IPK_Invoice_ID` ON `Invoice` (
	`Invoice_ID`
);
CREATE UNIQUE INDEX IF NOT EXISTS `IPK_Inbound_ID` ON `Inbound` (
	`Inbound_ID`
);
CREATE UNIQUE INDEX IF NOT EXISTS `IPK_Customer_Inbound` ON `Customer_Inbound` (
	`Customer_ID`,
	`Inbound_ID`
);
CREATE UNIQUE INDEX IF NOT EXISTS `IPK_Customer_ID` ON `Customer` (
	`Customer_ID`
);
CREATE UNIQUE INDEX IF NOT EXISTS `IPK_Bin_Products_ID` ON `Bin_Products` (
	`Product_ID`
);
CREATE UNIQUE INDEX IF NOT EXISTS `IPK_Bin_ID` ON `Bin` (
	`Bin_ID`
);
CREATE INDEX IF NOT EXISTS `IFK_Products_BinProducts` ON `Products` (
	`Product_ID`
);
CREATE INDEX IF NOT EXISTS `IFK_ProductsInboundID` ON `Products_Inbound` (
	`Inbound_ID`
);
CREATE INDEX IF NOT EXISTS `IFK_ProductOrdersID` ON `Product_Orders` (
	`Order_ID`
);
CREATE INDEX IF NOT EXISTS `IFK_ProductCustomerID` ON `Products` (
	`Customer_ID`
);
CREATE INDEX IF NOT EXISTS `IFK_OrdersOutboundID` ON `Orders` (
	`Outbound_ID`
);
CREATE INDEX IF NOT EXISTS `IFK_OrdersCustomerID` ON `Orders` (
	`Customer_ID`
);
CREATE INDEX IF NOT EXISTS `IFK_InvoiceOutbound` ON `Invoice` (
	`Outbound_ID`
);
CREATE INDEX IF NOT EXISTS `IFK_InvoiceCustomerID` ON `Invoice` (
	`Customer_ID`
);
CREATE INDEX IF NOT EXISTS `IFK_CustomerID` ON `Customer_Inbound` (
	`Customer_ID`
);
CREATE INDEX IF NOT EXISTS `IFK_BinProducts_BinID` ON `Bin_Products` (
	`Bin_ID`
);
COMMIT;
