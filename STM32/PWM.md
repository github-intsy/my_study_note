### 配置步骤
1. 配置RCC外部时钟
2. 配置时基单元
3. 配置输出比较单元
4. 配置GPIO,把PWM对应的IO口,初始化成复用推挽输出的配置
5. 运行控制,启动计数器
### 函数
|函数名|功能|
|---|---|
|TIM_OC1Init|结构体初始化输出比较单元|
|TIM_SetCompare1|设置比较单元|
### 配置步骤
1. 开启TIM2和GPIOA的时钟
2. 初始化GPIO口
   1. 配置成复用推挽输出,50mz,选择IO口
3. `TIM_InternalClockConfig(TIM2)`
   1.  采用内部时钟给TIM2提供时钟源
4. `TIM_TimeBaseInit(TIM2, &TIM_TimeBaseInitStructure)`, 向上计数,1分频,`RepetitionCounter`是高级计时器才有的,直接给0
   1. `TIM_TimeBaseInit`初始化时基单元
   2. `TIM_Period`就是公式中的ARR
   3. `TIM_Prescaler`就是公式中的PSC
5. `TIM_OCInit`, 结构体成员里面有很多都是高级定时器才用到的参数,所以选择部分就行
   1. `OCMode`//设置输出比较的模式
   2. `OCPolarity`//设置输出比较的极性
   3. `OutPutState`,设置输出使能
   4. `TIM_Pulse`//就是CCR
6. 启动定时器
---
### 引脚重映射
      引脚有时会出现和调试端口共用一个引脚的情况
      这个时候就使用GPIO_PinRemapConfig,引脚重映射配置

      比如P15的IO口,它上电后就默认复用为了调试端口JTDI
      如果想让他作为普通的GPIO或者复用定时器的通道
      就需要关闭调试端口的复用
      也就是使用GPIO_PinRemapConfig这个函数
#### 解除引脚重映射
1. 开启AFIO外部时钟
   1.  RCC_APB2PeriphClockCmd(RCC_APB2Periph_AFIO,ENABLE)
2. 使用解除重映射函数GPIO_PinRemapConfig

|复用功能|TIM2_REMAP[1:0]=00(没有重映射)|TIM2_REMAP[1:0]=01(部分重映射)|TIM2_REMAP[1:0]=10(部分重映射)|TIM2_REMAP[1:0]=11(完全重映射)|
|---|---|---|---|---|
|TIM2_CH1_ETR|PA0|PA15|PA0|PA15|
|TIM2_CH2|PA1|PB3|PA1|PB3|
|TIM2_CH3|PA2|PA2|PB10|PB10|
|TIM2_CH4|PA3|PA3|PB11|PB11|
1. 当不重映射时，默认TIM2四个的IO口是PA0、PA1、PA2、PA3
2. 要使用PA15、PB3、PA2、PA3的端口组合，要调用下面的语句进行部分重映射：
   1. `RCC_APB2PeriphClockCmd(RCC_APB2Periph_AFIO, ENABLE);`   //重映射必须要开AFIO时钟
   2. `GPIO_PinRemapConfig(GPIO_PartialRemap1_TIM2, ENABLE);`
3. 要使用PA0、PA1、PB10、PB11的端口组合，要调用下面的语句进行部分重映射：
   1. `RCC_APB2PeriphClockCmd(RCC_APB2Periph_AFIO, ENABLE);`   //重映射必须要开AFIO时钟
   2. `GPIO_PinRemapConfig(GPIO_PartialRemap2_TIM2, ENABLE);`
4. 要使用PA15、PB3、PB10、PB11的端口组合，要调用下面的语句进行完全重映射：
   1. ` RCC_APB2PeriphClockCmd(RCC_APB2Periph_AFIO, ENABLE);`   //重映射必须要开AFIO时钟
   2.  `GPIO_PinRemapConfig(GPIO_FullRemap_TIM2, ENABLE);`

同时还要禁用JTAG功能，PA15、PB3、PB10、PB11才会正常输出。

禁用JTAG就需要使用`GPIO_Remap_SWJ_JTAGDisable`参数

---
### 舵机
      舵机采用单线传输数据
      舵机要求的周期是20ms, 频率就是1/20ms = 50Hz
      则ARR是20000-1,PSC是72-1
      CCR的取值范围就是500-2500,对应的就是0.5ms-2.5ms
---
### 配置时基单元
1. `RCC`开启时钟,把`GPIO`和`TIM`的时钟打开
2. `GPIO`初始化,把`GPIO`配置成输入模式
3. 配置时基单元,让`CNT`计数器在内部时钟的驱动下自增运行
4. 配置输入捕获单元
5. 选择从模式的触发源
6. 调用`TIM_Cmd`,开启定时器

   需要使用`GPIO_Mode_IPU`,上拉输入
   ARR给到最大,65536-1
   PSC给720-1
---
### 启动直流电机
1. 开启对应的引脚
2. 电机为交叉电路,一端高电平,一端低电平就可以控制电机运转,改变电流输入方向就可以改变电机运转方向
3. 使用PWM控制电流频率来控制电机速度
4. 使用`GPIO_SetBits`函数控制引脚高电平,`ResetBits`控制低电平
---
### 输入捕获
      TIM2需要输出PWM,所以输入捕获需要换成TIM3
      TIM3也是APB1的外设
      选择上拉输入模式IPU
      ARR的值最好要设置大一些,防止技术溢出->采用测周法测频率
      所以N的值要越大越好,这里N对应着CNT传给CCR的值
1. 开启`TIM3`、`GPIOA`的外部时钟 | `TIM2`需要输出`PWM`,所以输入捕获使用`TIM3`,`TIM3`也是`APB1`的外设
2. 对应的引脚初始化,选择IPU上拉模式
3. 采用内部时钟给TIM3提供时钟源`TIM_InternalClockConfig`如果不提供时钟源,定时器就不能计时
4. 初始化时基单元`TIM_TimeBaseInit`
5. 定时器输入初始化`TIM_ICInit`
   1. `TIM_Channel`指定使用哪个通道(选择1)
   2. `TIM_ICFilter配置输入捕获的滤波器`(如果信号有毛刺和噪声,就可以增大参数,可以有效避免干扰)(**滤波器**滤波后不会改变频率,只会让信号变得更加平滑,**分频器**会改变频率,1kHz分频后变为500Hz,4分频就是250Hz)
   3. `TIM_ICPolarity`边沿检测,极性选择,选择上升沿触发
   4. `TIM_ICPrescaler`分频器,选择每几次触发捕获事件
   5. `TIM_ICSelection`选择引脚输入(直连通道、交叉通道)
6. 触发源选择、主从模式配置好
7. 配置TRGI的触发源`TIM_SelectInputTrigger`选择TI1FP1触发源,可查看PPT
8. 配置从模式为Reset使用`TIM_SelectSlaveMode`查看PPT的从模式下面四种模式,上面的是给编码器接口用的
9. TIM_Cmd启动定时器TIM3

如果没有数据过来,就会不断自增;如果有数据过来,就会在从模式的作用下自动清零

当我们想要查看频率时,需要读取CCR进行计算

|方法|内容|公式|
|---|---|---|
|测频法|在闸门时间T内,对上升沿计次,得到N,则频率Fx|Fx=N/T|
|测周法|两个上升沿内,以标准频率Fc计次,得到N,则频率Fx|Fx=Fc/N|
|中界频率|测频法和测周法误差相等的频率点|Fm=√Fc/T|

|参数|得法|
|:---:|:---:|
|Fc|Fc=72M/(PSC+1)|
|N|读取CCR的值,需要使用TIM_GetCapture1函数|

|库函数|作用|
|:---:|:---:|
|TIM_SelectOutputTrigger|选择输出触发源TRGO-选择主模式输出的触发源|
|TIM_SelectSlaveMode|选择从模式|
|TIM_SelectInputTrigger|选择输入触发源TRGI|
---
### PWMI模式测频率占空比
      修改输入捕获模式测量频率的代码
      将程序结构设置成两个通道获取同一个引脚的模式

      可以直接复制 输入捕获模式测量频率的代码 
      将通道1改成通道2,下降沿触发,交叉输入

      库函数里有专门实现这一步的函数,可以实现配置通道2和交叉输入
      TIM_PWMIConfig(TIM3,&之前配置好的直连输入的结构体)
      在TIM_PWMIConfig函数中,会自动把剩下的一个通道初始化成相反的配置
      TIM_PWMIConfig只支持通道1和2的配置,不支持通道3和4

- 高电平的计数值存在CCR2里
- 整个周期的计数值存在CCR1里
- 占空比=CCR2/CCR1
- CCR2 = TIM_GetCapture2()
- CCR1 = TIM_GetCapture1()
  - 返回的数据范围是0~1,可以*100,就是0~100
  - CCR计数从0开始,所以结果可以+1