### 函数
|函数名|作用|
|---|---|
|DMA_ITConfig|中断输出使能|
|DMA_SetCurrDataCounter|设置当前数据寄存器,给传输计数器写数据的|
|DMA_GetCurrDataCounter|DMA获取当前数据寄存器,返回传输计数器的值|
|DMA_GetFlagStatus|获取标志位状态|

#### DMA_InitStructure
    因为选择了存储器到存储器的转运
    所以可以选择任意通道,如果选择硬件触发,就只能选择指定的通道进行转运
    
    DMA_Mode不能运用在存储器到存储器的情况下,自动重装和软件触发不能同时使用,如果同时使用,DMA就会连续触发,永远也不会停下来,所以选择参数为DMA_Mode_Normal正常模式.
|成员|参数|
|---|---|
|DMA_Init|DMA1Channel1|
|DMA_PeripheralBaseAddr|外设站点的基地址,根据传递的地址设置|
|DMA_PeripheralDataSize|选择`DMA_PeripheralDataSize_Byte`以字节形式转换|
|DMA_PeripheralInc|地址是否自增,如果储存数据的地址连续,就自增|
|DMA_MemoryBaseAddr|存储器站点的基地址|
|DMA_MemoryDataSize|传输数据大小,选择字节|
|DMA_MemoryInc|选择自增ENABLE|
|DMA_DIR|指定外设站点是源端还是目的地`DMA_DIR_PeripheralSRC`外设站点作为SRC,源头,外设站点到存储器的传输方向,DataA外设站点,DataB存储器站点|
|DMA_BufferSize|传输寄存器,指定传输几次0~65535|
|DMA_Mode|选择DMA模式|
|DMA_M2M|DMA是否应用于存储器到存储器的转运,也就是是否为软件触发模式|
|DMA_Priority|选择通道优先级|

### 步骤
1. 开启时钟
   1. RCC_AHBPeriphClockCmd
   2. DMA是AHB总线的设备,所以要用到AHB开启时钟的函数
2. 初始化DMA
   1. DMA_Init
   2. 定义DMA结构体初始化DMA
3. 启动DMA

### MyDMA_Transfer函数
        传输计数器赋值,必须要给DMA失能
1. DMA失能
2. `DMA_SetCurrDataCounter`设置传输计数器的值
3. DMA使能
4. 转运开始后,需要完成一个动作(等待转运完成)
5. `DMA_GetFlagStatus`,选择转运完成标志位,用`while`等待转运,`while == reset`
   1. 标志位置一后,需要手动清除标志位
   2. 使用`DMA_ClearFlag`清除标志位

### DMA转运ADC信号
- 将DMA配置成硬件触发
- ADC配置成4个端口
- 源地址不自增,目标地址自增
- 使用ADC_DMACmd函数开启DMA的硬件信号(在ADC_Cmd使能之前)
- 获得ADC转换完成的信号函数中
  - 转运总是在转换之后的
  - 所以就不用等待ADC转换完成的代码了,而是需要等待DMA转运完成的代码

**注意:** 上面的步骤为ADC不连续转换,DMA不循环转运\
如果使用了ADC连续转换,DMA循环转运,就不需要创建函数获取ADC的值了,而是两者一直在工作,始终把最新的结果刷新到对于的数组中