package comulez.github.erecyclerview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Handler handler;
    private ArrayList<String> listData;
    private int time = 0;
    private SimpleAdapter mAdapter;
    private ERecyclerView mRecyclerView;
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (ERecyclerView) findViewById(R.id.recyView);
        handler = new Handler();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        // mRecyclerView.setRefreshHeader(new ArrowRefreshHeader(this));
        listData = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            listData.add("time=" + time + "--Item=" + i);
        }
        mAdapter = new SimpleAdapter<String, BaseHolder>(listData) {

            @Override
            protected void convert(BaseHolder holder, String item, int position) {
                holder.setText(R.id.tv, item);
            }

            @Override
            protected int getLayoutId(int viewType) {
                return R.layout.item_main;
            }
        };
        mRecyclerView.setFreshListener(new ERecyclerView.FreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        time++;
                        listData.clear();
                        for (int i = 0; i < 20; i++) {
                            listData.add("time=" + time + "--Item=" + i);
                        }
                        mRecyclerView.refreshComplete();
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int kk = listData.size();
                        if (kk < 22) {
                            for (int i = kk; i < kk + 20; i++) {
                                listData.add("time=" + time + "--Item=" + i);
                            }
                            mRecyclerView.loadMoreComplete();
                        } else {
                            for (int i = kk; i < kk + 15; i++) {
                                listData.add("time=" + time + "--Item=" + i);
                            }
                            mRecyclerView.loadAllComplete(true);
                        }
                    }
                }, 1000);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }
}
