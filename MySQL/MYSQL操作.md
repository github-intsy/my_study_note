<details><summary>目录</summary>

- [编码格式](#编码格式)
- [修改数据库](#修改数据库)
- [备份和恢复](#备份和恢复)
- [查看当前数据库访问用户](#查看当前数据库访问用户)
- [数据表操作](#数据表操作)
  - [创建表](#创建表)
  - [查看表结构](#查看表结构)
  - [修改表](#修改表)
    - [修改表名](#修改表名)
    - [修改列名](#修改列名)
  - [新增数据](#新增数据)
  - [删除属性](#删除属性)
- [修改表项内容](#修改表项内容)


</details>


## 编码格式
```
//校验规则，不区分大小写
default-character-set=utf8
default-collation=utf8_general_ci

//区分大小写
default-character-set=utf8
default-collation=utf8_bin
```
可以通过查找指定元素查看大小写

## 修改数据库
`alter database b1 chatset=gbk collate gbk_chinese_ci`\
修改数据库b1的编码格式

```
/*!40100 DEFAULT CHARACTER SET utf8 */
```
如果mysql版本大于40100，就执行

## 备份和恢复
`mysqldump -P3306 -uroot -p -B helloworld`\
备份数据库到本地\
如果没有带上`-B`，在恢复时需要先创建空数据库，使用数据库，再使用source来还原。\
带上`-B`就是在前面加上create指令\
`source /root/algorithm/test1.sql`\
直接执行恢复数据库创建到备份的所有指令

## 查看当前数据库访问用户
`show processlist`

## 数据表操作
### 创建表
```
create table table_name(
    field1 datatype,
    field1 datatype,
    field1 datatype
) character set 字符集 collate 校验规则 engine 存储引擎;

在datatype后面增加comment '说明';
可以增加选项说明
```
### 查看表结构
- `desc table_name`\
查看表详细信息

- `show create table table_name \G`\
查看创建表时的指令

### 修改表
#### 修改表名
`alter table OldName rename to NewName`\
to可以省略
#### 修改列名
`alter table table_name change 列名 新列名 varchar(40) DEFAULT NULL;`
### 新增数据
- 新增一列\
`alter table table_name add 列名 列属性 comment '描述' after 列名b`\
将新增列添加到**列名b**的后面
- 修改表长度\
`alter table 表名 modify 属性名 varchar(20)`\
本质上是覆盖
### 删除属性
`alter table table_name drop 列名`\
如果删除，曾经的属性全部消失

## 修改表项内容
`update table_name set name='张三' where id=101`\
将`id`值为**101**的表项中`name`值修改为**张三**