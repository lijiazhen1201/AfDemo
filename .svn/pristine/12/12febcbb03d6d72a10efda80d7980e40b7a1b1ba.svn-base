package cn.appoa.aframework.listener;

import java.util.List;

import cn.appoa.aframework.utils.JsonUtils;
import cn.appoa.aframework.view.IBaseView;

/**
 * 解析实体bean的回调
 *
 * @param <T> 实体bean
 */
public abstract class VolleyDatasListener<T> extends VolleySuccessListener {

    protected Class<T> clazz;

    public VolleyDatasListener(IBaseView mView, Class<T> clazz) {
        super(mView);
        this.clazz = clazz;
    }

    public VolleyDatasListener(IBaseView mView, String tag, Class<T> clazz) {
        super(mView, tag);
        this.clazz = clazz;
    }

    public VolleyDatasListener(IBaseView mView, String tag, int messageType, Class<T> clazz) {
        super(mView, tag, messageType);
        this.clazz = clazz;
    }

    @Override
    public void onSuccessResponse(String response) {
        if (clazz != null) {
            onDatasResponse(JsonUtils.parseJson(response, clazz));
        }
    }

    /**
     * 解析的回调
     *
     * @param datas
     */
    public abstract void onDatasResponse(List<T> datas);
}
