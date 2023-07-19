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
3. TIM_InternalClockConfig(TIM2)
   1.  采用内部时钟给TIM2提供时钟源
4. TIM_TimeBaseInit(TIM2, &TIM_TimeBaseInitStructure), 向上计数,1分频,RepetitionCounter是高级计时器才有的,直接给0
   1. TIM_TimeBaseInit初始化时基单元
   2. TIM_Period就是公式中的ARR
   3. TIM_Prescaler就是公式中的PSC
5. TIM_OCInit, 结构体成员里面有很多都是高级定时器才用到的参数,所以选择部分就行
   1. OCMode//设置输出比较的模式
   2. OCPolarity//设置输出比较的极性
   3. OutPutState,设置输出使能
   4. TIM_Pulse//就是CCR
6. 启动定时器
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
   1. RCC_APB2PeriphClockCmd(RCC_APB2Periph_AFIO, ENABLE);   //重映射必须要开AFIO时钟
   2. GPIO_PinRemapConfig(GPIO_PartialRemap1_TIM2, ENABLE);
3. 要使用PA0、PA1、PB10、PB11的端口组合，要调用下面的语句进行部分重映射：
   1. RCC_APB2PeriphClockCmd(RCC_APB2Periph_AFIO, ENABLE);   //重映射必须要开AFIO时钟
   2. GPIO_PinRemapConfig(GPIO_PartialRemap2_TIM2, ENABLE);
4. 要使用PA15、PB3、PB10、PB11的端口组合，要调用下面的语句进行完全重映射：
   1.  RCC_APB2PeriphClockCmd(RCC_APB2Periph_AFIO, ENABLE);   //重映射必须要开AFIO时钟
   2.  GPIO_PinRemapConfig(GPIO_FullRemap_TIM2, ENABLE);

同时还要禁用JTAG功能，PA15、PB3、PB10、PB11才会正常输出。

禁用JTAG就需要使用GPIO_Remap_SWJ_JTAGDisable参数
### 舵机
      舵机采用单线传输数据
      舵机要求的周期是20ms, 频率就是1/20ms = 50Hz
      则ARR是20000-1,PSC是72-1
      CCR的取值范围就是500-2500,对应的就是0.5ms-2.5ms
### 配置时基单元
1. RCC开启时钟,把GPIO和TIM的时钟打开
2. GPIO初始化,把GPIO配置成输入模式
3. 配置时基单元,让CNT计数器在内部时钟的驱动下自增运行
4. 配置输入捕获单元
5. 选择从模式的触发源
6. 调用TIM_Cmd,开启定时器

   需要使用GPIO_Mode_IPU,上拉输入
   ARR给到最大,65536-1
   PSC给720-1
