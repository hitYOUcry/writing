## Groovy 入门 ##
>随着Android/Gradle的发展，Groovy渐渐的被更多Android开发者熟知。

Groovy也是基于JVM的语言之一，语法和Java非常相似（对Android/Java开发者来说学习成本很低，这也是Gradle为什么选择Groovy作为脚本语言的原因）。同时它是一门脚本语言，`.groovy`的脚本代码会先被编译成Java字节码（也就是`.class`文件），然后由Java虚拟机执行。

>Groovy是一门**动态强类型**语言。

关于动态/静态、强类型/弱类型语言，可以参考[这个回答](https://www.zhihu.com/question/19918532)。
简单理解：Groovy在运行时做类型检查（动态语言），并且和Java一样对类型转换偏向于显示（强类型）。

不废话了，下边介绍Groovy的基础。

### 1.安装 ###
Windows平台可以去[这里](http://groovy-lang.org/download.html)下载Groovy的zip包，解压之后把bin目录添加到环境变量即可。

Linux/macOS平台官方推荐使用SDKMAN工具安装Groovy。具体命令行命令如下：

`$ curl -s get.sdkman.io | bash`  
`$ source "$HOME/.sdkman/bin/sdkman-init.sh"`  
`$ sdk install groovy`  
`$ groovy -version`

更详细的信息就不搬移了，参考上边的下载链接页面内容即可。

### 2.语法 ###
Groovy与Java最大的不同是实现了动态类型语言的特性，让代码编写和写脚本一样简单（实际上Groovy就是脚本语言）。
#### 2.1 关键字 ####
官方文档中贴出的关键字有：
| <font color=#ff0000>**as**</font> | assert | break | case |
| :---: | :---: | :---: | :---: |
| **catch** | **class** | **const** | **continue** |
| <font color=#ff0000>**def**</font> | **default** | **do** | **else** |
| **enum** | **extends** | **false** | **finally** |
| **for** | **goto** | **if** | **implements** |
| **import** | <font color=#ff0000>**in**</font> | **instanceof** | **interface** |
| **new** | **null** | **package** | **return** |
| **throws** | <font color=#ff0000>**trait**</font> | **true** | **try** |
| **while** |  |  |  |

其中被标红的`as/def/in/trait`是Java中没有的（其他的参看Java关键字含义，基本完全一致）。

<font color=#ff0000>**as**</font>  
`as`关键字有两个作用：**显示的类型强转**和**导包的别名**
```groovy
def a = "123"
def b = a as int

import java.util.Date
import java.sql.Date as SQLDate
```
<font color=#ff0000>**def**</font>   
`def`关键字用于声明类型不确定的变量或者返回值类型不确定的函数。  
`def a = 10`  
定义变量a的值为10，a的类型不确定（可以理解为是Object类型）。在Groovy中声明变量一般就使用def去定义，也可以不使用def注明，直接给标识符幅值：`b = 10`，此时变量b的作用域为整个脚本文件，而使用def定义的变量a可以理解为是一个局部变量。  

<font color=#ff0000>**in**</font>  
`in`关键字用于**for each语句**和**判断包含关系**：
```groovy
//用于遍历
def list = [1,2,3,4]
for (item in list) {
    println item
}

//用于判断包含关系
assert (3 in list)
```
<font color=#ff0000>**trait**</font>   
`trait`关键字是groovy对java面向对象的扩展，有点和Java8 interface的default方法类似，用`trait`标识的类，可以看做是一个同时包含实现以及方法声明的接口。这部分详细内容有兴趣可参考[官方文档](http://groovy-lang.org/objectorientation.html#_traits)。  
#### 2.2 标识符 ####
Groovy中普通的标识符说起来和Java系是一致的：由**字母**、**美元符**、**数字**和**下划线**组成，且数字**不**能是第一位。  
然而，对于`map`类型的key，却可以很奇怪：
```groovy
def foo = [this:'a']
foo.class = 1
println foo.class
println foo.this
```
以上都是合法的。  
更过分的是还有一种引用标识符（这样子：`person."name"`），被引号包含的内容可以是任意字符串。
```groovy
def map = [:]
map."*&^%#\$@" = "haha"
assert map."*&^%#\$@" == "haha"
```
其中\\$是为了转义"$"。

#### 2.3 字符串 ####
Groovy中字符串有两类：<font color=#ff0000>java.lang.String</font>和<font color=#ff0000>groovy.lang.GString</font>。第一类不用多说，就是Java中的字符串；第二类是可**插值**字符串。
以上是从JVM的角度以对象的类来区分不同的字符串，在groovy中字符串形式很多：  
```groovy
'I am a string'

"I am a string"

''' I
am a string '''

"""I am a 
string"""

/I am a string/

$/I am a string/$
```
以上都是Groovy中的合法字符串。那么，他们是java.lang.String还是groovy.lang.GString？GString有什么特点呢？

##### GString #####
GString是指被`"..."`  `"""..."""`  `/.../`  `$/.../$`包裹的且有插值行为的字符串。也就是单引号包裹的字符串都是普通类型的，它本身也不支持插值操作；另外，字符串中有插值行为是该字符串对象才会被认为是GString对象。  
字符串中的插值行为如下：
```groovy
def str = "hello my ${'frient'}"
def str_a = /a ${str}/
```
即使用`${}`包裹可变部分，这样整个字符串即是一个值可变的插值字符串。
##### /.../ 和 $/.../$ #####
这两类字符串是为了在某些特定情况下方便使用而出现。
- `/.../`中转义字符是"/"，也仅有"/"字符需要转义，其他字符都不需要转义，在写正则表达式时比较实用
- `$/.../$`中转义字符是"$",也仅有"$"字符需要转义，其他字符都不需要转义，适用于需要在字符串中写一些需要转义的字符而不想写转义符的场景（好像是屁话）。

官网总结表格如下：
|字符串类型 | 字符串语法 | 是否可插值 | 是否支持多行 | 转移符 |
| :---: | :---: | :---: | :---: | :---: |
| 单引号字符 | `'...'` | 否 | 否 | `\` |
| 多行单引号字符 | `'''...'''` | 否 | 是 | `\` |
| 双引号字符 | `"..."` | 是 | 否 | `\` |
| 多行双引号字符 | `"""..."""` | 是 | 是 | `\` |
| 斜杠字符 | `/.../` | 是 | 是 | `\` |
| 美元符斜杠字符 | `$/.../$` | 是 | 是 | `$` |

Groovy中字符安和Java字符串最大的区别就是它支持插值操作，可以在字符串中嵌入一些变量的值，这在脚本编写时会比较方便实用。
#### 2.4 数字 ####  
Groovy中有关于数字的操作和Java也基本是一致的，涉及到的类型也都是Java中数字操作相关的类型（`byte/char/short/int/long/float/double/BigInteger/BigDecimal`）。  
注意以下几点：
- 连续的两个`*`表示次方运算：`2 ** 3 = 8`
- 除法`/`不再是`Java`中的整数除法，它得到的结果是`double`（除数或被除数是float或者double）或者`BigDecimal`(除数和被除数是其他类型)，实现Java中的整数除法需要使用intdiv()函数:  
`8/3 = 2.6666666...`  
`8.intdiv(3) = 2`
- 数字太长可以用下划线隔开：`long bigNum = 8_123_324_123_423_454_586L`(并非强制要三个一组)  
#### 2.5 集合 ####
Groovy中的集合也是基于Java的集合API实现，分为：List、Array和Map三类。
集合的定义使用方括号：
```groovy
def list = [1,2,3]
assert list instanceof java.util.ArrayList

def array = [1,2,3] as int[]

def map = [red:"0xff0000", green:"0x00ff00", blue:"0x0000ff"]
assert list instanceof java.util.LinkedHashMap
```
对集合的操作很方便，也很好理解：
```groovy
assert list[-1] == 3
assert list[0,2] == [1,3]
assert list[1 .. 2] == [2,3]

assert arrary[1] == 2

assert map.red == "0xff0000"
assert map."blue" == "0x0000ff"
assert map['green'] == "0x00ff00"

```


Groovy的基础语法和Java没有太大区别，对于熟悉Java的同学来说，很好上手也容易理解（毕竟是个脚本语言）。

### 3.闭包 ###
闭包是Groovy区别于Java的核心，也是用于实现Gradle DSL风格配置块的语言基础。  
官网中对于闭包的定义描述很全面：**Groovy中的闭包是一个开放、匿名的代码块，它可以接收参数，可以返回值也可以把它作为值赋给某个变量**。  
对于熟悉Js/python的同学来说，闭包概念应该不陌生，这里假设我们只是一个小小的Java程序员，完全不了解闭包。
#### 闭包格式 ####
语言的特性都会体现在其编码方式和输出结果上，groovy中的闭包也不例外。  
groovy的闭包格式如下：
```groovy
{params -> statements}
```
闭包由花括号包裹，括号内包含参数列表和代码块两部分，并用`->`隔开。其中参数列表和`->`可以缺省(其实除了花括号，你可以什么都不写，但这样好像没什么意义)。
#### 闭包定义 ####
了解基本的格式之后，我们跃跃欲试。
看如下的闭包定义：
```groovy
def c1 = {int x -> println x}
def c2 = {String x, int y -> println "x:${x}, y:${y}"}
def c3 = { -> println x}
def c4 = {println x}
def c5 = {println it}

def empty = {}
```
且不论这些闭包调用时会不会出错，这样定义是完全OK的，而且他们的类型都是Closure。  
和闭包格式说明一致，闭包中的内容都可以缺省。`c1`和`c2`属于那种五脏俱全的；`c3`缺省了参数列表，表示它一个参数都没有；`c4`和`c5`省略了参数列表**和分隔符

#### 闭包调用 ####
别光定义啊，你倒是用一下啊。
闭包的调用方式有很多，我们先用一种面向对象的方式来调用：
```groovy
c1.call(1)
c2.call("1",1)
//c3.call(1) x未定义，调用异常
//c4.call(1) x未定义，调用异常
c5.call(1)
```
闭包是一个对象，它有用call方法，我们调用call方法，并根据闭包定义参数列表传入相应的参数既可调用这个闭包，来执行闭包中定义的代码块。  
从上边的例子还可以看出一点：c5可以被正常的调用，因为**it**是一个**默认的参数**，当然像c3这种显示定义参数列表为空的情况没有默认参数it。  
继续看花式调用方式：
```groovy
c1(1)//有点像函数调用
c1 1 //有没有看出gradle配置的影子？？
//c1 "1" "1"不是int类型，因此调用失败

c2("1",1)
//c2 "1",1 不要括号的调用仅限一个参数的闭包
```

#### 闭包的返回值 ####
类似于方法调用，闭包调用之后是有返回值的，只是我们定义闭包时并不会指定闭包的返回值。
```groovy
def c = {int x -> return x+1}
def c_1 = {int x -> x+1}
def c_2 = {int x -> println x+1}
assert c(1) == 2
assert c_1(1) == 2
assert c_2(1) == null
```
从示例也可以看出：闭包的返回值可以显示的用return语句执行（类型不限），没有return语句时，默认返回最后一行代码的值（println函数返回值类型为void，因此在c_2中我们得到null）。


这么来看闭包是不是挺简单的，当做一个对象即可，调用方式多看几遍也就熟悉了。  

#### 代理策略 ####
如上，闭包的格式并不复杂，然而像静态内部类一样，它会持有外部类的引用；同时它还有代理字段，这使得闭包的行为变得多样。  
比静态内部类更进一步，groovy闭包会持有：`this`、`owner`和`delegate`三个隐式对象，他们分别指向不同的对象，我们一个一个分析。

**this**  
this指向闭包定义时所在的离闭包最近的类的实例。
一切都是对象，不仅适用于java，也适用于groovy。因此，闭包的定义必然在某个类当中。看如下示例：
```groovy
class A {
    void run() {
        def closure_1 = {this}//此处的this是闭包closure_1的this对象
        assert closure_1() == this//此处的this是类A的this对象
    }
}
def a = new A()
a.run()
```
断言成功，说明闭包中的this确实是指向它所处的类的那个实例（除了显示使用this对象外，还可以在闭包内调用getThisObject()方法获得闭包的this对象）。

**owner**  
owner指向闭包定义时所在的离闭包最近的**类或者闭包**的示例。
闭包除了可能定义在某个类中，还可能会在闭包内，这个owner即是指向离本闭包最近的那个类或者闭包。
是不是很绕，是不是看代码更简单？？好的：
```groovy
class A {
    void run() {
        def closure_1 = {
            def clousre_2 = {owner}//此处的owner是闭包closure_2的owner对象,它指向闭包closure_1
            return clousre_2
        }
        assert closure_1()() == closure_1//调用闭包closure_1()得到闭包clousre_2，再次调用闭包clousre_2得到闭包clousre_2的owner对象

        assert closure_1.getThisObject() == this//this对象逻辑如上节

        assert closure_1().getOwner() == closure_1//调用闭包closure_1()得到闭包clousre_2,调用闭包clousre_2的getOwner方法得到它的owner对象
    }
}
def a = new A()
a.run()
```
一切尽在代码中(要理解闭包的返回值以及this/owner的指向关系)... 
**delegate**  
确切的说，delegate是闭包的一个扩展字段。  
本节的小标题为代理策略，说的就是给闭包设置不同的代理方式，它的delegate字段会指向不同的对象。默认情况下delegate指向owner字段。  
既然默认情况是指向owner，那么它肯定也是可以指向this的，另外，如果我们手动的给闭包的delegate字段赋值，它也可以指向一个八竿子打不着的对象。  
那么，给delegate字段指来指去有什么用呢？好玩吗？













