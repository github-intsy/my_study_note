### 操作步骤
1. 使用RCC开启GPIO的时钟
2. 使用GPIO_Init函数初始化GPIO
3. 使用初始化或者输入的函数控制GPIO口
### 使用函数(GPIO初始化)
1. 调用RCC里面的APB2外设时钟控制函数(RCC_APB2PeriphClockCmd)
2. 需要点亮PA0口的LED,所以选择RCC_APB2外设_GPIOA参数
3. 调用GPIO_Init函数,第一个参数输入GPIOA
4. 使用定义好的结构体GPIO_InitTypeDef定义结构体成员
5. 根据GPIO_InitTypeDef在源文件搜索定义好的枚举,赋值给对应的参数,开启对应的模式.这里选择推挽输出.._out_pp
6. 因为我们选择的是0引脚,所以选择GPIO_Pin_0;
7. 最后一样的选出50Mz
8. 把GPIO初始化结构体的地址放在GPIO_Init的第二个参数即可
### 输出函数
|函数名|功能|
|---|---|
|GPIO_SetBits|可以把指定的端口设置成高电平|
|GPIO_ResetBits|可以把指定的端口设置成低电平|
|GPIO_WriteBit|根据第三个参数的值来设置指定的端口|
|GPIO_Write|可以同时对16个端口进行写入操作|
- set就是设置端口值,也就是置高电平
- reset就是清除端口值,也就是置低电平
- 根据LED的连接方式来确定LED是高电平点亮还是低电平点亮
  - 开漏输出的模式下高电平是没有驱动能力的
  - 开漏输出的低电平是有驱动能力的
  - 推挽输出高低电平均有驱动能力
  - 一般情况下使用推挽输出即可,特殊的情况下才会使用开漏输出
### LED流水灯
- 当LED的数量大于1时,就需要将多个LED同时初始化
- 就可以在定义结构体的时候写出`GPIO_Pin_0 | GPIO_Pin_1 | GPIO_Pin_2`
  - 原理: 0,1,2在枚举的结构体里面对应着的1的位置不同
  - 0x0001,0x0002,0x0004,所以1在第一二三位
- 时钟外设也是可以用|来同时定义多个引脚
### 引脚定义
- 在连接电路的时候, 选择哪个端口,端口的型号和数字就确定了
  - 比如: 连接了PB12号口
  - 初始化就选择GPIOB
  - GPIO_Pin_12
### 输入函数
|函数名|参数|功能|
|---|---|---|
|uint8_t GPIO_ReadInputDataBit()|GPIOx,GPIO_Pin|获得指定端口的高低电平,读取外部输入的一个端口值|
|uint16_t GPIO_ReadInputData()|GPIOx|读取整个输入数据寄存器|
|uint8_t GPIO_ReadOutputDataBit()|GPIOx,GPIO_Pin|读取输出数据寄存器的某一位,一般用于输出模式下,用来查看自己输出的是什么,可以用来查看LED输出的是什么|
|uint16_t GPIO_ReadOutputData()|GPIOx|用来读取整个输出寄存器的|