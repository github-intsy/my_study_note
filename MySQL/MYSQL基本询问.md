<details><summary>目录</summary>

- [create](#create)
  - [单行插入+全列插入](#单行插入全列插入)
  - [多行插入](#多行插入)
  - [插入否则更新](#插入否则更新)
  - [替换](#替换)
- [Retrieve](#retrieve)
  - [全列查询](#全列查询)
  - [指定列顺序查询](#指定列顺序查询)
  - [结果去重](#结果去重)
- [where语句](#where语句)
  - [判断](#判断)
  - [逻辑运算符](#逻辑运算符)
- [结果排序](#结果排序)
- [update修改数据](#update修改数据)
- [delete删除数据](#delete删除数据)
  - [截断表](#截断表)
  - [三种日志文件](#三种日志文件)
- [去重表数据](#去重表数据)


</details>

## create
### 单行插入+全列插入
```sql
/* 单行插入 */
insert into table_name (id, name) values (1, '张三');

/* 全列插入 */
insert into table_name values (1,'张三');

/* 省略into */
insert table_name values(1,'张三');
```

### 多行插入
```sql
insert into table_name (id,name) 
    values (1,'张三'), (2,'李四') ,(3, '王五');
```

### 插入否则更新
```sql
insert into table_name (id, name) values (1,'张三')
    on duplicate key update id=1, name='李四';

Query OK, 2 rows affected(0.47 sec)
-- 0 row affected: 表中有冲突数据，但冲突数据的值和update的值相等
-- 1 row affected: 表中没有冲突数据，数据被插入
-- 2 row affected: 表中有冲突数据，并且数据已经被更新
```

### 替换
```sql
replace into student (id,name) values(3,'李四');
--1 row affected：表中没有冲突数据，数据被插入
--2 row affected：表中有冲突数据，删除后重新插入
--删除后插入，会导致原来的自增值+1
--id是19删除后插入，变成20
```

## Retrieve
### 全列查询
```sql
-- 通常情况下不建议使用*去查询
-- 1. 查询的越多，意味着需要传输的数据量越大
-- 2. 这样会遍历数据查看，导致没有使用索引进行查看
select * from table_name;
```
### 指定列顺序查询
```sql
select name,id from table_name;
-- 以name,id的顺序查询表

select name,id, id+10 from table_name;
-- 可以计算指定列的值并将结果显示在每一行后面

select name,id, id+10 as total from table_name;
-- 将计算出来的表达式名字替换成total

select name 姓名,id 学号, id+10 偏移量 from table_name;
-- 每列都可重命名，as可以省略不写
```
### 结果去重
```sql
select distinct name,id, id+10 from table_name;
-- 将查询结果进行去重
```
## where语句
### 判断
|判断符|作用|
|---|---|
|>,>=,<,<=|大于，大于等于，小于，小于等于|
|=|等于，NULL不安全，例如NULL=NULL的结果就是NULL|
|<=>|等于，NULL安全，例如NULL <=> NULL的结果就是true(1)|
|!=,<>|不等于|
|between a0 and a1|范围匹配，[a0,a1]，如果a0<=value<=a1，返回true|
|in(option,...)|如果是option中的任意一个，返回true|
|is null|是NULL|
|is not null|不是NULL|
|like|模糊匹配。%表示任意多个（包括0个）字符；_表示任意一个字符|

### 逻辑运算符
|运算符|作用|
|---|---|
|and|c中的&&|
|or|c中的或|
|not|条件为true，结果为false|

```sql
select id,class_id,name from student where name like '李%';
-- 查看name属性为李开头的人的指定信息

select * from student where id>=3 and id<=2;
-- 查找id中在2和3之间的所有信息

select name, chinese+english+math from exam_result where chinese+english+math < 200;
-- 查看总分在200以下的人

select name, chinese+english+math as total from exam_result where chinese+english+math < 200;
-- sql语句执行顺序
-- 1. from exam_result 先获取指定表
-- 2. where chinese+english+math < 200; 在表中筛选数据的条件
-- 3. chinese+english+math as total 最后再将指定列显示并重命名

select name,chinese, math, english, chinese+math+english 总分 from exam_result where name like '孙_' or (chinese+math+english>200 and chinese<math and english>80);
-- 要么是孙同学，要么是条件满足的同学
```

## 结果排序
>没有`order by`子句的查询，返回的顺序是未定义的，也就是表原来的顺序，不可信
```sql
-- ASC为升序
-- DESC为降序

select name,math from exam_result order by math desc;
```
>- NULL比任何值都小
```sql
-- 对学生成绩进行排序，按照数学降序、英语升序、语文升序的方式显示
-- 如果数学成绩相等，英语就按照升序进行排序，以此类推
select name,math, english,chinese 
    from exam_result
        order by math desc, english desc, chinese asc;
```
>查看总分并排序
```sql
select name,math+chinese+english as total 
    from exam_result
        order by total;
-- 为什么这次可以使用别名了？？？
-- 因为现有数据才能排序。
```
>仅查看前n条记录
```sql
select * from table_name limit 5;
-- 显示前5行

select * from table_name limit 2,5;
select * from table_name limit 5 offset 2;
-- 从第2行开始，连续查看5行数据

-- 下标从0开始
-- limit是将数据整理好了才显示
-- limit主要是显示，所以是最后执行的
```

## update修改数据
>本质：对查询到的结果进行列值更新
```sql
update table_name set english=80;
-- 将数据表所有元素的英语修改为80

update table_name set english=80 where name='张三';
-- 将张三的英语修改为80

update table_name set english=80,chinese=90 where name='张三';
-- 将张三的英语和语文成绩修改为80和90分

update exam_result set math=math+30 order by chinese+english+math asc limit 3;
-- 将最后三名的数学加上30分
```
## delete删除数据
```sql
delete from table_name;
-- 删除这个数据表

delete from table_name where name='孙悟空';
-- 将孙悟空表项删除

delete from table_name order by english+chinese+math asc limit 1;
-- 将总分成绩最低的删除
```
采用delete将表格删除，不会将auto_increment值置零
### 截断表
>将表的数据清空，不会删除表\
该操作不走事务，不会将操作记录在日志里
```sql
truncate [table] table_name
```
>- 1. 只能对整表操作，不能像delete一样针对部分数据操作
>- 2. 实际上mysql不对数据操作，所以比delete更快，但是truncate在删除数据的时候，并不经过真正的事务，所以无法回滚
>- 3. 会重置auto_increment项

### 三种日志文件
>- bin log\
> 将mysql所有的历史指令保存下来\
> 记录历史sql语句\
> 记录数据本身
>- redo log\
> 保证mysql宕机时数据断点保存，也就是数据崩溃安全策略
>- undo log\

## 去重表数据
> 以去重的方式打印表
```sql
select distinct * from table_name;
```
> 拷贝表结构，创建新表
```sql
create table table_name1 like table_name2;
-- 1拷贝2
```
> 将去重的结果插入到新表中
```sql
insert into table_name2 select distinct * from table_name1;
```
> 将表重命名
```sql
rename table table_name1 to old_table_name;
rename table table_name1 to old_table_name, table_name2 to table_name1;
```
为什么最后通过rename的方式进行？\
因为去重的目标数据表中有可能有文件正在被访问，先重命名等到一切就绪了，然后统一放入、更新、生效等。