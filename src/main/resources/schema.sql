CREATE TABLE IF NOT EXISTS "User" (
  "UserID" INT IDENTITY,
  "LoginName" VARCHAR(20),
  "Password"  VARCHAR(20),
  PRIMARY KEY ("UserID")
);

CREATE TABLE IF NOT EXISTS "Product" (
  "ID" INT IDENTITY,
  "Name" VARCHAR(32),
  "Type"  VARCHAR(8),
  "Description" VARCHAR(100),
  PRIMARY KEY ("ID")
);

CREATE TABLE IF NOT EXISTS "Basket" (
  "ID" INT IDENTITY,
  "ProductID" INT,
  "UserID"  INT,
  PRIMARY KEY ("ID")
);

ALTER TABLE "Basket" ADD CONSTRAINT "FK_Basket_User" FOREIGN KEY ("UserID") REFERENCES "User" ("UserID");
ALTER TABLE "Basket" ADD CONSTRAINT "FK_Basket_Product" FOREIGN KEY ("ProductID") REFERENCES "Product" ("ID");
