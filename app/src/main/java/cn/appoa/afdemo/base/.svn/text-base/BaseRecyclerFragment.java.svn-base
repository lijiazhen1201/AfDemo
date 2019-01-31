package cn.appoa.afdemo.base;

import com.scwang.smartrefresh.layout.fragment.PullToRefreshCoordinatorLayoutFragment;

import cn.appoa.aframework.utils.AsyncRun;

public abstract class BaseRecyclerFragment<T> extends PullToRefreshCoordinatorLayoutFragment<T> {

    /**
     * 是否开启测试（调接口时候改为false）
     */
    private static boolean isTestData = false;

    @Override
    public void initData() {
        if (isTestData) {
            AsyncRun.runInBack(new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(1 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    AsyncRun.runInMain(new Runnable() {

                        @Override
                        public void run() {
                            onSuccessResponse(null);
                        }
                    });
                }
            });
        } else {
            super.initData();
        }
    }

    @Override
    public void showLoading(CharSequence message) {
        // super.showLoading(message);
    }

    @Override
    public void dismissLoading() {
        // super.dismissLoading();
    }

//    // 需要自动加载更多请取消注释以下代码
//    @Override
//    public boolean setRefreshMode() {
//        return false;
//        // return true;
//    }
//
//    @Override
//    public void initRefreshLayout(Bundle savedInstanceState) {
//        // refreshLayout.setEnableAutoLoadMore(true);// 是否启用列表惯性滑动到底部时自动加载更多
//    }
//
//    @Override
//    protected void setAdapter() {
//        if (isHasLoadMore()) {
//            if (adapter != null) {
//                adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
//
//                    @Override
//                    public void onLoadMoreRequested() {
//                        onLoadMore(refreshLayout);
//                    }
//                }, recyclerView);
//            }
//        }
//    }
//
//    /**
//     * 是否自动加载更多
//     */
//    public boolean isHasLoadMore() {
//        return true;
//    }

}
