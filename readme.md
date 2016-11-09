#InputView
A view solve the layout change problem when the softKeyBoard show.

##Getting Started
You can add the cod into your module build.gradle
```java
dependencies {
    compile 'com.github.siyehua:inputview:1.0.1'
}
```
##Usage
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
more information, you can [click](/app/src/main/java/com/siyehua/inputsoft/StartActivity.java).

##How do is work?
![Progress](/progress.png)

1. init the view. add the layout into inputview and set the callback.
2. user click the editText and show the inputview.
3. showing the inputview: popup the softKeyBoard → get the keyboard height → set editext margin and set visible.
4. user input text, call back the text

##Demo
![demo-git](/demo_git.gif)

more information, you can [click](/app/src/main/java/com/siyehua/inputsoft/StartActivity.java).


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




