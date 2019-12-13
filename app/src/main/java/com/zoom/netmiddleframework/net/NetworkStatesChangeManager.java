package com.zoom.netmiddleframework.net;

import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;

public class NetworkStatesChangeManager extends ConnectivityManager.NetworkCallback {
    @Override
    public void onAvailable(Network network) {
        super.onAvailable(network);
        HttpRequestManager.getInstance().mNetConnectStatusChanged(true);
    }

    @Override
    public void onLost(Network network) {
        super.onLost(network);
        HttpRequestManager.getInstance().mNetConnectStatusChanged(false);
    }

    @Override
    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);
    }
}
