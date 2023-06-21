轻触按键：相当于是一种电子开关，按下时开关接通，松开时开关断开，实现原理是通过轻触按键内部的金属弹片受力弹动来实现接通和断开

这时就需要使用在硬件方面或者软件方面来控制

硬件就是连接触发器来解决,软件就是调用20ms的休眠来跳过抖动时段, 并通过while来接收我们按下的信息,等松手的时候跳出循环, 然后改变寄存器的值,达到控制灯亮灭的效果

如果不按独立按键,则对应的电路都是高电平,为1

按了则为0
```c
#include <REGX52.H>
#include <intrins.h>
void Delay1ms(unsigned int xms)		//@11.0592MHz
{
	unsigned char data i, j;
	_nop_();
	while(xms--)
	{
		
		i = 2;
		j = 199;
		do
		{
			while (--j);
		} while (--i);
	}
}

int main()
{
	while(1)
	{
		if(P3_1 == 0)
		{
			Delay1ms(20);
			while(P3_1 == 0);
            //松手也会造成波动,休眠20ms
			Delay1ms(20);
			P2_0 = ~P2_0;
            //将P2_0的值按位取反
		}
	}
	
	return 0;
}
```