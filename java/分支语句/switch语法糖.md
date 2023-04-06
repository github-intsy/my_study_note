```java
//switch的第一种写法
switch(number)
{
    case 1:
        System.out.pritln("1");
        break;
    case 2:
        System.out.pritln("1");
        break;
    case 3:
        System.out.pritln("1");
        break;
    default:
        System.out.pritln("1");
        break;
}
```
```java
//避免忘写break的情况
switch(number)
{
    case 1 ->
    {
        System.out.pritln("1");
    }
    case 2 ->
    {
        System.out.pritln("1");
    }
    case 3 ->
    {
        System.out.pritln("1");
    }
    default ->
    {
        System.out.pritln("1");
    }
}
```
```java
//几个不同分支,相同结果
switch(number)
{
    case 1,2,3,4,5 ->
    {
        System.out.pritln("1");
    }
    case 6,7 -> System.out.println("2");
    default ->
    {
        System.out.pritln("1");
    }
}
```