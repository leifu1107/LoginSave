package leifu.loginsave;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhouwei.library.CustomPopWindow;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xw.repo.XEditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leifu.shapelibrary.ShapeView;


public class MainActivity extends AppCompatActivity {


    @BindView(R.id.et_acount)
    XEditText et_acount;
    @BindView(R.id.et_pwd)
    XEditText et_pwd;
    @BindView(R.id.btn_login)
    ShapeView btn_login;
    @BindView(R.id.tv_forgetPwd)
    TextView tv_forgetPwd;
    @BindView(R.id.iv_btn)
    ImageView iv_btn;

    List<UserData> arrayList;
    private FileOutputStream fileOutputStream;
    private FileInputStream fileInputStream;
    private BufferedWriter bufferedWriter;
    private Gson gson;
    private BufferedReader bufferedReader;
    private CustomPopWindow mCustomPopWindow;
    private LoginAcountAdapter loginAcountAdapter;
    View pop_loginacount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //绑定activity
        ButterKnife.bind(this);
        gson = new Gson();
        arrayList = new ArrayList<>();
        isSaveacountAndPwd();
        iv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSaveacountAndPwd();
            }
        });

    }

    /**
     * 是否有以前保存过的账号和密码
     */
    private void isSaveacountAndPwd() {
        String data = readFile();
        Log.e("aaa", "isSaveacountAndPwd: " + data);
        if (!TextUtils.isEmpty(data) && !data.equals("[]")) {
            arrayList = gson.fromJson(data, new TypeToken<List<UserData>>() {
            }.getType());
            et_acount.setText(arrayList.get(0).getAcount());
            et_pwd.setText(arrayList.get(0).getPasswd());
        }
    }

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


    @OnClick({R.id.btn_login, R.id.ivbtn_drop})
    public void onClick(View view) {
        switch (view.getId()) {
            //点击登录按钮
            case R.id.btn_login:
                if (TextUtils.isEmpty(et_acount.getText().toString().trim())) {
                    Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(et_pwd.getText().toString().trim())) {
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                } else {
                    saveacountAndPwd();
                    Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.ivbtn_drop:
                //创建并显示popWindow
                if (pop_loginacount==null) {
                    pop_loginacount = LayoutInflater.from(MainActivity.this).inflate(R.layout.pop_loginacount, null);
                }
                //处理popWindow 显示内容
                handleListView(pop_loginacount);
                mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(this)
                        .setView(pop_loginacount)
                        .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)//显示大小
                        .enableBackgroundDark(false) //弹出popWindow时，背景是否变暗
                        .create()
                        .showAsDropDown(et_acount, 0, 0);
                break;
        }
    }

    private void handleListView(View pop_loginacount) {
        ListView listView = (ListView) pop_loginacount.findViewById(R.id.lv_acount);
        loginAcountAdapter = new LoginAcountAdapter(MainActivity.this, arrayList);
        listView.setAdapter(loginAcountAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                et_acount.setText(arrayList.get(position).getAcount());
                et_pwd.setText(arrayList.get(position).getPasswd());
                mCustomPopWindow.dissmiss();
            }
        });
    }

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
}
