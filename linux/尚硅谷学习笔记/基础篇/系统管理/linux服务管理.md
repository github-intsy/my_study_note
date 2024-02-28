#### Linux中的进程和服务
计算机中,一个正在执行的程序或命令,被叫做"进程"(process)\
启动之后一直存在,常驻内存的进程,一般被称作"服务"(service)

**守护进程**: 在启动系统的同时开启进程,在系统关闭的时候结束的进程被称为守护进程\
守护进程, 守护的就是我们的系统服务\
在linux中, 守护进程和系统服务其实是一回事
#### systemstl(重点掌握)
1. 基本语法\
   `systemctl   start|stop|restart|status   服务名`
2. 经验技巧\
   1. CentOS 6(重启网络)\
        service network restart
    2. CentOS 7(重启网络)\
        systemctl restart network

在CentOS 6中使用的是`service network restart`的格式\
在CentOS 7中使用的是`systemctl restart network`的格式