﻿#摘要
本文将介绍一种常用的神经网络---循环神经网络(recurrent neural network,RNN)以及循环神经网络的一个重要的变体---长短时记忆网络(long short-term memory,LSTM).
#循环神经网络
循环神经网络的主要用途是处理和预测序列数据.传统的卷积神经网络(CNN)或者全连接神经网络(FC)都是从输入层到隐含层再到输出层,层与层之间是全连接或部分连接的,但是每层之间是没有连接的.考虑这样一个问题,要预测一个句子的下一个单词是什么,比如当前单词是"很",前一个单词是"天空",那么下一个单词大概是"蓝".循环神经网络会记忆之前的信息,并利用之前的信息影响后面节点的输出.也就是说,循环神经网络的隐藏层之间的结点是有连接的,隐藏层的输入不仅包括输入层的输出,还包括上一时刻隐藏层的输出.
如下图所示:
![这里写图片描述](https://upload-images.jianshu.io/upload_images/42741-f16c8acc01d2d469.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/458)
这是一个典型的循环神经网络.对于循环神经网络一个重要的概念就是时刻.循环神经网络会对每一个时刻的输入结合当前模型的状态给出一个输出.从图中可以看到,循环神经网络的主体结构A的输入除了来自输入层X_t,还有一个循环的边来提供当前时刻的状态.在每个时刻,循环神经网络的模块A会读取t时刻的输入X_t,并输出一个值h_t,同时A的状态会从当前步传递到下一步.
因此,循环神经网络理论上可以看做是同一神经网络结构被无限复制的结果.但是处于优化的考虑,目前循环神经网络无法做到真正的无限循环,所以现实中一般会将循环体展开,得到下图:
![这里写图片描述](https://upload-images.jianshu.io/upload_images/42741-d6749df8fb93b0b0.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/700)
从循环神经网络的结构特征可以很容易看出它所擅长解决的问题是与时间序列相关的,循环神经网络也是处理这类问题最自然的神经网络结构.
对于一个序列数据,可以将这个序列上不同时刻的数据依次传入循环神经网络的输入层,而输出可以是对序列中下一个时刻的预测,也可以是对当前时刻信息的处理结果(比如语音识别结果).循环神经网络要求每一个时刻都有一个输入,但是不一定每一个时刻都需要有输出.

如前所述,循环神经网络可以被看做是同一神经网络结构在时间序列上被复制多次的结果,这个被复制多次的结构被称之为结构体.如何设计循环体的网络结构是循环神经网络解决实际问题的关键,类似卷积神经网络参数共享的原则,循环神经网络结构中的参数在不同时刻也是共享的.

如图:
![这里写图片描述](https://upload-images.jianshu.io/upload_images/42741-d6749df8fb93b0b0.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/700)
![这里写图片描述](https://img-blog.csdn.net/20180621235116272?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM2OTgyMTYw/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
上图展示了一个使用最简单的循环神经网络结构的循环神经网络,在这个循环体中只用了一个类似全连接层的神经网络结构.循环神经网络中的状态是通过一个向量来表示的,假设其为h,这个向量的维度也称为循环神经网络隐藏层的大小.假设输入向量的维度为x,那么上图中循环体的全连接层神经网络的输入大小为h+x.也就是将上一时刻的状态与当前时刻的输入拼接成一个大的向量作为循环体中神经网络的输入,因为该神经网络的输出为当前时刻的状态,于是输出层的节点个数也为h,循环体中的参数个数为(h+x)*h+h个.循环体中的神经网络输出不仅提供给下一时刻作为状态,也会提供给当前时刻的输出.
为了将当前时刻的状态转化为最终的输出,循环神经网络还需要另外一个全连接神经网络来完成这个过程.这和卷积神经网络最后的全连接层的意义是一样的.
为了利于大家理解循环神经网络钱箱传播的过程,这里举一个简单的例子,如图:
![这里写图片描述](https://img-blog.csdn.net/20180622001207196?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM2OTgyMTYw/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
在上图中,假设状态的维度为2,输入\输出的维度为1,而且循环体中的全连接层的权重为:

$$Wrnn=
 \left[
 \begin{matrix}
   0.1 & 0.2  \\
   0.3&0.4 \\
   0.5& 0.6
  \end{matrix}
  \right] 
$$
偏置项的大小为$$Brnn=
 \left[
 \begin{matrix}
  0.1  &   -0.1
  \end{matrix}
  \right] 
$$
用于输出的全连接层权重为$$Woutput=
 \left[
 \begin{matrix}
  1.0  \\
    2.0
  \end{matrix}
  \right] 
$$
偏置项大小为Boutput=0.1.
那么在t0时刻,因为没有上一时刻,所以将状态初始为[0,0],而当前输入为1,所以拼接得到的向量为[0,0,1],通过循环体中的全连接层神经网络得到的结果为:
![这里写图片描述](https://img-blog.csdn.net/20180622003136609?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM2OTgyMTYw/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
将这个结果作为下一个时刻的输入状态,同时循环神经网络也会使用该状态产生输出.将该向量作为输入提供给用于输出的全连接层神经网络可以得到t0时刻的最终输出:
![这里写图片描述](https://img-blog.csdn.net/20180622003342148?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM2OTgyMTYw/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
使用t0时刻的状态可以类似地推导得出t1时刻的状态为[0.860,0.884],而t1时刻的输出为2.73.

得到神经网络的前向传播结果之后,可以和其他神经网络类似地定义损失函数.
循环神经网络唯一的区别在于因为它每个时刻都有一个输出,所以循环神经网络的总损失为所有时刻(或者部分时刻)上的损失函数的和.
理论上循环神经网络可以支持任意序列长度的序列,然而在实际中,如果序列过长会导致优化时出现梯度消散的问题,所以实际中一般会规定一个最大长度,当序列超过规定长度之后会对序列进行截断.
#长短时记忆网络(LSTM)结构
在有些情况下,模型仅仅需要短期内的信息来执行当前的任务,比如预测短语"大海的颜色是蓝色"中最后一个单词"蓝色"的时候,模型不需要记忆这个短语之前更长的上下文信息,因为这一句话已经包含了足够的信息来预测最后一个词,在这种情况下循环神经网络可以比较容易地利用先前的信息.
但是同样也有一些上下文场景更加复杂的情况.比如当模型去预测"某地开设了大量的工厂,空气污染十分严重......这里的天空是灰色的"的最后一个单词的时候,仅仅根据短期依赖就无法很好地解决这种问题.因为只根据最后一段,最后一个词可以是"蓝色的"或者"黑色的".如果模型要预测清楚具体是什么颜色,就需要考虑先前提到的离当前位置较远的上下文信息.因此,当前预测位置和相关信息之间的文本间隔就有可能变的很大.当这个间隔很大时,上文中的循环神经网络就有可能丧失学习到距离如此远的信息的能力.或者在复杂语境中,有用信息的间隔有大有小,长短不一,循环神经网络的性能也会受到限制.

长短时记忆网络(long short term memory,LSTM)的设计就是为了解决这个问题的,而且循环神经网络被广泛应用的关键就是LSTM,在很多任务上,采用LSTM结构的神经网络比标准的循环神经网络表现的更好.
![这里写图片描述](https://img-blog.csdn.net/20180622234313328?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM2OTgyMTYw/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
如上图所示,LSTM靠一些"门"的结构让信息有选择地影响循环神经网络中每个时刻的状态.
所谓"门"的结构就是一个使用sigmoid神经网络和一个按位做乘法的操作,这两个操作合在一起就是一个"门"的结构.之所以该结构叫"门"是唯一能使用sigmoid作为激活函数的全连接层神经网络层会输出一个0到1之间的数值,描述当前输入有多少信息量可以通过这个结构.于是这个结构就类似于一个门,当门打开时(sigmoid神经网络的输出为1时),全部信息可以通过;当门关上时(sigmoid神经网络的输出为0时),任何信息都无法通过.
具体的每一个门的工作原理如下:
为了使循环神经网更有效的保存长期记忆, 图中“遗忘门”和“输入门”至关重要,它们是LSTM结构的核心.“遗忘门”的作用是让循环神经网络“忘记”之前没有用的信息.比如一段文章中先介绍了某地原来是绿水蓝天, 但后来被污染了.于是在看到被污染了之后,循环神经网络应该“忘记”之前绿水蓝天的状态.这个工作是通过“遗忘门”来完成的.“遗忘门”会根据当前的输入X_t和上一时刻的状态C_t-1和上一时刻的输出H_t-1共同决定哪一部分记忆需要被遗忘.在循环神经网络“忘记”了部分之前的状态后,它还需要从当前的输入补充最新的记忆.这个过程就是“输入门”完成的.“输入门”会根据X_t、  C_t-1和H_t-1决定哪些部分将进入当前时刻的状态C_t。比如当看到文章中提到环境被污染之后，模型需要将这个信息写入新的状态。通过“遗忘门”和“输入门”，LSTM结构可以更加有效的决定哪些信息应该被遗忘，哪些信息应该得到保留。
LSTM结构在计算得到新的状态C_t后需要产生当前时刻的输出，这个过程是通过“输出门”完成的。“输出门”会根据最新的状态C_t以及上一时刻的输出H_t-1和当前的输入X_t来决定该时刻的输出H_t。比如当前的状态为被污染，那么“天空的颜色”后面的单词很可能就是“灰色的”。