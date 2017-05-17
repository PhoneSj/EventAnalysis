# 事件传递分析

Android中事件传递使用的是**责任链模式**。
举例：一个互联网公司有总经理、经理、开发组长、程序员。当总经理提出开发需求后，会将开发任务分发经理、经理分发开发组长，开发组长分发给程序员。
* 当程序员可以完成任务时，完成后告知开发组长，开发组长告知经理，经理告知总经理。这样一次开发需求完毕。
* 当时当程序员无法完成时，同样告知开发组长任务无法完成，这时就需要开发组长来完成；若开发组长也完成不了，那么就一级一级网上报，直到任务发成或者总经理自己看着办。

同样的，在事件传递中，分发事件的方法是**dispatchTouchEvent(MotionEvent event)**，上一级与下一级的事件交流都是通过这个方法，而**onInterceptTouchEvent(MotionEvent event)**方法和**onTouchEvent(MotionEvent event)**方法都是在dispatchTouchEvent方法中调用的。

下图是事件传递的基本流程：
箭头上的序号为传递的选后顺序。

![事件传递基本流程](preview/Event_01.png)

## 事件传递的几种情况

由于上图不便于标示分析，下面的示意图都会讲onInterceptTouchEvent/onTouchEvent方法提到与dispatchTouchEvent平级来进行分析。
我们测试的demo中，Activity中只有一个ViewGroup、ViewGroup中有一个View。

控制事件流的方法有：
* dispatchTouchEvnet
* onInterceptTouchEvent
* onTouchEvent
* 
### 所有事件控件方法都返回默认值
**down事件**的传递流程

![down事件传递流程](preview/Event_10.png)

**move/up事件**的传递流程

![move/up事件传递流程](preview/Event_11.png)

### View的onTouchEvent返回true

**down事件**的传递流程

![down](preview/Event_12.png)

**move/up事件**的传递流程
与down一样

### ViewGroup的onTouchEvent返回true

**down事件**的传递流程

![down](preview/Event_14.png)

**move/up事件**的传递流程

![down](preview/Event_15.png)

### View的onTouchEvent返回true

**down事件**的传递流程

![down](preview/Event_16.png)

**move/up事件**的传递流程
与down一样

### View的onTouchEvent返回false

**down事件**的传递流程

![down](preview/Event_18.png)

**move/up事件**的传递流程

![down](preview/Event_19.png)

### View的onTouchEvent返回true，ViewGroup的onInterceptTouchEvent的move事件返回true

**down事件**的传递流程

![down](preview/Event_20.png)

**第一次move事件**的传递流程

![down](preview/Event_21.png)

**后续move事件**的传递流程

![down](preview/Event_22.png)

