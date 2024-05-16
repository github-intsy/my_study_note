
## Server代码
<details>

```c++
// UDP Server
1 #include<iostream>
2 #include<functional>
3 #include<string>
4 #include<arpa/inet.h>
5 #include<algorithm>
6 #include<cstdlib>
7 #include<cstdio>
8 #include<cstring>
9 #include<unistd.h>
10 #include<sys/types.h>
11 #include<sys/socket.h>
12 
13 namespace Server
14 {
15     enum{
16         SOKET_ERR=1,
17         BIND_ERR
18     };
19     static const std::string defaultIp = "0.0.0.0";
20         typedef std::function<void\
21             (std::string, uint16_t, std::string)> func_t;                                                                                    
22     class udpServer
23     {
24     public:
25         static const int capa = 1e3;
26         udpServer(const func_t& fb, uint16_t port = 8080, const std::string& ip = defaultIp)
27         :_sockfd(-1),
28         _port(port),
29         _ip(ip),
30         _func(fb)
31         {}
32         ~udpServer()
33         {}                                                                                                                                   
34         void initServer()
35         {
36             //创建sokcet
37             _sockfd = socket(AF_INET, SOCK_DGRAM, 0);
38             if(_sockfd == -1)
39             {
40                 std::cerr << "socket error: " << errno << " : " \
41                     << strerror(errno) << std::endl;
42                 exit(SOKET_ERR);
43             }
44             //绑定ip和套接字
45             struct  sockaddr_in local;
46             bzero((void*)&local, sizeof local);
47             local.sin_family = AF_INET;
48             local.sin_port = htons(_port); 
49             local.sin_addr.s_addr = INADDR_ANY; 
50            int n = bind(_sockfd, (struct sockaddr*)&local, sizeof local);
51            if(n == -1)
52            {
53                std::cerr << "bind error" << errno << strerror(errno)<<std::endl;
54                exit(BIND_ERR);
55            }
56         }
57         void start()
58         {
59             char buff[capa];
60             for(;;)                                                                                                                          
61             {
62                 struct sockaddr_in peer;
63                 socklen_t len = sizeof peer;
64                 ssize_t n = recvfrom(_sockfd,(void*) buff, sizeof buff-1, 0, (struct sockaddr*)&peer, &len);
65                 if(n > 0)
66                 {
67                     buff[n] = 0;
68                     std::string clientip = inet_ntoa(peer.sin_addr);//网络序列, int->点分十进制
69                     uint16_t clientport = ntohs(peer.sin_port);
70                     std::string massage = buff;
71                     _func(clientip, clientport, massage);
72                 }
73             }
74         }
75     private:
76         int _sockfd;
77         uint16_t _port;
78         std::string _ip;
79         func_t _func;
80     };
81 
82 
83 }//namespace Server
```

</details>

## Client代码
<details>

```c++
12 namespace Client
13 {
14     enum ERR{
15         SOKET_ERR=1
16     };
17     static const string defaultIp= "0.0.0.0";
18    class udpClient
19    {
20     public:
21         static const int capa=1e3;                                                                                                         
22        udpClient(uint16_t port = 8080, const string& ip = defaultIp )
23            :_sockfd(-1),
24            _port(port),
25            _ip(ip)
26        {}
27        ~udpClient()
28        {}
29        void initClient()
30        {
31             _sockfd = socket(AF_INET, SOCK_DGRAM, 0);
32             if(_sockfd == -1)
33             {
34                 std::cerr << "socket error: " << errno << " : " \
35                     << strerror(errno) << std::endl;
36                 exit(SOKET_ERR);
37             }
38        }
39        void start()
40        {
41             struct sockaddr_in local;
42             memset(&local, 0, sizeof local);                                                                                               
43             local.sin_port = htons(_port);
44             local.sin_family = AF_INET;
45             local.sin_addr.s_addr = inet_addr(_ip.c_str());
46             string massage;
47             for(;;)
48             {
49                 cout << "Please Enter# ";
50                 cin >> massage;
W> 51                 ssize_t t = sendto(_sockfd, massage.c_str(),massage.size() \
52                         , 0, (struct sockaddr*)&local, sizeof local);
53             }
54             
55        }
56     private:
57         int _sockfd;
58         uint16_t _port;
59         string _ip;
60    };
61 }//namespace Client
```

</details>

`static`修饰的函数只能在本文件中有效

客服端和服务端都可以收发数据
```c++
sendto()//发送数据
recvfrom()//接收数据
//1. 收发用的是用一个文件描述符
```

```c
#include <stdio.h>

FILE *popen(const char *command, const char *type);

int pclose(FILE *stream);

//popen相当于fork + exec* + pipe
//command未来要执行的命令字符串
//type打开文件方式(rwa)

if(cmd.find("rm) != string::npos) return;
//如果不等于结尾, 说明找到了, 此时直接返回
//等于结尾, 说明没找到, 不执行
```
文件描述符可以被子进程继承