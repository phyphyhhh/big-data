Ex. 1 - Processes, cgroups, and namespaces

1. Write a short summary describing what cgroups are.

Control Groups (cgroups) are a Linux kernel feature that allows the management and allocation of system resources, such as CPU time, system memory, disk I/O, and network bandwidth, among user-defined groups of tasks (processes). Cgroups provide fine-grained control over how resources are distributed and limited among processes. They are essential for ensuring that processes do not consume more than their fair share of resources, and they are widely used in containerization and virtualization technologies to isolate and manage resources efficiently.

2. Explain the differences and similarities between cgroups and processes in Linux.

Differences: Processes represent individual instances of running programs and are the fundamental units of execution, handling tasks such as running applications and executing code. In contrast, cgroups are a mechanism for managing and allocating system resources among groups of processes, controlling resource usage, and enforcing resource policies. Processes are organized in a hierarchical tree structure where each process has a parent and can have multiple children, while cgroups are organized in a hierarchy of directories, each corresponding to a cgroup containing processes and resource limits. 

Similarities: Both processes and cgroups can be managed using Linux commands and tools, such as ps, top, and kill for processes, and cgcreate, cgexec, and cgdelete for cgroups. Both can be controlled and monitored, with processes being controlled through signals and cgroups being controlled through resource constraints.

3. How does kernel namespace increase the security of the OS?

Kernel namespace enhances the security of the operating system by isolating various system resources and components for processes, effectively creating separate instances of global resources that are not visible or accessible to processes outside their namespace. This isolation ensures that processes are confined within their own environments, preventing them from interfering with each other or accessing unauthorized resources. For example, the PID namespace isolates process IDs, making processes in one namespace invisible to those in another, thus preventing process ID collisions and unauthorized process interaction. The network namespace isolates network interfaces, IP addresses, and routing tables, which secures network configurations and prevents unauthorized network access. Mount namespaces provide separate filesystem views, ensuring processes only see and access their designated files and directories.


Ex. 2 - Increasingly large dataset

1. Basic hardware profile.

a) What CPU does your computer have?

My computer is powered by an Intel Core i7-9750H CPU, which operates at 2.60GHz and is identified technically as Intel64 Family 6 Model 158 Stepping 10. This CPU is part of Intel's 9th generation Core series, known for providing high-performance capabilities suitable for demanding applications. The i7-9750H features 6 cores and 12 threads, making it particularly effective for tasks that require significant computational power such as gaming, video editing, and intensive multitasking in professional settings.

b) How much RAM does your computer have?

My computer has a total RAM of 8GB.

c) Explain how you will monitor the RAM and CPU usage in the following questions.

I will monitor the RAM and CPU usage through the psutil package in Python.

cpu_usage = psutil.cpu_percent

memory_usage = psutil.virtual_memory().percent

The following questions are answered by analyzing the data from 2005 to 2008, since there is no weather data in 1987, and adding more data may let the program fail.

2. Determine the following information:

a) Which carrier is most commonly late?

'WN'

b) Which are the three most commonly late origins, due to bad weather?

'ORD', 'ATL', 'DFW'

c) What is the longest delay experienced for each carrier?

9E    2339.0
AA    2395.0
AQ    2370.0
AS    2395.0
B6    2399.0
CO    2358.0
DL    2399.0
EV    2355.0
F9    2395.0
FL    2398.0
HA    2390.0
MQ    2395.0
NW    2399.0
OH    2399.0
OO    2399.0
UA    2399.0
US    2399.0
WN    2395.0
XE    2357.0
YV    2399.0

![ex2(1)](./assets/2(1).png)

![ex2(2)](./assets/2(2).png)

![ex2(3)](./assets/2(3).png)

   Can you discover any pattern explaining departure delays?

To explain departure delays, I plan to analyze DayOfWeek, DepTime, CRSDepTime, ArrTime , CRSArrTime. I will use a linear regression model. I first use the data of 2008, and the result is as follows:

DepDelay = 0.24 * DayOfWeek + 0.05 * DepTime + -0.03 * CRSDepTime + -0.02 * ArrTime + 0.02 * CRSArrTime

The result shows the pattern that DayOfWeek and DepTime have a positive correlation with Departure Delay, while CRSDepTime, ArrTime, and CRSArrTime have a negative correlation with Departure Delay. This pattern suggests that certain days of the week and specific departure times are more prone to delays, which can help airlines optimize their schedules and improve on-time performance.

![ex3(1)](./assets/3(2).png)

![ex3(2)](./assets/3(3).png)

![ex3(3)](./assets//3(4).png)

Ex. 3 - Very basic Java

Commands are written in README.md in the folders.




