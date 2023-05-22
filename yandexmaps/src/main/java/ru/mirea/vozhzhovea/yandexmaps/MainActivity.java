package ru.mirea.vozhzhovea.yandexmaps;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CompositeIcon;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.RotationType;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.image.ImageProvider;

import ru.mirea.vozhzhovea.yandexmaps.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements UserLocationObjectListener {
    private ActivityMainBinding binding;
    private boolean isWork = false;
    private static final int REQUEST_CODE_PERMISSION = 100;
    private UserLocationLayer userLocationLayer;
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int loc1PermissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int loc2PermissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int loc3PermissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION);

        MapKitFactory.initialize(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        //Log.d("Sl,dal", String.valueOf(isWork));
        setContentView(binding.getRoot());
        if (loc1PermissionStatus == PackageManager.PERMISSION_GRANTED && loc2PermissionStatus
                == PackageManager.PERMISSION_GRANTED) {
            isWork = true;
            Log.d("HHHHHHHHHHHHH","isWork");

        }
        else {
            Log.d("HHHHHHHHHHHHH","AAAAAAAAA");
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
//                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION}, REQUEST_CODE_PERMISSION);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        }
//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
//                Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION);
        mapView = binding.mapview;

        mapView.getMap().move(
                new CameraPosition(new Point(55.751574, 37.573856), 11.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);
        if (isWork) {
            loadUserLocationLayer();
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        // Вызов onStop нужно передавать инстансам MapView и MapKit.
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
    }

    @Override
    protected void onStart() {
        // Вызов onStart нужно передавать инстансам MapView и MapKit.
        super.onStart();
        mapView.onStart();
        MapKitFactory.getInstance().onStart();
    }



    @Override
    public void onObjectRemoved(@NonNull UserLocationView userLocationView) {
    }

    @Override
    public void onObjectUpdated(@NonNull UserLocationView userLocationView, @NonNull ObjectEvent objectEvent) {

    }

    private void loadUserLocationLayer() {
        MapKit mapKit = MapKitFactory.getInstance();
        mapKit.resetLocationManagerToDefault();
        userLocationLayer = mapKit.createUserLocationLayer(mapView.getMapWindow());
        userLocationLayer.setVisible(true);
        userLocationLayer.setHeadingEnabled(true);
        userLocationLayer.setObjectListener(this);
    }

    @Override
    public void onObjectAdded(UserLocationView userLocationView) {
        Log.w("DDDD", "DDD");
        userLocationLayer.setAnchor(
                new PointF((float) (mapView.getWidth() * 0.5),
                        (float) (mapView.getHeight() * 0.5)),
                new PointF((float) (mapView.getWidth() * 0.5),
                        (float) (mapView.getHeight() * 0.83)));
        // При определении направления движения устанавливается следующая иконка
        userLocationView.getArrow().setIcon(ImageProvider.fromResource(this, R.drawable.ic_loc));
        // При получении координат местоположения устанавливается следующая иконка
        CompositeIcon pinIcon = userLocationView.getPin().useCompositeIcon();
        pinIcon.setIcon(
                "pin",
                ImageProvider.fromResource(this, R.drawable.ic_loc), new IconStyle().setAnchor(new PointF(0.5f, 0.5f))
                        .setRotationType(RotationType.ROTATE).setZIndex(1f)
                        .setScale(0.5f)
        );
        userLocationView.getAccuracyCircle().setFillColor(Color.BLUE & 0x99ffffff);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            // permission granted
            isWork = grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
    }
}