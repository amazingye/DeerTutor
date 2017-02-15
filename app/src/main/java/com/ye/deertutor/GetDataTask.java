package com.ye.deertutor;

import android.os.AsyncTask;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nhaarman.listviewanimations.appearance.simple.SwingLeftInAnimationAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by X1 Carbon on 2017/2/12.
 */

public class GetDataTask extends AsyncTask<Void, Void, Void> {

    private PullToRefreshListView mPullRefreshListView;
    /*private ArrayAdapter<String> mAdapter;
    private LinkedList<String> mListItems;*/

    private Map<String, Object> profileMap;
    private List<Map<String, Object>> mprofilesList;
    private SwingLeftInAnimationAdapter mListViewAnimationAdapter;

    public GetDataTask(PullToRefreshListView listView,
                       /*ArrayAdapter<String> adapter*/SwingLeftInAnimationAdapter ListViewAnimationAdapter,
                       /*LinkedList<String> listItems*/List<Map<String, Object>> profilesList) {
        // TODO 自动生成的构造函数存根
        mPullRefreshListView = listView;
        /*mAdapter = adapter;
        mListItems = listItems;*/
        mprofilesList = profilesList;
        mListViewAnimationAdapter = ListViewAnimationAdapter;
    }

    @Override
    protected Void doInBackground(Void... params) {
        //模拟请求
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {

        super.onPostExecute(result);
        //得到当前的模式
        Mode mode = mPullRefreshListView.getCurrentMode();
        if(mode == Mode.PULL_FROM_START) {
            //mListItems.addFirst("这是刷新出来的数据");
        }
        else {
            //mListItems.addLast("这是刷新出来的数据");
            //mprofilesList.add();
        }
        // 通知数据改变了
        mListViewAnimationAdapter.notifyDataSetChanged();
        // 加载完成后停止刷新
        mPullRefreshListView.onRefreshComplete();

    }



}