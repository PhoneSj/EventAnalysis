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

先抛出四条结论，在下面例子中验证：
* 事件往下传递的过程就是找消费该事件的View的过程
* 在down事件中，每层级ViewGroup在寻找接收down事件的TargetView（可以时子控件，后者本身）
* 只有在dispatchTouchEvent和onTouchEvent方法中才能确定该层级的TargetView
* 只能在onInterceptTouchEvent方法中才能修改TargetView且修改时会发送cancel事件给之前的TargetView，并且将该次事件交由Activity。后续的事件就不会执行该方法
* 如果确定了TargeView，dispatchTouchEvent和onTouchEvent返回false，那么本次事件交由Activity
* 若果没有确定TargetView，dispatchTouchEvent和onTouchEvent返回false，那么事件交给父控件。

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

![move/up](preview/Event_19.png)

### View的onTouchEvent返回true，ViewGroup的onInterceptTouchEvent的move事件返回true

**down事件**的传递流程

![down](preview/Event_20.png)

**第一次move事件**的传递流程

![第一次move](preview/Event_21.png)

**后续move事件**的传递流程

![move](preview/Event_22.png)




## View源码分析

### dispatchTouchEvent方法

<pre><code>
    public boolean dispatchTouchEvent(MotionEvent event) {
        // If the event should be handled by accessibility focus first.
        if (event.isTargetAccessibilityFocus()) {
            // We don't have focus or no virtual descendant has it, do not handle the event.
            if (!isAccessibilityFocusedViewOrHost()) {
                return false;
            }
            // We have focus and got the event, then use normal event dispatch.
            event.setTargetAccessibilityFocus(false);
        }

        boolean result = false;

        if (mInputEventConsistencyVerifier != null) {
            mInputEventConsistencyVerifier.onTouchEvent(event, 0);
        }

        final int actionMasked = event.getActionMasked();
        if (actionMasked == MotionEvent.ACTION_DOWN) {
            // Defensive cleanup for new gesture
            stopNestedScroll();
        }

        if (onFilterTouchEventForSecurity(event)) {
            //noinspection SimplifiableIfStatement
            ListenerInfo li = mListenerInfo;
            if (li != null && li.mOnTouchListener != null
                    && (mViewFlags & ENABLED_MASK) == ENABLED
                    && li.mOnTouchListener.onTouch(this, event)) {
                //如果设置了OnTouchListener监听器，那么直接返回true，即将当前控件设置为TargetView，
                //且不会执行onTouchEvent方法
                result = true;
            }

            if (!result && onTouchEvent(event)) {
				//如果没有设置OnTouchListener监听器，此时执行onTouchEvent.
                result = true;
            }
        }

        if (!result && mInputEventConsistencyVerifier != null) {
            mInputEventConsistencyVerifier.onUnhandledEvent(event, 0);
        }

        // Clean up after nested scrolls if this is the end of a gesture;
        // also cancel it if we tried an ACTION_DOWN but we didn't want the rest
        // of the gesture.
        if (actionMasked == MotionEvent.ACTION_UP ||
                actionMasked == MotionEvent.ACTION_CANCEL ||
                (actionMasked == MotionEvent.ACTION_DOWN && !result)) {
            stopNestedScroll();
        }
		//若result为true，那么该View后者子View会消费事件
        return result;
    }
</code></pre>
由以上代码可知：
* OnTouchListener在onTouchEvent之前执行
* OnTouchListener返回true时，表示当前View或者其子View为TargeView，后面的onTouchEvent不会执行。

### onTouchEvent

<pre><code>
public boolean onTouchEvent(MotionEvent event) {
    ...
    if (mTouchDelegate != null) {
        if (mTouchDelegate.onTouchEvent(event)) {
            return true;
        }
    }

    ...
    if (mPerformClick == null) {
        mPerformClick = new PerformClick();
     }
    if (!post(mPerformClick)) {
		//在手指弹起时触发点击事件
        performClick();
    }
    ...
</code></pre>
由上面代码可知：
* 事件先会交给外界设置的代理mTouchEvent处理，若mTouchEvent处理，则事件被代理消费，该View为TargetView，代码不在往下执行。
* 点击事件响应performClick方法时在onTouchEvent中调用的
* 所以触摸监听器OnTouchEvent在点击监听器OnClick之前执行

#### onTouchListener与onClickListener调用时机
以下是事件传递过程（其中MyListView，MyItem是ViweGroup）:
![image](preview/Event_24.png)

## ViewGroup源码分析

### dispatchTouchEvent

#### 第一部分

<pre><code>
public boolean dispatchTouchEvent(MotionEvent ev){
    ...
            //检查是否拦截
            final boolean intercepted;
            if (actionMasked == MotionEvent.ACTION_DOWN
                    || mFirstTouchTarget != null) {
                //有requestDisallowIntercept方法确定，当子控件调用该方法时，父控件不做拦截判断
                final boolean disallowIntercept = (mGroupFlags & FLAG_DISALLOW_INTERCEPT) != 0;
                if (!disallowIntercept) {
                    intercepted = onInterceptTouchEvent(ev);
                    ev.setAction(action); // restore action in case it was changed
                } else {
                    intercepted = false;
                }
            } else {
                // There are no touch targets and this action is not an initial down
                // so this view group continues to intercept touches.
                intercepted = true;
            }
    ...
}
</code></pre>
功能：
* 子控件是否请求了不要拦截事件
* 判断是否需要拦截事件

#### 第二部分

<pre><code>
    ...
            //每次处理事件之前将目标控件链表置空
            TouchTarget newTouchTarget = null;
    ...
    ...
                    //当目标控件链表为空，或者子控件为空时(即相当于)
                    if (newTouchTarget == null && childrenCount != 0) {
                        final float x = ev.getX(actionIndex);
                        final float y = ev.getY(actionIndex);
                        // Find a child that can receive the event.
                        // Scan children from front to back.
                        //预排序子控件:按照子控件在Z轴的坐标，找出所有能接收该事件的子控件，并从前景到背景的方向遍历这些子控件
                        final ArrayList<View> preorderedList = buildOrderedChildList();
                        final boolean customOrder = preorderedList == null
                                && isChildrenDrawingOrderEnabled();
                        final View[] children = mChildren;
						//倒序遍历子控件
                        for (int i = childrenCount - 1; i >= 0; i--) {
                            final int childIndex = customOrder
                                    ? getChildDrawingOrder(childrenCount, i) : i;
                            final View child = (preorderedList == null)
                                    ? children[childIndex] : preorderedList.get(childIndex);

                            // If there is a view that has accessibility focus we want it
                            // to get the event first and if not handled we will perform a
                            // normal dispatch. We may do a double iteration but this is
                            // safer given the timeframe.
                            if (childWithAccessibilityFocus != null) {
                                if (childWithAccessibilityFocus != child) {
                                    continue;
                                }
                                childWithAccessibilityFocus = null;
                                i = childrenCount - 1;
                            }
							//判断事件触发点是否在该子控件内部，若不在直接下一轮循环
                            if (!canViewReceivePointerEvents(child)
                                    || !isTransformedTouchPointInView(x, y, child, null)) {
                                ev.setTargetAccessibilityFocus(false);
                                continue;
                            }

                            newTouchTarget = getTouchTarget(child);
                            if (newTouchTarget != null) {
                                // Child is already receiving touch within its bounds.
                                // Give it the new pointer in addition to the ones it is handling.
                                newTouchTarget.pointerIdBits |= idBitsToAssign;
                                break;
                            }

                            resetCancelNextUpFlag(child);
							//判断该子控件是否消费该事件
                            if (dispatchTransformedTouchEvent(ev, false, child, idBitsToAssign)) {
                                // Child wants to receive touch within its bounds.
                                mLastTouchDownTime = ev.getDownTime();
                                if (preorderedList != null) {
                                    // childIndex points into presorted list, find original index
                                    for (int j = 0; j < childrenCount; j++) {
                                        if (children[childIndex] == mChildren[j]) {
                                            mLastTouchDownIndex = j;
                                            break;
                                        }
                                    }
                                } else {
                                    mLastTouchDownIndex = childIndex;
                                }
                                mLastTouchDownX = ev.getX();
                                mLastTouchDownY = ev.getY();
                                newTouchTarget = addTouchTarget(child, idBitsToAssign);
                                alreadyDispatchedToNewTouchTarget = true;
                                break;
                            }

                            // The accessibility focus didn't handle the event, so clear
                            // the flag and do a normal dispatch to all children.
                            ev.setTargetAccessibilityFocus(false);
                        }
                        if (preorderedList != null) preorderedList.clear();
                    }

                    if (newTouchTarget == null && mFirstTouchTarget != null) {
                        // Did not find a child to receive the event.
                        // Assign the pointer to the least recently added target.
                        newTouchTarget = mFirstTouchTarget;
                        while (newTouchTarget.next != null) {
                            newTouchTarget = newTouchTarget.next;
                        }
                        newTouchTarget.pointerIdBits |= idBitsToAssign;
                    }
                }
    ...
</code></pre>

功能：查找消费事件的子View，过程如下：
* 先清空上次的TargetView；
* 将子控件按照Z坐标排序，值越大排在越后面，存入排序好子View集合中；
* 倒序遍历排序后的子View集合
* - 判断事件的x、y坐标是否在该子View的范围内
* - 若在该子View的范围内，且该子控件消费该事件，那么该子View就是TargetView，跳出遍历；否则判断下一个子View；


#### 第三部分


<pre><code>
            // Dispatch to touch targets.
            if (mFirstTouchTarget == null) {
				//没有找到消费该事件的子控件，那么将当前控件作为目标控件，并将事件传递给自己调用super.dispatchTouchEvent
                // No touch targets so treat this as an ordinary view.
                handled = dispatchTransformedTouchEvent(ev, canceled, null,
                        TouchTarget.ALL_POINTER_IDS);
            } else {
                // Dispatch to touch targets, excluding the new touch target if we already
                // dispatched to it.  Cancel touch targets if necessary.
                TouchTarget predecessor = null;
                TouchTarget target = mFirstTouchTarget;
                while (target != null) {
                    final TouchTarget next = target.next;
                    if (alreadyDispatchedToNewTouchTarget && target == newTouchTarget) {
                        handled = true;
                    } else {
                    	//当找到了消费该事件的子控件时，该值置为true
                        final boolean cancelChild = resetCancelNextUpFlag(target.child)
                                || intercepted;
                        if (dispatchTransformedTouchEvent(ev, cancelChild,
                                target.child, target.pointerIdBits)) {
                            //该子控件消费事件
                            handled = true;
                        }
						//若该置为true，表示链表不往下遍历了
                        if (cancelChild) {
                            if (predecessor == null) {
                                mFirstTouchTarget = next;
                            } else {
                                predecessor.next = next;
                            }
                            target.recycle();
                            target = next;
                            continue;
                        }
                    }
                    predecessor = target;
                    target = next;
                }
            }
</code></pre>

功能：
* 判断TargetView是否等于null
* - 若TargetView==null，那么事件交由自己，并调用super.dispatchTouchEvent方法，判断自己是否消费；
* - 若TargetView!=null，那么事件交由该子View，并调用child.dispatchTouchEvent方法，判断子View是否消费。
在dispatchTouchEvent方法中调用了dispatchTransformedTouchEvent(...)方法

<pre><code>
    private boolean dispatchTransformedTouchEvent(MotionEvent event, boolean cancel,View child, int desiredPointerIdBits) {
    ...
                if (child == null) {
					//TargetView为null，即所有子控件都不消费事件，则事件给自己，判断自己是否消费。
                    handled = super.dispatchTouchEvent(event);
                } else {
                    final float offsetX = mScrollX - child.mLeft;
                    final float offsetY = mScrollY - child.mTop;
                    event.offsetLocation(offsetX, offsetY);
					//TargetView不为null，即有子控件消费事件，则事假给该子控件，判断该子控件是否消费。
                    handled = child.dispatchTouchEvent(event);

                    event.offsetLocation(-offsetX, -offsetY);
                }
                return handled;
    ...
    }
</code></pre>

综上源码分析：
* 父View与子View之间的事件是通过dispatchTouchEvent(...)方法来传递的；
* onInterceptTouchEvent(...)方法和onTouchEvent(...)方法属于该View的内部方法，其他控件并不会调用；
* onInterceptTouchEvent(...)方法和onTouchEvent(...)方法都是在dispatchTouchEvent(...)方法内调用的；
* onTouchEvent(...)方法的返回值都是通过dispatchTouchEvent(...)方法返回的，（这也就解释了为什么只有onTouchEvent和dispatchTouchEvent两个方法可以确定TargetView，其实归根结底都是dispatchTouchEvent来确定TargetView的）。



## 常用滑动控件分析

### 默认状态下的处理情况
ScrollView/ HorizontalScrollView
		处理所有事件
		Action_Down:不拦截
		Action_Move:当竖向滑动距离大于某个值时，拦截
ListView
处理所有事件
Action_Down:不拦截、但处理
		Action_Move:当竖向滑动距离大于某个值时，拦截
ViewPager
		处理所有事件
		Action_Down:不拦截
		Action_Move:当横向滑动距离大于某个值时，拦截
		
以上滑动容器都不能设置onClickListener后者设置了也不响应，因为在它们的onTouchEvent中没有调用super.onTouchEvent（在View的onTouchEvent方法中才执行点击回调）。

### 常见嵌套问题的处理

#### ScrollView中嵌入ListView

**问题**：只能响应ScrollView的滑动，ListView滑动不响应

**Down事件**：ScrollView、ListView都不拦截，事件在ListView的onTouchEvent消费掉，此时目标控件为ListView

**Move事件**：ScrollView判断滑动距离大于mTouchSlop时，拦截，此时目标控件为ScrollView，后续所有的事件都不经过ListView
所有表现为ScrollView处理所有的move事件，即ScrollView能滑动，ListView不能滑动

**方法**

1. ScrollView的onInterceptTouchEvent返回false
2. B．	ListView的onTouchEvent中调用parent.requestDisallowIntercept是ScrollView不拦截
3. C．	将ListView实现onTouchListener接口，并设置给ScrollView，事件每次传递给ScrollView时，都会在onTouchEvent之前执行onTouch方法，我们就是在onTouch方法中处理ListView的滑动

#### ListView中嵌入HorizontalScrollView/ViewPager

**问题**：因内外控件滑动方向不一致，原生控件不产生冲突，但是当需要ListView的OnItemClickListener时，该监听器无效

**原因**：HorizontalScrollView、ViewPager中消费掉了down事件，ListView获取到down事件并进行消费。

**尝试解决的方法**：

1．在HorizontalScrollView、ViewPager的OnClickListner中调用ListView的OnItemClickListener，结果失败
原因：HorizontalScrollView、ViewPager的onTouchEvent中并没有调用View的onClick方法，故这两个控件没有点击事件（设置了也无效

#### ListView中的item为SwipeLayout(一个自定义的水平滑动控件)

**问题**：同样是ListView的onItemClickListener无法响应

**方法**

1. 丢弃原有的事件传递，我们将事件全部拦截在ListView中，再SwipeLayout中自定义一个方法（如：onSiwpe（MotionEvent）），将事件传递给SwipeLayout，这样事件在ListView和SwipeLayout中都可以进行各自的消费（即利用两次）
2. SwipeLayout不消费任何事件,并将SwipeLayout的左右移动交给ListView处理，ListView使用scrollTo、scrollBy移动SwipeLayout
3. 给SwipeLayout设置OnClickListener，并在onClick中回调OnItemClickListener(即利用SwipeLayout来响应ListView的接口)

#### 伪代码
* ViewGroup的伪代码
<pre><code>
	dispatchTouchEvent(event){
		If(!disallowIntercept){
			interceptTouchEvent();-------------------------------调用拦截方法
	}
	…
	for(倒序遍历所有子控件){
		if(当前时间的坐标在该子控件范围内){
			mTargetView=该子控件;
			break;
	}
	}
	…
	If(mTargetView==null){
		handle=Super.dispatchTouchEvent();--------------调用父类的分发，即View的分发事件
	}else{
		handle=mTargetView.dispatchTouchEvnet();----调用子控件的分发
	}
	…
	onTouchEvent();
	return handle;------------------------------------------------此处的返回值会影响目标控件的确定
	}

</code></pre>
* View的伪代码
<pre><code>
	dispatchTouchEvent(event){
		if(设置OnTouchListener并且消费事件){
			reslut=true;
	}
	if(!reslut&&onTouchEvent()){-----------------------调用onTouchEvent方法，消费则结果为true
		reslut=true
	}
	return result;--------------------------------------------此处的返回值会影响目标控件的确定
	}

</code></pre>