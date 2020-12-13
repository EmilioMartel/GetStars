package com.marquelo.getstars.working;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class PaintView extends View {
    //-------------- CONSTANTES --------------//
    public static int BRUSH_SIZE = 10;
    public static final int DEFAULT_COLOR = Color.BLACK;
    public static final int DEFAULT_BG_COLOR = Color.WHITE;
    private static final float TOUCH_TOLERANCE = 4;

    //------------------ ATRIBUTOS ------------------//
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private float mX, mY;
    private Path mPath;
    private Paint mPaint;
    private int currentColor;
    private int backgroundColor = DEFAULT_BG_COLOR;
    private int strokeWidth;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    private ArrayList<Draw> paths = new ArrayList<>();
    private ArrayList<Draw> undo = new ArrayList<>();
    private OutputStream outputStream;


    public PaintView(Context context) {
        super(context, null);
    }

    public PaintView(Context context, AttributeSet attrs) {

        super(context, attrs);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(DEFAULT_COLOR);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xff);

    }

    public void initialise (@NotNull DisplayMetrics displayMetrics) {

        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        currentColor = DEFAULT_COLOR;
        strokeWidth = BRUSH_SIZE;

    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.save();
        mCanvas.drawColor(backgroundColor); // WRONG

        for (Draw draw : paths) {

            mPaint.setColor(draw.color); // WRONG
            mPaint.setStrokeWidth(draw.strokeWidth);
            mPaint.setMaskFilter(null);

            mCanvas.drawPath(draw.path, mPaint);

        }

        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.restore();

    }

    private void touchStart (float x, float y) {

        mPath = new Path();

        Draw draw = new Draw(currentColor, strokeWidth, mPath);
        paths.add(draw);

        mPath.reset();
        mPath.moveTo(x, y);

        mX = x;
        mY = y;

    }

    private void touchMove (float x, float y) {

        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touchUp () {
        mPath.lineTo(mX, mY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                invalidate();
                break;

        }
        return true;
    }

    public void clear () {
        backgroundColor = DEFAULT_BG_COLOR;

        paths.clear();
        invalidate();
    }

    public void undo () {

        if (paths.size() > 0) {
            undo.add(paths.remove(paths.size() - 1));
            invalidate(); // add
        } else {
            Toast.makeText(getContext(), "Nada que deshacer", Toast.LENGTH_SHORT).show();
        }
    }

    public void redo () {

        if (undo.size() > 0) {
            paths.add(undo.remove(undo.size() - 1));
            invalidate(); // add
        } else {
            Toast.makeText(getContext(), "Nada que rehacer", Toast.LENGTH_SHORT).show();
        }
    }

    public void setStrokeWidth (int width) {
        strokeWidth = width;
    }

    public void setColor (int color) {
        currentColor = color;
    }


    public void saveImage () {
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

        String email = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();

        // Path
        String path = "usuarios/" + email +"/" + "AuthMan/";
        System.out.println(email);

        // Reference to manual autograph
        StorageReference imgRef = storageRef.child(path);
        CodigoRandom codigoRandom = new CodigoRandom();
        String codigo = codigoRandom.generarCodigoRandom(20);
        final StorageReference nameFirma = imgRef.child(codigo+".jpg");

        // Get the data from an ImageView as bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = nameFirma.putBytes(data);

        uploadTask.addOnFailureListener(exception -> {
            // Handle unsuccessful uploads
        }).addOnSuccessListener(taskSnapshot -> {
            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
            // Reference to an image file in Cloud Storage

        });
        //guardarImagenLocal();
        //redimensionarBitmap(mBitmap, 500, 500, nameFirma);
    }

    private void guardarImagenLocal(){

        int count = 0;
        File sdDirectory = Environment.getExternalStorageDirectory();
        File subDirectory = new File(sdDirectory.toString() + "/Pictures/GetStars");

        if (subDirectory.exists()) {

            File[] existing = subDirectory.listFiles();
            for (File file : existing) {
                if (file.getName().endsWith(".jpg") || file.getName().endsWith(".png")) {
                    count++;
                }
            }

        } else {
            subDirectory.mkdir();
        }

        if (subDirectory.exists()) {

            File image = new File(subDirectory, "/GetStars_" + count + ".png");
            FileOutputStream fileOutputStream;

            try {

                fileOutputStream = new FileOutputStream(image);

                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);

                fileOutputStream.flush();
                fileOutputStream.close();

                Toast.makeText(getContext(), "saved", Toast.LENGTH_LONG).show();

            } catch (FileNotFoundException e) {


            } catch (IOException e) {


            }

        }
    }

    private void redimensionarBitmap(Bitmap mBitmap, int newWidth, int newHeight, StorageReference nameRef) {
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();

        float scaleWidth = ((float)newWidth)/width;
        float scaleHeight = ((float)newHeight)/height;

        //creamos una matriz parda manipularla
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth,scaleHeight);

        //recreamos el bitmap
        Bitmap nuevoBitmap = Bitmap.createBitmap(mBitmap,0,0,width,height,matrix,false);

        // Get the data from an ImageView as bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        nuevoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = nameRef.putBytes(data);

        uploadTask.addOnFailureListener(exception -> {
            // Handle unsuccessful uploads
        }).addOnSuccessListener(taskSnapshot -> {
            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
            // Reference to an image file in Cloud Storage

        });
    }
}