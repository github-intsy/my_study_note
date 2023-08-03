### 硬件电路
- 所有I2C设备的SCL连在一起,SDA连在一起
- 设备的SCL和SDA均要配置成开漏输出模式
- SCL和SDA各添加一个上拉电阻,阻值一般为4,7千欧左右
##### 开漏输出模式
GND设备引脚直接接地,形成强下拉;不接正极,形成浮空模式
整个设备引脚只有强下拉和浮空输入两种状态
### 应答(发送数据)
1. 主机发送一个字节的地址,选择要操作的从机
   1. 从机下拉SDA表示应答
   2. 最低位表示读写位
      1. 0表示,之后的操作,主机要进行写入操作
      2. 1表示,之后的时序,主机要进行读出操作
2. 主机发送一个字节的地址,选择从机中的发送数据的位置
3. 主机发送一个字节,表示要在指定从机的指定地址发送的数据
### 接收(接收数据)
1. 主机发送一个字节的地址,最低位为1,选择指定从机
   1. 接收从机应答位
2. 马上转换为主机接收数据,从机发送数据
   1. 没有指定寄存器,使用的是指针变量指向的寄存器读取数据
      1. 指针变量每次读取数据后,向后移动一位,指向下一个寄存器
### 接收(指定地址读)
因为接收只能是在当前地址读,不能指定地址,所以出现了能够指定地址读的操作\
该操作是前半段发送数据,后半段当前地址读的复合操作
1. 起始条件
2. 指定设备地址并且最低位是0
3. 指定设备中寄存器的地址
4. 重复起始条件
5. 指定设备地址并且最低位为1
6. 读取数据
7. 结束

如果只想读取一个数据接停止,需要给1(非应答)\
表示主机没有应答,这样,就会让从机释放总线,将SDA控制权交还给主机\
如果主机想要读取多个字节,需要在最后一个字节给非应答(1)\
其他字节之间给应答(0)

### 寄存器
如果想要写入数据,就必须解除芯片的睡眠模式\
解除的方法:\
给PWR_MGMT_1寄存器传输数据为0x00即可解除睡眠模式\
该寄存器的地址为0x6B

---
### 硬件I2C
#### 步骤
1. 开启I2C外设和对应GPIO口的时钟
2. 把I2C外设对应的GPIO口初始化成复用开漏模式
3. 使用结构体,对I2C进行配置
4. I2C_Cmd,使能I2C
#### 函数
|函数名|作用|
|---|---|
|I2C_GenerateSTART|生成起始条件|
|I2C_GenerateSTOP|生成终止条件|
|I2C_AcknowledgeConfig|配置CR1的ACK这一位,在收到一个字节后,是否给从机应答|
|I2C_SendData|发送数据.就是将Data这个数据直接写入到DR寄存器|
|I2C_ReceiveData|读取DR的数据作为返回值|
|I2C_Send7bitAddress|发送7位地址的专用函数,自动帮忙设置最低位数据|
|I2C_CheckEvent`需要掌握`|(基本状态监控)同时判断一个活多个标志位|
|I2C_GetLastEvent`了解即可`|(高级状态监控)将SR1和SR2两个状态寄存器拼成16位返回|
|I2C_GetFlagStatus`熟悉的方法`|(基于标志位的状态监控)可以判断摸一个标志位是否置一了|
#### 代码细节(初始化I2C)
1. `I2C2`需要使用`APB1外设`
2. `GPIO`需要使用`APB2外设`
3. 将`GPIOB`的GPIO口配置成复用开漏输出模式,也就是`AF_OD`
   1. `开漏`,是I2C协议的设置要求
   2. `复用`,是GPIO的控制权要交给硬件外设
   3. 因为这是硬件I2C,所以控制GPIO就只能交给外设来做
4. 初始化I2C外设`I2C_Init`
   1. `I2C_Mode`选择`I2C_Mode_I2C`参数
   2. `I2C_ClockSpeed`配置SCL的时钟频率(数值大,SCL频率高,数据传输快)(取值范围0-400kMz)(0-100kMz普通模式)(100-400)快速模式
   3. `I2C_DutyCycle`时钟占空比参数只有`I2C_ClockSpeed`时钟频率>100kMz才有效
      1. 如果是<100kMz,就是1:1
      2. 16:9就是高电平和低电平时间是16:9的关系
      3. 2:1就是高电平和低电平时间是2:1的关系
   4. `I2C_Ack`是否给从机应答
   5. `I2C_AcknowledgedAddress`指stm32作为从机,可以响应几位的地址
   6. `I2C_OwnAddress1`自身地址1,用于指定stm32的自身地址,**注意**:需要和上面的`I2C_AcknowledgedAddress`选择相同的几位地址
5. `I2C_Cmd`使能I2C
#### 写寄存器函数
1. `I2C_GenerateSTART`起始条件
2. `I2C_CheckEvent`等待EV5事件的到来`I2C_EVENT_MASTER_MODE_SELECT`就是EV5
3. `I2C_Send7bitAddress`发送从机地址`I2C_Direction_Transmitter`选择发送数据0
4. `I2C_CheckEvent`等待EV6事件的到来`I2C_EVENT_MASTER_TRANSMITTER_MODE_SELECTED`就是EV6
5. EV6_1发送数据到DR,使用`I2C_SendData`选择从机寄存器地址
   1. RegAddress
6. `MPU6050_WaitEvent`等待EV8事件的结束
7. `I2C_SendData`发送数据Data
8. `MPU6050_WaitEvent`如果不发送就等待EV8_2事件
9. `I2C_GenerateSTOP`终止时序
#### 读寄存器函数
1. `I2C_GenerateSTART`起始条件
2. `I2C_CheckEvent`等待EV5事件的到来`I2C_EVENT_MASTER_MODE_SELECT`就是EV5
3. `I2C_Send7bitAddress`发送从机地址`I2C_Direction_Transmitter`选择发送数据0
4. `I2C_CheckEvent`等待EV6事件的到来`I2C_EVENT_MASTER_TRANSMITTER_MODE_SELECTED`就是EV6
5. EV6_1发送数据到DR,使用`I2C_SendData`选择从机寄存器地址
   1. RegAddress
6. `MPU6050_WaitEvent`等待EV8事件的结束
   1. 如果数据没有发送完成,下面的重复起始条件就会开始等待,直到数据发送完毕
7. `I2C_GenerateSTART`重复起始条件
8. 等待EV5事件`I2C_CheckEvent`
9. `I2C_Send7bitAddress`接收从机地址
   1.  `I2C_Direction_Receiver`最低位置一
10. 等待EV6事件(接收)`I2C_EVENT_MASTER_RECEIVER_MODE_SELECTED`主机接收EV6事件参数,使用`I2C_CheckEvent`函数
11. stop置1,Ack置0
    1.  因为数据收到之前,应答位已经发送出去了
    2.  `I2C_AcknowledgeConfig`ACk置0
    3.  `I2C_GenerateSTOP`stop置1
12. `MPU6050_WaitEvent`等待EV7事件
13. `I2C_ReceiveData`读取DR的数据,返回值就是DR数据
14. `I2C_AcknowledgeConfig`ACK置1
    1.  默认条件下ACK就是1,给从机应答
    2.  最后一个字节前,ACK置0,给非应答
    3.  这样做方便指定地址收多个字节