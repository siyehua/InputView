#InputView
一个用来解决输入文字问题的view

当输入框置于底部时候,需要控制Activity的windowSoftInputMode以便
解决输入框被遮挡/覆盖,layout上移等问题,当使用沉浸式模式后,输入框下沉等bug

inputview通过弹出方式一键解决这些问题


##添加
在你的Moduel的build.gralde的文件中添加以下代码
```java
dependencies {
    compile 'com.github.siyehua:inputview:1.0.1'
}
```
##使用
```java
    private InputView inputView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        final TextView textView = (TextView) findViewById(R.id.tv_input);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputView.show(textView.getText());
            }
        });
        inputView = InputView.getInsance(this).init(R.layout.item, true, true, false, new
                InputView.CallBack() {
            @Override
            public void textChange(CharSequence s) {
                textView.setText(s);
            }
        });
    }
```
更新信息,请 [点击](/app/src/main/java/com/siyehua/inputsoft/StartActivity.java).

##工作流程
![Progress](/progress.png)

1. 初始化view,传入布局,配置信息,以及callback回调
2. 调用show方法
3. 调用show方法后,inputview会自动弹出键盘,量取键盘高度,并设置输入框位置搞好位于输入框之上.
4. 用户输入文字过程中,会实时回调给Activity

##演示
<img src="/demo_git.gif" width = "240" height = "427" alt="demo" align=center />

更新信息,请 [点击](/app/src/main/java/com/siyehua/inputsoft/StartActivity.java).


##License
```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```




