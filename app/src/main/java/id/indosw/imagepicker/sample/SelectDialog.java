package id.indosw.imagepicker.sample;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public class SelectDialog extends Dialog implements OnClickListener,OnItemClickListener {
    private final SelectDialogListener mListener;
    private final Activity mActivity;
    private final List<String> mName;
    private String mTitle;
    private boolean mUseCustomColor = false;
    private int mFirstItemColor;
    private int mOtherItemColor;

    public interface SelectDialogListener {
        void onItemClick(AdapterView<?> parent, View view, int position, long id);
    }


    private SelectDialogCancelListener mCancelListener;

    public interface SelectDialogCancelListener {
        void onCancelClick(View v);
    }

    public SelectDialog(Activity activity, int theme,
                        SelectDialogListener listener,List<String> names) {
        super(activity, theme);
        mActivity = activity;
        mListener = listener;
        this.mName=names;

        setCanceledOnTouchOutside(true);
    }

    /**
     * @param activity 调用弹出菜单的activity
     * @param theme 主题
     * @param listener 菜单项单击事件
     * @param cancelListener 取消事件
     * @param names 菜单项名称
     *
     */
    public SelectDialog(Activity activity, int theme,SelectDialogListener listener,SelectDialogCancelListener cancelListener ,List<String> names) {
        super(activity, theme);
        mActivity = activity;
        mListener = listener;
        mCancelListener = cancelListener;
        this.mName=names;

        // 设置是否点击外围不解散
        setCanceledOnTouchOutside(false);
    }

    /**
     * @param activity 调用弹出菜单的activity
     * @param theme 主题
     * @param listener 菜单项单击事件
     * @param names 菜单项名称
     * @param title 菜单标题文字
     *
     */
    public SelectDialog(Activity activity, int theme,SelectDialogListener listener,List<String> names,String title) {
        super(activity, theme);
        mActivity = activity;
        mListener = listener;
        this.mName=names;
        mTitle = title;

        // 设置是否点击外围可解散
        setCanceledOnTouchOutside(true);
    }

    public SelectDialog(Activity activity, int theme,SelectDialogListener listener,SelectDialogCancelListener cancelListener,List<String> names,String title) {
        super(activity, theme);
        mActivity = activity;
        mListener = listener;
        mCancelListener = cancelListener;
        this.mName=names;
        mTitle = title;

        // 设置是否点击外围可解散
        setCanceledOnTouchOutside(true);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        @SuppressLint("InflateParams")
        View view = getLayoutInflater().inflate(R.layout.view_dialog_select,
                null);
        setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        Window window = getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = mActivity.getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = LayoutParams.MATCH_PARENT;
        wl.height = LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        onWindowAttributesChanged(wl);

        initViews();
    }

    private void initViews() {
        DialogAdapter dialogAdapter=new DialogAdapter(mName);
        ListView dialogList=(ListView) findViewById(R.id.dialog_list);
        dialogList.setOnItemClickListener(this);
        dialogList.setAdapter(dialogAdapter);
        Button mMBtn_Cancel = (Button) findViewById(R.id.mBtn_Cancel);
        TextView mTv_Title = (TextView) findViewById(R.id.mTv_Title);


        mMBtn_Cancel.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            if(mCancelListener != null){
                mCancelListener.onCancelClick(v);
            }
            dismiss();
        });

        if(!TextUtils.isEmpty(mTitle) && mTv_Title != null){
            mTv_Title.setVisibility(View.VISIBLE);
            mTv_Title.setText(mTitle);
        }else{
            Objects.requireNonNull(mTv_Title).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        dismiss();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        mListener.onItemClick(parent, view, position, id);
        dismiss();
    }
    private class DialogAdapter extends BaseAdapter {
        private final List<String> mStrings;
        private final LayoutInflater layoutInflater;
        public DialogAdapter(List<String> strings) {
            this.mStrings = strings;
            this.layoutInflater=mActivity.getLayoutInflater();
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mStrings.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return mStrings.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Viewholder viewholder;
            if (null == convertView) {
                viewholder =new Viewholder();
                convertView=layoutInflater.inflate(R.layout.view_dialog_item, null);
                viewholder.dialogItemButton=(TextView) convertView.findViewById(R.id.dialog_item_bt);
                convertView.setTag(viewholder);
            }else{
                viewholder =(Viewholder) convertView.getTag();
            }
            viewholder.dialogItemButton.setText(mStrings.get(position));
            if (!mUseCustomColor) {
                mFirstItemColor = mActivity.getResources().getColor(R.color.blue);
                mOtherItemColor = mActivity.getResources().getColor(R.color.blue);
            }
            if (1 == mStrings.size()) {
                viewholder.dialogItemButton.setTextColor(mFirstItemColor);
                viewholder.dialogItemButton.setBackgroundResource(R.drawable.dialog_item_bg_only);
            } else if (position == 0) {
                viewholder.dialogItemButton.setTextColor(mFirstItemColor);
                viewholder.dialogItemButton.setBackgroundResource(R.drawable.select_dialog_item_bg_top);
            } else if (position == mStrings.size() - 1) {
                viewholder.dialogItemButton.setTextColor(mOtherItemColor);
                viewholder.dialogItemButton.setBackgroundResource(R.drawable.select_dialog_item_bg_buttom);
            } else {
                viewholder.dialogItemButton.setTextColor(mOtherItemColor);
                viewholder.dialogItemButton.setBackgroundResource(R.drawable.select_dialog_item_bg_center);
            }
            return convertView;
        }

    }

    public static class Viewholder {
        public TextView dialogItemButton;
    }

    /**
     * 设置列表项的文本颜色
     */
    public void setItemColor(int firstItemColor, int otherItemColor) {
        mFirstItemColor = firstItemColor;
        mOtherItemColor = otherItemColor;
        mUseCustomColor = true;
    }
}
