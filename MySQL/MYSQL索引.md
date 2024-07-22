
## 概念
MYSQL的服务器，本质上是在内存当中的，所有数据库的CURD操作，全部都是在内存中进行的。索引也是如此
>提高效率的因素：
>1. 组织数据的方式
>2. 算法本身

索引的内存当中一种特点结构的数据结构

## 创建索引
>当数据量过于庞大的时候，直接查询不仅消耗网络和本地资源，而且查找速率过慢，因此，创建索引查找可以改善。
```sql
alter table EMP add index(empno);
```

## 聚簇和非聚簇索引
将b+树和数据分离存方法的索引叫做非聚簇索引

将B+树和数据合并存放的索引叫做聚簇索引

建表的存储引擎默认是innodb

### 创建innodb结构表
创建表结构后出现两个文件`test1.frm`、`test1.ibd`
- `test1.frm`记录创建的表结构
- `test1.ibd`索引和数据结构

innodb的其他列构建普通索引的时候，MYSQL会在b+树的叶子结点存储主键信息。找到主键信息后，再回到主键索引中进行查主键，找到之后，找到叶子结点，就能找到所有数据

### 创建myism表结构
```sql
create table test2(
    id int primary key,
    name varchar(20) not null
    )engine=myism;
```
存储引擎为myism后，出现三个文件
- `test2.frm`表结构
- `test2.MYD`存储的数据
- `test2.MYI`索引结构

**索引的本质就是数据结构**
## 索引操作

### 主键索引
>主键索引因为不能重复，所以效率较高
```sql
create table user(
    id int primary key,
    name varchar(20)
);-- 创建主键索引

create table user(
    id int,
    name varchar(20)
);
alter table user add primary key(id);
-- 添加主键，就会给该属性创建为主键的B+树结构的主键索引
```
### 唯一索引
>在给列添加唯一键约束时，默认创建唯一键索引
```sql
create table use(
    id int primary key, name varchar(30) unique
);

create table use(
    id int primary key, name varchar(30), unique(name)
);

create table use(
    id int primary key, name varchar(30)
);
alter table use add unique(name);
```
### 普通索引
>需要创建索引结构，并且其中元素存在重复时，就创建普通索引
```sql
create table use(
    id int, name varchar(30), index(name)
);
------------------------------------
create table use(
    id int, name varchar(30)
);
alter table use add index(name);
--------------------------------------------
create table use(
    id int, name varchar(30);
);
create index idx_name on use(name);
```
## 查询索引
```sql
show keys from table_name\G
show index from table_name\G
desc table_name\G
```

## 删除索引
```sql
alter table table_name drop primary key;
-- 删除主键索引

alter table table_name drop index 索引名;
-- 删除其他索引
-- 索引名就是show keys from 表名中的key_name 字段

drop index 索引名 on 表名

```
## 复合索引
>将两列内容同时充当key值创建b+树\
未来想高频的通过id来找到name，就可以创建复合索引。\
优化掉原来找到id然后通过主键索引再找name的情况

>索引是最左匹配原则，构建(id,name)复合索引，mysql会先找id，向后匹配。\
可以用(id,name),(id)查找，但是用单name是找不到的
```sql
create table use(
    id int, name varchar(30), index(name, int)
);
-- 和普通索引创建类似，就是指定两列
```
## 索引创建原则
> - 某一列非常频繁被作为查询条件
> - 唯一性太差的字段不适合创建索引
> - 更新太过频繁不适合
> - 永远不会在where中作为条件的

## 全文索引
>存在大量字段需要检索，会使用到全文索引。只有存储引擎是myism才能使用全文索引，并且默认支持英文，不支持中文。

>myism适合高并发读，不适合读写均衡的场景

```sql
explain select * from articles where body like '%database%'\G
-- 能够显示mysql的执行计划
-- key: NULL
-- 未用到索引

select * from articles
where match(title, body) against('database');
```