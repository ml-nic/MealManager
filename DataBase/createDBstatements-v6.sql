-- Creator:       MySQL Workbench 5.2.47/ExportSQLite plugin 2013.08.05
-- Author:        Daniel
-- Caption:       New Model
-- Project:       Name of the project
-- Changed:       2016-11-14 12:59
-- Created:       2016-10-24 15:29
PRAGMA foreign_keys = OFF;

-- Schema: mmdb
BEGIN;

DROP TABLE IF EXISTS "recipe";

CREATE TABLE IF NOT EXISTS "recipe"(
  "recipeID" INTEGER PRIMARY KEY NOT NULL,
  "name" VARCHAR(60),
  "preparation" VARCHAR(6000),
  "description" VARCHAR(6000),
  "healthrating" DOUBLE,
  "cookingtime" INTEGER,
  "favourite" INTEGER,
  "imageID" VARCHAR(60),
  "servings" INTEGER,
  "type" VARCHAR(60),
  "course" VARCHAR(60)
);

DROP TABLE IF EXISTS "item";

CREATE TABLE IF NOT EXISTS "item"(
  "itemID" INTEGER PRIMARY KEY NOT NULL,
  "name" VARCHAR(60),
  "unit" VARCHAR(60)
);

DROP TABLE IF EXISTS "recipeitems";

CREATE TABLE IF NOT EXISTS "recipeitems"(
  "recipeID" INTEGER NOT NULL,
  "itemID" INTEGER NOT NULL,
  "amount" INTEGER,
  "unit" VARCHAR(60),
  PRIMARY KEY("recipeID","itemID"),
  CONSTRAINT "fk_recipe_has_item_recipe"
    FOREIGN KEY("recipeID")
    REFERENCES "recipe"("recipeID"),
  CONSTRAINT "fk_recipe_has_item_item1"
    FOREIGN KEY("itemID")
    REFERENCES "item"("itemID")
);
CREATE INDEX "recipeitems.fk_recipe_has_item_item1_idx" ON "recipeitems"("itemID");
CREATE INDEX "recipeitems.fk_recipe_has_item_recipe_idx" ON "recipeitems"("recipeID");

DROP TABLE IF EXISTS "plannedrecipe";

CREATE TABLE IF NOT EXISTS "plannedrecipe"(
  "dateplanned" VARCHAR(60) PRIMARY KEY NOT NULL
);

DROP TABLE IF EXISTS "userplannedrecipe";

CREATE TABLE IF NOT EXISTS "userplannedrecipe"(
  "dateplanned" VARCHAR(60) NOT NULL,
  "recipeID" INTEGER NOT NULL,
  "plannedservings" INTEGER,
  PRIMARY KEY("dateplanned","recipeID"),
  CONSTRAINT "fk_plannedrecipe_has_recipe_plannedrecipe1"
    FOREIGN KEY("dateplanned")
    REFERENCES "plannedrecipe"("dateplanned"),
  CONSTRAINT "fk_plannedrecipe_has_recipe_recipe1"
    FOREIGN KEY("recipeID")
    REFERENCES "recipe"("recipeID")
);
CREATE INDEX "userplannedrecipe.fk_plannedrecipe_has_recipe_recipe1_idx" ON "userplannedrecipe"("recipeID");
CREATE INDEX "userplannedrecipe.fk_plannedrecipe_has_recipe_plannedrecipe1_idx" ON "userplannedrecipe"("dateplanned");

DROP TABLE IF EXISTS "shoppinglist";

CREATE TABLE IF NOT EXISTS "shoppinglist"(
  "shoppinglistID" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL
);

DROP TABLE IF EXISTS "shoppinglistitems";

CREATE TABLE IF NOT EXISTS "shoppinglistitems"(
  "shoppinglistID" INTEGER NOT NULL,
  "itemID" INTEGER NOT NULL,
  "amount" DOUBLE,
  "dateadded" VARCHAR(60),
  "datebought" VARCHAR(60),
  "bought" INTEGER DEFAULT 0,
  PRIMARY KEY("shoppinglistID","itemID"),
  CONSTRAINT "fk_shoppinglistitem_has_item_shoppinglistitem1"
    FOREIGN KEY("shoppinglistID")
    REFERENCES "shoppinglist"("shoppinglistID"),
  CONSTRAINT "fk_shoppinglistitem_has_item_item1"
    FOREIGN KEY("itemID")
    REFERENCES "item"("itemID")
);
CREATE INDEX "shoppinglistitems.fk_shoppinglistitem_has_item_item1_idx" ON "shoppinglistitems"("itemID");
CREATE INDEX "shoppinglistitems.fk_shoppinglistitem_has_item_shoppinglistitem1_idx" ON "shoppinglistitems"("shoppinglistID");

COMMIT;
