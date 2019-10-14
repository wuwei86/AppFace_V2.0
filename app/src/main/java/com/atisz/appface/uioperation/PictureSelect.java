package com.atisz.appface.uioperation;

import android.app.Activity;


import com.atisz.appface.R;
import com.atisz.appface.config.Config;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;

import java.util.List;

/**
 * Created by LaoWu on 2018/6/27 0027.
 * https://github.com/LuckSiege/PictureSelector
 * 图片选择的工具类(对pictureSelector的调用接口进行封装)
 */

public class PictureSelect {

    private static String getCompressPhotoPath() {
        return null;
    }

    /**
     * 图片选择
     * @param activity
     * @param isMuilt   是否为多图选择（true多图false单图）
     * @param isCrop    是否裁切
     * @param isCompress    是否压缩
     * @param selectPicture 多图选择时已经选择的照片
     */
    public static void selectorImage(Activity activity, boolean isMuilt,
                                     boolean isCrop,
                                     boolean isCompress,
                                     List<LocalMedia> selectPicture) {
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
//                .theme()//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
//                .maxSelectNum(maxNum)// 最大图片选择数量 int
//                .minSelectNum(minNum)// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(isMuilt ? PictureConfig.MULTIPLE : PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .previewVideo(true)// 是否可预览视频 true or false
                .enablePreviewAudio(true) // 是否可播放音频 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
//                .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                .enableCrop(isCrop)// 是否裁剪 true or false
                .compress(isCompress)// 是否压缩 true or false
//                .glideOverride()// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1,1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示 true or false
                .isGif(false)// 是否显示gif图片 true or false
                .compressSavePath(activity.getApplicationContext().getFilesDir().getAbsolutePath() + Config.PICTURE_COMPRESS_ROUTE)//压缩图片保存地址
                .freeStyleCropEnabled(false)// 裁剪框是否可拖拽 true or false
                .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .openClickSound(false)// 是否开启点击声音 true or false
                .selectionMedia(selectPicture)// 是否传入已选图片 List<LocalMedia> list
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .cropCompressQuality(60)// 裁剪压缩质量 默认90 int
                .minimumCompressSize(200)// 小于100kb的图片不压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
//                .cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
                .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
//                .videoQuality()// 视频录制质量 0 or 1 int
                .videoMaxSecond(15)// 显示多少秒以内的视频or音频也可适用 int
                .videoMinSecond(10)// 显示多少秒以内的视频or音频也可适用 int
//                .recordVideoSecond()//视频秒数录制 默认60s int
                .isDragFrame(false)// 是否可拖动裁剪框(固定)
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    /**
     * 预览图片
     * @param activity
     * @param position      预览的图片的位置
     * @param selectList    预览的图片的数组
     */
    public static void previewPicture(Activity activity, int position, List<LocalMedia> selectList) {
        PictureSelector.create(activity).themeStyle(R.style.picture_default_style).openExternalPreview(position, selectList);
    }

    /**
     * 清除裁切和压缩之后的缓存
     * @param activity
     */
    public static void clearCache(Activity activity) {
        PictureFileUtils.deleteCacheDirFile(activity);
    }
}
