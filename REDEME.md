1.使用已经提供好的console来实现箭头上下翻滚
2.服务端的输出重定向到socket outputstream
3.使用 Java8nashorn引擎（后期可以改为根据Jvm不一样实现不同的引擎，或者自己内置一个引擎,屏蔽个jvm的差异）
4.文件方式实现代码加载
5.允许自定义加载jar文件
6.多行字符串（以后实现）
7.string interpolation, 6和7和ES6的写法一样
8.使用Maven/Gradle插件打包，并且打包所有依赖到同一个jar里面


=============
0. STRING
    eg. ".+"|'.+'

1. SSTRING
    eg. `.+`|`.+`
    `hello $name.age` => 'hello' + (name.age)
    `hello ${1+1}` => 'hello' + (1+1)

1. SP_ID '@'ID
  eg. @redisTempldate => _jj$redisTemplate

2. ID
  identifier

==========
java -jar jj.jar
id   pid    name
1    1001   SpiderBoss
2    1002   AppServer

:help
:jjps list all jvm process
:attach
    attach id or attatch p pid
    u cannot attach another jvm before u detach current one,pls use detach first
    display some infomation about current jvm
:detach  detach current jvm

