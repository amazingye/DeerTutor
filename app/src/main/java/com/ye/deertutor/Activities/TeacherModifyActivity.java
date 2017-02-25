package com.ye.deertutor.Activities;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

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
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

public class TeacherModifyActivity extends Activity {
    public EditText teacherUsernameEdit;
    public Button teacherHeadiconEdit;
    public EditText availableGradeEdit;
    public EditText availableSubjectEdit;
    public EditText teacherDescribeEdit;
    public EditText priceEdit;

    public File file;

    public String teacherUsername;
    public String availableGrade;
    public String availableSubject;
    public String teacherDescribe;
    public String price;
    public BmobFile teacherHeadicon;

    public Button teacherModifySaveButton;

    public int REQUEST_CODE_GALLERY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_modify);

        teacherUsernameEdit = (EditText)findViewById(R.id.teacherusernameedit);
        availableGradeEdit = (EditText)findViewById(R.id.availablegradeedit);
        availableSubjectEdit = (EditText)findViewById(R.id.availablesubjectedit);
        teacherDescribeEdit = (EditText)findViewById(R.id.teacherdescribeedit);
        priceEdit = (EditText)findViewById(R.id.priceedit);
        teacherHeadiconEdit = (Button)findViewById(R.id.teacherheadiconedit);
        teacherModifySaveButton = (Button)findViewById(R.id.teachermodifysave);

        teacherHeadiconEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(Intent.ACTION_GET_CONTENT);  //打开媒体库，挑选图片
                intent.setType("image*//*");
                startActivityForResult(intent,1);*/

                FunctionConfig FCconfig = new FunctionConfig.Builder()
                        .setMutiSelectMaxSize(1)
                        .build();
                GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY,FCconfig,mCallback);
            }
        });
        teacherModifySaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveModify();
            }
        });

    }


    public void saveModify(){
        teacherUsername = teacherUsernameEdit.getText().toString();
        availableGrade = availableGradeEdit.getText().toString();
        availableSubject = availableSubjectEdit.getText().toString();
        teacherDescribe = teacherDescribeEdit.getText().toString();
        price = priceEdit.getText().toString();

        DeerUser currentUser = BmobUser.getCurrentUser(DeerUser.class);
        currentUser.setUsername(teacherUsername);
        currentUser.setHeadIcon(teacherHeadicon);
        currentUser.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    Log.i("bmob","用户资料更新成功");
                }else {
                    e.printStackTrace();
                }
            }
        });
        BmobQuery<Teacher> query = new BmobQuery<>();
        query.addWhereEqualTo("userId",currentUser);
        query.findObjects(new FindListener<Teacher>() {
            @Override
            public void done(List<Teacher> list, BmobException e) {
                if(e == null){
                    for(Teacher teacher2 : list){
                        updateTeacher(teacher2);
                    }
                }else {
                    Log.i("bmob",e.getMessage()+""+e.getErrorCode());
                }
            }
        });

    }

    public void updateTeacher(Teacher teacher){
        teacher.setAvailableGrade(availableGrade);
        teacher.setAvailableSubject(availableSubject);
        teacher.setTeacherDescribe(teacherDescribe);
        teacher.setPrice(price);
        teacher.update(teacher.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Log.i("bmob","更新成功");
                    Toast.makeText(TeacherModifyActivity.this,
                            "修改资料成功",Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    Log.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }




    public void upLoadHeadicon(String path){

        File iconFile = new File(path);
        teacherHeadicon = new BmobFile(iconFile);
        teacherHeadicon.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Log.i("bmob","上传文件成功");
                }else{
                    Log.i("bmob","上传文件失败:" + e.getMessage());
                }
            }

            @Override
            public void onProgress(Integer value) {
                Log.i("progress",value.toString());
            }
        });
    }



    public GalleryFinal.OnHanlderResultCallback mCallback =
            new GalleryFinal.OnHanlderResultCallback() {
                @Override
                public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                    String path = null;
                    for(PhotoInfo photoInfo : resultList){
                        getImage(photoInfo.getPhotoPath());
                        path = getPath(TeacherModifyActivity.this,Uri.fromFile(file));
                    }
                    upLoadHeadicon(path);
                }

                @Override
                public void onHanlderFailure(int requestCode, String errorMsg) {
                    Log.i("onHanlderFailure",errorMsg);
                }
            };




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
        file = new File(Environment.getExternalStorageDirectory(),"headicon.jpeg");

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


    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @author paulburke
     */
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
