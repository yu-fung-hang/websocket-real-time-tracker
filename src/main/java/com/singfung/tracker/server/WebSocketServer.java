package com.singfung.tracker.server;

import com.singfung.tracker.model.GPS;
import com.singfung.tracker.service.GPSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@ServerEndpoint(value = "/socket/{vehicleId}")
public class WebSocketServer
{
    @Autowired
    GPSService gpsService;

    private static Map<String, Session> sessionPools = new HashMap<>();

    public void sendMessage(Session session, String message) throws IOException
    {
        if(session != null)
        { session.getBasicRemote().sendText(message); }
    }

    @SendTo("/gps")
    public void trackGPS(String vehicleId, Session session)
    {
        GPS gps = gpsService.getGPSByVehicleId(vehicleId);

        if (gps != null)
        {
            double lat = gps.getLat();
            double lng = gps.getLng();
            String message = "{\"lat\": " + String.format("%.2f", lat) + ", \"lng\": " + String.format("%.2f", lng) + "}";

//            Session session = getSession(this.getId());
            if (session != null)
            {
                try {
                    sendMessage(session, message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void updateGPS(String vehicleId)
    {
        GPS gps = gpsService.getGPSByVehicleId(vehicleId);

        if (gps != null)
        {
            double lat = gps.getLat() + 0.01;
            double lng = gps.getLng() + 0.01;
            gps.setLat(lat);
            gps.setLng(lng);

            gpsService.saveGPS(gps);
        }
    }

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "vehicleId") String vehicleId)
    { sessionPools.put(vehicleId, session); }

    @OnClose
    public void onClose(@PathParam(value = "vehicleId") String vehicleId)
    { sessionPools.remove(vehicleId); }

    //group message
    @OnMessage
    public void onMessage(String message) throws IOException
    {
        for (Session session: sessionPools.values())
        {
            try {
                sendMessage(session, message);
            } catch(Exception e){
                e.printStackTrace();
                continue;
            }
        }
    }

    @Scheduled(cron = "*/2 * * * * ?")
    public void scheduledTask()
    {
        for (String key: sessionPools.keySet())
        {
            Session session = sessionPools.get(key);
            try {
                updateGPS(key);
                trackGPS(key, session);
            } catch(Exception e){
                e.printStackTrace();
                continue;
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable)
    {
        System.out.println("An error occurs");
        throwable.printStackTrace();
    }
}