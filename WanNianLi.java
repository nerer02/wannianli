package 万;

import java.util.Scanner;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WanNianLi {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int year;//定义年份
        int month;//定义月份
        int day;//定义日期
        boolean isRunNian;//判断是否为闰年
        int totalDays = 0;//从1900年到输入日期的总天数

        System.out.println("************************万年历************************");
        System.out.println("请输入年份：");
        year = input.nextInt();
        System.out.println("请输入月份：");
        month = input.nextInt();
        System.out.println("请输入日期：");
        day = input.nextInt();
        int days = 0; //定义输入月份的天数
        //判断是否为闰年
        //闰年：普通闰年（能被4整除,但不能被100整除）、世纪闰年（能被400整除）
        if (((year%4 == 0 && year%100 != 0))||(year%400 == 0)){
            isRunNian = true;
            System.out.println(year+"年是闰年");
        }else{
            isRunNian = false;
            System.out.println(year+"年不是闰年");
        }

        //遍历出从1900年到输入年份的总天数
        //平年365天，闰年366天
        for(int i = 1900; i < year; i++){
            //判断年份i是否为闰年
            if(((i%4 == 0 && i%100 != 0))||(i%400 == 0)){
                //闰年366天
                totalDays += 366;
            }else{
                //平年365天
                totalDays += 365;
            }
        }

        //循环累计月份的天数
        // 遍历月份i的天数
        for (int i = 1; i < month; i++) {
            //判断月份i的天数
            switch (i) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    totalDays += 31;
                    break;
                case 2:
                    //二月分闰月和平月
                    if (isRunNian){
                        totalDays += 29;
                    }else{
                        totalDays += 28;
                    }
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    totalDays += 30;
                    break;
                default:
                    System.out.println("输入信息有误！");;
                    break;
            }
            
        }

        //加上输入日期的天数
        totalDays += day;

        
        //创建一个Calendar实例，并设置它的时间为输入的年份、月份和日期
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day); //注意月份从0开始    //创建一个SimpleDateFormat实例，并设置它的格式为阴历格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd G");
        
        //打印出整个月份的日历
        System.out.println("日\t一\t二\t三\t四\t五\t六");
        
        //将具体的时间遍历出来
        //遍历之前先求出\t
        int beforeDays;//日期前面的\t
        beforeDays = totalDays % 7 + 1;
        if (beforeDays == 7){
            beforeDays = 0;//前面无空格，即为星期天
        }

        //遍历时间，先打印日期前面的\t
        for (int i = 0; i < beforeDays; i++) {
            System.out.print("\t");
        }
        
        //打印日期和阴历日期
        for (int i = 1; i <= days; i++) {
            System.out.print(i+"\t"); //打印阳历日期
            
            String lunarDate = sdf.format(cal.getTime()); //格式化阴历日期
            
            System.out.print(lunarDate+"\t"); //打印阴历日期
            
            cal.add(Calendar.DATE, 1); //增加一天
            
            //满七个换行
            //日期前的\t+遍历到的日期=7，就换行
            if ((beforeDays+i)%7 == 0){
                System.out.println();
            }
        }

        //打印出输入的日期和阴历日期
        System.out.println("输入的日期是：" + year + "年" + month + "月" + day + "日");
        
        //创建一个Calendar实例，并设置它的时间为输入的年份、月份和日期
        cal.set(year, month - 1, day); //注意月份从0开始
        
        String lunarDate = sdf.format(cal.getTime()); //格式化阴历日期
        
        //调用getLunar方法，获取农历的年份、月份和日期
        int[] lunar = getLunar(year, month, day);
        
        //将农历的年份、月份和日期转换为中文
        String lunarYear = toChineseYear(lunar[0]); //转换年份
        String lunarMonth = toChineseMonth(lunar[1]); //转换月份
        String lunarDay = toChineseDay(lunar[2]); //转换日期
        
        System.out.println("输入的农历日期是：" + lunarYear + lunarMonth + lunarDay);
        //计算输入的日期的忌宜信息
        //这里只是一个简单的示例，你可以自己编写更复杂和准确的算法
        //根据干支和星座来判断忌宜
        String ganzhi = lunarDate.substring(5, 11); //获取干支
        String xingzuo = getConstellation(month, day); //获取星座

        //定义一个字符串缓冲区，存储忌宜信息
        StringBuffer yiji = new StringBuffer();

        //根据干支判断宜
        if (ganzhi.contains("甲") || ganzhi.contains("乙")) {
            yiji.append("宜：种植 求财 开业 ");
        } else if (ganzhi.contains("丙") || ganzhi.contains("丁")) {
            yiji.append("宜：祭祀 祈福 出行 ");
        } else if (ganzhi.contains("戊") || ganzhi.contains("己")) {
            yiji.append("宜：修造 动土 安葬 ");
        } else if (ganzhi.contains("庚") || ganzhi.contains("辛")) {
            yiji.append("宜：订婚 嫁娶 安床 ");
        } else if (ganzhi.contains("壬") || ganzhi.contains("癸")) {
            yiji.append("宜：乘船 捕鱼 纳畜 ");
        }

        //根据干支判断忌
        if (ganzhi.contains("子") || ganzhi.contains("午")) {
            yiji.append("忌：开市 交易 签约 ");
        } else if (ganzhi.contains("丑") || ganzhi.contains("未")) {
            yiji.append("忌：出行 迁居 赴任 ");
        } else if (ganzhi.contains("寅") || ganzhi.contains("申")) {
            yiji.append("忌：结婚 领证 提车 ");
        } else if (ganzhi.contains("卯") || ganzhi.contains("酉")) {
            yiji.append("忌：修造 动土 安葬 ");
        } else if (ganzhi.contains("辰") || ganzhi.contains("戌")) {
            yiji.append("忌：祭祀 祈福 斋醮 ");
        } else if (ganzhi.contains("巳") || ganzhi.contains("亥")) {
            yiji.append("忌：订婚 嫁娶 安床 ");
        }

        //根据星座判断吉凶
        if (xingzuo.equals("白羊座")) {
            yiji.append("\n今日运势：吉\n今日贵人：双子座\n今日小财：100元");
        } else if (xingzuo.equals("金牛座")) {
            yiji.append("\n今日运势：凶\n今日贵人：处女座\n今日小财：50元");
        } else if (xingzuo.equals("双子座")) {
            yiji.append("\n今日运势：吉\n今日贵人：水瓶座\n今日小财：200元");
        } else if (xingzuo.equals("巨蟹座")) {
            yiji.append("\n今日运势：凶\n今日贵人：天蝎座\n今日小财：0元");
        } else if (xingzuo.equals("狮子座")) {
            yiji.append("\n今日运势：吉\n今日贵人：白羊座\n今日小财：300元");
        } else if (xingzuo.equals("处女座")) {
            yiji.append("\n今日运势：凶\n今日贵人：金牛座\n今日小财：10元");
        } else if (xingzuo.equals("天秤座")) {
            yiji.append("\n今日运势：吉\n今日贵人：双子座\n今日小财：150元");
        } else if (xingzuo.equals("天蝎座")) {
            yiji.append("\n今日运势：凶\n今日贵人：巨蟹座\n今日小财：20元");
        } else if (xingzuo.equals("射手座")) {
            yiji.append("\n今日运势：吉\n今日贵人：狮子座\n今日小财：250元");
        } else if (xingzuo.equals("摩羯座")) {
            yiji.append("\n今日运势：凶\n今日贵人：处女座\n今日小财：30元");
        } else if (xingzuo.equals("水瓶座")) {
            yiji.append("\n今日运势：吉\n今日贵人：天秤座\n今日小财：350元");
        } else if (xingzuo.equals("双鱼座")) {
            yiji.append("\n今日运势：凶\n今日贵人：天蝎座\n今日小财：40元");
        }

        //打印出忌宜信息
        System.out.println(yiji.toString());
    }

    //定义一个方法，根据月份和日期获取星座
    public static String getConstellation(int month, int day) {
        //定义一个字符串数组，存储12个星座的名称
        String[] constellations = {"摩羯座", "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座"};
        
        //定义一个整数数组，存储12个星座的分界日期
        int[] days = {20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22};
        
        //如果日期小于分界日期，就返回前一个星座，否则返回后一个星座
        //注意数组的下标从0开始，所以要减1
        //注意月份从1开始，所以要加11
        //注意取模12，防止越界
        if (day < days[month - 1]) {
            return constellations[(month + 10) % 12];
        } else {
            return constellations[(month + 11) % 12];
        }
    }

    //定义一个方法，根据阳历的年份、月份和日期，计算出农历的年份、月份和日期
    public static int[] getLunar(int year, int month, int day) {
        //定义一个整数数组，存储农历的年份、月份和日期
        int[] lunar = new int[3];
        
        //定义一个整数数组，存储1900年到2100年之间的农历数据
        //每个元素的前四位表示闰月的月份，没有闰月为0
        //后十二位表示每个月的大小，大月为1，小月为0
        int[] lunarData = {0x04bd8,0x04ae0,0x0a570,0x054d5,0x0d260,0x0d950,0x16554,0x056a0,
                0x09ad0,0x055d2,0x04ae0,0x0a5b6,0x0a4d0,0x0d250,0x1d255,0x0b540,
                0x0d6a0,0x096d5,0x04b60,0x04ae5,0x0a570,0x054e4,0x07260,0x09570,
                0x09975,0x05aa0,0x06ad6,0x06ae4,0x06c50,0x07552,0x086a5,0x05ac4,
                0x09b50,0x04b60,0x04b64,0x06570,0x054f5,0x05260,0x09570,
                1<<20|8<<16|30<<9|29<<8|30<<7|29<<6|30<<5|29<<4|30<<3|29<<2|30<<1|29,
                1<<20|7<<16|30<<9|29<<8|30<<7|29<<6|30<<5|30<<4|29<<3|30<<2|29<<1|30,
                1<<20|6<<16|29<<9|30<<8|29<<7|30<<6|29<<5|30<<4|30<<3|29<<2|30<<1|30,
                1<<20|5<<16|29<<9|30<<8|29<<7|30<<6|29<<5|30<<4|29<<3|30<<2|30<<1|29,
                1<<20|4<<16|29<<9|30<<8|30<<7|29<<6|30<<5|29<<4|30<<3|29<<2|30<<1|29,
                1<<20|3<<16|30<<9|29<<8|30<<7|30<<6|29<<5|30<<4|29<<3|30<<2|29<<1|30,
                1<<20|2<<16|29<<9|30<<8|29<<7|30<<6|30<<5|29<<4|30<<3|29<<2|30<<1|30,
                1<<20|1<<16|30<<9|29<<8|30<<7|29<<6|30<<5|30<<4|29<<3|30<<2|29<<1|30,
                0x04ae0,0x0a570,0x054d5,0x0d260,0x0d950,0x16554,0x056a0,0x09ad0,
                        0x055d2,0x04ae0,0x0a5b6,0x0a4d0,0x0d250,0x1d255,0x0b540,0x0d6a0,
                        0x096d5,0x04b60,0x04ae5,0x0a570,0x054e4,0x07260,0x09570,
                        1 << 20 | 11 << 16 | 29 << 9 | 30 << 8 | 29 << 7 | 30 << 6 | 29 << 5 | 30 << 4 | 29 << 3 | 30 << 2 | 29 << 1 | 30,
                        1 << 20 |10 <<16 |29 <<9 |30 <<8 |29 <<7 |30 <<6 |29 <<5 |30 <<4 |29 <<3 |30 <<2 |30 <<1 |29,
                        1 <<20 |9 <<16 |28 <<9 |31 <<8 |31 <<7 |31 <<6 |31 <<5 |31 <<4 |31 <<3 |31 <<2 |31 <<1 |31};
                
                //定义一个整数变量，存储阳历的总天数
                int totalDays = getDays(year, month, day);
                
                //定义一个整数变量，存储农历的年份
                int lunarYear = year;
                
                //定义一个整数变量，存储农历的月份
                int lunarMonth = month;
                
                //定义一个整数变量，存储农历的日期
                int lunarDay = day;
                
                //定义一个布尔变量，判断是否为闰月
                boolean isLeapMonth = false;
                
                //定义一个整数变量，存储1900年的农历数据
                int data = lunarData[year - 1900];
                
                //获取闰月的月份，如果为0表示没有闰月
                int leapMonth = data >> 16;
                
                //获取闰月的天数，如果为13表示大月，如果为12表示小月
                int leapDays = (data >> 12) & 15;
                
                //获取农历的总天数，如果为383表示闰年大月，如果为382表示闰年小月，如果为355表示平年大月，如果为354表示平年小月
                int lunarDays = data & ((1 <<12) -1);
                
                //如果阳历的总天数小于农历的总天数，说明农历还没有到该年份，需要减一年
                if (totalDays < lunarDays) {
                    lunarYear--;
                    //重新获取上一年的农历数据
                    data = lunarData[lunarYear -1900];
                    leapMonth = data >>16;
                    leapDays = (data >>12) &15;
                    lunarDays = data & ((1 <<12) -1);
                    //将阳历的总天数加上上一年的农历总天数，得到新的总天数
                    totalDays += lunarDays;
                }
                
                //定义一个整数变量，存储农历每个月的天数
                int daysPerMonth = 0;
                
                //循环遍历农历的每个月，从后往前
                for (int i = 12; i >= 1; i--) {
                    //判断是否为闰月
                    if (i == leapMonth + 1 && leapMonth > 0) {
                        //如果是闰月，就取出闰月的天数
                        daysPerMonth = leapDays;
                        //将闰月标志置为真
                        isLeapMonth = true;
                    } else {
                        //如果不是闰月，就取出该月的天数
                        daysPerMonth = (data >> (i - 1)) & 1;
                        //将闰月标志置为假
                        isLeapMonth = false;
                    }
                    
                    //如果阳历的总天数小于农历每个月的天数，说明农历还没有到该月份，需要减一月
                    if (totalDays < daysPerMonth) {
                        lunarMonth--;
                        //如果农历月份小于1，说明还没有到该年份，需要减一年
                        if (lunarMonth < 1) {
                            lunarYear--;
                            lunarMonth += 12;
                        }
                        //将阳历的总天数加上该月的天数，得到新的总天数
                        totalDays += daysPerMonth;
                    } else {
                        //如果阳历的总天数大于等于农历每个月的天数，说明已经到了该月份，跳出循环
                        break;
                    }
                }
                
                //将阳历的总天数减去农历每个月的天数，得到农历的日期
                lunarDay = totalDays - daysPerMonth + 1;
                
                //将农历的年份、月份和日期存入数组中
                lunar[0] = lunarYear;
                lunar[1] = lunarMonth;
                lunar[2] = lunarDay;
                
                //返回数组
                return lunar;
            }

            //定义一个方法，根据阳历的年份、月份和日期，计算出从1900年1月1日开始的总天数
            public static int getDays(int year, int month, int day) {
                //定义一个整数变量，存储总天数
                int totalDays = 0;
                
                //遍历出从1900年到输入年份之间的总天数
                for (int i = 1900; i < year; i++) {
                    //判断是否为闰年
                    if (((i%4 == 0 && i%100 != 0))||(i%400 == 0)){
                        //闰年366天
                        totalDays += 366;
                    }else{
                        //平年365天
                        totalDays += 365;
                    }
                }
                
                //遍历出从1月到输入月份之间的总天数
                for (int i = 1; i < month; i++) {
                    //判断每个月的天数
                    switch (i) {
                        case 1:
                        case 3:
                        case 5:
                        case 7:
                        case 8:
                        case 10:
                        case 12:
                            totalDays += 31;
                            break;
                        case 2:
                            //二月分闰年和平年
                            if (((year%4 == 0 && year%100 != 0))||(year%400 == 0)){
                                totalDays += 29;
                            }else{
                                totalDays += 28;
                            }
                            break;
                        case 4:
                        case 6:
                        case 9:
                        case 11:
                            totalDays += 30;
                            break;
                        default:
                            System.out.println("输入信息有误！");;
                            break;
                    }
                }
                
                //加上输入日期的天数
                totalDays += day;
                
                //返回总天数
                return totalDays;
            }

            //定义一个方法，将农历的年份转换为中文
            public static String toChineseYear(int year) {
                //定义一个字符串数组，存储天干的名称
                String[] tianGan = {"甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"};
                
                //定义一个字符串数组，存储地支的名称
                String[] diZhi = {"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};
                
                //定义一个字符串数组，存储生肖的名称
                String[] shengXiao = {"鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"};
                
                //定义一个字符串变量，存储中文年份
                String chineseYear = "";
                
                //根据年份计算天干的下标，注意1900年为庚子年
                int indexTianGan = (year - 1900 + 6) % 10;
                
                //根据年份计算地支的下标，注意1900年为庚子年
                int indexDiZhi = (year - 1900 + 6) % 12;
                
                //根据年份计算生肖的下标，注意1900年为鼠年
                int indexShengXiao = (year - 1900) % 12;
                
                //将天干、地支和生肖拼接起来，得到中文年份
                chineseYear = tianGan[indexTianGan] + diZhi[indexDiZhi] + "(" + shengXiao[indexShengXiao] + ")年";
                
                //返回中文年份
                return chineseYear;
            }

            //定义一个方法，将农历的月份转换为中文
            	public static String toChineseMonth(int month) {
            	    //定义一个字符串数组，存储月份的名称
            	    String[] months = {"正", "二", "三", "四", "五", "六", "七", "八", "九", "十", "冬", "腊"};
            	    
            	    //定义一个字符串变量，存储中文月份
            	    String chineseMonth = "";
            	    
            	    //根据月份的下标，取出对应的名称，得到中文月份
            	    chineseMonth = months[month - 1] + "月";
            	    
            	    //返回中文月份
            	    return chineseMonth;
            	}

            	//定义一个方法，将农历的日期转换为中文
            	public static String toChineseDay(int day) {
            	    //定义一个字符串数组，存储日期的前缀
            	    String[] prefixes = {"初", "十", "廿", "卅"};
            	    
            	    //定义一个字符串数组，存储日期的后缀
            	    String[] suffixes = {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十"};
            	    
            	    //定义一个字符串变量，存储中文日期
            	    String chineseDay = "";
            	    
            	    //根据日期计算前缀和后缀的下标
            	    int indexPrefix = (day - 1) / 10;
            	    int indexSuffix = (day - 1) % 10;
            	    
            	    //特殊处理10号和20号
            	    if (day == 10) {
            	        chineseDay = "初十";
            	    } else if (day == 20) {
            	        chineseDay = "二十";
            	    } else {
            	        //将前缀和后缀拼接起来，得到中文日期
            	        chineseDay = prefixes[indexPrefix] + suffixes[indexSuffix];
            	    }
            	    
            	    //返回中文日期
            	    return chineseDay;
            	}

}

