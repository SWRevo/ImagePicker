package id.indosw.imagepicker.sample.imageloader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;

import id.indosw.imagepicker.loader.ImageLoader;
import id.indosw.imagepicker.sample.R;

public class XUtils3ImageLoader implements ImageLoader {
    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        ImageOptions options = new ImageOptions.Builder()//
                .setLoadingDrawableId(R.drawable.ic_default_image)//
                .setFailureDrawableId(R.drawable.ic_default_image)//
                .setConfig(Bitmap.Config.RGB_565)//
                .setSize(width, height)//
                .setCrop(false)//
                .setUseMemCache(true)//
                .build();
        x.image().bind(imageView, Uri.fromFile(new File(path)).toString(), options);
    }

    @Override
    public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {
        ImageOptions options = new ImageOptions.Builder()//
                .setConfig(Bitmap.Config.RGB_565)//
                .setSize(width, height)//
                .setCrop(false)//
                .setUseMemCache(true)//
                .build();
        x.image().bind(imageView, Uri.fromFile(new File(path)).toString(), options);
    }

    @Override
    public void clearMemoryCache() {
    }
}
