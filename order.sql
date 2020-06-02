
CREATE TABLE `order` (
`id`  bigint(10) NOT NULL AUTO_INCREMENT COMMENT '主键id' ,
`name`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '商品名称' ,
`price`  decimal(10,2) NULL DEFAULT 0 COMMENT '商品价格，2位小数' ,
`db_source`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '所存的数据库' ,
PRIMARY KEY (`id`)
);

INSERT INTO `order`(name,price,db_source) VALUES ('跟武哥一起学 Spring Boot', 39.99, DATABASE());
INSERT INTO `order`(name,price,db_source) VALUES ('跟武哥一起学 Spring cloud', 39.99, DATABASE());