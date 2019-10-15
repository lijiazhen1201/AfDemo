# AfDemo

## 如何使用它？

- 先在 build.gradle(Project:XXXX) 的 repositories 添加:

```java
    allprojects {
        repositories {
            ...
            maven { url "https://jitpack.io" }
        }
    }
```

- 然后在 build.gradle(Module:app) 的 dependencies 添加:

```java
    dependencies {
            ...
            implementation 'com.github.lijiazhen1201:AfDemo:VERSION_CODE'
    }
```

**注意：**如果只想引用部分功能，可以这样添加

```java
    dependencies {
            ...
            implementation 'com.github.lijiazhen1201.AfDemo:AFramework:VERSION_CODE'
            implementation 'com.github.lijiazhen1201.AfDemo:AndroidImageCropper:VERSION_CODE'
            implementation 'com.github.lijiazhen1201.AfDemo:JiaoZiVideoPlayer:VERSION_CODE'
            implementation 'com.github.lijiazhen1201.AfDemo:QrCodeScan:VERSION_CODE'
            implementation 'com.github.lijiazhen1201.AfDemo:SmartRefreshLayoutFragment:VERSION_CODE'
            implementation 'com.github.lijiazhen1201.AfDemo:WxCameraView:VERSION_CODE'
            implementation 'com.github.lijiazhen1201.AfDemo:WxImageSelector:VERSION_CODE'
            implementation 'com.github.lijiazhen1201.AfDemo:WxVoiceSelector:VERSION_CODE'
    }
```

用这里的真实发行版本号, 替换 VERSION_CODE

**注意：**两个都必须要写，要不然无法加载成功。

- 创建MyApplication类继承框架的AfApplication

```java
import cn.appoa.aframework.app.AfApplication;

public class MyApplication extends AfApplication {

    @Override
    public void initApplication() {

    }
}
```

**注意：**在AndroidManifest中不要忘记注册

```java
<application
        android:name=".app.MyApplication" >

        ...

</application>
```

- 在AndroidManifest的application节点中添加设计稿的尺寸

```java
        <!-- 设计稿的尺寸 -->
        <meta-data
            android:name="design_width"
            android:value="1080" />
        <meta-data
            android:name="design_height"
            android:value="1920" />
```

**注意：**宽高的值取自UI设计图的宽高，单位像素

### 以下代码按需添加

- 适配全面屏

在AndroidManifest的application节点中添加

```java
        <!-- 适配全面屏 -->
        <meta-data
            android:name="android.max_aspect"
            android:value="2.1" />
```

- 适配拍照（直接或间接引用AFramework）

在AndroidManifest的manifest节点中添加权限

```java
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
```

在AndroidManifest的application节点中添加

```java
        <!-- provider authorities必须为【包名+.fileprovider】 -->
        <provider
            android:name="android.support.v4.content.FileProvider"
            android:authorities="${applicationId}.fileprovider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/file_provider_paths" />
        </provider>
```

- 图片裁剪（直接或间接引用AndroidImageCropper）

在AndroidManifest的manifest节点中添加权限

```java
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
```

在AndroidManifest的application节点中添加

```java
        <!-- 图片裁剪 -->
        <activity
            android:name="com.theartofdev.edmodo.cropper.CropImageActivity"
            android:screenOrientation="portrait"
            android:theme="@style/AppThemeFullscreen" />
```

- 图片多选（直接或间接引用WxImageSelector）

在AndroidManifest的manifest节点中添加权限

```java
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
```

在AndroidManifest的application节点中添加

```java
        <!-- 图片多选 -->
        <activity
            android:name="cn.appoa.wximageselector.ShowBigImageListActivity"
            android:screenOrientation="portrait"
            android:theme="@style/AppThemeBlack" />
        <activity
            android:name="cn.appoa.wximageselector.ClipImageActivity"
            android:screenOrientation="portrait"
            android:theme="@style/AppThemeBlack" />
        <activity
            android:name="cn.appoa.wximageselector.ImageSelectorActivity"
            android:screenOrientation="portrait"
            android:theme="@style/AppThemeBlack" />
        <activity
            android:name="cn.appoa.wximageselector.PreviewActivity"
            android:screenOrientation="portrait"
            android:theme="@style/AppThemeBlack" />
```

- 视频拍摄（直接或间接引用WxCameraView）

在AndroidManifest的manifest节点中添加权限

```java
    <uses-permission android:name="android.permission.RECORD_AUDIO" />
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
```

在AndroidManifest的application节点中添加

```java
        <!-- 视频拍摄 -->
        <activity
            android:name="com.cjt2325.cameralibrary.JCameraViewActivity"
            android:screenOrientation="portrait"
            android:theme="@style/AppThemeFullscreen" />
```

- 视频播放（直接或间接引用JiaoZiVideoPlayer）

在AndroidManifest的manifest节点中添加权限

```java
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
```

在AndroidManifest的application节点中添加

```java
        <!-- 视频播放 -->
        <activity
            android:name="cn.jzvd.JZVideoPlayerActivity"
            android:configChanges="orientation|keyboardHidden|screenSize"
            android:theme="@style/AppThemeFullscreen" />
```

- 二维码扫描（直接或间接引用QrCodeScan）

在AndroidManifest的manifest节点中添加权限

```java
    <uses-permission android:name="android.permission.FLASHLIGHT" />
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
```

- 部分主题styles

```java
    <style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">
        <item name="colorPrimary">@color/colorPrimary</item>
        <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
        <item name="colorAccent">@color/colorAccent</item>
    </style>
    
    <style name="AppThemeSplash" parent="AppTheme">
        <item name="windowNoTitle">true</item>
        <item name="windowActionBar">false</item>
        <item name="android:windowFullscreen">true</item>
        <item name="android:windowContentOverlay">@null</item>
        <item name="android:windowIsTranslucent">false</item>
        <item name="android:windowDisablePreview">true</item>
    </style>

    <style name="AppThemeFullscreen" parent="AppTheme">
        <item name="windowNoTitle">true</item>
        <item name="windowActionBar">false</item>
        <item name="android:windowFullscreen">true</item>
        <item name="android:windowContentOverlay">@null</item>
    </style>

    <style name="AppThemeBlack" parent="Theme.AppCompat.Light.NoActionBar">
        <item name="colorPrimary">@color/colorPrimary</item>
        <item name="colorPrimaryDark">@color/colorBlack</item>
        <item name="colorAccent">@color/colorAccent</item>
    </style>
    
    <style name="AppThemeWhite" parent="Theme.AppCompat.Light.NoActionBar">
        <item name="colorPrimary">@color/colorPrimary</item>
        <item name="colorPrimaryDark">@color/colorWhite</item>
        <item name="colorAccent">@color/colorAccent</item>
    </style>
```

values-v19

```java
    <style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">
        <item name="android:windowTranslucentStatus">true</item>
        <item name="android:windowFullscreen">false</item>
        <item name="colorPrimary">@color/colorPrimary</item>
        <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
        <item name="colorAccent">@color/colorAccent</item>
    </style>
```

values-v21

```java
    <style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">
        <item name="android:statusBarColor">@android:color/transparent</item>
        <item name="android:windowTranslucentStatus">true</item>
        <item name="android:windowFullscreen">false</item>
        <item name="colorPrimary">@color/colorPrimary</item>
        <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
        <item name="colorAccent">@color/colorAccent</item>
    </style>
```
