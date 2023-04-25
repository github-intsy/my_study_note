import java.util.Iterator;
import java.util.Scanner;
import java.util.ArrayList;
public class listen {
    public static void main(String[] args) {
        ArrayList<String> sing = new ArrayList<>();
        boolean s = true;
        InitSing(sing);
        do {
            menu();
            System.out.print("初始歌曲列表:");
            PrintArray(sing);
            System.out.print("请输入要执行的操作列表:");
            Scanner input = new Scanner(System.in);
            int in = input.nextInt();
            switch (in)
            {
                case 0 -> {
                    System.out.print("请输入要添加的歌曲名称:");
                    Scanner name = new Scanner(System.in);
                    String n = input.next();
                    sing.add(n);
                    System.out.println("已添加歌曲: " + n);
                    System.out.print("当前歌曲列表: ");
                    PrintArray(sing);
                }
                case 1,2 ->{
                    System.out.print("请输入要置顶的歌曲名称:");
                    Scanner name = new Scanner(System.in);
                    String n = input.next();
                    int i = sing.indexOf(n);
                    if(i == -1) {
                        System.out.println("未找到歌曲");
                        break;
                    }
                    System.out.println("已将歌曲" + n + "置顶");

                    sing.remove(i);
                    sing.add(0,n);
                    PrintArray(sing);
                }

            }
        } while (s);
    }
    static void menu()
    {
        System.out.println("-------------" + "欢迎来到点歌系统" + "-------------");
        System.out.println("0.添加歌曲至列表");
        System.out.println("1.将歌曲置顶");
        System.out.println("2.将歌曲前移一位");
        System.out.println("3.退出");
    }
    static void InitSing(ArrayList<String> s)
    {
        s.add("稻香");
        s.add("夜曲");
        s.add("夜的第七章");
        s.add("听妈妈的话");
        s.add("龙卷风");
    }

    static void PrintArray(ArrayList<String> s)
    {
        Iterator<String> it = s.iterator();
        System.out.print('[');
        int i = s.size() - 1;
        while(it.hasNext())
        {
            System.out.print(it.next());
            if(i-- != 0)
            {
                System.out.print(',');
            }
        }
        System.out.println(']');
    }
}

