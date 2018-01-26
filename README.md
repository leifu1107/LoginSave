# LoginSave
类似于QQ登录后保存账号和密码

实现原理:文件存储openFileOutput("user", MODE_PRIVATE);可下载demo详看

### 效果图
![](https://github.com/leifu1107/LoginSave/raw/master/screenshots/1.jpg) 

### 1.首次进入APP,是否有以前保存过的账号和密码

```java
/**
     * 读取存储的文件内容
     *
     * @return 账号和密码的json数据
     */
    private String readFile() {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try {
            //以防止没有创建时读取错误
            fileOutputStream = openFileOutput("user", MODE_APPEND);
            fileInputStream = openFileInput("user");
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            fileOutputStream.close();
            fileInputStream.close();
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
```
### 2.点击登录按钮,保存账号和密码
```java
 /**
     * 点击登录按钮,保存账号和密码
     */
    private void saveacountAndPwd() {
        if (!arrayList.contains(new UserData(et_acount.getText().toString().trim(), et_pwd.getText().toString().trim()))) {
            try {
                arrayList.add(new UserData(et_acount.getText().toString().trim(), et_pwd.getText().toString().trim()));
                String data = gson.toJson(arrayList);
                //MODE_PRIVATE 在该模式下，写入的内容会覆盖原文件的内容
                fileOutputStream = openFileOutput("user", MODE_PRIVATE);
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                Log.e("aaa", "gson.toJson(arrayList): " + data);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
```
