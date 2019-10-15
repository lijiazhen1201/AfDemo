package cn.appoa.afdemo.activity;

/**
 * 自定义控件
 */
public class CustomWidgetActivity extends AbsListActivity {

    @Override
    protected CharSequence initTitle() {
        return "自定义控件";
    }

    @Override
    protected String[] initTitles() {
        return new String[]{
                "HeaderGridView\n【可添加头布局和脚布局的GridView】",
                "FlowLayout\n【自动换行布局，流式布局，商品规格选择】",
                "GridPasswordView\n【支付密码输入框】",
                "CenterSquareImageView\n【图片截取正中间的正方形部分的ImageView】",
                "SuperImageView\n【圆形，圆角，带边框的ImageView】",
                "MaxHeightLayout\n【可设置最大高度的FrameLayout】",
                "RatioRelativeLayout\n【可设置宽高比的RelativeLayout】",
                "SwipeMenuDelLayout\n【Item侧滑删除菜单Layout】",
                "NoScrollView\n【ScrollView嵌套滑动布局】",
                "HeightWrapViewPager\n【自动适应图片高度的ViewPager】",
                "UPMarqueeView\n【仿淘宝头条，向上翻页】",
                "SideBar\n【字母表导航，城市选择】",
                "NumberAnimTextView\n【数字增加和减小动画TextView】",
                "WheelView\n【滚轮控件，各种底部滚轮选择框】",
        };
    }

    @Override
    protected Class[] initClass() {
        return new Class[]{
                HeaderGridViewActivity.class,
                FlowLayoutActivity.class,
                GridPasswordViewActivity.class,
                CenterSquareImageViewActivity.class,
                SuperImageViewActivity.class,
                MaxHeightLayoutActivity.class,
                RatioRelativeLayoutActivity.class,
                SwipeMenuDelLayoutActivity.class,
                NoScrollViewActivity.class,
                HeightWrapViewPagerActivity.class,
                UPMarqueeViewActivity.class,
                SideBarActivity.class,
                NumberAnimTextViewActivity.class,
                WheelViewActivity.class,
        };
    }

}
