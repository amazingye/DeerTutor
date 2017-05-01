
package com.ye.deertutor.Activities.accountAcvtivitys;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ye.deertutor.R;
import com.ye.deertutor.models.DeerUser;
import com.ye.deertutor.models.Teacher;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

public class VerifyActivity extends Activity {
    public String realName;
    public String idCardNumber;
    public String teacherSex;
    public List<String> IDPics;

    EditText realNameEdit;
    EditText idCardNumberEdit;
    EditText teacherSexEdit;
    Button chooseIdPic;
    Button submitVerifyButton;

    File file;
    public BmobFile IDPic;

    public int REQUEST_CODE_GALLERY = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        realNameEdit = (EditText)findViewById(R.id.teacherrealnameedit);
        idCardNumberEdit = (EditText)findViewById(R.id.idnumberedit);
        teacherSexEdit = (EditText)findViewById(R.id.teachersexedit);

        chooseIdPic = (Button)findViewById(R.id.iduploadbutton);
        chooseIdPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FunctionConfig FCconfig = new FunctionConfig.Builder()
                        .setMutiSelectMaxSize(2)
                        .build();
                GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY,FCconfig,mCallback);
            }
        });
        submitVerifyButton = (Button)findViewById(R.id.submitverify);
        submitVerifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitVerify();
            }
        });
    }



    public void submitVerify(){
        realName = realNameEdit.getText().toString();
        idCardNumber = idCardNumberEdit.getText().toString();
        teacherSex = teacherSexEdit.getText().toString();

        DeerUser currentUser = BmobUser.getCurrentUser(this,DeerUser.class);
        BmobQuery<Teacher> query = new BmobQuery<>();
        query.addWhereEqualTo("userId",currentUser);
        query.findObjects(VerifyActivity.this, new FindListener<Teacher>() {
            @Override
            public void onSuccess(List<Teacher> list) {
                for(Teacher teacher : list){
                    teacher.setRealName(realName);
                    teacher.setSex(teacherSex);
                    teacher.setVerifyStatus("待审核");
                    teacher.update(VerifyActivity.this, new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            Log.i("submitverify","提交审核成功");
                            finish();
                        }

                        @Override
                        public void onFailure(int i, String s) {

                        }
                    });
                    /*teacher.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e == null){
                                Log.i("submitverify","提交审核成功");
                                finish();
                            }else {
                                e.printStackTrace();
                            }
                        }
                    });*/
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.i("submitVerify",s);
            }
        });
        /*query.findObjects(new FindListener<Teacher>() {
            @Override
            public void done(List<Teacher> list, BmobException e) {
                if(e == null){
                    for(Teacher teacher : list){
                        teacher.setRealName(realName);
                        teacher.setSex(teacherSex);
                        teacher.setVerifyStatus("待审核");
                        teacher.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e == null){
                                    Log.i("submitverify","提交审核成功");
                                    finish();
                                }else {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }else {
                    Log.i("submitVerify",e.getMessage());
                }
            }
        });*/
    }



    public GalleryFinal.OnHanlderResultCallback mCallback =
            new GalleryFinal.OnHanlderResultCallback() {
                @Override
                public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                    String[] paths = new String[resultList.size()];
                    int i = 0;
                    for(PhotoInfo photoInfo : resultList){
                        getImage(photoInfo.getPhotoPath());
                        paths[i++] = getPath(VerifyActivity.this,Uri.fromFile(file));
                        //paths[i++] = Uri.fromFile(file).toString();
                        //paths[i++] = photoInfo.getPhotoPath();
                    }
                    uploadIDAction(paths);
                }

                @Override
                public void onHanlderFailure(int requestCode, String errorMsg) {
                    Log.i("onHanlderFailure",errorMsg);
                }
            };


    public void uploadIDAction(String[] paths){
        BmobFile.uploadBatch(this, paths, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> list, List<String> list1) {
                for(String url : list1){
                    Log.i("IDUrls",url);
                }
                IDPics = list1;
            }

            @Override
            public void onProgress(int i, int i1, int i2, int i3) {

            }

            @Override
            public void onError(int i, String s) {

            }
        });

        /*BmobFile.uploadBatch(paths, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> list, List<String> list1) {
                for(String url : list1){
                    Log.i("IDUrls",url);
                }
                IDPics = list1;
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total,int totalPercent) {
                Log.i("Progress",curIndex+" "+curPercent+" "+total+" "+totalPercent);
            }

            @Override
            public void onError(int i, String s) {
                Log.d("upLoadBatchError",i+""+s);
            }
        });*/

    }







    private void getImage(String srcPath) {

        //以下程序段为根据选择的图片路径进行压缩前比例设置
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        //compressImage(bitmap);  //压缩好比例大小后再进行质量压缩



        //以下为bitmap压缩算法
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024>100) {
            //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
            //这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        //把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap compressedBitmap = BitmapFactory.decodeStream(isBm, null, null);
        //把ByteArrayInputStream数据生成图片
        //saveImage(compressedBitmap);


        //将压缩后的bitmap存储到根目录
        file = new File(Environment.getExternalStorageDirectory(),
                "IDPic"+(int)(Math.random()*100)+".jpeg");

        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        }catch (IOException e) {
            e.printStackTrace();
        }


    }



    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
