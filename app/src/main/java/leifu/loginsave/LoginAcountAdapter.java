package leifu.loginsave;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

/**
 * 创建人: 雷富
 * 创建时间: 2018/1/26 11:34
 * 描述:
 */

public class LoginAcountAdapter extends BaseAdapter{
    private Context context;
    private List<UserData> arrayList;

    public LoginAcountAdapter(Context context, List<UserData> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Viewholder viewholder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_pop_loginacount, null);
            viewholder = new Viewholder(convertView);
            convertView.setTag(viewholder);
        } else {
             viewholder = (Viewholder) convertView.getTag();
        }
        viewholder.tv_acount.setText(arrayList.get(position).getAcount());
        viewholder.ivbtn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayList.remove(position);
                notifyDataSetChanged();
                saveacountAndPwd();
            }
        });
        return convertView;
    }
    /**
     * 点击删除按钮,重新保存账号和密码
     */
    private void saveacountAndPwd() {
            try {

                String data = new Gson().toJson(arrayList);
                //MODE_PRIVATE 在该模式下，写入的内容会覆盖原文件的内容
                FileOutputStream fileOutputStream = context.openFileOutput("user", MODE_PRIVATE);
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                Log.e("aaa", "点击删除按钮 " + data);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
        }
    }
    public class Viewholder {
        @BindView(R.id.tv_acount)
        TextView tv_acount;
        @BindView(R.id.ivbtn_delete)
        ImageView ivbtn_delete;
        public Viewholder(View view) {
            ButterKnife.bind(this,view);
        }
    }

}
