package com.example.carpc.widgets.dashboardScreen;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.carpc.MainActivity;
import com.example.carpc.R;
import com.example.carpc.instruments.ClientSocket;
import com.example.carpc.instruments.DataBase;
import com.example.carpc.instruments.DataParser;
import com.example.carpc.widgets.dashboardScreen.tabs.BatteryManagerWidget;
import com.example.carpc.widgets.dashboardScreen.tabs.BatteryWidget;
import com.example.carpc.widgets.dashboardScreen.tabs.IconStatusLeftWidget;
import com.example.carpc.widgets.dashboardScreen.tabs.IconStatusRightWidget;
import com.example.carpc.widgets.dashboardScreen.tabs.SpeedometerWidget;
import com.example.carpc.widgets.dashboardScreen.tabs.TripManagerWidget;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class DashboardWidget extends Fragment {
    private SpeedometerWidget speedometerWidget;
    private BatteryWidget batteryWidget;
    private BatteryManagerWidget batteryManagerWidget;
    private TripManagerWidget tripManagerWidget;
    private IconStatusRightWidget iconStatusRightWidget;
    private IconStatusLeftWidget iconStatusLeftWidget;
    private androidx.fragment.app.FragmentTransaction fragmentTransaction;
    private ClientSocket socket;
    private DataBase dataBase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dashboard_widget, container, false);
        speedometerWidget = new SpeedometerWidget();
        batteryManagerWidget = new BatteryManagerWidget();
        tripManagerWidget = new TripManagerWidget();
        iconStatusRightWidget = new IconStatusRightWidget();
        iconStatusLeftWidget = new IconStatusLeftWidget();
        batteryWidget = new BatteryWidget();
        dataBase = MainActivity.getDataBase();
        fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.replace(R.id.dashboardCenterCont, speedometerWidget);
        fragmentTransaction.replace(R.id.dashboardRightUpCont, iconStatusRightWidget);
        fragmentTransaction.replace(R.id.dashboardLeftUpCont, iconStatusLeftWidget);
        fragmentTransaction.replace(R.id.dashboardBottomLeftCont, batteryWidget);
        fragmentTransaction.setCustomAnimations(R.animator.left_in, R.animator.right_in).replace(R.id.dashboardLeftCont, batteryManagerWidget);
        fragmentTransaction.setCustomAnimations(R.animator.left_out, R.animator.right_out).replace(R.id.dashboardRightCont, tripManagerWidget);
        fragmentTransaction.commit();
        socket = new ClientSocket(dataBase.getIP(), dataBase.getPort(), MainActivity.getParser(), true);
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        socket.disconnect();
    }
}