BKP和FLSH闪存相识,都具有使数据持久化的功能\
但是BKP需要保持电源供电`3.3V`才能保存数据\
FLSH只要不擦除数据就会永久保存\
在主机断开电源后,如果连接了备用电源,里面的计时器不会停止,会继续运行
### 时间戳
时间戳指的就是一种计时方式\
从1970年1月1日开始计时,只计时秒\
计时的储存器分别为32位和64位计时器\
### BKP和RTC
    如果想要还用BKP或者RTC都要先执行步骤1和2
1. 开启PWR和BKP的时钟
2. 使用PWR,使能BKP和RTC的访问

RTC是低速时钟,APB1是高速时钟,只有等RTC_CRL产生上升沿表示准备结束时,才能接受数据,所以需要调用等待函数
### 函数
|函数名|作用|
|---|---|
|BKP_TamperPinLevelConfig|配置TAMPER引脚的有效电频,(高电平触发还是低电平触发)|
|BKP_TamperPinCmd|是否开启侵入检测功能,如果需要侵入检测,就先配置有效电频,再使能即可|
|BKP_ITConfig|中断配置,是否开启中断|
|BKP_RTCOutputConfig|时钟输出功能配置|
|BKP_SetRTCCalibrationValue|设置RTC校准值|
|BKP_WriteBackupRegister|写备份寄存器|
|BKP_ReadBackupRegister|读备份寄存器|
|PWR_BackupAccessCmd|备份寄存器访问使能,设置PWR_CR寄存器里面的DBP位|

### 步骤
1. 开启PWR和BKP的时钟(都是APB1的外设)
2. 使能对BKP和RTC的访问`PWR_BackupAccessCmd`
3. 写函数写入,读函数读出(DR中容量只有1-10)
   1. `BKP_WriteBackupRegister`
   2. `BKP_ReadBackupRegister`

---
### RTC实时时钟
#### 函数
|函数名|作用|
|---|---|
|RCC_LSEConfig|配置LSE外部低速时钟,用来启动LSE时钟|
|RCC_LSICmd|配置LSI内部低速时钟|
|RCC_RTCCLKConfig|选择RTCCLK的时钟源|
|RCC_RTCCLKCmd|启动RTCCLK,调用上面的选择时钟后,还需要调用这个函数使能|
|RCC_GetFlagStatus|获取标志位,第一个启动时钟时,需要等待其启动完成|
|
|RTC_ITConfig|配置中断输出|
|RTC_EnterConfigMode|进入配置模式,使RTC进入配置模式后,才能写入寄存器|
|RTC_ExitConfigMode|退出配置模式|
|RTC_GetCounter|获取CNT计数器的值,读取时钟|
|RTC_SetCounter|写入CNT计数器的值|
|RTC_SetPrescaler|写入预分频器,写入预分频器的PRL重装寄存器中|
|RTC_SetAlarm|写入闹钟值|
|RTC_GetDivider|读取预分频器中的DIV余数寄存器(自减寄存器)为了获取跟精细的事件,CNT计数间隔最短是1s|
|RTC_WaitForLastTask|等待上次操作完成(等待RTOFF状态位为1)|
|RTC_WaitForSynchro|等待同步(RSF置1)|

### 获取实时时钟步骤(Init)
1. 开启PWR和BKP的时钟.使能BKP和PWR的访问
   1. APB1Cmd
   2. PWR_BackupAccessCmd
2. 开启LSE时钟,并等待LSE时钟开启完成
   1. RCC_LSEConfig
      1. LSE_OFF,LSE振荡器关闭
      2. LSE_ON,LSE振荡器打开
      3. LSE_Bypass,LSE时钟旁路(不接晶振,直接从OSE32_IN这个引脚,输入一个指定频率的信号,也可以当做时钟源)
3. 等待LSE启动完成
   1. RTC_GetFlagStatus
      1. RCC_FLAG_LSERDY表示LSE振荡器时钟准备好了
4. 选择RTCCLK时钟源
   1. RCC_RTCCLKConfig
      1. 使用RCC_RTCCLKSource_LSE时钟
   2. RCC_RTCCLKCmd使能时钟
5. 等待同步,等待上一次写入完成
   1. RTC_WaitForSynchro等待同步
   2. RTC_WaitForLastTask等待上一次操作完成
   3. 防止因为时钟不同步,造成bug
6. 配置预分频器
   1. RTC_SetPrescaler
      1. LSE频率是32.768KHz,也就是32768Hz
      2. 选择1Hz,也就是32768 - 1
   2. RTC_WaitForLastTask等待写入完成
7. 设置时间
   1. RTC_SetCounter
   2. 设定初始时间为

如何保证时钟复位不清零;主电源掉电,负电源不掉电时不清零?

使用BKP来处理,随便用BKP存储一个数据,如果上电检测发现BKP没有清零,说明备用电池是存在的

判断代码,如果BKP1里面数据不等于指定数据,就进入判断,给BKP1赋值指定数据,使用函数`BKP_WriteBackupRegister`写入数据;
使用函数`BKP_ReadBackupRegister`读取数据