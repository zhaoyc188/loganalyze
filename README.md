# loganalyze
tomcat日志分析工具

tomcat url请求日志分析工具，可分析哪些url请求超时，输出Title：请求rul,请求总次数,超时请求次数,超时占比
日志格式：pattern: "%h %l %u %t \"%r\" %s %D"
jar方式启动，参数说明：
			System.out.println("1.第一个参数为日志所在目录的绝对路径或者具体日志文件的绝对路径");
			System.out.println("2.第二个参数为分析结果的输出文件");
			System.out.println("3.第三个参数为超时时间，单位毫秒");