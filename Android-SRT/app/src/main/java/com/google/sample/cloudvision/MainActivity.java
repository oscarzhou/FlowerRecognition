

package com.google.sample.cloudvision;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class MainActivity extends AppCompatActivity {
    private static final String CLOUD_VISION_API_KEY = "AIzaSyAy4tHJKuC2SQT1wYlXqBlxK5xmUonMZko";
    public static final String FILE_NAME = "temp.jpg";
    private static final String ANDROID_CERT_HEADER = "X-Android-Cert";
    private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int GALLERY_PERMISSIONS_REQUEST = 0;
    private static final int GALLERY_IMAGE_REQUEST = 1;
    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;
    public static final int DICTIONARY_PERMISSIONS_REQUEST = 4;
    public static final int DICTIONARY_IMAGE_REQUEST = 5;

    private TextView mImageDetails;
    private ImageView mMainImage;
    private ListView mListFlower;


    private String[] itemname ={
            "<a href='http://www.doc.govt.nz/nature/native-plants/beech-forest/' style='text-decoration:none;color:#0000FF'>Beech forest</a>",
            "<a href='http://www.doc.govt.nz/nature/native-plants/mistletoe/' style='text-decoration:none;color:#0000FF'>Mistletoe</a>",
            "<a href='http://www.doc.govt.nz/nature/native-plants/chatham-island-christmas-tree/' style='text-decoration:none'>Chatham Island Christmas tree</a>",
            "<a href='http://www.doc.govt.nz/nature/native-plants/chatham-islands-plants/' style='text-decoration:none'>Chatham Islands plants</a>",
            "<a href='http://www.doc.govt.nz/nature/native-plants/coastal-cress/' style='text-decoration:none'>Coastal cress</a>",
            "<a href='http://www.doc.govt.nz/nature/native-plants/dactylanthus/' style='text-decoration:none'>Dactylanthus</a>",
            "<a href='http://www.doc.govt.nz/nature/native-plants/ferns/' style='text-decoration:none'>New Zealand ferns</a>",
            "<a href='http://www.doc.govt.nz/nature/native-plants/harakeke-flax/' style='text-decoration:none'>Harakeke/flax</a>",
            "<a href='http://www.doc.govt.nz/nature/native-plants/kauri/' style='text-decoration:none'>Kauri</a>",
            "<a href='http://www.doc.govt.nz/nature/native-plants/kowhai/' style='text-decoration:none'>Kōwhai</a>",
            "<a href='http://www.doc.govt.nz/nature/native-plants/lancewood-horoeka/' style='text-decoration:none'>Lancewood/horoeka</a>",
            "<a href='http://www.doc.govt.nz/nature/native-plants/manuka-kahikatoa-and-kanuka/' style='text-decoration:none'>Mānuka/kāhikatoa and kānuka</a>"
    };


    private Integer[] imgid={
            R.drawable.pic1,
            R.drawable.pic2,
            R.drawable.pic3,
            R.drawable.pic4,
            R.drawable.pic5,
            R.drawable.pic6,
            R.drawable.pic7,
            R.drawable.pic8,
            R.drawable.pic9,
            R.drawable.pic10,
            R.drawable.pic11,
            R.drawable.pic12,
    };

    String[] filterWords = {"flower", "plant", "yellow", "read", "white", "flora", "land plant", "flowering plant", "daisy family", "petal", "field", "plant stem", "macro photography", "purple", "pink", "photography", "close up", "blossom", "black and white", "green", "annual plant", "botany", "floristry", "biology", "leaf", "branch", "tree", "shrub", "nature", "wildflower"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchLayout(R.layout.content_main);

                mImageDetails = (TextView) findViewById(R.id.image_details);
                mMainImage = (ImageView) findViewById(R.id.main_image);
                //mMainImage.setOnTouchListener(new MulitPointTouchListener ());
                startCamera();
            }
        });


        FloatingActionButton album = (FloatingActionButton) findViewById(R.id.album);
        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchLayout(R.layout.content_main);

                mImageDetails = (TextView) findViewById(R.id.image_details);
                mMainImage = (ImageView) findViewById(R.id.main_image);
                mMainImage.setOnTouchListener(new MulitPointTouchListener ());
                startGalleryChooser();
            }
        });

        FloatingActionButton dict = (FloatingActionButton) findViewById(R.id.dictionary);
        dict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switchLayout(R.layout.dictionary_main);
                startDictionary();
                /*
                // Do the real work in an async task, because we need to use the network anyway
                new AsyncTask<Object, Void, String>() {
                    @Override
                    protected String doInBackground(Object... params) {
                        startDictionary();
                        return "";
                    }
                }.execute();
                */
            }
        });
    }

    public void startGalleryChooser() {
        if (PermissionUtils.requestPermission(this, GALLERY_PERMISSIONS_REQUEST, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select a photo"),
                    GALLERY_IMAGE_REQUEST);
        }
    }

    public void startCamera() {
        if (PermissionUtils.requestPermission(
                this,
                CAMERA_PERMISSIONS_REQUEST,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA)) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, CAMERA_IMAGE_REQUEST);
        }
    }

    public void startDictionary(){
        if (PermissionUtils.requestPermission(this, DICTIONARY_PERMISSIONS_REQUEST, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            mListFlower=(ListView)findViewById(R.id.itemlist);
            CustomListAdapter adapter=new CustomListAdapter(this, itemname, imgid);
            mListFlower.setAdapter(adapter);
            mListFlower.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    /*
                    // TODO Auto-generated method stub
                    String Slecteditem= itemname[+position];
                    Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();
                    */
                }
            });

        }
    }

    public File getCameraFile() {
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(dir, FILE_NAME);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            uploadImage(data.getData());
        } else if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
            uploadImage(photoUri);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, CAMERA_PERMISSIONS_REQUEST, grantResults)) {
                    startCamera();
                }
                break;
            case GALLERY_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, GALLERY_PERMISSIONS_REQUEST, grantResults)) {
                    startGalleryChooser();
                }
                break;
            case DICTIONARY_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, DICTIONARY_PERMISSIONS_REQUEST, grantResults)) {
                    startDictionary();
                }
                break;
        }
    }

    public void uploadImage(Uri uri) {
        if (uri != null) {
            try {
                // scale the image to save on bandwidth
                Bitmap bitmap =
                        scaleBitmapDown(
                                MediaStore.Images.Media.getBitmap(getContentResolver(), uri),
                                1200);

                callCloudVision(bitmap);
                mMainImage.setImageBitmap(bitmap);
                mMainImage.setOnTouchListener(new MulitPointTouchListener ());

            } catch (IOException e) {
                Log.d(TAG, "Image picking failed because " + e.getMessage());
                Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d(TAG, "Image picker gave us a null image.");
            Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
        }
    }

    private void callCloudVision(final Bitmap bitmap) throws IOException {
        // Switch text to loading
        mImageDetails.setText(R.string.loading_message);

        // Do the real work in an async task, because we need to use the network anyway
        new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... params) {
                try {
                    HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
                    JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

                    VisionRequestInitializer requestInitializer =
                            new VisionRequestInitializer(CLOUD_VISION_API_KEY) {
                                /**
                                 * We override this so we can inject important identifying fields into the HTTP
                                 * headers. This enables use of a restricted cloud platform API key.
                                 */
                                @Override
                                protected void initializeVisionRequest(VisionRequest<?> visionRequest)
                                        throws IOException {
                                    super.initializeVisionRequest(visionRequest);

                                    String packageName = getPackageName();
                                    visionRequest.getRequestHeaders().set(ANDROID_PACKAGE_HEADER, packageName);

                                    String sig = PackageManagerUtils.getSignature(getPackageManager(), packageName);

                                    visionRequest.getRequestHeaders().set(ANDROID_CERT_HEADER, sig);
                                }
                            };

                    Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
                    builder.setVisionRequestInitializer(requestInitializer);

                    Vision vision = builder.build();

                    BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                            new BatchAnnotateImagesRequest();
                    batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
                        AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();

                        // Add the image
                        Image base64EncodedImage = new Image();
                        // Convert the bitmap to a JPEG
                        // Just in case it's a format that Android understands but Cloud Vision
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                        byte[] imageBytes = byteArrayOutputStream.toByteArray();

                        // Base64 encode the JPEG
                        base64EncodedImage.encodeContent(imageBytes);
                        annotateImageRequest.setImage(base64EncodedImage);

                        // add the features we want
                        annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
                            Feature labelDetection = new Feature();
                            labelDetection.setType("LABEL_DETECTION");
                            labelDetection.setMaxResults(15);
                            add(labelDetection);
                        }});

                        // Add the list of one thing to the request
                        add(annotateImageRequest);
                    }});

                    Vision.Images.Annotate annotateRequest =
                            vision.images().annotate(batchAnnotateImagesRequest);
                    // Due to a bug: requests to Vision API containing large images fail when GZipped.
                    annotateRequest.setDisableGZipContent(true);
                    Log.d(TAG, "created Cloud Vision request object, sending request");

                    BatchAnnotateImagesResponse response = annotateRequest.execute();
                    return convertResponseToString(response);

                } catch (GoogleJsonResponseException e) {
                    Log.d(TAG, "failed to make API request because " + e.getContent());
                } catch (IOException e) {
                    Log.d(TAG, "failed to make API request because of other IOException " +
                            e.getMessage());
                }
                return "Cloud Vision API request failed. Check logs for details.";
            }

            protected void onPostExecute(String result) {
                mImageDetails.setText(result);
            }
        }.execute();
    }

    public Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }

    private String convertResponseToString(BatchAnnotateImagesResponse response) {
        int counter = 0;
        List<String> filterWordList = Arrays.asList(filterWords);

        String message = "It should be:\n\n";
        List<EntityAnnotation> labels = response.getResponses().get(0).getLabelAnnotations();
        if (labels != null) {
            for (EntityAnnotation label : labels) {
                String parsedWord = label.getDescription();
                //int setIndex = Collections.binarySearch(filterWordList, parsedWord);
                boolean bPass = filterWordList.contains(parsedWord);
                //Log.d("SRT", "setIndex =" + setIndex+", parsedWord = "+ parsedWord);
                if(bPass) continue;
                if(counter == 5) break;
                counter++;
                message += String.format(Locale.US, "%f%%: %s", label.getScore()*100, parsedWord);
                message += "\n";
            }
        } else {
            message += "nothing";
        }

        if (counter == 0){
            if (labels != null) {
                for (EntityAnnotation label : labels) {
                    message += String.format(Locale.US, "%f%%: %s", label.getScore()*100, label.getDescription());
                    message += "\n";
                }
            } else {
                message += "nothing";
            }
        }

        return message;
    }

    /* switch the content in include layout */
    private void switchLayout(int replacedLayout){

        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.content_main);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(replacedLayout, null);
        mainLayout.removeAllViews();
        mainLayout.addView(layout);

    }
}
