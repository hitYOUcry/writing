// a = 10
// println a.getClass()
// a = '1'
// println a.getClass()
// a = 10L
// println a.getClass()

// def show(){

//     println a.getClass()
// }
// show()

// //用于遍历
// def list = [1,2,3,4]
// for (item in list) {
//     println item
// }

// //用于判断包含关系
// assert (3 in list)

// def foo = [this:'a']
// foo.class = 1
// println foo.class
// println foo.this

// def map = [:]
// map."*&^%#\$@" = "haha"
// assert map."*&^%#\$@" == "haha"

// def str = $/ $/ /$
// def str_a = $/ / /$
// assert str == str_a

// //Number
// int a = 8
// int b = 3
// println 8/3
// println 8.intdiv(3)
// long bigNum = 8_123_324_123_423_45_4_586L
// println bigNum

// //closure

// def c1 = {int x -> println x}
// def c2 = {String x, int y -> println "x:${x}, y:${y}"}
// def c3 = { -> println x}
// def c4 = {println x}
// def c5 = {println it}

// def empty = {}

// c1(1)
// c1 1
// //c1 "1"//"1"可以转为int，因此可以调用成功
// //c1 "a" //"a"无法转为int，因此调用失败

// c2("1",1)
// //c2 "1",1 不要括号的调用仅限一个参数的闭包

// def c = {int x -> return x+1}
// def c_1 = {int x -> x+1}
// def c_2 = {int x -> println x+1}
// assert c(1) == 2
// assert c_1(1) == 2
// assert c_2(1) == null

// class A {
//     void run() {
//         def closure_1 = {
//             def clousre_2 = {owner}//此处的owner是闭包closure_2的owner对象,它指向闭包closure_1
//             return clousre_2
//         }
//         assert closure_1()() == closure_1//调用闭包closure_1()得到闭包clousre_2，再次调用闭包clousre_2得到闭包clousre_2的owner对象

//         assert closure_1.getThisObject() == this//this对象逻辑如上节

//         assert closure_1().getOwner() == closure_1//调用闭包closure_1()得到闭包clousre_2,调用闭包clousre_2的getOwner方法得到它的owner对象
//     }
// }
// def a = new A()
// a.run()
// class B {
//     def name = "haha"
// }
// def closure = {
//     //默认情况下delegate=owner，无法调用到name字段
//     //手动把delegate指向B的实例之后就可以正常使用name字段
//     println name 
// }
// closure.delegate = new B()
// closure.resolveStrategy = Closure.OWNER_ONLY
// closure()



// def a = {Closure c ->
//     c.call()
// }

// a {
//     println "I am from closure"//花括号表示一个闭包，它作为参数传给了闭包a
// }

// def a(Closure c){
//     c.call()
// }

// a {
//     println "I am from closure"//花括号表示一个闭包，它作为参数传给了闭包a
// }
class Email {
    void from(String from){
        println "From : ${from}" 
    }

    void to(String to){
        println "To : ${to}"
    }

    void subject(String subject){
        println "Subject : ${subject}"
    }

    void body(Closure c){
        c.delegate = new EmailBody();
        c.resolveStrategy = Closure.DELEGATE_ONLY
        c.call()
    }

}

class EmailBody {
    void content(String content){
        println "Content : ${content}"
    }
}


void email(Closure c){
    c.delegate = new Email();
    c.resolveStrategy = Closure.DELEGATE_ONLY
    c.call()
}


email {
    from "abc@163.com"
    to "def@qq.com"
    subject "Secret"
    body {
        content "Money is no the position, do it!"
    }
}

void mm(String a,int b){
    println a + b
}

mm "qwe",2