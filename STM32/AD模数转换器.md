### 函数
|函数名|作用|
|---|---|
|ADC_Cmd|用于给ADC上电,开关控制|
|ADC_DMACmd|开启DMA输出信号,如果使用DMA转运数据,就使用|
|ADC_ITConfig|中断输出控制,控制某个中断,能不能通往NVIC|
|ADC_ResetCalibration|复位校准|
|ADC_GetResetCalibrationStatus|获取复位校准状态|
|ADC_StartCalibration|开始校准|
|ADC_GetCalibrationStatus|获取开始校准状态|
|ADC_SoftwareStartConvCmd|软件触发的函数,也就是触发控制|
|ADC_GetFlagStatus|获取标志位状态,参数给EOC的标志位,判断EOC标志位是不是置一了,如果转换结束,EOC标志位置一|
|ADC_DiscModeChannelCountConfig|每隔几个通道间断一次|
|ADC_DiscModeCmd|是不是启动间断模式|
|ADC_RegularChannelConfig|给序列的每个位置填写指定的通道|
|ADC_ExternalTrigConvCmd|是否允许外部中断触发转换|
|ADC_GetConversionValue|获取AD转换的数据寄存器,读取转换结果|
|ADC_AnalogWatchdogCmd|是否启动模拟看门狗|
|ADC_AnalogWatchdogThresholdsConfig|配置高低阈值|
|ADC_AnalogWatchdogSingleChannelConfig|配置看门的通道|
|ADC_TempSensorVrefintCmd|开启内部的两个通道|

#### 配置GPIO
在AIN模式下,GPIO是无效的,断开GPIO,防止GPIO的输入输出对模拟电压产生干扰  
所以AIN模式是ADC的专属模式

#### 校准函数
|函数名|作用|
|---|---|
|ADC_ResetCalibration|复位校准|
|ADC_GetResetCalibrationStatus|获取复位校准状态|
|ADC_StartCalibration|开始校准|
|ADC_GetCalibrationStatus|获取开始校准状态|

### 配置ADC
1. 开启ADC1,GPIO时钟
2. 选择ADC分频,选择6分频`RCC_ADCCLKConfig`
   1. ADC最高14Hz,stm32是72Hz的频率,所以选择6分频
3. 初始化GPIO,使用专属模式AIN
4. 选择规则组的输入通道
   1. `ADC_RegularChannelConfig`
   2. 根据ADC引脚选择通道
   3. 选择序列1(菜单)
   4. 选择转换时间,需要更快的转换就选择小的参数. 需要更稳定的转换就选择大的参数
5. 初始化ADC
   1. ADC_Init
   2. `ADC_Mode`选择`ADC_Mode_Independent`独立工作模式,其他都是双ADC模式
   3. `ADC_DataAlign`模式对齐,指定是右对齐还是左对齐,选择右对齐
   4. `ADC_ExternalTrigConv`外部触发转换选择,就是触发控制的触发源,本次使用软件触发,就选择`ADC_ExternalTrigConv_None`
   5. `ADC_ContinuousConvMode`连续转换模式,可以选择是连续转换还是单次转换
      1. `ENABLE`和`DISABLE`
      2. `ENABLE`就是连续模式
      3. `DISABLE`就是单次模式
   6. `ADC_ScanConvMode`扫描转换模式,可以选择是扫描模式还是非扫描模式
      1. `ENABLE`就是扫描模式
      2. `DISABLE`就是非扫描模式
   7. `ADC_NbrOfChannel`通道数目,指定在扫描模式下,总共会用到几个通道
      1. 1~16,指定转换所用的序列数量
   8. `ADC_Cmd`开启ADC
   9. 根据手册建议,需要对ADC进行校准
      1. `ADC_ResetCalibration`复位校准
      2. `ADC_GetResetCalibrationStatus`等待复位校准完毕
         1. 如果校准中,返回`SET`
         2. 校准完成,返回`RESET`
         3. 使用循环进行校准判断
      3. `ADC_StartCalibration`开始校准
      4. `ADC_GetCalibrationStatus`等待校准完成

### 获取AD的数据
AD_GetValue函数
1. `ADC_SoftwareStartConvCmd`软件触发转换的函数
2. `ADC_GetFlagStatus`获取标志位状态,
   1. AWM,模拟看门狗标志位
   2. EOC,规则组转换完成标志位
   3. JEOC,注入组转换完成标志位
   4. HSTRT,注入组开始转换标志位
   5. STRT,规则组开始转换标志位
   6. 选择EOC对规则组进行判断,`==RESET`时,转换未完成
3. 等待转换完成后就可以使用`ADC_GetConversionValue`函数获取转换的数据结果
   1. 它的返回值就是转换的结果
   2. 读取DR标志位后会自动清除EOC数据